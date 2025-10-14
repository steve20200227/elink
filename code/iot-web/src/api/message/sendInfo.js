import request from '@/utils/request'
const PREFIX_API = '/message'

export const selectPage = (current, size, params) => {
  return request({
    url: PREFIX_API+'/msgSendRecord/selectPage',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const templateParamList = (data) => {
  return request({
    url: PREFIX_API + '/msgTemplateParam/selectList',
    method: 'get',
    param:data
  })
}

export const templateParties = (data) => {
  return request({
    url: PREFIX_API + '/msgTemplateParties/selectById',
    method: 'get',
    param:data
  })
}
