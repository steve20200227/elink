<template>
  <div class="dashboard-editor-container">

    <panel-group
      ref="panelGroup"
      :countList="countList"/>

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:20px;border-radius: 12px;">
      <line-chart ref="lineChart" :chart-data="lineChartData" />
    </el-row>

    <el-row :gutter="20">
<!--        <el-col :span="8">-->
<!--        <div class="chart-wrapper">-->
<!--          <bar-chart ref="barChart" />-->
<!--        </div>-->
<!--      </el-col>-->
      <el-col :span="12">
        <div class="chart-wrapper">
          <pie-chart ref="ActionChart" />
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-wrapper">
          <raddar-chart ref="raddarChart" />
        </div>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import PanelGroup from '@/views/business/warning/controllerData/component/PanelGroup.vue'
import LineChart from '@/views/business/warning/controllerData/component/LineChart.vue'
import PieChart from "@/views/business/warning/controllerData/component/PieChart.vue";
import RaddarChart from '@/views/business/warning/controllerData/component/RaddarChart.vue'
import { getWarningCount } from '@/api/home/home-api'

export default {
  name: 'Index',
  components: {
    PanelGroup,
    LineChart,
    PieChart,
    RaddarChart
  },
  data() {
    return {
      lineChartData: {},
      countList: [],
      ACTION_TYPE: [],
      actionTypeList: {}, // 动作数据
      ruleList: {}, // 规则数据
    }
  },
  async mounted() {
    this.ACTION_TYPE = this.$store.state.dict.dictDatas.action_type
    await getWarningCount().then(res => {
      const data = res.data
      // 数量统计
      this.countList.actionCount = data.actionCount
      this.countList.configCount = data.configCount
      this.countList.recordCount = data.recordCount
      this.countList.configEnableCount = data.configEnableCount
      this.countList.configDisableCount = data.configDisableCount
      // 七天内告警数
      this.lineChartData.timeData = data.timeList
      this.lineChartData.actualData = data.warningCountList
      this.countList.actionType = data.actionType
      this.countList.warningNameList = data.warningNameList
      this.countList.warningTotalList = data.warningTotalList
      this.countList.warningHandleList = data.warningHandleList
      this.ruleList=[
        {
          "value":data.useCountMap.conditions,
          "name":"条件",
        },
        {
          "value":data.useCountMap.timing,
          "name":"定时",
        },
        {
          "value":data.useCountMap.manualOperation,
          "name":"手动",
        },
      ]
    })
    this.actionTypeList.dataList = this.countList.actionType.map(item => {
      return {
        value: item.count,
        name: this.ACTION_TYPE.find(item2 => item2.value == item.actionType)?.label
      }
    }).sort((a, b) => b.value - a.value)
    this.actionTypeList.nameList = this.ACTION_TYPE.map(item => item.label)

    this.$refs.panelGroup.init(this.countList)
    this.$refs.lineChart.initChart(this.lineChartData)
    this.$refs.ActionChart.initChart(this.actionTypeList)
    this.$refs.raddarChart.initChart(this.ruleList)
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

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
