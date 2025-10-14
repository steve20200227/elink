import request from "@/utils/request";

const CONTROLLER_NAME = "collectionGateway";

/**
 * 分页查询
 * @param current 页码
 * @param size  每页数量
 * @param params  查询条件
 */
export const page = (pageNo, pageSize, params) => {
  return request({
    url: `cps/collectionGateway/page`,
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
 * 配置下发
 */
export const configure = (data) => {
  return request({
    url: `cps/${CONTROLLER_NAME}/configure`,
    method: 'post',
    data: data
  })
}
