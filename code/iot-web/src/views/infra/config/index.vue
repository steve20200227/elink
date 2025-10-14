<template>
  <div class="app-container">
    <doc-alert title="配置中心" url="https://doc.iocoder.cn/config-center/" />
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" v-show="showSearch" label-width="68px" class="custom-form" style="margin-bottom: 20px">
      <el-form-item prop="name" style="margin-top: -1px">
        <el-input v-model="queryParams.name" placeholder="请输入参数名称" clearable style="width: 240px"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="key" style="margin-top: -1px">
        <el-input v-model="queryParams.key" placeholder="请输入参数键名" clearable style="width: 240px"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="type" style="margin-top: -1px">
        <el-select v-model="queryParams.type" placeholder="系统内置" clearable>
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.INFRA_CONFIG_TYPE)" :key="parseInt(dict.value)"
                     :label="dict.label" :value="parseInt(dict.value)"/>
        </el-select>
      </el-form-item>
      <el-form-item prop="createTime" style="margin-top: -1px">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="创建开始日期" end-placeholder="创建结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item class="top-button" style="margin-top: -1px">
        <el-button size="mini" @click="handleQuery" icon="el-icon-search"></el-button>
        <el-button size="mini" @click="resetQuery" icon="el-icon-refresh-right"></el-button>
      </el-form-item>
      <el-form-item class="add-button" style="margin-top: -4px">
        <el-button size="mini" type="primary" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="configList" style="height: calc(100vh - 240px);overflow-y: auto;">
      <el-table-column label="参数主键" align="center" prop="id" />
      <el-table-column label="参数分类" align="center" prop="category" />
      <el-table-column label="参数名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="参数键名" align="center" prop="key" :show-overflow-tooltip="true" />
      <el-table-column label="参数键值" align="center" prop="value" />
      <el-table-column label="系统内置" align="center" prop="type">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.INFRA_CONFIG_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="是否可见" align="center" prop="visible">
        <template v-slot="scope">
          <span>{{ scope.row.visible ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-setting" @click="handleUpdate(scope.row)"
                     v-hasPermi="['infra:config:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['infra:config:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="参数分类" prop="category">
          <el-input v-model="form.category" placeholder="请输入参数分类" />
        </el-form-item>
        <el-form-item label="参数名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="参数键名" prop="key">
          <el-input v-model="form.key" placeholder="请输入参数键名" />
        </el-form-item>
        <el-form-item label="参数键值" prop="value">
          <el-input v-model="form.value" placeholder="请输入参数键值" />
        </el-form-item>
        <el-form-item label="是否可见" prop="type">
          <el-radio-group v-model="form.visible">
            <el-radio :key="true" :label="true">是</el-radio>
            <el-radio :key="false" :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listConfig, getConfig, delConfig, addConfig, updateConfig, exportConfig } from "@/api/infra/config";

export default {
  name: "InfraConfig",
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
      // 参数表格数据
      configList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 类型数据字典
      typeOptions: [],
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        name: undefined,
        key: undefined,
        type: undefined,
        createTime: []
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        category: [
          { required: true, message: "参数分类不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "参数名称不能为空", trigger: "blur" }
        ],
        key: [
          { required: true, message: "参数键名不能为空", trigger: "blur" }
        ],
        value: [
          { required: true, message: "参数键值不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询参数列表 */
    getList() {
      this.loading = true;
      listConfig(this.queryParams).then(response => {
          this.configList = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        key: undefined,
        value: undefined,
        remark: undefined
      };
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加参数";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改参数";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addConfig(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除参数编号为"' + ids + '"的数据项?').then(function() {
          return delConfig(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
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

.add-button {
  margin-left: auto;
  margin-right: 5px;
}

</style>
