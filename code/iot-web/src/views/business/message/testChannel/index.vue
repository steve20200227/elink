<template>
  <div style="margin: 20px">

    <div style="margin-top: 5px">
      <el-form ref="gridHeadLayout" :inline="true" :model="formInline" class="demo-form-inline custom-form">
        <el-form-item>
          <el-input
            v-model="formInline.appName"
            size="mini"
            placeholder="请输入通道名称">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="formInline.channelType" size="mini" placeholder="请选择通道类型">
            <el-option
              v-for="item in channelDict"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="top-button">
          <el-button size="mini" @click="handleSearch" icon="el-icon-search"></el-button>
          <el-button size="mini" @click="handleEmpty" icon="el-icon-refresh-right"></el-button>
        </el-form-item>
        <el-button size="mini" type="primary" @click="handleAdd" class="add-button">新增通道</el-button>
      </el-form>
    </div>

    <!--  卡片组件  -->
    <div style="max-height: calc(100vh - 260px);height: calc(100vh - 260px);" v-if="tableData.length!=0">
      <el-row :gutter="10">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item, index) in tableData" :key="index">
          <el-card shadow="always" style="margin: 5px; padding-bottom: 10px;">
            <div style="position: relative; border-bottom: 1px solid #ccc; padding-bottom: 20px;height: 100px;">
              <span style="position:absolute; top: 10px">
                <!-- 根据 appType 的值显示不同的图片 -->
                <img style="height: 70px; width: 70px" :src="require(`@/assets/${item.channelType}.png`)" alt="图片"/>
                <!-- 添加更多条件根据需要显示不同的图片 -->
              </span>
              <span style="position: absolute; top: 0; left: 80px; margin-left: 10px">
                <strong style="font-size: 18px">{{
                    item.appName.length > 10 ? item.appName.substring(0, 10) + '...' : item.appName
                  }}</strong>
                <span
                  :style="{
                             display: 'inline-block',
                             height: '8px',
                             width: '8px',
                             background: item.appStatus === 1 ? 'rgb(10, 191, 91)' : 'rgb(229, 69, 69)',
                             borderRadius: '7px',
                             verticalAlign: 'middle',
                             marginLeft: '10px',
                             marginBottom: '5px'}">
                </span>
              </span>
              <el-tooltip class="item" effect="dark" content="编辑" placement="top">
                <a style="font-size: 14px; position: absolute; right: 0" @click="handleEdit(item)">
                  <span class="el-icon-setting" style="font-size: 18px;color: #1677FF"></span>
                </a>
              </el-tooltip>
              <span style="position: absolute; top: 39px; left: 80px; margin-left: 10px;font-size: 14px">
                <span style="color: #646a73">通道类型：</span>
                <span style="color: #1677ff;">
                  <dict-tag :type="DICT_TYPE.MESSAGE_CHANNEL" :value="item.channelType"></dict-tag>
                </span>
              </span>
              <span style="position: absolute; top: 69px; left: 80px; margin-left: 10px;font-size: 14px">
                <span style="color: #646a73">最新动态：</span>
                <span style="color: #1677ff;">{{ parseTime(item.updateTime) }}</span>
              </span>
            </div>
            <div style="margin-top: 10px;">
              <div style="float: left">
                <el-button size="small" @click="handleDisable(item)" v-if="item.appStatus === 1">禁 用</el-button>
                <el-button type="primary" @click="handlenable(item)" size="small" v-if="item.appStatus !== 1">启 用
                </el-button>
                <el-button size="small" @click="handleDelete(item)" v-if="item.appStatus !== 1">删 除</el-button>
              </div>
              <div style="float: right">
                <el-button type="text" @click="handleAppDescribe(item)" size="small">更多信息</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <!--  分页组件  -->
    <div class="block" style="float: right" >
      <el-pagination
        v-if="tableData.length!=0"
        background
        :current-page="page.current"
        :page-sizes="[12, 24, 36, 48, 120]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="changeSize"
        @current-change="changeCurrent"
        :total="page.total">
      </el-pagination>
    </div>
    <el-empty
      v-if="tableData.length==0"
      style="width: 100%; margin-top: 50px"
      description="暂无数据"
      :image-size="120"
    ></el-empty>
    <ChannelAddDialog
      ref="channelAdd"
      @restOnLoad="restOnLoad">
    </ChannelAddDialog>

    <AppDescribeDialog
      ref="appDescribe"
      v-if="appDescribeShow">
    </AppDescribeDialog>
  </div>
</template>

<script>
import ChannelAddDialog from "@/views/business/message/testChannel/component/channelAddDialog.vue";
import AppDescribeDialog from "@/views/business/message/testChannel/component/appDescribeDialog.vue";
import {deleteById, getPage, updateStatusById} from "@/api/message/channel";
import {parseTime} from "../../../../utils/ruoyi";

export default {
  // name: "testChannelIndex",
  components: {
    ChannelAddDialog,
    AppDescribeDialog
  },
  data() {
    return {
      appDescribeShow: false, // 更多信息弹窗
      formInline: {}, // 搜索过滤数据
      tableData: [], // 列表数据
      channelDict: [], // 通道字典
      page: {
        pageSize: 12,
        current: 1,
        total: 10
      },
    }
  },
  mounted() {
    this.channelDict = this.$store.state.dict.dictDatas.message_channel
    this.onLoad()
  },
  methods: {
    parseTime,
    handleAdd() {
      this.$refs.channelAdd.addChannel()
    },
    // 点击更多信息
    handleAppDescribe(row) {
      this.appDescribeShow = true
      this.$nextTick(() => {
        this.$refs.appDescribe.init(row)
      })
    },
    restOnLoad() {
      this.onLoad()
    },
    // 点击编辑按钮
    handleEdit(row) {
      this.$refs.channelAdd.editChannel(row)
    },
    // 点击删除按钮
    handleDelete(row) {
      this.$confirm("确定将选择数据删除", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        var data = {}
        data.ids = []
        data.ids.push(row.appId)
        deleteById(data).then(res => {
          this.$message.success("操作成功")
        })
      })
    },
    // 点击启用按钮
    handlenable(row) {
      const data = {}
      data.appId = row.appId
      data.appStatus = 1
      updateStatusById(data).then(res => {
        this.onLoad()
        this.$message.success("操作成功")
      })
    },
    // 点击禁用按钮
    handleDisable(row) {
      const data = {}
      data.appId = row.appId
      data.appStatus = 0
      updateStatusById(data).then(res => {
        this.onLoad()
        this.$message.success("操作成功")
      })
    },
    changeCurrent(val) {
      this.page.current = val
      this.onLoad()
    },
    changeSize(val) {
      this.page.pageSize = val
      this.onLoad()
    },
    handleSearch() {
      this.onLoad(this.formInline)
    },
    handleEmpty() {
      this.formInline = {}
      this.onLoad()
    },
    onLoad(params = {}) {
      params.currentPage = this.page.current
      params.pageSize = this.page.pageSize
      getPage(params).then(res => {
        const data = res.data
        this.tableData = data.records
        this.page.total = data.total
      })
    },
  }
};
</script>
<style scoped>
.custom-form {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
}

.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
}

.add-button {
  margin-top: -20px;
  margin-left: auto;
}

.box-card ::v-deep .el-divider--horizontal {
  margin: 0;
}
</style>
