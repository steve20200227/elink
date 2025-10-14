<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="60%"
      @close="cancel"
    >
      <div style="margin: 10px 70px;">
        <el-form :rules="rules" ref="ruleForm" :model="dataForm" label-width="120px" @submit.native.prevent
                 class="product-form">
          <!--     北向输出       -->
          <div>
            <div>
              <el-form-item label="北向输出名称:" prop="actionName">
                <el-input v-model="dataForm.actionName" :disabled="type == 'view'" size="small"
                          placeholder="请输入北向输出名称"></el-input>
              </el-form-item>
              <el-form-item label="输出方式:" prop="outputWay">
                <el-select v-model="dataForm.outputWay" :disabled="type == 'view'" size="small" style="width: 100%"
                           placeholder="请选择输出方式">
                  <el-option
                    v-for="item in outputWayDict"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </div>
            <!--     MQTT       -->
            <div v-if="dataForm.outputWay === 'MQTT'"
                 style="padding-top: 10px; border-top: 1px solid rgb(204, 204, 204)">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="MQTT地址:" prop="mqtt.mqttAddress">
                    <el-input v-model="dataForm.mqtt.mqttAddress" :disabled="type == 'view'" size="small"
                              placeholder="例如：tcp://127.0.0.1:1883"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="客户端ID:" prop="mqtt.clsId">
                    <el-input v-model="dataForm.mqtt.clsId" :disabled="type == 'view'" size="small"
                              placeholder="请输入客户端ID"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="用户名:" prop="mqtt.userName">
                    <el-input v-model="dataForm.mqtt.userName" :disabled="type == 'view'" size="small"
                              placeholder="请输入账号"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="密码:" prop="mqtt.passWord">
                    <el-input v-model="dataForm.mqtt.passWord" :disabled="type == 'view'" size="small"
                              placeholder="请输入密码"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="QOS:" prop="mqtt.qos">
                    <el-select v-model="dataForm.mqtt.qos" :disabled="type == 'view'" size="small" style="width: 100%"
                               placeholder="请选择QOS">
                      <el-option
                        v-for="item in mqttQosDict"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="是否保留:" prop="mqtt.retain">
                    <el-select v-model="dataForm.mqtt.retain" :disabled="type == 'view'" size="small"
                               style="width: 100%" placeholder="请输入是否保留">
                      <el-option
                        v-for="item in retainDict"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="发送主题:" prop="mqtt.theme">
                    <el-input v-model="dataForm.mqtt.theme" :disabled="type == 'view'" size="small"
                              placeholder="请输入发送主题"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            <!--     HTTP       -->
            <div v-if="dataForm.outputWay === 'HTTP'"
                 style="padding-top: 10px; border-top: 1px solid rgb(204, 204, 204)">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="URL请求路径:" label-width="110px" prop="http.requestPath">
                    <el-input v-model="dataForm.http.requestPath" :disabled="type == 'view'" size="small"
                              placeholder="请输入请求路径"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="请求方法:" prop="http.requestMethod">
                    <el-select v-model="dataForm.http.requestMethod" :disabled="type == 'view'" size="small"
                               style="width: 100%" placeholder="请选择请求方法">
                      <el-option
                        v-for="item in httpRequestDict"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-form>
      </div>

      <div class="dialog-footer">
        <el-button style="margin-top: 12px"
                   v-if="(dataForm.outputWay === 'MQTT') && (type != 'view') " type="primary"
                   @click="testConnection">测试连接
        </el-button>
        <el-button style="margin-top: 12px"
                   v-if="type != 'view'"
                   type="primary" @click="determine">保存
        </el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import {submit, detail, testConnection} from "@/api/warning/warning-north-api";

import {getPage as getTemplatePage} from "@/api/message/template";
import {getPage as getChannelPage} from "@/api/message/channel";
import {
  aliyunConfigData,
  tencentConfigData,
  mailConfigData,
  systemMessageData
} from '@/views/business/warning/action/messageData/messageConfigData'
import {listSimpleUsers} from '@/api/system/user'
import {VueEditor} from "vue2-editor";

export default {
  name: "actionView",
  components: {
    VueEditor
  },
  data() {
    return {
      tagValueList: {},
      isClear: false,
      inputValue: '',
      inputVisible: false,
      // 用户列表
      users: [],
      type: 'add',
      configData: [],
      msgTemplate: {},
      msgChannel: {},
      templateDisabled: true,
      channelList: [],
      templateList: [],
      title: '北向输出配置',
      dialogFlag: false,
      messageShow: false,
      actionDict: [],
      mqttQosDict: [],
      retainDict: [],
      httpRequestDict: [],
      outputWayDict: [],
      active: 0,
      dataForm: {
        actionName: '',
        actionType: '',
        outputWay: '',
        message: {
          msgChannel: '',
          msgTemplate: '',
          users: ''
        },
        mqtt: {
          mqttAddress: '',
          clsId: '',
          userName: '',
          passWord: '',
          qos: null,
          retain: '',
          theme: '',
        },
        http: {
          requestPath: '',
          requestMethod: '',
        },
      },
      rules: {
        'message.msgTemplate': [
          {
            required: true,
            message: '请选择消息模板',
            trigger: 'blur'
          }
        ],
        actionName: [
          {
            required: true,
            message: "请输入北向输出名称",
            trigger: 'blur'
          },
        ],
        actionType: [
          {
            required: true,
            message: "请选择北向输出类型",
            trigger: 'blur change'
          }
        ],
        outputWay: [
          {
            required: true,
            message: "请选择输出类型",
            trigger: 'blur change'
          }
        ],
        'mqtt.mqttAddress': [
          {
            required: true,
            message: "请选择输出类型",
            trigger: 'blur change'
          }
        ],
        'mqtt.clsId': [
          {
            required: true,
            message: "请输入客户端地址",
            trigger: 'blur change'
          }
        ],
        'mqtt.userName': [
          {
            required: true,
            message: "请输入用户名",
            trigger: 'blur change'
          }
        ],
        'mqtt.passWord': [
          {
            required: true,
            message: "请输入密码",
            trigger: 'blur change'
          }
        ],
        'mqtt.qos': [
          {
            required: true,
            message: "请输入QOS",
            trigger: 'blur change'
          }
        ],
        'mqtt.retain': [
          {
            required: true,
            message: "请输入是否保留",
            trigger: 'blur change'
          }
        ],
        'mqtt.theme': [
          {
            required: true,
            message: "请输入主题",
            trigger: 'blur change'
          }
        ],
        'http.requestPath': [
          {
            required: true,
            message: "请输入请求路径",
            trigger: 'blur change'
          }
        ],
        'http.requestMethod': [
          {
            required: true,
            message: "请输入请求方法",
            trigger: 'blur change'
          }
        ],
      },
    }
  },
  created() {
    //获取通道列表渲染
    let param = {
      pageNum: 1,
      pageSize: 999999
    }
    getChannelPage(param).then(res => {
      this.channelList = res.data.records
    })

    // 获得用户列表
    listSimpleUsers().then(response => {
      this.users = response.data;
    })
  },
  mounted() {
    this.actionDict = this.$store.state.dict.dictDatas.action_type
    this.mqttQosDict = this.$store.state.dict.dictDatas.mqtt_qos
    this.retainDict = this.$store.state.dict.dictDatas.is_retain
    this.outputWayDict = this.$store.state.dict.dictDatas.output_way
    this.httpRequestDict = this.$store.state.dict.dictDatas.request_method
  },
  methods: {
    handleClose(tag, prop) {
      this.dataForm.message[prop].splice(this.dataForm.message[prop].indexOf(tag), 1);
    },

    showInput(prop) {
      for (let i = 0; i < this.configData.length; i++) {
        if (this.configData[i].prop == prop) {
          this.$set(this.configData[i], 'isShow', !this.configData[i].isShow)
          //初始化值
          this.$set(this.tagValueList, prop, '')

        }
      }

      //this.inputVisible = true;
      this.$nextTick(_ => {

        this.$refs[prop][0].$refs.input.focus();
      });
    },

    handleInputConfirm(prop) {


      if (!this.dataForm.message[prop] || this.dataForm.message[prop] == "") {
        this.dataForm.message[prop] = []
      }


      if (this.tagValueList[prop]) {
        this.dataForm.message[prop].push(this.tagValueList[prop]);
      }
      for (let i = 0; i < this.configData.length; i++) {
        if (this.configData[i].prop == prop) {
          this.$set(this.configData[i], 'isShow', false)

        }
      }
      this.tagValueList[prop] = '';
    },

    usersChange(val) {
    },
    //从dataForm中获取配置参数 构建并设置 relatedParameter 到 dataForm

    getConfigData(channelType, relatedParameter) {
      switch (channelType) {
        case 0: {
          return systemMessageData
        }
        //打电话
        case 1: {
          if (relatedParameter.callProvider == "aliyun") {
            return aliyunConfigData
          }
          if (relatedParameter.callProvider == "tencent") {
            return tencentConfigData
          }
        }
          break
        case 2: {
          if (relatedParameter.smsProvider == "aliyun") {
            return aliyunConfigData
          }
          if (relatedParameter.smsProvider == "tencent") {
            return tencentConfigData
          }
        }
          break
        case 3:
          return mailConfigData;
          break
        case 4:
        case 5:
        case 6:
        default :
          return []
      }
    },

    selectTemplate(templateVal) {
      let that = this
      this.templateList.filter(item => {
        if (item.templateId == templateVal) {
          that.msgTemplate = item
          return item
        }
      })
      this.$set(this.dataForm.message, 'msgTemplate', templateVal)
      this.dataForm.message.templateId = templateVal


      //先判断渠道类型
      let pushWays = JSON.parse(this.msgTemplate.pushWays)
      this.dataForm.outputWay = pushWays.channelType

      let relatedParameter = JSON.parse(this.msgTemplate.relatedParameter)

      //设置通道id和模板id
      relatedParameter.msgChannel = this.msgChannel.appId
      relatedParameter.msgTemplate = this.msgTemplate.templateId
      relatedParameter.templateId = this.msgTemplate.templateId

      this.dataForm.relatedParameter = relatedParameter

      //获取对应配置参数
      this.configData = this.getConfigData(pushWays.channelType, relatedParameter)

      this.isClear = true

    },

    async initTemplateList(channelId) {
      let param = {
        pageNum: 1,
        pageSize: 999999
      }
      //暂时这样过滤  后面写接口
      await getTemplatePage(param).then(res => {
        this.templateList = res.data.records.filter(item => {
          if (item.appId == channelId) {
            return item
          }
        })
        //this.msgChannel=channelId
        if (this.templateList.length > 0) {
          this.templateDisabled = false

        } else {
          this.templateDisabled = true
          //this.dataForm.msgTemplate=null
        }
      })
    },
    selectChannel(channelVal) {

      let that = this
      this.$set(this.dataForm.message, 'msgTemplate', null)
      let param = {
        pageNum: 1,
        pageSize: 999999
      }
      //暂时这样过滤  后面写接口
      getTemplatePage(param).then(res => {
        this.templateList = res.data.records.filter(item => {
          if (item.appId == channelVal) {
            return item
          }
        })
        this.channelList.filter(item => {
          if (item.appId == channelVal) {
            that.msgChannel = item
          }
        })
        if (this.templateList.length > 0) {
          this.templateDisabled = false

        } else {
          this.templateDisabled = true
          //this.dataForm.msgTemplate=null
        }

      })


    },
    // 选择完消息模板后，将模板名称赋值给北向输出表单
    // selectMessageTemplate(row) {
    //   this.$set(this.dataForm, 'messageName', row.templateName)
    //   this.dataForm.messageId = row.id
    // },
    /*    // 改变北向输出类型
        actionChange() {
          this.dataForm.mqtt = {}
          this.dataForm.http = {}
          this.dataForm.outputWay = ""
        },
        // 改变输出类型
        outputWayChange() {
          this.dataForm.mqtt = {}
          this.dataForm.http = {}
        },*/
    testConnection() {
      const mqtt = this.dataForm.mqtt
      if (mqtt.mqttAddress === '' || mqtt.clsId === '' || mqtt.userName === '' || mqtt.passWord === '') {
        this.$message.warning("参数不能为空")
        return
      }
      let data = {}
      data.mqttAddress = mqtt.mqttAddress
      data.clsId = mqtt.clsId
      data.userName = mqtt.userName
      data.passWord = mqtt.passWord
      testConnection(data).then(res => {
        if (res.code === 0) {
          this.$message.success("测试成功")
        }
      })
    },
    previous() {
      this.active--;
    },
    next() {
      switch (this.active) {
        case 0:
          this.active1();
          break;
        case 1:
          this.active2();
          break;
        case 2:
          this.active3();
          break;
      }
    },
    active1() {
      // 判断北向输出名称和北向输出类型是否为空
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          //在去下一步前对需要使用的状态进行初始化
          if (this.dataForm.actionType == '1' && this.dataForm.message == null) {
            this.dataForm.message = {
              msgChannel: '',
              msgTemplate: '',
              users: ''
            }
          }
          this.isClear = false
          this.active++
        }
      })
    },
    active2() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          //隐藏tag输入框 显示新增按钮
          this.inputVisible = false;
          if (this.msgTemplate.pushWays && this.msgTemplate.relatedParameter) {
            let pushWays = JSON.parse(this.msgTemplate.pushWays)
            let relatedParameter = JSON.parse(this.msgTemplate.relatedParameter)

            this.configData = this.getConfigData(pushWays.channelType, relatedParameter)
            this.dataForm.relatedParameter.channelType = pushWays.channelType
            //获取 模板处的json  对当前json进行属性赋值 以保持模板变动这边跟着变动
            this.setConfigDataReverse(relatedParameter, this.isClear)

            //设置保存时的json
            this.setNotContainConfigData(relatedParameter)
            //将relatedParameter
          }


          this.active++
        }
      })

    },
    addPoint() {
      this.title = "北向输出新增";
      this.type = 'add'
      this.dialogFlag = true;
    },

    setConfigData(relatedParameter) {
      if (this.configData.length > 0) {
        for (let i = 0; i < this.configData.length; i++) {
          relatedParameter[this.configData[i].prop] = this.dataForm.message[this.configData[i].prop]
        }

      }

    },
    setNotContainConfigData(relatedParameter) {
      let configDataProp = []
      for (let i = 0; i < this.configData.length; i++) {
        configDataProp.push(this.configData[i].prop)
      }
      if (relatedParameter) {
        // 使用for...in遍历可枚举属性
        for (const prop in relatedParameter) {
          if (!configDataProp.includes(prop)) {
            this.dataForm.relatedParameter[prop] = relatedParameter[prop]
          }
        }
      }
    },
    //新增回显
    setConfigDataReverse(relatedParameter, isClear) {
      if (this.configData.length > 0) {
        for (let i = 0; i < this.configData.length; i++) {
          //回显不需要编辑  不设置响应式  只设置relatedParameter有值的属性  说明该属性从模板取出来  可以空掉users自定义属性
          if (isClear) {
            if (!this.configData[i].disabled) {
              this.$set(this.dataForm.message, this.configData[i].prop, "")
              //this.dataForm.message[this.configData[i].prop] = ""
            } else {
              this.$set(this.dataForm.message, this.configData[i].prop, relatedParameter[this.configData[i].prop])
              //this.dataForm.message[this.configData[i].prop] = relatedParameter[this.configData[i].prop]
            }
          } else {
            if (relatedParameter[this.configData[i].prop]) {
              this.$set(this.dataForm.message, this.configData[i].prop, relatedParameter[this.configData[i].prop])
              //this.dataForm.message[this.configData[i].prop] = relatedParameter[this.configData[i].prop]
            }
          }


        }

      }

    },
    // 编辑回显
    async editPoint(id) {
      let that = this
      this.type = 'edit'
      this.title = "北向输出编辑";
      await detail(id).then(async res => {

        this.dataForm = res.data
        if (this.dataForm.actionType == '1') {
          this.dataForm.relatedParameter = JSON.parse(this.dataForm.relatedParameter)
          await this.initTemplateList(this.dataForm.message.msgChannel)
          this.templateList.filter(template => {
            if (template.templateId == that.dataForm.message.msgTemplate) {
              that.msgTemplate = template
            }
          })

          this.channelList.filter(item => {
            if (item.appId == that.dataForm.message.msgChannel) {
              that.msgChannel = item
            }
          })

        }
        this.templateDisabled = false

        // this.actionTypeChange()
      })
      this.dialogFlag = true
      this.active = 0
    },
    // 查看回显
    async viewPoint(id) {
      let that = this
      this.type = 'view'
      this.title = "北向输出查看";
      await detail(id).then(async res => {

        this.dataForm = res.data
        if (this.dataForm.actionType == '1') {
          this.dataForm.relatedParameter = JSON.parse(this.dataForm.relatedParameter)
          await this.initTemplateList(this.dataForm.message.msgChannel)
          this.templateList.filter(template => {
            if (template.templateId == that.dataForm.message.msgTemplate) {
              that.msgTemplate = template
            }
          })

          this.channelList.filter(item => {
            if (item.appId == that.dataForm.message.msgChannel) {
              that.msgChannel = item
            }
          })

        }
        this.templateDisabled = false

        // this.actionTypeChange()
      })
      this.dialogFlag = true
      this.active = 0
    },
    // 确认按钮
    determine() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.dataForm.actionType = 3; // 先按原先逻辑固定死为北向输出逻辑；
          submit(this.dataForm).then(res => {
            if (res.code === 0) {
              this.$message.success("操作成功")
              this.cancel();
            } else {
              this.$message.warning(res.msg);
            }
          })
        }
      })
    },
    cancel() {
      this.active = 0
      this.messageShow = false
      this.dialogFlag = false;
      //初始化dataForm
      this.dataForm = {
        actionName: '',
        actionType: '',
        outputWay: '',
        message: {
          msgChannel: '',
          msgTemplate: '',
          users: ''
        },
        mqtt: {
          mqttAddress: '',
          clsId: '',
          userName: '',
          passWord: '',
          qos: null,
          retain: '',
          theme: '',
        },
        http: {
          requestPath: '',
          requestMethod: '',
        },
      };
      this.$refs['ruleForm'].clearValidate();
      this.$emit('restOnLoad');
    },
  }
}
</script>

<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

.input-new-tag {
  width: 140px;
  margin-left: 10px;
  vertical-align: bottom;
}

.product-form ::v-deep .el-form-item__label {
  color: #3f4448;
  font-weight: 400;
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: center;
  padding: 12px 0;
  border-top: 1px solid rgb(204, 204, 204);
}

.center-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 30px;
}
</style>
