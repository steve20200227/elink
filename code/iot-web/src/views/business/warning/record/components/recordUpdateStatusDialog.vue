<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="60%"
      @close="cancel"
    >
      <div style="margin: 10px 0;margin-right: 70px">
        <el-form ref="ruleForm" :model="dataForm" label-width="100px" class="product-form">
          <el-row>
            <el-col :span="16">
              <el-form-item label="记录状态:" prop="status">
                <el-select v-model="dataForm.status" :disabled="true" size="small" style="width: 100%" placeholder="请选择记录状态">
                  <el-option
                    v-for="item in recordDict"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value * 1">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="处理说明:" prop="remark">
<!--            <el-input type="textarea" :rows="6" v-model="dataForm.remark" maxlength="500" show-word-limit></el-input>-->
            <vue-editor
              ref="editor"
              :editorOptions="editorOption"
              v-model="dataForm.remark"
              style="width: 99%;"
              :useCustomImageHandler="false"
            ></vue-editor>
          </el-form-item>
        </el-form>
      </div>

      <div style="float: right">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" type="primary" @click="determine">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { updateStatus } from "@/api/warning/warning-record"
import {VueEditor} from "vue2-editor";

export default {
  name: "recordStatus",
  components: {
    VueEditor
  },
  data() {
    return {
      title: '记录处理',
      dialogFlag: false,
      dataForm: {},
      recordDict: [], // 记录状态
      handleUser: "",
      editorOption: {
        modules:{
          toolbar: [
            ['bold', 'italic', 'underline', 'strike', 'color', 'align', 'clean', 'image'], //加粗，斜体，下划线，删除线
            // ['blockquote', 'code-block'], //引用，代码块
            // [{'header': 1}, {'header': 2}], // 标题，键值对的形式；1、2表示字体大小
            // [{'list': 'ordered'}, {'list': 'bullet'}], //列表
            // [{'script': 'sub'}, {'script': 'super'}], // 上下标
            // [{'indent': '-1'}, {'indent': '+1'}], // 缩进
            // [{'direction': 'rtl'}], // 文本方向
            // [{'size': ['small', false, 'large', 'huge']}], // 字体大小
            // [{'header': [1, 2, 3, 4, 5, 6, false]}], //几级标题
            // [{'color': []}, {'background': []}], // 字体颜色，字体背景颜色
            // [{'font': []}], //字体
            // [{'align': []}], //对齐方式
            // ['clean'], //清除字体样式
            // ['image'] //上传图片、上传视频
          ]
        },
        placeholder: "输入内容..."
      } //编辑器配置项
    }
  },
  mounted() {
    this.recordDict = this.$store.state.dict.dictDatas.record_status
  },
  methods: {
    init(row) {
      this.dialogFlag = true;
      this.dataForm.id = row.id
      this.dataForm.status = 0
    },
    // 确认按钮
    determine() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.dataForm.handleUser = this.$store.state.user.nickname
          this.dataForm.status = 1;
          updateStatus(this.dataForm).then(res => {
            if (res.code === 0) {
              this.$message.success("操作成功")
              this.cancel()
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
  }
}
</script>

<style scoped>
.product-form ::v-deep .el-form-item__label {
  color: #3f4448;
  font-weight: 400;
}

.product-form ::v-deep .el-textarea__inner {
  resize: none;
  min-height: 111.6px !important;
}
</style>
