<template>
  <div style="margin: 20px">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="条件" name="condition">
        <div style="margin-top: 5px">
          <el-form ref="gridHeadLayout" :inline="true" :model="formInline" class="demo-form-inline">
            <el-form-item>
              <el-input
                v-model="formInline.configName"
                size="mini"
                placeholder="请输入场景名称">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="formInline.equipmentName"
                size="mini"
                placeholder="请输入设备名称">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="formInline.status" size="mini" style="width: 100%" placeholder="请选择记录状态">
                <el-option
                  v-for="item in recordDict"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value * 1">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                size="mini"
                v-model="formInline.createTime"
                type="daterange"
                range-separator="至"
                value-format="yyyy-MM-dd"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item class="top-button">
              <el-button size="mini" @click="handleSearch" icon="el-icon-search"></el-button>
              <el-button size="mini" @click="handleEmpty" icon="el-icon-refresh-right"></el-button>
            </el-form-item>
          </el-form>
        </div>

        <div>
          <div style="max-height: calc(100vh - 290px);height: calc(100vh - 285px); overflow: auto;" v-if="data.length!=0">
            <el-row style="margin-top: 20px">
              <div class="warning-card">
                <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8" v-for="(item, index) in data" :key="index">
                  <el-card shadow="always"
                           :class="{'record-success': (item.status == 1), 'record-danger': (item.status == 0)}"
                           style="position: relative;font-size: 14px; padding-bottom: 10px; margin-right: 10px;margin-bottom: 10px;">
                    <div style="margin-bottom: 10px;">
                      <div style="padding: 20px 20px 5px 20px">
                        <div class="point-head" style="position: relative;font-size: 18px; font-weight: bold; margin-bottom: 10px;">
                          {{ item.configName }}
                        </div>
                        <div class="point-content" style="margin-bottom: 10px">
                          <div style="margin: 16px 6px 0 0px;">
                            <el-row>
                              <el-col :span="24">
                                <span style="color: #646a73">设备名称：</span>
                                <span style="color: #1677ff;">{{ item.equipmentName }}</span>
                              </el-col>
                              <el-col :span="24">
                                <span style="color: #646a73">场景触发时间：</span>
                                <span style="color: #1677ff;">{{ parseTime(item.createTime) }}</span>
                              </el-col>
                            </el-row>
                          </div>
                        </div>
                      </div>
                      <div style="padding: 0 20px 0 20px;position: relative; z-index: 10;">
                        <div class="record-button" style="font-size: 14px">
                          <el-row>
                            <el-col :span="8">
                              <el-button size="small" type="info" v-if="item.status == 0" style="margin: 5px" plain
                                         @click="handle(item)">处 理
                              </el-button>
                            </el-col>
                            <el-col :span="8">
                              <el-button size="small" type="info" v-if="item.status == 1" style="margin: 5px" plain
                                         @click="handleLog(item)">处 理 日 志
                              </el-button>
                            </el-col>
                            <el-col :span="8">
                              <el-button size="small" type="info" v-if="item.warningType != '3'" style="margin: 5px" plain
                                         @click="warningLog(item)">场 景 日 志
                              </el-button>
                            </el-col>
                            <el-col :span="8">
                              <el-button size="small" type="info" style="margin: 5px" plain
                                         @click="actionLog(item)">动 作 日 志
                              </el-button>
                            </el-col>
                          </el-row>
                        </div>
                      </div>
                    </div>
                    <div v-if="item.warningType == '1'">
                      <div v-if="item.status == 1" style="position:absolute; top: -10px; right: -15px;font-size: 100px;z-index: 0;">
                        <svg-icon icon-class="processed" class-name="processed-icon" style="position:absolute; top: 5px; right: 30px;margin-top: 10px;margin-left: 30px;text-align: center;" />
                      </div>
                      <div v-if="item.status == 0" style="position:absolute; top: -10px; right: -15px;font-size: 100px;z-index: 0;">
                        <svg-icon icon-class="untreated" class-name="untreated-icon" style="position:absolute; top: 5px; right: 30px;margin-top: 10px;margin-left: 30px;text-align: center;" />
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </div>
            </el-row>
          </div>
          <div class="block" style="float: right;" v-if="data.length!=0">
            <el-pagination
              background
              :current-page="page.currentPage"
              :page-sizes="[9,18,27,36,72]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="page.total"
              :page-size="page.pageSize"
              :pager-count="5"
              @size-change="changeSize"
              @current-change="changeCurrent">
            </el-pagination>
          </div>

          <el-empty
            v-if="data.length==0"
            style="width: 100%; margin-top: 50px"
            description="暂无数据"
            :image-size="120"
          ></el-empty>
        </div>
      </el-tab-pane>
      <el-tab-pane label="定时" name="fixedTime">
        <div style="margin-top: 5px">
          <el-form ref="gridHeadLayout" :inline="true" :model="formInline" class="demo-form-inline">
            <el-form-item>
              <el-input
                v-model="formInline.configName"
                size="mini"
                placeholder="请输入场景名称">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                size="mini"
                v-model="formInline.createTime"
                type="daterange"
                range-separator="至"
                value-format="yyyy-MM-dd"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item class="top-button">
              <el-button size="mini" @click="handleSearch" icon="el-icon-search"></el-button>
              <el-button size="mini" @click="handleEmpty" icon="el-icon-refresh-right"></el-button>
            </el-form-item>
          </el-form>
        </div>

        <div>
          <div style="max-height: calc(100vh - 290px);height: calc(100vh - 285px); overflow: auto;" v-if="data.length!=0">
            <el-row style="margin-top: 20px">
              <div class="warning-card">
                <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8" v-for="(item, index) in data" :key="index">
                  <el-card shadow="always"
                           class="record-info"
                           style="position: relative;font-size: 14px; padding-bottom: 10px; margin-right: 10px;margin-bottom: 10px;">
                    <div style="margin-bottom: 10px;">
                      <div style="padding: 20px 20px 5px 20px">
                        <div class="point-head" style="position: relative;font-size: 18px; font-weight: bold; margin-bottom: 10px;">
                          {{ item.configName }}
                        </div>
                        <div class="point-content" style="margin-bottom: 10px">
                          <div style="margin: 16px 6px 0 0px;">
                            <span style="color: #646a73">场景触发时间：</span>
                            <span style="color: #1677ff;">{{ parseTime(item.createTime) }}</span>
                          </div>
                        </div>
                      </div>
                      <div style="padding: 0 20px 0 20px;position: relative; z-index: 10;">
                        <div class="record-button" style="font-size: 14px; width: 100%;text-align: center">
                          <el-button size="small" type="info" style="margin: 5px; width: 140px;" plain
                                     @click="actionLog(item)">动 作 日 志
                          </el-button>
                        </div>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </div>
            </el-row>
          </div>
          <div class="block" style="float: right;" v-if="data.length!=0">
            <el-pagination
              background
              :current-page="page.currentPage"
              :page-sizes="[9,18,27,36,72]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="page.total"
              :page-size="page.pageSize"
              :pager-count="5"
              @size-change="changeSize"
              @current-change="changeCurrent">
            </el-pagination>
          </div>

          <el-empty
            v-if="data.length==0"
            style="width: 100%; margin-top: 50px"
            description="暂无数据"
            :image-size="120"
          ></el-empty>
        </div>
      </el-tab-pane>
      <el-tab-pane label="手动" name="handMovemnet">
        <div style="margin-top: 5px">
          <el-form ref="gridHeadLayout" :inline="true" :model="formInline" class="demo-form-inline">
            <el-form-item>
              <el-input
                v-model="formInline.configName"
                size="mini"
                placeholder="请输入场景名称">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                size="mini"
                v-model="formInline.createTime"
                type="daterange"
                range-separator="至"
                value-format="yyyy-MM-dd"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item class="top-button">
              <el-button size="mini" @click="handleSearch" icon="el-icon-search"></el-button>
              <el-button size="mini" @click="handleEmpty" icon="el-icon-refresh-right"></el-button>
            </el-form-item>
          </el-form>
        </div>

        <div>
          <div style="max-height: calc(100vh - 290px);height: calc(100vh - 285px); overflow: auto;" v-if="data.length!=0">
            <el-row style="margin-top: 20px">
              <div class="warning-card">
                <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8" v-for="(item, index) in data" :key="index">
                  <el-card shadow="always"
                           class="record-info"
                           style="position: relative;font-size: 14px; padding-bottom: 10px; margin-right: 10px;margin-bottom: 10px;">
                    <div style="margin-bottom: 10px;">
                      <div style="padding: 20px 20px 5px 20px">
                        <div class="point-head" style="position: relative;font-size: 18px; font-weight: bold; margin-bottom: 10px;">
                          {{ item.configName }}
                        </div>
                        <div class="point-content" style="margin-bottom: 10px">
                          <div style="margin: 16px 6px 0 0px;">
                            <span style="color: #646a73">场景触发时间：</span>
                            <span style="color: #1677ff;">{{ parseTime(item.createTime) }}</span>
                          </div>
                        </div>
                      </div>
                      <div style="padding: 0 20px 0 20px;position: relative; z-index: 10;">
                        <div class="record-button" style="font-size: 14px; width: 100%; text-align: center">
                          <el-button size="small" type="info" style="margin: 5px; width: 140px;" plain
                                     @click="actionLog(item)">动 作 日 志
                          </el-button>
                        </div>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </div>
            </el-row>
          </div>
          <div class="block" style="float: right;" v-if="data.length!=0">
            <el-pagination
              background
              :current-page="page.currentPage"
              :page-sizes="[9,18,27,36,72]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="page.total"
              :page-size="page.pageSize"
              :pager-count="5"
              @size-change="changeSize"
              @current-change="changeCurrent">
            </el-pagination>
          </div>

          <el-empty
            v-if="data.length==0"
            style="width: 100%; margin-top: 50px"
            description="暂无数据"
            :image-size="120"
          ></el-empty>
        </div>
      </el-tab-pane>
    </el-tabs>

    <RecordUpdateStatusDialog
      ref="recordStatus"
      @restOnLoad="restOnLoad">
    </RecordUpdateStatusDialog>

    <RecordLogDialog
      ref="recordLog"
      @restOnLoad="restOnLoad">
    </RecordLogDialog>
    <WarningLogDialog
      ref="warningLog"
      @restOnLoad="restOnLoad">
    </WarningLogDialog>
    <actionLogDialog
      ref="actionLog">
    </actionLogDialog>

  </div>
</template>

<script>
import {page as getPage} from "@/api/warning/warning-record";
import RecordUpdateStatusDialog from "@/views/business/warning/record/components/recordUpdateStatusDialog.vue";
import RecordLogDialog from "@/views/business/warning/record/components/recordLogDialog.vue";
import WarningLogDialog from "@/views/business/warning/record/components/warningLogDialog.vue";
import actionLogDialog from "@/views/business/warning/record/components/actionLogDialog.vue";

export default {
  name: "record",
  computed: {
    ids() {
      let ids = [];
      this.selectionList.forEach(ele => {
        ids.push(ele.id);
      });
      return ids.join(",");
    },
  },
  components: {
    RecordUpdateStatusDialog,
    RecordLogDialog,
    WarningLogDialog,
    actionLogDialog
  },
  data() {
    return {
      activeName: 'condition',
      tableLoading: false,
      data: [],
      selectionList: [], // 表格多选框
      formInline: {},
      recordDict: [], // 场景记录状态字典
      page: {
        pageSize: 9,
        currentPage: 1,
        total: 0
      },
    }
  },
  mounted() {
    this.formInline.warningType = "1"
    this.onLoad(this.page, {});
    this.recordDict = this.$store.state.dict.dictDatas.record_status
  },
  methods: {
    handleClick(tab, event) {
      this.activeName = tab.name
      this.formInline = {}
      if (tab.name == "condition") {
        this.formInline.warningType = "1"
        this.page.pageSize = 9
        this.page.currentPage = 1
      }
      if (tab.name == "fixedTime") {
        this.formInline.warningType = "2"
        this.page.pageSize = 9
        this.page.currentPage = 1
      }
      if (tab.name == "handMovemnet") {
        this.formInline.warningType = "3"
        this.page.pageSize = 9
        this.page.currentPage = 1
      }
      this.onLoad(this.page, {})
    },
    restOnLoad() {
      this.onLoad(this.page, {})
    },
    actionLog(row) {
      this.$refs.actionLog.init(row)
    },
    warningLog(row) {
      this.$refs.warningLog.init(row)
    },
    // 点击处理日志按钮
    handleLog(row) {
      this.$refs.recordLog.init(row)
    },
    // 点击处理按钮
    handle(row) {
      this.$refs.recordStatus.init(row)
    },
    handleSearch () {
      this.page.currentPage = 1
      if (this.formInline.createTime) {
        this.formInline.createTimes = this.formInline.createTime.join(",")
      }
      this.onLoad(this.page, this.formInline)
    },
    handleEmpty () {
      let warningType = this.formInline.warningType
      this.formInline = {}
      this.formInline.warningType = warningType
      this.onLoad(this.page)
    },
    handleSelectionChange (selection) {
      this.selectionList = selection
    },
    changeCurrent (val) {
      this.page.currentPage = val
      this.onLoad(this.page, {})
    },
    changeSize (val) {
      this.page.pageSize = val
      this.onLoad(this.page, {})
    },
    indexMethod (index) {
      return index + 1
    },
    // 清空选项框
    // toggleSelection() {
    //   this.$refs.productTable.clearSelection();
    // },
    onLoad(page, params = {}) {
      this.page = page;
      this.tableLoading = true;
      getPage(
        page.currentPage,
        page.pageSize,
        this.formInline
      ).then(res => {
        const data = res.data;
        this.page.total = data.total;
        this.data = data.records;
        this.tableLoading = false;
        // this.toggleSelection();
      });
    },
  }
};
</script>
<style scoped>
.record-button ::v-deep .el-button {
  width: 95%;
}

.warning-card ::v-deep .el-card__body {
  padding: 0 !important;
}

.warning-card ::v-deep .el-divider--horizontal {
  margin: 0;
}

.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
}

.record-danger {
  background: linear-gradient(40deg, #fff, #fef0f0, #fde2e2);
}

.record-success {
  background: linear-gradient(40deg, #fff, #eef6ff, #b3d9ff);;
}
.record-info {
  background: linear-gradient(40deg, #fff, #f4f4f5, #e9e9ed);
}
.demo-form-inline ::v-deep .el-form-item{
  margin-bottom: 0;
}
</style>
