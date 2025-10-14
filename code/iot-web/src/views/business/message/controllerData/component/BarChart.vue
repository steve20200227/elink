<template>

  <div class="echart-card">
    <div class="echart-header">
      <span class="card-name">消息详情</span>
      <el-radio-group v-model="dateSelect" @input="selectData" size="mini">
        <el-radio-button label="1">今日</el-radio-button>
        <el-radio-button label="2">本周</el-radio-button>
        <el-radio-button label="3">本月</el-radio-button>
        <el-radio-button label="4">本年</el-radio-button>
      </el-radio-group>

    </div>
    <div class="echart" id="barChart"></div>
  </div>

</template>

<script>


import * as echarts from 'echarts'
import { getMessageInfo } from '@/api/message/controllerData'

export default {
  name: 'BarChart',

  data() {
    return {
      data: [],
      dateSelect: 1,
      param: {}
    }
  },
  created() {
    this.param.dateSelect = this.dateSelect

  },
  mounted() {
    this.initChart()
  },

  methods: {
    selectData(val) {
      this.param.dateSelect = val
      this.initChart()
    },
    async initChart() {
      var chartDom = document.getElementById('barChart')

      var myChart = echarts.init(chartDom)

      var option = {
        color:["#32DADDFF","#c8b2f4","#5ab1ef","#ffb980","#d87a80"],
        legend: {
          top: '20%'
        },
        tooltip: {},
        dataset: {},
        grid: {
          top: '30%',
          bottom: '10%'
        },
        xAxis: { type: 'category' },
        yAxis: {},
        series: [
          {
            type: 'bar',
            color: '#32DADDFF'
          },
          {
            type: 'bar',
            color: '#c8b2f4'
          }
        ]
      }

      await getMessageInfo(this.param).then(res => {
        option.dataset = {
          dimensions: ['product', '成功', '失败'],
          source: res.data.messageInfoList
        }

      })

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

    display: flex;
    justify-content: space-between;
    height: 32px;

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
