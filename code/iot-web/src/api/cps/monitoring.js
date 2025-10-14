import request from "@/utils/request";

const CONTROLLER_NAME = "monitoring";

/**
 * 查询数采监控的历史记录
 */
export const selectData = (row,distinction) => {
  return request({
    url: `cps/${CONTROLLER_NAME}/selectData`,
    method: 'get',
    params: {
      ...row,
      distinction,
    }
  })
}

/**
 * 点位反控
 */
export const pointControl = (data) => {
  return request({
    url: `cps/client/pointControl`,
    method: 'post',
    data: [data]
  })
}
