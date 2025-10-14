<template>
  <div>
    <el-container>
      <el-container>
        <el-main>
          <el-row>
            <el-col>
              <div style="display: flex; justify-content: space-between; font-size: 16px; padding-bottom: 5px">
                <div>
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
                    <el-form-item>
                      <el-input
                        v-model="formInline.productName"
                        size="mini"
                        placeholder="请输入产品名称">
                      </el-input>
                    </el-form-item>
                    <el-form-item>
                      <el-select v-model="formInline.equipmentType" size="mini" placeholder="请选择设备类型">
                        <el-option
                          v-for="item in equipmentTypeOptions"
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
                <div class="top-button">
                  <el-button size="mini" type="primary" @click="headAdd" style="margin-right: 10px">新增设备</el-button>
                  <el-button size="mini" type="primary" @click="BatchAdd">批量新增</el-button>
                </div>
              </div>
            </el-col>
          </el-row>

          <div>
            <div style="max-height: calc(100vh - 285px);height: calc(100vh - 285px); overflow: auto;" v-if="tableData.length!=0">
              <el-row>
                <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item, index) in tableData" :key="index">
                  <el-card shadow="always"
                           :class="{'record-success': item.isEnable == 1, 'record-danger': item.isEnable == 0}"
                           style="margin: 5px; padding-bottom: 10px;">
                    <router-link :to="{ path: '/business/cps/equipment/Add', query: { equipmentCode: item.equipmentCode, id: item.id, type: 'view' } }" style="text-decoration: none;">
                      <div style="position: relative; border-bottom: 1px solid #ccc; padding-bottom: 20px;height: 110px;">
                        <span style="position:absolute; top: 10px">
                          <!-- 根据 appType 的值显示不同的图片 -->
                          <img style="height: 70px; width: 70px" v-if="item.image != null && item.image != ''"
                               :src="item.image"/>
                          <img style="height: 70px; width: 70px" v-else src="@/assets/equipmentDefaultImg.png"/>
                          <!-- 添加更多条件根据需要显示不同的图片 -->
                        </span>
                        <span style="position: absolute; top: 0; left: 80px; margin-left: 10px">
                          <strong style="font-size: 18px">{{
                              item.equipmentName.length > 10 ? item.equipmentName.substring(0, 10) + '...' : item.equipmentName
                            }}</strong>
                          <el-tooltip placement="top" effect="dark">
                            <template #content>
                              <img :src="'data:image/png;base64,' +item.qrCode"/>
                            </template>
                            <a style="font-size: 14px; right: 0" @click="rowEdit(item)">
                              <svg-icon icon-class="barcode" style="margin-left: 8px"/>
                            </a>
                          </el-tooltip>
                        </span>
                        <span v-if="item.status == 1" style="font-size: 14px; position: absolute; right: 0; color: #3399FF"><svg-icon icon-class="online"
                                                                                                                                      style="margin-right: 5px"/>在线</span>
                        <span v-else style="font-size: 14px; position: absolute; right: 0; color: #f16506"><svg-icon icon-class="offline"
                                                                                                                     style="margin-right: 5px"/>离线</span>
                        <span style="position: absolute; top: 31px; left: 80px; margin-left: 10px;font-size: 14px">
                          <span style="color: #646a73">设备编码：</span>
                          <span style="color: #1677ff">{{ item.equipmentCode }}</span>
                        </span>
                        <span style="position: absolute; top: 54px; left: 80px; margin-left: 10px;font-size: 14px">
                          <span style="color: #646a73">产品名称：</span>
                          <span style="color: #1677ff">{{ item.productName }}</span>
                        </span>
                        <span style="position: absolute; top: 77px; left: 80px; margin-left: 10px;font-size: 14px">
                          <span style="color: #646a73">关联告警：</span>
                          <span style="color: #1677ff">{{ item.warningCount }}</span>
                        </span>
                      </div>
                    </router-link>
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
                      <div style="float: right" v-if="item.isEnable == 0">
                        <el-button type="primary" @click="rowEdit(item)" size="mini" style="margin: 5px">编辑</el-button>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
            <el-pagination
              v-if="tableData.length!=0"
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
              v-if="tableData.length==0"
              style="width: 100%; margin-top: 50px"
              description="暂无数据"
              :image-size="120"
            ></el-empty>
          </div>
        </el-main>
      </el-container>
    </el-container>

    <BatchAdd ref="batchAdd" @add-success="handleAddSuccess"></BatchAdd>
  </div>
</template>

<script>
import CommonTree from "@/views/components/ComTree/index.vue";
import BatchAdd from "@/views/business/cps/equipment/components/BatchAdd.vue";
import {
  page as getPage,
  enableEquipment,
  disenableEquipment,
  remove,
  reStart,
  submit,
  getChildEquipment
} from '@/api/cps/equipment-api'
import {formatDate} from "@/utils/dateUtils";
import {equipmentUntie} from "@/api/warning/warning-api";

export default {
  name: 'equipment',
  components: {
    CommonTree,
    BatchAdd
  },
  data() {
    return {
      options: [],
      tableLoading: false,
      tableData: [],
      formInline: {},
      selectionList: [],
      equipmentTypeOptions: [], // 设备类型字典
      searchTitle: 'cabinName',
      defaultProps: {
        label: "cabinName",
        value: "id"
      },
      page: {
        pageSize: 12,
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
    this.onLoad(this.page, {});
    this.options = this.$store.state.dict.dictDatas.is_enable
    this.equipmentTypeOptions = this.$store.state.dict.dictDatas.equipment_type
  },
  methods: {
    handleAddSuccess() {
      this.onLoad(this.page, {});
    },
    BatchAdd() {
      this.$refs.batchAdd.openDialog()
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
      console.log("rowrow", row)
      this.$router.push({
        path: `/business/cps/equipment/Add`,
        query: {
          equipmentCode: row.equipmentCode,
          id: row.id,
          type: 'view',
        }
      })
    },
    rowEdit(row) {
      this.$router.push({
        path: `/business/cps/equipment/Add`,
        query: {
          equipmentCode: row.equipmentCode,
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
      // 判断设备下有没有子设备
      getChildEquipment(row.equipmentCode).then(res => {
        if (res.data) {
          this.$message.warning("设备下存在子设备，无法删除！")
        } else {
          if (row.warningCount > 0) {
            this.$confirm("该设备下关联" + row.warningCount + "个告警，" + "确定删除该设备", {
              confirmButtonText: "确认",
              cancelButtonText: "取消",
              type: "warning"
            }).then(() => {
              equipmentUntie(row.equipmentCode).then(res => {
                if (res.data) {
                  remove(row).then(() => {
                    this.onLoad(this.page, {});
                    this.$message.success("操作成功")
                  });
                } else {
                  this.$message.warning('删除失败，解绑场景失败!')
                }
              })
            })
          } else {
            this.$confirm("确定删除数据", {
              confirmButtonText: "确认",
              cancelButtonText: "取消",
              type: "warning"
            }).then(() => {
              remove(row).then(() => {
                this.onLoad(this.page, {});
                this.$message.success("操作成功")
              });
            })
          }
        }
      })
    },
    rowEnable(row) {
      // 在方法里特殊处理进行启用
      enableEquipment(row.id).then(res => {
        const {code, msg} = res;
        if (code === 0) {
          this.$message.success("操作成功")
          this.onLoad(this.page, {})
        } else {
          this.$message.warning(msg);
        }
      })
    },
    rowDisEnable(row) {
      row.isEnable = 0;
      disenableEquipment(row.id).then(res => {
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
      this.page = page;
      this.tableLoading = true;
      getPage(
        page.currentPage,
        page.pageSize,
        Object.assign(params, this.formInline)
      ).then(res => {
        const data = res.data;
        this.page.total = data.total;
        this.tableData = data.records;
        this.tableLoading = false;
      });
    },
  }
}
</script>

<style scoped>
.equipment-card ::v-deep .el-card__body {
  padding: 0 !important;
}

.equipment-card ::v-deep .el-divider--horizontal {
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
</style>
