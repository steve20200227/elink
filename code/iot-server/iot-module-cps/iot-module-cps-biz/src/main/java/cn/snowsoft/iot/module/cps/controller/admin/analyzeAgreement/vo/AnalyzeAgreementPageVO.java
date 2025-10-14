package cn.snowsoft.iot.module.cps.controller.admin.analyzeAgreement.vo;

import cn.snowsoft.iot.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@TableName(value = "cps_analyze_agreement", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AnalyzeAgreementPageVO extends PageParam {
    /**
     * 接入地址
     */
    private String accessAddress;

    /**
     * 协议名称
     */
    private String agreementName;

    /**
     * 协议编码
     */
    private String agreementCode;


    /**
     * 自定义协议
     */
    private String customAgreement;
}