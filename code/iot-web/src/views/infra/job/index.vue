<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <div style="margin-bottom: 20px">
      <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" v-show="showSearch" label-width="100px" class="custom-form">
        <el-form-item prop="jobDesc" style="margin-top: -1px">
          <el-input v-model="queryParams.jobDesc" placeholder="请输入任务名称" clearable @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item prop="triggerStatus" style="margin-top: -1px">
          <el-select v-model="queryParams.triggerStatus" placeholder="请选择任务状态" clearable @keyup.enter.native="handleQuery">
            <el-option v-for="dict in getDictDatas(DICT_TYPE.INFRA_JOB_STATUS_CN)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
          </el-select>
        </el-form-item>
        <el-form-item prop="executorHandler" style="margin-top: -1px">
          <el-input v-model="queryParams.executorHandler" placeholder="请输入处理器的名字" clearable @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item class="top-button" style="margin-top: -1px">
          <el-button size="mini" @click="handleQuery" icon="el-icon-search"></el-button>
          <el-button size="mini" @click="resetQuery" icon="el-icon-refresh-right"></el-button>
        </el-form-item>
        <el-form-item class="export-button" style="margin-top: -4px">
          <el-button size="mini" type="primary" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

<!--    <el-row :gutter="10" class="mb8" style="margin-top: 5px">-->
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
<!--    </el-row>-->

    <el-table v-loading="loading" :data="jobList" style="height: calc(100vh - 240px);overflow-y: auto;" v-if="jobList.length!=0">
      <el-table-column label="任务编号" align="center" prop="id" />
      <el-table-column label="任务名称" align="center" prop="jobDesc" />
      <el-table-column label="任务状态" align="center" prop="triggerStatus">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.INFRA_JOB_STATUS_CN" :value="scope.row.triggerStatus" />
        </template>
      </el-table-column>>
      <el-table-column label="执行器" align="center" prop="jobGroup">
        <template slot-scope="scope">
          {{dictHandler.find(e=> e.id == scope.row.jobGroup).title}}
        </template>
      </el-table-column>
      <el-table-column label="处理器" align="center" prop="executorHandler" />
      <el-table-column label="处理器的参数" align="center" prop="executorParam" />
      <el-table-column label="CRON 表达式" align="center" prop="scheduleConf" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-setting" @click="handleUpdate(scope.row)"
                     v-hasPermi="['infra:job:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-check" @click="handleStop(scope.row)"
                     v-if="scope.row.triggerStatus === InfJobStatusEnum.NORMAL" v-hasPermi="['infra:job:update']">暂停</el-button>
          <el-button size="mini" type="text" icon="el-icon-close" @click="handleStart(scope.row)"
                     v-if="scope.row.triggerStatus === InfJobStatusEnum.INIT" v-hasPermi="['infra:job:update']">开启</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['infra:job:delete']">删除</el-button>
          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)"
                       v-hasPermi="['infra:job:trigger', 'infra:job:query']">
            <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
            <el-dropdown-menu slot="dropdown">
<!--              <el-dropdown-item command="handleStop" icon="el-icon-caret-right"
                                 v-if="scope.row.triggerStatus==1">停止</el-dropdown-item>
              <el-dropdown-item command="handleStart" icon="el-icon-caret-right"
                                 v-if="scope.row.triggerStatus==0">开启</el-dropdown-item>-->
              <el-dropdown-item command="handleRun" icon="el-icon-caret-right"
                                v-hasPermi="['infra:job:trigger']">执行一次</el-dropdown-item>
              <el-dropdown-item command="handleView" icon="el-icon-view"
                                v-hasPermi="['infra:job:query']">任务详细</el-dropdown-item>
              <el-dropdown-item command="handleJobLog" icon="el-icon-s-operation"
                                v-hasPermi="['infra:job:query']">调度日志</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <div class="block" style="float: right;" v-if="jobList.length!=0">
      <el-pagination
        background
        :current-page="page.currentPage"
        :page-sizes="[10, 20, 30, 50, 100]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      >
      </el-pagination>
    </div>
    <el-empty
      v-if="jobList.length==0"
      style="width: 100%; margin-top: 50px"
      description="暂无数据"
      :image-size="120"
    ></el-empty>
    <!-- 添加或修改定时任务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="任务名称" prop="jobDesc">
          <el-input v-model="form.jobDesc" maxlength="100" show-word-limit placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="执行器" prop="jobGroup">
          <el-select style="width: 100%" v-model="form.jobGroup" placeholder="请输入执行器" v-bind:readonly="form.id !== undefined" >
            <el-option v-for="(item, index) in dictHandler" :key="index" :label="item.title" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理器" prop="executorHandler">
          <el-input v-model="form.executorHandler"  maxlength="100" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="处理器的参数" prop="executorParam">
          <el-input v-model="form.executorParam"  maxlength="500" show-word-limit placeholder="请输入处理器的参数" />
        </el-form-item>
        <el-form-item label="CRON 表达式" prop="scheduleConf">
          <el-input v-model="form.scheduleConf" disabled="true" placeholder="请输入CRON 表达式">
            <template slot="append">
              <el-button type="primary" @click="handleShowCron">
                生成表达式
                <i class="el-icon-time el-icon--right"></i>
              </el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="重试次数" prop="executorFailRetryCount">
          <el-input-number v-model="form.executorFailRetryCount" controls-position="right" :precision="0" :min="0"></el-input-number>

        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Cron表达式生成器" :visible.sync="openCron" append-to-body class="scrollbar" destroy-on-close>
      <crontab @hide="openCron=false" @fill="crontabFill" :expression="expression"></crontab>
    </el-dialog>

    <!-- 任务详细 -->
    <el-dialog title="任务详细" :visible.sync="openView" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="200px" size="mini">
        <el-row>
          <el-col :span="24">
            <el-form-item label="任务编号：">{{ form.id }}</el-form-item>
            <el-form-item label="任务名称：">{{ form.jobDesc }}</el-form-item>
            <el-form-item label="执行器" >
              {{ dictHandler.find(e=>e.id == form.jobGroup)?dictHandler.find(e=>e.id == form.jobGroup).title : ''}}
            </el-form-item>
            <el-form-item label="处理器：">{{ form.executorHandler }}</el-form-item>
            <el-form-item label="处理器的参数：">{{ form.executorParam }}</el-form-item>
            <el-form-item label="cron表达式：">{{ form.scheduleConf }}</el-form-item>
            <el-form-item label="重试次数：">{{ form.executorFailRetryCount }}</el-form-item>
            <el-form-item label="后续执行时间："><span v-html="Array.from(nextTimes, x => parseTime(x)).join('; <br>')"></span></el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listJob,
  delJob,
  addJob,
  updateJob,
  exportJob,
  runJob,
  updateJobStatus,
  getJobNextTimes,
  listHandlerJob,
  handlerStop, handlerStart
} from "@/api/infra/job";
import { InfraJobStatusEnum } from "@/utils/constants";
import Crontab from '@/components/Crontab'
import {DICT_TYPE, getDictDatas} from "@/utils/dict";

export default {
  computed: {
    DICT_TYPE() {
      return DICT_TYPE
    }
  },
  components: { Crontab },
  name: "InfraJob",
  data() {
    return {
      expression: '',
      dictHandler:[],
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 定时任务表格数据
      jobList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示详细弹出层
      openView: false,
      // 是否显示Cron表达式弹出层
      openCron: false,
      // 传入的表达式
      scheduleConf: "",
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        start: 0,
        length: 10,
        triggerStatus: '-1',
        executorHandler: ''
      },
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        jobDesc: [{ required: true, message: "任务名称不能为空", trigger: "blur" }],
        executorHandler: [{ required: true, message: "处理器的名字不能为空", trigger: "blur" }],
        scheduleConf: [{ required: true, message: "CRON 表达式不能为空", trigger: "blur" }],
      },
      nextTimes: [], // 后续执行时间
      // 枚举
      InfJobStatusEnum: InfraJobStatusEnum
    };
  },
  created() {
    this.initDict()
  },
  methods: {
    getDictDatas,
    initDict(){
      listHandlerJob({
        start: 0,length: 99
      }).then(res=>{
        this.dictHandler = res.data;
        this.getList();
      })
    },
    /** 查询定时任务列表 */
    getList() {
      this.loading = true;
      // 后端接口是使用 limit start,length的sql语法进行分页的，此处需要转换
      this.queryParams.start = (this.page.currentPage - 1) * this.page.pageSize;
      this.queryParams.length = this.page.pageSize
      listJob(this.queryParams).then(response => {
        this.jobList = response.data;
        this.page.total = response.recordsTotal;
        this.loading = false;
      });
    },
    handleSizeChange(item) {
      this.page.pageSize = item
      this.getList()
    },
    handlePageChange(item) {
      this.page.currentPage = item
      this.getList()
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        jobDesc: undefined,
        jobGroup: undefined,
        executorHandler: undefined,
        executorParam: undefined,
        scheduleConf: undefined,
        executorFailRetryCount: undefined,
      };
      this.nextTimes = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.page.currentPage = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 立即执行一次 **/
    handleRun(row) {
      this.$modal.confirm('确认要立即执行一次"' + row.jobDesc + '"任务吗?').then(function() {
          return runJob(row.id,row.executorParam);
        }).then(() => {
          this.$modal.msgSuccess("执行成功");
      }).catch(() => {});
    },
    /** 立即启动 **/
    handleStart(row) {
      this.$modal.confirm('确认要立即启动"' + row.jobDesc + '"任务吗?').then(function() {
          return handlerStart(row.id);
        }).then(() => {
          this.$modal.msgSuccess("启动成功");
        this.getList();
      }).catch(() => {});
    },

    /** 立即停止 **/
    handleStop(row) {
      this.$modal.confirm('确认要立即停止"' + row.jobDesc + '"任务吗?').then(function() {
        return handlerStop(row.id);
      }).then(() => {
        this.$modal.msgSuccess("停止成功");
        this.getList();
      }).catch(() => {});
    },
    /** 任务详细信息 */
    handleView(row) {
      this.form = row;
      this.openView = true;
      // 获取下一次执行时间
      let param = {
        scheduleConf:row.scheduleConf,
        scheduleType:"CRON"
      }
      getJobNextTimes(param).then(response => {
        this.nextTimes = response.content;
      });
    },
    /** cron表达式按钮操作 */
    handleShowCron() {
      this.scheduleConf = this.form.scheduleConf;
      this.openCron = true;
    },
    /** 确定后回传值 */
    crontabFill(value) {
      this.form.scheduleConf = value;
    },
    /** 任务日志列表查询 */
    handleJobLog(row) {
      if (row.id) {
        this.$router.push({
          path:"/job/log",
          query:{
            jobId: row.id
          }
        });
      } else {
        this.$router.push("/job/log");
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加任务";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.form = row;
      this.title = "修改任务";
      this.open = true;
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.author='admin'
          this.form.scheduleType='CRON'
          this.form.glueType='BEAN'
          this.form.executorRouteStrategy='FIRST'
          this.form.misfireStrategy='DO_NOTHING'
          this.form.executorBlockStrategy='SERIAL_EXECUTION'
          this.form.executorTimeout=0
          if (this.form.id !== undefined) {
            updateJob(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addJob(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id;
      this.$modal.confirm('是否确认删除定时任务编号为"' + ids + '"的数据项?').then(function() {
          return delJob(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 更新状态操作 */
    handleChangeStatus(row, open) {
      const id = row.id;
      let status = open ? InfraJobStatusEnum.NORMAL : InfraJobStatusEnum.STOP;
      let statusStr = open ? '开启' : '关闭';
      this.$modal.confirm('是否确认' + statusStr + '定时任务编号为"' + id + '"的数据项?').then(function() {
        return updateJobStatus(id, status);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(statusStr + "成功");
      }).catch(() => {});
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleRun":
          this.handleRun(row);
          break;
        case "handleStart":
          this.handleStart(row);
          break;
        case "handleStop":
          this.handleStop(row);
          break;
        case "handleView":
          this.handleView(row);
          break;
        case "handleJobLog":
          this.handleJobLog(row);
          break;
        default:
          break;
      }
    },
  }
};
</script>
<style scoped>
.custom-form {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
}

.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
}

.export-button {
  margin-left: auto;
  margin-right: 5px;
}

</style>
