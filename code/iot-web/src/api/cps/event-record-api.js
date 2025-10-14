import request from '@/utils/request.js'


/**
 * 查询数据
 * @param current 页码
 * @param size  每页数量
 * @param params  查询条件
 */
export const list = (params) => {
  return request({
    url: `cps/eventRecord/list`,
    method: 'get',
    params: {
      ...params
    }
  })
}
