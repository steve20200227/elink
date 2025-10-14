<template>
  <div>
    <el-container>
      <el-main>
        <div style="margin-top: 5px">
          <el-form ref="gridHeadLayout"  :model="searchForm" class="monitoring">
            <el-row>
              <el-col :span="4" style="margin-top: -5px">
                <el-form-item>
                  <el-input
                    v-model="searchForm.equipmentName"
                    size="mini"
                    placeholder="请输入设备名称"
                  >
                  </el-input>

                </el-form-item>
              </el-col>

              <el-col :span="3">

                <el-button-group>
                  <el-button size="mini" icon="el-icon-search" @click="gridHeadSearch"></el-button>
                  <el-button size="mini" icon="el-icon-refresh-right" @click="gridHeadEmpty"></el-button>
                </el-button-group>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <div style="max-height: calc(100vh - 285px);height: calc(100vh - 285px); overflow: auto;" v-if="tableData.length!=0">
          <el-row>
            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item, index) in tableData" :key="index">
              <el-card shadow="always" style="margin: 5px; padding-bottom: 10px;">
                <div style="position: relative; border-bottom: 1px solid #ccc; padding-bottom: 20px;height: 110px;">
                      <span style="position:absolute; top: 10px">
                        <!-- 根据 appType 的值显示不同的图片 -->
                        <img style="height: 70px; width: 70px" v-if="item.image != null && item.image != ''" :src="item.image" />
                        <img style="height: 70px; width: 70px" v-else src="@/assets/equipmentDefaultImg.png" />
                        <!-- 添加更多条件根据需要显示不同的图片 -->
                      </span>
                  <span style="position: absolute; top: 0; left: 80px; margin-left: 10px">
                        <strong style="font-size: 18px">{{ item.equipmentName.length > 10 ? item.equipmentName.substring(0, 10) + '...' : item.equipmentName }}</strong>
                        <span
                          :style="{
                               display: 'inline-block',
                               height: '8px',
                               width: '8px',
                               background: item.isEnable === 1 ? 'rgb(10, 191, 91)' : 'rgb(229, 69, 69)',
                               borderRadius: '7px',
                               verticalAlign: 'middle',
                               marginLeft: '10px',
                               marginBottom: '5px'}">
                        </span>
                      </span>
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
                <div style="margin-top: 10px;">
                  <div style="float: right">
                    <el-button size="mini" type="primary" style="margin: 5px"
                               @click="rowDetail(item)">数据查询
                    </el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
        <div style="text-align: right;margin-top: 30px" v-if="tableData.length!=0">
          <el-pagination
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="page.currentPage"
            :page-sizes="[12, 24, 36, 48, 120]"
            :page-size="page.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="page.total">
          </el-pagination>
        </div>
        <el-empty
          v-if="tableData.length==0"
          style="width: 100%; margin-top: 50px"
          description="暂无数据"
          :image-size="120"
        ></el-empty>
      </el-main>
    </el-container>


  </div>
</template>

<script>
import { formatDate } from "@/utils/dateUtils"

import {page as getPage} from '@/api/cps/equipment-api'
export default {
  name: 'index',
  components:{

  },
  data() {
    return {
      tableLoading:false,
      tableData:[],
      page: {
        pageSize: 12,
        currentPage: 1,
        total: 0
      },
      searchForm:{
        equipmentName: ''
      },
      searchTitle: 'cabinName',
      treeData: [],
      defaultProps: {
        label: 'cabinName',
        value: 'id'
      }
    }
  },
  mounted() {
    this.onLoad(this.page)
  },
  methods: {
    //格式化日期
    formatDate(row, column, cellValue, index){
      let format = formatDate(new Date(cellValue),"yyyy-MM-dd HH:mm:ss")
      return format
    },
    rowDetail(row) {
      this.$router.push({
        path: `/business/cps/data-acquisition-monitoring/View`,
        query: {
          id: row.id,
        }
      })
    },
    gridHeadSearch(query) {
      this.page.currentPage = 1;
      this.onLoad(this.page)
    },
    gridHeadEmpty() {
      this.page.currentPage = 1;
      this.searchForm.equipmentName = '';
      this.onLoad(this.page)
    },
    onLoad(page) {
      this.tableLoading = true;
      let param = {
        // 默认查询条件,添加已启用条件
        isEnable: 1
      };
      param.equipmentName = this.searchForm.equipmentName;
      getPage(
        page.currentPage,
        page.pageSize,
        param
      ).then(res => {
        const data = res.data;
        this.tableData = data.records;
        this.page.total = data.total;
        this.tableLoading = false;
      });
    },
    handleSizeChange(val) {
      this.page.pageSize = val
      this.onLoad(this.page)
    },
    handleCurrentChange(val) {
      this.page.currentPage = val
      this.onLoad(this.page)
    },

  }
}
</script>

<style scoped>
.monitoring ::v-deep .el-form-item {
  width: 98%;
}
.monitoring ::v-deep .el-form-item__content {
  width: 98%;
}

.monitoring ::v-deep .el-select{
  width: 98%;
}
</style>
