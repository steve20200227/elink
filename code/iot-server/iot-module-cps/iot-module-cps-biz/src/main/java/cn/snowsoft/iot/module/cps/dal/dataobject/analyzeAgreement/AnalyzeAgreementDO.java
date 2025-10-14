package cn.snowsoft.iot.module.cps.dal.dataobject.analyzeAgreement;

import cn.snowsoft.iot.framework.mybatis.core.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

@TableName(value = "cps_analyze_agreement", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AnalyzeAgreementDO extends TenantEntity {

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
