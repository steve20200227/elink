<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      width="40%"
      @close="cancel"
    >
      <div style="margin: 10px 0; margin-right: 50px">
        <el-form  :rules="rules" ref="ruleForm" :model="dataForm" label-width="100px" class="product-form">
          <el-form-item label="通道名称:" prop="appName">
            <el-input v-model="dataForm.appName" size="small" placeholder="请填写长度在 3 到 20 个字符的 通道名称"></el-input>
          </el-form-item>
          <el-form-item label="通道选择:" prop="channelType">
            <el-select v-model="dataForm.channelType" @change="changeChannelType" size="small" placeholder="请选择通道类型">
              <el-option
                v-for="item in channelDict"
                :key="item.value"
                :label="item.label"
                :value="item.value * 1">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="通道配置:" prop="appConfig">
            <vue-json-editor
              v-model="jsonobj"
              currentMode="code"
              :modeList="modeList"
              :option="options"
              :showBtns="false"
              language="cn"
              @json-change="jsonChange"
              @has-error="onError"
              :mode="'code'"/>
          </el-form-item>
          <el-form-item label="通道状态:" prop="appStatus">
            <el-switch
              v-model="dataForm.appStatus"
              active-text="已启用"
              :active-value="1"
              :inactive-value="0"
              size="small"
              inactive-text="未启用">
            </el-switch>
          </el-form-item>
        </el-form>
      </div>
      <div style="float: right">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" @click="save" type="primary">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {save, selectChannel, updateById, updateStatusById} from "@/api/message/channel";
import vueJsonEditor from 'vue-json-editor'

export default {
  name: "addChannel",
  components: { vueJsonEditor },
  data() {
    const _this = this
    return {
      type: 'add',
      title: '新增通道',
      dialogFlag: false,
      dataForm: {
        appName: '',
        channelType: '',
        appConfig: '',
        appStatus: 0,
      },
      jsonobj: {}, // APP配置
      channelDict: [], // 通道字典
      rules: {
        appName: [
          {
            required: true,
            message: "请输入通道名称",
            trigger: 'blur'
          },
          { min: 3, max: 20, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
        ],
        channelType: [
          {
            required: true,
            message: "请选择通道类型",
            trigger: 'blur change'
          },
        ],
        appConfig: [
          {
            required: true,
            validator: async function (rule, value, callback) {
              if (!_this.hasJsonFlag) {
                throw new Error('请正确输入 APP 配置')
              }
              await Promise.resolve()
            },
            trigger: 'change' }
        ]
      },
      modeList: [
        'code'
      ],
      options: {
        search: false,
        history: false,
      },
      hasJsonFlag: true, // json是否验证通过 （true为通过，false为失败）
    }
  },
  mounted() {
    this.channelDict = this.$store.state.dict.dictDatas.message_channel
  },
  methods: {
    // 新增通道
    addChannel(){
      this.type = 'add'
      this.title = '新增通道'
      this.dialogFlag = true
    },
    // JSON配置改变事件
    jsonChange(value) {
      this.hasJsonFlag = true
    },
    onError(value) {
      this.hasJsonFlag = false
    },
    // 编辑-回显通道
    editChannel(data){
      this.dialogFlag = true
      this.type = 'edit'
      this.title = '编辑通道'
      this.dataForm = data
      this.jsonobj = JSON.parse(data.appConfig)
    },
    // 选择通道类型 - 附带app配置格式数据
    changeChannelType(val) {
      var data = {}
      data.channelType = val
      selectChannel(data).then(res => {
        this.jsonobj = JSON.parse(res.data)
        this.dataForm.appConfig = JSON.stringify(this.jsonobj)
      })
    },
    // 新增 - 点击确定
    save() {
      console.log(this.jsonobj, 'jsonObj22222')
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.dataForm.appConfig = JSON.stringify(this.jsonobj)
          if (this.type === 'add') {
            save(this.dataForm).then(res => {
              this.cancel()
              this.$message.success("新增成功")
            })
          }
          if (this.type === 'edit') {
            console.log(this.dataForm, 'dataFrom')
            updateById(this.dataForm).then(res => {
              this.$message.success("编辑成功")
              this.cancel()
            })
          }
        }
      })
    },
    cancel() {
      this.dialogFlag = false;
      this.dataForm = {};
      this.jsonobj = {}
      this.$refs['ruleForm'].clearValidate();
      this.$emit('restOnLoad');
    },
  }
}
</script>

<style scoped>
.product-form ::v-deep .el-form-item__label {
  color: #3f4448;
  font-weight: 400;
}

.el-form-item .el-select {
  width: 100%;
}
</style>
