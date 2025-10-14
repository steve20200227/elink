<template>
  <div>
    <el-container>
      <el-container>
        <el-main>
          <div style="margin-top: -20px">
            <el-form ref="gridHeadLayout" :inline="true" :model="formInline" class="demo-form-inline">
              <el-form-item>
                <el-input
                  v-model="formInline.equipmentName"
                  size="mini"
                  placeholder="请输入设备名称">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-select v-model="formInline.isEnable" size="mini" placeholder="请选择是否启用">
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
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
              height="calc(100vh - 285px)"
              :data="tableData"
              v-loading="tableLoading"
              tooltip-effect="dark"
              @selection-change="handleSelectionChange"
              style="width: 100%;">
              <el-table-column
                fixed
                type="index"
                label="序号"
                align="center"
                :index="indexMethod">
              </el-table-column>
              <el-table-column
                label="设备编码"
                align="center"
                prop="equipmentCode">
                <template slot-scope="scope">
                  <el-tag type="info">
                    <el-button
                      style="margin:0 3px"
                      size="small"
                      type="text"
                      @click="rowDetail(scope.row)">
                      {{ scope.row.equipmentCode }}
                    </el-button>
                  </el-tag>

                </template>
              </el-table-column>
              <el-table-column
                prop="equipmentName"
                label="设备名称"
                align="center">
                <template slot-scope="scope">
                  <el-tag>
                    {{ scope.row.equipmentName }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column
                prop="warningCount"
                label="关联告警"
                align="center"
                width="120">
                <template v-slot="scope">
                  <el-tag type="success">
                    {{ scope.row.warningCount }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column
                prop="createTime"
                label="创建时间"
                align="center"
                show-overflow-tooltip>
                <template v-slot="scope">
                  <el-tag>
                    <span>{{ parseTime(scope.row.createTime) }}</span>
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column
                prop="isEnable"
                label="状态"
                align="center"
                show-overflow-tooltip>
                <template slot-scope="scope">
                  <el-tag type="success" v-if="scope.row.isEnable == 1">
                    <dict-tag :type="DICT_TYPE.IS_ENABLE" :value="scope.row.isEnable"/>
                  </el-tag>
                  <el-tag type="danger" v-if="scope.row.isEnable == 0">
                    <dict-tag :type="DICT_TYPE.IS_ENABLE" :value="scope.row.isEnable"/>
                  </el-tag>
                </template>
              </el-table-column>
              <!--<el-table-column
                fixed="right"
                prop="address"
                label="操作"
                align="center"
                width="150"
                show-overflow-tooltip>
                <template slot-scope="scope">
                  <el-button
                    style="margin:0 3px"
                    size="small"
                    type="text"
                    @click="rowEnable(scope.row)"
                    v-if="scope.row.isEnable === 0"
                  >启用
                  </el-button>
                  <el-button
                    style="margin:0 3px"
                    size="small"
                    type="text"
                    @click="rowDisEnable(scope.row)"
                    v-if="scope.row.isEnable === 1"
                  >失效
                  </el-button>
                  <el-button
                    style="margin:0 3px"
                    size="small"
                    type="text"
                    @click="rowEdit(scope.row)"
                    v-if="scope.row.isEnable === 0"
                  >编辑
                  </el-button>
                  <el-button
                    style="margin:0 3px"
                    size="small"
                    type="text"
                    @click="rowRemove(scope.row)"
                    v-if="scope.row.isEnable === 0"
                  >删除
                  </el-button>
                </template>
              </el-table-column>-->
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
    </el-container>
  </div>
</template>

<script>
import CommonTree from "@/views/components/ComTree/index.vue";
import {pageByProductCode as getPage, enableEquipment, disenableEquipment, remove, reStart, submit} from '@/api/cps/equipment-api'
import {formatDate} from "@/utils/dateUtils";

export default {
  name: 'equipment',
  components: {
    CommonTree,
  },
  data() {
    return {
      productCode: '',
      options: [],
      tableLoading: false,
      tableData: [],
      formInline: {},
      selectionList: [],
      searchTitle: 'cabinName',
      defaultProps: {
        label: "cabinName",
        value: "id"
      },
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
    }
  },
  computed: {
    ids() {
      let ids = [];
      this.selectionList.forEach(ele => {
        ids.push(ele.id);
      });
      return ids.join(",");
    },
  },
  mounted() {
    // this.initTree();
    this.onLoad(this.page, {});
    this.options = this.$store.state.dict.dictDatas.is_enable
  },
  methods: {
    getEquipment(productCode) {
      this.productCode = productCode
      this.onLoad(this.page, {})
      this.options = this.$store.state.dict.dictDatas.is_enable
    },
    formatter(row, column, cellValue, index) {
      return formatDate(new Date(cellValue), "yyyy-MM-dd HH:mm:ss")
    },
    headRemove() {
      if (this.selectionList.length === 0) {
        this.$message.warning("请选择数据");
        return;
      }
      if (this.selectionList.find(item => item.isEnable === 1)) {
        this.$message.warning("已启用的设备不可删除");
        return;
      }
      this.$confirm("确定将选择数据删除", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let ids = this.selectionList.map(item => {
            return item.id
          }).join(',')
          return remove(ids);
        })
        .then(() => {
          // 刷新表格数据并重载
          this.data = [];
          // 表格数据重载
          this.onLoad(this.page, {});
          this.$message({
            type: "success",
            message: "操作成功",
          });
        });
    },
    headDisenable() {
      if (this.selectionList.length === 0) {
        this.$message.warning("请选择数据");
        return;
      }
      this.$confirm("确定将选择数据失效", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          return disenableEquipment(this.ids);
        })
        .then(() => {
          // 刷新表格数据并重载
          this.data = [];
          // 表格数据重载
          this.onLoad(this.page, {});
          this.$message({
            type: "success",
            message: "操作成功",
          });
        });
    },
    rowDetail(row) {
      this.$router.push({
        path: `/business/cps/equipment/Add`,
        query: {
          id: row.id,
          type: 'view',
        }
      })
    },
    rowEdit(row) {
      this.$router.push({
        path: `/business/cps/equipment/Add`,
        query: {
          id: row.id,
          type: 'edit',
        }
      })
    },
    headAdd() {
      this.$router.push({
        path: `/business/cps/equipment/Add`,
        query: {
          type: 'add',
        }
      })
    },
    rowRemove(row) {
      this.$confirm("确定删除数据", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          return remove(row.id);
        })
        .then(() => {
          this.onLoad(this.page, {});
          this.$message({
            type: "success",
            message: "操作成功"
          });
        });
    },
    rowEnable(row) {
      // 在方法里特殊处理进行启用
      enableEquipment(row.id).then(res => {
        const {code, msg} = res;
        if (code === 0) {
          this.$message.success("操作成功")
          this.onLoad(this.page, {})
        } else {
          this.$message.success(msg);
        }
      })
    },
    rowDisEnable(row) {
      row.isEnable = 0;
      submit(row).then(res => {
        const {code, msg} = res;
        if (code === 0) {
          this.$message.success("操作成功")
        } else {
          this.$message.success(msg);
        }
      })
    },
    indexMethod(index) {
      return index + 1
    },
    initTree() {
      getList().then(res => {
        this.treeData = res.data.data;
      });
    },
    treeNodeClick(node) {
      this.node = node;
      const {cabinCode} = node;
      Object.assign(this.$refs.gridHeadLayout.searchForm, {cabinCode: cabinCode})
      this.onLoad(this.page, this.$refs.gridHeadLayout.searchForm);
    },
    // 批量新增按钮
    // headBatchAdd() {
    //   this.$refs.batchAddDialog.openDialog();
    // },
    // 重启按钮
    headeRestart() {
      this.$confirm('确认重启？', {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        reStart().then(res => {
          const {code, msg} = res;
          if (code === 0) {
            this.$message.success("操作成功");
          } else {
            this.$message.error(msg || '未知错误');
          }
        }).catch(error => {
          this.$message.error('没有设备需要重启');
        });
      }).catch(() => {
        this.$message.info('已取消');
      });
    },
    // 启用按钮
    headEnable() {
      if (this.selectionList.length === 0) {
        this.$message.warning("请选择数据");
        return;
      }
      if (this.selectionList.find(item => item.isEnable === 1)) {
        this.$message.warning("已启用的设备不可再次启用");
        return;
      }
      this.$confirm('确定将选择数据启用', {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          console.log('-----------------')
          return enableEquipment(this.ids);
        })
        .then(() => {
          // 刷新表格数据并重载
          this.data = [];
          // 表格数据重载
          this.onLoad(this.page, {});
          this.$message({
            type: "success",
            message: "操作成功",
          });
        });
    },
    // 清空选项框
    toggleSelection() {
      this.$refs.equipmentTable.clearSelection();
    },
    handleSearch() {
      this.page.currentPage = 1
      this.onLoad(this.page, this.formInline)
    },
    handleEmpty() {
      this.formInline = {}
      this.onLoad(this.page, {})
    },
    // 选项框
    handleSelectionChange(selection) {
      this.selectionList = selection
    },
    changeSize(val) {
      this.page.pageSize = val
      this.onLoad(this.page, {})
    },
    changeCurrent(val) {
      this.page.currentPage = val
      this.onLoad(this.page, {})
    },
    onLoad(page, params) {
      if (this.productCode) {
        this.formInline.productCode = this.productCode
        this.page = page;
        getPage(
          page.currentPage,
          page.pageSize,
          Object.assign(params, this.formInline)
        ).then(res => {
          const data = res.data;
          this.page.total = data.total;
          this.tableData = data.records;
          this.toggleSelection();
        });
      }
    },
  }
}
</script>

<style scoped>
.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
}
</style>
