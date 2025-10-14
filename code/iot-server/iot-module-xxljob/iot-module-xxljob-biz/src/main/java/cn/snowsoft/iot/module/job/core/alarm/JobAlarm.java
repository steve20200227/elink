package cn.snowsoft.iot.module.job.core.alarm;

import cn.snowsoft.iot.module.job.core.model.XxlJobInfo;
import cn.snowsoft.iot.module.job.core.model.XxlJobLog;

/**
 * @author xuxueli 2020-01-19
 */
public interface JobAlarm {

    /**
     * job alarm
     *
     * @param info
     * @param jobLog
     * @return
     */
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog);

}
