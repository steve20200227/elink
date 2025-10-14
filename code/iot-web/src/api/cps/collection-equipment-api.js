import request from "@/utils/request";

const CONTROLLER_NAME = "collectionEquipment";

/**
 * 分页查询
 * @param current 页码
 * @param size  每页数量
 * @param params  查询条件
 */
export const page = (pageNo, pageSize, params) => {
  return request({
    url: `cps/${CONTROLLER_NAME}/page`,
    method: 'get',
    params: {
      ...params,
      pageNo,
      pageSize,
    }
  })
}


/**
 * 详情
 */
export const detail = (id) => {
  return request({
    url: `cps/${CONTROLLER_NAME}/detail`,
    method: 'get',
    params: {
      id
    }
  })
}



/**
 * 保存或修改
 */
export const submit = (data) => {
  return request({
    url: `cps/${CONTROLLER_NAME}/submit`,
    method: 'post',
    data: data
  })
}

/**
 * 逻辑删除
 */
export const remove = (ids) => {
  return request({
    url: `cps/${CONTROLLER_NAME}/remove`,
    method: 'post',
    params: {
      ids,
    }
  })
}

/**
 * 解除采集网关的绑定
 * @param id 采集设备Id
 * @param gatewayId 采集网关Id
 */
export const unbind = (id, gatewayId) => {
  return request({
    url: `cps/${CONTROLLER_NAME}/unbind`,
    method: 'post',
    params: {
      id,
      gatewayId,
    }
  })
}

/**
 * 查询未绑定采集网关的采集设备
 * @param current 页码
 * @param size  每页数量
 * @param params  查询条件
 */
export const getGatewayPage = (pageNo, pageSize, params) => {
  return request({
    url: `cps/${CONTROLLER_NAME}/gatewayPage`,
    method: 'get',
    params: {
      ...params,
      pageNo,
      pageSize,
    }
  })
}

/**
 * 采集网关的绑定
 * @param data 采集设备集合
 */
export const bind = (data) => {
  return request({
    url: `cps/${CONTROLLER_NAME}/bind`,
    method: 'post',
    data: data
  })
}

/**
 * 查询已绑定采集站点的资产台账编码
 */
export const getBindCode = () => {
  return request({
    url: `cps/${CONTROLLER_NAME}/getBindCode`,
    method: 'post',
  })
}
