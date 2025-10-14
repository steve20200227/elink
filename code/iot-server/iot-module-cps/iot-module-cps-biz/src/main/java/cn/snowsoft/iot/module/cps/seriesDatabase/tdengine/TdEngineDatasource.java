package cn.snowsoft.iot.module.cps.seriesDatabase.tdengine;

import cn.snowsoft.iot.module.cps.controller.admin.monitoring.vo.SearchParamVO;
import cn.snowsoft.iot.module.cps.controller.admin.monitoring.vo.SeriesSearchParamVO;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.SeriesDatasource;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.model.BaseSeriesTable;
import cn.snowsoft.iot.module.cps.seriesDatabase.tdengine.build.TdEngineSqlBuild;
import cn.snowsoft.iot.module.cps.seriesDatabase.tdengine.model.TdEngineTable;
import com.alibaba.fastjson.JSONObject;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.taosdata.jdbc.TSDBDriver;
import com.xxdb.data.BasicTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
@ConditionalOnProperty(name = "seriesType",havingValue = "tdengine")
public class TdEngineDatasource implements SeriesDatasource {
    private Connection connection;
    @Value("${tdengine.url}")
    private String url;
    @Override
    public String getType() {
        return "tdengine";
    }

    @PostConstruct
    public void init(){
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
        try {
            connection = DriverManager.getConnection(url, connProps);
            log.info("初始化TDengine连接成功");
        } catch (Exception ex) {
            log.error("初始化TDengine连接失败");
            ex.printStackTrace();

        }

    }
    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public Boolean createTable(BaseSeriesTable baseSeriesTable) {

        TdEngineTable tdEngineTable = new TdEngineTable();
        BeanUtils.copyProperties(baseSeriesTable, tdEngineTable);
        Statement statement = null;
        try {
            String createSql = tdEngineTable.createTableSql();
            String createChildSql = tdEngineTable.createChildTableSql();
            statement = connection.createStatement();
            statement.execute(createSql);
            statement.execute(createChildSql);
            log.info("TdEngine建超级表成功：sql -> {}" ,createSql);
            log.info("TdEngine建子表成功：sql -> {}" ,createChildSql);
        } catch (Exception e) {
            log.error("TdEngine建表失败：sql:{}", tdEngineTable.createTableSql());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Map<String, Object> queryHistory(SeriesSearchParamVO seriesSearchParamVO) {
        Map<String, Object> result = new HashMap<>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 执行时序数据库脚本进行查询测定
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            TdEngineSqlBuild build = new TdEngineSqlBuild();
            String sql = build.from("`" + seriesSearchParamVO.getDeviceCode() + "`")
                    .where("thetime >= ' " + sf.format(seriesSearchParamVO.getBeginDate()) + "'")
                    .where("and thetime <= '" + sf.format(seriesSearchParamVO.getEndDate()) + "'")
                    .toString();
            log.info("查询的sql为 {}",sql);
            resultSet = statement.executeQuery(sql);
            //获取列集
            ResultSetMetaData metaData = resultSet.getMetaData();
            //获取列的数量
            int columnCount = metaData.getColumnCount();

            List<Object> xData = new ArrayList();
            List<Object> yData = new ArrayList();
            String pointCode = seriesSearchParamVO.getPointCodeList().get(0);
            while (resultSet.next()) {
                for (int i = 0; i < columnCount; i++) {
                    //通过序号获取列名   起始值为1
                    String columnName = metaData.getColumnName(i+1);
                    String columnValue = resultSet.getString(columnName);
                    if ("thetime".equals(columnName)) {
                        xData.add(columnValue);
                        continue;
                    }
                    if (pointCode.equals(columnName)) {
                        yData.add(columnValue);
                    }
                }
            }

            resultSet.close();
            statement.close();
            result.put("xdata", xData);
            result.put("ydata", yData);
            return result;
        } catch (Exception e) {
            log.error("历史数据查询失败");
        }finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //查询最大值 最小  平均 最大差值  最近值
    @Override
    public Map<String, Object> selectDataHistoryStatistic(SeriesSearchParamVO seriesSearchParamVO) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String pointCode = seriesSearchParamVO.getPointCodeList().get(0);
        //先给默认值
        Map<String, Object> result = new HashMap();
        result.put("currentValue",result.getOrDefault("firstValue",0));
        result.put("avgValue", result.getOrDefault("avgValue",0));
        result.put("maxValue", result.getOrDefault("maxValue",0));
        result.put("minValue",result.getOrDefault("minValue",0));
        result.put("maxDiff", result.getOrDefault("maxDiff",0));

        // 执行时序数据库脚本进行查询测定
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            TdEngineSqlBuild tdEngineSqlBuild =  new TdEngineSqlBuild();
            String sql = tdEngineSqlBuild.from("`" + seriesSearchParamVO.getDeviceCode() + "`")
                    .column("first(${column}) as firstValue".replace("${column}","`"+pointCode+"`"))
                    .column("avg(${column}) as avgValue".replace("${column}","`"+pointCode+"`"))
                    .column("max(${column}) as maxValue".replace("${column}","`"+pointCode+"`"))
                    .column("min(${column}) as minValue".replace("${column}","`"+pointCode+"`"))
                    .column("max(${column}) - min(${column}) as maxDiff ".replace("${column}","`"+pointCode+"`"))
                    .where("thetime >= '" + sf.format(seriesSearchParamVO.getBeginDate()) + "'")
                    .where("and thetime <= '" + sf.format(seriesSearchParamVO.getBeginDate()) + "'")
                    .toString();

            log.info("查询的sql为 {}",sql);
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {

                result.put("currentValue",resultSet.getBigDecimal("firstValue").setScale(2, BigDecimal.ROUND_HALF_UP));
                result.put("avgValue", resultSet.getBigDecimal("avgValue").setScale(2, BigDecimal.ROUND_HALF_UP));
                result.put("maxValue", resultSet.getBigDecimal("maxValue").setScale(2, BigDecimal.ROUND_HALF_UP) );
                result.put("minValue",resultSet.getBigDecimal("minValue").setScale(2, BigDecimal.ROUND_HALF_UP));
                result.put("maxDiff", resultSet.getBigDecimal("maxDiff").setScale(2, BigDecimal.ROUND_HALF_UP));
            }

            resultSet.close();
            statement.close();

            return result;
        } catch (Exception e) {
            log.error("历史数据查询失败");
        }finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}
