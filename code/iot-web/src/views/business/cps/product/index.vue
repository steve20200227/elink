<template>
  <el-container>
    <CommonTree
      :treeData="treeData"
      :defaultProps="defaultProps"
      :searchTitle="searchTitle"
      :isShowdig="true"
      :titleShow="true"
      :showCheckbox="false"
      :treeTitle="'产品分类'"
      @getNodeClick="getNodeClick"
      @getTreeAdd="getTreeAdd"
      @getTreeDelete="treeDelete"
      @getTreeEdit="getTreeEdit"
    />
    <div style="margin: 20px;width: 100%;">

      <el-row>
        <el-col>
          <div style="display: flex; justify-content: space-between; font-size: 16px; padding-bottom: 5px">
            <div>
              <el-form ref="gridHeadLayout" :inline="true" :model="formInline" class="demo-form-inline">
                <el-form-item>
                  <el-input
                    v-model="formInline.productName"
                    size="mini"
                    placeholder="请输入产品名称">
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
            <div class="top-button" style="margin-top: 4px">
              <el-button size="mini" type="primary" @click="headAdd">新增产品</el-button>
              <!--        <el-button size="mini" @click="headRemove">删除</el-button>-->
            </div>
          </div>
        </el-col>
      </el-row>


      <div>
        <div style="max-height: calc(100vh - 285px);height: calc(100vh - 285px); overflow: auto;" v-if="data.length!=0">
          <el-row>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="6" v-for="(item, index) in data" :key="index">
              <el-card shadow="always" :class="{'record-success': item.isEnable == 1, 'record-danger': item.isEnable == 0}"
                       style="margin: 5px; padding-bottom: 10px;">
                <div style="position: relative; border-bottom: 1px solid #ccc; padding-bottom: 25px;height: 100px;">
                <span style="position:absolute; top: 10px">
                  <!-- 根据 appType 的值显示不同的图片 -->
                  <img style="height: 70px; width: 70px" v-if="item.image != null && item.image != ''"
                       :src="item.image"/>
                  <img style="height: 70px; width: 70px" v-else src="../../../../assets/productDefaultImg.png"/>
                  <!-- 添加更多条件根据需要显示不同的图片 -->
                </span>
                  <span style="position: absolute; top: 0; left: 80px; margin-left: 10px">
                  <strong style="font-size: 18px">{{
                      item.productName.length > 10 ? item.productName.substring(0, 10) + '...' : item.productName
                    }}</strong>
                  <span
                    :style="{
                         display: 'inline-block',
                         height: '8px',
                         width: '8px',
                         // background: item.isEnable === 1 ? 'rgb(10, 191, 91)' : 'rgb(229, 69, 69)',
                         borderRadius: '7px',
                         verticalAlign: 'middle',
                         marginLeft: '10px',
                         marginBottom: '5px'}">
                  </span>
                </span>
                  <el-tooltip class="item" effect="dark" content="编辑" placement="top" v-if="item.isEnable == 0">
                    <a style="font-size: 14px; position: absolute; right: 0" @click="rowEdit(item)">
                      <span class="el-icon-setting" style="font-size: 18px;color: #1677FF"></span>
                    </a>
                  </el-tooltip>
                  <span style="position: absolute; top: 31px; left: 80px; margin-left: 10px;font-size: 14px">
                  <span style="color: #646a73">产品编码：</span>
                  <span style="color: #1677ff">{{ item.productCode }}</span>
                </span>
                  <span style="position: absolute; top: 54px; left: 80px; margin-left: 10px;font-size: 14px">
                  <span style="color: #646a73">关联设备：</span>
                  <span style="color: #1677ff">{{ item.equipmentCount }}</span>
                </span>
                  <span style="position: absolute; top: 77px; left: 80px; margin-left: 10px;font-size: 14px">
                        <span style="color: #646a73">设备类型：</span>
                        <span style="color: #1677ff">{{ item.equipmentType }}</span>
                      </span>
                </div>
                <div style="margin-top: 10px;">
                  <div style="float: left">
                    <el-button size="mini" type="primary" v-if="item.isEnable == 0" style="margin: 5px"
                               @click="rowEnable(item)">启用
                    </el-button>
                    <el-button size="mini" type="primary" v-if="item.isEnable == 1" style="margin: 5px"
                               @click="rowDisEnable(item)">禁用
                    </el-button>
                    <el-button size="mini" v-if="item.isEnable == 0" style="margin: 5px"
                               @click="rowRemove(item)">删除
                    </el-button>
                  </div>
                  <div style="float: right">
                    <el-button type="text" @click="rowDetail(item)" size="small">更多信息</el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
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
          style="float: right; margin: 20px 0;">
        </el-pagination>
        <el-empty
          v-if="data.length==0"
          style="width: 100%; margin-top: 50px"
          description="暂无数据"
          :image-size="120"
        ></el-empty>
        <el-dialog
          v-dialog-drag
          :title="'产品分类'"
          :modal-append-to-body="false"
          :close-on-click-modal="false"
          :visible.sync="sortDialog"
          v-if="sortDialog"
          width="60%"
          @close="sortDialog = false"
          class="custom-dialog"
        >
          <sortDialog ref="productSortDialog" :rowData="rowData"
                      @sortDialogClose="sortDialogClose"></sortDialog>
        </el-dialog>
      </div>
    </div>
  </el-container>

</template>

<script>
import {page as getPage, remove, submit} from '@/api/cps/product-api'
import {mapState} from 'vuex';
import CommonTree from "@/views/components/ComTree";
import sortDialog from "@/views/business/cps/product/components/SortDialog.vue";
import {getTree, remove as treeRemove} from '@/api/cps/product-sort.js';

export default {
  components: {
    sortDialog,
    CommonTree
  },
  name: "product",
  computed: {
    ...mapState('SET_DICT_DATAS'),
    ids() {
      let ids = [];
      this.selectionList.forEach(ele => {
        ids.push(ele.id);
      });
      return ids.join(",");
    },
  },
  data() {
    return {
      rowData: {},
      sortDialog: false,
      treeData: [],
      searchTitle: "sortName",
      defaultProps: {
        label: "sortName",
        value: "id"
      },
      tableLoading: false,
      options: [],
      data: [],
      selectionList: [],
      formInline: {},
      page: {
        pageSize: 12,
        currentPage: 1,
        total: 0
      },
    }
  },
  mounted() {
    this.initTree();
    this.onLoad(this.page, {});
    this.options = this.$store.state.dict.dictDatas.is_enable
  },
  methods: {
    initTree() {
      getTree().then((res) => {
        this.treeData = res.data;
      })
    },
    sortDialogClose() {
      this.initTree();
      this.sortDialog = false;
      this.page.currentPage = 1;
      this.onLoad(this.page, {parentId: this.treeDeptId});
    },
    getTreeAdd(node) {
      this.sortDialog = true;
      this.$nextTick(() => {
        this.$refs.productSortDialog.init(this.treeDeptId, 'add');
      })
    },
    treeDelete() {
      treeRemove(this.treeDeptId).then((res) => {
        if (res.data) {
          this.$message.success("删除成功！")
          this.initTree();
          this.onLoad(this.page, {parentId: this.treeDeptId});
        } else {
          this.$message.warning("删除失败！当前节点下还有分类子集或分类数据")
        }
      })
    },
    getTreeEdit() {
      this.sortDialog = true;
      this.$nextTick(() => {
        this.$refs.productSortDialog.init(this.treeDeptId, 'edit');
      })
    },
    getNodeClick(data) {
      this.treeDeptId = data.id;
      this.page.currentPage = 1;
      this.onLoad(this.page, {parentId: this.treeDeptId, productSortId: this.treeDeptId});
    },
    changeCurrent(val) {
      this.page.currentPage = val
      this.onLoad(this.page, {})
    },
    changeSize(val) {
      this.page.pageSize = val
      this.onLoad(this.page, {})
    },
    rowDisEnable(row) {
      console.log()
      row.isEnable = 0;
      submit(row).then(res => {
        const {code, msg} = res.data;
        if (res.code === 0) {
          this.$message.success("操作成功")
        } else {
          this.$message.success(msg);
        }
      })
    },
    rowEnable(row) {
      row.isEnable = 1;
      submit(row).then(res => {
        const {code, msg} = res.data;
        if (res.code === 0) {
          this.$message.success("操作成功")
        } else {
          this.$message.success(msg);
        }
      })
    },
    rowRemove(row) {
      this.$confirm("确定将选择数据删除", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
          remove(row.id).then(res => {
            if (res.data) {
              this.onLoad(this.page);
              this.$message.success("操作成功");
            } else {
              this.$message.warning("产品下存在关联设备，无法删除");
            }
          })
        })
    },
    rowEdit(row) {
      this.$router.push({
        path: `/business/cps/product/Add`,
        query: {
          id: row.id,
          type: 'edit',
          productSortId: this.treeDeptId,
        }
      })
    },
    rowDetail(row) {
      this.$router.push({
        path: `/business/cps/product/Add`,
        query: {
          id: row.id,
          type: 'view',
          productSortId: this.treeDeptId,
        }
      })
    },
    handleSearch() {
      this.page.currentPage = 1
      this.onLoad(this.page, this.formInline)
    },
    handleEmpty() {
      this.formInline = {}
      this.onLoad(this.page)
    },
    headRemove() {
      if (this.selectionList.length === 0) {
        this.$message.warning("请选择数据");
        return;
      }
      this.$confirm("确定将选择数据删除", {
        confirmButtonText: "确定",
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
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功",
          });
        });
    },
    handleSelectionChange(selection) {
      this.selectionList = selection
    },
    indexMethod(index) {
      return index + 1
    },
    headAdd() {
      if (this.treeDeptId) {
        this.$router.push({
          path: `/business/cps/product/Add`,
          query: {
            type: 'add',
            productSortId: this.treeDeptId,
          }
        })
      } else {
        this.$message.warning("请先选择左侧分类节点！");
      }
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
        this.data = data.records;
        this.tableLoading = false;
      });
    },
  }
};
</script>
<style scoped>
.product-card ::v-deep .el-card__body {
  padding: 0 !important;
}

.product-card ::v-deep .el-divider--horizontal {
  margin: 0;
}

.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
}

.record-danger {
  background: linear-gradient(40deg, #fff, #f4f4f5, #e9e9ed);
}

.record-success {
  background: linear-gradient(40deg, #fff, #eef6ff, #b3d9ff);;
}
.record-info {
  background: linear-gradient(40deg, #fff, #f4f4f5, #e9e9ed);
}
</style>
