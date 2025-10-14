<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item>
        <el-select v-model="queryParams.outputStatus" placeholder="请选择状态" clearable size="mini">
          <el-option
            v-for="item in messageStatusDict"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="queryParams.outputWay" placeholder="请选择输出方式" clearable size="mini">
          <el-option
            v-for="item in outputWayDict"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="top-button">
        <el-button size="mini" @click="handleQuery" icon="el-icon-search"></el-button>
        <el-button size="mini" @click="resetQuery" icon="el-icon-refresh-right"></el-button>
      </el-form-item>
    </el-form>



    <div style="height: calc(100vh - 245px); overflow: auto" v-if="list.length!=0">
      <el-row>
        <div>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8" v-for="(item, index) in list" :key="index">
            <el-card shadow="always" :class="{'record-success': item.outputStatus == 1, 'record-danger': item.outputStatus == 0}" style="position: relative;font-size: 14px; margin-right: 10px;margin-bottom: 10px;margin-top: 5px">
              <div>
                <div>
                  <div class="point-head" style="position: relative;font-size: 18px; font-weight: bold; margin-bottom: 10px;">
                    {{ item.actionName }}
                  </div>
                  <div class="point-content" style="margin-bottom: 10px">
                    <div style="margin: 16px 6px 0 0px;position: relative; z-index: 10;">
                      <el-row style="margin-bottom: 10px">
                        <el-col :span="12">
                          <span style="color: #646a73">推送地址：</span>
                          <span style="color: #1677ff;">{{ item.pushUrl }}</span>
                        </el-col>
                        <el-col :span="12" v-if="item.requestType != null && item.requestType != ''">
                          <span style="color: #646a73">请求方式：</span>
                          <span style="color: #1677ff;">
                            {{ item.requestType }}
                          </span>
                        </el-col>

                      </el-row>
                      <el-row style="height: 20px;">
                        <el-col :span="12">
                          <span style="color: #646a73">请求时间：</span>
                          <span style="color: #1677ff;">{{ parseTime(item.createTime) }}</span>
                        </el-col>
                        <el-col :span="12" v-if="item.outputStatus == 0">
                          <span style="color: #646a73">失败原因：</span>
                          <span style="color: #1677ff;" v-if="item.channelType == '0'">{{ item.nickName }}</span>
                          <span style="color: #1677ff;" v-else>{{ item.failReason }}</span>
                        </el-col>
                      </el-row>
                    </div>
                  </div>
                </div>
                <div style="padding: 0 10px 0 10px; z-index: 10;">
                  <div class="record-button" style="font-size: 15px;width: 100%;text-align: center">
                      <el-button size="mini" type="info"  style="margin: 5px; width: 100px;" plain
                                 @click="viewParam(item.requestParam)">查 看 参 数
                      </el-button>
                  </div>
                </div>
              </div>

              <div v-if="item.outputStatus == 1" style="position:absolute; top: 0px; right: -15px;font-size: 100px;z-index: 0;">
                <svg-icon icon-class="successSeal" class-name="processed-icon" style="position:absolute; top: 5px; right: 30px;margin-top: 10px;margin-left: 30px;text-align: center;" />
              </div>
              <div v-if="item.outputStatus == 0" style="position:absolute; top: 0px; right: -15px;font-size: 100px;z-index: 0;">
                <svg-icon icon-class="failSeal" class-name="untreated-icon" style="position:absolute; top: 5px; right: 30px;margin-top: 10px;margin-left: 30px;text-align: center;" />
              </div>
            </el-card>
          </el-col>
        </div>
      </el-row>

    </div>
    <!-- 分页组件 -->
    <el-pagination
      v-if="list.length!=0"
      background
      :current-page="page.pageNo"
      :page-sizes="[12, 24, 36, 48, 120]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.total"
      :page-size="page.pageSize"
      :pager-count="5"
      @size-change="changeSize"
      @current-change="changeCurrent"
      style="float: right;">
    </el-pagination>
    <el-empty
      v-if="list.length==0"
      style="width: 100%; margin-top: 50px"
      description="暂无数据"
      :image-size="120"
    ></el-empty>
    <el-dialog
      title="查看参数"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="45%"

    >
      <div style="margin: 10px 0;">

        <pre><code>{{ requestParam }}</code></pre>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {pageRecord as pageRecord} from '@/api/warning/warning-north-api'
export default {
  name: 'index',
  data() {
    return {
      messageStatusDict:[],
      outputWayDict: [],
      requestParam:"",
      dialogFlag:false,
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 消息记录列表
      list: [],
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 选中行
      currentRow: {},
      // 查询参数
      queryParams: {
        outputStatus: null
      },
      page: {
        pageNo: 1,
        pageSize: 12,
        total: 0
      },
    };
  },
  created() {
    const actionId = this.$route.query.actionId;
    if (actionId!=null){
      this.queryParams.actionId = actionId
    }
    this.getList();
  },
  mounted() {
    this.messageStatusDict = this.$store.state.dict.dictDatas.message_status
    this.outputWayDict = this.$store.state.dict.dictDatas.output_way

  },
  methods: {
    viewParam(data){
      this.dialogFlag = true
      this.requestParam = JSON.parse(data)
    },
    /** 查询列表 */
    async getList() {
      try {
        this.loading = true;
        const res = await pageRecord(this.page.pageNo,this.page.pageSize,this.queryParams);
        this.list = res.data.records;
        this.page.total = res.data.total;
      } finally {
        this.loading = false;
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.page.pageNo = 1;
      this.getList();
    },
    changeCurrent (val) {
      this.page.pageNo = val
      this.getList()
    },
    changeSize (val) {
      this.page.pageSize = val
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {}

      this.resetForm("queryForm");
      const actionId = this.$route.query.actionId;
      if (actionId!=null){
        this.queryParams.actionId = actionId
      }
      this.handleQuery();
    }
  }
}
</script>

<style scoped>
.record-danger {
  background: linear-gradient(40deg, #fff, #fef0f0, #fde2e2);
}

.record-success {
  background: linear-gradient(40deg, #fff, #eef6ff, #b3d9ff);;
}
.record-button ::v-deep .el-button {
  width: 95%;
}
.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
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
</style>
