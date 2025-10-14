<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="45%"
      @close="cancel"
    >
      <div style="margin: 10px 0; max-height: calc(100vh - 600px)">
        <el-collapse>
          <el-collapse-item v-for="(item, index) in dataForm.warningData" :key="index" :name="index">
            <template slot="title">
              <div class="title" style="width: 100%;">
                <el-tag>{{ item.thetime }}</el-tag>
              </div>
            </template>

            <div>
              <pre><code>{{ item }}</code></pre>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {detail} from "@/api/warning/warning-record"
import { detail as warningDetail } from "@/api/warning/warning-api";

export default {
  name: "warningLog",
  data() {
    return {
      title: '场景日志',
      dialogFlag: false,
      dataForm: {},
      warningConfid: {},
    }
  },
  methods: {
    init(row) {
      this.dialogFlag = true;
      this.dataForm.id = row.id
      detail(row.id).then(res => {
        if (res.code === 0) {
          var warningData = res.data.warningData
          warningData = JSON.parse(warningData)
          this.dataForm = res.data
          this.dataForm.warningData = warningData
        }
      })
      warningDetail(row.configId).then(res => {
        if (res.code === 0) {
          this.warningConfid = res.data
        }
      })
    },
    cancel() {
      this.dialogFlag = false;
      this.dataForm = {};
      // this.$refs['ruleForm'].clearValidate();
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

pre {
  background-color: #f4f4f4;
  border: 1px solid #ddd;
  border-left: 3px solid #f36d33;
  color: #666;
  page-break-inside: avoid;
  font-family: monospace;
  font-size: 15px;
  line-height: 1.6;
  margin-bottom: 1.6em;
  max-width: 100%;
  overflow: auto;
  padding: 1em 1.5em;
  display: block;
  word-wrap: break-word;
}

code {
  background-color: #f4f4f4;
  border-radius: 3px;
  font-family: monospace;
  font-size: 15px;
  padding: 2px 4px;
}
</style>
