package cn.snowsoft.iot.module.job.service;


import cn.snowsoft.iot.module.job.core.model.XxlJobInfo;
import cn.snowsoft.iot.module.job.core.model.XxlJobUser;
import com.xxl.job.core.biz.model.ReturnT;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * core job action for xxl-job
 * 
 * @author xuxueli 2016-5-28 15:30:33
 */
public interface XxlJobService {

	/**
	 * page list
	 *
	 * @param start
	 * @param length
	 * @param jobGroup
	 * @param jobDesc
	 * @param executorHandler
	 * @param author
	 * @param jobType
	 * @param relevanceId
	 * @return
	 */
	public Map<String, Object> pageList(int start, int length, int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author, String jobType, Long relevanceId);

	/**
	 * add job
	 *
	 * @param jobInfo
	 * @return
	 */
	public ReturnT<String> add(XxlJobInfo jobInfo);

	/**
	 * update job
	 *
	 * @param jobInfo
	 * @return
	 */
	public ReturnT<String> update(XxlJobInfo jobInfo);

	/**
	 * remove job
	 * 	 *
	 * @param id
	 * @return
	 */
	public ReturnT<String> remove(int id);

	/**
	 * start job
	 *
	 * @param id
	 * @return
	 */
	public ReturnT<String> start(int id);

	/**
	 * stop job
	 *
	 * @param id
	 * @return
	 */
	public ReturnT<String> stop(int id);

	/**
	 * trigger
	 *
	 * @param loginUser
	 * @param jobId
	 * @param executorParam
	 * @param addressList
	 * @return
	 */
	public ReturnT<String> trigger(XxlJobUser loginUser, int jobId, String executorParam, String addressList);

	/**
	 * dashboard info
	 *
	 * @return
	 */
	public Map<String,Object> dashboardInfo();

	/**
	 * chart info
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ReturnT<Map<String,Object>> chartInfo(Date startDate, Date endDate);

	List<XxlJobInfo> getByRelevanceId(Long relevanceId, int jobId);
}
