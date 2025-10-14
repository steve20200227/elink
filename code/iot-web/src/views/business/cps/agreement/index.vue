<template>
  <div>
    <el-row>
      <el-col>
        <div style="display: flex; justify-content: space-between; font-size: 16px; padding-bottom: 5px; margin-top: 20px">
          <div>
            <el-form ref="gridHeadLayout" :inline="true" :model="queryParams" class="demo-form-inline" style="margin-left: 20px">
              <el-form-item>
                <el-input
                  v-model="queryParams.agreementName"
                  size="mini"
                  placeholder="请输入协议名称">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="queryParams.agreementCode"
                  size="mini"
                  placeholder="请输入协议编码">
                </el-input>
              </el-form-item>
              <el-form-item class="top-button">
                <el-button size="mini" icon="el-icon-search" @click="handleQuery"></el-button>
                <el-button size="mini" icon="el-icon-refresh-right" @click="resetQuery"></el-button>
              </el-form-item>
            </el-form>
          </div>
          <div class="top-button">
            <el-button size="mini" type="primary" style="margin-right: 20px" @click="handleAdd">新增协议</el-button>
          </div>
        </div>
      </el-col>
    </el-row>

    <div style="margin-left: 20px">
      <div style="max-height: calc(100vh - 285px);height: calc(100vh - 285px); overflow: auto;" v-if="analyzeAgreementList.length!=0">
        <el-row>
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item, index) in analyzeAgreementList" :key="index">
            <el-card shadow="always" style="margin: 5px; padding-bottom: 10px;">
              <div style="position: relative; border-bottom: 1px solid #ccc; padding-bottom: 20px;height: 90px;">
                      <span style="position:absolute; top: 10px">
                        <img style="height: 70px; width: 70px" v-if="item.image != null && item.image != ''"
                             :src="item.image"/>
                        <img style="height: 70px; width: 70px" v-else src="@/assets/accessProtocol.png"/>
                      </span>
                <span style="position: absolute; top: 0; left: 80px; margin-left: 10px">
                        <strong style="font-size: 18px">{{
                            item.agreementName.length > 10 ? item.agreementName.substring(0, 10) + '...' : item.agreementName
                          }}</strong>
                      </span>
                <el-tooltip class="item" effect="dark" content="编辑" placement="top">
                  <a style="font-size: 14px; position: absolute; right: 0" @click="handleUpdate(item)">
                    <span class="el-icon-setting" style="font-size: 18px;color: #1677FF"></span>
                  </a>
                </el-tooltip>
                <span style="position: absolute; top: 31px; left: 80px; margin-left: 10px;font-size: 14px">
                        <span style="color: #646a73">接入地址：</span>
                        <span style="color: #1677ff">{{ item.accessAddressCopy }}</span>
                      </span>
                <span style="position: absolute; top: 54px; left: 80px; margin-left: 10px;font-size: 14px">
                        <span style="color: #646a73">协议编码：</span>
                        <span style="color: #1677ff">{{ item.agreementCode }}</span>
                      </span>
              </div>
              <div style="margin-top: 10px;">
                <div style="float: left">
                  <el-button size="mini" style="margin: 5px"
                             @click="handleDelete(item)">删除
                  </el-button>
                </div>
                <div style="float: right">
                  <el-button type="text" @click="handleView(item)" size="small">更多信息</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <el-pagination
        v-if="analyzeAgreementList.length!=0"
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
        v-if="analyzeAgreementList.length==0"
        style="width: 100%; margin-top: 50px"
        description="暂无数据"
        :image-size="120"
      ></el-empty>
    </div>



    <el-dialog
      v-dialog-drag
      :title="'自定义协议'"
      :modal-append-to-body="false"
      :close-on-click-modal="false"
      :visible.sync="customProtocolDialog"
      v-show="customProtocolDialog"
      width="40%"
      @close="customProtocolClose"
      class="custom-dialog"
    >
      <CustomProtocolDialog ref="customProtocolRef" :rowData="customProtocolData"
                            @close="customProtocolClose"></CustomProtocolDialog>
      <div slot="footer" class="dialog-footer">
        <el-button @click="customProtocolDialog = false">取 消</el-button>
        <el-button type="primary" v-if="isView" @click="customProtocolConfirm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getAnalyzeAgreementPage, remove} from "@/api/cps/analyze_agreement_api";
import CustomProtocolDialog from '@/views/business/cps/product/components/CustomProtocolDialog.vue'

export default {
  components: {
    CustomProtocolDialog,
  },
  name: "SystemRole",
  data() {
    return {
      isView:true,
      page: {
        pageSize: 12,
        currentPage: 1,
        total: 0
      },
      customProtocolDialog: false,
      customProtocolData: {},
      //数据
      analyzeAgreementList: [],
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 12,
        agreementName: undefined,
        agreementCode: undefined,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    customProtocolClose() {
      this.$refs.customProtocolRef.dataForm = {}
      this.customProtocolDialog = false;
      this.getList();
    },
    /** 查询接入协议列表 */
    getList() {
      this.loading = true;
      getAnalyzeAgreementPage(this.page.currentPage, this.page.pageSize, this.queryParams).then(
        response => {
          this.analyzeAgreementList = response.data.records;
          this.analyzeAgreementList.forEach(item =>{
            item.accessAddressCopy = item.accessAddress
          })
          this.page.total = response.data.total;
          this.loading = false;
        }
      );
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.agreementName = ''
      this.queryParams.agreementCode = ''
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.customProtocolDialog = true;
      this.$nextTick(() => {
        this.$refs.customProtocolRef.init(null, 'add')
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.isView = true
      this.customProtocolDialog = true;
      this.$nextTick(() => {
        this.$refs.customProtocolRef.init(row, 'edit')
      })
    },
    handleView(row){
      this.isView = false
      this.customProtocolDialog = true;
      this.$nextTick(() => {
        this.$refs.customProtocolRef.init(row, 'view')
      })
    },
    /**
     * 确定
     */
    async customProtocolConfirm() {
      await this.cc();
      this.customProtocolDialog = false;
      this.getList();
    },
    cc() {
      return new Promise((resolve, reject) => {
        this.$refs.customProtocolRef.save()
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除?').then(function () {
        return remove(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    changeSize(val) {
      this.page.pageSize = val
      this.getList(this.page, {})
    },
    changeCurrent(val) {
      this.page.currentPage = val
      this.getList(this.page, {})
    },
  }
};
</script>
<style scoped>

.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
}

</style>
