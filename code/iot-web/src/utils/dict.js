/**
 *
 * 数据字典工具类
 */
import store from '@/store'

export const DICT_TYPE = {
  USER_TYPE: 'user_type',
  COMMON_STATUS: 'common_status',

  // ========== SYSTEM 模块 ==========
  SYSTEM_USER_SEX: 'system_user_sex',
  SYSTEM_MENU_TYPE: 'system_menu_type',
  SYSTEM_ROLE_TYPE: 'system_role_type',
  SYSTEM_DATA_SCOPE: 'system_data_scope',
  SYSTEM_NOTICE_TYPE: 'system_notice_type',
  SYSTEM_OPERATE_TYPE: 'system_operate_type',
  SYSTEM_LOGIN_TYPE: 'system_login_type',
  SYSTEM_LOGIN_RESULT: 'system_login_result',
  SYSTEM_ERROR_CODE_TYPE: 'system_error_code_type',
  SYSTEM_OAUTH2_GRANT_TYPE: 'system_oauth2_grant_type',
  SYSTEM_NOTIFY_TEMPLATE_TYPE: 'system_notify_template_type',

  // ========== INFRA 模块 ==========
  INFRA_BOOLEAN_STRING: 'infra_boolean_string',
  INFRA_REDIS_TIMEOUT_TYPE: 'infra_redis_timeout_type',
  INFRA_JOB_STATUS: 'infra_job_status',
  INFRA_JOB_STATUS_CN: 'infra_job_status_cn',
  INFRA_JOB_LOG_STATUS: 'infra_job_log_status',
  INFRA_API_ERROR_LOG_PROCESS_STATUS: 'infra_api_error_log_process_status',
  INFRA_CONFIG_TYPE: 'infra_config_type',
  INFRA_CODEGEN_TEMPLATE_TYPE: 'infra_codegen_template_type',
  INFRA_CODEGEN_FRONT_TYPE: 'infra_codegen_front_type',
  INFRA_CODEGEN_SCENE: 'infra_codegen_scene',
  INFRA_FILE_STORAGE: 'infra_file_storage',


  // ========== business - cps 模块 ==========
  IS_ENABLE: 'is_enable', // 是否启用
  READ_WRITE: 'read_write', // 读/写
  WRITE_INSTRUCTION: 'write_instruction', // 写指令
  READ_INSTRUCTION: 'read_instruction', // 读指令
  ATTRIBUTE_DATA_TYPE: 'attribute_data_type', // 属性（点位）数据类型
  AGREEMENT_TYPE: 'agreement_type', // 接入协议
  SWITCH_ON_INPUT_TYPE: 'switch_on_input_type', // 接入认证-输入类型
  ACCESS_EVENT: 'access_event', // 接入认证-输入类型
  MOVEMENT_TYPE: 'movement_type', // 接入认证-活动类型
  PAYLOAD: 'payload', // 接入认证-有效载荷指示器
  ACCESS_PARSING_PROTOCOL: 'access_parsing_protocol', // 接入认证-有效载荷指示器

  // ========== business - WARNING 模块 ==========
  RULE_CONDITION_TYPE: 'rule_condition_type', // 规则条件类型
  RULE_TYPE: 'rule_type', // 规则类型
  ACTION_TYPE: 'action_type', // 动作类型
  PROCESSING_TYPE: 'processing_type', // 处理类型
  WARNING_PRIORITY: 'warning_priority', // 告警优先级
  RECORD_STATUS: 'record_status', // 告警记录状态
  COMPUTE_TYPE: 'compute_type', // 计算类型
  ACTION_OUTPUT_WAY: 'action_output_way', // 动作输出方式
  // ========== business - message 模块 ==========
  TEMPLATE_MODE: 'template_mode',
  MESSAGE_YES_OR_NO: 'yes_or_no',
  SEND_STATUS: 'send_status',
  MESSAGE_PUSH_METHOD: 'message_push_method', // 消息推送方式
  MESSAGE_USER_TYPE: 'message_user_type', // 消息用户类型
  MESSAGE_CHANNEL: 'message_channel', // 消息渠道
  MESSAGE_STATUS: 'message_status', // 消息渠道
}

/**
 * 获取 dictType 对应的数据字典数组
 *
 * @param dictType 数据类型
 * @returns {*|Array} 数据字典数组
 */
export function getDictDatas(dictType) {
  return store.getters.dict_datas[dictType] || []
}

/**
 * 获取 dictType 对应的数据字典数组
 *
 * @param dictType 数据类型
 * @param values 数组、单个元素
 * @returns {*|Array} 数据字典数组
 */
export function getDictDatas2(dictType, values) {
  if (values === undefined) {
    return [];
  }
  // 如果是单个元素，则转换成数组
  if (!Array.isArray(values)) {
    values = [this.value];
  }
  // 获得字典数据
  const results = [];
  for (const value of values) {
    const dict = getDictData(dictType, value);
    if (dict) {
      results.push(dict);
    }
  }
  return results;
}

export function getDictData(dictType, value) {
  // 获取 dictType 对应的数据字典数组
  const dictDatas = getDictDatas(dictType)
  if (!dictDatas || dictDatas.length === 0) {
    return ''
  }
  // 获取 value 对应的展示名
  value = value + '' // 强制转换成字符串，因为 DictData 小类数值，是字符串
  for (const dictData of dictDatas) {
    if (dictData.value === value) {
      return dictData;
    }
  }
  return undefined
}

export function getDictDataLabel(dictType, value) {
  const dict = getDictData(dictType, value);
  return dict ? dict.label : '';
}
