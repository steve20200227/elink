<template>
  <div class="echart-card">
    <div class="echart-header">
      <span class="card-name">设备关联场景数 TOP5</span>
    </div>
<!--    <div class="echart" id="equipmentRelatedWarning"></div>-->
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getPushUserInfo } from '@/api/message/controllerData'

export default {
  name: 'PieTemplateChart',

  data() {
    return {
      dateSelect:1,
      param:{
        dateSelect:1
      }
    }
  },

  methods: {
    // async initChart(data){
    //   var nameList = []
    //   var dataList = []
    //   data.forEach(item => {
    //     nameList.push(item.equipmentName)
    //     dataList.push(item.warningCount)
    //   })
    //   var chartDom = document.getElementById('equipmentRelatedWarning')
    //
    //   var myChart = echarts.init(chartDom)
    //
    //   var option = {
    //     color: ['#ffb980'],
    //     tooltip: {
    //       trigger: 'axis',
    //       axisPointer: { // 坐标轴指示器，坐标轴触发有效
    //         type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
    //       }
    //     },
    //     xAxis: {
    //       type: 'category',
    //       data: nameList
    //     },
    //     yAxis: {
    //       type: 'value'
    //     },
    //     series: [
    //       {
    //         data: dataList,
    //         type: 'bar'
    //       }
    //     ]
    //   }
    //   option && myChart.setOption(option)
    // }
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
