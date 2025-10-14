import request from '@/utils/request.js'

export const getEquipmentTypeData = () => {
  return request({
    url: `cps/cpsPanel/getEquipmentTypeData`,
    method: 'get'
  })
}

export const getAgreementTypeData = () => {
  return request({
    url: `cps/cpsPanel/getAgreementTypeData`,
    method: 'get'
  })
}

export const getProductRelevanceEquipmentData = () => {
  return request({
    url: `cps/cpsPanel/getProductRelevanceEquipmentData`,
    method: 'get'
  })
}

export const getEquipmentWarningData = () => {
  return request({
    url: `cps/cpsPanel/getEquipmentWarningData`,
    method: 'get'
  })
}
