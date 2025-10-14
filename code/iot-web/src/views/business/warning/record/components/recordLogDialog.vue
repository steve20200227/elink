<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="45%"
      @close="cancel"
    >
      <div style="margin: 10px 0;margin-right: 70px">
        <el-form ref="ruleForm" :model="dataForm" label-width="100px" class="product-form">
          <el-form-item label="处理人:" prop="handleUser">
            <el-input v-model="dataForm.handleUser" :readonly="true" size="small"></el-input>
          </el-form-item>
          <el-form-item label="处理时间:" prop="handleTime">
            <el-input v-model="dataForm.handleTime" :readonly="true" size="small"></el-input>
          </el-form-item>
          <el-form-item label="处理说明:" prop="remark">
<!--            <el-input type="textarea" :rows="6" v-model="dataForm.remark" v-html :readonly="true" maxlength="500" show-word-limit></el-input>-->
            <vue-editor
              ref="editor"
              :editorOptions="editorOption"
              :disabled="true"
              v-model="dataForm.remark"
              style="width: 99%;"
            ></vue-editor>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {detail} from "@/api/warning/warning-record"
import {getRole} from "@/api/system/role"
import {parseTime} from "@/utils/ruoyi";
import {VueEditor} from "vue2-editor";

export default {
  name: "recordLog",
  components: {
    VueEditor
  },
  data() {
    return {
      title: '处理日志',
      dialogFlag: false,
      dataForm: {},
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
        }
      } //编辑器配置项
    }
  },
  methods: {
    init(row) {
      this.dialogFlag = true;
      this.dataForm.id = row.id
      detail(row.id).then(res => {
        if (res.code === 0) {
          this.dataForm = res.data
          this.dataForm.handleTime = parseTime(res.data.handleTime)
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
