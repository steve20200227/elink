<template>
  <div id="app">
    <div id="display-content">
      <el-form :rules="rules" ref="ruleForm" :model="dataForm" :disabled="type == 'view'" label-width="100px"
               class="product-form">
        <el-row>
          <el-col :span="12">
            <el-form-item label="协议名称:" prop="agreementName">
              <el-input v-model="dataForm.agreementName" placeholder="请输入协议名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="协议编码:" prop="agreementCode">
              <el-input v-model="dataForm.agreementCode" placeholder="请输入协议编码"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="接入地址:" prop="accessAddress">
              <el-input placeholder="请输入1500-1510的数字" type="number" style="width: 200px;" v-model="dataForm.accessAddress">
                <template slot="prepend">192.168.1.1:</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span style="color: green; cursor: pointer;"></span>
      <div id="codeEditor" style="height: 400px; width: 100%; margin-top: 10px"></div>
    </div>
  </div>
</template>
<script>
import 'ace-builds/src-noconflict/ace.js';
import 'ace-builds/src-noconflict/mode-java.js';
import 'ace-builds/src-noconflict/theme-chrome.js';
import 'ace-builds/src-noconflict/ext-language_tools.js';
import {submit} from "@/api/cps/analyze_agreement_api";

export default {
  name: 'App',
  data() {
    return {
      dataForm: {},
      type: 'add',
      rules: {
        agreementName: [
          {
            required: true,
            message: '请输入协议名称',
            trigger: 'blur'
          },
          { max: 50, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
        ],
        agreementCode: [
          {
            required: true,
            message: '请输入协议编码',
            trigger: 'blur'
          },
          { max: 50, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
        ],
        accessAddress: [
          {
            required: true,
            message: '请输入接入协议',
            trigger: 'blur'
          },
          {
            validator: (rule, value, callback) => {
              const regex = /(1500|1501|1502|1503|1504|1505|1506|1507|1508|1509|1510)$/;
              if (!value || !regex.test(value)) {
                callback(new Error('请输入1500到1510之间的数字'));
              } else {
                callback();
              }
            },
            trigger: 'blur'
          }
        ],
      },
    }
  },
  methods: {
    save() {
      let that = this;
      const editor = window.ace.edit("codeEditor");
      that.dataForm.customAgreement = editor.getValue();
      let dataForm = that.dataForm;
      that.$refs.ruleForm.validate().then((validate) => {
        if (validate) {
          submit(dataForm).then((res) => {
            if (res.code == 0) {
              this.dataForm = res.data;
              this.$message.success("保存成功");
              this.$emit("close");
            }
          })
        }
      })
    },
    init(data, type) {
      this.type = type;
      const editor = window.ace.edit("codeEditor");
      editor.setTheme("ace/theme/chrome");
      editor.session.setMode("ace/mode/java");
      editor.setOptions({
        fontSize: "16px",
      });
      if (data != null && typeof data != "undefined") {
        this.dataForm = data
        if (data.accessAddress.includes(':')) {
          this.dataForm.accessAddress = data.accessAddress.split(':')[1]
        }
        editor.setValue(data.customAgreement, -1);
      }else {
        editor.setValue(null, -1);
      }
      if (type == 'view') {
        editor.setReadOnly(true);
      } else {
        editor.setReadOnly(false);
      }
    }
  }
}
</script>
