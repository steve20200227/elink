<template>
  <div class="echart-card">
    <div class="echart-header">
      <span class="card-name">消息通道使用 TOP5</span>

      <el-radio-group v-model="dateSelect" @input="selectData"  size="mini">
        <el-radio-button label="1">今日</el-radio-button>
        <el-radio-button label="2">本周</el-radio-button>
        <el-radio-button label="3">本月</el-radio-button>
        <el-radio-button label="4">本年</el-radio-button>
      </el-radio-group>

    </div>
    <div class="echart" id="pieAppChart"></div>
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
  created() {

  },
  mounted() {
    this.initChart();
  },

  methods: {
    selectData(val){
      this.param.dateSelect=val
      this.initChart()
    },
   async initChart(){
     var chartDom = document.getElementById('pieAppChart')

     var myChart = echarts.init(chartDom)

     var option = {
       color:["#5ab1ef","#32DADDFF","#c8b2f4","#ffb980","#d87a80"],
       tooltip: {
         trigger: 'item'
       },
       series: [
         {
           // name: 'AppId',
           type: 'pie',
           radius: '50%',
           center: ['50%', '60%'],
           label: {
             alignTo: 'edge',
             // formatter: '{name|{b}}\n{value|计数:{c}}',
             minMargin: 5,
             lineHeight: 15,
             rich: {
               time: {
                 fontSize: 10,
                 color: '#999'
               }
             }
           },
           data: [],
           emphasis: {
             itemStyle: {
               shadowBlur: 10,
               shadowOffsetX: 0,
               shadowColor: 'rgba(0, 0, 0, 0.5)'
             }
           }
         }
       ]
     }
     await getAppInfo(this.param).then(res=>{
       if (option.series !== undefined) {
         option.series[0].data = res.data.dashboardInfoList
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
