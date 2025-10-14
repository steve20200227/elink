<template>
  <div class="echart-card">
    <div class="echart-header">
      <span class="card-name">设备关联场景数 TOP5</span>
    </div>
    <div class="echart" id="equipmentRelatedWarning"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {getEquipmentWarningData} from "@/api/cps/cps_panel"

export default {
  name: 'PieTemplateChart',

  data() {
    return {
      equipmentWarning: [],
      dateSelect: 1,
      param: {
        dateSelect: 1
      }
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      getEquipmentWarningData().then((res) => {
        if (res.data && Array.isArray(res.data)) {
          let nameList = res.data
            .filter(item => item.name && item.value)
            .slice(0, 5)
            .map(item => item.name);
          let dataList = res.data
            .filter(item => item.name && item.value)
            .slice(0, 5)
            .map(item => item.value);
          this.initChart(nameList, dataList);
        }
      })
    },
    async initChart(nameList, dataList) {
      var chartDom = document.getElementById('equipmentRelatedWarning')
      var myChart = echarts.init(chartDom)
      var option = {
        color: ['#ffb980'],
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: nameList
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: dataList,
            type: 'bar'
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
