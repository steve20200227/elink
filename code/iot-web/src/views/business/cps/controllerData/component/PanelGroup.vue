<template>
  <el-row :gutter="20" class="panel-group">
    <el-col :xs="24" :sm="24":md="24" :lg="12" class="card-panel-col">
      <div class="card-content">
        <el-row :gutter="20" class="panel-group">
          <el-col :span="8">
            <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
              <div class="card-panel-icon-wrapper icon-people">
                <svg-icon icon-class="product" class-name="card-panel-icon" />
              </div>
              <div class="card-panel-description">
                <span class="card-panel-text" style="margin-right: 10px;">产品</span>
                <count-to :start-val="0" :end-val="countList.productCount || 0" :duration="1000" class="card-panel-num" />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
              <div class="card-panel-icon-wrapper icon-people">
                <svg-icon icon-class="product" class-name="card-panel-icon" />
              </div>
              <div class="card-panel-description">
                <span class="card-panel-text" style="margin-right: 10px;">已启用</span>
                <count-to :start-val="0" :end-val="countList.productEnabelCount || 0" :duration="1000"
                  class="card-panel-num" />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
              <div class="card-panel-icon-wrapper icon-people">
                <svg-icon icon-class="product" class-name="card-panel-icon" />
              </div>
              <div class="card-panel-description">
                <span class="card-panel-text" style="margin-right: 10px;">已停用</span>
                <count-to :start-val="0" :end-val="countList.productDisableCount || 0" :duration="1000"
                  class="card-panel-num" />
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-col>
    <el-col :xs="24" :sm="24":md="24" :lg="12" class="card-panel-col">
      <div class="card-content">
        <el-row :gutter="20" class="panel-group">
          <el-col :span="8">
            <div class="card-panel" @click="handleSetLineChartData('messages')">
              <div class="card-panel-icon-wrapper icon-message">
                <svg-icon icon-class="deviceMini" class-name="card-panel-icon" />
              </div>
              <div class="card-panel-description">
                <span class="card-panel-text" style="margin-bottom: 10px;margin-right: 10px">设备</span>
                <count-to :start-val="0" :end-val="countList.equipmentCount || 0" :duration="1000" class="card-panel-num" />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="card-panel" @click="handleSetLineChartData('messages')">
              <div class="card-panel-icon-wrapper icon-message">
                <svg-icon icon-class="deviceMini" class-name="card-panel-icon" />
              </div>
              <div class="card-panel-description">
                <span class="card-panel-text" style="margin-bottom: 10px;margin-right: 10px">已启用</span>
                <count-to :start-val="0" :end-val="countList.equipmentEnableCount || 0" :duration="1000"
                  class="card-panel-num" />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="card-panel" @click="handleSetLineChartData('messages')">
              <div class="card-panel-icon-wrapper icon-message">
                <svg-icon icon-class="deviceMini" class-name="card-panel-icon" />
              </div>
              <div class="card-panel-description">
                <span class="card-panel-text" style="margin-bottom: 10px;margin-right: 10px">已停用</span>
                <count-to :start-val="0" :end-val="countList.equipmentDisableCount || 0" :duration="1000"
                  class="card-panel-num" />
              </div>
            </div>
          </el-col>
        </el-row>
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
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 26px;
    cursor: pointer;
    font-size: 12px;
    color: #666;
    background: #f0f2f5;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    border-radius: 12px;

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
        padding: 8px;
        transition: all 0.38s ease-out;
        border-radius: 4px;
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
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      font-weight: bold;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 20px;
        //margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width: 550px) {
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
