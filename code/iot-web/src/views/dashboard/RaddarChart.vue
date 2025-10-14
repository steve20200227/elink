<template>
  <div class="echart-card">
    <div class="echart-header">
      <span class="card-name">消息发送速率</span>
    </div>
    <div class="echart" id="messageSent"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'homeSentChart',
  data() {
    return {
      dateSelect:1,
      param:{
        dateSelect:1,
      }
    }
  },
  mounted() {
    var chartDom = document.getElementById('messageSent')
    echarts.init(chartDom)
  },
  methods: {
    async initChart(data){
      let myChart = echarts.getInstanceByDom(
        document.getElementById('messageSent')
      )
      var option = {
        color: ['#ffb980'],
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: data.timeList
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: "发送速率",
            type: 'bar',
            step: 'start',
            data: data.sentList,
          }
        ]
      }
      option && myChart.setOption(option)
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
