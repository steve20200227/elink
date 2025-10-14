<template>
  <div class="dashboard-editor-container">

    <panel-group
      ref="panelGroup"
      :countList="countList"
    />

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:20px;border-radius: 12px;">
      <line-chart ref="lineChart" :chart-data="lineChartData" />
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="chart-wrapper">
          <raddar-chart ref="raddarChart"/>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-wrapper">
          <pie-chart ref="pieChart"/>
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import RaddarChart from './dashboard/RaddarChart'
import PieChart from './dashboard/PieChart'
import {getCpsCount, getEmqxMonitor, getRecordCount, getWarningCount} from '@/api/home/home-api'
import {parseTime} from "@/utils/ruoyi";


export default {
  name: 'Index',
  components: {
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
  },
  data() {
    return {
      lineChartData: {},
      countList: [],
      messageData: {},
      timer: null //定时器
    }
  },
  async mounted() {
    // 获取数采相关数据
    await getCpsCount().then(res => {
      this.countList.driveCount = res.data.driveCount
      this.countList.equipmentCount = res.data.equipmentCount
      this.countList.productCount = res.data.productCount
    })
    await getWarningCount().then(res => {
      this.countList.configCount = res.data.configCount
      this.countList.timeList = res.data.timeList
      this.countList.warningCountList = res.data.warningCountList
      this.countList.warningNameList = res.data.warningNameList
      this.countList.warningTotalList = res.data.warningTotalList
      this.countList.warningHandleList = res.data.warningHandleList
    })
    await getRecordCount().then(res => {
      this.countList.messageRecordCount = res.data
    })
    await this.handleEmqxonitor()
    this.lineChartData.actualData = this.countList.warningCountList
    this.lineChartData.timeData = this.countList.timeList
    this.$refs.panelGroup.init(this.countList)
    this.$refs.lineChart.initChart(this.lineChartData)

    // 轮询刷新消息流入流出数据
    if (!this.timer) {
      this.timer = setInterval(() => {
        this.handleEmqxonitor()
      },1000 * 10)
    }
  },
  beforeDestroy() {
    // 清除定时器
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    handleEmqxonitor(type) {
      getEmqxMonitor().then(res => {
        const data =  JSON.parse(res.data.body)
        var timeList = []
        var sentList = []
        var receivedList = []
        data.forEach(item => {
          timeList.push(parseTime(item.time_stamp).split(' ')[1])
          sentList.push(item.sent)
          receivedList.push(item.received)
        })
        this.messageData.timeList = timeList
        this.messageData.sentList = sentList
        this.messageData.receivedList = receivedList
        this.$refs.raddarChart.initChart(this.messageData)
        this.$refs.pieChart.initChart(this.messageData)
      })
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

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
