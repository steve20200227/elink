<template>
  <div>
    <el-container class="el-container">
      <el-main>
        <div class="container">
          <div class="sidebar">
            <h2>点位列表</h2>
            <ul id="tag-list" v-if="pointData.length>0">
              <li v-for="point in pointData"
                  :key="point.id"
                  :data-content="point.pointName"
                  @click="pointClick(point)"
                  :class="{ selected: point.id === currentPoint.id }">
                {{ point.pointName }}
              </li>
            </ul>

          </div>
          <div class="content">
            <h2 v-if="currentPoint && currentPoint.pointName">{{ currentPoint.pointName }}</h2>
            <div style="display: flex; justify-content: space-between; font-size: 18px; padding-bottom: 5px">
              <el-form style="margin: -15px auto 0; font-size: 18px" ref="gridHeadLayout" :inline="true" :model="dataForm" class="demo-form-inline">
                <el-form-item>
                  <el-date-picker
                    v-model="dataForm.dateTimeRange"
                    size="max"
                    type="datetimerange"
                    range-separator="至"
                    start-placeholder="开始时间"
                    end-placeholder="结束时间"
                    value-format="yyyy-MM-dd HH:mm:ss">
                  </el-date-picker>
                </el-form-item>
                <el-form-item class="top-button">
                  <el-button size="max" icon="el-icon-search" @click="handleSearch"></el-button>
                  <el-button size="max" icon="el-icon-refresh-right" @click="handleEmpty"></el-button>
                </el-form-item>
              </el-form>
            </div>
            <div id="display-tag" style="text-align: center;">
              <el-tag>最近值: {{ dataForm.currentValue }}</el-tag>
              <el-tag type="success">平均值: {{ dataForm.avgValue }}</el-tag>
              <el-tag type="warning">最大值: {{ dataForm.maxValue }}</el-tag>
              <el-tag type="danger">最小值: {{ dataForm.minValue }}</el-tag>
              <el-tag >最大差值: {{ dataForm.maxDiff }}</el-tag>
            </div>
            <div id="display-content"></div>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {list, selectData, selectDataHistoryStatistic} from "@/api/cps/equipment-point-api";
import {detailMonitor} from "@/api/cps/equipment-api";

let base = +new Date(1968, 9, 3)
let oneDay = 24 * 3600 * 1000
let date = []
let data = [Math.random() * 300]
for (let i = 1; i < 20000; i++) {
  const now = new Date((base += oneDay))
  date.push([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'))
  data.push(Math.round((Math.random() - 0.5) * 20 + data[i - 1]))
}
export default {
  name: 'historyData',
  data() {
    return {
      dataForm: {
        dateTimeRange: [this.getCurrentTime(), new Date],
        currentValue: 0,
        avgValue: 0,
        maxValue: 0,
        minValue: 0,
        maxDiff: 0,
      },
      deviceCode: '',
      tableLoading: false,
      pointData: [],
      currentPoint: {},
    }
  },
  methods: {
    init(equipmentCode) {
      //历史数据处只需要展示属性点位，所以pointType: 'pointStats',
      let param = {
        pointType: "pointStats",
        equipmentCode: equipmentCode
      }
      list(param).then((res) => {
        const data = res.data;
        this.pointData = data; //设置点位列表数据
        this.currentPoint = data[0]; //当前节点取第一条数据
        this.initData(this.currentPoint); //初始化图表
      });

    },
    initData(currentPoint) {
      if(typeof currentPoint != "undefined"){
        detailMonitor(currentPoint.equipmentId).then(res => {
          if (res.data.equipmentCode) {
            this.deviceCode = res.data.equipmentCode
            let data = {};
            data.deviceCode = this.deviceCode
            data.pointCode = currentPoint.pointCode
            data.beginDate = this.timeChange(this.dataForm.dateTimeRange[0])
            data.endDate = this.timeChange(this.dataForm.dateTimeRange[1])
            selectData(data).then(row => {
              const xData = row.data.data.xdata
              const yData = row.data.data.ydata
              if (yData.length > 0) {
                this.settingsValue(data);
              }
              this.initChart(xData, yData)
            })
          } else {
            this.$message({
              message: '设备未配置监控点位',
              type: 'warning'
            })
          }
        })
      }
    },
    initChart(xData, yData) {
      const chart = echarts.init(document.getElementById('display-content'));
      const option = {
        tooltip: {
          trigger: 'axis',
          position: function (pt) {
            return [pt[0], '10%']
          }
        },
        toolbox: {
          feature: {
            dataZoom: {
              yAxisIndex: 'none'
            },
            restore: {},
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: xData
        },
        yAxis: {
          type: 'value',
          boundaryGap: [0, '100%'],
        },
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 10
          },
          {
            start: 0,
            end: 10
          }
        ],
        series: [
          {
            name: 'Fake Data',
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            itemStyle: {
              color: 'rgb(255, 70, 131)'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgb(255, 158, 68)'
                },
                {
                  offset: 1,
                  color: 'rgb(255, 70, 131)'
                }
              ])
            },
            data: yData
          }
        ]
      };
      chart.setOption(option);
    },
    settingsValue(data) {
      // 设置点位总览数据
      selectDataHistoryStatistic(data).then(res => {
        var data1 = res.data.data;
        this.dataForm.avgValue = data1.avgValue;
        this.dataForm.maxValue = data1.maxValue;
        this.dataForm.minValue = data1.minValue;
        this.dataForm.currentValue = data1.currentValue;
        this.dataForm.maxDiff = data1.maxDiff;
      })
    },
    pointClick(point) {
      this.currentPoint = point
      this.initData(point);
    },
    handleSearch() {
      // 搜索
      this.initData(this.currentPoint);
    },
    handleEmpty() {
      // 刷新到最近一小时的数据
      this.dataForm.dateTimeRange = [this.getCurrentTime(), new Date];
      this.initData(this.currentPoint);
    },
    //获取一小时之前的日期
    getCurrentTime() {
      return new Date(new Date().getTime() - 60 * 60 * 1000)// 前一个小时  当前时间
    },
    timeChange(UTCDateString) {
      if (!UTCDateString) {
        return '-'
      }

      function formatFunc(str) {
        return str > 9 ? str : '0' + str
      }

      const date2 = new Date(UTCDateString)
      let year = date2.getFullYear()
      let mon = formatFunc(date2.getMonth() + 1)
      let day = formatFunc(date2.getDate())
      let hour = date2.getHours()
      hour = hour >= 12 ? hour : hour // 24小时制
      hour = formatFunc(hour)
      let min = formatFunc(date2.getMinutes())
      let sec = formatFunc(date2.getSeconds())
      return year + '-' + mon + '-' + day + ' ' + hour + ':' + min + ':' + sec
    },
  }
}
</script>

<style scoped>

* {
  box-sizing: border-box;
  margin: 0;
}

body {
  font-family: Arial, sans-serif;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f4f4f4;
}

.container {
  display: flex;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 8px;
  overflow: hidden;
  margin-top: -10px;
  height: calc(80vh - 69px);;
}

.sidebar {
  width: 14%;
  background-color: #1890FFFF;
  color: white;
  padding: 20px 10px;
  font-size: 17px;
  flex-grow: 0;
  overflow-y: auto; /* 添加垂直滚动条 */
}

::-webkit-scrollbar {
  width: 5px;
}

::-webkit-scrollbar-thumb {
  background-color: #888;
  border-radius: 5px;
}

::-webkit-scrollbar-thumb:hover {
  background-color: #555;
}

.sidebar h2 {
  margin-bottom: 20px;
}

.sidebar ul {
  list-style-type: none;
  padding: 0;
}

.sidebar li {
  margin: 0;
  width: 100%;
  cursor: pointer;
  padding: 10px;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.sidebar li.selected {
  background-color: #1763aa; /* 或您希望的其他颜色 */
  color: white; /* 如果需要改变文字颜色以保证可读性 */
}

.sidebar li:hover {
  background-color: #3d72a3;
}

.content {
  width: 100%;
  padding: 20px;
  background-color: white;
  flex-grow: 1;
  height: calc(80vh - 69px);;
}

.content h2 {
  margin-bottom: 20px;
}

#display-content {
  margin-top: 5px;
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
  min-height: calc(60vh - 49px);
}

#display-tag .el-tag {
  margin: 10px 40px;
  font-size: 17px; /* 调整字体大小以放大标签文本 */
  padding: 20px 16px;
  line-height: 0; /* 调整行高以确保文字垂直居中 */
  vertical-align: middle; /* 确保元素在垂直方向上居中 */
}

#display-tag .el-tag--success {
  font-size: 17px; /* 同样可以单独调整每种类型的标签字体大小 */
}

#display-tag .el-tag--warning {
  font-size: 17px; /* 同样可以单独调整每种类型的标签字体大小 */
}

#display-tag .el-tag--danger {
  font-size: 17px; /* 同样可以单独调整每种类型的标签字体大小 */
}
</style>
