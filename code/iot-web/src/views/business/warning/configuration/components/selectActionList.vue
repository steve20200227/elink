<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="50%"
      @close="cancel"
    >
      <div style="margin: 10px 0;height: calc(100vh - 245px)">
        <el-table
          ref="actionTable"
          border
          height="calc(100vh - 310px)"
          :data="actionList"
          v-loading="tableLoading"
          @selection-change="handleSelectionChange"
          tooltip-effect="dark"
          style="width: 100%;">
          <el-table-column
            type="selection"
            align="center"
            :selectable="selectable"
            width="40">
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            align="center"
            :index="indexMethod">
          </el-table-column>
          <el-table-column
            prop="actionName"
            label="动作名称"
            align="center"
            show-overflow-tooltip>
          </el-table-column>
          <el-table-column
            prop="outputWay"
            label="动作类型"
            align="center"
            show-overflow-tooltip>
            <template v-slot="scope">
              <dict-tag :type="DICT_TYPE.ACTION_OUTPUT_WAY" :value="scope.row.outputWay"></dict-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="创建时间"
            align="center"
            width="250"
            show-overflow-tooltip>
            <template v-slot="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          background
          :current-page="page.currentPage"
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total"
          :page-size="page.pageSize"
          :pager-count="5"
          @size-change="changeSize"
          @current-change="changeCurrent"
          style="float: right; margin: 20px 0;">
        </el-pagination>
      </div>

      <div style="float: right">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" type="primary" @click="determine">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { page as getPage } from "@/api/warning/warning-north-api";

export default {
  name: "actionSelect",
  data() {
    return {
      tableLoading: false,
      title: '动作列表',
      dialogFlag: false,
      actionList: [],
      selectionList: [],
      actionData: [],
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
    }
  },
  methods: {
    selectable(row) {
      return !this.actionData.some(item => item.actionId === row.id)
    },
    init(list) {
      this.actionData = list
      this.dialogFlag = true
      this.onLoad(this.page, {})
    },
    changeCurrent (val) {
      this.page.currentPage = val
      this.onLoad(this.page, {})
    },
    changeSize (val) {
      this.page.pageSize = val
      this.onLoad(this.page, {})
    },
    handleSelectionChange (selection) {
      this.selectionList = selection
    },
    indexMethod (index) {
      return index + 1
    },
    // 清空选项框
    toggleSelection() {
      this.$refs.actionTable.clearSelection()
    },
    // 确认按钮
    determine() {
      this.dialogFlag = false
      this.$emit("saveAction", this.selectionList.concat(this.actionData))
    },
    cancel() {
      this.dialogFlag = false;
      this.toggleSelection()
    },
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
        this.actionList = data.records;
        this.tableLoading = false;
        this.toggleSelection();
      });
    },
  }
}
</script>

<style scoped>
.product-form ::v-deep .el-form-item__label {
  color: #3f4448;
  font-weight: 400;
}
</style>
