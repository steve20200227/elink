<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="60%"
      @close="cancel">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="消息通知" name="message">
          <div>
            <div style="margin-top: 5px; height: 100%">
              <el-form ref="gridHeadLayout" :inline="true" :model="messageArgument"
                       class="demo-form-inline custom-form">
                <el-form-item>
                  <el-input
                    v-model="messageArgument.templateName"
                    size="mini"
                    placeholder="请输入模板名称">
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-select v-model="messageArgument.pushRange" size="mini" placeholder="请选择推送范围">
                    <el-option
                      v-for="item in pushMethodDict"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-select v-model="messageArgument.usersType" size="mini" placeholder="请选择用户类型">
                    <el-option
                      v-for="item in userTypeDict"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item class="top-button" style="margin-top: -1px">
                  <el-button size="mini" @click="messageSearch" icon="el-icon-search"></el-button>
                  <el-button size="mini" @click="messageEmpty" icon="el-icon-refresh-right"></el-button>
                </el-form-item>
                <el-table
                  row-key="id"
                  ref="messageTable"
                  border
                  :data="messageTableData"
                  v-loading="tableLoading"
                  @selection-change="messageSelectionChange"
                  :selection="selectedMessage"
                  tooltip-effect="dark"
                  height="48vh"
                  style="width: 100%;">
                  <el-table-column
                    type="selection"
                    :reserve-selection="true"
                    align="center"
                    :selectable="messageSelectable"
                    width="40">
                  </el-table-column>
                  <el-table-column
                    prop="templateName"
                    label="模板名称"
                    align="center">
                  </el-table-column>
                  <el-table-column
                    prop="messageType"
                    label="消息类型"
                    align="center">
                    <template slot-scope="scope">
                      {{ messageTypeList.find(demo => demo.id === scope.row.pushWays.messageType)?.name || '未知类型' }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="channelType"
                    label="通道类型"
                    align="center">
                    <template slot-scope="scope">
                      <dict-tag
                        :type="DICT_TYPE.MESSAGE_CHANNEL"
                        :value="scope.row.pushWays.channelType"
                      />
                    </template>
                  </el-table-column>
                </el-table>
              </el-form>
              <el-pagination
                background
                :current-page="messagePage.currentPage"
                :page-sizes="[10, 20, 30, 40, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="messagePage.total"
                :page-size="messagePage.pageSize"
                :pager-count="5"
                @size-change="messageChangeSize"
                @current-change="messageCurrent"
                style="float: right; margin: 20px 0;">
              </el-pagination>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="北向输出" name="exportation">
          <div>
            <div style="margin-top: 5px; height: 100%">
              <div style="margin-top: 5px">
                <el-form ref="gridHeadLayout" :inline="true" :model="exportationArgument"
                         class="demo-form-inline custom-form">
                  <el-form-item>
                    <el-input
                      v-model="exportationArgument.actionName"
                      size="mini"
                      placeholder="请输入北向输出名称">
                    </el-input>
                  </el-form-item>
                  <el-form-item class="top-button" style="margin-top: -1px">
                    <el-button size="mini" @click="exportationSearch" icon="el-icon-search"></el-button>
                    <el-button size="mini" @click="exportationEmpty" icon="el-icon-refresh-right"></el-button>
                  </el-form-item>
                  <el-table
                    row-key="id"
                    ref="exportationTable"
                    border
                    :data="exportationTableData"
                    v-loading="tableLoading"
                    @selection-change="exportationSelectionChange"
                    tooltip-effect="dark"
                    :selection="selectedExportation"
                    height="48vh"
                    style="width: 100%;">
                    <el-table-column
                      type="selection"
                      align="center"
                      :reserve-selection="true"
                      :selectable="exportationSelectable"
                      width="40">
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
                </el-form>
                <el-pagination
                  background
                  :current-page="exportationPage.currentPage"
                  :page-sizes="[10, 20, 30, 40, 50, 100]"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="exportationPage.total"
                  :page-size="exportationPage.pageSize"
                  :pager-count="5"
                  @size-change="exportationChangeSize"
                  @current-change="exportationCurrent"
                  style="float: right; margin: 20px 0;">
                </el-pagination>
              </div>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="设备反控" name="recriminate">
          <div style="display: flex;">
            <div style="width: 150px; height: 50vh;flex-shrink: 0;margin-top: 30px">
              <el-steps direction="vertical" :active="active">
                <el-step :title="currentProduct?.productName || '请选择产品'"></el-step>
                <el-step
                  :title="equipmentSelectList.length > 0 ? '已勾选设备量(' + equipmentSelectList.length + ')': '请选择设备'"></el-step>
                <el-step :title="featureNames !== '' ? featureNames : '请选择功能'"></el-step>
              </el-steps>
            </div>
            <div style="width: calc(100% - 150px);">
              <div style="margin-top: 5px; height: 100%;">
                <div style="margin-top: 5px;">
                  <div v-show="active === 1">
                    <el-form ref="gridHeadLayout" :inline="true" :model="productArgument"
                             class="demo-form-inline custom-form">
                      <el-form-item>
                        <el-input
                          v-model="productArgument.productName"
                          size="mini"
                          placeholder="请输入产品名称">
                        </el-input>
                      </el-form-item>
                      <el-form-item class="top-button" style="margin-top: -1px">
                        <el-button size="mini" @click="productSearch" icon="el-icon-search"></el-button>
                        <el-button size="mini" @click="productEmpty" icon="el-icon-refresh-right"></el-button>
                      </el-form-item>
                      <el-table
                        ref="productTable"
                        :data="productTableData"
                        border
                        highlight-current-row
                        height="46vh"
                        @current-change="productCurrentChange"
                        style="width: 100%;">
                        <el-table-column
                          prop="productName"
                          label="产品名称"
                          align="center"
                          show-overflow-tooltip>
                        </el-table-column>
                        <el-table-column
                          prop="productCode"
                          label="产品编码"
                          align="center"
                          show-overflow-tooltip>
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
                    </el-form>
                    <el-pagination
                      background
                      :current-page="productPage.currentPage"
                      :page-sizes="[10, 20, 30, 40, 50, 100]"
                      layout="total, sizes, prev, pager, next, jumper"
                      :total="productPage.total"
                      :page-size="productPage.pageSize"
                      :pager-count="5"
                      @size-change="productChangeSize"
                      @current-change="productCurrent"
                      style="float: right; margin: 20px 0;">
                    </el-pagination>
                  </div>
                  <div v-show="active === 2">
                    <el-form ref="gridHeadLayout" :inline="true" :model="recriminateArgument"
                             class="demo-form-inline custom-form">
                      <el-form-item>
                        <el-input
                          v-model="recriminateArgument.equipmentName"
                          size="mini"
                          placeholder="请输入设备名称">
                        </el-input>
                      </el-form-item>
                      <el-form-item class="top-button" style="margin-top: -1px">
                        <el-button size="mini" @click="recriminateSearch" icon="el-icon-search"></el-button>
                        <el-button size="mini" @click="recriminateEmpty" icon="el-icon-refresh-right"></el-button>
                      </el-form-item>
                      <el-table
                        row-key="id"
                        ref="equipmentTable"
                        border
                        :data="recriminateTableData"
                        v-loading="tableLoading"
                        @selection-change="recriminateSelectionChange"
                        tooltip-effect="dark"
                        :selection="selectedRecriminate"
                        style="width: 100%;">
                        <el-table-column
                          type="selection"
                          :reserve-selection="true"
                          align="center"
                          width="40">
                        </el-table-column>
                        <el-table-column
                          prop="equipmentName"
                          label="设备名称"
                          align="center"
                          show-overflow-tooltip>
                        </el-table-column>
                        <el-table-column
                          prop="equipmentCode"
                          label="设备编码"
                          align="center"
                          show-overflow-tooltip>
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
                    </el-form>
                    <el-pagination
                      background
                      :current-page="recriminatePage.currentPage"
                      :page-sizes="[10, 20, 30, 40, 50, 100]"
                      layout="total, sizes, prev, pager, next, jumper"
                      :total="recriminatePage.total"
                      :page-size="recriminatePage.pageSize"
                      :pager-count="5"
                      @size-change="recriminateChangeSize"
                      @current-change="recriminateCurrent"
                      style="float: right; margin: 20px 0;">
                    </el-pagination>
                  </div>
                  <div v-show="active === 3">
                    <el-form ref="gridHeadLayout" :inline="true" :model="featureArgument"
                             class="demo-form-inline custom-form">
                      <el-form-item>
                        <el-input
                          v-model="featureArgument.pointName"
                          size="mini"
                          placeholder="请输入功能名称">
                        </el-input>
                      </el-form-item>
                      <el-form-item class="top-button" style="margin-top: -1px">
                        <el-button size="mini" @click="featureTableSearch" icon="el-icon-search"></el-button>
                        <el-button size="mini" @click="featureTableEmpty" icon="el-icon-refresh-right"></el-button>
                      </el-form-item>
                      <el-table
                        row-key="id"
                        ref="featureTable"
                        border
                        :data="featureTableData"
                        v-loading="tableLoading"
                        @selection-change="featureSelectionChange"
                        tooltip-effect="dark"
                        :selection="selectedRecriminate"
                        style="width: 100%;">
                        <el-table-column
                          type="selection"
                          :reserve-selection="true"
                          align="center"
                          :selectable="featureSelectable"
                          width="40">
                        </el-table-column>
                        <el-table-column
                          prop="pointName"
                          label="功能名称"
                          align="center"
                          show-overflow-tooltip>
                        </el-table-column>
                        <el-table-column
                          prop="pointCode"
                          label="功能编码"
                          align="center"
                          show-overflow-tooltip>
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
                    </el-form>
                    <el-pagination
                      background
                      :current-page="featurePage.currentPage"
                      :page-sizes="[10, 20, 30, 40, 50, 100]"
                      layout="total, sizes, prev, pager, next, jumper"
                      :total="featurePage.total"
                      :page-size="featurePage.pageSize"
                      :pager-count="5"
                      @size-change="featureChangeSize"
                      @current-change="featureCurrent"
                      style="float: right; margin: 20px 0;">
                    </el-pagination>
                  </div>
                  <div style="margin-top: 10px">
                    <el-button size="mini" v-if="active > 1" type="primary" @click="previous">上一步</el-button>
                    <el-button size="mini" v-if="active < 3 " type="primary" @click="next">下一步</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
      <div style="float: right">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" type="primary" @click="determine">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getPage, getByIds as getMessageByIds} from "@/api/message/template";
import {page as getExportationPage, getByIds as getExportationByIds} from "@/api/warning/warning-north-api";
import {getProductByEnable, page as getProductPage} from "@/api/cps/product-api";
import {page as getRecriminatePage, getByIds as getRecriminateByIds} from '@/api/cps/equipment-api'
import {getByEquipmentIdsAndCode, page as getFeaturePageByEquipmentId} from '@/api/cps/equipment-point-api'
import {page as getFeaturePageByProductId} from '@/api/cps/product-point-api'

export default {
  data() {
    return {
      title: '动作配置',
      active: 1,
      productCurrentRow: null,
      currentProduct: {},
      currentEquipment: {},
      isProduct: false,
      selectedRecriminate: [], // 勾选存储设备
      selectedMessage: [], // 勾选存储消息列表
      selectedExportation: [], // 勾选存储北向输出列表
      equipmentSelectList: [], // 已勾选设备列表
      messageSelectList: [], // 已勾选消息列表
      exportationSelectList: [], // 已勾选北向输出列表
      equipmentSelect: [], // 控制是否能勾选设备列表
      messageSelect: [], // 控制是否能勾选发消息列表
      exportationSelect: [], // 控制是否能勾选北向输出列表
      productCode: '',
      productId: '',
      productList: [],
      recriminateTableData: [],
      featureTableData: [],
      featureSelectList: [], // 勾选功能列表
      featureIdsSelect: [], // 已勾选功能列表id集合
      featureSelect: [], // 已勾选功能列表集合
      recriminateActionData: [],
      recriminateArgument: {},
      featureArgument: {},
      productTableData: [],
      productArgument: {},
      recriminatePage: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      productPage: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      featurePage: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      equipmentNames: '',
      featureNames: '',

      exportationArgument: {},
      exportationTableData: [],
      exportationActionData: [],
      exportationPage: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      messageActionData: [],
      dialogFlag: false,
      tableLoading: false,
      activeName: 'second',
      messageArgument: {},
      pushMethodDict: [],
      userTypeDict: [],
      messageTableData: [],
      messagePage: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      messageTypeList: [
        {id: '1', name: '文本消息'},
        //
        {id: '4-1', name: '钉钉-图片消息'},
        {id: '4-2', name: '钉钉-语音消息'},
        {id: '4-3', name: '钉钉-文件消息'},
        {id: '4-4', name: '钉钉-链接消息'},
        {id: '4-5', name: '钉钉-OA 消息'},
        {id: '4-6', name: '钉钉-markdown 消息'},
        {id: '4-7', name: '钉钉-卡片消息'},
        //
        {id: '5-1', name: '企业微信-图片消息'},
        {id: '5-2', name: '企业微信-语音消息'},
        {id: '5-3', name: '企业微信-视频消息'},
        {id: '5-4', name: '企业微信-文件消息'},
        {id: '5-5', name: '企业微信-文本卡片消息'},
        {id: '5-6', name: '企业微信-图文消息(mpnews)'},
        {id: '5-7', name: '企业微信-markdown 消息'},
        {id: '5-8', name: '企业微信-小程序通知消息'},
        //
        {id: '6-1', name: '飞书-富文本 post'},
        {id: '6-2', name: '飞书-图片 image'},
        {id: '6-3', name: '飞书-消息卡片 interactive'},
        {id: '6-4', name: '飞书-分享群名片 share_chat'},
        {id: '6-5', name: '飞书-分享个人名片 share_user'},
        {id: '6-6', name: '飞书-语音 audio'},
        {id: '6-7', name: '飞书-视频 media'},
        {id: '6-8', name: '飞书-文件 file'},
        {id: '6-9', name: '飞书-表情包 sticker'}
      ]
    };
  },
  mounted() {
    this.pushMethodDict = this.$store.state.dict.dictDatas.message_push_method
    this.userTypeDict = this.$store.state.dict.dictDatas.message_user_type
    this.getProduct()
  },
  methods: {
    // 消息类型方法
    messageSelectionChange(data) {
      // 勾选发消息
      if (data.length > 0) {
        this.messageSelectList = data;
      }
    },
    messageSelectable(row) {
      if (this.messageSelect.length > 0) {
        let ids = this.messageSelect.map(item => item.id)
        return !ids.includes(row.id)
      } else {
        return true;
      }
    },
    messageChangeSize(val) {
      this.messagePage.pageSize = val
      this.messageOnLoad()
    },
    messageCurrent(val) {
      this.messagePage.currentPage = val
      this.messageOnLoad()
    },
    messageSearch() {
      this.messageOnLoad(this.messageArgument)
    },
    messageEmpty() {
      this.messageArgument = {}
      this.messageOnLoad()
    },
    messageInit() {
      this.activeName = 'message';
      this.messageOnLoad();
    },
    messageOnLoad(params = {}) {
      params.currentPage = this.messagePage.currentPage
      params.pageSize = this.messagePage.pageSize
      this.tableLoading = true
      getPage(params).then(res => {
        const data = res.data
        this.messageTableData = data.records
        this.messageTableData.forEach(item => {

          item.pushWays = JSON.parse(item.pushWays)
          //设置发送消息的必要参数
          item.relatedParameter = JSON.parse(item.relatedParameter)
          item.relatedParameter.templateId = item.id
          item.relatedParameter.channelType = item.pushWays.channelType
          item.relatedParameter = JSON.stringify(item.relatedParameter)
        })
        this.tableLoading = false
        this.messagePage.total = data.total
      })
    },

    // 北向输出类型方法
    exportationSelectionChange(data) {
      // 勾选北向输出
      if (data.length > 0) {
        this.exportationSelectList = data;
      }
    },
    exportationInit() {
      this.activeName = 'exportation'
      this.exportationOnLoad(this.exportationPage, {})
    },
    exportationSearch() {
      this.exportationOnLoad(this.exportationPage, this.exportationArgument)
    },
    exportationEmpty() {
      this.exportationArgument = {}
      this.exportationOnLoad(this.exportationPage, {})
    },
    exportationChangeSize(val) {
      this.exportationPage.pageSize = val
      this.exportationOnLoad(this.exportationPage, {})
    },
    exportationCurrent(val) {
      this.exportationPage.currentPage = val
      this.exportationOnLoad(this.exportationPage, {})
    },
    exportationSelectable(row) {
      if (this.exportationSelect.length > 0) {
        let ids = this.exportationSelect.map(item => item.id)
        return !ids.includes(row.id)
      } else {
        return true;
      }
    },
    exportationOnLoad(page, params = {}) {
      this.exportationPage = page;
      this.tableLoading = true;
      getExportationPage(
        page.currentPage,
        page.pageSize,
        Object.assign(params, this.exportationArgument)
      ).then(res => {
        const data = res.data;
        this.exportationPage.total = data.total;
        this.exportationTableData = data.records;
        this.tableLoading = false;
      });
    },

    // 设备反控类型方法
    recriminateSelectionChange(data) {
      // 勾选设备
      this.equipmentSelectList = data;
      this.equipmentNames = this.equipmentSelectList.map(item => item.equipmentName).join(',')

    },
    initRecriminate() {
      this.activeName = 'recriminate'
      this.productOnLoad(this.productPage, {})
    },
    getProduct() {
      getProductByEnable().then((res) => {
        if (res.data.length > 0) {
          this.productList = res.data
          this.productCode = res.data[0].productCode
          this.productId = res.data[0].id
        }
      })
    },
    recriminateChangeSize(val) {
      this.recriminatePage.pageSize = val
      this.recriminateOnLoad(this.recriminatePage, {})
    },
    recriminateCurrent(val) {
      this.recriminatePage.currentPage = val
      this.recriminateOnLoad(this.recriminatePage, {})
    },
    recriminateSearch() {
      this.recriminatePage.currentPage = 1
      this.recriminateOnLoad(this.recriminatePage, this.recriminateArgument)
    },
    recriminateEmpty() {
      this.recriminateArgument = {}
      this.recriminateOnLoad(this.recriminatePage, {})
    },
    recriminateOnLoad(page, params) {
      params.productCode = this.currentProduct.productCode;
      params.isEnable = 1;
      this.recriminatePage = page;
      this.tableLoading = true;
      getRecriminatePage(
        page.currentPage,
        page.pageSize,
        Object.assign(params, this.recriminateArgument)
      ).then(res => {
        const data = res.data;
        this.recriminatePage.total = data.total;
        this.recriminateTableData = data.records;
        this.tableLoading = false;
      });
    },
    next() {
      if (this.active === 3) return;
      if (this.active === 1) {
        if (!this.productCurrentRow) {
          this.$message.warning('请选择产品信息！')
          return;
        } else {
          this.currentProduct = this.productCurrentRow;
        }
      }
      if (this.active === 2) {
        if (this.equipmentSelectList.length === 0) {
          this.$message.warning('请勾选设备信息！')
          return;
        } else if (this.equipmentSelectList.length === 1) {
          // 走查询设备下所有已启用功能逻辑
          this.currentEquipment = this.equipmentSelectList[0]
        } else {
          // 走查询产品下所有已启用功能逻辑
          this.isProduct = true;
        }
      }
      this.active++;
      this.$nextTick(() => {
        if (this.active === 2) {
          this.recriminateOnLoad(this.recriminatePage, {});
        } else if (this.active === 3) {
          this.featureOnLoad(this.featurePage, {});
        }
      })
    },
    previous() {
      if (this.active === 1) return;
      if (this.active === 2) {
        this.currentProduct = {}
        this.$refs.equipmentTable.clearSelection();
        this.equipmentSelectList = []
        this.equipmentNames = ''
      }
      if (this.active === 3) {
        this.isProduct = false
        this.$refs.equipmentTable.clearSelection();
        this.$refs.featureTable.clearSelection();
        this.featureSelectList = [];
        this.currentEquipment = {}
        this.featureNames = ''
      }
      this.active--;
      this.$nextTick(() => {
        this.productOnLoad(this.productPage, {})
      })
    },
    productSearch() {
      this.productPage.currentPage = 1
      this.productOnLoad(this.productPage, this.productArgument)
    },
    productEmpty() {
      this.productArgument = {}
      this.productOnLoad(this.productPage, {})
    },
    productCurrentChange(val) {
      this.productCurrentRow = val;
    },
    productChangeSize(val) {
      this.productPage.pageSize = val
      this.productOnLoad(this.productPage, {})
    },
    productCurrent(val) {
      this.productPage.currentPage = val
      this.productOnLoad(this.productPage, {})
    },
    productOnLoad(page, params) {
      params.isEnable = 1;
      this.productPage = page;
      this.tableLoading = true;
      getProductPage(
        page.currentPage,
        page.pageSize,
        Object.assign(params, this.productArgument)
      ).then(res => {
        const data = res.data;
        this.productPage.total = data.total;
        this.productTableData = data.records;
        this.tableLoading = false;
      });
    },
    featureTableSearch() {
      this.featurePage.currentPage = 1
      this.featureOnLoad(this.featurePage, this.featureArgument)
    },
    featureTableEmpty() {
      this.featureArgument = {}
      this.featureOnLoad(this.productPage, {})
    },
    featureChangeSize(val) {
      this.featurePage.pageSize = val
      this.featureOnLoad(this.featurePage, {})
    },
    featureCurrent(val) {
      this.featurePage.currentPage = val
      this.featureOnLoad(this.featurePage, {})
    },
    // 选择功能
    featureSelectionChange(data) {
      this.featureSelectList = data;
      if (this.featureSelectList.length > 0) {
        this.featureNames = this.featureSelectList.map(item => item.pointName).join(',')
      }
    },
    featureSelectable(row) {
      if (this.featureSelectList.length > 0) { // 保证每次只能选择一个功能
        return this.featureSelectList[0].id == row.id;
      } else {
        if (this.featureIdsSelect.length > 0) {
          return !this.featureIdsSelect.includes(row.id)
        } else {
          return true;
        }
      }
    },
    featureOnLoad(page, params) {
      this.featurePage = page;
      this.tableLoading = true;
      params.isEnable = 1;
      params.pointType = 'pointFeature';
      if (this.isProduct) {
        params.productId = this.currentProduct.id;
        getFeaturePageByProductId(
          page.currentPage,
          page.pageSize,
          Object.assign(params, this.productArgument)
        ).then((res) => {
          const data = res.data;
          this.featurePage.total = data.total;
          this.featureTableData = data.records;
          this.tableLoading = false;
        })
      } else {
        params.equipmentId = this.currentEquipment.id;
        getFeaturePageByEquipmentId(
          page.currentPage,
          page.pageSize,
          Object.assign(params, this.productArgument)
        ).then((res) => {
          const data = res.data;
          this.featurePage.total = data.total;
          this.featureTableData = data.records;
          this.tableLoading = false;
        })
      }
    },

    // 整体操作类型方法
    handleClick(tab) {
      // 切换动作类型
      if (tab.name === 'message') {
        this.messageInit();
      } else if (tab.name === 'exportation') {
        this.exportationInit();
      } else {
        this.initRecriminate();
      }
    },
    init(actionData) {
      let messageData = []
      let recriminateData = []
      let exportationData = []
      if (actionData.length > 0) {
        actionData.forEach(e => {
          if (e.actionType == 1) {
            // 构建发消息

            messageData.push(e);
          } else if (e.actionType == 2) {
            // 构建设备反控
            recriminateData.push(e);
          } else {
            // 构建北向输出
            exportationData.push(e);
          }
        })
        if (messageData.length > 0) {
          this.selectedMessage = messageData;
          this.selectedMessage.forEach(item => {
            item.isAppend = true;
          });
          const ids = messageData.map(item => item.actionId).join(',');
          getMessageByIds(ids).then((res) => {
            this.messageSelect = res.data;
          })
        }
        if (recriminateData.length > 0) {
          this.featureIdsSelect = recriminateData.map(item => item.actionId);
          this.selectedRecriminate = recriminateData;
          this.selectedRecriminate.forEach(item => {
            item.isAppend = true;
          });
          getRecriminateByIds(recriminateData[0].sourceIds).then((res) => {
            this.equipmentSelect = res.data;
          })
        }
        if (exportationData.length > 0) {
          this.selectedExportation = exportationData;
          this.selectedExportation.forEach(item => {
            item.isAppend = true;
          });
          const ids = exportationData.map(item => item.actionId).join(',');
          getExportationByIds(ids).then((res) => {
            this.exportationSelect = res.data;
          })
        }
      }
      this.messageInit();
      this.dialogFlag = true;
    },
    determine() {

      this.messageSelectList.push(...this.selectedMessage);
      this.exportationSelectList.push(...this.selectedExportation);
      this.featureSelectList.push(...this.selectedRecriminate);
      this.buildAndSave()
    },
    buildAndSave() {
      // 构建保存的参数并进行保存操作
      let actionData = [];
      this.$nextTick(async () => {
        const sourceIds = this.equipmentSelectList.map(e => e.id).join(',')
        // 构建出功能参数
        let featurateParameter = []
        if (this.equipmentSelectList.length > 0) {
          // 单个设备的情况
          if (this.equipmentSelectList.length == 1) {
            let data = {}
            data.topic = this.equipmentSelectList[0].featureIssued
            data.payload = this.featureSelectList[0].inputParameter
            featurateParameter.push(data)
          }
          // 多个设备的情况
          if (this.equipmentSelectList.length > 1) {
            // 获取设备对应的功能数据
            let equipmentFeatureList = []
            let params = {}
            params.equipmentIds = sourceIds.split(",")
            params.featureCode = this.featureSelectList[0].pointCode
            await getByEquipmentIdsAndCode(params).then(res => {
              equipmentFeatureList = res.data
            })
            // 构造参数
            for (let i = 0; i < this.equipmentSelectList.length; i++) {
              let data = {}
              data.topic = this.equipmentSelectList[i].featureIssued
              data.payload = equipmentFeatureList[i].inputParameter
              featurateParameter.push(data)
            }
          }
        }
        // 如果勾选的同一产品下的功能模型有数据，则将勾选的功能点位进行保存并覆盖原先的数据
        this.featureSelectList.forEach(e => {
          if (e.isAppend) {
            // 原先的数据，直接保存
            actionData.push(e);
          } else {
            let data = {
              actionId: e.id, // 功能的id
              actionName: e.pointName, // 功能模型名称
              actionType: 2, // 2 设备反控
              outputWay: "feature", // 输出方式 - 设备反控
              relatedParameter: JSON.stringify(featurateParameter), // 输入参数
              sourceIds: sourceIds, // 记录了源数据(设备数据ids)
              productName: this.productCurrentRow.productName
            }
            actionData.push(data);
          }
        });
        this.messageSelectList.forEach(e => {
          if (e.isAppend) {
            actionData.push(e);
          } else {
            let data = {
              actionId: e.id,
              actionName: e.templateName,
              actionType: 1, // 1 发消息
              outputWay: e.pushWays.channelType,
              relatedParameter: e.relatedParameter,
            }
            actionData.push(data);
          }
        });
        this.exportationSelectList.forEach(e => {
          if (e.isAppend) {
            actionData.push(e);
          } else {
            let data = {
              actionId: e.id,
              actionName: e.actionName,
              actionType: e.actionType, // 这里维护了状态北向输出为3
              outputWay: e.outputWay,
              relatedParameter: e.relatedParameter,
            }
            actionData.push(data);
          }
        });
        this.$emit("saveAction", actionData)
        this.cancel()
      })
    },
    cancel() {
      this.dialogFlag = false; // 关闭对话框
      // 清除数据
      this.selectedRecriminate = [];
      this.selectedMessage = [];
      this.selectedExportation = [];
      this.equipmentSelectList = [];
      this.messageSelectList = [];
      this.exportationSelectList = [];
      this.equipmentSelect = [];
      this.messageSelect = [];
      this.exportationSelect = [];
      this.featureSelectList = [];
      this.featureIdsSelect = [];
      this.featureSelect = [];
      this.featureNames = '';
      this.active = 1;
      // 清除勾选框
      this.$refs.messageTable.clearSelection();
      this.$refs.exportationTable.clearSelection();
      this.$refs.productTable.clearSelection();
      this.$refs.equipmentTable.clearSelection();
      this.$refs.featureTable.clearSelection();
    }
  },
};
</script>

<style scoped>
</style>
