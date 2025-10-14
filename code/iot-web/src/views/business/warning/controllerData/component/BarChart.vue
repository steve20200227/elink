<template>
  <div class="echart-card">
    <div class="echart-header">
      <span class="card-name">设备告警 TOP 5</span>

    </div>
    <div class="echart" id="warningEquipment"></div>
  </div>

</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from '@/views/dashboard/mixins/resize'
import {getMessageInfo} from "@/api/message/controllerData";

const animationDuration = 6000

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '200px'
    }
  },
  data() {
    return {
      chart: null,
      countList: [],
    }
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    init(countList) {
      this.countList = countList
    },
    initChart(data) {
      var countList = data
      var chartDom = document.getElementById('warningEquipment')
      var myChart = echarts.init(chartDom)
      var option = {
        color: ['#60bcf9', '#ffb980', '#c7b1f3'],
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: 10,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: countList.warningNameList,
          axisTick: {
            alignWithLabel: true
          }
        }],
        yAxis: [{
          type: 'value',
          axisTick: {
            show: false
          }
        }],
        series: [{
          name: '告警总数',
          type: 'bar',
          stack: 'vistors',
          barWidth: '60%',
          data: countList.warningTotalList,
          animationDuration
        }, {
          name: '已处理数',
          type: 'bar',
          stack: 'vistors',
          barWidth: '60%',
          data: countList.warningHandleList,
          animationDuration
        },
        ]
      }

      option && myChart.setOption(option)
      // this.chart = echarts.init(this.$el, 'macarons')
      // var countList = data
      // this.chart.setOption({
      //   title: {
      //     text: '设备告警榜单',
      //     lineHeight: 50,
      //   },
      //   tooltip: {
      //     trigger: 'axis',
      //     axisPointer: { // 坐标轴指示器，坐标轴触发有效
      //       type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
      //     }
      //   },
      //   grid: {
      //     top: 10,
      //     left: '2%',
      //     right: '2%',
      //     bottom: '3%',
      //     containLabel: true
      //   },
      //   xAxis: [{
      //     type: 'category',
      //     data: countList.warningNameList,
      //     axisTick: {
      //       alignWithLabel: true
      //     }
      //   }],
      //   yAxis: [{
      //     type: 'value',
      //     axisTick: {
      //       show: false
      //     }
      //   }],
      //   series: [{
      //     name: '告警总数',
      //     type: 'bar',
      //     stack: 'vistors',
      //     barWidth: '60%',
      //     data: countList.warningTotalList,
      //     animationDuration
      //   }, {
      //     name: '已处理数',
      //     type: 'bar',
      //     stack: 'vistors',
      //     barWidth: '60%',
      //     data: countList.warningHandleList,
      //     animationDuration
      //   },
      //   ]
      // })
    }
  }
}
</script>

<style lang="scss" scoped>

.echart-card {
  width: 100%;
  height: 100%;

  .echart-header {
    height: 32px;
    margin-bottom: 20px;

    .card-name {
      font-size: 20px;
      font-weight: 700;
    }
  }

  .echart {
    width: 100%;
    height: 320px;
  }
}

</style>
