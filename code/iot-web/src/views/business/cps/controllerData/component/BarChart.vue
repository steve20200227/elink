<template>
  <div class="echart-card">
    <div class="echart-header">
      <span class="card-name">设备类型分布</span>
    </div>
    <div class="echart" id="equipmentAgreementType"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

require('echarts/theme/macarons') // echarts theme
import resize from '@/views/dashboard/mixins/resize'
import {getEquipmentTypeData} from "@/api/cps/cps_panel"

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
      equipmentType: [],
      chart: null
    }
  },
  mounted() {
    this.init()
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    init() {
      getEquipmentTypeData().then((res) => {
        if (res.data && Array.isArray(res.data)) {
          this.equipmentType = res.data.map(item => ({
            name: item.name,
            value: item.value
          }));
          this.initChart(this.equipmentType);
        }
      })
    },
    initChart(data) {
      var chartDom = document.getElementById('equipmentAgreementType')
      var myChart = echarts.init(chartDom)
      var option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'left'
        },
        series: [
          {
            name: '设备类型分布',
            type: 'pie',
            radius: ['50%', '80%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 40,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: data
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
