import request from '@/utils/request.js'

/**
 * 分页查询
 * @param current 页码
 * @param size  每页数量
 * @param params  查询条件
 */
export const page = (current, size, params) => {
  return request({
    url: `warning/record/page`,
    method: 'get',
    params: {
      ...params,
      pageNo: current,
      pageSize: size,
    }
  })
}

/**
 * 更改告警记录状态
 * @param data
 * @returns {*}
 */
export const updateStatus = (data) => {
  return request({
    url: `warning/record/updateStatus`,
    method: 'post',
    data
  })
}

/**
 * 根据 id 查询记录数据
 * @param id
 * @returns {*}
 */
export const detail = (id) => {
  return request({
    url: 'warning/record/detail',
    method: 'get',
    params: {
      id
    }
  })
}

/**
 * 查询动作日志
 * @param configId
 * @param warningTag
 * @returns {*}
 */
export const getRecodeLog = (configId, warningTag) => {
  return request({
    url: 'warning/record/getRecodeLogList',
    method: 'get',
    params: {
      configId,
      warningTag
    }
  })
}
