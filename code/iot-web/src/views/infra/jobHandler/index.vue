<template>
  <div class="app-container">
    <div style="margin-bottom: 20px">
      <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" v-show="showSearch" label-width="100px" class="custom-form">
        <el-form-item prop="appname" style="margin-top: -1px">
          <el-input v-model="queryParams.appname" placeholder="请输入执行器编码" clearable @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item class="top-button" style="margin-top: -1px">
          <el-button size="mini" @click="handleQuery" icon="el-icon-search"></el-button>
          <el-button size="mini" @click="handleQuery('reset')" icon="el-icon-refresh-right"></el-button>
        </el-form-item>
        <el-button size="mini" type="primary" @click="handleAdd" class="add-button">新增</el-button>
      </el-form>
    </div>

    <el-table v-if="dataList.length!=0" v-loading="loading" :data="dataList" style="height: calc(100vh - 240px);">
      <el-table-column label="编号" align="center" type="index" />
      <el-table-column label="执行器编码" align="center" prop="appname" />
      <el-table-column label="名称" align="center" prop="title" />
      <el-table-column label="注册方式" align="center">
        <template slot-scope="scope">
          <span> {{scope.row.addressType == 0 ?'自动注册':'手动注册'}} </span>
        </template>
      </el-table-column>
      <el-table-column show-overflow-tooltip label="主机列表" align="center" prop="addressList" />
      <el-table-column label="更新时间" align="center" prop="updateTime">
        <template slot-scope="scope">
          {{parseTime(scope.row.updateTime)}}
        </template>
      </el-table-column>
      <el-table-column label="操作"  class-name="small-padding fixed-width" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-setting" @click="handleUpdate(scope.row)"
                     >修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['infra:job:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="float: right;" v-if="dataList.length!=0">
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
      v-if="dataList.length==0"
      style="width: 100%; margin-top: 50px"
      description="暂无数据"
      :image-size="120"
    ></el-empty>
    <el-dialog :visible.sync="show" @close="dataForm = {}" title="添加执行器" width="500px" append-to-body>
      <el-form ref="form" :model="dataForm" label-width="100px" size="mini">
        <el-row>
          <el-col :span="24">
            <el-form-item label="执行器编码：">
              <el-input v-model="dataForm.appname" maxlength="64" show-word-limit placeholder="请输入执行器编码" />
            </el-form-item>
            <el-form-item label="名称：">
              <el-input v-model="dataForm.title" maxlength="12" show-word-limit placeholder="请输入名称" />
            </el-form-item>
            <el-form-item label="注册方式：">
              <el-select @change="addressType" v-model="dataForm.addressType" placeholder="请输入注册方式" >
                <el-option label="自动注册" value="0"></el-option>
                <el-option label="手动注册" value="1"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="主机列表：">
              <el-input type="textarea" :disabled="dataForm.addressType == 0" maxlength="10000" show-word-limit :autosize="{ minRows: 4, maxRows: 8 }" v-model="dataForm.addressList" placeholder="请输入主机列表" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="show = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {addHandlerJob, delHandlerJob, listHandlerJob, updateHandlerJob} from "@/api/infra/job";

export default {
  name: "index",
  data(){
    return {
      show:false,
      dataForm:{},
      total: 0,
      queryParams:{
        start: 0,
        length: 10,
        appname: ''
      },
      showSearch: true,
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      dataList:[],
      loading: false
    }
  },
  created() {
    this.handleQuery();
  },
  methods:{
    addressType(e){
      if (e == 0) this.dataForm.addressList = ''
    },
    submitForm(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (!this.dataForm.id) {
            addHandlerJob(this.dataForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.show = false;
              this.handleQuery();
            });
          } else {
            updateHandlerJob(this.dataForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.show = false;
              this.handleQuery();
            });
          }
        }
      });
    },
    handleSizeChange(item) {
      this.page.pageSize = item
      this.handleQuery()
    },
    handlePageChange(item) {
      this.page.currentPage = item
      this.handleQuery()
    },
    handleAdd(){
      this.show = true
    },
    handleUpdate(row){
      // 浅拷贝，防止修改值直接影响表格数据
      this.dataForm = Object.assign({}, row)
      this.dataForm.addressType = row.addressType+""
      this.show = true
    },
    handleDelete(row){
      this.$modal.confirm('是否确认删除名称为"' + row.appname + '"的数据项?').then(function() {
        return delHandlerJob(row.id);
      }).then(() => {
        this.handleQuery();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleQuery(type){
      if (type == 'reset') {
        this.queryParams.appname = "";
        this.page.currentPage = 1;
      }
      this.loading = true
      // 后端接口是使用 limit start,length的sql语法进行分页的，此处需要转换
      this.queryParams.start = (this.page.currentPage - 1) * this.page.pageSize;
      this.queryParams.length = this.page.pageSize
      listHandlerJob(this.queryParams).then(res => {
        this.dataList = res.data
        this.page.total = res.recordsTotal
        this.loading = false;
      })
    }
  }
}
</script>

<style scoped>
.form {
  padding:20px 40px 20px 0;
  position:absolute;
  top:0px;
  height: 100vh;
  width: 100%;
  background-color:#fff;
  z-index: 1999;
}

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
