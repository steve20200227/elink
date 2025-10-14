<template>
  <div>
    dsafsdfa    dsafsdfa    dsafsdfa    dsafsdfa
    <div :class="className" :style="{height:height,width:width}" />
  </div>

</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

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
      default: '300px'
    }
  },
  data() {
    return {
      chart: null,
      countList: [],
    }
  },
  // mounted() {
  //   this.$nextTick(() => {
  //     this.initChart()
  //   })
  // },
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
      this.chart = echarts.init(this.$el, 'macarons')
      var countList = data
      this.chart.setOption({
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
          name: '处理数',
          type: 'bar',
          stack: 'vistors',
          barWidth: '60%',
          data: countList.warningHandleList,
          animationDuration
        },
        ]
      })
    }
  }
}
</script>
