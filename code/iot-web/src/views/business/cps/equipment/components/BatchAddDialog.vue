<template>
  <div class="product-dialog">
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="40%"
      @close="cancel"
    >

      <el-form ref="formLayout" :rules="rules" label-width="90px" :model="dataForm" size="small" class="product-form">
        <el-form-item label="设备名称:" prop="equipmentName">
          <el-input
            v-model="dataForm.equipmentName"
            :readonly="true"
            @click.native="handleProduct"
            placeholder="请点击选择设备">
          </el-input>
        </el-form-item>
        <el-form-item label="产品名称:" prop="productName">
          <el-input
            v-model="dataForm.productName"
            :readonly="true"
            @click.native="handleDevice"
            placeholder="请输入点击选择产品">
          </el-input>
        </el-form-item>
      </el-form>

      <div style="float: right">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" @click="determine" type="primary">确 定</el-button>
      </div>
    </el-dialog>

    <!--  选择设备  -->
    <common-dialog
      ref="assetDialog"
      @select-data="selectData">
    </common-dialog>

    <!--  选择产品  -->
    <SelectProductDialog
      ref="productDialog"
      @select-product="selectProduct"
    ></SelectProductDialog>
  </div>
</template>

<script>
import { batchSubmit } from '@/api/cps/equipment-api'
import CommonDialog from "@/components/CommonDialog/index.vue";
import SelectProductDialog from "@/views/business/cps/equipment/components/SelectProductDialog";

export default {
  name: "add",
  props: {
    disabledList: {
      default: () => ([]),
      type: Array,
    },
  },
  components: {
    SelectProductDialog,
    CommonDialog
  },
  data() {
    return {
      title: '批量新增设备',
      dialogFlag: false,
      dataForm: {},
      selectAsset: false,
      rules: {
        equipmentName: [
          {
            required: true,
            message: "请点击选择设备",
            trigger: ["blur", "change"],
          },
        ],
        productName: [
          {
            required: true,
            message: "请点击选择产品",
            trigger: ["blur", "change"],
          },
        ]
      }
    }
  },
  methods: {
    handleProduct () {
      this.$refs.assetDialog.openDialog()
    },
    handleDevice () {
      this.$refs.productDialog.openDialog()
    },
    openDialog() {
      this.dialogFlag = true;
    },
    // getAssetData() {
    //   this.$refs.assetDialog.selectData();
    // },
    // 选择设备
    selectData(data) {
      // 清空数组，确保没有旧的值
      // this.dataForm.equipmentName = [];
      this.dataForm.equipmentList = [];
      var equipmentName = ''
      data.forEach(item => {
        // equipmentName.push(item.assetName)
        // this.dataForm.equipmentName.push(item.assetName);
        equipmentName += item.assetName
      })
      this.$set(this.dataForm, 'equipmentName', equipmentName)
      this.dataForm.equipmentList = data.map(item => {
        return {
          equipmentName: item.assetName,
          equipmentCode: item.assetCode,
          cabinCode: item.cabinCode,
        }
      })
    },
    // 选择产品
    selectProduct(data) {
      this.dataForm.productCode = data.productCode;
      this.dataForm.productId = data.id;
      this.$set(this.dataForm, 'productName', data.productName)
    },
    // 确定
    determine() {
      this.$refs["formLayout"].validate((valid) => {
        if (valid) {
          batchSubmit(this.dataForm).then(res => {
            const {msg} = res.data;
            if (res.data.code === 200) {
              this.$message.success("操作成功")
              this.cancel();
            } else {
              this.$message.warning(msg);
            }
          }).finally(() => {
            // 设置表单禁用
            this.$refs.formLayout.$refs.form.allDisabled = false;
          });
        }
      })
    },
    // 取消
    cancel() {
      this.dialogFlag = false;
      this.dataForm = {};
      this.$emit('restOnLoad');
    },
  }
}
</script>

<style scoped>
.product-form ::v-deep .el-form-item__label {
  color: #3f4448;
  font-weight: 400;
}

.product-dialog ::v-deep .el-dialog__body {
  padding: 20px 20px;
}

</style>
