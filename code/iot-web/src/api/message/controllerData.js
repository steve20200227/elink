import request from '@/utils/request'
const PREFIX_API = '/message'

export const getDashboardHeadData = (data) => {
  return request({
    url: PREFIX_API+'/dashboard/getDashboardHeadData',
    method: 'post',
    data
  })
}


export const getMessageInfo = (data)=>{
  return  request({
    url: PREFIX_API+'/dashboard/getMessageInfo/vue',
    method: 'post',
    data
  })
}


export const getTemplateInfo = (data)=>{
  return  request({
    url: PREFIX_API+'/dashboard/getTemplateInfo',
    method: 'post',
    data
  })
}


export const getAppInfo = (data)=>{
  return  request({
    url: PREFIX_API+'/dashboard/getAppInfo',
    method: 'post',
    data
  })
}

export const getPushUserInfo = (data)=>{
  return  request({
    url: PREFIX_API+'/dashboard/getPushUserInfo',
    method: 'post',
    data
  })
}
