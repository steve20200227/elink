import request from '@/utils/request'
import axios from "axios";
const PREFIX_API = '/xxljob'
// 查询定时任务调度列表
export function listJob(query) {
  return request({
    url: PREFIX_API+'/jobinfo/pageList',
    method: 'get',
    params: query
  })
}

// 查询定时任务调度详细
export function getJob(jobId) {
  return request({
    url: '/infra/job/get?id=' + jobId,
    method: 'get'
  })
}

// 新增定时任务调度
export function addJob(data) {
  return request({
    url: PREFIX_API+'/jobinfo/add',
    method: 'post',
    data: data
  })
}

// 修改定时任务调度
export function updateJob(data) {
  return request({
    url: PREFIX_API+'/jobinfo/update',
    method: 'put',
    data: data
  })
}

// 删除定时任务调度
export function delJob(jobId) {
  return request({
    url: PREFIX_API+'/jobinfo/remove?id=' + jobId,
    method: 'delete'
  })
}

// 导出定时任务调度
export function exportJob(query) {
  return request({
    url: '/infra/job/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

// 任务状态修改
export function updateJobStatus(jobId, status) {
  return request({
    url: '/infra/job/update-status',
    method: 'put',
    headers:{
      'Content-type': 'application/x-www-form-urlencoded'
    },
    data: 'id=' + jobId + "&status=" + status,
  })
}

// 定时任务立即执行一次
export function runJob(jobId,executorParam) {
  return request({
    url: PREFIX_API+ '/jobinfo/trigger?id=' + jobId + "&executorParam=" + executorParam,
    method: 'put'
  })
}

// 获得定时任务的下 n 次执行时间
export function getJobNextTimes(data) {
  return request({
    url: PREFIX_API+'/jobinfo/nextTriggerTime',
    method: 'post',
    data
  })
}



// 查询定时任务调度列表
export function listHandlerJob(query) {
  return request({
    url: PREFIX_API+'/jobgroup/pageList',
    method: 'post',
    data: query
  })
}


// 新增定时任务调度
export function addHandlerJob(data) {
  return request({
    url: PREFIX_API+'/jobgroup/save',
    method: 'post',
    data: data
  })
}

// 修改定时任务调度
export function updateHandlerJob(data) {
  return request({
    url: PREFIX_API+ '/jobgroup/update',
    method: 'put',
    data: data
  })
}

// 删除定时任务调度
export function delHandlerJob(jobId) {
  return request({
    url: PREFIX_API+'/jobgroup/remove?id=' + jobId,
    method: 'delete'
  })
}


// 启动
export function handlerStart(jobId) {
  return request({
    url: PREFIX_API+'/jobinfo/start?id=' + jobId,
    method: 'get'
  })
}

// 启动
export function handlerStop(jobId) {
  return request({
    url: PREFIX_API+'/jobinfo/stop?id=' + jobId,
    method: 'get'
  })
}

export function saveOrUpdate(data)  {
  return request({
    url: PREFIX_API+ '/jobinfo/saveOrUpdate',
    method: 'post',
      data
  })
}
