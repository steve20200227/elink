package cn.snowsoft.iot.module.cps.service.monitoring;


import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.cps.controller.admin.monitoring.vo.SearchParamVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.monitoring.Monitoring;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;

/**
 * 数采监控
 */
public interface IMonitoringService extends IService<Monitoring> {
 /*   *//**
     * 自定义分页
     *
     * @param page       page
     * @param monitoring monitoring
     * @return IPage
     * @throws Exception Exception
     *//*
    IPage<Monitoring> selectMonitoringPage(IPage<Monitoring> page, Monitoring monitoring) throws Exception;

    *//**
     * 新增或修改 数采监控
     *
     * @param monitoring monitoring
     * @return Boolean
     *//*
    CommonResult saveMonitoringType(Monitoring monitoring);

    *//**
     * 筛选后的 设备信息
     *
     * @param page   page
     * @param device device
     * @return IPage
     *//*
    IPage<Device> selectDevicePage(IPage<Device> page, Device device);

    *//**
     * 筛选后的 首页的设备信息
     *
     * @param page   分页所需要的参数
     * @param device 传参
     * @return IPage
     *//*
    IPage<Device> devicePage(IPage<Device> page, Device device);

    *//**
     * 启用/失效
     *
     * @param ids      ids
     * @param isStatus isStatus
     * @return Boolean
     *//*
    CommonResult updateMonitoringStatus(Long ids, String isStatus);

    *//**
     * 导入设备数据
     *
     * @param file file
     * @return boolean
     *//*
    CommonResult importMonitoring(MultipartFile file);

    *//**
     * 导出设备数据
     *
     * @param response   response
     * @param monitoring monitoring
     *//*
    void exportMonitoring(HttpServletResponse response, Monitoring monitoring);

    *//**
     * 导出模板
     *
     * @param response response
     *//*
    void exportTemplate(HttpServletResponse response);

    *//**
     * 返回属性，流计算，服务，事件数据的总和
     *
     * @param deviceCode   deviceCode
     * @param variableType variableType
     * @param query        query
     * @return DeviceCardVO
     *//*
    DeviceCardVO monitoringCard(String deviceCode, String variableType, Query query);

    *//**
     * 返回属性，流计算，服务，事件数据的总和 首页
     *
     * @param variableType variableType
     * @param stationCode  stationCode
     * @param stationName  stationName
     * @param query        query
     * @return DeviceCardVO
     *//*
    PartyPage monitoringDialog(String variableType, String stationCode, String stationName, Query query);

    *//**
     * 删除 数采监控 首页
     *
     * @param toLongList     toLongList
     * @param identification identification
     * @return boolean
     *//*
    boolean removeDevice(List<Long> toLongList, Integer identification);

    *//**
     * 删除 数采监控
     *
     * @param toLongList toLongList
     * @return boolean
     *//*
    boolean removeDevice(List<Long> toLongList);

    *//**
     * 进行保存或者修改编码
     *
     * @param terminalCoding terminalCoding
     * @param toLongList     toLongList
     * @return boolean
     *//*
    boolean saveDevice(String terminalCoding, List<Long> toLongList);

    *//**
     * 配置下发
     *
     * @param terminalCoding terminalCoding
     * @return Boolean
     *//*
    CommonResult configuration(String terminalCoding);*/

    /**
     * @param searchParamVO searchParamVO
     * @return Boolean
     */
    CommonResult cardDate(SearchParamVO searchParamVO) throws IOException;
    //查询历史数据 总和 平均值 最大值 最小值
    CommonResult selectDataHistoryStatistic(SearchParamVO searchParamVO) throws IOException;

   /* *//**
     * 筛选后的 首页的设备信息 弹出层
     *
     * @param variableType 类型
     * @param stationCode  查询编码
     * @param stationName  查询名称
     * @param query        分页
     * @return 返回数据
     *//*
    DeviceCardVO cardStationDialog(String variableType, String stationCode, String stationName, Query query);

    *//**
     * 导出历史数据
     *
     * @param searchParamVO 开始时间结束时间 数据
     * @param response      流
     *//*
    void exportCard(HttpServletResponse response, SearchParamVO searchParamVO) throws IOException;

    *//**
     * 进行修改Emqx的状态
     *
     * @param iplocation IP地址
     * @param endTime    结束时间
     * @param isEnable   修改状态
     * @return boolean
     *//*
    CommonResult updateEmqxStatus(String iplocation, String endTime, String isEnable);

    *//**
     * 流计算的历史数据的展示
     * 今日，本周，本月，开始到结束
     *
     * @param calcHistoryVO 传入开始时间，结束时间
     * @param distinction   月份等等的判断
     * @return 返回的数据
     *//*
    CommonResult calcHistoryData(CalcHistoryVO calcHistoryVO, String distinction);

    *//**
     * 导出历史数据 流计算
     *
     * @param response      流
     * @param calcHistoryVO 开始时间，结束时间 数据
     * @throws IOException 流异常
     *//*
    void exportCardCalc(HttpServletResponse response, CalcHistoryVO calcHistoryVO) throws IOException;
*/}
