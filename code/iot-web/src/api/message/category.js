import request from '@/utils/request'
const PREFIX_API = '/message'
export function list(query) {
  return request({
    url: PREFIX_API+'/msgTypeItem/selectTree',
    method: 'get',
    params: query
  })
}

export function save(data) {
  return request({
    url: PREFIX_API+'/msgTypeItem/saveOrUpdate',
    method: 'post',
    data
  })
}

export const remove = (ids) => {
  return request({
    url: PREFIX_API+'/msgTypeItem/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}
