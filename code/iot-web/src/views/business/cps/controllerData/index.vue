<template>
  <div class="dashboard-editor-container">
    <panel-group
      ref="panelGroup"
      :countList="countList"
      @handleSetLineChartData="handleSetLineChartData" />

    <el-row :gutter="20">
      <el-col :span="12" >
        <div class="chart-wrapper">
          <bar-chart ref="barChart" />
        </div>
      </el-col>
      <el-col :span="12" >
        <div class="chart-wrapper">
          <pie-template-chart ref="pipTemplateChart" />
        </div>
      </el-col>
      <el-col :span="12" >
        <div class="chart-wrapper">
          <pie-app-chart ref="pipAppChart" />
        </div>
      </el-col>
      <el-col :span="12" >
        <div class="chart-wrapper">
          <pie-push-user-chart ref="pipPushUserChart" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PanelGroup from '@/views/business/cps/controllerData/component/PanelGroup.vue'
import BarChart from '@/views/business/cps/controllerData/component/BarChart.vue'
import PieTemplateChart from '@/views/business/cps/controllerData/component/PieTemplateChart.vue'
import PieAppChart from '@/views/business/cps/controllerData/component/PieAppChart.vue'
import PiePushUserChart from '@/views/business/cps/controllerData/component/PiePushUserChart.vue'
import {getCpsCount, getEquipmentRelateWarning} from "@/api/home/home-api";

export default {
  name: 'index',
  components: {
    PanelGroup,
    BarChart,
    PieTemplateChart,
    PieAppChart,
    PiePushUserChart
  },
  data() {
    return {
      lineChartData: {},
      countList: [], // 头部数据
      equipmentAgreementList: [], // 设备接入协议
      productREquipmentList: [], // 产品关联设备统计
      gatewayRelateEquipment: [], // 驱动关联设备统计
      equipmentRelateWarningList: [], // 设备关联告警统计
    }
  },
  async mounted() {
    await getCpsCount().then(res => {
      const data = res.data
      this.countList.productEnabelCount = data.productEnabelCount
      this.countList.productDisableCount = data.productDisableCount
      this.countList.equipmentEnableCount = data.equipmentEnableCount
      this.countList.equipmentDisableCount = data.equipmentDisableCount
      this.countList.driveEnableCount = data.driveEnableCount
      this.countList.driveDisableCount = data.driveDisableCount
      this.countList.driveCount = data.driveCount
      this.countList.equipmentCount = data.equipmentCount
      this.countList.productCount = data.productCount
      this.countList.passageCount = data.passageCount
      this.equipmentAgreementList = data.equipmentType
      this.productREquipmentList = data.productREquipmentCount
      this.gatewayRelateEquipment = data.gatewayRelateEquipment
    })

    this.$refs.panelGroup.init(this.countList)
  },
  methods: {
    handleSetLineChartData(type) {
      // this.lineChartData = lineChartData[type]
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 20px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 20px;
    border-radius: 12px;
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}


</style>
