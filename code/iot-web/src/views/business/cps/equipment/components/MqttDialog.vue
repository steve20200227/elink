<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="60%"
      @close="cancel">
      <div style="margin: 10px 0">
        <el-form :rules="rules" ref="ruleForm" :model="dataForm" :before-close="disableClose" label-width="100px"
                 class="product-form">
          <el-row>
            <el-col :span="12">
              <el-form-item label="点位编码:" prop="pointCode">
                <el-input v-model="dataForm.pointCode" :disabled="type === 'view'" size="small" placeholder="请输入点位编码"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="点位名称:" prop="pointName">
                <el-input v-model="dataForm.pointName" :disabled="type === 'view'" size="small" placeholder="请输入点位名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单位名称:" prop="unit" v-if="pointType === 'pointStats'">
                <el-input v-model="dataForm.unit" :disabled="type === 'view'" size="small" placeholder="请输入单位名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="输入参数:" prop="inputParameter" v-if="pointType === 'pointFeature'">
                <el-input v-model="dataForm.inputParameter" :disabled="type === 'view'" size="small" placeholder="请输入输入参数"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="输出参数:" prop="outputParameter" v-if="pointType === 'pointIncident'">
                <el-input v-model="dataForm.outputParameter" :disabled="type === 'view'" size="small" placeholder="请输入输出参数"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="数据类型:" prop="dataType" v-if="pointType === 'pointStats'">
                <el-select v-model="dataForm.dataType" :disabled="type === 'view'" style="width: 100%;" size="small" placeholder="请选择数据类型">
                  <el-option
                    v-for="item in dataTypeDict"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="属性字典:" prop="pointDict">
                <el-select v-model="dataForm.pointDict" clearable :disabled="type === 'view'" style="width: 100%;" size="small" placeholder="请选择属性字典">
                  <el-option
                    v-for="item in pointDictList"
                    :key="item.type"
                    :label="item.name"
                    :value="item.type">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否启用:" prop="isEnable">
                <el-switch
                  :disabled="type === 'view'"
                  v-model="dataForm.isEnable"
                  active-text="已启用"
                  :active-value="1"
                  :inactive-value="0"
                  inactive-text="未启用">
                </el-switch>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>

      <div style="float: right">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" v-if="type !== 'view'" @click="savePoint" type="primary">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// TODO 重新引用
import {submit, detail} from '@/api/cps/equipment-point-api'
import { listType } from "@/api/system/dict/type";

export default {
  name: "MqttDialog",
  data() {
    return {
      pointType: '',
      type: 'edit',
      title: '点位信息',
      dialogFlag: false,
      dataForm: {},
      // 判断写指令是否必填
      isWrite: true,
      dataTypeDict: [],
      pointDictList: [], // 属性字典
      rules: {
        pointCode: [
          {
            required: true,
            message: "请输入点位编码",
            trigger: 'blur'
          },
        ],
        pointName: [
          {
            required: true,
            message: "请输入点位名称",
            trigger: 'blur'
          }
        ],
        unit: [
          {
            required: false,
            message: "请输入单位名称",
            trigger: 'blur'
          }
        ],
        dataType: [
          {
            required: true,
            message: "请选择数据类型",
            trigger: 'blur'
          }
        ],
        inputParameter: [
          {
            required: true,
            message: "请输入输入参数",
            trigger: 'blur'
          }
        ],
        outputParameter: [
          {
            required: true,
            message: "请输入输出参数",
            trigger: 'blur'
          }
        ]
      },
      queryParams: {
        pageNo: 1,
        pageSize: 100,
        type: "point"
      }
    }
  },
  mounted() {
    this.dataTypeDict = this.$store.state.dict.dictDatas.attribute_data_type
    listType(this.queryParams).then(res => {
      this.pointDictList = res.data.list
    })
  },
  methods: {
    disableClose() {
      this.dataForm = {};
      this.dialogFlag = false;
    },
    addPoint(id, pointType) {
      this.dataForm.quantity = 1
      this.dataForm.equipmentId = id;
      this.dialogFlag = true;
      this.pointType = pointType;
    },
    editPoint(data) {
      if (data.type) {
        this.type = data.type;
      }
      this.pointType = data.pointType;
      this.dialogFlag = true;
      this.getDetail(data.id)
      if (this.dataForm.writeInstruction === -1) {
        this.dataForm.writeInstruction = "";
      }
    },
    savePoint() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.dataForm.pointType = this.pointType;
          this.dataForm.agreementType = 'MQTT';
          submit(this.dataForm).then(res => {
            const {msg} = res;
            if (res.code === 0) {
              this.$message.success("操作成功")
              this.cancel();
            } else {
              this.$message.warning(msg);
            }
          })
        }
      })
    },
    cancel() {
      this.dialogFlag = false;
      this.dataForm = {};
      this.$refs['ruleForm'].clearValidate();
      this.$emit('restOnLoad');
    },
    getDetail(id) {
      detail(id).then(res => {
        this.dataForm = res.data;
      })
    },
  }
}
</script>

<style scoped>
.product-form ::v-deep .el-form-item__label {
  color: #3f4448;
  font-weight: 400;
}
</style>
