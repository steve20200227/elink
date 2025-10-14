<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="45%"
      @close="cancel"
    >
      <div class="action-log" style="margin: 10px 0;" v-loading="actionLoading">
        <el-collapse v-if="dataForm.length > 0">
          <el-collapse-item v-for="(item, index) in dataForm" :key="index" :name="index">
            <template slot="title">
              <div class="title" style="width: 100%;">
                <el-row>
                  <el-col :span="10">
                    <el-tag>{{ item.actionName }}</el-tag>
                  </el-col>
                  <el-col :span="5">
                    <el-tag>
                      <dict-tag :type="DICT_TYPE.ACTION_TYPE" :value="item.actionType"/>
                    </el-tag>
                  </el-col>
                  <el-col :span="6" style="text-align: right">
                    <span>
                      <span v-if="item.actionType == 1">
                        <el-tag type="success">
                          成功：{{ item.successCount }}
                        </el-tag>
                        <el-tag type="danger">
                          失败：{{ item.failCount }}
                        </el-tag>
                      </span>
                      <span v-if="item.actionType == 3">
                        <el-tag type="success">
                          成功：{{ item.successCount }}
                        </el-tag>
                        <el-tag type="danger">
                          失败：{{ item.failCount }}
                        </el-tag>
                      </span>
                      <span v-if="item.actionType == 2">
                        <el-tag type="success">
                          成功：{{ item.successCount }}
                        </el-tag>
                        <el-tag type="danger">
                          失败：{{ item.failCount }}
                        </el-tag>
                      </span>
                    </span>
                  </el-col>
                </el-row>
              </div>


            </template>
            <!--     发送消息       -->
            <div v-if="item.actionType == 1" v-for="(messageItem, messageIndex) in item.dataList" :key="messageIndex" style="padding: 0 30px; margin-bottom: 10px;font-size: 14px">
              <el-row>
                <el-col :span="12">
                  <span style="color: #646a73">推送人：</span>
                  <span style="color: #1677ff;">{{ messageItem.pushUser }}</span>
                </el-col>
                <el-col :span="12">
                  <span style="color: #646a73">执行时间：</span>
                  <span style="color: #1677ff;">{{ parseTime(messageItem.createTime) }}</span>
                </el-col>
                <el-col :span="12">
                  <span style="color: #646a73">推送状态：</span>
                  <span style="color: #1677ff;">
                    <dict-tag :type="DICT_TYPE.MESSAGE_STATUS" :value="messageItem.messageStatus"/>
                  </span>
                </el-col>
              </el-row>
              <el-divider v-if="(messageIndex + 1) != item.dataList.length"></el-divider>
            </div>

            <!--     设备反控       -->
            <div v-if="item.actionType == 2" v-for="(val, valindex) in item.dataList" style="padding: 0 30px; margin-bottom: 10px;font-size: 14px">
              <el-row>
                <el-col :span="12">
                  <span style="color: #646a73">推送地址：</span>
                  <span style="color: #1677ff;">{{ val.pushUrl }}</span>
                </el-col>
                <el-col :span="12">
                  <span style="color: #646a73">执行时间：</span>
                  <span style="color: #1677ff;">{{ parseTime(val.createTime) }}</span>
                </el-col>
                <el-col :span="12">
                  <span style="color: #646a73">失败原因：</span>
                  <span style="color: #1677ff;">{{ val.failReason }}</span>
                </el-col>
              </el-row>
              <el-divider v-if="(valindex + 1) != item.dataList.length"></el-divider>
            </div>

            <!--     北向输出       -->
            <div v-if="item.actionType == 3" v-for="(val, valindex) in item.dataList" style="padding: 0 30px; margin-bottom: 10px;font-size: 14px">
              <el-row>
                <el-col :span="12">
                  <span style="color: #646a73">推送地址：</span>
                  <span style="color: #1677ff;">{{ val.pushUrl }}</span>
                </el-col>
                <el-col :span="12" v-if="item.outputWay == 'MQTT'">
                  <span style="color: #646a73">主题：</span>
                  <span style="color: #1677ff;">{{ val.requestType }}</span>
                </el-col>
                <el-col :span="12" v-if="item.outputWay == 'HTTP'">
                  <span style="color: #646a73">请求方式：</span>
                  <span style="color: #1677ff;">{{ val.requestType }}</span>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <span style="color: #646a73">失败原因：</span>
                  <span style="color: #1677ff;">{{ val.failReason }}</span>
                </el-col>
                <el-col :span="12">
                  <span style="color: #646a73">执行时间：</span>
                  <span style="color: #1677ff;">{{ parseTime(val.createTime) }}</span>
                </el-col>
              </el-row>
              <el-divider v-if="(valindex + 1) != item.dataList.length"></el-divider>
            </div>
          </el-collapse-item>
        </el-collapse>
        <el-empty v-else description="暂无数据"></el-empty>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import {getRecodeLog} from "@/api/warning/warning-record";

export default {
  name: "warningLog",
  data() {
    return {
      title: '动作日志',
      dialogFlag: false,
      actionLoading: false,
      dataForm: [],
      warningConfid: {},
    }
  },
  methods: {
    init(row) {
      this.dialogFlag = true;
      this.actionLoading = true
      getRecodeLog(row.configId, row.warningTag).then(res => {
        this.actionLoading = false
        this.$set(this, 'dataForm', res.data);
        this.dataForm = res.data;
      })
    },
    cancel() {
      this.dialogFlag = false;
      this.dataForm = {};
      this.$emit('restOnLoad');
    },
  }
}
</script>

<style scoped>
.successStatus {
  color: #67C23A;
}

.failStatus {
  color: #F56C6C;
}

.action-log ::v-deep .el-collapse-item__header {
  //font-size: 16px;
  //font-weight: 700;
}

.product-form ::v-deep .el-form-item__label {
  color: #3f4448;
  font-weight: 400;
}

.product-form ::v-deep .el-textarea__inner {
  resize: none;
  min-height: 111.6px !important;
}
</style>
