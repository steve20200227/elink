package cn.snowsoft.iot.module.cps.controller.admin.monitoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * SearchParamVO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchParamVO {
    //开始日期
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDate;
    //结束日期
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    //测点编号
    private String pointCode;
    private String deviceCode;
}
