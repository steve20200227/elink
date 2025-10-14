package cn.snowsoft.iot.module.cps.seriesDatabase.influxdb;

import cn.snowsoft.iot.module.cps.controller.admin.monitoring.vo.SeriesSearchParamVO;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.SeriesDatasource;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.model.BaseSeriesTable;
import cn.snowsoft.iot.module.cps.seriesDatabase.influxdb.build.FilterBuilder;
import cn.snowsoft.iot.module.cps.seriesDatabase.influxdb.build.FluxSqlBuildUtils;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
@ConditionalOnProperty(name = "seriesType",havingValue = "influxdb")
public class InfluxdbDatasource implements SeriesDatasource {
    private InfluxDBClient influxDBClient;
    @Value("${influxdb.token}")
    private String token;
    @Value("${influxdb.org}")
    private String organization;
    @Value("${influxdb.bucket}")
    private String bucket;
    @Value("${influxdb.url}")
    private String url;

    @PostConstruct
    public void init(){
        try {
            influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray(), organization, bucket);
            log.info("初始化influxdb客户端成功");
        } catch (Exception e) {
            log.error("初始化influxdb客户端失败");
            e.printStackTrace();
        }
    }
    @Override
    public String getType() {
        return "influxdb";
    }

    @Override
    public InfluxDBClient getConnection() {
        return influxDBClient;
    }

    @Override
    public Boolean createTable(BaseSeriesTable baseSeriesTable) {
        log.info("设备数据持久化influxdb建表完成 {}", baseSeriesTable.getTableName());
        //influxdb中无需提前建表，所以此处直接返回true
        return true;
    }

    @Override
    public Map<String, Object> queryHistory(SeriesSearchParamVO seriesSearchParamVO) {
        Map<String, Object> result = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String startTime = simpleDateFormat.format(seriesSearchParamVO.getBeginDate());
        String endTime = simpleDateFormat.format(seriesSearchParamVO.getEndDate());
        log.info("历史数据查询条件 开始时间："+startTime+" 结束时间："+endTime);

        FluxSqlBuildUtils fluxSqlBuildUtils = new FluxSqlBuildUtils();
        //构建查询
        FluxSqlBuildUtils measurement = fluxSqlBuildUtils.from(bucket)
                .range(startTime, endTime)
                .measurement(seriesSearchParamVO.getDeviceCode());
        FilterBuilder filterBuilder =  new FilterBuilder();
        for (String columnName : seriesSearchParamVO.getPointCodeList()) {
            filterBuilder.eq("_field", columnName);

        }
        String s = measurement
                .filter(filterBuilder)
                .pivot(Arrays.asList("_time"),Arrays.asList("_field"),Arrays.asList("_value"))
                .getFluxSql()
                .append("|>map(fn: (r) => ({ r with thetime: r._time }))")
                .toString();
        log.info("历史数据查询sql {}", s);
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> query = queryApi.query(s);

        List<Object> xData = new ArrayList();
        List<Object> yData = new ArrayList();
        for (FluxTable fluxTable : query) {
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord record : records) {
                Map<String, Object> values = record.getValues();
                xData.add(values.get("thetime"));
                yData.add(values.get(seriesSearchParamVO.getPointCodeList().get(0)));
            }
        }

        result.put("xdata", xData);
        result.put("ydata", yData);
        return result;
    }

    @Override
    public Map<String, Object> selectDataHistoryStatistic(SeriesSearchParamVO seriesSearchParamVO) {
        String pointCode = seriesSearchParamVO.getPointCodeList().get(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String startTime = simpleDateFormat.format(seriesSearchParamVO.getBeginDate());
        String endTime = simpleDateFormat.format(seriesSearchParamVO.getEndDate());
        log.info("开始时间："+startTime+" 结束时间："+endTime);

        //先给默认值
        Map<String, Object> result = new HashMap();
        result.put("currentValue",result.getOrDefault("firstValue",0));
        result.put("avgValue", result.getOrDefault("avgValue",0));
        result.put("maxValue", result.getOrDefault("maxValue",0));
        result.put("minValue",result.getOrDefault("minValue",0));
        result.put("maxDiff", result.getOrDefault("maxDiff",0));


        //构建 first
        FluxSqlBuildUtils tableBuild = new FluxSqlBuildUtils();
        FilterBuilder filterBuilder =  new FilterBuilder();
        filterBuilder.eq("_field", pointCode);
        tableBuild.from(bucket)
                .range(startTime, endTime)
                .measurement(seriesSearchParamVO.getDeviceCode())
                .filter(filterBuilder)
                .script("|> first()")
                .script("|> map(fn: (r) => ({ r with firstValue: r._value }))")
                .keep(Arrays.asList("firstValue"))
                .yield("first");

        //avg

        tableBuild.from(bucket)
                .range(startTime, endTime)
                .measurement(seriesSearchParamVO.getDeviceCode())
                .filter(filterBuilder)
                .script("|> mean()")
                .script("|> map(fn: (r) => ({ r with avgValue: r._value }))")
                .keep(Arrays.asList("avgValue"))
                .yield("avg");

        //max

        tableBuild.from(bucket)
                .range(startTime, endTime)
                .measurement(seriesSearchParamVO.getDeviceCode())
                .filter(filterBuilder)
                .script("|> max()")
                .script("|> map(fn: (r) => ({ r with maxValue: r._value }))")
                .keep(Arrays.asList("maxValue"))
                .yield("max");

        //min

        tableBuild.from(bucket)
                .range(startTime, endTime)
                .measurement(seriesSearchParamVO.getDeviceCode())
                .filter(filterBuilder)
                .script("|> min()")
                .script("|> map(fn: (r) => ({ r with minValue: r._value }))")
                .keep(Arrays.asList("minValue"))
                .yield("min");


        String s = tableBuild.getFluxSql().toString();
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> query = queryApi.query(s);
        for (FluxTable fluxTable : query) {
            for (FluxRecord record : fluxTable.getRecords()) {
                if (ObjectUtils.isNotEmpty(record.getValueByKey("firstValue"))){
                    result.put("currentValue",record.getValueByKey("firstValue"));
                }

                if (ObjectUtils.isNotEmpty(record.getValueByKey("avgValue"))){
                    result.put("avgValue",record.getValueByKey("avgValue"));
                }
                if (ObjectUtils.isNotEmpty(record.getValueByKey("maxValue"))){
                    result.put("maxValue",record.getValueByKey("maxValue"));
                }
                if (ObjectUtils.isNotEmpty(record.getValueByKey("minValue"))){
                    result.put("minValue",record.getValueByKey("minValue"));
                }
            }

        }
        BigDecimal maxValue = new BigDecimal(result.get("maxValue").toString());
        BigDecimal minValue = new BigDecimal(result.get("minValue").toString());
        BigDecimal subtract = maxValue.subtract(minValue).setScale(2, BigDecimal.ROUND_HALF_UP);
        result.put("maxDiff",subtract);

        return result;
    }


}
