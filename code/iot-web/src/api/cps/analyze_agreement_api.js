import request from '@/utils/request.js'

const CONTROLLER_NAME = "analyzeAgreement";

/**
 * 获取接入协议
 */
export const getAnalyzeAgreement = () => {
  return request({
    url: `cps/analyzeAgreement/getAnalyzeAgreement`,
    method: 'get',
  })
}
/**
 * 获取接入协议分页
 */
export const getAnalyzeAgreementPage = (pageNo, pageSize, params) => {
  return request({
    url: `cps/analyzeAgreement/page`,
    method: 'get',
    params: {
      ...params,
      pageNo,
      pageSize,
    }
  })
}
/**
 * 删除接入协议
 */
export const remove = (id) => {
  return request({
    url: `cps/analyzeAgreement/remove?id=`+id,
    method: 'post',
  })
}
/**
 * 保存接入协议
 */
export const submit = (data) => {
  return request({
    url: `cps/analyzeAgreement/submit`,
    method: 'post',
    data: data
  })
}
