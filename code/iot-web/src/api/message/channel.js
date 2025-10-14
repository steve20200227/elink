import request from '@/utils/request'
const PREFIX_API = '/message'

export const selectPage = (current, size, params) => {
  return request({
    url: PREFIX_API+'/msgChannel/selectPage',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}
export const saveOrUpdate = (data) => {
  return request({
    url: PREFIX_API+'/msgChannel/saveOrUpdate',
    method: 'post',
    data
  })
}
export const remove = (ids) => {
  return request({
    url: PREFIX_API+'/msgChannel/remove',
    method: 'post',
    params:{
      ids
    }
  })
}

// ---------------


// 消息通道分页
export const getPage = (data) => {
  return request({
    url: PREFIX_API+'/app/search',
    method: 'post',
    data
  })
}

// 根据id启用禁用
export const updateStatusById = (data) => {
  return request({
    url: PREFIX_API + '/app/updateStatusById',
    method: 'post',
    data
  })
}

// 根据 ids 删除
export const deleteById = (data) => {
  return request({
    url: PREFIX_API + '/app/deleteByIds',
    method: 'post',
    data
  })
}

// 新增
export const save = (data) => {
  return request({
    url: PREFIX_API + '/app/save',
    method: 'post',
    data
  })
}

// 选择消息通道
export const selectChannel = (data) => {
  return request({
    url: PREFIX_API + '/app/getAppConfigByChannelType',
    method: 'post',
    data
  })
}

/**
 * 更新
 * @param data
 * @returns {*}
 */
export const updateById = (data) => {
  return request({
    url: PREFIX_API + '/app/updateById',
    method: 'post',
    data
  })
}




