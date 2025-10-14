import request from '@/utils/request.js'

/**
 * 告警动作新增、编辑
 * @param data
 * @returns {*}
 */
export const submit = (data) => {
  return request({
    url: `warning/north/submit`,
    method: 'post',
    data
  })
}

/**
 * 分页查询
 */
export const page = (current, size, params) => {
  return request({
    url: `warning/north/page`,
    method: 'get',
    params: {
      ...params,
      pageNo: current,
      pageSize: size,
    }
  })
}

/**
 * 详情
 */
export const detail = (id) => {
  return request({
    url: `warning/north/detail`,
    method: 'get',
    params: {
      id
    }
  })
}

/**
 * 批量删除
 */
export const remove = (ids) => {
  return request({
    url: `warning/north/remove`,
    method: "get",
    params: {
      ids
    }
  })
}

/**
 * 查询所有消息模板
 */
export const getMessageList = () => {
  return request({
    url: `message/msgTemplate/getList`,
    method: 'get'
  })
}

/**
 * 测试连接
 * @returns {AxiosPromise}
 */
export const testConnection = (data) => {
  return request({
    url: 'warning/north/testMQTTConnection',
    method: 'post',
    data
  })
}

/**
 * 测试连接
 * @returns {AxiosPromise}
 */
export const testHttpConnection = (data) => {
  return request({
    url: 'warning/north/testHTTPConnection',
    method: 'post',
    data
  })
}

export const getByIds = (ids) => {
  return request({
    url: 'warning/north/getByIds',
    method: 'post',
    params: {
      ids: ids
    }
  })
}

/**
 * 分页查询告警动作执行记录
 * @param current 页码
 * @param size  每页数量
 * @param params  查询条件
 */
export const pageRecord = (current, size, params) => {
  return request({
    url: `warning/warningActionRecord/selectPage`,
    method: 'get',
    params: {
      ...params,
      pageNo: current,
      pageSize: size,
    }
  })
}
