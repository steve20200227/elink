<template>
  <div class="product-dialog">
    <el-dialog
      title="选择产品"
      :visible.sync="selectProductShow"
      @close="close"
      class="avue-dialog avue-dialog--top"
      width="60%"
    >
      <el-container>
        <el-main>
          <div class="top-button">
            <el-form ref="gridHeadLayout" :inline="true" :model="formInline" class="demo-form-inline">
              <el-form-item>
                <el-input
                  v-model="formInline.productCode"
                  size="mini"
                  placeholder="请输入产品编码">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="formInline.productName"
                  size="mini"
                  placeholder="请输入产品名称">
                </el-input>
              </el-form-item>
              <el-form-item class="top-button">
                <el-button size="mini" icon="el-icon-search" @click="handleSearch"></el-button>
                <el-button size="mini" icon="el-icon-refresh-right" @click="handleEmpty"></el-button>
              </el-form-item>
            </el-form>
          </div>

          <div>
            <el-table
              ref="equipmentTable"
              border
              height="48vh"
              :data="tableData"
              v-loading="tableLoading"
              tooltip-effect="dark"
              style="width: 100%;">
              <el-table-column
                prop="productCode"
                label="产品编码"
                align="center">
              </el-table-column>
              <el-table-column
                prop="productName"
                label="产品名称"
                align="center"
                show-overflow-tooltip>
              </el-table-column>
              <el-table-column
                fixed="right"
                prop="address"
                label="操作"
                align="center"
                width="200"
                show-overflow-tooltip>
                <template slot-scope="scope">
                  <el-button
                    style="margin:0 3px"
                    size="small"
                    type="text"
                    @click="handleSelect(scope.row)">
                  确认
                  </el-button>

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
        </el-main>
      </el-container>
    </el-dialog>
  </div>

</template>

<script>

import {page as getPage} from "@/api/cps/product-api";

export default {
  name: "tableDialog",
  data() {
    return {
      selectProductShow: false,
      formInline: {},
      query: {},
      dataTotal: 0,
      tableLoading: false,
      disabledList:[],
      tableData: [],
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 10
      },
    }
  },
  mounted() {

  },
  methods: {
    changeCurrent (val) {
      this.page.currentPage = val
      this.onLoad(this.page, {})
    },
    changeSize (val) {
      this.page.pageSize = val
      this.onLoad(this.page, {})
    },
    handleSelect (row) {
      this.selectProductShow = false
      this.$emit('select-product', row)
    },
    handleEmpty () {
      this.formInline = {}
      this.query = {}
      this.onLoad(this.page, {})
    },
    handleSearch () {
      this.page.currentPage = 1
      this.onLoad(this.page, this.formInline)
    },
    onLoad(page, params = {}) {
      this.tableLoading = true;
      this.query.isEnable = 1
      getPage(
        page.currentPage,
        page.pageSize,
        Object.assign(this.query, params)
      ).then(res => {
        const data = res.data;
        this.page.total = data.total;
        this.tableData = data.records
        this.tableLoading = false;
      });
    },
    openDialog() {
      this.selectProductShow = true
      this.onLoad(this.page, {})
    },
    close() {
      this.$emit('close-dialog', () => {
        this.selectProductShow = false
      })
    },
  }
}
</script>

<style scoped lang="scss">
.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
}
::v-deep .warehouseTable .avue-crud .el-table {
  height: calc(100vh - 500px) !important;
  max-height: calc(100vh - 500px) !important;
}
.product-dialog ::v-deep .el-dialog__body {
  padding: 0 20px !important;
}
</style>
