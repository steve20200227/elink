import request from '@/utils/request.js'

/**
 * 新增或修改
 * @param data
 * @returns {*}
 */
export const submit = (data) => {
  return request({
    url: `warning/configuration/submit`,
    method: 'post',
    data
  })
}

/**
 * 分页查询
 * @param current 页码
 * @param size  每页数量
 * @param params  查询条件
 */
export const page = (current, size, params) => {
  return request({
    url: `warning/configuration/page`,
    method: 'get',
    params: {
      ...params,
      pageNo: current,
      pageSize: size,
    }
  })
}

/**
 * 逻辑删除
 */
export const remove = (id) => {
  return request({
    url: `warning/configuration/remove`,
    method: 'post',
    params: {
      id,
    }
  })
}

/**
 * 详情
 */
export const detail = (id) => {
  return request({
    url: `warning/configuration/detail`,
    method: 'get',
    params: {
      id
    }
  })
}

/**
 * updateStatus
 *
 * @param data
 * @returns {*}
 */
export const updateStatus = (data) => {
  return request({
    url: `warning/configuration/updateStatus`,
    method: 'post',
    data
  })
}


/**
 * 重启
 */
export const restart = (id) => {
  return request({
    url: `warning/configuration/restart`,
    method: 'get',
    params: {
      id
    }
  })
}

/**
 * 解绑
 */
export const untie = (id, equipmentCode) => {
  return request({
    url: `warning/configuration/untie`,
    method: 'get',
    params: {
      id,
      equipmentCode
    }
  })
}

/**
 * 根据设备编码解绑场景
 */
export const equipmentUntie = (equipmentCode) => {
  return request({
    url: `warning/configuration/equipmentUntie`,
    method: 'get',
    params: {
      equipmentCode
    }
  })
}
