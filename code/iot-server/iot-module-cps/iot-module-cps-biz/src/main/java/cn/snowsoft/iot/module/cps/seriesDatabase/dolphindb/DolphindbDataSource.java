package cn.snowsoft.iot.module.cps.seriesDatabase.dolphindb;

import cn.snowsoft.iot.module.cps.controller.admin.monitoring.vo.SeriesSearchParamVO;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.SeriesDatasource;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.model.BaseColumn;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.model.BaseSeriesTable;
import cn.snowsoft.iot.module.cps.seriesDatabase.dolphindb.model.DolphindbColumn;
import cn.snowsoft.iot.module.cps.seriesDatabase.dolphindb.model.DolphindbTable;
import cn.snowsoft.iot.module.cps.seriesDatabase.tdengine.build.TdEngineSqlBuild;
import cn.snowsoft.iot.module.cps.seriesDatabase.tdengine.model.TdEngineTable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xxdb.DBConnection;
import com.xxdb.data.BasicString;
import com.xxdb.data.BasicTable;
import com.xxdb.data.Entity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

@Component
@Slf4j
@ConditionalOnProperty(name = "seriesType",havingValue = "dolphindb")
public class DolphindbDataSource implements SeriesDatasource, DisposableBean {
    @Value("${dolphin.dbPath:}")
    private String dbPath;
    /**
     * 用户ID
     */
    @Value("${dolphin.userId:}")
    private String userId;
    /**
     * 密码
     */
    @Value("${dolphin.password:}")
    private String password;
    /**
     * 启用加密
     */
    @Value("${dolphin.enableEncryption:true}")
    private boolean enableEncryption;

    /**
     * hostName
     */
    @Value("${dolphin.hostName:}")
    private String hostName;

    /**
     * 数据节点端口
     */
    @Value("${dolphin.port:8900}")
    private int port;


    /**
     * DolphinDB连接对象
     */
    private static DBConnection CONN;

    @PostConstruct
    public void init(){
        try {

            if (StringUtils.isBlank(dbPath)) {
                // 数据库
                log.error("未配置dolphin.dbPath");
                return;
            }
            if (StringUtils.isBlank(hostName)) {
                log.error("未配置dolphin.hostName");
                return;
            }
            // 连接数据节点
            CONN = new DBConnection();
            CONN.connect(hostName, port, userId, password, "use IotCps", false, (String[])null, true);
            log.info("初始化dolphindb客户端成功");
        } catch (Exception e) {
            log.error("初始化dolphindb客户端失败");
            e.printStackTrace();
        }
    }

    /**
     * 释放连接
     */
    @Override
    public void destroy() {
        if (CONN != null) {
            CONN.close();
        }
    }

    @Override
    public String getType() {
        return "dolphindb";
    }

    @Override
    public Object getConnection() {
        return CONN;
    }

    @Override
    public Boolean createTable(BaseSeriesTable baseSeriesTable) {
        String tableName = baseSeriesTable.getTableName();
        DolphindbTable dolphindbTable = new DolphindbTable();
        BeanUtils.copyProperties(baseSeriesTable, dolphindbTable);
        JSONArray jsonArray = dolphindbTable.createTableBuild();
        try {
            List<Entity> arguments = new ArrayList<>();
            arguments.add(new BasicString(dbPath));
            arguments.add(new BasicString(tableName));
            arguments.add(new BasicString(jsonArray.toJSONString()));
            CONN.run("IotCps::createDeviceTable", arguments);
        } catch (IOException e) {
            log.error("dolphin数据库创建失败: ", e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public Map<String, Object> queryHistory(SeriesSearchParamVO seriesSearchParamVO) {
        Map<String, Object> result = new HashMap<>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        try {
            // 执行时序数据库脚本进行查询测定
            TdEngineSqlBuild tdEngineSqlBuild = new TdEngineSqlBuild();
            tdEngineSqlBuild.from("loadTable('"+ dbPath +"','pt_"+ seriesSearchParamVO.getDeviceCode() +"')");
            tdEngineSqlBuild.column("thetime");
            for (String pointCode : seriesSearchParamVO.getPointCodeList()) {
                tdEngineSqlBuild.column(pointCode);
            }
            tdEngineSqlBuild.where("thetime >= " + sf.format(seriesSearchParamVO.getBeginDate()))
                    .where(" and thetime <= " + sf.format(seriesSearchParamVO.getEndDate()));
            String sql = tdEngineSqlBuild.toString();
            log.info("查询的sql为 {}",sql);
            BasicTable data = (BasicTable) CONN.run(sql);

            if (ObjectUtils.isEmpty(data)) {
                return result;
            }

            List<Object> xData = new ArrayList();
            List<Object> yData = new ArrayList();
            int rows = data.rows();
            IntStream.range(0, rows).forEach(index -> {
                String rowJson = data.getRowJson(index);
                Map<String, Object> innerMap = JSON.parseObject(rowJson).getInnerMap();
                xData.add(innerMap.get("thetime"));
                yData.add(innerMap.get(seriesSearchParamVO.getPointCodeList().get(0)));
            });
            result.put("xdata", xData);
            result.put("ydata", yData);
            return result;
        } catch (IOException e) {
            log.error("历史数据查询失败 {}", e);
            return result;
        }

    }

    @Override
    public Map<String, Object> selectDataHistoryStatistic(SeriesSearchParamVO seriesSearchParamVO) {
        String pointCode = seriesSearchParamVO.getPointCodeList().get(0);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Map<String, Object> result = new HashMap();
        // 执行时序数据库脚本进行查询测定
        BasicTable data = null;
        try {
            TdEngineSqlBuild tdEngineSqlBuild = new TdEngineSqlBuild();
            tdEngineSqlBuild.from("loadTable('"+ dbPath +"','pt_"+ seriesSearchParamVO.getDeviceCode() +"')")
                    .column("last(${column}) as lastValue".replace("${column}",pointCode))
                    .column("avg(${column}) as avgValue".replace("${column}",pointCode))
                    .column("max(${column}) as maxValue".replace("${column}",pointCode))
                    .column("min(${column}) as minValue".replace("${column}",pointCode))
                    .column("max(${column}) - min(${column}) as maxDiff ".replace("${column}",pointCode))
                    .where("thetime >= " + sf.format(seriesSearchParamVO.getBeginDate()))
                    .where(" and thetime <= " + sf.format(seriesSearchParamVO.getEndDate()))
                    .toString();
            String sql = tdEngineSqlBuild.toString();
            log.info("查询的sql为 {}",sql);
            data = (BasicTable) CONN.run(sql);

        } catch (IOException e) {
            log.error("历史数据查询失败 {}", e);
        }
        //发生错误时处理
        if (ObjectUtils.isEmpty(data)) {
            result.put("currentValue","0");
            result.put("avgValue", "0");
            result.put("maxValue","0" );
            result.put("minValue","0");
            result.put("maxDiff","0");
            return result;
        }

        int rows = data.rows();

        for (int i = 0; i < rows; i++) {
            String rowJson = data.getRowJson(i);
            JSONObject o = JSON.parseObject(rowJson);
            result.put("currentValue",o.getString("lastValue") == null ? "0" : new BigDecimal(o.getString("lastValue")).setScale(2, BigDecimal.ROUND_HALF_UP));
            result.put("avgValue",o.getString("avgValue") == null ? "0" : new BigDecimal(o.getString("avgValue")).setScale(2, BigDecimal.ROUND_HALF_UP));
            result.put("maxValue",o.getString("maxValue") == null ? "0" : new BigDecimal(o.getString("maxValue")).setScale(2, BigDecimal.ROUND_HALF_UP));
            result.put("minValue",o.getString("minValue") == null ? "0" : new BigDecimal(o.getString("minValue")).setScale(2, BigDecimal.ROUND_HALF_UP));
            result.put("maxDiff",o.getString("maxDiff") == null ? "0" : new BigDecimal(o.getString("maxDiff")).setScale(2, BigDecimal.ROUND_HALF_UP));

        }
        return result;
    }


    //查询 最近 最大等数据
    public BasicTable searchHistoryStatistic(String tableName, String columnName, String begin, String end) {
        try {
            BasicTable table = (BasicTable) CONN.run(String.format("select last(%s) as lastValue,avg(%s) as avgValue,max(%s) as maxValue,min(%s) as minValue,max(%s) - min(%s) as maxDiff from loadTable('%s','%s') where thetime >= %s and thetime <= %s",
                    columnName,columnName,columnName,columnName,columnName,columnName, dbPath, "pt_" + tableName, begin, end));
            return table;
        } catch (IOException e) {
            log.error("历史数据查询失败 {}", e);
            return null;
        }
    }
}
