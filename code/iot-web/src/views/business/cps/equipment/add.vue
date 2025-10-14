<template>
  <div style="margin: 20px">
    <div
      style="display: block; font-size: 26px; font-weight: bold; padding-bottom: 15px">
      <div v-if="this.type !== 'add'">
        <el-button icon="el-icon-back" type="primary" circle @click="headCancel" style="margin-right: 15px"/>
        <el-divider direction="vertical" style="margin-right: 15px"></el-divider>
        <svg-icon icon-class="deviceMini" style="margin-right: 15px; margin-left: 15px"/>
        <span style="margin-right: 20px">{{ dataForm.equipmentName }}</span>
        <el-tooltip placement="top" effect="dark">
          <template #content>
            <img :src="'data:image/png;base64,' +dataForm.qrCode"/>
          </template>
          <a style="font-size: 14px; right: 0" @click="headCancel">
            <svg-icon icon-class="barcode" style="margin-right: 5px"/>
          </a>
        </el-tooltip>
      </div>
      <div v-else>
        <el-button icon="el-icon-back" type="primary" circle @click="headCancel" style="margin-right: 15px"/>
        <el-divider direction="vertical" style="margin-right: 15px"></el-divider>
        <svg-icon icon-class="deviceMini" style="margin-right: 15px; margin-left: 15px"/>
        <span style="margin-right: 15px">创建设备</span>
      </div>
    </div>
    <div v-if="this.type !== 'add'"
         style="display: block; font-size: 16px; padding-bottom: 30px">
      <div>
        <span style="margin-right: 55px">编码: {{ dataForm.equipmentCode }}</span>
        <span style="margin-right: 55px">类型: {{ dataForm.equipmentType }}</span>
        <span v-if="dataForm.status == 1" style="margin-right: 25px; color: #3399FF"><svg-icon icon-class="online"
                                                                                                 style="margin-right: 5px"/>在线</span>
        <span v-else style="margin-right: 25px; color: #f16506"><svg-icon icon-class="offline"
                                                                          style="margin-right: 5px"/>离线</span>
      </div>
    </div>

    <el-tabs v-model="activeName" @tab-click="handleClickTab">
      <el-tab-pane label="基础信息" name="base" style="overflow-y: auto;height: calc(100vh - 300px);">
        <div style="margin: 10px 20px; border: 1px solid #dcdfe6; background-color: #f9fafc; border-radius: 8px; padding: 20px;">
          <el-form :rules="rules" ref="ruleForm" :inline="true" :model="dataForm" label-width="100px"
                   class="product-form">
            <el-row>
              <el-col :span="12">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="设备名称:" prop="equipmentName">
                      <el-input v-model="dataForm.equipmentName" :disabled="readOnly" size="small" style="width: 100%;"
                                placeholder="请输入设备名称"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="所在位置:" prop="location">
                      <el-input v-model="dataForm.location" readonly size="small" style="width: 100%;"
                                placeholder="请使用地图定位"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="设备编码:" prop="equipmentCode">
                      <el-input v-model="dataForm.equipmentCode" :disabled="!(type == 'add')" size="small"
                                @change="equipmentCodeRenewal" placeholder="请输入设备编码"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="经度位置:" prop="lat">
                      <el-input v-model="dataForm.lat" readonly size="small"
                                placeholder="请使用地图定位"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="关联产品:" prop="productName">
                      <el-input
                        v-model="dataForm.productName"
                        size="small"
                        :disabled="readOnly"
                        :readonly="true"
                        placeholder="请选择产品">
                        <el-button slot="append" icon="el-icon-search" @click.native="headSelectProduct"></el-button>
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="纬度位置:" prop="lng">
                      <el-input v-model="dataForm.lng" readonly size="small"
                                placeholder="请使用地图定位"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="接入协议:" prop="agreementType">
                      <el-input v-model="dataForm.agreementType" :readonly="true"
                                :disabled="this.$route.query.type!=='add'"
                                size="small" placeholder="产品选择后自动带出"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="网络位置:" prop="netLocation">
                      <el-input v-model="dataForm.netLocation" :readonly="true" :disabled="readOnly"
                                size="small" placeholder="设备上线后自动获取"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="设备类型:">
                      <el-select v-model="dataForm.equipmentType" :disabled="true"
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
                  <el-col :span="12" v-if="dataForm.equipmentType === '子设备'">
                    <el-form-item label="关联父设备:" prop="parentEquipment">
                      <el-select v-model="dataForm.parentEquipment"
                                 size="small"
                                 placeholder="请选择设备类型"
                                 style="width: 100%;"
                                 @change="parentEquipmentAlter">
                        <el-option
                          v-for="item in equipmentOptions"
                          :key="item.equipmentCode"
                          :label="item.equipmentName"
                          :value="item.equipmentCode">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="是否启用:" prop="isEnable">
                      <el-switch
                        v-model="dataForm.isEnable"
                        active-text="已启用"
                        :disabled="true"
                        size="small"
                        :active-value="1"
                        :inactive-value="0"
                        inactive-text="未启用">
                      </el-switch>
                    </el-form-item>
                  </el-col>

                  <el-col :span="12">
                    <el-form-item label="持久化数据:" prop="isHistory">
                      <el-switch
                        v-model="dataForm.isHistory"
                        active-text="已启用"

                        size="small"
                        :active-value="'1'"
                        :inactive-value="'0'"
                        inactive-text="未启用">
                      </el-switch>
                    </el-form-item>
                  </el-col>


                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="设备图片:">
                      <el-upload
                        :action="uploadPath"
                        list-type="picture-card"
                        :on-remove="handleRemove"
                        :on-preview="handlePictureCardPreview"
                        :on-success="handleSuccess"
                        :on-exceed="handleExceed"
                        :limit="1"
                        :show-file-list="true"
                        :file-list="fileList"
                        :headers="headers"
                        :disabled="type == 'view'"
                        accept=".jpeg, .jpg, .png">
                        <i class="el-icon-plus"></i>
                      </el-upload>
                    </el-form-item>

                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12" class="remark">
                    <el-form-item label="备注:">
                      <el-input type="textarea" v-model="dataForm.remark" :disabled="readOnly" maxlength="500"
                                placeholder="请输入备注" show-word-limit></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-col>
              <el-col :span="12" style="padding-left: 30px">
                <el-input class="addressInput" v-model="addressKeyword"
                          placeholder="请输入地址来查找相关位置"></el-input>
                <baidu-map ak="ocjtpCpd3HusX9VUmkfc49P9jxRV5Vch" :center="center" :zoom="zoom" @ready="handler"
                           @click="getClickInfo" :scroll-wheel-zoom='true' v-loading='loadingMap'>
                  <bm-view style="width: 100%; height:550px; flex: 1"></bm-view>
                  <bm-local-search :keyword="addressKeyword" :auto-viewport="true"
                                   style="display: none"></bm-local-search>
                  <bm-geolocation @locationSuccess="locationSuccess" @locationError="locationError"
                                  anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :showAddressBar="true"
                                  :autoLocation="true"></bm-geolocation>
                </baidu-map>
              </el-col>
            </el-row>
          </el-form>
          <div style="margin: 10px 25px">
            <el-button v-if="!readOnly" type="primary" @click="headSave()">{{ this.type !== 'add' ? '更新' : '创建' }}
            </el-button>
          </div>
        </div>
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
                <el-form-item label="客户端ID:">
                  <el-input v-model="dataForm.equipmentCode" disabled size="small" style="width: 100%;"
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
            </el-row>
          </el-form>
          <div style="text-align: center; margin: 10px 25px;">
            <el-button v-if="!readOnly && dataForm.equipmentType !== '子设备'" type="primary" @click="generateAccountPassword()">生成账号密码
            </el-button>
          </div>
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
              <el-col :span="2">
                <el-button v-if="!readOnly" type="primary" style="width: 80%; margin-left: -50px"
                           @click="convert(dataForm.reportId,'attribute')">转换
                </el-button>
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
              <el-col :span="2">
                <el-button v-if="!readOnly" type="primary" style="width: 80%; margin-left: -50px"
                           @click="convert(dataForm.eventId,'event')">转换
                </el-button>
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
              <el-col :span="2">
                <el-button v-if="!readOnly" type="primary" style="width: 80%; margin-left: -50px"
                           @click="convert(dataForm.deliverAnId,'feature')">转换
                </el-button>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <el-dialog
          v-dialog-drag
          :title="'规则创建'"
          :modal-append-to-body="false"
          :close-on-click-modal="false"
          :visible.sync="convertDialog"
          v-if="convertDialog"
          width="60%"
          @close="convertDialog = false"
          class="custom-dialog"
        >
          <ConvertDialog ref="convertDialogRef" :rowData="convertData"
                         @convertDialogClose="convertDialogClose"></ConvertDialog>
          <div slot="footer" class="dialog-footer">
            <el-button @click="convertDialog = false">取 消</el-button>
            <el-button type="primary" @click="convertConfirm">确 定</el-button>
          </div>
        </el-dialog>
      </el-tab-pane>
      <el-tab-pane label="属性" name="pointStats" v-if="this.type !== 'add'" style="overflow-y: auto;height: calc(100vh - 300px);">
        <div
          style="display: flex; justify-content: space-between; font-size: 16px; padding-bottom: 5px">
          <el-form ref="gridHeadLayout" :inline="true" :model="formPoint" class="demo-form-inline">
            <el-form-item>
              <el-input
                v-model="formPoint.pointName"
                size="mini"
                placeholder="请输入属性名称">
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
              <el-button size="mini" icon="el-icon-search" @click="pointSearch"></el-button>
              <el-button size="mini" icon="el-icon-refresh-right" @click="pointEmpty"></el-button>
            </el-form-item>
          </el-form>
          <div>
            <el-button type="primary" v-if="!readOnly" size="mini" @click="headAdd()">新增</el-button>
          </div>
        </div>
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
                    <span style="color: #646a73">来源：</span>
                    <span style="color: #1677ff">{{ item.source == "product" ? "产品属性" : "自定义属性" }}</span>
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

        <div class="block" style="float: right;" v-if="page.total !== 0">
          <el-pagination
            background
            :current-page="page.currentPage"
            :page-sizes="[12, 24, 36, 48, 120]"
            :page-size="page.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="page.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange">
          </el-pagination>
        </div>
      </el-tab-pane>
      <el-tab-pane label="事件" name="pointIncident" v-if="this.type !== 'add'" style="overflow-y: auto;height: calc(100vh - 300px);">
        <div
          style="display: flex; justify-content: space-between; font-size: 16px; padding-bottom: 5px">
          <el-form ref="gridHeadLayout" :inline="true" :model="formPoint" class="demo-form-inline">
            <el-form-item>
              <el-input
                v-model="formPoint.pointName"
                size="mini"
                placeholder="请输入事件名称">
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
              <el-button size="mini" icon="el-icon-search" @click="pointSearch"></el-button>
              <el-button size="mini" icon="el-icon-refresh-right" @click="pointEmpty"></el-button>
            </el-form-item>
          </el-form>
          <div>
            <el-button type="primary" v-if="!readOnly" size="mini" @click="headAdd()">新增</el-button>
          </div>
        </div>
        <el-row :gutter="20" style="height: calc(100vh - 418px);overflow-y: auto;padding-top: 5px">
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
                  <el-tooltip class="item" effect="dark" content="事件记录" placement="top">
                    <a style="font-size: 14px; position: absolute; right: 56px" @click="openHistory(item)">
                      <span class="el-icon-tickets" style="font-size: 18px;color: #1677FF"></span>
                    </a>
                  </el-tooltip>
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
                    <span style="color: #646a73">来源：</span>
                    <span style="color: #1677ff">{{ item.source == "product" ? "产品事件" : "自定义事件" }}</span>
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

        <div class="block" style="float: right;" v-if="page.total !== 0">
          <el-pagination
            background
            :current-page="page.currentPage"
            :page-sizes="[12, 24, 36, 48, 120]"
            :page-size="page.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="page.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange">
          </el-pagination>
        </div>
      </el-tab-pane>
      <el-tab-pane label="功能" name="pointFeature" v-if="this.type !== 'add'" style="overflow-y: auto;height: calc(100vh - 300px);">
        <div
          style="display: flex; justify-content: space-between; font-size: 16px; padding-bottom: 5px">
          <el-form ref="gridHeadLayout" :inline="true" :model="formPoint" class="demo-form-inline">
            <el-form-item>
              <el-input
                v-model="formPoint.pointName"
                size="mini"
                placeholder="请输入功能名称">
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
              <el-button size="mini" icon="el-icon-search" @click="pointSearch"></el-button>
              <el-button size="mini" icon="el-icon-refresh-right" @click="pointEmpty"></el-button>
            </el-form-item>
          </el-form>
          <div>
            <el-button type="primary" v-if="!readOnly" size="mini" @click="headAdd()">新增</el-button>
          </div>
        </div>
        <el-row style="height: calc(100vh - 418px);overflow-y: auto;padding-top: 5px;" :gutter="20">
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
                    <span style="color: #646a73">来源：</span>
                    <span style="color: #1677ff">{{ item.source == "product" ? "产品功能" : "自定义功能" }}</span>
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

        <div class="block" style="float: right;" v-if="page.total !== 0">
          <el-pagination
            background
            :current-page="page.currentPage"
            :page-sizes="[12, 24, 36, 48, 120]"
            :page-size="page.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="page.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange">
          </el-pagination>
        </div>
      </el-tab-pane>
      <el-tab-pane label="实时监控" name="monitor" v-if="this.type !== 'add'" style="overflow-y: auto;height: calc(100vh - 300px);">
        <monitoringView ref="monitoring" :equipmentId="dataForm.id"></monitoringView>
      </el-tab-pane>
      <el-tab-pane label="关联场景" name="config" v-if="this.type !== 'add'" style="overflow-y: auto;height: calc(100vh - 300px);">
        <div style="margin-top: 5px">
          <el-form ref="gridHeadLayout" :inline="true" :model="formInline" class="demo-form-inline">
            <el-form-item>
              <el-input
                v-model="formInline.warningName"
                size="mini"
                placeholder="请输入关联场景名称">
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
        <div style="max-height: calc(100vh - 245px);height: calc(100vh - 245px); overflow: auto;">
          <el-row v-if="configTable.length">
            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item, index) in configTable" :key="index">
              <el-card shadow="always"
                       :class="{'record-success': item.isEnable == 1, 'record-danger': item.isEnable == 0}"
                       style="margin: 5px; padding-bottom: 10px;">
                <div style="position: relative; border-bottom: 1px solid #ccc; padding-bottom: 20px;height: 110px;">
                <span style="position:absolute; top: 15px; left: 10px;">
                  <!-- 根据 场景类型 的值显示不同的图片 -->
                  <img style="height: 70px; width: 70px" v-if="item.warningType == '1'" src="@/assets/whereType.png"/>
                  <img style="height: 70px; width: 70px" v-else-if="item.warningType == '2'"
                       src="@/assets/taskType.png"/>
                  <img style="height: 70px; width: 70px" v-else src="@/assets/shouType.png"/>
                  <!-- 添加更多条件根据需要显示不同的图片 -->
                </span>
                  <el-tooltip v-if="item.isEnable == 0" class="item" effect="dark" content="编辑" placement="top">
                    <a style="font-size: 14px; position: absolute; right: 0" @click="warningConfigRestartEdit(item)">
                      <span class="el-icon-setting" style="font-size: 18px;color: #1677FF"></span>
                    </a>
                  </el-tooltip>
                  <span style="position: absolute; top: 10px; left: 110px; margin-left: 10px;font-size: 24px;width: 220px;overflow: hidden;text-overflow: ellipsis;
    white-space: nowrap;">
                  <span style="color: #646a73">{{ item.warningName }}</span>
                </span>
                  <span style="position: absolute; top: 54px; left: 110px; margin-left: 10px;font-size: 14px">
                  <span style="color: #646a73">类型：</span>
                  <span style="color: #1677ff">{{ warningTypeMap.get(item.warningType) }}</span>
                </span>
                  <span style="position: absolute; top: 77px; left: 110px; margin-left: 10px;font-size: 14px">
                  <span style="color: #646a73">优先级：</span>
                  <span style="color: #1677ff">{{ priorityMap.get(item.priority) }}</span>
                </span>
                </div>
                <div style="margin-top: 10px;">
                  <div style="float: left">
                    <el-button size="mini" type="primary" style="margin: 5px"
                               @click="warningConfigUntie(item)">解绑
                    </el-button>
                  </div>
                  <div style="float: right">
                    <el-button type="text" @click="warningConfigView(item)" size="small">更多信息</el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

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
            @current-change="warningConfigPageChange"
            @size-change="warningConfigSizeChange"
          >
          </el-pagination>
        </div>
      </el-tab-pane>
      <el-tab-pane label="告警记录" name="warningRecord" v-if="this.type !== 'add'" style="overflow-y: auto;height: calc(100vh - 300px);">
        <div style="margin-top: 5px">
          <el-form ref="gridHeadLayout" :inline="true" :model="warningRecordFormInline" class="demo-form-inline">
            <el-form-item>
              <el-input
                v-model="warningRecordFormInline.configName"
                size="mini"
                placeholder="请输入场景名称">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="warningRecordFormInline.status" size="mini" style="width: 100%" placeholder="请选择记录状态">
                <el-option
                  v-for="item in recordDict"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value * 1">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                size="mini"
                v-model="warningRecordFormInline.createTime"
                type="daterange"
                range-separator="至"
                value-format="yyyy-MM-dd"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item class="top-button">
              <el-button size="mini" @click="handleSearchWarningRecord" icon="el-icon-search"></el-button>
              <el-button size="mini" @click="handleEmptyWarningRecord" icon="el-icon-refresh-right"></el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="warningRecordData.length!=0">
          <el-table v-loading="loading" :data="warningRecordData" height="calc(100vh - 410px)" style="height: calc(100vh - 410px);">
            <el-table-column label="告警名称" align="center" prop="configName"/>
            <el-table-column label="告警时间" align="center" prop="warningTime" width="180">
              <template v-slot="scope">
                <span>{{ parseTime(scope.row.warningTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="状态" align="center" prop="status">
              <template v-slot="scope">
                <el-tag :type="scope.row.status == 0 ? 'danger' : 'success'">
                  <dict-tag :type="DICT_TYPE.RECORD_STATUS" :value="scope.row.status" />
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="处理时间" align="center" prop="handleTime" width="180">
              <template v-slot="scope">
                <span>{{ parseTime(scope.row.handleTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template v-slot="scope">
                <el-button size="small" type="primary" v-if="scope.row.status == 0" style="margin: 5px" plain
                           @click="handle(scope.row)">处 理
                </el-button>
                <el-button size="small" type="primary" v-if="scope.row.status == 1" style="margin: 5px" plain
                           @click="handleLog(scope.row)">处 理 日 志
                </el-button>
                <el-button size="small" type="primary" style="margin: 5px" plain
                           @click="warningLog(scope.row)">场 景 日 志
                </el-button>
                <el-button size="small" type="primary" style="margin: 5px" plain
                           @click="actionLog(scope.row)">动 作 日 志
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <el-empty
          v-if="warningRecordData.length==0"
          style="width: 100%"
          description="暂无数据"
          :image-size="120"
        ></el-empty>
        <div class="block" style="float: right;" v-if="pageWarningRecord.total !== 0">
          <el-pagination
            background
            :current-page="pageWarningRecord.currentPage"
            :page-sizes="[10, 20, 30, 50, 100]"
            :page-size="pageWarningRecord.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pageWarningRecord.total"
            @current-change="changeCurrentWarningRecord"
            @size-change="changeSizeWarningRecord"
          >
          </el-pagination>
        </div>
      </el-tab-pane>
      <el-tab-pane label="历史数据" name="history" v-if="this.type !== 'add'" style="overflow-y: auto;height: calc(100vh - 300px);">
        <HistoryData v-if="activeName === 'history'" ref="historyData"></HistoryData>
      </el-tab-pane>
      <el-tab-pane label="定时任务" name="task" v-if="this.type !== 'add'" style="overflow-y: auto;height: calc(100vh - 300px);">
        <div style="display: flex; justify-content: space-between; font-size: 14px; padding-bottom: 5px">
          <el-form ref="gridHeadLayout" :inline="true" :model="jobQueryParams" class="demo-form-inline">
            <el-form-item>
              <el-input
                v-model="jobQueryParams.jobDesc"
                size="mini"
                placeholder="请输入任务名称">
              </el-input>
            </el-form-item>
            <el-form-item class="top-button">
              <el-button size="mini" icon="el-icon-search" @click="handleJobSearch"></el-button>
              <el-button size="mini" icon="el-icon-refresh-right" @click="handleJobEmpty"></el-button>
            </el-form-item>
          </el-form>
          <div>
            <el-button type="primary" size="mini" @click="handleAdd" >新增</el-button>
          </div>
        </div>

        <div>
          <el-table v-loading="loading" :data="jobList" style="height: calc(100vh - 410px);">
            <el-table-column label="任务名称" align="center" prop="jobDesc"/>
            <el-table-column label="任务状态" align="center" prop="triggerStatus">
              <template v-slot="scope">
                <dict-tag :type="DICT_TYPE.INFRA_JOB_STATUS_CN" :value="scope.row.triggerStatus"/>
              </template>
            </el-table-column>
            >
            <el-table-column label="执行参数" align="center" prop="executorParam"/>
            <el-table-column label="CRON 表达式" align="center" prop="scheduleConf"/>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template v-slot="scope">
                <el-button size="mini" type="text" icon="el-icon-check" @click="handleExecute(scope.row,'stop')"
                           v-if="scope.row.triggerStatus === 1" >暂停
                </el-button>
                <el-button size="mini" type="text" icon="el-icon-check" @click="handleExecute(scope.row,'start')"
                           v-if="scope.row.triggerStatus === 0" >开启
                </el-button>
                <el-button size="mini" type="text" icon="el-icon-setting" @click="handleUpdate(scope.row)"
                >修改
                </el-button>
                <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                >删除
                </el-button>
                <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)"
                >
                  <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="handleRun" icon="el-icon-caret-right"
                    >执行一次
                    </el-dropdown-item>

                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            background
            :current-page="configPage.currentPage"
            :page-sizes="[10, 20, 30, 50,100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="configPage.total"
            :page-size="configPage.pageSize"
            :pager-count="5"
            @size-change="changeSize"
            @current-change="changeCurrent"
            style="float: right; margin: 20px 0;">
          </el-pagination>

          <!-- 添加或修改定时任务对话框 -->
          <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="jobRules" label-width="120px">
              <el-form-item label="任务名称" prop="jobName">
                <el-input v-model="form.jobDesc" placeholder="请输入任务名称"/>
              </el-form-item>
              <el-form-item label="功能模型" prop="jobParam">
                <el-select v-model="form.jobParam" style="width: 100%;" placeholder="请选择功能模型">
                  <el-option
                    v-for="item in functionalModelList"
                    :key="item.pointCode"
                    :label="item.pointName"
                    :value="item.pointCode">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="CRON 表达式" prop="cron">
                <el-input v-model="form.cron" :disabled="true" placeholder="请输入CRON 表达式">
                  <template slot="append">
                    <el-button type="primary" @click="handleShowCron">
                      生成表达式
                      <i class="el-icon-time el-icon--right"></i>
                    </el-button>
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item label="重试次数" prop="executorFailRetryCount">
                <el-input-number v-model="form.executorFailRetryCount" :min="0" :precision="0" placeholder="请输入重试次数"/>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="submitForm">确 定</el-button>
              <el-button @click="cancel">取 消</el-button>
            </div>
          </el-dialog>

          <el-dialog title="Cron表达式生成器" :visible.sync="openCron" append-to-body class="scrollbar"
                     destroy-on-close>
            <crontab @hide="openCron=false" @fill="crontabFill"></crontab>
          </el-dialog>
        </div>
      </el-tab-pane>

    </el-tabs>
    <el-dialog
      v-dialog-drag
      title="事件历史记录"
      :modal-append-to-body="false"
      :close-on-click-modal="false"
      :visible.sync="eventRecordDialog"
      v-show="eventRecordDialog"
      width="40%"
      class="avue-dialog avue-dialog--top"
    >
      <el-collapse accordion v-if="eventRecordData.length > 0">
        <el-collapse-item v-for="(item,index) in eventRecordData" :key="index" :title="convertDate(item.createTime,'yyyy-MM-dd HH:mm:ss')" :name="index">
          <div style="margin: 10px 0;">

            <pre><code>{{JSON.parse(item.payload)}}</code></pre>
          </div>

        </el-collapse-item>
      </el-collapse>

    </el-dialog>

    <mqtt-dialog
      ref="mqtt"
      @restOnLoad="restOnLoad">
    </mqtt-dialog>
    <eventModelDialog
      ref="eventModel"
      @restOnLoad="restOnLoad">
    </eventModelDialog>
    <functionalModelDialog
      ref="functionalModel"
      @restOnLoad="restOnLoad">
    </functionalModelDialog>
    <!--  选择产品  -->
    <SelectProductDialog
      ref="productDialog"
      @select-product="selectProduct"
    ></SelectProductDialog>


    <RecordUpdateStatusDialog
      ref="recordStatus"
      @restOnLoad="onLoadWarningRecord(pageWarningRecord)">
    </RecordUpdateStatusDialog>

    <RecordLogDialog
      ref="recordLog">
    </RecordLogDialog>
    <WarningLogDialog
      ref="warningLog">
    </WarningLogDialog>
    <actionLogDialog
      ref="actionLog">
    </actionLogDialog>
  </div>
</template>

<script>
import {page as getWarningRecordPage} from "@/api/warning/warning-record";
import {submit, detail, addAuthenticationAdd, getEquipmentOptions} from "@/api/cps/equipment-api";
import {page as getPage, remove, coveragePoint, getByEquipmentId} from "@/api/cps/equipment-point-api"
import {page as getConifgPage, untie} from "@/api/warning/warning-api"
import SelectProductDialog from "@/views/business/cps/equipment/components/SelectProductDialog.vue";
import eventModelDialog from "@/views/business/cps/equipment/components/EventModelDialog.vue";
import functionalModelDialog from "@/views/business/cps/equipment/components/FunctionalModelDialog.vue";
import {DICT_TYPE} from "@/utils/dict";
import MqttDialog from '@/views/business/cps/equipment/components/MqttDialog.vue'
import monitoringView from '@/views/business/cps/equipment/components/MonitoringView.vue'
import {getAccessToken} from "@/utils/auth";
import Icons from "@/views/components/icons/index.vue";
import HistoryData from '@/views/business/cps/equipment/components/HistoryData.vue'; //历史数据

import BaiduMap from "vue-baidu-map/components/map/Map.vue";
import BmView from "vue-baidu-map/components/map/MapView"; //地图视图
import BmLocalSearch from "vue-baidu-map/components/search/LocalSearch"; //搜索
import BmMarker from "vue-baidu-map/components/overlays/Marker"; //点标注
import BmInfoWindow from "vue-baidu-map/components/overlays/InfoWindow"; //标注弹窗
import BmGeolocation from "vue-baidu-map/components/controls/Geolocation";
import ConvertDialog from "@/views/business/cps/equipment/components/ConvertDialog.vue";
import {addJob, delJob, handlerStart, handlerStop, listJob, runJob, saveOrUpdate, updateJob} from "@/api/infra/job";
import {list} from "@/api/cps/event-record-api";
import Crontab from '@/components/Crontab'
import {formatDate} from "@/utils/dateUtils"
import {InfraJobStatusEnum as InfJobStatusEnum} from "@/utils/constants";
import RecordUpdateStatusDialog from "@/views/business/warning/record/components/recordUpdateStatusDialog.vue";
import WarningLogDialog from "@/views/business/warning/record/components/warningLogDialog.vue";
import RecordLogDialog from "@/views/business/warning/record/components/recordLogDialog.vue";
import actionLogDialog from "@/views/business/warning/record/components/actionLogDialog.vue";

export default {
  name: 'equipment',
  computed: {
    InfJobStatusEnum() {
      return InfJobStatusEnum
    },
    DICT_TYPE() {
      return DICT_TYPE
    }
  },
  components: {
    actionLogDialog,
    RecordLogDialog,
    WarningLogDialog,
    RecordUpdateStatusDialog,
    ConvertDialog,
    eventModelDialog,
    functionalModelDialog,
    HistoryData,
    Icons,
    SelectProductDialog,
    MqttDialog,
    BaiduMap,
    BmView,
    BmLocalSearch,
    BmMarker,
    BmInfoWindow,
    BmGeolocation,
    monitoringView,
    Crontab
  },
  data() {
    return {
      WarningRecordTableLoading: false,
      eventRecordData: [],
      recordDict: [],
      warningRecordData: [], // 预警记录数据
      eventRecordDialog: false,
      iotCode: '',
      customProtocolData: {},
      isParsingProtocolDisabled: false,
      pointType: '',
      accessParsingProtocolOption: [],
      parsingProtocolOption: [],
      accessParsingProtocol: '',
      parsingProtocol: '',
      location: '',
      convertDialog: false,
      convertData: {},
      equipmentOptions: [],
      priorityMap: new Map(),
      warningTypeMap: new Map(),
      equipmentTypeOptions: [],
      equipmentId: "1826830809789648898",//设备id
      loadingMap: true,
      BMap: '',
      map: '',
      geoc: '',
      addressKeyword: '',
      pointLngLat: {},
      center: {lng: 109.45744048529967, lat: 36.49771311230842},
      zoom: 13,
      options: [],
      title: '设备新增',
      // 是否显示弹出层
      open: false,
      type: "view",
      activeName: "base",
      readOnly: false,
      tableLoading: true,
      formInline: {}, // 告警过滤条件
      formPoint: {}, // 点位过滤条件
      warningRecordFormInline: {
        warningType: 1
      }, // 预警记录过滤条件
      // 用于判断是否超出容器
      isTextOverflow: false,
      tableData: [],
      configTable: [], // 配置列表
      fileList: [],
      uploadPath: process.env.VUE_APP_BASE_API + '/admin-api/infra/file/upload',
      headers: {Authorization: "Bearer " + getAccessToken()}, // 设置上传的请求头部
      productPoint: [],
      rules: {
        equipmentName: [
          {
            required: true,
            message: "请输入设备名称",
            trigger: ["blur", "change"],
          },
          {max: 50, message: '输入内容超出限制，请重新编辑。', trigger: 'blur'}
        ],
        productName: [
          {
            required: true,
            message: "请点击选择产品按钮",
            trigger: ["blur", "change"],
          }
        ],
        equipmentCode: [
          {
            required: true,
            message: "请输入设备编码",
            trigger: ["blur", "change"],
          },
          {max: 50, message: '输入内容超出限制，请重新编辑。', trigger: 'blur'}
        ],
        parentEquipment: [
          {
            required: true,
            message: "请选择关联父设备",
            trigger: ["blur", "change"],
          }
        ],
      },
      dataForm: {
        location: ''
      },
      authenticationForm: {
        accessAddress: process.env.VUE_APP_EMQX_URL,
        userPassword: "",
        userAccount: ""
      },
      page: {
        pageSize: 12,
        currentPage: 1,
        total: 0,
      },
      configPage: {
        pageSize: 10,
        currentPage: 1,
        total: 0,
      },
      pageWarningRecord: {
        pageSize: 10,
        currentPage: 1,
        total: 0,
      },
      // 遮罩层
      loading: true,
      // 定时任务表格数据
      jobList: [],
      // 表单参数
      form: {},
      jobRules: {
        jobDesc: [{required: true, message: "任务名称不能为空", trigger: "blur"}],
        jobParam: [{required: true, message: "功能模型不能为空", trigger: "blur"}],
        scheduleConf: [{required: true, message: "CRON 表达式不能为空", trigger: "blur"}],
      },
      // 查询参数
      queryParams: {
        pageSize: 10,
        currentPage: 1,
        total: 0,
      },
      // 查询参数
      jobQueryParams: {
        equipmentId: '',
        jobName: ''
      },
      // 总条数
      total: 0,
      dictHandler: [],
      // 是否显示Cron表达式弹出层
      openCron: false,
      // 功能模型列表
      functionalModelList: [],
    }
  },
  async created() {
    this.getConfigKey("iot").then((res) => {
      this.iotCode = res.data;
    })
    let {equipmentCode, id, type} = this.$route.query;
    this.type = type;
    if (["edit"].includes(this.type)) {
      this.dataForm.equipmentCode = equipmentCode;
      this.dataForm.id = id;
      this.title = "设备编辑";
      this.initData();
      this.onLoad(this.page);
      this.configOnload(this.configPage, {})
    }
    if (["view"].includes(this.type)) {
      this.dataForm.equipmentCode = equipmentCode;
      this.dataForm.id = id;
      this.readOnly = true;
      this.title = "设备查看";
      this.initData();
      this.onLoad(this.page);
      this.configOnload(this.configPage, {})
    }
  },
  mounted() {
    this.accessParsingProtocolOption = this.$store.state.dict.dictDatas.access_parsing_protocol
    this.equipmentTypeOptions = this.$store.state.dict.dictDatas.equipment_type
    this.options = this.$store.state.dict.dictDatas.is_enable
    this.recordDict = this.$store.state.dict.dictDatas.record_status
    const type = this.$route.query.type;
    if (type === 'add') {
      this.$tab.updatePage(Object.assign({}, this.$route, {title: '设备新增'}));
    } else if (type === 'edit') {
      this.$tab.updatePage(Object.assign({}, this.$route, {title: '设备编辑'}));
    } else {
      this.$tab.updatePage(Object.assign({}, this.$route, {title: '设备查看'}));
    }
    this.getList();
  },
  methods: {
    actionLog(row) {
      this.$refs.actionLog.init(row)
    },
    warningLog(row) {
      this.$refs.warningLog.init(row)
    },
    // 点击处理日志按钮
    handleLog(row) {
      this.$refs.recordLog.init(row)
    },
    // 点击处理按钮
    handle(row) {
      this.$refs.recordStatus.init(row)
    },
    convertDate(date,format){
      return formatDate(new Date(date),format)
    },
    openHistory(item){
      let params = {}
      params.eventCode = item.pointCode
      params.deviceCode = this.dataForm.equipmentCode
      this.eventRecordDialog = true
      list(params).then(res=>{
        this.eventRecordData = res.data
      })

    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.appName = "iot-cps"
          this.form.jobParam = this.dataForm.equipmentCode + "," + this.form.jobParam
          this.form.jobGroup = 3
          this.form.executorHandler = "funcDistributionHandler"
          this.form.jobType = "equipment"
          this.form.relevanceId = this.dataForm.id

          saveOrUpdate(this.form).then(res => {
            this.$modal.msgSuccess("操作成功");
            this.open = false;
            this.getList();
          })
        }
      });
    },
    /** 确定后回传值 */
    crontabFill(value) {
      this.form.cron = value;
    },
    changeCurrentWarningRecord (val) {
      this.pageWarningRecord.currentPage = val
      this.onLoadWarningRecord(this.pageWarningRecord, {})
    },
    changeSizeWarningRecord (val) {
      this.pageWarningRecord.pageSize = val
      this.onLoadWarningRecord(this.pageWarningRecord, {})
    },
    handleSearchWarningRecord () {
      this.pageWarningRecord.currentPage = 1
      if (this.warningRecordFormInline.createTime) {
        this.warningRecordFormInline.createTimes = this.warningRecordFormInline.createTime.join(",")
      }
      this.onLoadWarningRecord(this.pageWarningRecord, this.warningRecordFormInline)
    },
    handleEmptyWarningRecord () {
      this.warningRecordFormInline = {}
      this.warningRecordFormInline.warningType = "1"
      this.onLoadWarningRecord(this.pageWarningRecord)
    },
    onLoadWarningRecord(page, params = {}) {
      this.pageWarningRecord = page;
      this.WarningRecordTableLoading = true;
      this.warningRecordFormInline.equipmentCode = this.dataForm.equipmentCode
      getWarningRecordPage(
        page.currentPage,
        page.pageSize,
        this.warningRecordFormInline
      ).then(res => {
        const data = res.data;
        this.pageWarningRecord.total = data.total;
        this.warningRecordData = data.records;
        this.WarningRecordTableLoading = false;
      });
    },
    /** cron表达式按钮操作 */
    handleShowCron() {
      this.jobCron = this.form.jobCron;
      this.openCron = true;
    },
    /** 立即停止 **/
    handleExecute(row, status) {
      if (status === 'stop') {
        row.jobStatus = 0
        this.$modal.confirm('确认要立即停止"' + row.jobDesc + '"任务吗?').then(function () {
          return handlerStop(row.id)
        }).then(() => {
          this.$modal.msgSuccess("停止成功");
          this.getList();
        });
      } else {
        row.jobStatus = 1
        this.$modal.confirm('确认要立即启动"' + row.jobDesc + '"任务吗?').then(function () {
          return handlerStart(row.id)
        }).then(() => {
          this.$modal.msgSuccess("启动成功");
          this.getList();
        });
      }

    },
    /** 立即执行一次 **/
    handleRun(row) {
      if (this.dataForm.isEnable == 1) {
        this.$modal.confirm('确认要立即执行一次"' + row.jobDesc + '"任务吗?').then(function () {
          return runJob(row.id,row.executorParam);
        }).then(() => {
          this.$modal.msgSuccess("执行成功");
        }).catch(() => {
        });
      } else {
        this.$message.warning('请先启用当前设备')
      }
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleRun":
          this.handleRun(row);
          break;
        case "handleStart":
          this.handleStart(row);
          break;
        case "handleExecute":
          this.handleExecute(row);
          break;
        case "handleView":
          this.handleView(row);
          break;
        case "handleJobLog":
          this.handleJobLog(row);
          break;
        default:
          break;
      }
    },
    /** 查询定时任务列表 */
    getList(currentPage, jobQuery = {}) {
      let query = {}
      query.start = 0
      query.length = this.queryParams.pageSize
      //此处的jobStatus实际对应后端jobType
      query.jobStatus = "equipment"
      query.relevanceId = this.dataForm.id
      if (currentPage) {
        query.pageNo = currentPage
      } else {
        query.pageNo = this.queryParams.currentPage
      }
      //如果有根据任务名称过滤
      if (jobQuery) {
        query.jobDesc = jobQuery.jobDesc
      }
      listJob(query).then(res => {
        this.jobList = res.data
        this.configPage.total = res.recordsTotal
        this.loading = false;
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id;
      this.$modal.confirm('是否确认删除定时任务' + row.jobDesc + '?').then(function () {
        return delJob(ids)
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      // 获取当前设备下的功能模型
      getByEquipmentId(this.dataForm.id).then(res => {
        this.functionalModelList = res.data
      })
      this.form = row;
      this.form.jobParam = this.form.executorParam.split(",")[1]
      this.form.cron = this.form.scheduleConf
      this.title = "修改任务";
      this.open = true;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加任务";
      // 获取当前设备下的功能模型
      getByEquipmentId(this.dataForm.id).then(res => {
        this.functionalModelList = res.data
      })
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        jobName: undefined,
        jobParam: undefined,
        jobCron: undefined
      };
      this.nextTimes = [];
      this.resetForm("form");
    },
    equipmentCodeRenewal() {
      this.dataForm.attributePush = this.iotCode + '/' + this.dataForm.productCode + '/' + this.dataForm.equipmentCode + '/attributePush'
      this.dataForm.eventPush = this.iotCode + '/' + this.dataForm.productCode + '/' + this.dataForm.equipmentCode + '/eventPush'
      this.dataForm.featureIssued = this.iotCode + '/' + this.dataForm.productCode + '/' + this.dataForm.equipmentCode + '/featureIssued'
    },
    convert(convertId, type) {
      //type: attribute 属性上报  event 事件上报  feature 功能下发
      this.convertDialog = true;
      this.$nextTick(() => {
        this.$refs.convertDialogRef.initData(convertId, type)
      })
    },
    convertDialogClose() {
      this.convertDialog = false;
    },
   async convertConfirm() {
     await this.$refs.convertDialogRef.confirm(this.dataForm.id)
      this.convertDialog = false;
      //更新设备详情数据
      this.initData();
    },
    getEquipmentOptions() {
      getEquipmentOptions(this.dataForm.productCode).then((res) => {
        this.equipmentOptions = res.data;
      })
    },
    parentEquipmentAlter() {
      let parentEquipment = this.equipmentOptions.find(e => e.equipmentCode === this.dataForm.parentEquipment);
      this.dataForm.parsingProtocol = parentEquipment.parsingProtocol;
      this.dataForm.serviceIp = parentEquipment.serviceIp;
      this.dataForm.userAccount = parentEquipment.userAccount;
      this.dataForm.userPassword = parentEquipment.userPassword;
    },
    warningConfigRestartEdit(item) {
      this.$router.push({
        path: `/warning/configuration/Add`,
        query: {
          type: 'edit',
          id: item.id,
        }
      })
    },
    warningConfigUntie(item) {
      untie(item.id, this.dataForm.equipmentCode).then((res) => {
        if (res.data) {
          this.$message.success('解绑成功!')
          this.configOnload(this.configPage, {})
        } else {
          this.$message.warning('解绑失败!')
        }
      })
    },
    warningConfigView(item) {
      this.$router.push({
        path: `/warning/configuration/Add`,
        query: {
          type: 'view',
          id: item.id
        }
      })
    },
    warningConfigPageChange(item) {
      this.configPage.currentPage = item
      this.configOnload(this.configPage, {})
    },
    warningConfigSizeChange(item) {
      this.configPage.pageSize = item
      this.configOnload(this.configPage, {})
    },
    /**
     * 进行生成账号密码
     */
    generateAccountPassword() {
      if (this.dataForm.equipmentType === '子设备') {
        this.$message.warning('子设备无需生成账号密码！')
        return;
      }
      this.$loading();
      addAuthenticationAdd(this.dataForm).then((res) => {
        const {data, code} = res
        if (code === 0) {
          this.$message.success(
            "操作成功"
          );
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
    locationError({StatusCode}) {
      this.$message.warning(`自动定位失败，请手动选择位置`);
    },
    locationSuccess({point, addressComponent, marker}) {
      this.center = {lng: point.lng, lat: point.lat}
      this.pointLngLat = {lng: point.lng, lat: point.lat}
      var addComp = addressComponent
      var pointAddress = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber
      this.dataForm.lng = point.lng
      this.dataForm.lat = point.lat
      this.dataForm.location = pointAddress
      this.addPoint({BMap: this.BMap, map: this.map});
    },
    getScrollTop() {
      let scrollTop = 0
      if (document.documentElement && document.documentElement.scrollTop) {
        scrollTop = document.documentElement.scrollTop
      } else if (document.body) {
        scrollTop = document.body.scrollTop
      }
      return scrollTop
    },
    setScrollTop(top) {
      if (!isNaN(top)) {
        if (document.documentElement && document.documentElement.scrollTop !== undefined) {
          document.documentElement.scrollTop = top
        } else if (document.body) {
          document.body.scrollTop = top
        }
      }
    },
    // 地图初始化
    handler({BMap, map}) {
      this.top = this.getScrollTop()
      if (this.top) {
        this.setScrollTop(0)
      }
      this.BMap = BMap
      this.map = map
      this.loadingMap = true
      var geolocation = new BMap.Geolocation()
      this.geoc = new BMap.Geocoder() // 地址解析对象
      var $this = this
      // 调用百度地图api 中的获取当前位置接口
      geolocation.getCurrentPosition(function (r) {
        let myGeo = new BMap.Geocoder()
        myGeo.getLocation(new BMap.Point(r.point.lng, r.point.lat), function (result) {
          if (result) {
            $this.loadingMap = false
            $this.$set($this, 'pointLngLat', {lng: result.point.lng, lat: result.point.lat})
            $this.dataForm.lng = result.point.lng
            $this.dataForm.lat = result.point.lat
            $this.dataForm.location = result.address
            map.enableScrollWheelZoom(true) // 开启鼠标滚轮缩放,默认关闭
            $this.addPoint({BMap, map})
          }
        })
      })
    },
    // 添加点位
    addPoint({BMap, map}) {
      map.clearOverlays()
      var point = new BMap.Point(this.pointLngLat.lng, this.pointLngLat.lat)
      let zoom = map.getZoom()
      setTimeout(() => {
        map.centerAndZoom(point, zoom)
      }, 2)
      var marker = new BMap.Marker(point) // 创建标注
      map.addOverlay(marker) // 将标注添加到地图中
    },
    // 点击地图
    getClickInfo(e) {
      this.center = {lng: e.point.lng, lat: e.point.lat}
      this.pointLngLat = {lng: e.point.lng, lat: e.point.lat}
      // 获取点位信息
      let $this = this
      this.geoc.getLocation(e.point, function (rs) {
        var addComp = rs.addressComponents
        var pointAddress = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber
        $this.dataForm.lng = rs.point.lng
        $this.dataForm.lat = rs.point.lat
        $this.dataForm.location = pointAddress
        $this.addPoint({BMap: $this.BMap, map: $this.map});
      })
    },

    //文件上传个数
    handleExceed(files, fileList) {
      this.$message.warning(`只能上传一个文件`);
    },
    handleSuccess(response, file, fileList) {
      this.dataForm.image = response.data
    },
    handlePictureCardPreview(file) {
      this.dataForm.image = file.url
    },
    handleRemove(file, fileList) {
      this.dataForm.image = ""
    },
    handleJobSearch() {
      this.configPage.currentPage = 1
      this.getList(this.configPage, this.jobQueryParams)
    },
    handleJobEmpty() {

      this.formInline = {}
      this.jobQueryParams.jobDesc = ''
      this.getList(this.configPage, {})
    },
    handleSearch() {
      this.configPage.currentPage = 1
      this.configOnload(this.configPage, this.formInline)
    },
    handleEmpty() {
      this.formInline = {}
      this.configOnload(this.configPage, {})
    },
    pointSearch() {
      this.page.currentPage = 1
      this.onLoad(this.page, this.formPoint)
    },
    pointEmpty() {
      this.formPoint = {}
      this.onLoad(this.page, {})
    },
    handleClickTab(tab) {
      this.formPoint = {}
      if (tab.name === 'history') {
        this.$nextTick(() => {
          this.$refs.historyData.init(this.dataForm.equipmentCode);
        })
      }
      if (tab.name === 'pointStats') {
        this.pointType = 'pointStats';
        this.onLoad(this.page, {});
      }
      if (tab.name === 'pointIncident') {
        this.pointType = 'pointIncident';
        this.onLoad(this.page, {});
      }
      if (tab.name === 'pointFeature') {
        this.pointType = 'pointFeature';
        this.onLoad(this.page, {});
      }
      if (tab.name === 'config') {
        this.configOnload(this.configPage, {})
      }
      if (tab.name === 'monitor') {
        this.$nextTick(() => {
          this.$refs.monitoring.initData();
        })
      }
      if (tab.name === 'warningRecord') {
        this.$nextTick(() => {
          this.onLoadWarningRecord(this.pageWarningRecord)
        })
      }
      this.activeName = tab.name
    },
    indexMethod(index) {
      return index + 1
    },
    changeSize(val) {
      this.configPage.pageSize = val
      this.configOnload(this.configPage, {})
    },
    changeCurrent(val) {
      this.configPage.currentPage = val
      this.configOnload(this.configPage, {})
    },
    restOnLoad() {
      this.onLoad(this.page);
    },
    headAdd() {
      if (!this.dataForm.id) {
        this.$message.warning("请先保存设备信息");
        return;
      }
      if (this.dataForm.agreementType == "MQTT") {
        if (this.pointType === 'pointStats') {
          this.$refs.mqtt.addPoint(this.dataForm.id, this.pointType);
        } else if (this.pointType === 'pointIncident') {
          this.$refs.eventModel.addPoint(this.dataForm.id, this.pointType, this.dataForm.agreementType);
        } else {
          this.$refs.functionalModel.addPoint(this.dataForm.id, this.pointType, this.dataForm.agreementType);
        }
      }
    },
    // 选择产品
    selectProduct(data) {
      if (data.image) {
        this.$set(this.dataForm, 'image', data.image)
        let dataObject = {
          url: data.image
        }
        this.fileList = [];
        this.fileList.push(dataObject)
      } else {
        this.dataForm.image = ''
      }
      this.$set(this.dataForm, 'agreementType', data.agreementType)
      this.$set(this.dataForm, 'productCode', data.productCode)
      this.$set(this.dataForm, 'productName', data.productName)
      this.$set(this.dataForm, 'equipmentType', data.equipmentType)
      this.$set(this.dataForm, 'image', data.image)
      // EMQX客户端认证，默认使用产品带过来的。
      this.$set(this.dataForm, 'userAccount', data.userAccount)
      this.$set(this.dataForm, 'userPassword', data.userPassword)

      if (data.equipmentType === '子设备') {
        //设备关联的产品变动后，重置该设备关联的父设备信息
        this.$set(this.dataForm, 'parentEquipment', '')
        this.$nextTick(() => {
          this.getEquipmentOptions();
        })
      }
    },
    initData() {
      detail(this.dataForm.id).then((res) => {
        this.dataForm = res.data;
        this.getEquipmentOptions();
        if (this.dataForm.equipmentType === '子设备') {
          this.isParsingProtocolDisabled = true;
        }
        if (this.dataForm.attributePush === '' || this.dataForm.attributePush === undefined || this.dataForm.attributePush === null) {
          this.dataForm.attributePush = this.iotCode + '/' + this.dataForm.productCode + '/' + this.dataForm.equipmentCode + '/attributePush';
        }
        if (this.dataForm.eventPush === '' || this.dataForm.eventPush === undefined || this.dataForm.eventPush === null) {
          this.dataForm.eventPush = this.iotCode + '/' + this.dataForm.productCode + '/' + this.dataForm.equipmentCode + '/eventPush';
        }
        if (this.dataForm.featureIssued === '' || this.dataForm.featureIssued === undefined || this.dataForm.featureIssued === null) {
          this.dataForm.featureIssued = this.iotCode + '/' + this.dataForm.productCode + '/' + this.dataForm.equipmentCode + '/featureIssued';
        }
        if (res.data.image !== "" && res.data.image !== null && res.data.image !== undefined) {
          let file = {url: res.data.image}
          this.fileList = [];
          this.fileList.push(file)
        }
      });
    },
    headCancel() {
      this.$router.back();
    },
    headSave() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          const equipmentCode = this.dataForm.equipmentCode;
          const regex = /^[a-zA-Z0-9][a-zA-Z0-9_]*$/;
          if (!regex.test(equipmentCode)) {
            this.$message.warning("设备编码只能以字母开头，只能包含字母、数字和下划线!");
            return;
          }
          this.$loading();
          this.dataForm.attributePush = this.iotCode + '/' + this.dataForm.productCode + '/' + this.dataForm.equipmentCode + '/attributePush'
          this.dataForm.eventPush = this.iotCode + '/' + this.dataForm.productCode + '/' + this.dataForm.equipmentCode + '/eventPush'
          this.dataForm.featureIssued = this.iotCode + '/' + this.dataForm.productCode + '/' + this.dataForm.equipmentCode + '/featureIssued'

          submit(this.dataForm)
            .then((res) => {
              const {data, code} = res
              if (code === 0) {
                this.$message.success(
                  "操作成功"
                );
                //刷新数据
                Object.assign(this.dataForm, data);
                this.dataForm = data;
                this.initData();
                this.type = "edit";
                this.getList();
              } else {
                this.$message.warning(msg);
              }
            })
            .finally(() => {
              this.$loading().close();
            });
        }
      })
    },
    // 选择产品
    headSelectProduct() {
      if (["add"].includes(this.type)) {
        this.$refs.productDialog.openDialog();
      } else {
        this.$confirm("重新选择产品后，除设备特有点位外，其余点位信息将被覆盖", {
          confirmButtonText: "确认",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          this.$refs.productDialog.openDialog();
        });
      }
    },
    rowEdit(row, type) {
      row.pointType = this.pointType
      row.type = type
      if (row.agreementType == "MQTT") {
        if (this.pointType === 'pointStats') {
          this.$refs.mqtt.editPoint(row);
        } else if (this.pointType === 'pointIncident') {
          this.$refs.eventModel.editPoint(row);
        } else {
          this.$refs.functionalModel.editPoint(row);
        }
      }
    },
    rowRemove(row) {
      this.$confirm("确定删除数据", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return remove(row.id);
        })
        .then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功",
          });
        });
    },
    configOnload(page, params = {}) {
      if (this.dataForm.equipmentCode === undefined) {
        return
      }
      // this.page = page;
      this.tableLoading = true;
      params.equipmentCode = this.dataForm.equipmentCode
      getConifgPage(page.currentPage, page.pageSize, params).then(res => {
        const data = res.data
        this.configTable = data.records
        this.configPage.total = data.total
        this.tableLoading = false;
      })
    },
    onLoad(page, params = {}) {
      // this.page = page;
      if (this.dataForm.id) {
        params.equipmentId = this.dataForm.id;
      }
      params.pointType = this.pointType
      getPage(page.currentPage, page.pageSize, Object.assign(params, this.formPoint)).then((res) => {
        const data = res.data;
        this.page.total = data.total;
        this.tableData = data.records;
      });
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.onLoad(this.page);
    },
    handleCurrentChange(val) {
      this.page.currentPage = val;
      this.onLoad(this.page);
    },
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

      .topBottom {
        display: flex;
        align-items: center;
        justify-content: space-between;
        flex: 1;

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
}

.el-tabs__item {
  font-weight: bold; /* 设置所有 tab 项的字体加粗 */
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

.map {
  width: 80%;

  height: 500px;
}
pre {
  background-color: #f4f4f4;
  border: 1px solid #ddd;
  border-left: 3px solid #f36d33;
  color: #666;
  page-break-inside: avoid;
  font-family: monospace;
  font-size: 15px;
  line-height: 1.6;
  margin-bottom: 1.6em;
  max-width: 100%;
  overflow: auto;
  padding: 1em 1.5em;
  display: block;
  word-wrap: break-word;
}
</style>
