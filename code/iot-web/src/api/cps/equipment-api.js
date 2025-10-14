import request from '@/utils/request.js'

const CONTROLLER_NAME = "equipment";

/**
 * 分页查询
 * @param current 页码
 * @param size  每页数量
 * @param params  查询条件
 */
export const page = (pageNo, pageSize, params) => {
  return request({
    url: `cps/equipment/page`,
    method: 'get',
    params: {
      ...params,
      pageNo,
      pageSize,
    }
  })
}

/**
 * 查询设备集合
 * @param params  查询条件
 */
export const getDeviceList = (params) => {
  return request({
    url: `cps/equipment/list`,
    method: 'get',
    params: {
      ...params
    }
  })
}


/**
 * 生成账号密码接入协议
 * @param data
 * @returns {*}
 */
export const addAuthenticationAdd = (data) => {
  return request({
    url: `cps/equipment/addAuthentication`,
    method: 'post',
    data: data
  })
}
/**
 * 分页查询
 * @param current 页码
 * @param size  每页数量
 * @param params  查询条件
 */
export const pageByProductCode = (params) => {
  return request({
    url: `cps/equipment/pageByProductCode`,
    method: 'get',
    params: {
      ...params,
    }
  })
}

/**
 * 启用
 */
export const enableEquipment = (ids) => {
  return request({
    url: `cps/equipment/enableEquipment`,
    method: 'post',
    params: {
      ids,
    }
  })
}

/**
 * 失效
 */
export const disenableEquipment = (ids) => {
  return request({
    url: `cps/equipment/disenableEquipment`,
    method: 'post',
    params: {
      ids,
    }
  })
}

/**
 * 逻辑删除
 */
export const remove = (data) => {
  return request({
    url: `cps/equipment/remove`,
    method: 'post',
    data
  })
}

/**
 * 重启
 * @returns {AxiosPromise}
 */
export const reStart = () => {
  return request({
    url: `cps/equipment/reStart`,
    method: 'get'
  })
}

/**
 * 保存或修改
 */
export const submit = (data) => {
  return request({
    url: `cps/equipment/submit`,
    method: 'post',
    data: data
  })
}

/**
 * 批量新增
 */
export const saveBatch = (data) => {
  return request({
    url: 'cps/equipment/BatchAdd',
    method: 'post',
    data: data
  })
}

/**
 * 批量新增
 */
export const batchSubmit = (data) => {
  return request({
    url: `cps/equipment/batchSubmit`,
    method: 'post',
    data: data
  })
}


/**
 * 详情
 */
export const detail = (id) => {
  return request({
    url: `cps/equipment/detail`,
    method: 'get',
    params: {
      id
    }
  })
}

/**
 * 设备数据监控接口，此接口会针对当前设备进行ekuiper规则创建，请勿在其他业务处使用
 */
export const detailMonitor = (id) => {
  return request({
    url: `cps/equipment/detailMonitor`,
    method: 'get',
    params: {
      id
    }
  })
}

/**
 *
 */
export const cancelPassage = (id) => {
  return request({
    url: `cps/equipment/cancelPassage`,
    method: 'get',
    params: {
      id
    }
  })
}

/**
 *
 */
export const getEquipmentOptions = (productCode) => {
  return request({
    url: `cps/equipment/getEquipmentOptions?productCode=` + productCode,
    method: 'get',
  })
}


/**
 * 设备转换规则的保持
 */
export const cpsRuleSubmit = (data) => {
  return request({
    url: `/cps/cpsRule/saveRuleInformation`,
    method: 'post',
    data: data
  })
}

/**
 * 根据转换规则的id查询详情
 */
export const cpsRuleDetail = (id) => {
  return request({
    url: `/cps/cpsRule/detail`,
    method: 'get',
    params: {
      id
    }
  })
}

export const getByIds = (ids) => {
  return request({
    url: 'cps/equipment/getByIds',
    method: 'post',
    params: {
      ids: ids
    }
  })
}

export const getChildEquipment = (equipmentCode) => {
  return request({
    url: 'cps/equipment/getChildEquipment',
    method: 'get',
    params: {
      equipmentCode
    }
  })
}



/**
 * 分页查询
 * @param current 页码
 * @param size  每页数量
 * @param params  查询条件
 */
export const selectRuleList = (pageNo, pageSize, params) => {
  return request({
    url: `cps/equipment/selectRuleList`,
    method: 'get',
    params: {
      pageNo,
      pageSize,
      ...params,
    }
  })
}


/**
 * 设备转换规则详情查询 根据关联id即设备id和类型：属性、事件、功能
 */
export const cpsRuleDetailByData = (params) => {
  return request({
    url: `/cps/cpsRule/detailByWhere`,
    method: 'get',
    params: params
  })
}
