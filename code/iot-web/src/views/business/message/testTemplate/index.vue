<template>
  <div style="margin: 20px">
    <div style="margin-top: 5px">
      <el-form ref="gridHeadLayout" :inline="true" :model="formInline" class="demo-form-inline custom-form">
        <el-form-item>
          <el-input
            v-model="formInline.templateName"
            size="mini"
            placeholder="请输入模板名称">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="formInline.pushRange" size="mini" placeholder="请选择推送范围">
            <el-option
              v-for="item in pushMethodDict"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="formInline.usersType" size="mini" placeholder="请选择用户类型">
            <el-option
              v-for="item in userTypeDict"
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
        <el-button size="mini" type="primary" @click="handleAdd" class="add-button">新增模板</el-button>
      </el-form>
    </div>

    <div>
      <div style="height: calc(100vh - 285px);" v-if="tableData.length!=0">
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item, index) in tableData" :key="index">
            <el-card shadow="always" style="margin: 5px; padding-bottom: 10px;">
              <div style="position: relative; border-bottom: 1px solid #ccc; padding-bottom: 20px;height: 100px;">
              <span style="position:absolute; top: 10px">
                <!-- 根据 appType 的值显示不同的图片 -->
                <img style="height: 70px; width: 70px" :src="require(`@/assets/${item.pushWays.channelType}.png`)" alt="图片" />
                <!-- 添加更多条件根据需要显示不同的图片 -->
              </span>
                <span style="position: absolute; top: 0; left: 80px; margin-left: 10px">
                <strong style="font-size: 18px">{{ item.templateName.length > 10 ? item.templateName.substring(0, 10) + '...' : item.templateName }}</strong>
                <span
                  :style="{
                             display: 'inline-block',
                             height: '8px',
                             width: '8px',
                             background: item.templateStatus === 1 ? 'rgb(10, 191, 91)' : 'rgb(229, 69, 69)',
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
                <span style="color: #646a73">消息类型：</span>
                <span style="color: #1677ff;">
                  {{ messageTypeList.find(demo => demo.id == item.pushWays.messageType).name }}
                </span>
              </span>
                <span style="position: absolute; top: 69px; left: 80px; margin-left: 10px;font-size: 14px">
                  <span style="color: #646a73">通道类型：</span>
                  <span style="color: #1677ff;">
                    <dict-tag :type="DICT_TYPE.MESSAGE_CHANNEL" :value="item.pushWays.channelType" />
                  </span>
                </span>
              </div>
              <div style="margin-top: 10px;">
                <div style="float: left">
                  <el-button type="primary" size="small" @click="updateStatus(item)" v-if="item.templateStatus == 1">禁 用</el-button>
                  <el-button type="primary" @click="updateStatus(item)" size="small" v-if="item.templateStatus == 0">启 用</el-button>
                  <el-button @click="handleClone(item)" size="small">克 隆</el-button>
                  <el-button size="small" @click="handleDeleted(item)" v-if="item.templateStatus == 0">删 除</el-button>
                </div>
                <div style="float: right">
                  <el-button type="text" @click="handleView(item)" size="small">更多信息</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <el-pagination
        v-if="tableData.length!=0"
        background
        :current-page="page.currentPage"
        :page-sizes="[12, 24, 36, 48, 120]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total"
        :page-size="page.pageSize"
        :pager-count="5"
        @size-change="changeSize"
        @current-change="changeCurrent"
        style="float: right; margin: 20px 0;">
      </el-pagination>
      <el-empty
        v-if="tableData.length==0"
        style="width: 100%; margin-top: 50px"
        description="暂无数据"
        :image-size="120"
      ></el-empty>
    </div>

    <TemplateAddDialog
      ref="templateAdd"
      v-if="templateAddShow"
      @restOnLoad="restOnLoad">
    </TemplateAddDialog>
  </div>
</template>

<script>
import {deleteByIds, getPage, updateStatusById} from '@/api/message/template'
import TemplateAddDialog from "@/views/business/message/testTemplate/component/templateAddDialog.vue";

export default {
  name: "templateIndex",
  components: {
    TemplateAddDialog
  },
  data() {
    return {
      tableLoading: false,
      templateAddShow: false,
      formInline: {}, // 搜索过滤数据
      tableData: [], // 列表数据
      selectionList: [], // 选择列表数据
      pushMethodDict: [], // 推送方式字典
      userTypeDict: [], // 用户类型字典
      page: {
        pageSize: 12 ,
        currentPage: 1,
        total: 0
      },
      messageTypeList: [
        { id: '1', name: '文本消息' },
        //
        { id: '4-1', name: '钉钉-图片消息' },
        { id: '4-2', name: '钉钉-语音消息' },
        { id: '4-3', name: '钉钉-文件消息' },
        { id: '4-4', name: '钉钉-链接消息' },
        { id: '4-5', name: '钉钉-OA 消息' },
        { id: '4-6', name: '钉钉-markdown 消息' },
        { id: '4-7', name: '钉钉-卡片消息' },
        //
        { id: '5-1', name: '企业微信-图片消息' },
        { id: '5-2', name: '企业微信-语音消息' },
        { id: '5-3', name: '企业微信-视频消息' },
        { id: '5-4', name: '企业微信-文件消息' },
        { id: '5-5', name: '企业微信-文本卡片消息' },
        { id: '5-6', name: '企业微信-图文消息(mpnews)' },
        { id: '5-7', name: '企业微信-markdown 消息' },
        { id: '5-8', name: '企业微信-小程序通知消息' },
        //
        { id: '6-1', name: '飞书-富文本 post' },
        { id: '6-2', name: '飞书-图片 image' },
        { id: '6-3', name: '飞书-消息卡片 interactive' },
        { id: '6-4', name: '飞书-分享群名片 share_chat' },
        { id: '6-5', name: '飞书-分享个人名片 share_user' },
        { id: '6-6', name: '飞书-语音 audio' },
        { id: '6-7', name: '飞书-视频 media' },
        { id: '6-8', name: '飞书-文件 file' },
        { id: '6-9', name: '飞书-表情包 sticker' }
      ]
    }
  },
  mounted() {
    this.pushMethodDict = this.$store.state.dict.dictDatas.message_push_method
    this.userTypeDict = this.$store.state.dict.dictDatas.message_user_type
    this.onLoad()
  },
  methods: {
    // 点击新增
    handleAdd() {
      this.templateAddShow = true
      this.$nextTick(() => {
        this.$refs.templateAdd.addTemplate()
      })
    },
    // 点击编辑
    handleEdit(row) {
      const data = {...row}
      this.templateAddShow = true
      this.$nextTick(() => {
        this.$refs.templateAdd.editTemplate(data)
      })
    },
    handleView(row) {
      const data = {...row}
      this.templateAddShow = true
      this.$nextTick(() => {
        this.$refs.templateAdd.viewTemplate(data)
      })
    },
    // 点击克隆
    handleClone(row) {
      const data = {...row }
      data.id = null
      data.templateId = null
      this.templateAddShow = true
      this.$nextTick(() => {
        this.$refs.templateAdd.cloneTemplate(data)
      })
    },
    // 点击启用禁用
    updateStatus(row) {
      let data = {}
      data.templateId = row.templateId
      if (row.templateStatus == 0) {
        data.templateStatus = 1
      } else if (row.templateStatus == 1) {
        data.templateStatus = 0
      } else {
        this.$message.error("参数异常")
        return
      }
      updateStatusById(data).then(res => {
        if (res.code == 0) {
          this.$message.success("操作成功")
          this.onLoad()
        } else {
          this.$message.error("操作失败")
        }
      })

    },
    // 点击行删除
    handleDeleted(row) {
      this.$confirm("确定将选择数据删除", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        let ids = []
        ids.push(row.templateId)
        deleteByIds({ids: ids}).then(res => {
          if (res.code === 0) {
            this.$message.success("删除成功")
            this.onLoad()
          } else {
            this.$message.error("删除失败")
          }
        })
      })
    },
    // 点击删除（批量删除）
    handleDeleteds() {
      if (this.selectionList.length === 0) {
        this.$message.warning("请选择数据");
        return;
      }
      this.$confirm("确定将选择数据删除", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
          let ids = this.selectionList.map(item => {
            return item.templateId
          })
        deleteByIds({ids: ids}).then(res => {
          if (res.code === 0) {
            this.$message.success("删除成功")
            this.onLoad()
          } else {
            this.$message.error("删除失败")
          }
        })
      })

    },
    restOnLoad() {
      this.templateAddShow = false
      this.onLoad()
    },
    changeCurrent (val) {
      this.page.currentPage = val
      this.onLoad()
    },
    changeSize (val) {
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
    // 序号
    indexMethod (index) {
      return index + 1
    },
    // 列表选择
    handleSelectionChange (selection) {
      this.selectionList = selection
    },
    onLoad(params = {}) {
      params.currentPage = this.page.currentPage
      params.pageSize = this.page.pageSize
      this.tableLoading = true
      getPage(params).then(res => {
        const data = res.data
        this.tableData = data.records
        this.tableData.forEach(item => {
          item.pushWays = JSON.parse(item.pushWays)
        })
        this.page.total = data.total
        this.tableLoading = false
      })
    }
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

</style>
