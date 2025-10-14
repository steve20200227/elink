import request from '@/utils/request'





// 获得消息记录分页
export function getRecordPage(params) {
  return request({
    url: '/message/record/getPageList',
    method: 'get',
    params
  })
}

