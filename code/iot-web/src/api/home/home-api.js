import request from '@/utils/request.js'

/**
 * 获取产品、设备和驱动数量
 */
export const getCpsCount = () => {
  return request({
    url: `cps/home/count`,
    method: 'get',
  })
}

/**
 * 获取告警记录相关数据
 */
export const getWarningCount = () => {
  return request({
    url: `warning/home/count`,
    method: 'get'
  })
}

/**
 * 获取设备关联告警数
 */
export const getEquipmentRelateWarning = () => {
  return request({
    url: `warning/home/getEquipmentRelateWarning`,
    method: 'get'
  })
}

/**
 * 获取消息总数
 */
export const getRecordCount = () => {
  return request({
    url: `message/dashboard/getRecordCount`,
    method: 'get'
  })
}

/**
 * 获取EMQX列出监视器数据
 * @returns {*}
 */
export const getEmqxMonitor = () => {
  return request({
    url: 'cps/home/getEmqxMonitor',
    method: 'get'
  })
}
