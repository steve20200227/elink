<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      width="40%"
      @close="cancel"
    >
      <div class="center-container">
        <el-steps :active="active" align-center>
          <el-step title="第一步"/>
          <el-step title="第二步"/>
          <el-step title="第三步"/>
        </el-steps>
      </div>
      <div style="margin: 10px 20px;margin-right: 80px;">
        <el-form :rules="rules" ref="ruleForm" :model="dataForm" label-width="140px" class="template-form">
          <!--     步骤一     -->
          <div v-if="active == 0">
            <el-form-item label="模板名称:" prop="templateName">
              <el-input v-model="dataForm.templateName" :disabled="type == 'view'" size="small" placeholder="请输入模板名称"></el-input>
            </el-form-item>
            <el-form-item label="通道类型:" prop="channelType">
              <el-select v-model="dataForm.channelType" :disabled="type == 'view'" @change="changeChannel" size="small" placeholder="请选择通道">
                <el-option
                  v-for="item in channelDict"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value * 1">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="通道选择:" prop="appId">
              <el-select v-model="dataForm.appId" :disabled="(appMessageTypeShow) || (type == 'view')" size="small" placeholder="请选择通道App">
                <el-option
                  v-for="item in channelAppDict"
                  :key="item.appId"
                  :label="item.appName"
                  :value="item.appId * 1">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="消息类型:" prop="messageType">
              <el-select v-model="dataForm.messageType" :disabled="true"  size="small" placeholder="请选择消息类型">
                <el-option
                  v-for="item in messageTypeDict"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="模板状态:" prop="templateStatus">
              <el-switch
                v-model="dataForm.templateStatus"
                :disabled="type == 'view'"
                active-text="已启用"
                :active-value="1"
                :inactive-value="0"
                size="small"
                inactive-text="未启用">
              </el-switch>
            </el-form-item>
          </div>
          <!--     步骤二     -->
          <div v-if="active == 1">
            <!--    站内信     -->
            <div v-if="dataForm.channelType == 0">
              <el-form-item label="模板内容:" prop="relatedParameter.templateContext">
                <div>
                  <el-input v-model="dataForm.relatedParameter.templateContext" type="textarea" :rows="3" :disabled="type == 'view'" size="small" placeholder="请输入模板内容"></el-input>
                </div>
              </el-form-item>
              <el-form-item label="接收人列表:" prop="relatedParameter.userIds">
                <el-select v-model="userData" :disabled="type == 'view'" @change="selectUserIds" multiple placeholder="请选择接收人">
                  <el-option
                    v-for="item in users"
                    :key="item.id + ''"
                    :label="item.nickname"
                    :value="item.id + ''">
                  </el-option>
                </el-select>
              </el-form-item>
            </div>
            <!--    电话     -->
            <div v-if="dataForm.channelType == 1">
              <el-form-item label="运营商:" prop="relatedParameter.callProvider">
                <el-select v-model="dataForm.relatedParameter.callProvider" :disabled="type == 'view'" size="small" placeholder="请选择运营商">
                  <el-option
                    v-for="item in providerTypeDict"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <div v-if="dataForm.relatedParameter.callProvider == 'tencent'">
                <el-form-item label="区域:" prop="relatedParameter.region">
                  <el-input v-model="dataForm.relatedParameter.region" :disabled="type == 'view'" size="small" placeholder="请输入区域"></el-input>
                </el-form-item>
                <el-form-item label="模板ID:" prop="relatedParameter.templateId">
                  <el-input v-model="dataForm.relatedParameter.templateId" :disabled="type == 'view'" size="small" placeholder="请输入模板ID"></el-input>
                </el-form-item>
                <el-form-item label="播放次数:" prop="relatedParameter.playTimes">
                  <el-input v-model="dataForm.relatedParameter.playTimes" :disabled="type == 'view'" size="small" placeholder="请输入播放次数"></el-input>
                </el-form-item>
              </div>
              <div v-if="dataForm.relatedParameter.callProvider == 'aliyun'">
                <el-form-item label="区域:" prop="relatedParameter.region">
                  <el-input v-model="dataForm.relatedParameter.region" :disabled="type == 'view'" size="small" placeholder="请输入区域"></el-input>
                </el-form-item>
                <el-form-item label="模板编码:" prop="relatedParameter.ttsCode">
                  <el-input v-model="dataForm.relatedParameter.ttsCode" :disabled="type == 'view'" size="small" placeholder="请输入模板编码"></el-input>
                </el-form-item>
                <el-form-item label="手机号码:">
                  <el-tag
                    :key="tag"
                    v-for="tag in dynamicTags"
                    :closable = "!type == 'view'"
                    :disable-transitions="false"
                    @close="handleClose(tag)">
                    {{tag}}
                  </el-tag>
                  <el-input
                    class="input-new-tag"
                    v-if="inputVisible"
                    v-model="inputValue"
                    ref="saveTagInput"
                    size="small"
                    @keyup.enter.native="handleInputConfirm"
                    @blur="handleInputConfirm"
                  >
                  </el-input>
                  <el-button v-else class="button-new-tag" :disabled="type == 'view'" size="small" @click="showInput">新 增</el-button>
                </el-form-item>
              </div>
            </div>
            <!--    短信     -->
            <div v-if="dataForm.channelType == 2">
              <el-form-item label="运营商:" prop="relatedParameter.smsProvider">
                <el-select v-model="dataForm.relatedParameter.smsProvider" :disabled="type == 'view'" size="small" placeholder="请选择运营商">
                  <el-option
                    v-for="item in providerTypeDict"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <div v-if="dataForm.relatedParameter.smsProvider == 'tencent'">
                <el-form-item label="区域:" prop="relatedParameter.region">
                  <el-input v-model="dataForm.relatedParameter.region" :disabled="type == 'view'" size="small" placeholder="请输入region"></el-input>
                </el-form-item>
                <el-form-item label="短信AppId:" prop="relatedParameter.smsSdkAppId">
                  <el-input v-model="dataForm.relatedParameter.smsSdkAppId" :disabled="type == 'view'" size="small" placeholder="请输入短信AppId"></el-input>
                </el-form-item>
                <el-form-item label="签名:" prop="relatedParameter.signName">
                  <el-input v-model="dataForm.relatedParameter.signName" :disabled="type == 'view'" size="small" placeholder="请输入签名"></el-input>
                </el-form-item>
                <el-form-item label="模板ID:" prop="relatedParameter.templateId">
                  <el-input v-model="dataForm.relatedParameter.templateId" :disabled="type == 'view'" size="small" placeholder="请输入模板ID"></el-input>
                </el-form-item>
              </div>
              <div v-if="dataForm.relatedParameter.smsProvider == 'aliyun'">
                <el-form-item label="区域:" prop="relatedParameter.region">
                  <el-input v-model="dataForm.relatedParameter.region" :disabled="type == 'view'" size="small" placeholder="请输入区域"></el-input>
                </el-form-item>
                <el-form-item label="签名:" prop="relatedParameter.signName">
                  <el-input v-model="dataForm.relatedParameter.signName" :disabled="type == 'view'" size="small" placeholder="请输入签名"></el-input>
                </el-form-item>
                <el-form-item label="模板编码:" prop="relatedParameter.templateCode">
                  <el-input v-model="dataForm.relatedParameter.templateCode" :disabled="type == 'view'" size="small" placeholder="请输入模板编码"></el-input>
                </el-form-item>
                <el-form-item label="电话号码:">
                  <el-tag
                    :key="tag"
                    v-for="tag in dynamicTags"
                    :closable = "!type == 'view'"
                    :disable-transitions="false"
                    @close="handleClose(tag)">
                    {{tag}}
                  </el-tag>
                  <el-input
                    class="input-new-tag"
                    v-if="inputVisible"
                    v-model="inputValue"
                    ref="saveTagInput"
                    size="small"
                    @keyup.enter.native="handleInputConfirm"
                    @blur="handleInputConfirm"
                  >
                  </el-input>
                  <el-button v-else class="button-new-tag" size="small" @click="showInput">新 增</el-button>
                </el-form-item>
              </div>
            </div>
            <!--    邮件     -->
            <div v-if="dataForm.channelType == 3">
              <el-form-item label="邮件标题:" prop="relatedParameter.title">
                <el-input v-model="dataForm.relatedParameter.title" :disabled="type == 'view'" size="small" placeholder="请输入邮件标题"></el-input>
              </el-form-item>
              <el-form-item label="邮件内容:" prop="relatedParameter.content">
                <el-input v-model="dataForm.relatedParameter.content" type="textarea" :rows="3" :disabled="type == 'view'" size="small" placeholder="请输入邮件内容"></el-input>
              </el-form-item>
              <el-form-item label="收件人邮箱配置:">
                <el-tag
                  :key="tag"
                  v-for="tag in dynamicTags"
                  :closable = "!type == 'view'"
                  :disable-transitions="false"
                  @close="handleClose(tag)">
                  {{tag}}
                </el-tag>
                <el-input
                  class="input-new-tag"
                  v-if="inputVisible"
                  v-model="inputValue"
                  ref="saveTagInput"
                  size="small"
                  @keyup.enter.native="handleInputConfirm"
                  @blur="handleInputConfirm"
                >
                </el-input>
                <el-button v-else class="button-new-tag" size="small" :disabled="type == 'view'" @click="showInput">新 增</el-button>
              </el-form-item>
              <el-form-item label="抄送人:">
                <el-tag
                  :key="tag"
                  v-for="tag in dynamicToCCTags"
                  :closable = "!type == 'view'"
                  :disable-transitions="false"
                  @close="handleCloseToCC(tag)">
                  {{tag}}
                </el-tag>
                <el-input
                  class="input-new-tag"
                  v-if="inputVisibleToCC"
                  v-model="inputValueToCC"
                  ref="saveTagInputTaCC"
                  size="small"
                  @keyup.enter.native="handleInputConfirmToCC"
                  @blur="handleInputConfirmToCC"
                >
                </el-input>
                <el-button v-else class="button-new-tag" size="small" :disabled="type == 'view'" @click="showInputToCC">新 增</el-button>
              </el-form-item>
            </div>
          </div>
        </el-form>
      </div>
      <!--     步骤三     -->
      <div v-if="active == 2" style="margin: 10px 20px; text-align: center">
        <el-table
          :data="templateParam"
          style="width: 100%">
          <el-table-column label="模板参数" width="200" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.key" :disabled="type == 'view'" size="small" placeholder="请输入相关key"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="平台参数" align="center">
            <template slot-scope="scope">
              <el-select v-model="scope.row.value" placeholder="请输入相关参数" style="width: 100%;">
                <el-option
                  v-for="item in messageDefaultParams"
                  :disabled="type == 'view'"
                  size="small"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                  <span style="float: left">{{ item.label }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.remark == null ? '' : item.remark }}</span>
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column v-if="type != 'view'" width="80" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" icon="el-icon-delete" plain @click="handleDeleteParam(scope.row)"></el-button>
            </template>
          </el-table-column>
          <div slot="append" v-if="type != 'view'">
            <el-button size="mini" type="primary" icon="el-icon-plus" plain @click="handleAddParam"></el-button>
          </div>
        </el-table>
      </div>
      <div class="dialog-footer">
        <el-button style="margin-top: 12px" v-if="active != 0" @click="previous">上一步</el-button>
        <el-button style="margin-top: 12px" v-if="active !== 2" @click="next">下一步</el-button>
        <el-button style="margin-top: 12px" v-if="active === 2 && type != 'view'" @click="save" type="primary">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import vueJsonEditor from 'vue-json-editor'
import {
  getAppByChannelType,
  getById,
  getMessageTypeByChannelType,
  saveTemplate,
  updateById
} from "@/api/message/template"
import axios from "axios";
import {listData} from "@/api/system/dict/data";
import {listSimpleUsers} from "@/api/system/user";

export default {
  name: "templateView",
  components: { vueJsonEditor },
  data() {
    const _this = this
    return {
      dynamicTags: [],
      dynamicToCCTags: [],
      inputVisible: false,
      inputVisibleToCC: false,
      inputValue: '',
      inputValueToCC: '',
      selectedRecipient: null,
      selectedRecipients: [],
      users: [],
      userData: [],
      active: 0,
      type: 'add',
      title: '新增模板',
      dialogFlag: false, // dialog显示隐藏
      channelSelectShow: true,
      appMessageTypeShow: true,
      hasJsonFlag: true, // json是否验证通过 （true为通过，false为失败）
      jsonobj: {},
      dataForm: {
        pushRange: 0,
        messageType: '1',
        relatedParameter: {}
      },
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        dictName: undefined,
        dictType: undefined,
        status: undefined
      },
      templateParam: [], // 自定义参数
      // relatedParameter: {}, // 相关参数
      pushMethodDict: [], // 推送方式字典
      userDict: [], // 用户类型字典
      userTypeDict: [], // 外部人员类型字典
      channelTypeDict: [], // 通道类型字典
      channelAppDict: [], // 通道App字典
      messageTypeDict:[
        { id: '1', name: '文本消息' },
        //
        { id: '4-1', name: '钉钉-图片消息' },
        { id: '4-2', name: '钉钉-语音消息' },
        { id: '4-3', name: '钉钉-文件消息' },
        { id: '4-4', name: '钉钉-链接消息' },
        { id: '4-5', name: '钉钉-OA 消息' },
        { id: '4-6', name: '钉钉-markdown 消息' },
        { id: '4-7', name: '钉钉-卡片消息' },
        //
        { id: '5-1', name: '企业微信-图片消息' },
        { id: '5-2', name: '企业微信-语音消息' },
        { id: '5-3', name: '企业微信-视频消息' },
        { id: '5-4', name: '企业微信-文件消息' },
        { id: '5-5', name: '企业微信-文本卡片消息' },
        { id: '5-6', name: '企业微信-图文消息(mpnews)' },
        { id: '5-7', name: '企业微信-markdown 消息' },
        { id: '5-8', name: '企业微信-小程序通知消息' },
        //
        { id: '6-1', name: '飞书-富文本 post' },
        { id: '6-2', name: '飞书-图片 image' },
        { id: '6-3', name: '飞书-消息卡片 interactive' },
        { id: '6-4', name: '飞书-分享群名片 share_chat' },
        { id: '6-5', name: '飞书-分享个人名片 share_user' },
        { id: '6-6', name: '飞书-语音 audio' },
        { id: '6-7', name: '飞书-视频 media' },
        { id: '6-8', name: '飞书-文件 file' },
        { id: '6-9', name: '飞书-表情包 sticker' }
      ], // 消息类型字典
      channelDict: [], // 通道字典
      providerTypeDict: [], // 运营商类型字典
      messageDefaultParams: [],
      modeList: [
        'code'
      ],
      options: {
        search: false,
        history: false,
      },
      rules: {
        templateName: [
          {
            required: true,
            message: "请输入模板名称",
            trigger: 'blur'
          },
        ],
        // pushRange: [
        //   {
        //     required: true,
        //     message: "请选择推送范围",
        //     trigger: 'blur change'
        //   },
        // ],
        // usersType: [
        //   {
        //     required: true,
        //     message: "请输入用户类型",
        //     trigger: 'blur'
        //   },
        // ],
        channelType: [
          {
            required: true,
            message: "请选择通道类型",
            trigger: 'blur change'
          },
        ],
        appId: [
          {
            required: true,
            message: "请选择消息通道",
            trigger: 'blur'
          },
        ],
        messageType: [
          {
            required: true,
            message: "请选择消息类型",
            trigger: 'blur change'
          },
        ],
        // relatedParameter: [
        //   {
        //     required: true,
        //     validator: async function (rule, value, callback) {
        //       if (!_this.hasJsonFlag) {
        //         throw new Error('请正确输入JSON格式')
        //       }
        //       await Promise.resolve()
        //     },
        //     trigger: 'change' }
        // ]
        'relatedParameter.templateContext': [
          {
            required: true,
            message: "请输入模板内容站内信",
            trigger: 'blur'
          },
        ],
        'relatedParameter.callProvider': [
          {
            required: true,
            message: "请选择运营商111",
            trigger: 'blur'
          },
        ],
        'relatedParameter.region': [
          {
            required: true,
            message: "请输入区域",
            trigger: 'blur'
          },
        ],
        'relatedParameter.templateId': [
          {
            required: true,
            message: "请输入模板ID",
            trigger: 'blur'
          },
        ],
        'relatedParameter.playTimes': [
          {
            required: true,
            message: "请输入播放次数",
            trigger: 'blur'
          },
        ],
        'relatedParameter.ttsCode': [
          {
            required: true,
            message: "请输入模板编码",
            trigger: 'blur'
          },
        ],
        'relatedParameter.smsProvider': [
          {
            required: true,
            message: "请选择运营商2222",
            trigger: 'blur'
          },
        ],
        'relatedParameter.smsSdkAppId': [
          {
            required: true,
            message: "请输入短信AppId",
            trigger: 'blur'
          },
        ],
        'relatedParameter.signName': [
          {
            required: true,
            message: "请输入签名",
            trigger: 'blur'
          },
        ],
        'relatedParameter.templateCode': [
          {
            required: true,
            message: "请输入模板编码",
            trigger: 'blur'
          },
        ],
        'relatedParameter.users': [
          {
            required: true,
            message: "请输入配置信息",
            trigger: 'blur'
          },
        ],
        'relatedParameter.toCC': [
          {
            required: true,
            message: "请输入抄送人",
            trigger: 'blur'
          },
        ],
        'relatedParameter.title': [
          {
            required: true,
            message: "请输入邮件标题",
            trigger: 'blur'
          },
        ],
        'relatedParameter.content': [
          {
            required: true,
            message: "请输入邮件内容",
            trigger: 'blur'
          },
        ],
        // 'relatedParameter.userIds': [
        //   {
        //     required: true,
        //     message: "请选择接收人",
        //     trigger: 'blur'
        //   },
        // ],
      },
    }
  },
  mounted() {
    this.queryParams.dictType = 'message_params'
    listData(this.queryParams).then(res => {
      this.messageDefaultParams = res.data.list
    })
    // this.messageDefaultParams = this.$store.state.dict.dictDatas.message_params
    this.pushMethodDict = this.$store.state.dict.dictDatas.message_push_method
    this.userDict = this.$store.state.dict.dictDatas.message_user_type
    this.channelDict = this.$store.state.dict.dictDatas.message_channel
    this.providerTypeDict = this.$store.state.dict.dictDatas.provider_type
    this.userDict.forEach(item => {
      if (item.value === '2' || item.value === '3') {
        this.userTypeDict.push(item)
      }
    })
    listSimpleUsers().then(response => {
      this.users = response.data;
      if (this.type != 'add') {
        let userData = this.users;
        let split = this.dataForm.userIds.split(',');
        this.userData = userData.filter((item) => {
          return split.includes(item.id.toString());
        }).map(item => item.id)
      }
    })
  },
  methods: {
    handleClose(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    handleInputConfirm() {
      let users = this.inputValue;
      if (users) {
        this.dynamicTags.push(users);
      }
      this.inputVisible = false;
      this.inputValue = '';
      this.dataForm.relatedParameter.users = this.dynamicTags;
    },
    handleCloseToCC(tag) {
      this.dynamicToCCTags.splice(this.dynamicToCCTags.indexOf(tag), 1);
    },
    showInputToCC() {
      this.inputVisibleToCC = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInputTaCC.$refs.input.focus();
      });
    },
    handleInputConfirmToCC() {
      let toCC = this.inputValueToCC;
      if (toCC) {
        this.dynamicToCCTags.push(toCC);
      }
      this.inputVisibleToCC = false;
      this.inputValueToCC = '';
      this.dataForm.relatedParameter.toCC = this.dynamicToCCTags;
    },
    selectUserIds() {
      let userData = this.userData;
      this.dataForm.userIds = userData.join(',');
    },
    // 删除参数
    handleDeleteParam(row) {
      const data = this.templateParam.filter(item => !(row.key === item.key))
      this.templateParam = data
    },
    // 新增参数
    handleAddParam() {
      var data = { id: Date.now(), key: '', value: ''}
      this.templateParam.push(data)
    },
    // 上一步
    previous() {
      this.active--;
    },
    // 下一步
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
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.active++
        }
      })
    },
    active2(){
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          var dataList = []
          var regex = /\{([^}]+)\}/g
          var match
          var templateParam
          //存 users
          let userList = []
          if (this.dataForm.userIds != null && this.dataForm.userIds !== ""){
            userList = this.dataForm.userIds.toString().split(",")
          }
          this.dataForm.relatedParameter.users = userList
          if (this.type == 'add') {
            if ([0, 3].includes(this.dataForm.channelType)) {
              if (this.dataForm.channelType == 0) {
                // 操作为站内信
                templateParam = this.dataForm.relatedParameter.templateContext
              }
              if (this.dataForm.channelType == 3) {
                templateParam = this.dataForm.relatedParameter.title + ',' + this.dataForm.relatedParameter.content
              }

              while ((match = regex.exec(templateParam)) !== null) {
                dataList.push(match[1]);
              }
              dataList.forEach(item => {
                var i = 1;
                var dataId = Date.now().toString(36) + Math.random().toString(36).substr(2, 9) + i
                var data = {id: dataId + i, key: item, value: ''}
                this.templateParam.push(data)
                i++
              })
            }
          }
          this.active++
        }
      })
    },
    // JSON配置改变事件
    jsonChange(value) {
      this.hasJsonFlag = true
    },
    onError(value) {
      this.hasJsonFlag = false
    },
    // 新增模板
    addTemplate(){
      this.type = 'add'
      this.title = '新增模板'
      this.dialogFlag = true
    },
    // 编辑模板 - 回显
    editTemplate(data){
      const fromData = data
      this.dialogFlag = true
      this.channelSelectShow = false
      this.appMessageTypeShow = false
      this.type = 'edit'
      this.title = '编辑模板'
      this.dataForm = fromData
      this.jsonobj = JSON.parse(this.dataForm.relatedParameter)
      this.dataForm.relatedParameter = this.jsonobj
      this.dataForm.channelType = fromData.pushWays.channelType
      this.dataForm.messageType = fromData.pushWays.messageType
      if (this.dataForm.relatedParameter.templateParam != null || this.dataForm.relatedParameter.templateParam != undefined) {
        this.templateParam = Object.keys(this.dataForm.relatedParameter.templateParam).map(key => ({ key, value: this.dataForm.relatedParameter.templateParam[key] }))
      }
      if (this.dataForm.relatedParameter.users !== '' && this.dataForm.relatedParameter.users !== undefined) {
        // this.dynamicTags = this.dataForm.relatedParameter.users.split(',')
        this.dynamicTags = this.dataForm.relatedParameter.users
        //this.handleInputConfirm()
      }
      console.log(this.dynamicTags,"this.dynamicTags")
      if (this.dataForm.relatedParameter.toCC !== '' && this.dataForm.relatedParameter.toCC !== undefined) {
        this.dynamicToCCTags = this.dataForm.relatedParameter.toCC
        //this.handleInputConfirmToCC()
      }
      this.changePushRange()
      getAppByChannelType({channelType: this.dataForm.channelType}).then(res => {
        this.channelAppDict = res.data
      })
    },
    // 查看模板 - 回显
    viewTemplate(data){
      const fromData = data
      this.dialogFlag = true
      this.channelSelectShow = false
      this.appMessageTypeShow = false
      this.type = 'view'
      this.title = '查看模板'
      this.dataForm = fromData
      this.jsonobj = JSON.parse(this.dataForm.relatedParameter)
      this.dataForm.relatedParameter = this.jsonobj
      this.dataForm.channelType = fromData.pushWays.channelType
      this.dataForm.messageType = fromData.pushWays.messageType
      if (this.dataForm.relatedParameter.templateParam != null || this.dataForm.relatedParameter.templateParam != undefined) {
        this.templateParam = Object.keys(this.dataForm.relatedParameter.templateParam).map(key => ({ key, value: this.dataForm.relatedParameter.templateParam[key] }))
      }
      if (this.dataForm.relatedParameter.users !== '' && this.dataForm.relatedParameter.users !== undefined) {
        this.userData = this.dataForm.relatedParameter.users;
        this.dynamicTags = this.dataForm.relatedParameter.users
      }
      if (this.dataForm.relatedParameter.toCC !== '' && this.dataForm.relatedParameter.toCC !== undefined) {
        this.dynamicToCCTags = this.dataForm.relatedParameter.toCC
      }
      this.changePushRange()
      getAppByChannelType({channelType: this.dataForm.channelType}).then(res => {
        this.channelAppDict = res.data
      })
    },
    // 场景中点击查看
    viewById(row) {

      getById(row.actionId).then(res => {
        let data = res.data
        //取当前 独立的动作参数  不用查询出来的模板参数 为了查看时 可以查看到编辑后的
        data.relatedParameter = row.relatedParameter
        data.pushWays = JSON.parse(data.pushWays)
        this.viewTemplate(res.data)
      })
    },
    // 克隆模板 - 回显
    cloneTemplate(data) {
      const fromData = data
      this.dialogFlag = true
      this.channelSelectShow = false
      this.appMessageTypeShow = false
      this.type = 'add'
      this.title = '新增模板'
      this.dataForm = fromData
      this.jsonobj = JSON.parse(this.dataForm.relatedParameter)
      this.dataForm.relatedParameter = this.jsonobj
      this.dataForm.channelType = fromData.pushWays.channelType
      this.dataForm.messageType = fromData.pushWays.messageType
      if (this.dataForm.relatedParameter.templateParam != null || this.dataForm.relatedParameter.templateParam != undefined) {
        this.templateParam = Object.keys(this.dataForm.relatedParameter.templateParam).map(key => ({ key, value: this.dataForm.relatedParameter.templateParam[key] }))
      }
      this.changePushRange()
      getAppByChannelType({channelType: this.dataForm.channelType}).then(res => {
        this.channelAppDict = res.data
      })
      // getMessageTypeByChannelType({channelType: this.dataForm.channelType}).then(res => {
      //   this.messageTypeDict = res.data
      // })
      /*      const fromData = {}
            this.dialogFlag = true
            this.channelSelectShow = false
            this.appMessageTypeShow = false
            this.type = 'add'
            this.title = '新增模板'
            fromData.templateName = data.templateName
            fromData.pushRange = data.pushRange
            fromData.usersType = data.usersType
            fromData.appId = data.appId
            fromData.templateStetus = data.templateStetus
            fromData.channelType = data.pushWays.channelType
            fromData.messageType = data.pushWays.messageType
            this.dataForm = fromData
            this.jsonobj = JSON.parse(this.dataForm.relatedParameter)
            this.changePushRange()
            getAppByChannelType({channelType: this.dataForm.channelType}).then(res => {
              this.channelAppDict = res.data
            })
            getMessageTypeByChannelType({channelType: this.dataForm.channelType}).then(res => {
              this.messageTypeDict = res.data
            })*/
    },
    // 用户类型变化
    changeUsers() {
      this.channelSelectShow = false
    },
    // 推送类型变化
    changePushRange() {
      if (this.dataForm.pushRange !== 2) {
        this.userTypeDict = this.userDict
      } else {
        this.userTypeDict = []
        this.userDict.forEach(item => {
          if (item.value === '2' || item.value === '3') {
            this.userTypeDict.push(item)
          }
        })
      }
    },
    // 消息通道变化
    changeChannel() {
      this.appMessageTypeShow = false
      getAppByChannelType({channelType: this.dataForm.channelType}).then(res => {
        this.channelAppDict = res.data
      })
      // getMessageTypeByChannelType({channelType: this.dataForm.channelType}).then(res => {
      //   this.messageTypeDict = res.data
      // })
    },
    // 点击确定
    save() {
      // 校验模板参数是否合规
      if (!this.checkTemplateParam()) {
        this.$message.warning("模板参数不合规")
        return
      }
      // 将模板参数转化为对象数据
      var paramsList = this.templateParam.filter(item => item.key && item.value)
      var templateParam = paramsList.reduce((acc, cur) => {
        if (cur.key != '' || cur.value != '') {
          acc[cur.key] = cur.value
          return acc
        }
      }, {})
      // 补充相关参数
      if (this.dataForm.channelType == 0) {
        // 站内信的用户类型为平台UserID
        this.dataForm.usersType = 4
      }
      if (this.dataForm.channelType == 1 || this.dataForm.channelType == 2) {
        // 电话和短信的用户类型为电话
        this.dataForm.usersType = 2
      }
      if (this.dataForm.channelType == 3) {
        // 邮件的用户类型为邮箱
        this.dataForm.usersType = 3
        this.dataForm.relatedParameter.htmlFlag = true
      }
      // 将必要参数和模板参数统一封装到 relatedParameter 相关参数中
      // this.dataForm.relatedParameter.templateParam = JSON.stringify(templateParam)
      this.dataForm.relatedParameter.templateParam = templateParam
      this.dataForm.relatedParameter = JSON.stringify(this.dataForm.relatedParameter)
      let pushWays = {}
      pushWays.channelType = this.dataForm.channelType
      pushWays.messageType = this.dataForm.messageType
      this.dataForm.pushWays = JSON.stringify(pushWays)
      console.log(this.dataForm,"this.dataForm")
      if (this.type === 'add') {
        saveTemplate(this.dataForm).then(res => {
          if (res.code === 0) {
            this.$message.success("新增成功")
            this.cancel()
          } else {
            this.$message.error("新增失败")
          }
        })
      }
      if (this.type === 'edit') {
        updateById(this.dataForm).then(res => {
          if (res.code === 0) {
            this.$message.success("编辑成功")
            this.cancel()
          } else {
            this.$message.error("编辑失败")
          }
        })
      }
      // this.$refs["ruleForm"].validate((valid) => {
      //   if (valid) {
      //     let pushWays = {}
      //     pushWays.channelType = this.dataForm.channelType
      //     pushWays.messageType = this.dataForm.messageType
      //     this.dataForm.relatedParameter = JSON.stringify(this.jsonobj)
      //     this.dataForm.pushWays = JSON.stringify(pushWays)
      //
      //
      //   }
      // })
    },
    // 校验模板参数是否合规
    checkTemplateParam() {
      var status = true
      this.templateParam.forEach(item  => {
        if ((item.key == '' && item.value != '') || (item.key != '' && item.value == '')) {
          status = false
        } else {
          status = true
        }
      })
      return status
    },
    cancel() {
      this.active = 0
      this.channelSelectShow = true
      this.appMessageTypeShow = true
      this.dialogFlag = false;
      this.dataForm = {};
      this.$refs['ruleForm'].clearValidate();
      this.$emit('restOnLoad');
    },
  }
}
</script>

<style scoped>
.template-form ::v-deep .el-textarea__inner {
  resize: none;
  min-height: 111.6px !important;
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: center;
  padding: 12px 0;
  border-top: 1px solid rgb(204, 204, 204);
}

.product-form ::v-deep .el-form-item__label {
  color: #3f4448;
  font-weight: 400;
}

.el-form-item .el-select {
  width: 100%;
}

.form-json-editor {
  pointer-events: none;
}

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
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
