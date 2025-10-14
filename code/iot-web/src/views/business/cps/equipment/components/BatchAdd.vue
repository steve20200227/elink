<template>
  <div class="warning_dialog">
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="25%"
      @close="cancel">
      <div class="dialog-form">
        <el-form :model="deviceForm" ref="ruleForm" :rules="rules" label-width="120px">
          <el-form-item label="设备编码前缀：" prop="equipmentCodePrefixes">
            <el-input v-model="deviceForm.equipmentCodePrefixes" placeholder="请输入设备编码前缀">
            </el-input>
          </el-form-item>
          <el-form-item label="设备编码后缀：" prop="equipmentCodeSuffix">
            <el-input v-model="deviceForm.equipmentCodeSuffix" placeholder="请输入设备编码后缀">
            </el-input>
          </el-form-item>
          <el-form-item label="设备名称前缀：" prop="equipmentNamePrefixes">
            <el-input v-model="deviceForm.equipmentNamePrefixes" placeholder="请输入设备名称前缀">
            </el-input>
          </el-form-item>
          <el-form-item label="设备名称后缀：" prop="equipmentNameSuffix">
            <el-input v-model="deviceForm.equipmentNameSuffix" placeholder="请输入设备名称后缀">
            </el-input>
          </el-form-item>
          <el-form-item label="关联产品：" prop="productName">
            <el-input v-model="deviceForm.productName" size="small" placeholder="请选择产品" readonly="true">
              <el-button slot="append" icon="el-icon-search" @click.native="headSelectProduct"></el-button>
            </el-input>
          </el-form-item>
          <el-form-item label="新增数量：" prop="equipmentNum">
            <el-input-number v-model="deviceForm.equipmentNum" :min="1" :max="100" controls-position="right" placeholder="请输入新增数量" style="width: 100%;"></el-input-number>
          </el-form-item>

        </el-form>
      </div>

      <div style="text-align: center;">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" type="primary" @click="submit">确 定</el-button>
      </div>
    </el-dialog>

    <!--  选择产品  -->
    <SelectProductDialog
      ref="productDialog"
      @select-product="selectProduct"
    ></SelectProductDialog>
  </div>
</template>

<script>
import SelectProductDialog from "@/views/business/cps/equipment/components/SelectProductDialog.vue";
import {getEquipmentOptions, saveBatch} from "@/api/cps/equipment-api";

export default {
  name: "add",
  components: {SelectProductDialog},
  data() {
    return {
      title: '批量新增',
      dialogFlag: false,
      iotCode: '',
      deviceForm: {
        equipmentCodePrefixes: '',
        equipmentCodeSuffix: '',
        equipmentNamePrefixes: '',
        equipmentNameSuffix: '',
        isEnable: 0,
        isHistory: "0",
        location: ''
      }, // 批量新增表单数据
      equipmentOptions: [],
      fileList: [],
      rules: {
        equipmentCodePrefixes: [
          { max: 20, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
        ],
        equipmentCodeSuffix: [
          { max: 20, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
        ],
        productName: [
          { required: true, message: "请点击选择产品按钮", trigger: ["blur", "change"], }
        ],
        equipmentNamePrefixes: [
          { max: 20, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
        ],
        equipmentNameSuffix: [
          { max: 20, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
        ],
        equipmentNum: [
          { required: true, message: "请输入新增数量", trigger: ["blur", "change"], },
          { pattern: /^\d+$/, message: "请输入数字", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getConfigKey("iot").then((res) => {
      this.iotCode = res.data;
    })
  },
  methods: {
    // 选择产品后，更新表单数据
    selectProduct(data) {
      if (data.image) {
        this.$set(this.deviceForm, 'image', data.image)
        let dataObject = {
          url: data.image
        }
        this.fileList = [];
        this.fileList.push(dataObject)
      } else {
        this.deviceForm.image = ''
      }
      this.$set(this.deviceForm, 'agreementType', data.agreementType)
      this.$set(this.deviceForm, 'productCode', data.productCode)
      this.$set(this.deviceForm, 'productName', data.productName)
      this.$set(this.deviceForm, 'equipmentType', data.equipmentType)
      this.$set(this.deviceForm, 'image', data.image)
      // EMQX客户端认证，默认使用产品带过来的。
      this.$set(this.deviceForm, 'userAccount', data.userAccount)
      this.$set(this.deviceForm, 'userPassword', data.userPassword)

      if (data.equipmentType === '子设备') {
        //设备关联的产品变动后，重置该设备关联的父设备信息
        this.$set(this.deviceForm, 'parentEquipment', '')
        this.$nextTick(() => {
          this.getEquipmentOptions();
        })
      }
    },
    getEquipmentOptions() {
      getEquipmentOptions(this.deviceForm.productCode).then((res) => {
        this.equipmentOptions = res.data;
      })
    },
    // 选择产品
    headSelectProduct() {
      this.$refs.productDialog.openDialog();
    },
    cancel () {
      this.$refs["ruleForm"].clearValidate();
      this.dialogFlag = false;
    },
    openDialog() {
      this.deviceForm = {
        equipmentCodePrefixes: '',
        equipmentCodeSuffix: '',
        equipmentNamePrefixes: '',
        equipmentNameSuffix: '',
        isEnable: 0,
        isHistory: "0",
        location: ''
      }
      this.dialogFlag = true
    },
    submit() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          const equipmentCodePrefixes = this.deviceForm.equipmentCodePrefixes;
          const equipmentCodeSuffix = this.deviceForm.equipmentCodeSuffix;
          const regex = /^[a-zA-Z0-9][a-zA-Z0-9_]*$/;
          if (equipmentCodePrefixes && !regex.test(equipmentCodePrefixes)) {
            this.$message.warning("设备编码前缀只能以字母开头，只能包含字母、数字和下划线!");
            return;
          }
          if (equipmentCodeSuffix && !regex.test(equipmentCodeSuffix)) {
            this.$message.warning("设备编码后缀只能以字母开头，只能包含字母、数字和下划线!");
            return;
          }
          //后端按真实设备编码直接替换掉"equipmentCode"
          this.deviceForm.attributePush = this.iotCode + '/' + this.deviceForm.productCode + '/' + "equipmentCode" + '/attributePush'
          this.deviceForm.eventPush = this.iotCode + '/' + this.deviceForm.productCode + '/' + "equipmentCode" + '/eventPush'
          this.deviceForm.featureIssued = this.iotCode + '/' + this.deviceForm.productCode + '/' + "equipmentCode" + '/featureIssued'

          saveBatch(this.deviceForm).then(res => {
            const {data, code, msg} = res
            if (code == 0) {
              this.$message.success('批量新增成功')
              // 关闭新增弹窗
              this.$emit("add-success")
              this.cancel()
            } else {
              this.$message.warning(msg)
            }
          })
        }
      })
    }
  }
}
</script>
<style>
.dialog-form {
  margin-left: 25px;
  margin-right: 30px;
}

.el-input-number .el-input__inner{
  text-align: left;
}
</style>
