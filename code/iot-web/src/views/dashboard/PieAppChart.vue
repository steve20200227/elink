<template>
  <div class="echart-card">
    <div class="echart-header">
      <span class="card-name">驱动关联设备数 TOP5</span>
    </div>
<!--    <div class="echart" id="driveRelateEquipment"></div>-->
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getAppInfo } from '@/api/message/controllerData'

export default {
  name: 'PieTemplateChart',

  data() {
    return {
      dateSelect:1,
      param:{
        dateSelect:1,
      }
    }
  },
  methods: {
   // async initChart(data){
   //   data.sort((a, b) => b.equipmentCount -a.equipmentCount)
   //   var countList = data.slice(0,5)
   //   var nameList = []
   //   var dataList = []
   //   countList.forEach(item => {
   //     nameList.push(item.gatewayName)
   //     dataList.push(item.equipmentCount)
   //   })
   //   var chartDom = document.getElementById('driveRelateEquipment')
   //
   //   var myChart = echarts.init(chartDom)
   //
   //   var option = {
   //     color: ['#c7b1f3'],
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
