import request from '@/utils/request.js'

export const getTree = () => {
  return request({
    url: `cps/productPointSort/getTree`,
    method: 'get'
  })
}

export const save = (data) => {
  return request({
    url: `cps/productPointSort/save`,
    method: 'post',
    data
  })
}

export const remove = (data) => {
  return request({
    url: `cps/productPointSort/remove`,
    method: 'post',
    data
  })
}


export const detail = (id) => {
  return request({
    url: `cps/productPointSort/detail`,
    method: 'get',
    params: {
      id
    }
  })
}
