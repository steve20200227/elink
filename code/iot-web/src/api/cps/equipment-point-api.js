import request from '@/utils/request.js'

/**
 * 分页查询
 * @param current 页码
 * @param size  每页数量
 * @param params  查询条件
 */
export const page = (current, size, params) => {
  return request({
    url: `cps/equipmentPoint/page`,
    method: 'get',
    params: {
      ...params,
      pageNo: current,
      pageSize: size,
    }
  })
}

/**
 * 查询指定设备的已启用的属性和事件列表
 * @param params  查询条件
 */
export const getDeviceAttributeAndEvent = (equipmentCode) => {
  return request({
    url: `cps/equipmentPoint/getDeviceAttributeAndEvent`,
    method: 'get',
    params: {
      equipmentCode
    }
  })
}

/**
 * 逻辑删除
 */
export const remove = (ids) => {
  return request({
    url: `cps/equipmentPoint/remove`,
    method: 'post',
    params: {
      ids,
    }
  })
}

/**
 * 保存或修改
 */
export const submit = (data) => {
  return request({
    url: `cps/equipmentPoint/submit`,
    method: 'post',
    data: data
  })
}

/**
 * 选择产品后覆盖之前的点位信息
 */
export const coveragePoint = (equipmentId, productId) => {
  return request({
    url: `cps/equipmentPoint/coveragePoint`,
    method: 'post',
    params: {
      equipmentId,
      productId
    }
  })
}
/**
 * 查询数采监控的历史记录
 */
export const selectData = (row) => {
  return request({
    url: `cps/monitoring/selectData`,
    method: 'get',
    params: {
      ...row,
    }
  })
}
/**
 * 设置点位总览数据
 */
export const selectDataHistoryStatistic = (row) => {
  return request({
    url: `cps/monitoring/selectDataHistoryStatistic`,
    method: 'get',
    params: {
      ...row,
    }
  })
}
export const ipUtil = () => {
  return request({
    url: `cps/monitoring/ipUtil`,
    method: 'post',
  })
}
/**
 * 详情
 */
export const detail = (id) => {
  return request({
    url: `cps/equipmentPoint/detail`,
    method: 'get',
    params: {
      id
    }
  })
}

/**
 * 根据设备id查询设备点位信息
 */
export const getByEquipmentId = (equipmentId) => {
  return request({
    url: `cps/equipmentPoint/getByEquipmentId`,
    method: 'post',
    params: {
      equipmentId
    }
  })
}

/**
 * 根据设备Ids和功能编码查询功能数据
 */
export const getByEquipmentIdsAndCode = (data) => {
  return request({
    url: 'cps/equipmentPoint/getByEquipmentIdsAndCode',
    method: 'post',
    data
  })
}

export const list = (params) => {
  return request({
    url: `cps/equipmentPoint/list`,
    method: 'get',
    params: {
      ...params
    }
  })
}
