import request from '@/utils/request.js'

/**
 * 分页查询
 * @param current 页码
 * @param size  每页数量
 * @param params  查询条件
 */
export const page = (current, size, params) => {
  return request({
    url: `cps/product/page`,
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
export const remove = (ids) => {
  return request({
    url: `cps/product/remove`,
    method: 'get',
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
    url: `cps/product/submit`,
    method: 'post',
    data: data
  })
}

/**
 * 详情
 */
export const detail = (id) => {
  return request({
    url: `cps/product/detail`,
    method: 'get',
    params: {
      id
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
    url: `cps/product/addAuthentication`,
    method: 'post',
    data: data
  })
}


/**
 * 获取网关设备类型产品
 */
export const getProductOptions = () => {
  return request({
    url: `cps/product/getProductOptions`,
    method: 'get',
  })
}

/**
 * 获取所有已启用产品
 * @returns {*}
 */
export const getProductByEnable = () => {
  return request({
    url: `cps/product/getProductByEnable`,
    method: 'get',
  })
}
