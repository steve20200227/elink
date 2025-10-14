package cn.snowsoft.iot.module.job.controller;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.job.api.XxljobAdminApiImpl;
import cn.snowsoft.iot.module.job.dao.XxlJobGroupDao;
import cn.snowsoft.iot.module.job.dao.XxlJobUserDao;
import cn.snowsoft.iot.module.job.dto.XxlJobEntity;
import cn.snowsoft.iot.module.job.service.XxlJobService;
import cn.snowsoft.iot.module.job.core.exception.XxlJobException;
import cn.snowsoft.iot.module.job.core.model.XxlJobGroup;
import cn.snowsoft.iot.module.job.core.model.XxlJobInfo;
import cn.snowsoft.iot.module.job.core.model.XxlJobUser;
import cn.snowsoft.iot.module.job.core.route.ExecutorRouteStrategyEnum;
import cn.snowsoft.iot.module.job.core.scheduler.MisfireStrategyEnum;
import cn.snowsoft.iot.module.job.core.scheduler.ScheduleTypeEnum;
import cn.snowsoft.iot.module.job.core.thread.JobScheduleHelper;
import cn.snowsoft.iot.module.job.core.util.I18nUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController {
	private static Logger logger = LoggerFactory.getLogger(JobInfoController.class);

	@Resource
	private XxlJobGroupDao xxlJobGroupDao;
	@Resource
	private XxlJobService xxlJobService;

	@Resource
	private XxlJobUserDao xxlJobUserDao;

	@Resource
	private XxljobAdminApiImpl xxljobAdminApi;

	static XxlJobUser loginUser;
	@RequestMapping
	@PermitAll
	public String index(HttpServletRequest request, Model model, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

		// 枚举-字典
		model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values());	    // 路由策略-列表
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());								// Glue类型-字典
		model.addAttribute("ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values());	    // 阻塞处理策略-字典
		model.addAttribute("ScheduleTypeEnum", ScheduleTypeEnum.values());	    				// 调度类型
		model.addAttribute("MisfireStrategyEnum", MisfireStrategyEnum.values());	    			// 调度过期策略

		// 执行器列表
		List<XxlJobGroup> jobGroupList_all =  xxlJobGroupDao.findAll();
		loginUser = xxlJobUserDao.loadByUserName("admin");

		// filter group
		List<XxlJobGroup> jobGroupList = filterJobGroupByRole(request, jobGroupList_all);
		if (jobGroupList==null || jobGroupList.size()==0) {
			throw new XxlJobException(I18nUtil.getString("jobgroup_empty"));
		}

		model.addAttribute("JobGroupList", jobGroupList);
		model.addAttribute("jobGroup", jobGroup);

		return "jobinfo/jobinfo.index";
	}

	public static List<XxlJobGroup> filterJobGroupByRole(HttpServletRequest request, List<XxlJobGroup> jobGroupList_all){
		List<XxlJobGroup> jobGroupList = new ArrayList<>();
		if (jobGroupList_all!=null && jobGroupList_all.size()>0) {
			if (loginUser.getRole() == 1) {
				jobGroupList = jobGroupList_all;
			} else {
				List<String> groupIdStrs = new ArrayList<>();
				if (loginUser.getPermission()!=null && loginUser.getPermission().trim().length()>0) {
					groupIdStrs = Arrays.asList(loginUser.getPermission().trim().split(","));
				}
				for (XxlJobGroup groupItem:jobGroupList_all) {
					if (groupIdStrs.contains(String.valueOf(groupItem.getId()))) {
						jobGroupList.add(groupItem);
					}
				}
			}
		}
		return jobGroupList;
	}
	public static void validPermission(HttpServletRequest request, int jobGroup) {
		if (!loginUser.validPermission(jobGroup)) {
			throw new RuntimeException(I18nUtil.getString("system_permission_limit") + "[username="+ loginUser.getUsername() +"]");
		}
	}
	
	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
			@RequestParam(required = false, defaultValue = "10") int length,
			@RequestParam(required = false, defaultValue = "0") int jobGroup,
			@RequestParam(required = false, defaultValue = "0")int triggerStatus, String jobDesc, String executorHandler, String author, String jobType, Long relevanceId) {
		
		return xxlJobService.pageList(start, length, jobGroup, triggerStatus, jobDesc, executorHandler, author, jobType, relevanceId);
	}
	
	@RequestMapping("/add")
	@ResponseBody
	@PermitAll
	public ReturnT<String> add(@RequestBody XxlJobInfo jobInfo) {
		return xxlJobService.add(jobInfo);
	}
	
	@RequestMapping("/update")
	@ResponseBody
	@PermitAll
	public ReturnT<String> update(@RequestBody XxlJobInfo jobInfo) {
		return xxlJobService.update(jobInfo);
	}
	
	@RequestMapping("/remove")
	@ResponseBody
	@PermitAll
	public ReturnT<String> remove(int id) {
		return xxlJobService.remove(id);
	}
	
	@RequestMapping("/stop")
	@ResponseBody
	@PermitAll
	public ReturnT<String> pause(int id) {
		return xxlJobService.stop(id);
	}
	
	@RequestMapping("/start")
	@ResponseBody
	@PermitAll
	public ReturnT<String> start(int id) {
		return xxlJobService.start(id);
	}
	
	@RequestMapping("/trigger")
	@ResponseBody
	@PermitAll
	public ReturnT<String> triggerJob(HttpServletRequest request, int id, String executorParam, String addressList) {
		// login user
		// trigger
		return xxlJobService.trigger(loginUser, id, executorParam, addressList);
	}

	@RequestMapping("/nextTriggerTime")
	@ResponseBody
	public ReturnT<List<String>> nextTriggerTime(@RequestBody XxlJobInfo paramXxlJobInfo) {

		/*XxlJobInfo paramXxlJobInfo = new XxlJobInfo();
		paramXxlJobInfo.setScheduleType(scheduleType);
		paramXxlJobInfo.setScheduleConf(scheduleConf);*/

		List<String> result = new ArrayList<>();
		try {
			Date lastTime = new Date();
			for (int i = 0; i < 5; i++) {
				lastTime = JobScheduleHelper.generateNextValidTime(paramXxlJobInfo, lastTime);
				if (lastTime != null) {
					result.add(DateUtil.formatDateTime(lastTime));
				} else {
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ReturnT<List<String>>(ReturnT.FAIL_CODE, (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_unvalid")) + e.getMessage());
		}
		return new ReturnT<List<String>>(result);

	}

	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public CommonResult<String> saveOrUpdate(@RequestBody XxlJobEntity xxlJobEntity) {
		return xxljobAdminApi.saveOrUpdate(xxlJobEntity);
	}
	
}
