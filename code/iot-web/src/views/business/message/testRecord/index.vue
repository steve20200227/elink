<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item>
        <el-select v-model="queryParams.messageStatus" placeholder="请选择消息状态" clearable size="mini">
          <el-option
            v-for="item in messageStatusDict"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="queryParams.channelType" placeholder="请选择通道类型" clearable size="mini">
          <el-option
            v-for="item in messageChannelDict"
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
      <el-row style="margin-top: 20px">
        <div class="warning-card">
          <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="8" v-for="(item, index) in list" :key="index">
            <el-card shadow="always" :class="{'record-success': item.messageStatus == 1, 'record-danger': item.messageStatus == 0}" style="position: relative;font-size: 14px; padding-bottom: 10px; margin-right: 10px;margin-bottom: 10px;">
              <div>
                <div>
                  <div class="point-content" style="margin-bottom: 10px">
                    <div style="margin: 16px 6px 0 0px;position: relative; z-index: 10;">
                      <el-row style="margin-bottom: 10px">
                        <el-col :span="12">
                          <span style="color: #646a73">模板名称：</span>
                          <span style="color: #1677ff;">{{ item.templateName }}</span>
                        </el-col>
                        <el-col :span="12">
                          <span style="color: #646a73">通道名称：</span>
                          <span style="color: #1677ff;">{{ item.appName }}</span>
                        </el-col>
                      </el-row>
                      <el-row style="margin-bottom: 10px">
                        <el-col :span="12">
                          <span style="color: #646a73">用户类型：</span>
                          <span style="color: #1677ff;">
                            <dict-tag :type="DICT_TYPE.MESSAGE_USER_TYPE" :value="item.userType"/>
                          </span>
                        </el-col>
                        <el-col :span="12">
                          <span style="color: #646a73">消息推送人：</span>
                          <span style="color: #1677ff;" v-if="item.channelType == '0'">{{ item.nickName }}</span>
                          <span style="color: #1677ff;" v-else>{{ item.pushUser }}</span>
                        </el-col>
                      </el-row>
                      <el-row style="margin-bottom: 10px">
                        <el-col :span="12">
                          <span style="color: #646a73">发送时间：</span>
                          <span style="color: #1677ff;">{{ parseTime(item.createTime) }}</span>
                        </el-col>
                        <el-col :span="12">
                          <span style="color: #646a73">发送通道类型：</span>
                          <span style="color: #1677ff;">
                            <dict-tag :type="DICT_TYPE.MESSAGE_CHANNEL" :value="item.channelType"/>
                          </span>
                        </el-col>
                      </el-row>
                    </div>
                  </div>
                </div>
              </div>
              <div v-if="item.messageStatus == 1" style="position:absolute; top: 0px; right: -15px;font-size: 100px;z-index: 0;">
                <svg-icon icon-class="successSeal" class-name="processed-icon" style="position:absolute; top: 5px; right: 30px;margin-top: 10px;margin-left: 30px;text-align: center;" />
              </div>
              <div v-if="item.messageStatus == 0" style="position:absolute; top: 0px; right: -15px;font-size: 100px;z-index: 0;">
                <svg-icon icon-class="failSeal" class-name="untreated-icon" style="position:absolute; top: 5px; right: 30px;margin-top: 10px;margin-left: 30px;text-align: center;" />
              </div>
            </el-card>
          </el-col>
        </div>
      </el-row>

<!--      <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">

        <el-table-column label="消息推送人" align="center" prop="pushUser" />
        <el-table-column label="发送时间" align="center" prop="createTime" width="180">
          <template v-slot="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>



        <el-table-column label="消息状态" align="center" prop="messageStatus" >
          <template slot-scope="scope">
            <el-tag type="danger" v-if="scope.row.messageStatus === 0">
              <dict-tag :type="DICT_TYPE.MESSAGE_STATUS" :value="scope.row.messageStatus"/>
            </el-tag>
            <el-tag type="success" v-else>
              <dict-tag :type="DICT_TYPE.MESSAGE_STATUS" :value="scope.row.messageStatus"/>
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发送渠道类型" align="center" prop="channelType" >
          <template slot-scope="scope">
            <el-tag >
              <dict-tag :type="DICT_TYPE.MESSAGE_CHANNEL" :value="scope.row.channelType"/>
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="模板名称" align="center" prop="templateName" />
        <el-table-column label="通道名称" align="center" prop="appName" />

        <el-table-column label="用户类型" align="center" prop="userType" >
          <template slot-scope="scope">
            <el-tag >
              <dict-tag :type="DICT_TYPE.MESSAGE_USER_TYPE" :value="scope.row.userType"/>
            </el-tag>
          </template>
        </el-table-column>
        &lt;!&ndash;      <el-table-column label="消息类型" align="center" prop="messageType" />&ndash;&gt;

        &lt;!&ndash;      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template v-slot="scope">
                  <el-button size="mini" type="text" icon="el-icon-setting" @click="openForm(scope.row.traceId)"
                             >查看</el-button>

                </template>
              </el-table-column>&ndash;&gt;
      </el-table>-->
    </div>

    <!-- 分页组件 -->
    <pagination v-if="list.length!=0" :total="total" :page-sizes="[12, 24, 36, 48, 120]" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <el-empty
      v-if="list.length==0"
      style="width: 100%; margin-top: 50px"
      description="暂无数据"
      :image-size="120"
    ></el-empty>
  </div>
</template>

<script>
import {getRecordPage} from '@/api/message/record'
export default {
  name: 'index',
  data() {
    return {
      messageStatusDict:[],
      messageChannelDict:[],
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
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
        pageNo: 1,
        pageSize: 12,
        traceId: null,
        templateId: null,
        appId: null,
        messageStatus: null,
        userType: null,
        pushUser: null,
        channelType: null,
        messageType: null,
        pushRange: null,
        retried: null,
        createTime: [],
      },
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    this.messageStatusDict = this.$store.state.dict.dictDatas.message_status
    this.messageChannelDict = this.$store.state.dict.dictDatas.message_channel

  },
  methods: {
    /** 查询列表 */
    async getList() {
      try {
        this.loading = true;
        const res = await getRecordPage(this.queryParams);
        this.list = res.data.records;
        this.total = res.data.total;
      } finally {
        this.loading = false;
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {}
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 添加/修改操作 */
    openForm(id) {
      this.$refs["formRef"].open(id);
    },
    /** 删除按钮操作 */
    async handleDelete(row) {
      const traceId = row.traceId;
      await this.$modal.confirm('是否确认删除消息记录编号为"' + traceId + '"的数据项?')
      try {
        await RecordApi.deleteRecord(traceId);
        await this.getList();
        this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有消息记录数据项?');
      try {
        this.exportLoading = true;
        const res = await RecordApi.exportRecordExcel(this.queryParams);
        this.$download.excel(res.data, '消息记录.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
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

.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
}
</style>
