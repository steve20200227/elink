<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="60%"
      @close="cancel"
    >
      <div style="margin: 10px 0;">
        <el-table
          ref="actionTable"
          border
          height="calc(100vh - 285px)"
          :data="pointData"
          v-loading="tableLoading"
          tooltip-effect="dark"
          style="width: 100%;">
          <el-table-column
            type="index"
            label="序号"
            align="center"
            :index="indexMethod">
          </el-table-column>
          <el-table-column
            prop="pointName"
            label="点位名称"
            align="center">
          </el-table-column>
          <el-table-column
            prop="dataType"
            label="数据类型"
            align="center"
            show-overflow-tooltip>
            <template slot-scope="scope">
              <dict-tag :type="DICT_TYPE.OPC_POINT_DATA_TYPE" :value="scope.row.dataType" />
            </template>
          </el-table-column>
          <el-table-column
            prop="isEnable"
            label="状态"
            align="center"
            width="250"
            show-overflow-tooltip>
            <template slot-scope="scope">
              <dict-tag :type="DICT_TYPE.IS_ENABLE" :value="scope.row.isEnable" />
            </template>
          </el-table-column>
          <el-table-column
            prop="address"
            label="操作"
            align="center"
            width="250"
            show-overflow-tooltip>
            <template slot-scope="scope">
              <el-button
                style="margin:0 3px"
                size="small"
                type="text"
                @click="selectEquipment(scope.row)"
              >选择
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

    </el-dialog>
  </div>
</template>

<script>
import { page as getPage } from "@/api/cps/equipment-point-api"

export default {
  name: "actionSelect",
  data() {
    return {
      tableLoading: false,
      title: '选择点位',
      pointData: [],
      dialogFlag: false,
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
    }
  },
  mounted() {
    // this.onLoad(this.page, {})
  },
  methods: {
    init(id) {
      this.equipmentId = id
      this.dialogFlag = true
      this.onLoad(this.page, {})
    },
    // 选择点位
    selectEquipment(row) {
      this.dialogFlag = false
      this.$emit('selectPoint', row)
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
    cancel() {
      this.dialogFlag = false;
    },
    onLoad(page, params = {}) {
      this.page = page;
      this.tableLoading = true;
      params.isEnable = 1
      params.equipmentId = this.equipmentId
      getPage(
        page.currentPage,
        page.pageSize,
        Object.assign(params, this.formInline)
      ).then(res => {
        const data = res.data;
        this.page.total = data.total;
        this.pointData = data.records;
        this.tableLoading = false;
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
