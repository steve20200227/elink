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
              <el-form-item label="配置名称:" prop="actionName">
                <el-input v-model="dataForm.actionName" :disabled="type == 'view'" size="small"
                          placeholder="请输入配置名称"></el-input>
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
                <el-col :span="6">
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
                <el-col :span="18">
                  <el-form-item label="URL请求路径:" label-width="110px" prop="http.requestPath">
                    <el-input v-model="dataForm.http.requestPath" :disabled="type == 'view'" size="small"
                              placeholder="请输入请求路径"></el-input>
                  </el-form-item>
                </el-col>

              </el-row>

              <el-row>
                <el-tabs  v-model="activeName" @tab-click="tabClick"	>
                  <el-tab-pane label="Params" name="Params">
                    <el-table
                      :data="dataForm.http.requestParams"
                      style="width: 100%">
                      <el-table-column
                        prop="key"
                        label="key">
                        <template v-slot="{ row, $index }">
                          <el-input placeholder="Key" v-model="row.key" @blur="paramsBlur(row,$index)"></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column
                        prop="value"
                        label="value">
                        <template v-slot="{ row, $index }">
                          <el-input placeholder="Value" v-model="row.value" @blur="paramsBlur(row,$index)"></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column
                        label="操作"
                        width="100">
                        <template slot-scope="scope">
                          <el-button type="text" size="small" v-if="scope.$index != dataForm.http.requestParams.length -1" @click="paramsDeleteRow(scope.row,scope.$index)">删除</el-button>
                        </template>
                      </el-table-column>
                    </el-table>


                  </el-tab-pane>
                  <el-tab-pane label="Headers" name="Headers">

                    <el-table
                      :data="dataForm.http.requestHeaders"
                      style="width: 100%">
                      <el-table-column
                        prop="key"
                        label="key">
                        <template v-slot="{ row, $index }">
                          <el-input placeholder="Key" v-model="row.key" @blur="headersBlur(row,$index)"></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column
                        prop="value"
                        label="value">
                        <template v-slot="{ row, $index }">
                          <el-input placeholder="Value" @blur="headersBlur(row,$index)" v-model="row.value"></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column
                        label="操作"
                        width="100">
                        <template slot-scope="scope">
                          <el-button type="text" size="small" v-if="scope.$index != dataForm.http.requestHeaders.length -1" @click="headersDeleteRow(scope.row,scope.$index)">删除</el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </el-tab-pane>
                  <!--        Body          -->
                  <el-tab-pane label="Body" name="Body" :lazy="false">

                    <div style="padding: 0 20px;">
                      <div id="codeEditor" style="height: 400px; width: 100%; margin-top: 10px; border-right: 2px solid #ebebeb"></div>
                    </div>
                  </el-tab-pane>
                </el-tabs>
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
                   v-if="(dataForm.outputWay === 'HTTP') && (type != 'view') " type="primary"
                   @click="testHttpConnection">测试连接
        </el-button>
        <el-button style="margin-top: 12px"
                   v-if="type != 'view'"
                   type="primary" @click="determine">保存
        </el-button>
      </div>

    </el-dialog>
    <el-dialog
      title="响应结果"
      class="avue-dialog avue-dialog--top"
      :visible.sync="responseDialog"
      width="50%"
      >
      <div style="height: 500px" class="routeManagement">
        <vue-json-editor
          v-model="responseData"
          currentMode="code"
          :modeList="modeList"
          :option="options"
          :showBtns="false"
          language="cn"
          @json-change="jsonChange"
          @has-error="onError"
          :mode="'code'"/>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {submit, detail, testConnection,testHttpConnection} from "@/api/warning/warning-north-api";
import {getPage as getChannelPage} from "@/api/message/channel";
import {listSimpleUsers} from '@/api/system/user'
import {VueEditor} from "vue2-editor";
import 'ace-builds/src-noconflict/ace.js';
import 'ace-builds/src-noconflict/mode-json.js';
import 'ace-builds/src-noconflict/theme-chrome.js';
import 'ace-builds/src-noconflict/ext-language_tools.js';
import vueJsonEditor from 'vue-json-editor'

export default {
  name: "actionAdd",
  components: {
    VueEditor,
    vueJsonEditor
  },
  data() {
    return {
      modeList: [
        'code'
      ],
      options: {
        search: false,
        history: false,
      },
      hasJsonFlag: true, // json是否验证通过 （true为通过，false为失败）

      responseData:"",
      responseDialog:false,
      activeName:"Params",
      tagValueList: {},
      isClear: false,
      inputValue: '',
      inputVisible: false,
      // 用户列表
      users: [],
      type: 'add',
      configData: [],
      templateDisabled: true,
      channelList: [],
      templateList: [],
      title: '配置信息',
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
          requestParams:[
            {
              key:"",
              value:""
            }
          ],
          requestHeaders:[
            {
              key:"",
              value:""
            }
          ],
          requestBody:""
        },
      },
      rules: {

        actionName: [
          {
            required: true,
            message: "请输入配置名称",
            trigger: 'blur'
          },
          { max: 50, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
        ],
        actionType: [
          {
            required: true,
            message: "请选择输出方式",
            trigger: 'blur change'
          }
        ],
        outputWay: [
          {
            required: true,
            message: "请选择输出方式",
            trigger: 'blur change'
          }
        ],
        'mqtt.mqttAddress': [
          {
            required: true,
            message: "请输入MQTT地址",
            trigger: 'blur change'
          }
        ],
        'mqtt.clsId': [
          {
            required: true,
            message: "请输入客户端ID",
            trigger: 'blur change'
          }
        ],
        'mqtt.userName': [
          {
            required: true,
            message: "请输入用户名",
            trigger: 'blur change'
          },
          { max: 50, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
        ],
        'mqtt.passWord': [
          {
            required: true,
            message: "请输入密码",
            trigger: 'blur change'
          },
          { max: 50, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
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
            message: "请选择是否保留",
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
          },
          { max: 500, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
        ],
        'http.requestMethod': [
          {
            required: true,
            message: "请选择请求方法",
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
    // JSON配置改变事件
    jsonChange(value) {
      this.hasJsonFlag = true
    },
    onError(value) {
      this.hasJsonFlag = false
    },

    initializeEditor(value) {
      const editor = window.ace.edit("codeEditor");
      editor.setTheme("ace/theme/chrome");
      editor.session.setMode("ace/mode/json");
      editor.setOptions({
        fontSize: "16px",
      });
      if (value) {
        editor.setValue(value, -1);
      }
    },
    headersDeleteRow(row,index){
      //如果删到最后一个 则给个空的数据
      if (this.dataForm.http.requestHeaders.length == 1){
        this.dataForm.http.requestHeaders=[{
          key:"",
          value:""
        }]
      }else {
        this.dataForm.http.requestHeaders.splice(index,1)
      }

    },
    headersBlur(row,index){
      if (index+1 == this.dataForm.http.requestHeaders.length){
        if ((row.key != null && row.key !== '') || (row.value != null && row.value !== '')){
          this.dataForm.http.requestHeaders.push({
            key:"",
            value:""
          })
        }
      }
    },
    paramsDeleteRow(row,index){
      //如果删到最后一个 则给个空的数据
      if (this.dataForm.http.requestParams.length == 1){
        this.dataForm.http.requestParams=[{
          key:"",
          value:""
        }]
      }else {
        this.dataForm.http.requestParams.splice(index,1)
      }

    },
    paramsBlur(row,index){
      if (index+1 == this.dataForm.http.requestParams.length){
        if ((row.key != null && row.key !== '') || (row.value != null && row.value !== '')){
          this.dataForm.http.requestParams.push({
            key:"",
            value:""
          })
        }
      }

      this.dataForm.http.requestPath = this.dataForm.http.requestPath.split("?")[0] + this.convertParamUrl(this.dataForm.http.requestParams)
    },

    convertParamUrl(requestParams){

      let urlParam = ""
      for (let i = 0; i < requestParams.length - 1; i++) {
        if (i == 0){
          urlParam += "?"
        }

        urlParam += encodeURIComponent(requestParams[i].key) + '=' + encodeURIComponent(requestParams[i].value)
        if (requestParams.length-2 != i){
          urlParam += "&"
        }
      }
      return urlParam;
    },

    testHttpConnection(){
      const http = this.dataForm.http
      testHttpConnection(http).then(res => {
       try{
         this.responseData = JSON.parse(res.data)
       }catch (e) {
         this.responseData = res
       }
       this.$message.success("测试成功")
        this.responseDialog = true
      }).catch(error =>{
        this.responseData = error.message
        this.responseDialog = true
      })
    },
    tabClick(tab, event){
      if (this.activeName == "Body"){
        this.$nextTick(() =>{
          this.initializeEditor();
        })
      }

    },
    //从dataForm中获取配置参数 构建并设置 relatedParameter 到 dataForm
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
    addPoint() {
      //初始化 HTTP 数据
      this.dataForm.http = {
        requestPath: '',
        requestMethod: '',
        requestParams:[
          {
            key:"",
            value:""
          }
        ],
        requestHeaders:[
          {
            key:"",
            value:""
          }
        ],
        requestBody:""
      }
      this.title = "新增配置";
      this.type = 'add'
      this.dialogFlag = true;
    },
    // 编辑回显
    async editPoint(id) {
      let that = this
      this.type = 'edit'
      this.title = "配置信息编辑";
      await detail(id).then(async res => {

        this.dataForm = res.data
        if (this.dataForm.outputWay == 'HTTP') {
          if (this.dataForm.http.requestParams == null ){
            this.dataForm.http.requestParams = [{ key:"", value:"" }]
          }
          if (this.dataForm.http.requestHeaders == null ){
            this.dataForm.http.requestHeaders = [{ key:"", value:"" }]
          }
          this.$nextTick(() => {
            that.initializeEditor(this.dataForm.http.requestBody);
          })

          this.dataForm.mqtt = {
            mqttAddress: '',
            clsId: '',
            userName: '',
            passWord: '',
            qos: null,
            retain: '',
            theme: '',
          }
        }
        if (this.dataForm.outputWay == 'MQTT') {
          this.dataForm.http = {
            requestPath: '',
            requestMethod: '',
            requestParams:[
              {
                key:"",
                value:""
              }
            ],
            requestHeaders:[
              {
                key:"",
                value:""
              }
            ],
            requestBody:""
          }
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
      this.title = "配置信息";
      await detail(id).then(async res => {

        this.dataForm = res.data
        this.templateDisabled = false

      })
      this.dialogFlag = true
      this.active = 0
    },
    // 确认按钮
    determine() {
      //单独处理 HTTP body的参数
      if (this.dataForm.outputWay === 'HTTP'){
        const editor = window.ace.edit("codeEditor");
        let outputParameter = editor.getValue();
        this.dataForm.http.requestBody = outputParameter
      }
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //目前业务中动作分为三大类  1.发消息 2.设备反控 3.北向输出  此处无论是mqtt http均为3
          this.dataForm.actionType = 3;
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

<style lang="scss" scoped>
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
.routeManagement{
  width:98%;
  margin:16px auto;
}
  .routeManagement ::v-deep .jsoneditor-vue{
    height:500px;
  }
  .routeManagement ::v-deep .json-save-btn{
    cursor: pointer;
}
</style>
