<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="50%"
      @close="cancel">
      <div style="margin: 10px 0">
        <el-form :rules="rules" ref="ruleForm" :model="dataForm" :before-close="disableClose" label-width="100px"
                 class="product-form">
          <el-row>
            <el-col :span="8">
              <el-form-item label="事件编码:" prop="pointCode">
                <el-input v-model="dataForm.pointCode" :disabled="type === 'view'" size="small"
                          placeholder="请输入事件编码"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="事件名称:" prop="pointName">
                <el-input v-model="dataForm.pointName" :disabled="type === 'view'" size="small"
                          placeholder="请输入事件名称"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="是否启用:" prop="isEnable">
                <el-switch
                  :disabled="type == 'view'"
                  v-model="dataForm.isEnable"
                  active-text="已启用"
                  :active-value="1"
                  :inactive-value="0"
                  inactive-text="未启用">
                </el-switch>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="输出参数:" prop="outputParameter">
              </el-form-item>
              <span style="color: green; cursor: pointer;"></span>
              <div style="padding: 0 20px;">
                <div id="codeEditor" style="height: 400px; width: 90%; max-width: 750px; margin-top: 10px; border-right: 2px solid #ebebeb"></div>
              </div>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div style="float: right">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" v-if="type !== 'view'" @click="savePoint" type="primary">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// TODO 重新引用
// import {DICT, DICT_BIZ} from "@/util/dictConstant";
// import {detail, submit} from '@/api/cps/product-point-api';
import {submit, detail} from '@/api/cps/product-point-api'
import 'ace-builds/src-noconflict/ace.js';
import 'ace-builds/src-noconflict/mode-json.js';
import 'ace-builds/src-noconflict/theme-chrome.js';
import 'ace-builds/src-noconflict/ext-language_tools.js';
import IFrame from "@/components/iFrame/index.vue";

export default {
  name: "add",
  data() {
    return {
      title: '事件模型',
      pointType: '',
      type: 'edit',
      dialogFlag: false,
      dataForm: {},
      // 判断写指令是否必填
      isWrite: true,
      rwDict: [],
      readDict: [],
      writeDict: [],
      dataTypeDict: [],
      rules: {
        pointCode: [
          {
            required: true,
            message: "请输入事件编码",
            trigger: 'blur'
          },
        ],
        pointName: [
          {
            required: true,
            message: "请输入事件名称",
            trigger: 'blur'
          }
        ],
        outputParameter: [
          {
            required: true,
          }
        ],
      },
    }
  },
  mounted() {
    this.rwDict = this.$store.state.dict.dictDatas.read_write
    this.readDict = this.$store.state.dict.dictDatas.read_instruction
    this.writeDict = this.$store.state.dict.dictDatas.write_instruction
    this.dataTypeDict = this.$store.state.dict.dictDatas.attribute_data_type
  },
  methods: {
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
    disableClose() {
      this.dataForm = {};
      this.dialogFlag = false;
    },
    addPoint(id, pointType, agreementType) {
      this.title = '事件模型新增'
      this.dataForm.quantity = 1
      this.dataForm.agreementType = agreementType
      this.dataForm.productId = id;
      this.dialogFlag = true;
      this.pointType = pointType;
      this.$nextTick(() => {
        this.initializeEditor();
      })
    },
    editPoint(data) {
      this.title = '事件模型编辑'
      if (data.type) {
        this.type = data.type;
      }
      this.pointType = data.pointType;
      this.dialogFlag = true;
      this.getDetail(data.id)
      if (this.dataForm.writeInstruction === -1) {
        this.dataForm.writeInstruction = "";
      }
    },
    savePoint() {
      const editor = window.ace.edit("codeEditor");
      let outputParameter = editor.getValue();
      if (outputParameter === '' || outputParameter === undefined) {
        this.$message.warning("请输入输出参数");
        return
      }
      this.dataForm.outputParameter = outputParameter;
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.dataForm.pointType = this.pointType;
          submit(this.dataForm).then(res => {
            const {msg} = res;
            if (res.code === 0) {
              this.$message.success("操作成功")
              this.cancel();
            } else {
              this.$message.warning(msg);
            }
          })
        }
      })
    },
    cancel() {
      this.dialogFlag = false;
      this.dataForm = {};
      this.$refs['ruleForm'].clearValidate();
      this.$emit('restOnLoad');
    },
    getDetail(id) {
      detail(id).then(res => {
        this.dataForm = res.data;
        this.initializeEditor(this.dataForm.outputParameter);
      })
    },
  }
}
</script>

<style scoped>
.product-form ::v-deep .el-form-item__label {
  color: #3f4448;
  font-weight: 400;
}
</style>
