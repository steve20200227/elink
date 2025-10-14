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
          <div style="padding: 5px 2px 2px 10px">
            <div class="point-head" style="position: relative;font-size: 18px; font-weight: bold; margin-bottom: 10px;">
              普通条件
            </div>
            <div class="point-content" style="margin-bottom: 10px">
              <div style="margin: 16px 6px 0 0px">
                <span style="color: #646a73">仅针对数据进行当前值或上一次值的判断</span>
              </div>
            </div>
          </div>
        </el-card>
        <el-card class="warningTypeCard" :class="{isActive:warningType == '2'}" shadow="always" @click.native="selectType('2')">
          <div style="padding: 5px 2px 2px 10px">
            <div class="point-head" style="position: relative;font-size: 18px; font-weight: bold; margin-bottom: 10px;">
              窗口条件
            </div>
            <div class="point-content" style="margin-bottom: 10px">
              <div style="margin: 16px 6px 0 0px">
                <span style="color: #646a73">仅针对数据进行指定时间内的平均值，最大值等窗口值的判断</span>
              </div>
            </div>
          </div>
        </el-card>
        <el-card class="warningTypeCard" :class="{isActive:warningType == '3'}" shadow="always" @click.native="selectType('3')">
          <div style="padding: 5px 2px 2px 10px">
            <div class="point-head" style="position: relative;font-size: 18px; font-weight: bold; margin-bottom: 10px;">
              设备事件
            </div>
            <div class="point-content" style="margin-bottom: 10px">
              <div style="margin: 16px 6px 0 0px">
                <span style="color: #646a73">仅针对设备事件进行操作!</span>
              </div>
            </div>
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
      title: '条件类型',
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
    openDialog(warningType) {
      this.warningType = warningType
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
