<template>
  <div class="app-container">
    <doc-alert title="系统日志" url="https://doc.iocoder.cn/system-log/" />
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" v-show="showSearch" label-width="68px" class="custom-form" style="margin-bottom: 20px">
      <el-form-item prop="userId" style="margin-top: -1px">
        <el-input v-model="queryParams.userId" placeholder="请输入用户编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="userType" style="margin-top: -1px">
        <el-select v-model="queryParams.userType" placeholder="请选择用户类型" clearable>
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.USER_TYPE)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item prop="applicationName" style="margin-top: -1px">
        <el-input v-model="queryParams.applicationName" placeholder="请输入应用名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="requestUrl" style="margin-top: -1px">
        <el-input v-model="queryParams.requestUrl" placeholder="请输入请求地址" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="exceptionTime" style="margin-top: -1px">
        <el-date-picker v-model="queryParams.exceptionTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item class="top-button" style="margin-top: -1px">
        <el-button size="mini" @click="handleQuery" icon="el-icon-search"></el-button>
        <el-button size="mini" @click="resetQuery" icon="el-icon-refresh-right"></el-button>
      </el-form-item>
    </el-form>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list" style="height: calc(100vh - 240px);overflow-y: auto;">
      <el-table-column label="日志编号" align="center" prop="id" />
      <el-table-column label="用户编号" align="center" prop="userId" />
      <el-table-column label="用户类型" align="center" prop="userType">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType"/>
        </template>
      </el-table-column>>
      <el-table-column label="应用名" align="center" prop="applicationName" />
      <el-table-column label="请求方法名" align="center" prop="requestMethod" />
      <el-table-column label="请求地址" align="center" prop="requestUrl" width="250" />
      <el-table-column label="异常发生时间" align="center" prop="exceptionTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.exceptionTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="异常名" align="center" prop="exceptionName" width="250" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row,scope.index)"
                     v-hasPermi="['infra:api-access-log:query']">详细</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 查看明细 -->
    <el-dialog title="API 异常日志详细" :visible.sync="open" width="1280px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="24">
            <el-form-item label="日志主键：">{{ form.id }}</el-form-item>
            <el-form-item label="链路追踪：">{{ form.traceId }}</el-form-item>
            <el-form-item label="应用名：">{{ form.applicationName }}</el-form-item>
            <el-form-item label="用户信息：">
              {{ form.userId }} <dict-tag :type="DICT_TYPE.USER_TYPE" :value="form.userType" /> | {{ form.userIp }} | {{ form.userAgent}}
            </el-form-item>
            <el-form-item label="请求信息：">{{ form.requestMethod }} | {{ form.requestUrl }} </el-form-item>
            <el-form-item label="请求参数：">{{ form.requestParams }}</el-form-item>
            <el-form-item label="异常时间：">{{ parseTime(form.exceptionTime) }}</el-form-item>
            <el-form-item label="异常名">{{ form.exceptionName }}</el-form-item>
            <el-form-item label="异常名">
              <el-input type="textarea" :readonly="true" :autosize="{ maxRows: 20}" v-model="form.exceptionStackTrace"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { updateApiErrorLogProcess, getApiErrorLogPage, exportApiErrorLogExcel } from "@/api/infra/apiErrorLog";
import { InfraApiErrorLogProcessStatusEnum } from '@/utils/constants'

export default {
  name: "InfraApiErrorLog",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // API 错误日志列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        userId: null,
        userType: null,
        applicationName: null,
        requestUrl: null,
        processStatus: null,
        exceptionTime: []
      },
      // 表单参数
      form: {},
      // 枚举
      InfApiErrorLogProcessStatusEnum: InfraApiErrorLogProcessStatusEnum,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getApiErrorLogPage(this.queryParams).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 表单重置 */
    reset() {
      this.form = {};
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    }
  }
};
</script>
<style scoped>
.custom-form {
  display: inline-block;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
}

.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
}
</style>
