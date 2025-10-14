<template>
  <div class="product-dialog">
    <el-dialog
      :close-on-click-modal="true"
      title="选择设备"
      :visible.sync="selectProductShow"
      @close="close"
      width="60%">
      <el-container>
        <div>
          <CommonTree
            :treeData="treeData"
            :defaultProps="defaultProps"
            :searchTitle="searchTitle"
            :isShowdig="false"
            :titleShow="false"
            :showCheckbox="false"
            @getNodeClick="getNodeClick"
          />
        </div>

        <el-container class="right-table">
          <el-main>
            <div>
              <el-form ref="gridHeadLayout" :inline="true" :model="formInline" class="demo-form-inline">
                <el-form-item>
                  <el-input
                    v-model="formInline.assetCode"
                    size="mini"
                    placeholder="请输入设备编码">
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-input
                    v-model="formInline.assetName"
                    size="mini"
                    placeholder="请输入设备名称">
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
                height="calc(100vh - 360px)"
                :data="tableData"
                v-loading="tableLoading"
                tooltip-effect="dark"
                @selection-change="handleSelectionChange"
                style="width: 100%;">
                <el-table-column
                  fixed
                  type="selection"
                  align="center"
                  width="40">
                </el-table-column>
                <el-table-column
                  label="设备编码"
                  align="center"
                  prop="assetCode">
                </el-table-column>
                <el-table-column
                  prop="assetName"
                  label="设备名称"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="cabinName"
                  label="舱室信息"
                  align="center"
                  show-overflow-tooltip>
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
      </el-container>
      <div style="float: right; margin-bottom: 10px">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" @click="selectData" type="primary">确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import { tree, getPage } from '@/api/cps/equipment-api'
import CommonTree from "@/views/components/ComTree/index.vue";

export default {
  name: "tableDialog",
  components: {CommonTree},
  data() {
    return {
      searchTitle: "assetClassifyName",
      selectProductShow: false,
      formInline: {},
      query: {},
      dataTotal: 0,
      tableLoading: false,
      disabledList:[],
      tableData: [
          {
            "id": "1802979443885744130",
            "createUser": "1123598821738675201",
            "createDept": "1647788810121388033",
            "createTime": "2024-06-18 16:19:32",
            "updateUser": "1123598821738675201",
            "updateTime": "2024-06-25 11:39:11",
            "status": 10001,
            "isDeleted": 0,
            "tenantId": "000000",
            "cabinName": "test24",
            "cabinCode": "test24",
            "assetCode": "test001",
            "assetName": "测试数据",
            "importanceLevel": "10001",
            "assetClassifyCode": "",
            "assetLocation": "",
            "assetPrincipalId": "-1",
            "constructionStartDate": "",
            "constructionEndDate": "",
            "activeDate": "",
            "latitude": "-1.000",
            "longitude": "-1.000",
            "assetModel": "",
            "assetSupplier": "",
            "lastInspectTime": "2024-06-18 17:50:25",
            "nextInspectTime": "",
            "parentId": "1777583970600742913",
            "seq": null,
            "assetImages": "",
            "assetFiles": "",
            "address": "",
            "purchaseDate": "2024-06-18",
            "warrantyDate": 180,
            "isExceedShelfLife": 0,
            "assetClassifyOneId": null,
            "assetClassifyTwoId": null,
            "assetClassifyCodeOne": "ZC001",
            "assetClassifyNameOne": "设备",
            "assetClassifyCodeTwo": "CJSB",
            "assetClassifyNameTwo": "采集设备",
            "dtlVOList": [],
            "spareVOList": [],
            "assetPrincipalName": "",
            "disabled": false,
            "importanceLevelName": "",
            "deviceClassifyId": -1,
            "codeOrName": "",
            "label": "",
            "value": "",
            "hasChildren": false
          },
          {
            "id": "1801203398083637250",
            "createUser": "1123598821738675201",
            "createDept": "1647788810121388033",
            "createTime": "2024-06-13 18:42:09",
            "updateUser": "1123598821738675201",
            "updateTime": "2024-06-13 18:42:09",
            "status": 10001,
            "isDeleted": 0,
            "tenantId": "000000",
            "cabinName": "II标段K0",
            "cabinCode": "II-K0",
            "assetCode": "GXSB001",
            "assetName": "采集器控制单元003",
            "importanceLevel": "",
            "assetClassifyCode": "GXSB",
            "assetLocation": "",
            "assetPrincipalId": null,
            "constructionStartDate": "",
            "constructionEndDate": "",
            "activeDate": "",
            "latitude": -1,
            "longitude": -1,
            "assetModel": "",
            "assetSupplier": "",
            "lastInspectTime": "",
            "nextInspectTime": "",
            "parentId": "1800780367761063937",
            "seq": 0,
            "assetImages": "",
            "assetFiles": "",
            "address": "rtsp://admin:*****HAOren778@60.174.100.175:554/cam/realmonitor?channel=1&subtype=0&unicast=true",
            "purchaseDate": "2024-03-01",
            "warrantyDate": null,
            "isExceedShelfLife": null,
            "assetClassifyOneId": null,
            "assetClassifyTwoId": null,
            "assetClassifyCodeOne": "ZC001",
            "assetClassifyNameOne": "设备",
            "assetClassifyCodeTwo": "GXSB",
            "assetClassifyNameTwo": "光纤设备",
            "dtlVOList": [],
            "spareVOList": [],
            "assetPrincipalName": "",
            "disabled": false,
            "importanceLevelName": "",
            "deviceClassifyId": -1,
            "codeOrName": "",
            "label": "",
            "value": "",
            "hasChildren": false
          },
          {
            "id": "1790952507156230147",
            "createUser": "1123598821738675201",
            "createDept": "1647791050479509506",
            "createTime": "2024-05-16 11:48:47",
            "updateUser": "1123598821738675201",
            "updateTime": "2024-05-31 18:13:30",
            "status": 10001,
            "isDeleted": 0,
            "tenantId": "000000",
            "cabinName": "Ⅲ新桥1",
            "cabinCode": "XC11201",
            "assetCode": "JUJWSD1",
            "assetName": "九纯建温湿度检测器1号",
            "importanceLevel": "10001",
            "assetClassifyCode": "",
            "assetLocation": "",
            "assetPrincipalId": "-1",
            "constructionStartDate": "",
            "constructionEndDate": "",
            "activeDate": "",
            "latitude": -1,
            "longitude": -1,
            "assetModel": "",
            "assetSupplier": "",
            "lastInspectTime": "",
            "nextInspectTime": "",
            "parentId": "1800780367761063937",
            "seq": null,
            "assetImages": "",
            "assetFiles": "",
            "address": "",
            "purchaseDate": "2024-05-01",
            "warrantyDate": 1800,
            "isExceedShelfLife": 0,
            "assetClassifyOneId": null,
            "assetClassifyTwoId": null,
            "assetClassifyCodeOne": "ZC001",
            "assetClassifyNameOne": "设备",
            "assetClassifyCodeTwo": "GXSB",
            "assetClassifyNameTwo": "光纤设备",
            "dtlVOList": [],
            "spareVOList": [],
            "assetPrincipalName": "",
            "disabled": false,
            "importanceLevelName": "",
            "deviceClassifyId": -1,
            "codeOrName": "",
            "label": "",
            "value": "",
            "hasChildren": false
          },
          {
            "id": "1790952507156230146",
            "createUser": "1123598821738675201",
            "createDept": "1647791050479509506",
            "createTime": "2024-05-16 11:48:47",
            "updateUser": "1123598821738675201",
            "updateTime": "2024-05-31 18:13:30",
            "status": 10001,
            "isDeleted": 0,
            "tenantId": "000000",
            "cabinName": "II标段K0",
            "cabinCode": "II-K0",
            "assetCode": "JUJWSD1",
            "assetName": "九纯建温湿度检测器1号",
            "importanceLevel": "10001",
            "assetClassifyCode": "",
            "assetLocation": "",
            "assetPrincipalId": "-1",
            "constructionStartDate": "",
            "constructionEndDate": "",
            "activeDate": "",
            "latitude": -1,
            "longitude": -1,
            "assetModel": "",
            "assetSupplier": "",
            "lastInspectTime": "",
            "nextInspectTime": "",
            "parentId": "1777583970600742913",
            "seq": null,
            "assetImages": "",
            "assetFiles": "",
            "address": "",
            "purchaseDate": "2024-05-01",
            "warrantyDate": 1800,
            "isExceedShelfLife": 0,
            "assetClassifyOneId": null,
            "assetClassifyTwoId": null,
            "assetClassifyCodeOne": "ZC001",
            "assetClassifyNameOne": "设备",
            "assetClassifyCodeTwo": "CJSB",
            "assetClassifyNameTwo": "采集设备",
            "dtlVOList": [],
            "spareVOList": [],
            "assetPrincipalName": "",
            "disabled": false,
            "importanceLevelName": "",
            "deviceClassifyId": -1,
            "codeOrName": "",
            "label": "",
            "value": "",
            "hasChildren": false
          },
          {
            "id": "1790952283490775041",
            "createUser": "1123598821738675201",
            "createDept": "1647791050479509506",
            "createTime": "2024-05-16 11:47:53",
            "updateUser": "1123598821738675201",
            "updateTime": "2024-05-31 18:13:20",
            "status": 10001,
            "isDeleted": 0,
            "tenantId": "000000",
            "cabinName": "II标段K0",
            "cabinCode": "II-K0",
            "assetCode": "JUJH2S1",
            "assetName": "九纯建硫化氢检测器1号",
            "importanceLevel": "10001",
            "assetClassifyCode": "",
            "assetLocation": "",
            "assetPrincipalId": "-1",
            "constructionStartDate": "",
            "constructionEndDate": "",
            "activeDate": "",
            "latitude": "-1.000",
            "longitude": "-1.000",
            "assetModel": "",
            "assetSupplier": "",
            "lastInspectTime": "",
            "nextInspectTime": "",
            "parentId": "1777583970600742913",
            "seq": null,
            "assetImages": "",
            "assetFiles": "",
            "address": "",
            "purchaseDate": "2024-05-01",
            "warrantyDate": 1800,
            "isExceedShelfLife": 0,
            "assetClassifyOneId": null,
            "assetClassifyTwoId": null,
            "assetClassifyCodeOne": "ZC001",
            "assetClassifyNameOne": "设备",
            "assetClassifyCodeTwo": "CJSB",
            "assetClassifyNameTwo": "采集设备",
            "dtlVOList": [],
            "spareVOList": [],
            "assetPrincipalName": "",
            "disabled": false,
            "importanceLevelName": "",
            "deviceClassifyId": -1,
            "codeOrName": "",
            "label": "",
            "value": "",
            "hasChildren": false
          },
          {
            "id": "1790952091580395521",
            "createUser": "1123598821738675201",
            "createDept": "1647791050479509506",
            "createTime": "2024-05-16 11:47:08",
            "updateUser": "1123598821738675201",
            "updateTime": "2024-05-31 18:13:11",
            "status": 10001,
            "isDeleted": 0,
            "tenantId": "000000",
            "cabinName": "II标段K0",
            "cabinCode": "II-K0",
            "assetCode": "JUJCO21",
            "assetName": "九纯建二氧化碳检测器1号",
            "importanceLevel": "10001",
            "assetClassifyCode": "",
            "assetLocation": "",
            "assetPrincipalId": "-1",
            "constructionStartDate": "",
            "constructionEndDate": "",
            "activeDate": "",
            "latitude": "-1.000",
            "longitude": "-1.000",
            "assetModel": "",
            "assetSupplier": "",
            "lastInspectTime": "",
            "nextInspectTime": "",
            "parentId": "1777583970600742913",
            "seq": null,
            "assetImages": "",
            "assetFiles": "",
            "address": "",
            "purchaseDate": "2024-05-01",
            "warrantyDate": 1800,
            "isExceedShelfLife": 0,
            "assetClassifyOneId": null,
            "assetClassifyTwoId": null,
            "assetClassifyCodeOne": "ZC001",
            "assetClassifyNameOne": "设备",
            "assetClassifyCodeTwo": "CJSB",
            "assetClassifyNameTwo": "采集设备",
            "dtlVOList": [],
            "spareVOList": [],
            "assetPrincipalName": "",
            "disabled": false,
            "importanceLevelName": "",
            "deviceClassifyId": -1,
            "codeOrName": "",
            "label": "",
            "value": "",
            "hasChildren": false
          },
          {
            "id": "1790951908624855041",
            "createUser": "1123598821738675201",
            "createDept": "1647791050479509506",
            "createTime": "2024-05-16 11:46:24",
            "updateUser": "1123598821738675201",
            "updateTime": "2024-05-31 18:13:02",
            "status": 10001,
            "isDeleted": 0,
            "tenantId": "000000",
            "cabinName": "II标段K0",
            "cabinCode": "II-K0",
            "assetCode": "JUJCO1",
            "assetName": "九纯建一氧化碳检测器1号",
            "importanceLevel": "10001",
            "assetClassifyCode": "",
            "assetLocation": "",
            "assetPrincipalId": "-1",
            "constructionStartDate": "",
            "constructionEndDate": "",
            "activeDate": "",
            "latitude": "-1.000",
            "longitude": "-1.000",
            "assetModel": "",
            "assetSupplier": "",
            "lastInspectTime": "",
            "nextInspectTime": "",
            "parentId": "1777583970600742913",
            "seq": null,
            "assetImages": "",
            "assetFiles": "",
            "address": "",
            "purchaseDate": "2024-05-01",
            "warrantyDate": 1800,
            "isExceedShelfLife": 0,
            "assetClassifyOneId": null,
            "assetClassifyTwoId": null,
            "assetClassifyCodeOne": "ZC001",
            "assetClassifyNameOne": "设备",
            "assetClassifyCodeTwo": "CJSB",
            "assetClassifyNameTwo": "采集设备",
            "dtlVOList": [],
            "spareVOList": [],
            "assetPrincipalName": "",
            "disabled": false,
            "importanceLevelName": "",
            "deviceClassifyId": -1,
            "codeOrName": "",
            "label": "",
            "value": "",
            "hasChildren": false
          },
          {
            "id": "1790951634325762050",
            "createUser": "1123598821738675201",
            "createDept": "1647791050479509506",
            "createTime": "2024-05-16 11:45:19",
            "updateUser": "1123598821738675201",
            "updateTime": "2024-05-16 11:45:19",
            "status": 10001,
            "isDeleted": 0,
            "tenantId": "000000",
            "cabinName": "II标段K0",
            "cabinCode": "II-K0",
            "assetCode": "JUJO21",
            "assetName": "九纯建氧气检测器1号",
            "importanceLevel": "10001",
            "assetClassifyCode": "",
            "assetLocation": "",
            "assetPrincipalId": "-1",
            "constructionStartDate": "",
            "constructionEndDate": "",
            "activeDate": "",
            "latitude": -1,
            "longitude": -1,
            "assetModel": "",
            "assetSupplier": "",
            "lastInspectTime": "",
            "nextInspectTime": "",
            "parentId": "1777583970600742913",
            "seq": null,
            "assetImages": "",
            "assetFiles": "",
            "address": "",
            "purchaseDate": "2024-05-01",
            "warrantyDate": 1800,
            "isExceedShelfLife": 0,
            "assetClassifyOneId": null,
            "assetClassifyTwoId": null,
            "assetClassifyCodeOne": "ZC001",
            "assetClassifyNameOne": "设备",
            "assetClassifyCodeTwo": "CJSB",
            "assetClassifyNameTwo": "采集设备",
            "dtlVOList": [],
            "spareVOList": [],
            "assetPrincipalName": "",
            "disabled": false,
            "importanceLevelName": "",
            "deviceClassifyId": -1,
            "codeOrName": "",
            "label": "",
            "value": "",
            "hasChildren": false
          },
          {
            "id": "1782299289024577538",
            "createUser": "1123598821738675201",
            "createDept": "1647791050479509506",
            "createTime": "2024-04-22 14:43:59",
            "updateUser": "1123598821738675201",
            "updateTime": "2024-06-26 10:07:38",
            "status": 10001,
            "isDeleted": 0,
            "tenantId": "000000",
            "cabinName": "test11",
            "cabinCode": "test11",
            "assetCode": "ZC-003",
            "assetName": "资产名称",
            "importanceLevel": "10001",
            "assetClassifyCode": "",
            "assetLocation": "",
            "assetPrincipalId": "-1",
            "constructionStartDate": "",
            "constructionEndDate": "",
            "activeDate": "",
            "latitude": "-1.000",
            "longitude": "-1.000",
            "assetModel": "",
            "assetSupplier": "",
            "lastInspectTime": "2024-06-18 17:50:25",
            "nextInspectTime": "",
            "parentId": "1776796137585762306",
            "seq": null,
            "assetImages": "",
            "assetFiles": "",
            "address": "rtsp://admin:*****HAOren778@60.174.100.175:554/cam/realmonitor?channel=1&amp;subtype=0&amp;unicast=true",
            "purchaseDate": "2024-06-26",
            "warrantyDate": 1,
            "isExceedShelfLife": 0,
            "assetClassifyOneId": null,
            "assetClassifyTwoId": null,
            "assetClassifyCodeOne": "ZC001",
            "assetClassifyNameOne": "设备",
            "assetClassifyCodeTwo": "ZCFL001",
            "assetClassifyNameTwo": "监控设备",
            "dtlVOList": [],
            "spareVOList": [],
            "assetPrincipalName": "",
            "disabled": false,
            "importanceLevelName": "",
            "deviceClassifyId": -1,
            "codeOrName": "",
            "label": "",
            "value": "",
            "hasChildren": false
          },
          {
            "id": "1780141498660360194",
            "createUser": "1123598821738675201",
            "createDept": "1647791050479509506",
            "createTime": "2024-04-16 15:49:41",
            "updateUser": "1123598821738675201",
            "updateTime": "2024-04-23 09:50:56",
            "status": 10001,
            "isDeleted": 0,
            "tenantId": "000000",
            "cabinName": "II标段K0",
            "cabinCode": "II-K0",
            "assetCode": "CJSB0002",
            "assetName": "采集设备3号",
            "importanceLevel": "10001",
            "assetClassifyCode": "",
            "assetLocation": "",
            "assetPrincipalId": "-1",
            "constructionStartDate": "",
            "constructionEndDate": "",
            "activeDate": "",
            "latitude": -1,
            "longitude": -1,
            "assetModel": "",
            "assetSupplier": "",
            "lastInspectTime": "",
            "nextInspectTime": "",
            "parentId": "1777583970600742913",
            "seq": null,
            "assetImages": "",
            "assetFiles": "",
            "address": "",
            "purchaseDate": "",
            "warrantyDate": null,
            "isExceedShelfLife": null,
            "assetClassifyOneId": null,
            "assetClassifyTwoId": null,
            "assetClassifyCodeOne": "ZC001",
            "assetClassifyNameOne": "设备",
            "assetClassifyCodeTwo": "CJSB",
            "assetClassifyNameTwo": "采集设备",
            "dtlVOList": [],
            "spareVOList": [],
            "assetPrincipalName": "",
            "disabled": false,
            "importanceLevelName": "",
            "deviceClassifyId": -1,
            "codeOrName": "",
            "label": "",
            "value": "",
            "hasChildren": false
          }
      ],
      selectionList: [],
      treeData: [
          {
            "id": "1776795749902049282",
            "createUser": "1123598821738675201",
            "createDept": "1647791050479509506",
            "createTime": "2024-04-07 10:14:53",
            "updateUser": "1123598821738675201",
            "updateTime": "2024-04-07 10:14:53",
            "status": 1,
            "isDeleted": 0,
            "tenantId": "000000",
            "assetClassifyCode": "ZC001",
            "assetClassifyName": "设备",
            "parentId": "0",
            "seq": null,
            "remark": "",
            "children": [
              {
                "id": "1800780367761063937",
                "createUser": "1123598821738675201",
                "createDept": "1647788810121388033",
                "createTime": "2024-06-12 14:41:11",
                "updateUser": "1123598821738675201",
                "updateTime": "2024-06-12 14:41:11",
                "status": 1,
                "isDeleted": 0,
                "tenantId": "000000",
                "assetClassifyCode": "GXSB",
                "assetClassifyName": "光纤设备",
                "parentId": "1776795749902049282",
                "seq": null,
                "remark": "",
                "hasChildren": false
              },
              {
                "id": "1776796137585762306",
                "createUser": "1123598821738675201",
                "createDept": "1647791050479509506",
                "createTime": "2024-04-07 10:16:25",
                "updateUser": "1123598821738675201",
                "updateTime": "2024-04-07 10:16:25",
                "status": 1,
                "isDeleted": 0,
                "tenantId": "000000",
                "assetClassifyCode": "ZCFL001",
                "assetClassifyName": "监控设备",
                "parentId": "1776795749902049282",
                "seq": 1,
                "remark": "",
                "hasChildren": false
              },
              {
                "id": "1776796137585762307",
                "createUser": "1123598821738675201",
                "createDept": "1647791050479509506",
                "createTime": "2024-04-07 10:16:25",
                "updateUser": "1123598821738675201",
                "updateTime": "2024-04-07 10:16:25",
                "status": 1,
                "isDeleted": 0,
                "tenantId": "000000",
                "assetClassifyCode": "ZCFL002",
                "assetClassifyName": "排水设备",
                "parentId": "1776795749902049282",
                "seq": 2,
                "remark": "",
                "hasChildren": false
              },
              {
                "id": "1776796137585762308",
                "createUser": "1123598821738675201",
                "createDept": "1647791050479509506",
                "createTime": "2024-04-07 10:16:25",
                "updateUser": "1123598821738675201",
                "updateTime": "2024-04-07 10:16:25",
                "status": 1,
                "isDeleted": 0,
                "tenantId": "000000",
                "assetClassifyCode": "ZCFL003",
                "assetClassifyName": "排风设备",
                "parentId": "1776795749902049282",
                "seq": 3,
                "remark": "",
                "hasChildren": false
              },
              {
                "id": "1776796137585762309",
                "createUser": "1123598821738675201",
                "createDept": "1647791050479509506",
                "createTime": "2024-04-07 10:16:25",
                "updateUser": "1123598821738675201",
                "updateTime": "2024-04-07 10:16:25",
                "status": 1,
                "isDeleted": 0,
                "tenantId": "000000",
                "assetClassifyCode": "ZCFL004",
                "assetClassifyName": "环境监测设备",
                "parentId": "1776795749902049282",
                "seq": 4,
                "remark": "",
                "hasChildren": false
              },
              {
                "id": "1776796137585762310",
                "createUser": "1123598821738675201",
                "createDept": "1647791050479509506",
                "createTime": "2024-04-07 10:16:25",
                "updateUser": "1123598821738675201",
                "updateTime": "2024-04-07 10:16:25",
                "status": 1,
                "isDeleted": 0,
                "tenantId": "000000",
                "assetClassifyCode": "ZCFL005",
                "assetClassifyName": "照明设备",
                "parentId": "1776795749902049282",
                "seq": 5,
                "remark": "",
                "hasChildren": false
              },
              {
                "id": "1777583970600742913",
                "createUser": "1123598821738675201",
                "createDept": "1647791050479509506",
                "createTime": "2024-04-09 14:26:59",
                "updateUser": "1123598821738675201",
                "updateTime": "2024-04-09 14:26:59",
                "status": 1,
                "isDeleted": 0,
                "tenantId": "000000",
                "assetClassifyCode": "CJSB",
                "assetClassifyName": "采集设备",
                "parentId": "1776795749902049282",
                "seq": 6,
                "remark": "",
                "hasChildren": false
              },
              {
                "id": "1782298184551088129",
                "createUser": "1123598821738675201",
                "createDept": "1647791050479509506",
                "createTime": "2024-04-22 14:39:35",
                "updateUser": "1123598821738675201",
                "updateTime": "2024-04-22 14:39:53",
                "status": 1,
                "isDeleted": 0,
                "tenantId": "000000",
                "assetClassifyCode": "ZC-003",
                "assetClassifyName": "设备",
                "parentId": "1776795749902049282",
                "seq": 7,
                "remark": "003",
                "hasChildren": false
              }
            ],
            "hasChildren": false
          },
          {
            "id": "1776795821364600834",
            "createUser": "1123598821738675201",
            "createDept": "1647791050479509506",
            "createTime": "2024-04-07 10:15:10",
            "updateUser": "1123598821738675201",
            "updateTime": "2024-04-07 10:15:10",
            "status": 1,
            "isDeleted": 0,
            "tenantId": "000000",
            "assetClassifyCode": "ZC002",
            "assetClassifyName": "构筑物",
            "parentId": "0",
            "seq": null,
            "remark": "",
            "children": [
              {
                "id": "1776860567568076801",
                "createUser": "1123598821738675201",
                "createDept": "1647791050479509506",
                "createTime": "2024-04-07 14:32:26",
                "updateUser": "1123598821738675201",
                "updateTime": "2024-04-07 14:32:26",
                "status": 1,
                "isDeleted": 0,
                "tenantId": "000000",
                "assetClassifyCode": "PSSB",
                "assetClassifyName": "排水系统",
                "parentId": "1776795821364600834",
                "seq": null,
                "remark": "",
                "hasChildren": false
              }
            ],
            "hasChildren": false
          },
          {
            "id": "1776795868911230978",
            "createUser": "1123598821738675201",
            "createDept": "1647791050479509506",
            "createTime": "2024-04-07 10:15:21",
            "updateUser": "1123598821738675201",
            "updateTime": "2024-04-07 10:15:21",
            "status": 1,
            "isDeleted": 0,
            "tenantId": "000000",
            "assetClassifyCode": "ZC003",
            "assetClassifyName": "虚拟资产",
            "parentId": "0",
            "seq": null,
            "remark": "",
            "hasChildren": false
          }
      ],
      defaultProps: {
        label: "assetClassifyName",
        value: "id"
      },
      page: {
        pageSize: 20,
        currentPage: 1,
        total: 10
      },
    }
  },
  mounted() {

  },
  methods: {
    // 获取左树数据
    getTreeData() {
      tree().then((res) => {
        this.treeData = res.data.data
      })
    },
    // 点击确定
    selectData() {
      if (this.selectionList === undefined || this.selectionList.length === 0) {
        this.$message.warning('请选择数据');
        return;
      }
      this.selectProductShow = false
      this.$emit('select-data', this.selectionList);
    },
    handleSelectionChange (selection) {
      this.selectionList = selection
    },
    cancel () {

    },
    openDialog () {
      this.selectProductShow = true
      // TODO 初始化数据
      // this.getTreeData()
      // this.onLoad()
    },
    // 点击左侧重新渲染右侧列表
    getNodeClick(data) {
      this.treeDeptId = data.id;
      this.page.currentPage = 1;
      this.onLoad(this.page, {parentId: this.treeDeptId});
    },
    close () {
      console.log('213')
    },
    handleSearch () {
      this.page.currentPage = 1
      this.onLoad(this.page, this.formInline)
    },
    handleEmpty () {
      this.formInline = {}
      this.onLoad(this.page)
    },
    changeSize (val) {
      this.page.pageSize = val
      this.onLoad(this.page, {})
    },
    changeCurrent (val) {
      this.page.currentPage = val
      this.onLoad(this.page, {})
    },
    // 列表获取数据
    onLoad(page, params) {
      this.page = page;
      this.tableLoading = true;
      Object.assign(params, {status: 10001})
      getPage(page.currentPage, page.pageSize, Object.assign(params, this.formInline)).then(res => {
        const data = res.data.data;
        this.page.total = data.total;
        this.dataTotal = data.total;
        this.tableData = data.records;
        this.tableLoading = false;
      });
    },
  }
}
</script>

<style scoped>
.product-dialog ::v-deep .el-dialog__body {
  padding: 0 20px !important;
}
.right-table ::v-deep .el-main {
  padding-top: 8px !important;
}
</style>
