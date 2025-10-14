<template>
  <el-row :gutter="20" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" style="height: 148px;" @click="handleSetLineChartData('messages')">
        <div class="card-panel-icon-wrapper icon-message">
          <svg-icon icon-class="warningAction" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description" style="float: right">
          <div class="card-panel-text" style="margin-bottom: 12px;">
            动作
          </div>
          <count-to :start-val="0" :end-val="countList.actionCount" :duration="1000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="12" class="card-panel-col">
      <div class="card-content">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="card-panel" style="background: #f0f2f5;" @click="handleSetLineChartData('purchases')">
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="warningConfig" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text" style="margin-bottom: 10px;margin-right: 10px">
                <span>场景</span>
                <count-to :start-val="0" :end-val="countList.configCount" :duration="1000" class="card-panel-num" />
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="card-panel" style="background: #f0f2f5;" @click="handleSetLineChartData('purchases')">
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="warningConfig" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text" style="margin-bottom: 10px;margin-right: 10px">
                <span>启用</span>
                <count-to :start-val="0" :end-val="countList.configEnableCount" :duration="1000"
                  class="card-panel-num" />
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="card-panel" style="background: #f0f2f5;" @click="handleSetLineChartData('purchases')">
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="warningConfig" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text" style="margin-bottom: 10px;margin-right: 10px">
                <span>禁用</span>
                <count-to :start-val="0" :end-val="countList.configDisableCount" :duration="1000"
                  class="card-panel-num" />
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" style="height: 148px;" @click="handleSetLineChartData('shoppings')">
        <div class="card-panel-icon-wrapper icon-money">
          <svg-icon icon-class="warningRecord" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description" style="float: right">
          <div class="card-panel-text" style="margin-bottom: 12px;">
            场景触发记录
          </div>
          <count-to :start-val="0" :end-val="countList.recordCount" :duration="1000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'

export default {
  components: {
    CountTo
  },
  data() {
    return {
      countList: [],
    }
  },
  methods: {
    init(countList) {
      this.countList = countList
    },
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  .card-content {
    background: #ffffff;
    padding: 20px;
    border-radius: 12px;
  }
  .card-panel-col {
    margin-bottom: 20px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
        padding: 8px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon {
      font-size: 48px;
    }

    .card-panel-description {
      //float: right;
      font-weight: bold;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 20px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
