<template>
  <div class="app-describe">
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      :close-on-click-modal="true"
      width="40%"
      @close="cancel">
      <div>
        <div style="margin: 0 20px; padding-bottom: 10px; border-bottom: 1px solid rgb(204, 204, 204)">
          <el-row>
            <el-col :span="12">
              <span>应用ID：</span>
              <span>{{ dataFrom.appId }}</span>
              <span class="el-icon-document-copy" @click="handleCopy(dataFrom.appId)" style="margin-left: 5px; color: #1677ff; cursor: pointer;"></span>
            </el-col>
            <el-col :span="12">
              <span>应用名：</span>
              <span>{{ dataFrom.appName }}</span>
            </el-col>
          </el-row>
        </div>
        <div class="describe" style="margin-top: 10px; position: relative;padding: 0 20px;" v-if="dataFrom.appConfig !== ''">
          <el-descriptions title="APP配置" :column="2">
            <el-descriptions-item v-for="(value, label, index) in JSON.parse(dataFrom.appConfig)" :label="label" :key="index">

              <div v-show="showAppConfig" style="color: #1677ff">{{ value }}</div>
              <div v-show="!showAppConfig">{{ '*'.repeat((value + '').length) }}</div>
            </el-descriptions-item>
          </el-descriptions>
          <div style="position:absolute; top: -5px; right: 20px">
            <el-button v-if="!showAppConfig" @click="showAppConfig = true" type="text">显示</el-button>
            <el-button v-if="showAppConfig" @click="showAppConfig = false" type="text">隐藏</el-button>
          </div>
        </div>
        <div style="margin: 10px 20px 10px 20px;padding-top: 10px; border-top: 1px solid rgb(204, 204, 204)">
          <div class="appStatistic">
            <el-statistic
              group-separator=","
              :precision="2"
              :value="dataFrom.useCount"
              title="累计使用量"
            ></el-statistic>
          </div>
        </div>
        <div style="margin: 0 20px; padding-top: 10px; border-top: 1px solid rgb(204, 204, 204)">
          <el-row>
            <el-col :span="24">
              <span>创建时间：</span>
              <span>{{ parseTime(dataFrom.createTime) }}</span>
            </el-col>
          </el-row>
        </div>
      </div>

    </el-dialog>
  </div>
</template>

<script>

import {parseTime} from "../../../../../utils/ruoyi";

export default {
  data() {
    return {
      title: '通道信息',
      dialogFlag: false,
      showAppConfig: false, // 是否显示app配置
      dataFrom: {
        appName: '',
        channelType: '',
        appConfig: '',
        appStatus: 0,
        useCount: 0,
      }, // app数据
    }
  },
  mounted() {
  },
  methods: {
    parseTime,
    init(data) {
      this.dialogFlag = true
      this.dataFrom = data
    },
    async handleCopy(appId) {
      try {
        await navigator.clipboard.writeText(String(appId))
        this.$message.success('已成功复制应用ID')
      } catch (err) {
        this.$message.error('复制到剪贴板时出现错误：' + err)
      }
    },
    cancel() {
      this.dialogFlag = false;
      this.dataForm = {};
      this.showAppConfig = false
    },

  }
}
</script>

<style scoped>
.describe /deep/ .el-descriptions__body {
  margin: 0 20px !important;
}

.appStatistic /deep/ .el-statistic {
  display: flex !important;
}

.appStatistic /deep/ .head {
  margin-right: 5px !important;
}
</style>
