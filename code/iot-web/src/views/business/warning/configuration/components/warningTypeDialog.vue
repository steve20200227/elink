<template>
  <div class="warning_dialog">
    <el-dialog
      :title="title"
      :visible.sync="dialogFlag"
      class="avue-dialog avue-dialog--top"
      width="25%"
      @close="cancel">
      <div style="margin: 10px 0">
        <el-card class="warningTypeCard" :class="{isActive:warningType == '1'}" shadow="always" @click.native="selectType('1')">
          <div style="position: relative; height: 100px;">
            <span style="position:absolute; top: 15px; left: 10px;">
              <!-- 根据 场景类型 的值显示不同的图片 -->
              <img style="height: 70px; width: 70px" src="@/assets/whereType.png" />
            </span>
            <span style="position: absolute; top: 15px; left: 110px; margin-left: 10px; font-weight: bold; font-size: 20px">
              <span style="color: #646a73">条件</span>
            </span>
            <span style="position: absolute; top: 60px; left: 110px; margin-left: 10px;font-size: 14px">
              <span style="color: #1677ff">适用于满足指定条件进行动作执行的场景，常见于设备告警。</span>
            </span>
          </div>
        </el-card>
        <el-card class="warningTypeCard" :class="{isActive:warningType == '2'}" shadow="always" @click.native="selectType('2')">
          <div style="position: relative; height: 100px;">
            <span style="position:absolute; top: 15px; left: 10px;">
              <!-- 根据 场景类型 的值显示不同的图片 -->
              <img style="height: 70px; width: 70px" src="@/assets/taskType.png" />
            </span>
            <span style="position: absolute; top: 15px; left: 110px; margin-left: 10px; font-weight: bold; font-size: 20px">
              <span style="color: #646a73">定时</span>
            </span>
            <span style="position: absolute; top: 60px; left: 110px; margin-left: 10px;font-size: 14px">
              <span style="color: #1677ff">适用于周期性执行动作的场景。例如：定时启动或关闭设备。</span>
            </span>
          </div>
        </el-card>
        <el-card class="warningTypeCard" :class="{isActive:warningType == '3'}" shadow="always" @click.native="selectType('3')">
          <div style="position: relative; height: 100px;">
            <span style="position:absolute; top: 15px; left: 10px;">
              <!-- 根据 场景类型 的值显示不同的图片 -->
              <img style="height: 70px; width: 70px" src="@/assets/shouType.png" />
            </span>
            <span style="position: absolute; top: 15px; left: 110px; margin-left: 10px; font-weight: bold; font-size: 20px">
              <span style="color: #646a73">手动</span>
            </span>
            <span style="position: absolute; top: 60px; left: 110px; margin-left: 10px;font-size: 14px">
              <span style="color: #1677ff">适用于第三方平台向物联网平台下发指令控制设备。例如：用户通过点击手动触发按钮，实现开启空调设备。</span>
            </span>
          </div>
        </el-card>
      </div>

      <div style="float: right">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" type="primary" @click="submit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "add",
  data() {
    return {
      title: '场景触发类型',
      dialogFlag: false,
      warningType: 1,
    }
  },
  methods: {
    selectType(warningType) {
      this.warningType = warningType;
    },
    submit () {
      this.$emit('submit', this.warningType);
      this.dialogFlag = false;
    },
    cancel () {
      this.dialogFlag = false;
    },
    openDialog() {
      this.dialogFlag = true
    },
  }
}
</script>
<style>
.warningTypeCard {
  font-size: 14px;
  margin-right: 10px;
  margin-bottom: 10px;
}
.isActive {
  border: 1px solid #409EFF;
}

</style>
