package cn.snowsoft.iot.module.cps.seriesDatabase.influxdb.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Slf4j
@TableName("test")
public class InfluxdbQuery {
    //桶名
    private String tableName;
    //表名
    @TableField("measurement")
    private String measurement;

    //开始日期
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDate;
    //结束日期
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    private List<String> fieldList;
}
