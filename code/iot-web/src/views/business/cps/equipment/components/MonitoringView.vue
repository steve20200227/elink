<template>
  <div>
    <el-container>
      <el-main style="padding: 0px;">
        <div
          v-show="showTips && deviceEventData.length!=0"
          class="custom-alert warning"      style="margin: 0 0 10px 0; width: 25%;"
        >
          <i class="el-icon-warning custom-alert-icon"></i>
          <span class="custom-alert-title">当前设备未启用，无法查看实时数据！</span>
          <button class="custom-alert-close" @click="enableEquipment">立即启用</button>
        </div>
        <!--  卡片组件  -->
        <div style="max-height: calc(100vh - 410px);height: calc(100vh - 410px);" v-if="deviceEventData.length!=0">
          <el-row>
            <el-col :span="6" v-for="(item, index) in deviceEventData" :key="index">
              <el-card shadow="always" style="margin: 5px;">
                <div style="position: relative">
                  <span style="position: absolute; top: 0; margin-left: 10px">
                    <strong style="font-size: 15px">{{ item.pointName + '(' + item.pointCode + ')' }}</strong>
                    <span
                      :style="{
                          display: 'inline-block',
                          height: '8px',
                          width: '8px',
                          background: 'rgb(10, 191, 91)' ,
                          borderRadius: '7px',
                          verticalAlign: 'middle',
                          marginLeft: '10px',
                          marginBottom: '5px'}">
                    </span>
                  </span>
                  <a style="font-size: 14px; position: absolute; right: 0; z-index: 100" @click="openHistory(item)">
                    <span class="el-icon-search" v-if="false" style="font-size: 16px;color: #1677FF">历史</span>
                  </a>
                  <span style="position: absolute; top: 24px; margin-left: 10px">
                  </span>
                </div>
                <div class="ChartOne">
                  <div>
                    <span style="font-size: 45px; font-weight: bold; margin-left: 10%;">
                      {{ item.latestValue }}
                    </span>
                    <span style="font-size: 15px; color: #808080;">
                      {{ item.unit }}
                    </span>
                  </div>
                  <div style="margin-top: 50px;">
                    <span style="font-size: 15px;color: #808080;">
                      采集时间:
                    </span>
                    <span style="font-size: 15px;color: #808080;">
                      {{item.latestTime}}
                    </span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>


        </div>
        <div class="block" v-if="deviceEventData.length!=0">
          <el-pagination background
                         @size-change="handleSizeChange"
                         @current-change="handleCurrentChange"
                         :current-page="page.currentPage"
                         :page-sizes="[8]"
                         :page-size="page.pageSize"
                         layout="total, sizes, prev, pager, next, jumper"
                         :total="page.total"
          >
          </el-pagination>
        </div>
        <el-empty
          v-if="deviceEventData.length==0"
          style="width: 100%; margin-top: 50px"
          description="暂无数据"
          :image-size="120"
        ></el-empty>
        <el-dialog :visible.sync="showHistory" v-if="showHistory" :title="equipmentTitle" width="60%"
                   :close-on-click-modal="false"
                   class="avue-dialog avue-dialog--top" append-to-body v-dialog-drag
        >

          <div style="display: flex;flex-direction: column-reverse;align-items: center;">
            <div>
              <el-date-picker style="margin-left: 10px"
                              v-model="value2"
                              type="datetimerange"
                              unlink-panels
                              range-separator="至"
                              start-placeholder="开始日期"
                              end-placeholder="结束日期"
                              size="mini"
                              format="yyyy-MM-dd HH:mm:ss"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              @change="dataChange"
              >
              </el-date-picker>
            </div>
            <!--        <div>-->
            <!--          <el-button round size="mini" style="margin-left: 20px" @click="exportBoxHandle">-->
            <!--            {{ $t('cip.cmn.btn.exportBtn') }}-->
            <!--          </el-button>-->
            <!--        </div>-->
          </div>
          <div id="history"></div>
        </el-dialog>
      </el-main>
    </el-container>
  </div>
</template>
<script>


import {ipUtil, page as getPage, selectData} from "@/api/cps/equipment-point-api";
import {detailMonitor, enableEquipment} from "@/api/cps/equipment-api";
import {getDictDataList} from "@/api/system/dict/data.js"
import * as echarts from 'echarts'

let ws
//请勿删除 历史数据计算 请勿删除！！！！！！
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
  props: {
    equipmentId: {
      type: String,
      default: ""
    },
  },
  name: 'view',
  components: {
    echarts
  },
  data() {
    return {
      type: 'view',
      id: '',
      deviceEventData: [],
      dictTypeMap: [], // 业务字典集合
      page: {
        pageSize: 8,
        currentPage: 1,
        total: 0
      },
      // 用于判断是否超出容器
      isTextOverflow: false,
      // 历史弹框
      showHistory: false,
      equipmentTitle: '',
      dataVO: {
        beginDate: '',
        endDate: '',
        pointCode: '',
        deviceCode: ''
      },
      value2: [this.getCurrentTime(), new Date],
      realData: {},
      historyX: [],
      historyY: [],
      dataForm: {},
      equipmentCode: '',
      pointCode: '',
      pointName: '',
      echarts: '',
      ws: null,
      showTips: false,
      dataStatusMap: {
        '-1': '无数据',
        '0': '关',
        '1': '开'
      },
      pointInfo: {
        targetId: 0,
        deviceCode: '',
        pointCode: '',
        value: ''
      }
    }
  },
  mounted() {
    this.dataForm.id = this.equipmentId;
  },
  destroyed() {
    if (this.ws) {
      this.ws.close()
    }
  },
  methods: {
    initData() {
      detailMonitor(this.dataForm.id).then(async res => {
        const data = res.data
        this.dataForm = data
        this.equipmentCode = this.dataForm.equipmentCode
        this.onLoad(this.page);
      })
    },
    enableEquipment() {
      // 启用设备
      enableEquipment(this.dataForm.id).then(res => {
        const {code, msg} = res;
        if (code === 0) {
          this.$message.success("操作成功");
          this.initData();
        } else {
          this.showTips = true;
          this.$message.warning(msg);
        }
      })
    },
    handleSizeChange(val) {
      this.page.pageSize = val
      this.onLoad(this.page)
    },
    handleCurrentChange(val) {
      this.page.currentPage = val
      this.onLoad(this.page)
    },
    headCancel() {
      if (typeof this.ws == 'undefined') {
        return
      }
      this.ws.close()

      this.$router.back()
    },
    restOnLoad() {
      this.onLoad(this.page)
    },
    onLoad(page, params = {}) {
      this.deviceEventData = []
      this.page = page
      this.tableLoading = true
      if (this.dataForm.id !== '') {
        params.equipmentId = this.dataForm.id
      }
      //默认条件，实时监控只需要获取属性点位，事件和功能排除
      params.pointType = 'pointStats';
      getPage(
        page.currentPage,
        page.pageSize,
        params
      ).then(res => {
        const data = res.data
        this.deviceEventData = data.records.map(record => ({
          ...record,
          latestValue: " — ",
          latestTime: "-"
        }))
        this.page.total = data.total
        this.tableLoading = false
        // 如果设备未启用，则不需要进行websocket的连接, 因为设备此时的源配置暂未生成，对应的ws服务端点也未生成
        if (this.dataForm.isEnable == 1) {
          this.showTips = false;
          this.openWs()
        } else {
          this.showTips = true;
        }
        // 遍历获取属性字典
        const dictList = []
        this.deviceEventData.forEach(item => {
          if (item.pointDict != null) {
            dictList.push(item.pointDict)
          }
        })
        getDictDataList(dictList.join(",")).then(res => {
          this.dictTypeMap = res.data
        })
      })
    },
    openHistory(row) {
      this.showHistory = true
      this.pointName = row.pointName
      this.pointCode = row.pointCode
      this.equipmentTitle = row.pointCode + '-' + row.pointName
      //默认时间
      this.dataVO.beginDate = this.timeChange(this.value2[0])
      this.dataVO.endDate = this.timeChange(this.value2[1])
      this.dataVO.deviceCode = this.equipmentCode
      this.dataVO.pointCode = row.pointCode
      selectData(this.dataVO, 'today').then(row => {
        console.log(row)
        /* this.historyX = row.data.data.data.xdata
        this.historyY = row.data.data.data.ydata */
        this.historyX = row.data.data.xdata
        this.historyY = row.data.data.ydata
        this.openHistoryShow()
      })
    },
    webSockets(val) {
      let that = this;
      that.deviceEventData.forEach(e => {
        if (val[e.pointCode] !== undefined) {
          const dataTypeDict = this.dictTypeMap[e.pointDict]
          if (dataTypeDict) {
            dataTypeDict.forEach(item => {
              if (item.value == val[e.pointCode]) {
                e.latestValue = item.label
              }
            })
          } else {
            e.latestValue = val[e.pointCode]; // 更新 latestValue
          }
          e.latestTime = val.thetime; // 更新 latestTime
        }
      })
    },
    openHistoryShow() {
      this.$nextTick(() => {
        this.getHistory(this.historyX, this.historyY)
      })
    },

    getHistory(axlex, axley) {
      let myChart = echarts.getInstanceByDom(
        document.getElementById('history')
      )
      let option = {
        tooltip: {
          trigger: 'axis',
          position: function(pt) {
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
          data: axlex
        },
        yAxis: {
          type: 'value',
          boundaryGap: [0, '100%']
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
            data: axley
          }
        ]
      }
      //检测是否已经存在echarts实例，如果不存在，则不再去初始化
      if (myChart == null) {
        myChart = echarts.init(document.getElementById('history'))
      }
      myChart.setOption(option)
      window.addEventListener('resize', function() {
        myChart.resize()
      })
    },
    openWs() {
      let that = this
      that.ws = new WebSocket(this.dataForm.webSocket)
      that.ws.onopen = function () {
        that.$message.success('开始监听实时数据...')
      }
      that.ws.onerror = function (error) {
        that.$message.error('监听实时数据失败: ' + error.message)
      }
      that.ws.onclose = function() {}
      that.ws.doSend = function(message) {}
      that.ws.onmessage = function(data) {
        //进行类型的转换
        let msg = JSON.parse(data.data)
        if (that.dataForm.equipmentCode != null) {
          that.webSockets(msg)
        }
      }
    },
    // 时间转换
    // 前端时间格式的转换
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
    dataChange(val) {
      if (val[0] != null) {
        //开始时间结束时间
        this.dataVO.beginDate = val[0].toString()
        this.dataVO.endDate = val[1].toString()
        if (this.dataVO.beginDate > this.Date()) {
          return this.$message.error("结束时间不能比当前时间大");
        }
        let time = this.time(this.dataVO.beginDate, this.dataVO.endDate)
        if (time < -1 && time <= 0) {
          return this.$message.error("时间范围不允许超出一天")
        }
        this.dataVO.deviceCode = this.deviceCode
        this.dataVO.pointCode = this.deviceEventCode
        selectData(this.dataVO, 'dataChange').then(row => {
          this.historyX = row.data.data.data.xdata
          this.historyY = row.data.data.data.ydata
          this.openHistoryShow()
        })
      }
    },
    //获取当前年月日时分秒
    Date() {
      const nowDate = new Date()
      const date = {
        year: nowDate.getFullYear(),
        month: nowDate.getMonth() + 1,
        date: nowDate.getDate(),
        hours: nowDate.getHours(),
        minutes: nowDate.getMinutes(),
        seconds: nowDate.getSeconds()
      }

      const newmonth = date.month >= 10 ? date.month : '0' + date.month
      const newday = date.date >= 10 ? date.date : '0' + date.date
      const newminutes = date.minutes >= 10 ? date.minutes : '0' + date.minutes
      const newseconds = date.seconds >= 10 ? date.seconds : '0' + date.seconds
      //   const newminutes = date.minutes < 10 ? "0" + date.minutes : date.minutes;
      //   const newseconds = date.seconds < 10 ? "0" + date.seconds : date.seconds;
      let dateTime
      dateTime =
        date.year +
        '-' +
        newmonth +
        '-' +
        newday +
        ' ' +
        date.hours +
        ':' +
        newminutes +
        ':' +
        newseconds
      return dateTime
    },
    //获取一小时之前的日期
    getCurrentTime() {
      return new Date(new Date().getTime() - 60 * 60 * 1000)// 前一个小时  当前时间
    },
    today() {
      //今日
      this.dataVO.deviceCode = this.deviceCode
      this.dataVO.pointCode = this.deviceEventCode
      selectData(this.dataVO, 'today').then(row => {
        this.historyX = row.data.data.data.x
        this.historyY = row.data.data.data.y
        this.openHistoryShow()
      })
    },
    //判断时间是否超出一天
    time(finish, begin) {
      let startDate = Date.parse(finish)
      let endDate = Date.parse(begin)
      return (startDate - endDate) / (24 * 60 * 60 * 1000)
    },
    month() {
      //本月
      this.dataVO.deviceCode = this.deviceCode
      this.dataVO.pointCode = this.deviceEventCode
      selectData(this.dataVO, 'month').then(row => {
        this.historyX = row.data.data.data.x
        this.historyY = row.data.data.data.y
        this.openHistoryShow()

      })
    },

    Week() {
      //本周
      this.dataVO.deviceCode = this.deviceCode
      this.dataVO.pointCode = this.deviceEventCode
      selectData(this.dataVO, 'Week').then(row => {
        this.historyX = row.data.data.data.x
        this.historyY = row.data.data.data.y
        this.openHistoryShow()
      })

    }
  }
}
</script>

<style scoped lang="scss">

#history {
  width: 100%;
  height: 300px;
}

.ChartOne {
  width: 100%;
  height: 170px;
  margin-top: 80px;
  text-align: center;
}

.cardBigBox {
  width: calc(100% - 24px);
  padding: 12px;
  display: flex;
  flex-wrap: wrap;
  overflow: auto;
  background-color: #ffffff;
  align-content: flex-start;

  .cardbox {
    width: 24%;
    box-shadow: 0 0 5px #dde9ef;
    border: 1px solid #e7eff3;
    border-radius: 10px;
    margin: 16px 6px 0 6px;
    cursor: pointer;

    .cardTop {
      padding: 16px;

      .topContent {
        .instruction {
          white-space: nowrap; /* 防止文本换行 */
          overflow: hidden; /* 隐藏超出容器的文本 */
          text-overflow: ellipsis; /* 用省略号表示被截断的文本 */
          cursor: pointer; /* 当鼠标悬停时显示手形光标，提示用户有tooltip */
          font-size: 20px;
          color: #333;
        }

        .topItem {
          font-size: 15px;
          color: #666;
          line-height: 30px;
        }
      }
    }

    .cardBottom {
      font-size: 14px;
      line-height: 25px;
      display: flex;
      border: 1px solid #DCDFE6;
      justify-content: space-between;

      .topBottom {
        display: flex;
        align-items: center;
        justify-content: space-between;
        flex: 1;

        .statusContent {
          display: flex;
          align-items: center;

          .iconCard {
            width: 8px;
            height: 8px;
            border-radius: 50%;
            background-color: red;
            margin: 10px 6px;
          }
        }
      }
    }
  }
}

.block {
  //position: absolute;
  bottom: 0;
  width: calc(100% - 48px);
  padding: 12px 24px;
  background-color: #FFFFFF;
  display: flex;
  flex-direction: row-reverse;
}

.monitoring ::v-deep .el-form-item {
  width: 98%;
}

.monitoring ::v-deep .el-form-item__content {
  width: 98%;
}

.monitoring ::v-deep .el-select {
  width: 98%;
}

.el-card__body {
  padding: 15px 20px 0px 20px;
}
.custom-alert {
  padding: 10px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.warning {
  background-color: #fff6e5;
  border: 1px solid #f5dab1;
  color: #e6a23c;
}

.custom-alert-icon {
  margin-right: 10px;
}

.custom-alert-title {
  flex-grow: 1;
}

.custom-alert-close {
  background-color: transparent;
  border: none;
  color: #e6a23c;
  cursor: pointer;
  font-size: 14px;
}

.custom-alert-close:hover {
  text-decoration: underline;
}
</style>
