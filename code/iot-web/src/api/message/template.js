import request from '@/utils/request'

const PREFIX_API = '/message'

export const selectPage = (current, size, params) => {
  return request({
    url: PREFIX_API + '/msgTemplate/selectPage',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}
export const selectType = () => {
  return request({
    url: PREFIX_API + '/msgTypeItem/selectType',
    method: 'get'
  })
}

export const saveOrUpdateRule = (data) => {
  return request({
    url: PREFIX_API + '/msgTemplateRule/saveOrUpdate',
    method: 'post',
    data
  })
}

export const selectRuleById = (id) => {
  return request({
    url: PREFIX_API + '/msgTemplateRule/selectById',
    method: 'get',
    params: {
      id
    }
  })
}


export const saveOrUpdateParam = (data) => {
  return request({
    url: PREFIX_API + '/msgTemplateParam/saveOrUpdate',
    method: 'post',
    data
  })
}
export const selectParam = (current, size, params) => {
  return request({
    url: PREFIX_API + '/msgTemplateParam/selectList',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}


export const removeParam = (ids) => {
  return request({
    url: PREFIX_API + '/msgTemplateParam/remove',
    method: 'post',
    params: {
      ids
    }
  })
}


export const saveOrUpdate = (data) => {
  return request({
    url: PREFIX_API + '/msgTemplate/saveOrUpdate',
    method: 'post',
    data
  })
}
export const saveOrUpdateChannel = (rows) => {
  return request({
    url: `${PREFIX_API}/msgTemplateChannel/saveOrUpdate`,
    method: 'post',
    data: rows
  })
}

export const selectPageChannel = (current, size, params) => {
  return request({
    url: `${PREFIX_API}/msgTemplateChannel/selectList`,
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const removeChannel = (ids) => {
  return request({
    url: `${PREFIX_API}/msgTemplateChannel/remove`,
    method: 'post',
    params: {
      ids,
    }
  })
}

export const selectPartiesById = (id) => {
  return request({
    url: `${PREFIX_API}/msgTemplateParties/selectById`,
    method: 'get',
    params: {
      id
    }
  })
}

export const getParmsPartiesList = (params) => {
  return request({
    url: `${PREFIX_API}/msgTemplateParam/getList`,
    method: 'get',
    params
  })
}
export const saveOrUpdateParties = (row) => {
  return request({
    url: `${PREFIX_API}/msgTemplateParties/saveOrUpdate`,
    method: 'post',
    data: row
  })
}

export const detailselectPage = (current, size, params) => {
  return request({
    url: PREFIX_API + '/msgSendDetail/selectMsg',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  });
};

export const myMessageDetail = (current, size, params) => {
  return request({
    url: PREFIX_API + '/msgSendDetail/selectMsgByAccount',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  });
};

// -------------


// 分页
export const getPage = (data) => {
  return request({
    url: PREFIX_API + '/template/search',
    method: 'post',
    data
  })
}

// 获取通道App
export const getAppByChannelType = (data) => {
  return request({
    url: PREFIX_API + '/app/getAppByChannelType',
    method: 'post',
    data
  })
}

// 获取消息类型
export const getMessageTypeByChannelType = (data) => {
  return request({
    url: PREFIX_API + '/template/getMessageTypeByChannelType',
    method: 'post',
    data
  })
}

// 新增
export const saveTemplate = (data) => {
  return request({
    url: PREFIX_API + '/template/saveTemplate',
    method: 'post',
    data
  })
}

// 编辑
export const updateById = (data) => {
  return request({
    url: PREFIX_API + '/template/updateById',
    method: 'post',
    data
  })
}

// 删除
export const deleteByIds = (data) => {
  return request({
    url: PREFIX_API + '/template/deleteByIds',
    method: 'post',
    data
  })
}

// 根据id更改模板状态
export const updateStatusById = (data) => {
  return request({
    url: PREFIX_API + '/template/updateStatusById',
    method: 'post',
    data
  })
}

export const getByIds = (ids) => {
  return request({
    url: PREFIX_API + '/template/getByIds',
    method: 'post',
    params: {
      ids: ids
    }
  })
}

export const getById = (id) => {
  return request({
    url: PREFIX_API + '/template/getById',
    method: 'post',
    params: {
      id: id
    }
  })
}

export const getTemplateList = (status) => {
  return request({
    url: PREFIX_API + '/template/getTemplateList',
    method: 'get',
    params: {
      status
    }
  })
}
