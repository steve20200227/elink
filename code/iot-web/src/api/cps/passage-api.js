import request from '@/utils/request.js'

/**
 * 保存或修改
 */
export const submit = (data) => {
  return request({
    url: `cps/passage/saveOrUpdate`,
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
export const page = (current, size, params) => {
  return request({
    url: `cps/passage/page`,
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

/**
 * 获取通道详情
 * @param id
 * @returns {*}
 */
export const getDetail = (id) => {
  return request({
    url: `cps/passage/detail`,
    method: 'get',
    params: {
      id
    }
  })
}

/**
 * 通道关联设备
 * @param ids
 * @param passageId
 * @returns {AxiosPromise}
 */
export const relevancyEquipment = (ids, passageId) => {
  return request({
    url: `cps/passage/relevancyEquipment`,
    method: 'get',
    params: {
      ids,
      passageId
    }
  })
}

/**
 * 删除通道
 * @param id
 * @returns {*}
 */
export const remove = (id) => {
  return request({
    url: `cps/passage/remove`,
    method: 'get',
    params: {
      id
    }
  })
}
