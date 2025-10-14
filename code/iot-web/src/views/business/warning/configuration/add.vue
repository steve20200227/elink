<template>
  <div style="margin: 20px">
    <div
      style="display: block; font-size: 26px; font-weight: bold; padding-bottom: 15px">
      <div v-if="this.type !== 'add'">
        <el-button icon="el-icon-back" type="primary" circle @click="headCancel" style="margin-right: 15px"/>
        <el-divider direction="vertical" style="margin-right: 15px"></el-divider>
        <!-- 根据 场景类型 的值显示不同的图片 -->
        <img style="vertical-align: middle;margin-right: 15px;height: 30px; width: 30px" v-if="dataForm.warningType == '1'" src="@/assets/whereType.png" />
        <img style="vertical-align: middle;margin-right: 15px;height: 30px; width: 32px" v-else-if="dataForm.warningType == '2'" src="@/assets/taskType.png" />
        <img style="vertical-align: middle;margin-right: 15px;height: 30px; width: 30px" v-else src="@/assets/shouType.png" />
        <span style="margin-right: 20px">{{ dataForm.warningName }}</span>
      </div>
      <div v-else>
        <el-button icon="el-icon-back" type="primary" circle @click="headCancel" style="margin-right: 15px"/>
        <el-divider direction="vertical" style="margin-right: 15px"></el-divider>
        <!-- 根据 场景类型 的值显示不同的图片 -->
        <img style="vertical-align: middle;margin-right: 15px;height: 30px; width: 30px" v-if="dataForm.warningType == '1'" src="@/assets/whereType.png" />
        <img style="vertical-align: middle;margin-right: 15px;height: 30px; width: 32px" v-else-if="dataForm.warningType == '2'" src="@/assets/taskType.png" />
        <img style="vertical-align: middle;margin-right: 15px;height: 30px; width: 30px" v-else src="@/assets/shouType.png" />
        <span style="margin-right: 15px">创建场景</span>
      </div>
    </div>
    <div v-if="this.type !== 'add'"
         style="display: block; font-size: 16px; padding-bottom: 30px">
      <div>
        <span style="margin-right: 55px">优先级: {{ priorityMap.get(dataForm.priority) }}</span>
        <span style="margin-right: 55px">类型: {{ warningTypeMap.get(dataForm.warningType) }}</span>
        <span v-if="dataForm.isEnable == 1" style="margin-right: 25px; color: #3399FF"><svg-icon icon-class="online" style="margin-right: 5px"/>运行中</span>
        <span v-else style="margin-right: 25px; color: #f16506"><svg-icon icon-class="offline" style="margin-right: 5px"/>已停用</span>
      </div>
    </div>
    <!--  Tab标签页  -->
    <div>
      <el-tabs v-model="activeTab"  @tab-click="handleClick">
        <el-tab-pane label="场景设置" name="base">
          <div style="margin: 10px 20px; border: 1px solid #dcdfe6; background-color: #f9fafc; border-radius: 8px; padding: 20px;">
            <el-form
              ref="baseForm"
              :inline="true"
              :rules="rules"
              :model="dataForm"
              :hide-required-asterisk="true"
              class="product-form"
            >
              <el-row style="margin: 10px 25px">
                <el-col :span="8">
                  <el-form-item label="场景名称:" prop="warningName" label-width="100px">
                    <el-input
                      v-model="dataForm.warningName"
                      size="small"
                      style="width: 100%"
                      :disabled="type == 'view'"
                      placeholder="请输入场景名称"
                    ></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row style="margin: 0 25px">
                <el-col :span="8">
                  <el-form-item label="优先级:" prop="priority" label-width="100px">
                    <el-radio-group v-model="dataForm.priority" :disabled="type == 'view'">
                      <el-radio
                        border
                        v-for="item in priorityDict"
                        :key="item.value * 1"
                        :label="item.value * 1"
                      >
                        {{item.label}}
                      </el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row style="margin: 10px 25px">
                <el-col :span="8">
                  <el-form-item label="适用设备:" prop="productCode" label-width="100px" v-if="dataForm.warningType == '1'">
                    <el-button :disabled="readOnly" type="primary" @click="handleEquipment()">{{dataForm.productName ? dataForm.productName : '配置'}}</el-button>
                    <el-button :disabled="readOnly" type="primary" @click="handleEquipment()" v-if="dataForm.productName">设备数 {{dataForm.equipmentList.length}}</el-button>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row style="margin: 10px 25px">
                <el-col :span="8">
                  <el-form-item label="已选设备:" label-width="100px">
                    <div class="equipment">
                      <el-tag class="equipment-name" v-if="readOnly"  v-for="item in dataForm.equipmentName">
                        {{item.equipmentName}}
                      </el-tag>
                      <el-tag class="equipment-name" v-if="!readOnly" @close="handleTagClose(item)" :key="index" :disable-transitions="false" closable v-for="(item ,index) in dataForm.equipmentName">
                        {{item.equipmentName}}
                      </el-tag>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row style="margin: 10px 25px" v-if="dataForm.warningType == 1">
                <el-col :span="8">
                  <el-form-item label="生效时间段:" prop="enableTimeRange" label-width="100px" v-if="dataForm.warningType != '3'">
                    <el-time-picker
                      format="HH:mm:ss"
                      value-format="HH:mm:ss"
                      is-range
                      :disabled="readOnly"
                      v-model="dataForm.enableTimeRange"
                      range-separator="至"
                      start-placeholder="开始时间"
                      end-placeholder="结束时间"
                      placeholder="选择时间范围">
                    </el-time-picker>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
            <div style="margin: 10px 25px">
              <el-button v-if="!readOnly" type="primary" @click="headSave()">{{this.type !== 'add' ? '更新配置' : '创建'}}</el-button>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="触发条件" name="where" v-if="dataForm.warningType != '2' && dataForm.warningType != '3'">
          <div style="margin: 10px 20px; border: 1px solid #dcdfe6; background-color: #f9fafc; border-radius: 8px; padding: 20px;">
            <el-form
              ref="ruleForm"
              :inline="true"
              :model="dataForm"
              :hide-required-asterisk="true"
              class="product-form"
            >
              <el-row class="conditionBox" disabled="type == 'view'" style="margin: 10px 25px">
                <el-col style="width: 200px">
                  <el-form-item label="条件类型:" prop="whereType" style="width: 100px">
                    <el-button :disabled="readOnly" type="primary" @click="showWhereType()">{{dataForm.whereType ? (dataForm.whereType == '1' ? '普通条件' : dataForm.whereType == '2' ? '时间窗口' : '设备事件') : '配置'}}</el-button>
                  </el-form-item>
                </el-col>
                <el-col style="width: 350px" v-if="dataForm.whereType == '2'">
                  <el-form-item label="时间区间:" prop="timeRange" style="width: 300px;">
                    <el-input :disabled="readOnly" placeholder="请输入时间" v-model="dataForm.timeRange">
                      <template slot="append">
                        <el-select :disabled="readOnly" v-model="dataForm.timeType" style="width: 80px;">
                          <el-option
                            v-for="item in timeTypeOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                          </el-option>
                        </el-select>
                      </template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :key="index" v-if="dataForm.whereType != '3'" v-for="(where, index) in dataForm.whereList" style="margin: 10px 25px 10px 25px">
                <div class="condition" style="display: flex;">
                  <el-tag style="width: 910px">
                    <el-col v-if="index == 0" style="padding-right: 5px; width: 100px">
                      <el-input :disabled="true" value="满足" style="height: 42px; line-height: 35px;">
                      </el-input>
                    </el-col>
                    <el-col v-else style="padding-right: 5px; width: 100px">
                      <el-select :disabled="readOnly" @change="forceUpdate" v-model="where.whereType" style="height: 42px; line-height: 35px;">
                        <el-option
                          v-for="item in ruleWhereTypeOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                        </el-option>
                      </el-select>
                    </el-col>
                    <el-col style="padding-right: 5px; width: 200px">
                      <el-select :disabled="readOnly" @change="forceUpdate" v-model="where.pointCode" style="height: 42px; line-height: 35px;">
                        <el-option-group
                          v-for="group in pointOptions"
                          :key="group.label"
                          :label="group.label">
                          <el-option
                            v-for="item in group.options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                          </el-option>
                        </el-option-group>

                      </el-select>
                    </el-col>
                    <el-col style="padding-right: 5px; width: 120px">
<!--                      <el-select :disabled="readOnly" @change="forceUpdate" v-model="where.valueType" style="height: 42px; line-height: 35px;">-->
<!--                        <el-option-->
<!--                          v-for="item in computeTypeOptions"-->
<!--                          :key="item.value"-->
<!--                          :label="item.label"-->
<!--                          :value="item.value">-->
<!--                        </el-option>-->
<!--                      </el-select>-->
                      <el-select :disabled="readOnly" @change="changeCompute" v-model="where.valueType" style="height: 42px; line-height: 35px;">
                        <el-option-group
                          v-for="group in computeTypeOptions"
                          :key="group.label"
                          :label="group.label">
                          <el-option
                            v-for="item in group.options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                          </el-option>
                        </el-option-group>
                      </el-select>
                    </el-col>
                    <el-col style="padding-right: 5px; width: 150px" v-if="!['8', '9', '10', '11'].includes(where.valueType)">
                    <!--    等于    -->
                      <el-select :disabled="readOnly" @change="forceUpdate" v-model="where.conditionType" style="height: 42px; line-height: 35px;">
                        <el-option
                          v-for="item in ruleConditionTypeOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                        </el-option>
                      </el-select>
                    </el-col>
                    <el-col style="padding-right: 5px; width: 100px" v-if="!['8', '9', '10', '11'].includes(where.valueType)">
                      <el-input
                        :disabled="readOnly"
                        @input="forceUpdate"
                        v-model="where.param1"
                        maxlength="50"
                        style="height: 42px; line-height: 35px;">
                      </el-input>
                    </el-col>
                    <el-col style="padding-right: 5px; width: 100px" v-if="!['8', '9', '10', '11'].includes(where.valueType)">
                      <el-input v-if="['7', '8'].includes(where.conditionType)"
                                @input="forceUpdate"
                                v-model="where.param2"
                                maxlength="50"
                                :disabled="readOnly"
                                style="height: 42px; line-height: 35px;">
                      </el-input>
                    </el-col>
                    <el-col style="width: auto">
                      <el-button style="margin-left: 15px" icon="el-icon-delete" v-if="index != 0 && !readOnly" type="info" @click="whereRemove(index)" circle></el-button>
                    </el-col>
                    <el-col style="width: auto">
                      <el-button style="margin-left: 15px;margin-top: 10px;width: 36px;height: 36px;" icon="el-icon-plus" type="primary" v-if="!readOnly" @click="whereAdd(index)" circle></el-button>
                    </el-col>
                  </el-tag>
                </div>
              </el-row>
              <el-row :key="index" v-if="dataForm.whereType == '3'" v-for="(where, index) in dataForm.whereList" style="margin: 10px 25px 10px 25px">
                <div style="display: flex;">
                  <el-tag style="width: 350px">
                    <el-col v-if="index == 0" style="padding-right: 5px; width: 100px">
                      <el-input :disabled="true" value="满足" style="height: 42px; line-height: 35px;">
                      </el-input>
                    </el-col>
                    <el-col v-else style="padding-right: 5px; width: 100px">
                      <el-select :disabled="readOnly" @change="forceUpdate" v-model="where.whereType" style="height: 42px; line-height: 35px;">
                        <el-option
                          v-for="item in ruleWhereTypeOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                        </el-option>
                      </el-select>
                    </el-col>
                    <el-col style="padding-right: 5px; width: 200px">
                      <el-select :disabled="readOnly" @change="forceUpdate" v-model="where.pointCode" style="height: 42px; line-height: 35px;">
                        <el-option-group
                          v-for="group in pointOptions"
                          :key="group.label"
                          :label="group.label">
                          <el-option
                            v-for="item in group.options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                          </el-option>
                        </el-option-group>

                      </el-select>
                    </el-col>
                  </el-tag>
                </div>
              </el-row>
              <el-row style="margin: 30px 25px 10px 25px;" v-if="dataForm.warningType == 1">
                <el-col style="width: 250px">
                  <el-form-item label="动作收敛方式:" prop="actionProcessType" label-width="100px" v-if="dataForm.warningType != '3'">
                    <el-select :disabled="readOnly" @change="changeActionProcess()" v-model="dataForm.actionProcessType" style="width: 120px;">
                      <el-option
                        v-for="item in actionProcessTypes"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col style="width: 150px"  v-if="['1', '2'].includes(dataForm.actionProcessType)">
                  <el-input :disabled="readOnly" placeholder="请输入" v-model="dataForm.actionTimeValue">
                    <template slot="append">
                      <el-select :disabled="readOnly" v-model="dataForm.actionTimeType" style="width: 80px;">
                        <el-option
                          v-for="item in timeTypeOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                        </el-option>
                      </el-select>
                    </template>
                  </el-input>
                </el-col>
                <el-col style="width: 80px"  v-if="['1', '2'].includes(dataForm.actionProcessType)">
                  <span style="display: inline-block;height: 36px;width: 80px; line-height: 36px; text-align: right;font-size: 15px">
                    至少重复
                  </span>
                </el-col>
                <el-col style="width: 150px; margin-left: 25px"  v-if="['1', '2'].includes(dataForm.actionProcessType)">
                  <el-input :disabled="readOnly" placeholder="请输入" v-model="dataForm.actionProcessValue">
                    <template slot="append">次</template>
                  </el-input>
                </el-col>
              </el-row>
            </el-form>
            <div style="margin: 10px 25px">
              <el-button v-if="!readOnly" type="primary" @click="headSave()">{{this.type !== 'add' ? '更新配置' : '创建'}}</el-button>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="定时设置" name="corn" v-if="dataForm.warningType == '2'">
          <div style="margin: 10px 20px; border: 1px solid #dcdfe6; background-color: #f9fafc; border-radius: 8px; padding: 20px;">
            <el-form
                ref="ruleForm"
                :inline="true"
                :model="dataForm"
                :hide-required-asterisk="true"
                class="product-form"
            >
              <el-row class="conditionBox" style="margin: 10px 25px">
                <el-col :span="6" >
                  <el-form-item label="CRON 表达式:" prop="cron" label-width="100px">
                    <el-input v-model="dataForm.cron" placeholder="请输入CRON 表达式">
                      <template slot="append">
                        <el-button type="primary" @click="handleShowCron">
                          生成表达式
                          <i class="el-icon-time el-icon--right"></i>
                        </el-button>
                      </template>
                    </el-input>
                  </el-form-item>

                </el-col>

              </el-row>
              <el-row class="conditionBox" style="margin: 10px 25px">
                <el-col :span="6" >
                  <el-form-item label="重试次数:" prop="executorFailRetryCount" label-width="100px">
                      <el-input-number v-model="dataForm.executorFailRetryCount" :min="0" :precision="0" placeholder="请输入重试次数"/>
                  </el-form-item>
                </el-col>
              </el-row>

            </el-form>
            <div style="margin: 10px 25px">
              <el-button v-if="!readOnly" type="primary" @click="headSave()">{{this.type !== 'add' ? '更新配置' : '创建'}}</el-button>
            </div>
          </div>
        </el-tab-pane >
        <el-tab-pane label="执行动作" name="action">
          <div>
            <div>
              <div
                style="
                display: flex;
                justify-content: space-between;
                font-size: 16px;
                /*border-bottom: 1px solid #ccc;*/
                padding-bottom: 5px;
                padding-top: 10px;
                font-weight: 700;
              "
              >
                <div>
                  <el-button
                    style="margin-left: 25px"
                    type="primary"
                    @click="handleAddAction"
                    v-if="type != 'view'"
                  >新增</el-button>
                </div>
              </div>
              <div style="margin: 10px 25px">
                <el-row v-if="actionData.length" style="margin-top: 20px">
                  <div class="warning-card">
                    <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item, index) in actionData" :key="index">
                      <el-card class="card" shadow="always" v-if="item.actionType != 2" style="font-size: 14px; padding-bottom: 10px; margin-right: 10px;margin-bottom: 10px">
                        <div style="margin-bottom: 10px">
                          <div style="padding: 20px 20px 5px 20px">
                            <div class="point-head" style="position: relative;font-size: 18px; font-weight: bold; margin-bottom: 10px;">
                              {{ item.actionName }}
                              <el-tooltip v-if="item.actionType != 2" class="item" effect="dark" content="编辑" placement="top">
                                <a style="font-size: 14px; position: absolute; right: 0" @click="viewConfig(item)">
                                  <span class="el-icon-view" style="font-size: 18px;color: #1677FF"></span>
                                </a>
                              </el-tooltip>
                            </div>
                            <div class="point-content" style="margin-bottom: 10px">
                              <div style="margin: 16px 6px 0 0px">
                                <span style="color: #646a73">动作类型：</span>
                                <span style="color: #1677ff;">
                                  <dict-tag :type="DICT_TYPE.ACTION_TYPE" :value="item.actionType" />
                                </span>
                              </div>
                              <div style="margin: 16px 6px 0 0px">
                                <span style="color: #646a73">创建时间：</span>
                                <span style="color: #1677ff;">{{ parseTime(item.createTime) }}</span>
                              </div>
                            </div>
                            <el-divider v-if="type != 'view'"></el-divider>
                          </div>

                          <div style="padding: 0 20px 0 20px;" v-if="type != 'view'">
                            <div style="float: left">
                              <el-button type="primary" size="mini" style="margin: 5px" v-if="item.actionType == '1'"
                                         @click="editConfig(item)">编辑
                              </el-button>
                              <el-button size="mini" style="margin: 5px"
                                         @click="rowDeleteAction(item)">删除
                              </el-button>
                            </div>
                          </div>
                        </div>
                      </el-card>
                      <el-card class="card" shadow="always" v-else style="font-size: 14px; padding-bottom: 10px; margin-right: 10px;margin-bottom: 10px">
                        <div style="margin-bottom: 10px">
                          <div style="padding: 20px 20px 5px 20px">
                            <div class="point-head" style="position: relative;font-size: 18px; font-weight: bold; margin-bottom: 10px;">
                              {{ item.actionName }}
                              <el-tooltip v-if="item.actionType != 2" class="item" effect="dark" content="编辑" placement="top">
                                <a style="font-size: 14px; position: absolute; right: 0" @click="viewConfig(item)">
                                  <span class="el-icon-view" style="font-size: 18px;color: #1677FF"></span>
                                </a>
                              </el-tooltip>
                            </div>
                            <div class="point-content" style="margin-bottom: 10px">
                              <div style="margin: 16px 6px 0 0px">
                                <span style="color: #646a73">产品名称：</span>
                                <span style="color: #1677ff;">{{ item.productName.length > 8 ? item.productName.substring(0, 8) + '...' : item.productName }}</span>
                              </div>
                              <div style="margin: 16px 6px 0 0px">
                                    <span style="color: #646a73">设备数：</span>
                                    <span style="color: #1677ff;">{{ item.sourceIds.split(",").length }}</span>
                              </div>
                            </div>
                            <el-divider v-if="type != 'view'"></el-divider>
                          </div>

                          <div style="padding: 0 20px 0 20px;" v-if="type != 'view'">
                            <div style="float: left">
                              <el-button type="primary" size="mini" style="margin: 5px" v-if="item.actionType == '1'"
                                         @click="editConfig(item)">编辑
                              </el-button>
                              <el-button size="mini" style="margin: 5px"
                                         @click="rowDeleteAction(item)">删除
                              </el-button>
                            </div>
                          </div>
                        </div>
                      </el-card>
                    </el-col>
                  </div>
                </el-row>
              </div>
            </div>
            <div style="margin: 10px 25px">
              <el-button v-if="!readOnly" type="primary" @click="headSave()">{{this.type !== 'add' ? '更新配置' : '创建'}}</el-button>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="执行日志" v-if="type !== 'add'" name="record">
          <div style="margin-top: 5px">
            <el-form ref="gridHeadLayout" :inline="true" :model="formInline" class="demo-form-inline">
              <el-form-item v-if="dataForm.warningType == '1'">
                <el-input
                  v-model="formInline.equipmentName"
                  size="mini"
                  placeholder="请输入设备名称">
                </el-input>
              </el-form-item>
              <el-form-item v-if="dataForm.warningType == '1'">
                <el-select v-model="formInline.status" size="mini" style="width: 100%" placeholder="请选择记录状态">
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
                  v-model="formInline.createTime"
                  type="daterange"
                  range-separator="至"
                  value-format="yyyy-MM-dd"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期">
                </el-date-picker>
              </el-form-item>
              <el-form-item class="top-button">
                <el-button size="mini" @click="handleSearch" icon="el-icon-search"></el-button>
                <el-button size="mini" @click="handleEmpty" icon="el-icon-refresh-right"></el-button>
              </el-form-item>
            </el-form>
          </div>
          <div style="margin-top: 10px" v-if="dataForm.warningType != '1'">
            <div style="max-height: calc(100vh - 285px);height: calc(100vh - 345px); overflow: auto;">
              <el-row v-if="recordData.length" style="margin-top: 20px">
                <div class="warning-card">
                  <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6" v-for="(item, index) in recordData" :key="index">
                    <el-card shadow="always"
                             class="record-info"
                             style="position: relative;font-size: 14px; padding-bottom: 10px; margin-right: 10px;margin-bottom: 10px;">
                      <div style="margin-bottom: 10px;">
                        <div style="padding: 20px 20px 5px 20px">
                          <div class="point-head" style="position: relative;font-size: 18px; font-weight: bold; margin-bottom: 10px;">
                            {{ item.configName }}
                          </div>
                          <div class="point-content" style="margin-bottom: 10px">
                            <div style="margin: 16px 6px 0 0px;">
                              <span style="color: #646a73">场景触发时间：</span>
                              <span style="color: #1677ff;">{{ parseTime(item.createTime) }}</span>
                            </div>
                          </div>
                        </div>
                        <div style="padding: 0 20px 0 20px;position: relative; z-index: 10;">
                          <div class="record-button" style="font-size: 14px;width: 100%;text-align: center">
                            <el-button size="mini" type="info" style="margin: 5px;width: 140px;" plain
                                       @click="actionLog(item)">动 作 日 志
                            </el-button>
                          </div>
                        </div>
                      </div>
                    </el-card>
                  </el-col>
                </div>
              </el-row>
            </div>
            <el-pagination
              background
              :current-page="recordPage.currentPage"
              :page-sizes="[9, 18, 27, 36, 72]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="recordPage.total"
              :page-size="recordPage.pageSize"
              :pager-count="5"
              @size-change="recordChangeSize"
              @current-change="recordChangeCurrent"
              style="float: right; margin: 20px 0"
            >
            </el-pagination>
          </div>
          <div style="margin-top: 10px" v-else>
            <div style="max-height: calc(100vh - 285px);height: calc(100vh - 345px); overflow: auto;">
              <el-row v-if="recordData.length" style="margin-top: 20px">
                <div class="warning-card">
                  <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8" v-for="(item, index) in recordData" :key="index">
                    <el-card shadow="always"
                             :class="{'record-success': (item.status == 1), 'record-danger': (item.status == 0)}"
                             style="position: relative;font-size: 14px; padding-bottom: 10px; margin-right: 10px;margin-bottom: 10px;">
                      <div style="margin-bottom: 10px;">
                        <div style="padding: 20px 20px 5px 20px">
                          <div class="point-head" style="position: relative;font-size: 18px; font-weight: bold; margin-bottom: 10px;">
                            {{ item.configName }}
                          </div>
                          <div class="point-content" style="margin-bottom: 10px">
                            <div style="margin: 16px 6px 0 0px;">
                              <el-row>
                                <el-col :span="24">
                                  <span style="color: #646a73">设备名称：</span>
                                  <span style="color: #1677ff;">{{ item.equipmentName }}</span>
                                </el-col>
                              </el-row>
                              <el-row>
                                <el-col :span="24">
                                  <span style="color: #646a73">场景触发时间：</span>
                                  <span style="color: #1677ff;">{{ parseTime(item.createTime) }}</span>
                                </el-col>
                              </el-row>
                            </div>
                          </div>
                        </div>
                        <div style="padding: 0 20px 0 20px;position: relative; z-index: 10;">
                          <div class="record-button" style="font-size: 14px">
                            <el-row>
                              <el-col :span="8">
                                <el-button size="mini" type="info" v-if="item.status == 0" style="margin: 5px" plain
                                           @click="handle(item)">处 理
                                </el-button>
                              </el-col>
                              <el-col :span="8">
                                <el-button size="mini" type="info" v-if="item.status == 1" style="margin: 5px" plain
                                           @click="handleLog(item)">处 理 日 志
                                </el-button>
                              </el-col>
                              <el-col :span="8">
                                <el-button size="mini" type="info" v-if="item.warningType != '3'" style="margin: 5px" plain
                                           @click="warningLog(item)">场 景 日 志
                                </el-button>
                              </el-col>
                              <el-col :span="8">
                                <el-button size="mini" type="info" style="margin: 5px" plain
                                           @click="actionLog(item)">动 作 日 志
                                </el-button>
                              </el-col>
                            </el-row>
                          </div>
                        </div>
                      </div>
                      <div>
                        <div v-if="item.status == 1" style="position:absolute; top: -10px; right: -15px;font-size: 100px;z-index: 0;">
                          <svg-icon icon-class="processed" class-name="processed-icon" style="position:absolute; top: 5px; right: 30px;margin-top: 10px;margin-left: 30px;text-align: center;" />
                        </div>
                        <div v-if="item.status == 0" style="position:absolute; top: -10px; right: -15px;font-size: 100px;z-index: 0;">
                          <svg-icon icon-class="untreated" class-name="untreated-icon" style="position:absolute; top: 5px; right: 30px;margin-top: 10px;margin-left: 30px;text-align: center;" />
                        </div>
                      </div>
                    </el-card>
                  </el-col>
                </div>
              </el-row>
            </div>
            <el-pagination
              background
              :current-page="recordPage.currentPage"
              :page-sizes="[9, 18, 27, 36]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="recordPage.total"
              :page-size="recordPage.pageSize"
              :pager-count="5"
              @size-change="recordChangeSize"
              @current-change="recordChangeCurrent"
              style="float: right; margin: 20px 0"
            >
            </el-pagination>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <select-action-list ref="actionList" @saveAction="saveAction">
    </select-action-list>

    <select-equipment-list
      ref="equipmentList"
      @selectEquipment="selectEquipment"
    >
    </select-equipment-list>

    <RecordUpdateStatusDialog ref="recordStatus" @restOnLoad="restOnLoad">
    </RecordUpdateStatusDialog>

    <RecordLogDialog ref="recordLog" @restOnLoad="restOnLoad">
    </RecordLogDialog>
    <WarningLogDialog ref="warningLog" @restOnLoad="restOnLoad">
    </WarningLogDialog>

    <actionLogDialog
      ref="actionLog">
    </actionLogDialog>
    <whereTypeDialog ref="whereTypeDialog" @submit="submitWhereType">
    </whereTypeDialog>
    <el-dialog title="Cron表达式生成器" :visible.sync="openCron" append-to-body class="scrollbar" destroy-on-close>
      <crontab @hide="openCron=false" @fill="crontabFill" :expression="expression"></crontab>
    </el-dialog>
    <el-dialog
      title="消息配置"
      :visible.sync="dialogVisible"
      width="50%"
      >
      <el-form :model="message" label-width="100px"  @submit.native.prevent>
        <el-form-item label-width="120px" v-for="(item, index) in configData" :label="item.label+':'" :prop="item.prop" :key="index">

          <el-row v-if="item.type=='input'">
            <el-col>
              <el-input  v-model="message[item.prop]" :disabled="type == 'view'||item.disabled" size="small" style="width: 100%" :placeholder="item.placeholder">
              </el-input>
            </el-col>
          </el-row>

          <el-row v-if="item.type=='select'">
            <el-col>
              <el-select @change="usersChange"  multiple  v-model="message[item.prop]" :disabled="type == 'view'||item.disabled" size="small" style="width: 100%" :placeholder="item.placeholder">
                <el-option v-for="item in users" :key="item.id+''" :label="item.nickname" :value="item.id+''" />
              </el-select>
            </el-col>
          </el-row>

          <el-row v-if="item.type=='tag'">
            <el-tag
              :key="tag"
              v-for="tag in message[item.prop]"
              style="font-size: 12px;line-height: 26px;height: 28px;"
              :closable="!(type=='view')"
              :disable-transitions="false"
              @close="handleClose(tag,item.prop)">
              {{tag}}
            </el-tag>

            <el-input
              class="input-new-tag"
              v-show="item.isShow"
              v-model="tagValueList[item.prop]"
              :ref="item.prop"
              size="small"
              @keyup.enter.native="handleInputConfirm(item.prop)"
              @blur="handleInputConfirm(item.prop)"
            >
            </el-input>
            <el-button v-if="!item.isShow" :disabled="type == 'view'" class="button-new-tag" size="small" @click="showInput(item.prop)">新增</el-button>


          </el-row>
          <el-row v-if="item.type=='editor'">
            <vue-editor
              ref="editor"

              :disabled="true"
              v-model="message[item.prop]"
              style="width: 99%;"
            ></vue-editor>
          </el-row>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveConfig">确 定</el-button>
      </span>
    </el-dialog>
    <addExecutionAction
      ref="executionAction"
      @saveAction="saveAction">
    </addExecutionAction>

    <TemplateAddDialog
      ref="templateAdd"
      v-if="templateAddShow"
      @restOnLoad="restOnLoad">
    </TemplateAddDialog>
    <addDialog
      ref="actionAddDialog"
      @restOnLoad="restOnLoad">
    </addDialog>
  </div>
</template>

<script>
import SelectActionList from "@/views/business/warning/configuration/components/selectActionList.vue";
import SelectEquipmentList from "@/views/business/warning/configuration/components/selectEquipmentList.vue";
import RecordUpdateStatusDialog from "@/views/business/warning/record/components/recordUpdateStatusDialog.vue";
import { page as getPage } from "@/api/warning/warning-record";
import { submit, detail } from "@/api/warning/warning-api";
import {list as getEquipmentPointList} from "@/api/cps/equipment-point-api"
import {list as getProductPointList} from "@/api/cps/product-point-api";
import WarningLogDialog from "@/views/business/warning/record/components/warningLogDialog.vue";
import RecordLogDialog from "@/views/business/warning/record/components/recordLogDialog.vue";
import whereTypeDialog from './components/whereTypeDialog.vue'
import Crontab from '@/components/Crontab'
import {
  aliyunConfigData,
  tencentConfigData,
  mailConfigData,
  systemMessageData
} from '@/views/business/warning/action/messageData/messageConfigData'
import { listSimpleUsers } from '@/api/system/user'
import {VueEditor} from "vue2-editor";
import actionLogDialog from "@/views/business/warning/record/components/actionLogDialog.vue";
import addExecutionAction from "@/views/business/warning/configuration/components/addExecutionAction.vue";
import TemplateAddDialog from "@/views/business/warning/configuration/components/messageTemplateViewDialog.vue";
import addDialog from "@/views/business/warning/configuration/components/actionViewDialog.vue";

export default {
  name: "add",
  components: {
    addDialog,
    TemplateAddDialog,
    addExecutionAction,
    actionLogDialog,
    RecordLogDialog,
    WarningLogDialog,
    SelectActionList,
    SelectEquipmentList,
    RecordUpdateStatusDialog,
    VueEditor,
    whereTypeDialog,
    Crontab
  },
  data() {
    return {
      // 执行动作编辑页 - 消息
      templateAddShow: false,
      // 传入的表达式
      expression: "",
      // 是否显示Cron表达式弹出层
      openCron: false,
      tagValueList:{},
      inputValue:'',
      inputVisible:false,
      formInline: {},
      recordDict: [], // 场景记录状态字典
      // 用户列表
      users: [],
      message:{
      },
      pointOptions: [],
      eventOption: [],
      configData:[],
      dialogVisible:false,
      equipmentShow: true,
      pointShow: true,
      actionDialog: false,
      tableLoading: false,
      rules: {
        warningName: [
          { max: 50, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
        ]
      },
      equipment: {
        equipmentName: "",
        equipmentId: "",
        equipmentCode: "",
      }, // 选择的设备
      point: {
        pointName: "",
        pointCode: "",
        pointId: "",
      }, // 选择的点位
      selectionList: [], // 选择的数据
      actionData: [], // 动作列表
      recordData: [], // 记录列表
      ruleExpression: "", // 记录列表
      ruleOnly: false,
      pointOnly: false,
      readOnly: false,
      activeTab: "base",
      priorityDict: [],
      priorityMap: new Map(),
      warningTypeMap: new Map(),
      computeTypeOptions: [],
      computeTypeOptionsBase: [],
      computeTypeOptionsTime: [],
      ruleConditionTypeOptions: [],
      ruleWhereTypeOptions: [],
      timeTypeOptions: [],
      actionProcessTypes: [],
      type: "add",
      tempWhereType: '',
      dataForm: {
        enableTimeRange: ['00:00:00', '23:59:59'],
        warningRuleDO: {},
        whereList: [{pointCode: '',whereType: ''}],
        productCode: '',
        productName: '',
        whereType: '',
        timeRange: '',
        timeType: 'mi',
        actionProcessType: '0',
        priority: 1,
        actionProcessValue: 0,
        actionTimeType: 'mi',
        actionTimeValue: 0,
        equipmentList: []
      },
      computeTypeShow: false,
      tableData: [],
      page: {
        pageSize: 9,
        currentPage: 1,
        total: 0,
      },
      recordPage: {
        pageSize: 9,
        currentPage: 1,
        total: 0,
      },
    };
  },
  created() {
    let {id, type, warningType} = this.$route.query;
    this.type = type;
    let computeTypeOptionsAll = this.$store.state.dict.dictDatas.compute_type;
    this.recordDict = this.$store.state.dict.dictDatas.record_status
    computeTypeOptionsAll.forEach(item => {
      if (['1', '2', '8', '9', '10', '11'].includes(item.value)) {
        this.computeTypeOptionsBase.push(item);
        return;
      } else {
        this.computeTypeOptionsTime.push(item);
      }
    })
    //todo 场景编辑页面需要根据warningType进行显隐，目前仅支持条件类型
    this.dataForm.warningType = warningType;
    if (["edit"].includes(this.type)) {
      this.dataForm.id = id;
      this.title = "场景编辑";
      this.onLoad(this.page, {});
      this.initData();
    }
    if (["view"].includes(this.type)) {
      this.dataForm.id = id;
      this.readOnly = true;
      this.title = "场景查看";
      this.initData();
      this.onLoad(this.page, {})
    }
    const obj = Object.assign({}, this.$route, { title: this.title });
    this.$tab.updatePage(obj);

    // 获得用户列表
    listSimpleUsers().then(response => {
      this.users = response.data;
    })
  },
  mounted() {
    this.priorityDict = this.$store.state.dict.dictDatas.warning_priority;
    this.priorityMap = this.priorityDict.reduce((m, current) => {
      m.set(current.value * 1, current.label);
      return m;
    }, new Map());
    this.warningTypeMap.set("1", "条件触发");
    this.warningTypeMap.set("2", "定时触发");
    this.warningTypeMap.set("3", "手动触发");
    this.timeTypeOptions = this.$store.state.dict.dictDatas.time_type
    this.actionProcessTypes = this.$store.state.dict.dictDatas.action_process_type
    this.ruleConditionTypeOptions = this.$store.state.dict.dictDatas.rule_condition_type
    this.ruleWhereTypeOptions = this.$store.state.dict.dictDatas.rule_where_type
    const type = this.$route.query.type;
    if (type === "add") {
      this.$tab.updatePage(
        Object.assign({}, this.$route, { title: "配置新增" })
      );
    } else if (type === "edit") {
      this.$tab.updatePage(
        Object.assign({}, this.$route, { title: "配置编辑" })
      );
    } else {
      this.$tab.updatePage(
        Object.assign({}, this.$route, { title: "配置查看" })
      );
    }
  },
  methods: {
    // 将当前设备从该场景中移除
    handleTagClose(tag) {
      this.$confirm('确认移除设备：' + tag.equipmentName +' ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.dataForm.equipmentList.splice(this.dataForm.equipmentList.indexOf(tag.equipmentCode), 1)
        for (let i = 0; i < this.dataForm.equipmentName.length; i++) {
          if (this.dataForm.equipmentName[i].equipmentCode == tag.equipmentCode) {
            this.dataForm.equipmentName.splice(i, 1)
            break
          }
        }
        if (this.dataForm.equipmentList.length <= 0) {
          this.dataForm.productId = ""
          this.dataForm.equipmentList = []
          this.dataForm.productName = ""
        }
        this.$message.success("移除成功！")
      }).catch(() => {
        this.$message.info('移除失败');
      })
    },
    changeActionProcess() {
      this.dataForm.actionProcessValue = 0
      this.dataForm.actionTimeValue = 0
      this.dataForm.actionTimeType = 'mi'
    },
    handleClick(tab, event) {
      if (tab.name == 'record') {
        if (this.dataForm.warningType != '1') {
          this.recordPage.pageSize = 9
          this.recordPage.currentPage = 1
          // 告警记录分页
          this.onLoad(this.recordPage, {})
        }
      }
    },
    /** cron表达式按钮操作 */
    handleShowCron() {
      this.expression = this.dataForm.cron;
      this.openCron = true;
    },
    /** 确定后回传值 */
    crontabFill(value) {
      this.dataForm.cron = value;
    },
    forceUpdate() {
      this.$forceUpdate();
    },
    changeCompute(val) {
      this.computeTypeShow = false
      if (['8', '9', '10', '11'].includes(val)) {
        this.computeTypeShow = true
      }
    },
    whereRemove(index) {
      this.dataForm.whereList.splice(index, 1);
      this.$forceUpdate();
    },
    whereAdd(index) {
      this.dataForm.whereList.splice(index + 1, 0, {});
      this.$forceUpdate();
    },

    handleClose(tag,prop) {
      this.message[prop].splice(this.message[prop].indexOf(tag), 1);
    },

    showInput(prop) {
      for (let i = 0; i < this.configData.length; i++) {
        if (this.configData[i].prop==prop){
          this.$set(this.configData[i],'isShow',!this.configData[i].isShow)
          //初始化值
          this.$set(this.tagValueList,prop,'')

        }
      }

      //this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs[prop][0].$refs.input.focus();
      });
    },

    handleInputConfirm(prop) {
      if (!this.message[prop] || this.message[prop]==""){
        this.message[prop]=[]
      }

      if (this.tagValueList[prop]!=="") {
        this.message[prop].push(this.tagValueList[prop]);
      }
      for (let i = 0; i < this.configData.length; i++) {
        if (this.configData[i].prop==prop){
          this.$set(this.configData[i],'isShow',false)
        }
      }
      this.tagValueList[prop] = '';
    },

    usersChange(val){
    },


    isShow(row){
      let arr = ['MQTT','HTTP']
      return  arr.indexOf(row.outputWay)<0
    },

    computeTypeTool(whereType) {
      let dataList = []
      if (whereType == 1) {
        let dataObjectGeneral = {}
        let dataObject = {}
        dataObjectGeneral.label = "普通"
        dataObjectGeneral.options = []
        dataObject.label = "比较"
        dataObject.options = []
        this.computeTypeOptionsBase.forEach(item => {
          if (['1', '2'].includes(item.value)) {
            dataObjectGeneral.options.push(item)
          } else {
            dataObject.options.push(item)
          }
        })
        dataList.push(dataObjectGeneral)
        dataList.push(dataObject)
      } else {
        let dataObject = {}
        dataObject.label = '聚合'
        dataObject.options = this.computeTypeOptionsTime
        dataList.push(dataObject)
      }
      this.computeTypeOptions = dataList
    },
    submitWhereType(whereType) {
      if (this.tempWhereType != whereType) {
        this.dataForm.whereList = [{pointCode: '',whereType: ''}]
      }
      this.dataForm.whereType = whereType;
      //对计算类型进行过滤, 普通条件:当前值,上一次值   窗口条件:其余聚合值
      this.computeTypeTool(whereType)
      // 处理属性或时间的下拉框
      this.pointOptions = []
      let pointType = 'pointStats'
      if (this.dataForm.whereType == '3') {
        pointType = 'pointIncident'
      } else {
        pointType = 'pointStats'
      }
      if (this.dataForm.equipmentList.length == 1) {
        // 单选设备，查询设备下的属性
        getEquipmentPointList({equipmentCode: this.dataForm.equipmentList[0],  pointType: pointType}).then(res => {
          const data = res.data.map(item => {
            return {
              source: item.source,
              label: item.pointName,
              value: item.pointCode,
            }
          })
          let productPoint = {}
          let equipmentPoint = {}
          productPoint.label = "产品"
          productPoint.options = []
          equipmentPoint.label = "设备"
          equipmentPoint.options = []
          data.forEach(item => {
            if (item.source == 'product') {
              productPoint.options.push(item)
            } else {
              equipmentPoint.options.push(item)
            }
          })
          if (productPoint.options.length > 0) {
            this.pointOptions.push(productPoint)
          }
          if (equipmentPoint.options.length > 0) {
            this.pointOptions.push(equipmentPoint)
          }
        })
      }
      if (this.dataForm.equipmentList.length > 1) {
        //根据选择的产品,重新渲染用户可选的点位列表
        getProductPointList({productId: this.dataForm.productId, pointType: pointType}).then((res) => {
          const data = res.data;
          let pointOption = {}
          pointOption.label = "产品"
          pointOption.options = data.map(item => {
            return {
              label: item.pointName,
              value: item.pointCode,
            }
          })
          this.pointOptions.push(pointOption)
        })
      }

      this.$forceUpdate();
    },

    showWhereType() {
      this.tempWhereType = this.dataForm.whereType
      this.$refs.whereTypeDialog.openDialog(this.dataForm.whereType);
    },
    saveConfig(){
      this.actionData.map(item=>{
        if (item.id==this.message.id){
          let relatedParameter = JSON.parse(item.relatedParameter)

          this.setConfigDataReverse(relatedParameter)
          item.relatedParameter=JSON.stringify(relatedParameter)
        }
      })
      this.dialogVisible=false
    },
    setConfigDataReverse(relatedParameter){
      if (this.configData.length>0){
        for (let i = 0; i < this.configData.length; i++) {
          relatedParameter[this.configData[i].prop]=this.message[this.configData[i].prop]
        }
      }
    },
    //从dataForm中获取配置参数 构建并设置 relatedParameter 到 dataForm
    getConfigData(channelType,relatedParameter){
      switch (channelType) {
        case 0 :{
          return systemMessageData
        }
        //打电话
        case 1:
        {
          if (relatedParameter.callProvider=="aliyun"){
            return  aliyunConfigData
          }
          if (relatedParameter.callProvider=="tencent"){
            return tencentConfigData
          }
        }
          break
        case 2:
        {
          if (relatedParameter.smsProvider=="aliyun"){
            return  aliyunConfigData
          }
          if (relatedParameter.smsProvider=="tencent"){
            return tencentConfigData
          }
        }
          break
        case 3: {
          return mailConfigData
        }
          break
        case 4:
        case 5:
        case 6:
        default :
          return []
      }
    },
    setConfigData(relatedParameter){
      if (this.configData.length>0){
        for (let i = 0; i < this.configData.length; i++) {
          //设置响应式  set  get
          if (!relatedParameter[this.configData[i].prop]){
            this.$set(this.message,this.configData[i].prop,"")
            //this.message[this.configData[i].prop] = ""
          }else {
            this.$set(this.message,this.configData[i].prop,relatedParameter[this.configData[i].prop])
            //this.message[this.configData[i].prop] = relatedParameter[this.configData[i].prop]
          }
        }

      }
    },
    viewConfig(row){
      if (row.actionType == "1") {
        this.templateAddShow = true
        this.$nextTick(() => {
          this.$refs.templateAdd.viewById(row)
        })
      }
      if (row.actionType == "3") {
        this.$refs.actionAddDialog.viewPoint(row.actionId)
      }
    },
    editConfig(row) {
      let relatedParameter = JSON.parse(row.relatedParameter)
      this.configData = this.getConfigData(Number(row.outputWay == null?row.actionType:row.outputWay),relatedParameter)
      this.setConfigData(relatedParameter)
      //this.message.users=relatedParameter.users
      this.message.id=row.id
      //初始化tag输入框值
      this.inputVisible=false
      this.dialogVisible=true
    },
    restOnLoad() {
      this.onLoad(this.page, {});
    },
    actionLog(row) {
      this.$refs.actionLog.init(row)
    },
    warningLog(row) {
      this.$refs.warningLog.init(row);
    },
    // 点击处理日志按钮
    handleLog(row) {
      this.$refs.recordLog.init(row);
    },
    handle(row) {
      this.$refs.recordStatus.init(row);
    },
    parseTimeToSeconds(timeStr) {
      const [hours, minutes, seconds] = timeStr.split(':').map(Number);
      return new Date(
        new Date().getFullYear(),
        new Date().getMonth(),
        new Date().getDate(),
        hours,
        minutes,
        seconds
      );
    },
    initData() {
      detail(this.dataForm.id).then((res) => {
        this.dataForm = res.data;
        if (!this.dataForm.equipmentList) {
          // 重新初始化场景配置 - 重新选择适用设备和触发条件
          this.dataForm.equipmentList = []
          this.dataForm.productId = ''
          this.dataForm.productName = ''
          this.dataForm.productCode = ''
          this.dataForm.whereList = [{pointCode: '',whereType: ''}]
          this.dataForm.whereType = ''
        } else {
          //初始化可选值类型列表，当前值，上一次值等
          this.computeTypeOptions = this.dataForm.whereType == '1' ? this.computeTypeOptionsBase : this.computeTypeOptionsTime;
        }
        this.$set(this.dataForm, 'enableTimeRange', [this.dataForm.beginTime, this.dataForm.endTime]);
        if (!this.dataForm.whereList) {
          this.dataForm.whereList= [{whereType: ''}];
        }
        this.actionData = res.data.actionDOList
        this.equipmentShow = false;
        this.pointShow = false;
        this.computeTypeTool(this.dataForm.whereType)
      }).then(() => {
        this.pointOptions = []
        let pointType = 'pointStats'
        if (this.dataForm.whereType == '3') {
          pointType = 'pointIncident'
        } else {
          pointType = 'pointStats'
        }
        if (this.dataForm.equipmentList.length == 1) {
          // 单选设备，查询设备下的属性
          getEquipmentPointList({equipmentCode: this.dataForm.equipmentList[0],  pointType: pointType}).then(res => {
            const data = res.data.map(item => {
              return {
                source: item.source,
                label: item.pointName,
                value: item.pointCode,
              }
            })
            let productPoint = {}
            let equipmentPoint = {}
            productPoint.label = "产品"
            productPoint.options = []
            equipmentPoint.label = "设备"
            equipmentPoint.options = []
            data.forEach(item => {
              if (item.source == 'product') {
                productPoint.options.push(item)
              } else {
                equipmentPoint.options.push(item)
              }
            })
            if (productPoint.options.length > 0) {
              this.pointOptions.push(productPoint)
            }
            if (equipmentPoint.options.length > 0) {
              this.pointOptions.push(equipmentPoint)
            }
          })
        }
        if (this.dataForm.equipmentList.length > 1) {
          //根据选择的产品,重新渲染用户可选的点位列表
          getProductPointList({productId: this.dataForm.productId, pointType: pointType}).then((res) => {
            const data = res.data;
            let pointOption = {}
            pointOption.label = "产品"
            pointOption.options = data.map(item => {
              return {
                label: item.pointName,
                value: item.pointCode,
              }
            })
            this.pointOptions.push(pointOption)
          })
        }
      });
    },

    headCancel() {
      this.$router.back();
    },
    headSave() {
      if (!this.dataForm.warningName) {
        this.$message.warning("请输入场景名称");
        return
      }
      if (this.dataForm.warningType != '3' && !this.dataForm.enableTimeRange) {
        this.$message.warning("请选择生效时间段");
        return;
      }
      if (this.dataForm.warningType == '1' && (!this.dataForm.productId || !this.dataForm.equipmentList)) {
        this.$message.warning("请选择适用设备");
        return;
      }
      //条件类型必选校验
      if (this.dataForm.warningType == '1' && !this.dataForm.whereType) {
        this.$message.warning("请选择条件类型");
        return;
      }
      if (this.dataForm.warningType == '1') {
        for (let i = 0; i < this.dataForm.whereList.length; i++) {
          if (!this.dataForm.whereList[i].pointCode) {
            this.$message.warning("请配置触发条件");
            return
          }
        }
        // 动作收敛校验
        if (this.dataForm.actionProcessType == 1) {
          if (!this.dataForm.actionTimeValue) {
            this.$message.warning("请输入正整数的收敛时间")
            return
          } else if (!(/^[1-9]\d*$/.test(this.dataForm.actionTimeValue))) {
            this.$message.warning("请输入正整数的收敛时间")
            return
          }
          if (!this.dataForm.actionTimeType) {
            this.$message.warning("请选择收敛时间类型")
            return
          }
          if (!this.dataForm.actionProcessValue) {
            this.$message.warning("请输入正整数的收敛次数")
            return
          } else if (!(/^[1-9]\d*$/.test(this.dataForm.actionProcessValue))) {
            this.$message.warning("请输入正整数的收敛次数")
            return
          }
        }
      }
      this.dataForm.beginTime = this.dataForm.enableTimeRange[0];
      this.dataForm.endTime = this.dataForm.enableTimeRange[1];
      this.dataForm.actionDOList = this.actionData;
      this.$refs["baseForm"].validate((valid) => {
        if (valid) {
          this.$loading();
          submit(this.dataForm)
            .then((res) => {
              this.$message.success("操作成功");
              this.type = "edit";
              this.dataForm.id = res.data.id;
              this.initData();
            })
            .finally(() => {
              this.$loading().close();
            });
        }
      });
    },
    //执行日志查询
    handleSearch() {
      this.page.currentPage = 1
      if (this.formInline.createTime) {
        this.formInline.createTimes = this.formInline.createTime.join(",")
      }
      this.onLoad(this.page)
    },
    //执行日志查询
    handleEmpty() {
      this.formInline = {}
      this.onLoad(this.page)
    },
    // 选择适用设备集合后回调 productCode是选择的产品编码，使用这个编码查询可供选择的点位，list是设备编码集合的数组
    selectEquipment(productId, productCode, productName, list, equipmentName) {
      this.dataForm.productId = productId;
      this.dataForm.productCode = productCode;
      this.dataForm.productName = productName;
      this.dataForm.equipmentList = list;
      this.dataForm.equipmentName = equipmentName
      this.pointOptions = []
      let pointType = 'pointStats'
      if (this.dataForm.whereType == '3') {
        pointType = 'pointIncident'
      } else {
        pointType = 'pointStats'
      }
      if (this.dataForm.equipmentList.length == 1) {
        // 单选设备，查询设备下的属性
        getEquipmentPointList({equipmentCode: list[0],  pointType: pointType}).then(res => {
          const data = res.data.map(item => {
            return {
              source: item.source,
              label: item.pointName,
              value: item.pointCode,
            }
          })
          let productPoint = {}
          let equipmentPoint = {}
          productPoint.label = "产品"
          productPoint.options = []
          equipmentPoint.label = "设备"
          equipmentPoint.options = []
          data.forEach(item => {
            if (item.source == 'product') {
              productPoint.options.push(item)
            } else {
              equipmentPoint.options.push(item)
            }
          })
          if (productPoint.options.length > 0) {
            this.pointOptions.push(productPoint)
          }
          if (equipmentPoint.options.length > 0) {
            this.pointOptions.push(equipmentPoint)
          }
        })
      }
      if (this.dataForm.equipmentList.length > 1) {
        //根据选择的产品,重新渲染用户可选的点位列表
        getProductPointList({productId: productId, pointType: pointType}).then((res) => {
          const data = res.data;
          let pointOption = {}
          pointOption.label = "产品"
          pointOption.options = data.map(item => {
            return {
              label: item.pointName,
              value: item.pointCode,
            }
          })
          this.pointOptions.push(pointOption)
        })
      }
      this.$forceUpdate();
    },

    // 选择适用设备
    handleEquipment() {
      let data = this.dataForm
      if (this.type != 'add') {
        this.$refs.equipmentList.init(data.productId, data.productCode, data.productName, data.equipmentList, data.equipmentName);
      } else {
        this.$refs.equipmentList.init('', '', '', []);
      }
    },

    handleSelectionChange(selection) {
      this.selectionList = selection;
    },
    // 保存动作类型
    saveAction(list) {
      // list.forEach(item => {
      //   item.actionId = item.id
      // })
      this.actionData = list;
      this.actionDialog = false;
    },
    rowDeleteAction(row) {
      this.$confirm("确定将选择数据删除", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.actionData = this.actionData.filter(
          (item) => !(row.actionId == item.actionId)
        )
      })
    },

    // 新增执行动作
    handleAddAction() {
      // 点击出现动作列表页
      // this.$refs.actionList.init(this.actionData);
      this.$refs.executionAction.init(this.actionData);
    },

    recordChangeCurrent(val) {
      this.recordPage.currentPage = val;
      this.onLoad(this.page, {});
    },
    recordChangeSize(val) {
      this.recordPage.pageSize = val;
      this.onLoad(this.page, {});
    },
    // 场景记录分页查询
    onLoad(page, params = {}) {
      this.recordPage = page;
      this.tableLoading = true;
      params.configId = this.dataForm.id;
      getPage(
        page.currentPage,
        page.pageSize,
        Object.assign(params, this.formInline)
      ).then((res) => {
        const data = res.data;
        this.recordPage.total = data.total;
        this.recordData = data.records;
        this.tableLoading = false;
      });
    },
  },
};
</script>
<style scoped lang="scss">
.card {
  font-size: 14px;
  padding-bottom: 10px;
  margin-right: 10px;
  margin-bottom: 10px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
}

.card:hover {
  -webkit-box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  box-shadow: 0 20px 12px 0 rgba(0, 0, 0, 0.1);
}

.record-button ::v-deep .el-button {
  width: 95%;
}

.warning-card ::v-deep .el-card__body {
  padding: 0 !important;
}

.warning-card ::v-deep .el-divider--horizontal {
  margin: 0;
}

.record-danger {
  background: linear-gradient(40deg, #fff, #fef0f0, #fde2e2);
}

.record-success {
  background: linear-gradient(40deg, #fff, #eef6ff, #b3d9ff);;
}

.record-info {
  background: linear-gradient(40deg, #fff, #f4f4f5, #e9e9ed);
}

.condition .el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 140px;
  margin-left: 10px;
  vertical-align: bottom;
}

.text-hover:hover {
  color: #1890ff;
  cursor: pointer;
}

.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
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

.inputBox {
  margin: 0 12px;
  cursor: pointer;
}

.conditionBox {
  margin: 0 45px;
  height: 50px;
}

.el-tag {
  height: 56px;
  line-height: 54px;
  font-size: 16px;
  padding: 0 14px;
}

.equipment .el-tag {
  height: 35px;
  line-height: 35px;
  font-size: 16px;
  padding: 0 14px;
  margin-right: 10px;
}

.equipment .equipment-name {
  margin-bottom: 10px;
}
</style>
