<template>
  <div class="echart-card">
    <div class="echart-header">
      <span class="card-name">动作类型分布</span>
    </div>
    <div class="echart" id="warningActionChart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from '@/views/dashboard/mixins/resize'

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
      chart: null
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
    initChart(data) {
      var dataList = data
      var chartDom = document.getElementById('warningActionChart')
      var myChart = echarts.init(chartDom)
      var option = {
        color: ['#60bcf9', '#c7b1f3', '#ffb980', '#34e2e6', '#34bfa3'],
        tooltip: {
          trigger: 'item',
          formatter: '{b} : {c} ({d}%)'
        },
        legend: {
          left: 'left',
          data: dataList.nameList
        },
        series: [
          {
            name: 'WEEKLY WRITE ARTICLES',
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: dataList.dataList,
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      }
      option && myChart.setOption(option)
/*      option && myChart.setOption(option)
      this.chart = echarts.init(this.$el, 'macarons')
      const dataList = data
      this.chart.setOption({
        title: {
          text: '动作类型分布',
          lineHeight: 50,
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: dataList.nameList
        },
        series: [
          {
            name: 'WEEKLY WRITE ARTICLES',
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: dataList.dataList,
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      })*/
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
