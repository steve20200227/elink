<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="60%"
      height="80%"
      @close="cancel"
    >
      <template>
        <div class="center-container">
          <el-steps style="width: 100%; max-width: 800px" :active="active" align-center>
            <el-step title="选择产品"/>
            <el-step title="选择设备"/>
          </el-steps>
        </div>
      </template>
      <!--   第一步   产品选择  -->
      <div v-show="active == 0">
        <div style="margin-top: 5px; padding-top: 15px; border-top: 1px solid rgb(204, 204, 204);">
          <el-form ref="gridHeadLayout" :inline="true" :model="productForm" class="demo-form-inline">
            <el-form-item>
              <el-input
                v-model="productForm.productName"
                size="mini"
                placeholder="请输入产品名称">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="productForm.isEnable" size="mini" placeholder="请选择是否启用">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item class="top-button">
              <el-button size="mini" icon="el-icon-search" @click="handleProductSearch"></el-button>
              <el-button size="mini" icon="el-icon-refresh-right" @click="handleProductEmpty"></el-button>
            </el-form-item>
          </el-form>
        </div>

        <div>
          <div style="max-height: calc(100vh - 685px);height: calc(100vh - 685px); overflow: auto;" v-if="productData.length!=0">
            <el-row>
              <el-col :span="6" v-for="(item, index) in productData" :key="index">
                <el-card shadow="always" :class="{'isActive':item.id == product.id}" style="margin: 5px; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)" @click.native="selectProduct(item)">
                  <div style="position: relative; height: 100px;">
                    <span style="position:absolute; top: 10px">
                      <img style="height: 70px; width: 70px" v-if="item.image != null && item.image != ''" :src="item.image" />
                      <img style="height: 70px; width: 70px" v-else src="@/assets/productDefaultImg.png" />
                    </span>
                    <span style="position: absolute; top: 0; left: 80px; margin-left: 10px">
                      <strong style="font-size: 18px">{{ item.productName.length > 10 ? item.productName.substring(0, 10) + '...' : item.productName }}</strong>
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
                    <span style="position: absolute; top: 39px; left: 80px; margin-left: 10px;font-size: 14px">
                      <span style="color: #646a73">产品编码：</span>
                      <span style="color: #1677ff">{{ item.productCode }}</span>
                    </span>
                    <span style="position: absolute; top: 69px; left: 80px; margin-left: 10px;font-size: 14px">
                      <span style="color: #646a73">关联设备：</span>
                      <span style="color: #1677ff">{{ item.equipmentCount }}</span>
                    </span>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <el-pagination
            v-if="productData.length!=0"
            background
            :current-page="productPage.currentPage"
            :page-sizes="[8, 16, 32, 64, 128]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="productPage.total"
            :page-size="productPage.pageSize"
            :pager-count="5"
            @size-change="changeProductSize"
            @current-change="changeProductCurrent"
            style="float: right; margin: 20px 0;">
          </el-pagination>
          <el-empty
            v-if="productData.length==0"
            style="width: 100%; margin-top: 50px"
            description="暂无数据"
            :image-size="120"
          ></el-empty>
        </div>
      </div>
      <!--   第二步  设备选择   -->
      <div v-show="active == 1">
        <div style="margin-top: 5px; padding-top: 15px; border-top: 1px solid rgb(204, 204, 204);">
          <el-form ref="gridHeadLayout" :inline="true" :model="deviceForm" class="demo-form-inline">
            <el-form-item>
              <el-input
                v-model="deviceForm.equipmentName"
                size="mini"
                placeholder="请输入设备名称">
              </el-input>
            </el-form-item>
            <el-form-item class="top-button">
              <el-button size="mini" icon="el-icon-search" @click="handleDeviceSearch"></el-button>
              <el-button size="mini" icon="el-icon-refresh-right" @click="handleDeviceEmpty"></el-button>
            </el-form-item>
          </el-form>
        </div>
        <div>
          <div style="max-height: calc(100vh - 685px);height: calc(100vh - 685px); overflow: auto;" v-if="deviceData.length!=0">
            <el-row>
              <el-col :span="6" >
                <el-card shadow="always" @click.native="selectAllDevice()" :class="{'isActive':isAllDevice, 'record-success': true, 'record-danger': true}"
                         style="margin: 5px; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)">
                  <div style="position: relative; height: 100px;">
                    <span style="position:absolute; top: 10px">
                      <img style="height: 70px; width: 70px"  src="@/assets/equipmentDefaultImg.png" />
                      <!-- 添加更多条件根据需要显示不同的图片 -->
                    </span>
                    <span style="position: absolute; top: 0; left: 80px; margin-left: 10px">
                      <strong style="font-size: 18px">全选设备</strong>
                    </span>

                  </div>
                </el-card>
              </el-col>
              <el-col :span="6" v-for="(item, index) in deviceData" :key="index">
                <el-card shadow="always" @click.native="selectDevice(item)" :class="{'isActive':selectDeviceList.includes(item.equipmentCode), 'record-success': item.isEnable == 1, 'record-danger': item.isEnable == 0}"
                         style="margin: 5px; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)">
                  <div style="position: relative; height: 100px;">
                    <span style="position:absolute; top: 10px">
                      <!-- 根据 appType 的值显示不同的图片 -->
                      <img style="height: 70px; width: 70px" v-if="item.image != null && item.image != ''" :src="item.image" />
                      <img style="height: 70px; width: 70px" v-else src="@/assets/equipmentDefaultImg.png" />
                      <!-- 添加更多条件根据需要显示不同的图片 -->
                    </span>
                    <span style="position: absolute; top: 0; left: 80px; margin-left: 10px">
                      <strong style="font-size: 18px">{{ item.equipmentName.length > 10 ? item.equipmentName.substring(0, 10) + '...' : item.equipmentName }}</strong>
                    </span>
                    <span style="position: absolute; top: 39px; left: 80px; margin-left: 10px;font-size: 14px">
                      <span style="color: #646a73">设备编码：</span>
                      <span style="color: #1677ff">{{ item.equipmentCode }}</span>
                    </span>
                    <span style="position: absolute; top: 69px; left: 80px; margin-left: 10px;font-size: 14px">
                      <span style="color: #646a73">关联告警：</span>
                      <span style="color: #1677ff">{{ item.warningCount }}</span>
                    </span>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <el-empty
            v-if="deviceData.length==0"
            style="width: 100%; margin-top: 50px"
            description="暂无数据"
            :image-size="120"
          ></el-empty>
        </div>
      </div>
      <div class="dialog-footer">
        <el-button style="margin-top: 12px" v-if="active != 0" @click="previous">上一步</el-button>
        <el-button style="margin-top: 12px" v-if="active == 0" @click="next">下一步</el-button>
        <el-button style="margin-top: 12px" v-if="active != 0" type="primary" @click="selectEquipment">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page as getProductPage} from '@/api/cps/product-api'
import { pageByProductCode as getDevicePage } from '@/api/cps/equipment-api'

export default {
  name: "actionSelect",
  data() {
    return {
      tempProduct: '',    // 临时存储已选产品
      tempDeviceList: [], // 临时存储已选设备
      tempAllDeviceList: [], // 临时存储所有设备
      isAllDevice:false,
      product: {},
      options: [],
      tableLoading: false,
      title: '选择适用设备',
      productData: [],
      deviceData: [],
      productForm: {},
      deviceForm: {},
      dialogFlag: false,
      active: 0, // 步骤
      selectDeviceList: [],
      deviceList: [], // 设备List
      productPage: {
        pageSize: 8,
        currentPage: 1,
        total: 0
      },
    }
  },
  methods: {
    previous() {
      this.active--;
    },
    next() {
      if (!this.product.id) {
        this.$message.warning("请先选择产品");
        return;
      }
      if (this.tempProduct != this.product.id) {
        // 产品选择发生变化，清空已选设备
        this.selectDeviceList = []
        this.deviceList = []
        this.isAllDevice = false
        this.onLoadDevice(this.deviceForm);
      } else {
        this.tableLoading = true;
        // 产品选择没有发生变化 - 首先获取所有设备
        this.deviceForm.productCode = this.product.productCode;
        // 设备默认查询 “已启用”的设备
        this.deviceForm.isEnable = 1
        getDevicePage(this.deviceForm).then(res => {
          const data = res.data;
          // this.devicePage.total = data.total;
          this.deviceData = data;
          this.tableLoading = false;
          this.tempAllDeviceList = []
          for (let i = 0; i < data.length; i++) {
            this.tempAllDeviceList.push(data[i].equipmentCode)
          }
          if (this.tempAllDeviceList.length == this.selectDeviceList.length) {
            this.isAllDevice = true
          } else {
            this.isAllDevice = false
          }
        })
      }
      this.active++;
    },

    //选择产品后
    selectProduct(item) {
      this.product = item;
    },

    //选中设备
    selectDevice(item) {
      if (this.selectDeviceList.includes(item.equipmentCode)) {
        this.selectDeviceList.splice(this.selectDeviceList.indexOf(item.equipmentCode), 1);
        for (let i = 0; i < this.deviceList.length; i++) {
          if (this.deviceList[i].equipmentCode == item.equipmentCode) {
            this.deviceList.splice(i, 1)
            break
          }
        }
      } else {
        this.selectDeviceList.push(item.equipmentCode);
        this.deviceList.push(item)
      }
      if (this.tempAllDeviceList.length == this.selectDeviceList.length) {
        this.isAllDevice = true
      } else {
        this.isAllDevice = false
      }
    },
    //全选设备
    selectAllDevice(){
      this.isAllDevice = !this.isAllDevice
      if (this.isAllDevice){
        this.selectDeviceList = []
        this.deviceList = []
        if (this.deviceData.length > 1){
          this.deviceData.forEach(item => {
            this.selectDeviceList.push(item.equipmentCode)
            this.deviceList.push(item)
          })
        }else {
          this.deviceData.forEach(item=>{
            this.selectDeviceList.push(item.equipmentCode)
            this.deviceList.push(item)
          })
        }
      }else {
        this.selectDeviceList = []
        this.deviceList = []
      }
    },
    handleProductSearch () {
      this.productPage.currentPage = 1
      this.onLoadProduct(this.productPage, this.productForm)
    },
    handleProductEmpty () {
      this.formInline = {}
      this.onLoadProduct(this.productPage)
    },
    changeProductCurrent (val) {
      this.productPage.currentPage = val
      this.onLoadProduct(this.productPage, {})
    },
    changeProductSize (val) {
      this.productPage.pageSize = val
      this.onLoadProduct(this.productPage, {})
    },
    handleDeviceSearch() {
      this.onLoadDevice(this.deviceForm)
    },
    handleDeviceEmpty() {
      this.deviceForm = {}
      this.onLoadDevice({})
    },
    init(productId, productCode, productName, equipmentCode, equipmentName) {
      this.tempProduct = productId
      this.tempDeviceList = equipmentCode
      this.selectDeviceList = equipmentCode
      this.deviceList = equipmentName
      this.product.productCode = productCode
      this.product.productName = productName
      this.product.id = productId
      this.options = this.$store.state.dict.dictDatas.is_enable
      this.active = 0;
      this.dialogFlag = true
      this.productPage.currentPage = 1
      this.onLoadProduct(this.productPage, {})
    },
    // 选择设备
    selectEquipment(row) {
      if (this.selectDeviceList.length == 0) {
        this.$message.warning("请选择设备")
        return
      }
      this.dialogFlag = false
      this.$emit('selectEquipment', this.product.id, this.product.productCode, this.product.productName, this.selectDeviceList, this.deviceList)
    },
    indexMethod(index) {
      return index + 1
    },
    cancel() {
      this.dialogFlag = false;
    },
    onLoadProduct(page, params = {}) {
      this.productPage = page;
      this.tableLoading = true;
      getProductPage(
        page.currentPage,
        page.pageSize,
        Object.assign(params, this.productForm)
      ).then(res => {
        const data = res.data;
        this.productPage.total = data.total;
        this.productData = data.records;
        this.tableLoading = false;
      });
    },
    onLoadDevice(params) {
      this.tableLoading = true;
      this.deviceForm.productCode = this.product.productCode;
      // 设备默认查询 “已启用”的设备
      this.deviceForm.isEnable = 1
      getDevicePage(
        Object.assign(params, this.deviceForm)
      ).then(res => {
        this.deviceData = res.data;
        this.tableLoading = false;
      });
    },
    getAllDevice(){
      let params = {}
      this.deviceForm.productCode = this.product.productCode;
      // 设备默认查询 “已启用”的设备
      this.deviceForm.isEnable = 1
      getDevicePage(
        1,
        99999999,
        Object.assign(params, this.deviceForm)
      ).then(res => {
        const data = res.data;
        data.records.forEach(item =>{
          this.selectDeviceList.push(item.equipmentCode)
        });
      });
    }
  }
}
</script>

<style scoped>
.product-form ::v-deep .el-form-item__label {
  color: #3f4448;
  font-weight: 400;
}
.isActive {
  border: 1px solid #409EFF;
}
.record-danger {
  background: linear-gradient(40deg, #fff, #fef0f0, #fde2e2);
}

.record-success {
  background: linear-gradient(40deg, #fff, #eef6ff, #b3d9ff);;
}
.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: center;
  padding: 12px 0;
  border-top: 1px solid rgb(204, 204, 204);
}
.center-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 30px;
}
</style>
