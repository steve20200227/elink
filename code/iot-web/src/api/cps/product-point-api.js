import request from '@/utils/request.js'

/**
 * 分页查询
 * @param current 页码
 * @param size  每页数量
 * @param params  查询条件
 */
export const page = (current, size, params) => {
  return request({
    url: `cps/productPoint/page`,
    method: 'get',
    params: {
      ...params,
      pageNo: current,
      pageSize: size,
    }
  })
}

/**
 * 列表查询
 * @param params  查询条件
 */
export const list = (params) => {
  return request({
    url: `cps/productPoint/list`,
    method: 'get',
    params: {
      ...params
    }
  })
}


/**
 * 详情
 */
export const detail = (id) => {
  return request({
    url: `cps/productPoint/detail`,
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
    url: `cps/productPoint/submit`,
    method: 'post',
    data: data
  })
}

/**
 * 逻辑删除
 */
export const remove = (ids) => {
  return request({
    url: `cps/productPoint/remove`,
    method: 'post',
    params: {
      ids,
    }
  })
}

/**
 * 根据产品id查询设备点位信息
 */
export const getByProductId = (productId) => {
  return request({
    url: `cps/productPoint/getByProductId`,
    method: 'post',
    params: {
      productId
    }
  })
}
