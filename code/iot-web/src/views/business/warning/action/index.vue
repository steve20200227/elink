<template>
  <div style="margin: 20px">
    <div style="margin-top: 5px">
      <el-form ref="gridHeadLayout" :inline="true" size="mini" :model="formInline" class="custom-form">
        <el-form-item style="margin-top: -1px">
          <el-input
            v-model="formInline.actionName"
            placeholder="请输入配置名称">
          </el-input>
        </el-form-item>
        <el-form-item class="top-button" style="margin-top: -1px">
          <el-button size="mini" @click="handleSearch" icon="el-icon-search"></el-button>
          <el-button size="mini" @click="handleEmpty" icon="el-icon-refresh-right"></el-button>
        </el-form-item>
        <el-button size="mini" type="primary" @click="handleAdd" class="add-button">新增</el-button>
      </el-form>
    </div>

    <div style="max-height: calc(100vh - 245px);height: calc(100vh - 245px); overflow: auto;" v-if="data.length!=0">
      <div style="max-height: calc(100vh - 285px);height: calc(100vh - 285px); overflow: auto;">
        <el-row style="margin-top: 20px">
          <div class="warning-card">
            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item, index) in data" :key="index">
              <el-card shadow="always" style="font-size: 14px; padding-bottom: 10px; margin-right: 10px;margin-bottom: 10px">
                <div style="margin-bottom: 10px">
                  <div style="padding: 20px 20px 5px 20px">
                    <div class="point-head" style="position: relative;font-size: 18px; font-weight: bold; margin-bottom: 10px;">
                      {{ item.actionName }}
                      <el-tooltip class="item" effect="dark" content="编辑" placement="top">
                        <a style="font-size: 14px; position: absolute; right: 0" @click="rowEdit(item)">
                          <span class="el-icon-setting" style="font-size: 18px;color: #1677FF"></span>
                        </a>
                      </el-tooltip>
                    </div>
                    <div class="point-content" style="margin-bottom: 10px">
                      <div style="margin: 16px 6px 0 0px">
                        <span style="color: #646a73">动作类型：</span>
                        <span style="color: #1677ff;">
                        <dict-tag :type="DICT_TYPE.ACTION_TYPE" :value="item.actionType" />
                      </span>
                      </div>
                      <div style="margin: 16px 6px 0 0px">
                        <span style="color: #646a73">创建时间：</span>
                        <span style="color: #1677ff;">{{ parseTime(item.createTime) }}</span>
                      </div>
                    </div>
                    <el-divider></el-divider>
                  </div>

                  <div style="padding: 0 20px 0 20px;">
                    <div style="float: left">
                      <el-button size="mini" type="primary" style="margin: 5px"
                                 @click="viewRecord(item)">输出记录
                      </el-button>
                      <el-button size="mini"   style="margin: 5px"
                                 @click="rowRemove(item)">删除
                      </el-button>
                    </div>
                    <div style="float: right">
                      <el-button type="text" @click="handleView(item)" size="small">更多信息</el-button>
                    </div>
                  </div>
                </div>

              </el-card>
            </el-col>
          </div>
        </el-row>
      </div>


    </div>
    <el-pagination
      v-if="data.length!=0"
      background
      :current-page="page.currentPage"
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
      v-if="data.length==0"
      style="width: 100%; margin-top: 50px"
      description="暂无数据"
      :image-size="120"
    ></el-empty>
    <addDialog
      ref="addDialog"
      @restOnLoad="restOnLoad">
    </addDialog>
  </div>

</template>

<script>
import addDialog from "@/views/business/warning/action/components/addDialog.vue";
import {page as getPage, remove} from "@/api/warning/warning-north-api";

export default {
  name: "action",
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
    addDialog
  },
  data() {
    return {
      tableLoading: false,
      data: [],
      selectionList: [], // 表格多选框
      formInline: {},
      actionDict: [], // 动作类型字典
      page: {
        pageSize: 12,
        currentPage: 1,
        total: 0
      },
    }
  },
  mounted() {
    this.onLoad(this.page, {});
    this.actionDict = this.$store.state.dict.dictDatas.action_type
  },
  methods: {
    viewRecord(data){
      this.$router.push({
        path: `/warning/action/actionRecord/index`,
        query:{
          actionId: data.id
        }
      });
    },
    handleSearch () {
      this.page.currentPage = 1
      this.onLoad(this.page, this.formInline)
    },
    handleEmpty () {
      this.formInline = {}
      this.onLoad(this.page)
    },
    // 批量删除
    handleRomove() {
      if (this.selectionList.length === 0) {
        this.$message.warning("请选择数据");
        return;
      }
      this.$confirm("确定将选择数据删除", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        remove(this.ids).then(res => {
          if (res.code === 0) {
            // 刷新表格数据并重载
            this.data = [];
            // 表格数据重载
            this.onLoad(this.page);
            this.$message.success("操作成功");
          }
        })
      })
    },
    // 行删除
    rowRemove(row) {
      this.$confirm("确定将选择数据删除", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        remove(row.id).then(res => {
          if (res.code === 0) {
            // 刷新表格数据并重载
            this.data = [];
            // 表格数据重载
            this.onLoad(this.page);
            this.$message.success("操作成功");
          }
        })
      })
    },
    // 行编辑
    rowEdit(row) {
      this.$refs.addDialog.editPoint(row.id)
    },
    handleView(row) {
      this.$refs.addDialog.viewPoint(row.id)
    },
    handleAdd() {
      this.$refs.addDialog.addPoint()
    },
    // 表单返回
    restOnLoad() {
      // 重新刷新页面
      this.onLoad(this.page, {})
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
        Object.assign(params, this.formInline)
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
.warning-card ::v-deep .el-card__body {
  padding: 0 !important;
}

.warning-card ::v-deep .el-divider--horizontal {
  margin: 0;
}

.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
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
