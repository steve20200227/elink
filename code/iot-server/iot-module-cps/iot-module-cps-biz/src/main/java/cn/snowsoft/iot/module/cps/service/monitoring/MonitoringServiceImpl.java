package cn.snowsoft.iot.module.cps.service.monitoring;


import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.monitoring.vo.SearchParamVO;
import cn.snowsoft.iot.module.cps.controller.admin.monitoring.vo.SeriesSearchParamVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.monitoring.Monitoring;
import cn.snowsoft.iot.module.cps.dal.mysql.monitoring.MonitoringMapper;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.SeriesDatasource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * 数采监控
 */
@Service

public class MonitoringServiceImpl extends ServiceImpl<MonitoringMapper, Monitoring> implements IMonitoringService {

	@Autowired(required = false)
	private SeriesDatasource seriesDatasource;

	/**
	 * @param searchParamVO searchParamVO 开始时间结束时间
	 * @return 时序数据库查询结果
	 */
	@Override
	public CommonResult cardDate(SearchParamVO searchParamVO) throws IOException {
		SeriesSearchParamVO seriesSearchParamVO = new SeriesSearchParamVO();
		BeanUtils.copyProperties(searchParamVO, seriesSearchParamVO);
		List<String> list = new ArrayList<>();
		list.add(searchParamVO.getPointCode());
		seriesSearchParamVO.setPointCodeList(list);
		return CommonResult.success(seriesDatasource.queryHistory(seriesSearchParamVO));
	}
	@Override
	public CommonResult selectDataHistoryStatistic(SearchParamVO searchParamVO) throws IOException {
		return CommonResult.success(historyStatistic(searchParamVO));
	}


	public Map<String, Object> historyStatistic(SearchParamVO searchParamVO) throws IOException {
		SeriesSearchParamVO seriesSearchParamVO = new SeriesSearchParamVO();
		BeanUtils.copyProperties(searchParamVO, seriesSearchParamVO);
		List<String> list = new ArrayList<>();
		list.add(searchParamVO.getPointCode());
		seriesSearchParamVO.setPointCodeList(list);
		Map<String, Object> stringObjectMap = seriesDatasource.selectDataHistoryStatistic(seriesSearchParamVO);
		return stringObjectMap;
	}
}
