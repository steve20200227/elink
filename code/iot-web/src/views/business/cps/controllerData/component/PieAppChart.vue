<template>
  <div class="echart-card">
    <div class="echart-header">
      <span class="card-name">设备协议类型分布</span>
    </div>
    <div class="echart" id="driveRelateEquipment"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {getAgreementTypeData} from "@/api/cps/cps_panel"

export default {
  name: 'PieTemplateChart',

  data() {
    return {
      agreementTypeData: [],
      dateSelect: 1,
      param: {
        dateSelect: 1,
      }
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      getAgreementTypeData().then((res) => {
        if (res.data && Array.isArray(res.data)) {
          this.agreementTypeData = res.data.map(item => ({
            name: item.name + " 协议",
            value: item.value
          }));
          this.initChart(this.agreementTypeData);
        }
      })
    },
    async initChart(data) {
      var chartDom = document.getElementById('driveRelateEquipment')

      var myChart = echarts.init(chartDom)

      var option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '设备协议类型分布',
            type: 'pie',
            radius: '80%',
            data: data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
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
