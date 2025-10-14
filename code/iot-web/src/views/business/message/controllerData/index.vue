<template>

  <div class="dashboard-editor-container">


    <panel-group-message
      ref="panelGroup"
      :handleSetLineChartData="handleSetLineChartData"
    />

    <!--    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
          <line-chart ref="lineChart" :chart-data="lineChartData" />
        </el-row>-->

        <el-row :gutter="32">
          <el-col :span="12" >
            <div class="chart-wrapper">

              <bar-chart>

              </bar-chart>
            </div>
          </el-col>
          <el-col :span="12" >
            <div class="chart-wrapper">
              <pie-template-chart>

              </pie-template-chart>
            </div>
          </el-col>

          <el-col :span="12" >
            <div class="chart-wrapper">
              <pie-app-chart>

              </pie-app-chart>
            </div>
          </el-col>

          <el-col :span="12" >
            <div class="chart-wrapper">
              <pie-push-user-chart>

              </pie-push-user-chart>
            </div>
          </el-col>

        </el-row>


  </div>


</template>

<script>


import * as echarts from 'echarts'
import PanelGroupMessage from './component/PanelGroupMessage'
import BarChart from './component/BarChart'
import PieTemplateChart from './component/PieTemplateChart'
import PieAppChart from './component/PieAppChart'
import PiePushUserChart from './component/PiePushUserChart'
import { getDashboardHeadData } from '@/api/message/controllerData'
import CountTo from 'vue-count-to'

export default {
  name: 'index',
  components: {
    PanelGroupMessage,
    BarChart,
    PieTemplateChart,
    PieAppChart,
    PiePushUserChart,
    CountTo
  },
  data() {
    return {
      handleSetLineChartData: {}
    }
  },
  created() {
    getDashboardHeadData().then(res => {
      this.handleSetLineChartData = res.data
    })
  },
  mounted() {

  },
  methods: {}
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}


</style>
