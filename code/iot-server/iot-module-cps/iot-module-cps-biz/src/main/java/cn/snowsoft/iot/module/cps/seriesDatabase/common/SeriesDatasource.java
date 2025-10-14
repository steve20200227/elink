package cn.snowsoft.iot.module.cps.seriesDatabase.common;


import cn.snowsoft.iot.module.cps.controller.admin.monitoring.vo.SearchParamVO;
import cn.snowsoft.iot.module.cps.controller.admin.monitoring.vo.SeriesSearchParamVO;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.model.BaseSeriesTable;

import java.util.List;
import java.util.Map;

public interface SeriesDatasource {

        String getType();
        Object getConnection();
        //执行建表
        Boolean createTable(BaseSeriesTable baseSeriesTable);

        Map<String, Object> queryHistory(SeriesSearchParamVO seriesSearchParamVO);

        Map<String, Object> selectDataHistoryStatistic(SeriesSearchParamVO seriesSearchParamVO);
}
