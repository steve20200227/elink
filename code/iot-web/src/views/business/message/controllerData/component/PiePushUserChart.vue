<template>
  <div class="echart-card">
    <div class="echart-header">
      <span class="card-name">推送用户 TOP5</span>
      <el-radio-group v-model="dateSelect" @input="selectData"  size="mini">
        <el-radio-button label="1">今日</el-radio-button>
        <el-radio-button label="2">本周</el-radio-button>
        <el-radio-button label="3">本月</el-radio-button>
        <el-radio-button label="4">本年</el-radio-button>
      </el-radio-group>

    </div>
    <div class="echart" id="piePushUserChart"></div>
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
  created() {

  },
  mounted() {
    this.initChart()
  },

  methods: {
    selectData(val){
      this.param.dateSelect=val
      this.initChart()
    },
    async initChart(){
      var chartDom = document.getElementById('piePushUserChart')

      var myChart = echarts.init(chartDom)

      var option = {

        color:["#32DADDFF","#c8b2f4","#5ab1ef","#ffb980","#d87a80"],
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            // name: 'UserId',
            type: 'pie',
            radius: ['50%', '60%'],
            center: ['50%', '60%'],
            avoidLabelOverlap: false,
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
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },

          }
        ]
      }

      await getPushUserInfo(this.param).then(res=>{
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
