import request from '@/utils/request.js'

/**
 * 获取token
 */
export const getToken = () => {
  return request({
    url: `cps/emqx/getToken`,
    method: 'get',
  })
}
