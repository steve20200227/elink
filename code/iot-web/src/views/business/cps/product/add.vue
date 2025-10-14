<template>
  <div style="margin: 20px">
    <template>
      <div
        style="display: block; font-size: 26px; font-weight: bold; padding-bottom: 15px">
        <div v-if="this.type !== 'add'">
          <el-button icon="el-icon-back" type="primary" circle @click="headCancel" style="margin-right: 15px"/>
          <el-divider direction="vertical" style="margin-right: 15px"></el-divider>
          <svg-icon icon-class="product" style="margin-right: 15px; margin-left: 15px; transform: scale(1.3);"/>
          <span style="margin-right: 20px">{{ dataForm.productName }}</span>
        </div>
        <div v-else>
          <el-button icon="el-icon-back" type="primary" circle @click="headCancel" style="margin-right: 15px"/>
          <el-divider direction="vertical" style="margin-right: 15px"></el-divider>
          <svg-icon icon-class="product" style="margin-right: 15px; margin-left: 15px; transform: scale(1.3);"/>
          <span style="margin-right: 15px">创建产品</span>
        </div>
      </div>
      <div v-if="this.type !== 'add'"
           style="display: block; font-size: 16px; padding-bottom: 30px">
        <div>
          <span style="margin-right: 55px">编码: {{ dataForm.productCode }}</span>
          <span style="margin-right: 55px">类型: {{ this.productSortName }}</span>
          <span v-if="dataForm.isEnable == 1" style="margin-right: 25px; color: #3399FF"><svg-icon icon-class="online"
                                                                                                   style="margin-right: 5px"/>已启用</span>
          <span v-else style="margin-right: 25px; color: #f16506"><svg-icon icon-class="offline"
                                                                            style="margin-right: 5px"/>未启用</span>
        </div>
      </div>
      <div>
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
          <el-tab-pane label="基本信息" name="basic">
          </el-tab-pane>
          <el-tab-pane label="关联设备" name="relatedDevices" v-if="this.type !== 'add'" style="overflow-y: auto;height: calc(100vh - 300px);">
            <equipment ref="equipment"></equipment>
          </el-tab-pane>
          <el-tab-pane label="接入认证" name="auth" v-if="this.type !== 'add'" style="overflow-y: auto;height: calc(100vh - 300px);">
            <div v-if="dataForm.agreementType == 'MQTT'"
                 style="margin: 10px 20px; border: 1px solid #dcdfe6; background-color: #f9fafc; border-radius: 8px; padding: 20px;">
              <el-form ref="authenticationForm" :inline="true" :model="authenticationForm"
                       label-width="100px"
                       class="product-form">
                <el-row style="padding-top: 10px">
                  <el-col :span="6">
                    <el-form-item label="接入地址:">
                      <el-input v-model="authenticationForm.accessAddress" disabled size="small" style="width: 100%;"
                      ></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="账号:">
                      <el-input v-model="dataForm.userAccount" readOnly show-password size="small"
                                placeholder="请输入账号"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="密码:">
                      <el-input v-model="dataForm.userPassword" readOnly show-password size="small"
                                placeholder="请输入密码"></el-input>
                    </el-form-item>
                  </el-col>

                  <el-col :span="3">
                    <div style="text-align: center; ">
                      <el-button v-if="!readOnly && dataForm.equipmentType !== '子设备'" type="primary" @click="generateAccountPassword()">生成账号密码
                      </el-button>
                    </div>
                  </el-col>
                </el-row>
              </el-form>
              <div style="padding: 10px 0; border-bottom: 1px solid #ccc; margin-bottom: 20px"></div>
              <el-form ref="authenticationForm" :inline="true" :model="authenticationForm"
                       label-width="100px"
                       class="product-form">

                <el-row style="margin-left: 31px;">
                  <el-col :span="16">
                    <el-form-item>
                      <el-input placeholder="请输入内容" v-model="dataForm.attributePush"
                                :disabled="true">
                        <template slot="prepend">属性上报</template>
                      </el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row style="margin-left: 31px;">
                  <el-col :span="16">
                    <el-form-item>
                      <el-input placeholder="请输入内容" v-model="dataForm.eventPush"
                                :disabled="true">
                        <template slot="prepend">事件上报</template>
                      </el-input>
                    </el-form-item>
                  </el-col>

                </el-row>
                <el-row style="margin-left: 31px;">
                  <el-col :span="16">
                    <el-form-item>
                      <el-input placeholder="请输入内容" v-model="dataForm.featureIssued"
                                :disabled="true">
                        <template slot="prepend">功能下发</template>
                      </el-input>
                    </el-form-item>
                  </el-col>

                </el-row>
              </el-form>
            </div>
          </el-tab-pane>
          <el-tab-pane label="属性" name="pointStats" v-if="this.type !== 'add'">
            <div style="display: flex; justify-content: space-between; font-size: 16px; padding-bottom: 5px">
              <el-form ref="gridHeadLayout" :inline="true" :model="formPoint" class="demo-form-inline">
                <el-form-item>
                  <el-input
                    v-model="formPoint.pointName"
                    size="mini"
                    placeholder="请输入点位名称">
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-select v-model="formPoint.isEnable" size="mini" placeholder="请选择是否启用">
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
              <div>
                <el-button type="primary" v-if="!readOnly" size="mini" @click="headAdd()">新增</el-button>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="事件" name="pointIncident" v-if="this.type !== 'add'">
            <div style="display: flex; justify-content: space-between; font-size: 16px; padding-bottom: 5px">
              <el-form ref="gridHeadLayout" :inline="true" :model="formPoint" class="demo-form-inline">
                <el-form-item>
                  <el-input
                    v-model="formPoint.pointName"
                    size="mini"
                    placeholder="请输入点位名称">
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-select v-model="formPoint.isEnable" size="mini" placeholder="请选择是否启用">
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
              <div>
                <el-button type="primary" v-if="!readOnly" size="mini" @click="headAdd()">新增</el-button>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="功能" name="pointFeature" v-if="this.type !== 'add'">
            <div style="display: flex; justify-content: space-between; font-size: 16px; padding-bottom: 5px">
              <el-form ref="gridHeadLayout" :inline="true" :model="formPoint" class="demo-form-inline">
                <el-form-item>
                  <el-input
                    v-model="formPoint.pointName"
                    size="mini"
                    placeholder="请输入点位名称">
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-select v-model="formPoint.isEnable" size="mini" placeholder="请选择是否启用">
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
              <div>
                <el-button type="primary" v-if="!readOnly" size="mini" @click="headAdd()">新增</el-button>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </template>

    <div v-if="activeTab === 'basic'" style="overflow-y: auto;height: calc(100vh - 300px);">
      <div>
        <div style="margin: 10px 20px; border: 1px solid #dcdfe6; background-color: #f9fafc; border-radius: 8px; padding: 20px;">
          <el-form :rules="rules" ref="ruleForm" :inline="true" :model="dataForm" label-width="100px"
                   class="product-form">
            <el-row>
              <el-col :span="6">
                <el-form-item label="产品名称:" prop="productName">
                  <el-input v-model="dataForm.productName" :disabled="readOnly" size="small" style="width: 100%;"
                            placeholder="请输入产品名称"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="产品编码:" prop="productCode">
                  <el-input v-model="dataForm.productCode" :disabled="!(type == 'add')" size="small"
                            @change="productCodeRenewal" placeholder="请输入产品编码"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="4">
                <el-form-item label="设备类型:" prop="equipmentType">
                  <el-select v-model="dataForm.equipmentType" :disabled="!(type == 'add')"
                             size="small" placeholder="请选择设备类型" style="width: 100%;">
                    <el-option
                      v-for="item in equipmentTypeOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6" v-if="dataForm.equipmentType === '子设备'">
                <el-form-item label="关联父产品:" prop="parentProduct">
                  <el-select v-model="dataForm.parentProduct"
                             size="small"
                             placeholder="请选择设备类型"
                             style="width: 100%;"
                             @change="parentProductAlter">
                    <el-option
                      v-for="item in productOptions"
                      :key="item.productCode"
                      :label="item.productName"
                      :value="item.productCode">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="4">
                <el-form-item label="接入协议:" prop="agreementType">
                  <el-select v-model="dataForm.agreementType" :disabled="this.$route.query.type!=='add'"
                             style="width: 100%;" size="small"
                  >
                    <el-option
                      v-for="item in aTDict"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="产品图片:">
                  <el-upload
                    :action="uploadPath"
                    list-type="picture-card"
                    :on-preview="handlePictureCardPreview"
                    :on-remove="handleRemove"
                    :on-success="handleSuccess"
                    :on-exceed="handleExceed"
                    :limit="1"
                    :show-file-list="true"
                    :file-list="fileList"
                    :headers="headers"
                    :disabled="this.$route.query.type == 'view'"
                    accept=".jpeg, .jpg, .png"
                  >
                    <i class="el-icon-plus"></i>
                  </el-upload>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6" class="remark">
                <el-form-item label="备注:">
                  <el-input type="textarea" :rows="6" :disabled="readOnly" v-model="dataForm.remark" maxlength="500"
                            show-word-limit
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div style="margin: 10px 25px">
            <el-button v-if="!readOnly" type="primary" @click="headSave()">{{ this.type !== 'add' ? '更新' : '创建' }}
            </el-button>
          </div>
        </div>
        <el-dialog :visible.sync="uploadDialogVisible">
          <img width="100%" :src="dataForm.image" alt="">
        </el-dialog>
      </div>
    </div>
    <div
      v-if="activeTab === 'point' || activeTab === 'pointStats' || activeTab === 'pointFeature' || activeTab === 'pointIncident'">
      <el-row :gutter="20" style="height: calc(100vh - 418px);overflow-y: auto;padding-top: 5px;">
        <div class="point-DA point-card" v-if="dataForm.agreementType == 'MQTT'">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item, index) in tableData" :key="index" style="padding: 10px">
            <el-card shadow="always" style="font-size: 14px">
              <div class="point-head" style="position: relative;font-size: 20px; margin-bottom: 10px">
                {{ item.pointName }}
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
                <el-tooltip class="item" v-if="!readOnly" effect="dark" content="编辑" placement="top">
                  <a style="font-size: 14px; position: absolute; right: 28px" @click="rowEdit(item)">
                    <span class="el-icon-setting" style="font-size: 18px;color: #1677FF"></span>
                  </a>
                </el-tooltip>
                <el-tooltip class="item" v-if="!readOnly" effect="dark" content="删除" placement="top">
                  <a style="font-size: 14px; position: absolute; right: 0" @click="rowRemove(item)">
                    <span class="el-icon-delete" style="font-size: 18px;color: #1677FF"></span>
                  </a>
                </el-tooltip>
              </div>
              <div class="point-content" style="margin-bottom: 5px">
                <div style="margin: 16px 6px 0 6px">
                  <span style="color: #646a73">点位编码：</span>
                  <span style="color: #1677ff">{{ item.pointCode }}</span>
                </div>
                <div style="margin: 16px 6px 0 6px">
                  <span style="color: #646a73">创建时间：</span>
                  <span style="color: #1677ff">{{ parseTime(item.createTime) }}</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </div>
      </el-row>

      <el-empty
        v-if="tableData.length==0"
        style="width: 100%"
        description="暂无数据"
        :image-size="120"
      ></el-empty>
      <div class="block" style="float: right;" v-if="page.total !== 0">
        <el-pagination
          background
          :current-page="page.currentPage"
          :page-sizes="[12, 24, 36, 48, 120]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </div>
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
        <el-button type="primary" @click="customProtocolConfirm">确 定</el-button>
      </div>
    </el-dialog>
    <mqtt-dialog
      ref="mqtt"
      @restOnLoad="restOnLoad"
    >
    </mqtt-dialog>
    <eventModelDialog
      ref="eventModel"
      @restOnLoad="restOnLoad">
    </eventModelDialog>
    <functionalModelDialog
      ref="functionalModel"
      @restOnLoad="restOnLoad">
    </functionalModelDialog>
  </div>

</template>

<script>
import eventModelDialog from "@/views/business/cps/product/components/EventModelDialog.vue";
import functionalModelDialog from "@/views/business/cps/product/components/FunctionalModelDialog.vue";
import {addAuthenticationAdd, detail, getProductOptions, removeAuthentication, submit} from '@/api/cps/product-api'
import {getAnalyzeAgreement} from '@/api/cps/analyze_agreement_api.js'
import {page as getPage, remove} from '@/api/cps/product-point-api'
import MqttDialog from '@/views/business/cps/product/components/MqttDialog.vue'
import {DICT_TYPE} from '@/utils/dict'
import {getAccessToken} from '@/utils/auth'
import equipment from '@/views/business/cps/product/components/EquipmentComponents.vue'
import CustomProtocolDialog from '@/views/business/cps/product/components/CustomProtocolDialog.vue'

export default {
  name: 'add',
  computed: {
    DICT_TYPE() {
      return DICT_TYPE
    }
  },
  components: {
    CustomProtocolDialog,
    eventModelDialog,
    functionalModelDialog,
    MqttDialog,
    equipment
  },
  data() {
    return {
      iotCode: '',
      pointType: '',
      customProtocolDialog: false,
      customProtocolData: {},
      productOptions: [],
      equipmentTypeOptions: [],
      activeTab: 'basic',
      tabs: [
        {
          label: '基本信息',
          name: 'basic',
        },
        {
          label: '点位信息',
          name: 'point',
        },
        {
          label: '关联设备',
          name: 'device',
        },
      ],
      fileList: [],
      headers: {Authorization: "Bearer " + getAccessToken()}, // 设置上传的请求头部
      uploadDialogVisible: false,
      uploadPath: process.env.VUE_APP_BASE_API + '/admin-api/infra/file/upload',
      title: '产品新增',
      readOnly: false,
      type: 'view',
      productSortId: '',
      productSortName: '',
      dataForm: {},
      accessParsingProtocol: {},
      tableData: [],
      accessParsingProtocolOption: [],
      // 用于判断是否超出容器
      isTextOverflow: false,
      aTDict: [], // 接入协议
      formPoint: {}, // 点位过滤条件
      options: [],
      configPage: {
        pageSize: 10,
        currentPage: 1,
        total: 0,
      },
      rules: {
        productName: [
          {
            required: true,
            message: '请输入产品名称',
            trigger: 'blur'
          },
          { max: 50, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
        ],
        productCode: [
          {
            required: true,
            message: '请输入产品编码',
            trigger: 'blur'
          },
          2
        ],
        agreementType: [
          {
            required: true,
            message: '请选择接入协议',
            trigger: 'blur'
          }
        ],
        equipmentType: [
          {
            required: true,
            message: '请选择设备类型',
            trigger: 'blur'
          }
        ],
        parentProduct: [
          {
            required: true,
            message: '请选择父产品',
            trigger: 'blur'
          }
        ]
      },
      attributePush: '',
      featureIssued: '',
      authenticationForm: {
        accessAddress: process.env.VUE_APP_EMQX_URL,
        userPassword: "",
        userAccount: ""
      },
      dictBizMap: {},
      page: {
        pageSize: 12,
        currentPage: 1,
        total: 0
      }
    }
  },
  created() {
    this.getConfigKey("iot").then((res) => {
      this.iotCode = res.data;
    })
    this.equipmentTypeOptions = this.$store.state.dict.dictDatas.equipment_type
    let {id, type, productSortId} = this.$route.query
    this.type = type
    this.productSortId = productSortId
    if (['edit'].includes(this.type)) {
      this.dataForm.id = id
      this.title = '产品编辑'
      this.initData()
      this.onLoad(this.page)
    }
    if (['view'].includes(this.type)) {
      this.dataForm.id = id
      this.readOnly = true
      this.title = '产品查看'
      this.initData()
      this.onLoad(this.page)
    }
  },
  mounted() {
    this.getProductOptions();
    this.options = this.$store.state.dict.dictDatas.is_enable
    this.aTDict = this.$store.state.dict.dictDatas.agreement_type
    const type = this.$route.query.type;
    if (type === 'add') {
      this.$tab.updatePage(Object.assign({}, this.$route, {title: '产品新增'}));
    } else if (type === 'edit') {
      this.$tab.updatePage(Object.assign({}, this.$route, {title: '产品编辑'}));
    } else {
      this.$tab.updatePage(Object.assign({}, this.$route, {title: '产品查看'}));
    }
  },
  methods: {
    //如果是子产品，在选择父产品后，将父产品的接入认证信息复制给当前子产品，子产品不需要自己创建账号密码
    parentProductAlter() {
      let parentProduct = this.productOptions.find(e => e.productCode === this.dataForm.parentProduct);
      this.dataForm.serviceIp = parentProduct.serviceIp;
      this.dataForm.userAccount = parentProduct.userAccount;
      this.dataForm.userPassword = parentProduct.userPassword;
    },
    productCodeRenewal() {
      this.dataForm.attributePush = this.iotCode + '/' + this.dataForm.productCode + '/{设备编码}/attributePush';
      this.dataForm.eventPush = this.iotCode + '/' + this.dataForm.productCode + '/{设备编码}/eventPush';
      this.dataForm.featureIssued = this.iotCode + '/' + this.dataForm.productCode + '/{设备编码}/featureIssued';
    },
    customProtocolConfirm() {
      this.$refs.customProtocolRef.save()
      this.dataForm.parsingProtocol = this.$refs.customProtocolRef.dataForm.agreementCode
      this.dataForm.serviceIp = this.$refs.customProtocolRef.dataForm.accessAddress
    },
    customProtocolClose() {
      this.$refs.customProtocolRef.dataForm = {}
      this.customProtocolDialog = false;
      this.getAccessParsingProtocolOption();
    },
    editCustomProtocol() {
      this.customProtocolDialog = true;
      this.$nextTick(() => {
        if (this.accessParsingProtocol.agreementCode === 'CUSTOM') {
          let data = {
            agreementCode: "",
            agreementName: "",
            customAgreement: "",
            accessAddress: "1500"
          }
          this.$refs.customProtocolRef.init(data)
        } else {
          this.$refs.customProtocolRef.init(this.accessParsingProtocol)
        }
      })
    },
    selectParsingProtocol() {
      this.accessParsingProtocol = this.accessParsingProtocolOption.find(e => e.agreementCode === this.dataForm.parsingProtocol)
      this.dataForm.serviceIp = this.accessParsingProtocol.accessAddress
    },
    getAccessParsingProtocolOption() {
      getAnalyzeAgreement().then((res) => {
        this.accessParsingProtocolOption = res.data;
        let data = {
          agreementCode: "CUSTOM",
          agreementName: "自定义"
        }
        this.accessParsingProtocolOption.push(data);
        if (this.dataForm.parsingProtocol) {
          this.selectParsingProtocol()
        }
      })
    },
    getProductOptions() {
      getProductOptions().then((res) => {
        this.productOptions = res.data;
      })
    },
    /**
     * 进行生成账号密码
     */
    generateAccountPassword() {
      this.$loading();
      addAuthenticationAdd(this.dataForm).then((res) => {
        const {data, code} = res
        if (code === 0) {
          this.$message.success("操作成功");
          //刷新数据
          Object.assign(this.dataForm, data);
          this.dataForm = data;
          this.initData();
          this.type = "edit";
        } else {
          this.$message.warning(msg);
        }
      }).finally(() => {
        this.$loading().close();
      });
    },
    handleSearch() {
      this.configPage.currentPage = 1
      this.onLoad(this.configPage, this.formPoint)
    },
    handleEmpty() {
      this.formPoint = {}
      this.onLoad(this.configPage, {})
    },
    handleTabClick(tab, event) {
      this.formPoint = {};
      if (tab.name === 'relatedDevices') {
        this.equipment();
      }
      if (tab.name === 'pointStats') {
        this.pointType = 'pointStats';
        this.onLoad(this.configPage, {});
      }
      if (tab.name === 'pointIncident') {
        this.pointType = 'pointIncident';
        this.onLoad(this.configPage, {});
      }
      if (tab.name === 'pointFeature') {
        this.pointType = 'pointFeature';
        this.onLoad(this.configPage, {});
      }
    },
    equipment() {
      this.$refs.equipment.getEquipment(this.dataForm.productCode)
    },
    //文件上传个数
    handleExceed(files, fileList) {
      this.$message.warning(`只能上传一个文件`);
    },
    handleSuccess(response, file, fileList) {
      this.dataForm.image = response.data
    },
    handleRemove(file, fileList) {
      this.dataForm.image = ""
    },
    handlePictureCardPreview(file) {
      this.dataForm.image = file.url
      this.uploadDialogVisible = true;
    },
    handleSizeChange(val) {
      this.page.pageSize = val
      this.onLoad(this.page)
    },
    handleCurrentChange(val) {
      this.page.currentPage = val
      this.onLoad(this.page)
    },
    restOnLoad() {
      this.onLoad(this.page)
    },
    headAdd() {
      if (!this.dataForm.id) {
        this.$message.warning('请先保存产品信息')
        return
      }
      if (this.dataForm.agreementType == 'MQTT') {
        if (this.pointType === 'pointStats') {
          this.$refs.mqtt.addPoint(this.dataForm.id, this.pointType, this.dataForm.agreementType)
        } else if (this.pointType === 'pointIncident') {
          this.$refs.eventModel.addPoint(this.dataForm.id, this.pointType, this.dataForm.agreementType);
        } else {
          this.$refs.functionalModel.addPoint(this.dataForm.id, this.pointType, this.dataForm.agreementType);
        }
      }
    },
    headCancel() {
      this.$router.back()
    },
    initData() {
      detail(this.dataForm.id).then((res) => {
        this.dataForm = res.data
        this.getAccessParsingProtocolOption()
        if (this.dataForm.attributePush === '' || this.dataForm.attributePush === undefined || this.dataForm.attributePush === null) {
          this.dataForm.attributePush = this.iotCode + '/' + this.dataForm.productCode + '/{设备编码}/attributePush';
        }
        if (this.dataForm.eventPush === '' || this.dataForm.eventPush === undefined || this.dataForm.eventPush === null) {
          this.dataForm.eventPush = this.iotCode + '/' + this.dataForm.productCode + '/{设备编码}/eventPush';
        }
        if (this.dataForm.featureIssued === '' || this.dataForm.featureIssued === undefined || this.dataForm.featureIssued === null) {
          this.dataForm.featureIssued = this.iotCode + '/' + this.dataForm.productCode + '/{设备编码}/featureIssued';
        }
        this.productSortName = res.data.sortName
        if (res.data.image !== "" && res.data.image !== null) {
          let file = {url: res.data.image}
          this.fileList.push(file)
        }
      })
    },
    headSave(cancel = false) {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          const productCode = this.dataForm.productCode;
          const regex = /^[a-zA-Z0-9][a-zA-Z0-9_]*$/;
          if (!regex.test(productCode)) {
            this.$message.warning("产品编码只能以字母开头，只能包含字母、数字和下划线!");
            return;
          }
          this.$loading()
          this.dataForm.productSortId = this.productSortId
          if (this.dataForm.agreementType === 'MQTT') {
            if (this.dataForm.attributePush === '' || this.dataForm.attributePush === undefined || this.dataForm.attributePush === null) {
              this.dataForm.attributePush = this.iotCode + '/' + this.dataForm.productCode + '/{设备编码}/attributePush';
            }
            if (this.dataForm.eventPush === '' || this.dataForm.eventPush === undefined || this.dataForm.eventPush === null) {
              this.dataForm.eventPush = this.iotCode + '/' + this.dataForm.productCode + '/{设备编码}/eventPush';
            }
            if (this.dataForm.featureIssued === '' || this.dataForm.featureIssued === undefined || this.dataForm.featureIssued === null) {
              this.dataForm.featureIssued = this.iotCode + '/' + this.dataForm.productCode + '/{设备编码}/featureIssued';
            }
          }
          submit(this.dataForm)
            .then((res) => {
              if (res.code === 0) {
                this.dataForm = res.data;
                this.$message.success('操作成功')
              } else {
                this.$message.success(msg)
              }
              if (cancel) {
                this.$router.back()
              } else {
                //刷新数据
                this.initData()
                this.type = 'edit'
              }
            })
            .finally(() => {
              this.$loading().close()
            })
        }
      })
    },
    onLoad(page, params = {}) {
      this.page = page
      this.tableLoading = true;
      if (this.dataForm.id) {
        params.productId = this.dataForm.id
      }
      params.pointType = this.pointType
      getPage(page.currentPage, page.pageSize, params).then((res) => {
        const data = res.data
        this.page.total = data.total
        this.tableData = data.records
        this.tableLoading = false;
      })
    },
    rowEdit(row, type) {
      row.type = type;
      row.pointType = this.pointType
      if (this.dataForm.agreementType == 'MQTT') {
        if (this.pointType === 'pointStats') {
          this.$refs.mqtt.editPoint(row)
        } else if (this.pointType === 'pointIncident') {
          this.$refs.eventModel.editPoint(row);
        } else {
          this.$refs.functionalModel.editPoint(row);
        }
      }
    },
    rowRemove(row) {
      this.$confirm('确定将选择数据删除', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          return remove(row.id)
        })
        .then(() => {
          this.onLoad(this.page)
          this.$message({
            type: 'success',
            message: '操作成功'
          })
        })
    }
  }
}
</script>
<style scoped lang="scss">
.point-card ::v-deep .el-card__body {
  padding: 15px 20px 15px 20px !important;
}

.point-card ::v-deep .el-divider--horizontal {
  margin: 0;
}

.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
}

.product-form ::v-deep .el-textarea__inner {
  resize: none;
  min-height: 111.6px !important;
}

.product-form ::v-deep .el-form-item {
  width: 100%;
}

.product-form ::v-deep .el-form-item__content {
  width: calc(100% - 100px);
}

.product-form ::v-deep .el-form-item__label {
  color: #3f4448;
  font-weight: 400;
}

.cardBigBox {
  width: calc(100% - 24px);
  padding: 12px;
  display: flex;
  flex-wrap: wrap;
  overflow: auto;
  background-color: #ffffff;
  align-content: flex-start;

  .cardbox {
    width: 24%;
    box-shadow: 0 0 5px #dde9ef;
    border: 1px solid #e7eff3;
    border-radius: 10px;
    margin: 16px 6px 0 6px;
    cursor: pointer;

    .cardTop {
      padding: 16px;

      .topContent {
        .instruction {
          white-space: nowrap; /* 防止文本换行 */
          overflow: hidden; /* 隐藏超出容器的文本 */
          text-overflow: ellipsis; /* 用省略号表示被截断的文本 */
          cursor: pointer; /* 当鼠标悬停时显示手形光标，提示用户有tooltip */
          font-size: 20px;
          color: #333;
          text-align: center;
        }

        .topItem {
          font-size: 15px;
          color: #666;
          line-height: 30px;
        }
      }
    }

    .cardBottom {
      font-size: 14px;
      line-height: 25px;
      display: flex;
      border-top: 1px solid #dcdfe6;
      justify-content: space-between;

      .statusContent {
        display: flex;
        align-items: center;

        .iconCard {
          width: 8px;
          height: 8px;
          border-radius: 50%;
          background-color: red;
          margin: 10px 6px;
        }
      }
    }
  }
}

.block {
  //position: absolute;
  bottom: 0;
  width: calc(100% - 48px);
  padding: 12px 24px;
  background-color: #ffffff;
  display: flex;
  flex-direction: row-reverse;
}
</style>
