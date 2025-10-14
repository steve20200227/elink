export const aliyunConfigData=[
  {
    label:"电话号码配置",
    prop:"users",
    type:"tag",
    placeholder:"请输入电话号码"
  }
]


export const tencentConfigData=[
  {
    label:"电话号码配置",
    prop:"users",
    type:"tag",
    placeholder:"请输入电话号码"
  }
]

export const systemMessageData=[
  {
    label:"接收人列表",
    prop:"users",
    type:"select",
    placeholder:"请选择用户"
  }
]

export const mailConfigData=[
  {
    label:"收件人邮箱配置",
    prop:"users",
    isShow:false,
    type:"tag",
    placeholder:"请输入收件人邮箱"
  },
  {
    label:"抄送人",
    prop:"toCC",
    isShow:false,
    type:"tag",
    placeholder:"请输入抄送人邮箱"
  },
  {
    label:"密送",
    prop:"toBCC",
    isShow:false,
    type:"tag",
    placeholder:"请输入密送人邮箱"
  },
  {
    label:"邮件标题",
    prop:"title",
    type:"input",
    disabled:true,
    placeholder:"请输入邮箱标题"
  },
  {
    label:"邮件内容",
    prop:"content",
    type:"editor",
    disabled:true,
    placeholder:"请输入邮件内容"
  }
]
