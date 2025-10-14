<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col" v-for="(value, name, index) in handleSetLineChartData" :key="name">
      <div class="card-panel" > <!---->
        <div :class="iconsColor[index]">
          <!--          <svg-icon icon-class="peoples" class-name="card-panel-icon" />-->
          <svg-icon :icon-class="icons[index]" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            {{nameMap.get(name)}}
          </div>
          <count-to :start-val="0" :end-val="Number(value)" :duration="1000" class="card-panel-num" />
        </div>
      </div>
    </el-col>

  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'
const icons = ['message', 'skill', 'form', 'phone']
const iconsColor = ['card-panel-icon-wrapper icon-people', 'card-panel-icon-wrapper icon-message', 'card-panel-icon-wrapper icon-shopping', 'card-panel-icon-wrapper icon-money']
const globalMap = new Map()
globalMap.set('numberOfMessagesToday', '今日消息')
globalMap.set('numberOfPlatformFiles', '平台文件数')
globalMap.set('messageRecordTotal', '累计消息')
globalMap.set('accumulatedTemplateOwnership', '消息模板')
globalMap.set('numberOfApps', '消息通道')
globalMap.set('chartsMessageOption', '消息详情')
globalMap.set('chartsTemplateOption', '模板使用 TOP5')
globalMap.set('chartsAppOption', '消息通道使用 TOP5')
globalMap.set('chartsPushUserOption', '推送用户 TOP5')

export default {
  name:"PanelGroupMessage",
  props:{
    handleSetLineChartData: {
      type: Object,
      default:{}
    }
  },
  components: {
    CountTo
  },
  data() {
    return {
      nameMap:globalMap,
      icons:icons,
      iconsColor:iconsColor,

    }
  }
}
</script>


<style lang="scss" scoped>
.panel-group {
  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    border-radius: 12px;
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
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

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px 26px 26px 0;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
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
