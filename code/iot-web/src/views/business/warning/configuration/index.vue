<template>
  <div style="margin: 20px">

    <div
      style="display: flex; justify-content: space-between; font-size: 16px; padding-bottom: 5px">
      <div class="warningForm" style="margin-top: 5px">
        <el-form ref="gridHeadLayout" :inline="true" :model="formInline" class="demo-form-inline">
          <el-form-item>
            <el-input
              v-model="formInline.warningName"
              size="mini"
              placeholder="请输入场景名称">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="formInline.isEnable" size="mini" placeholder="请选择是否启用">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="top-button">
            <el-button size="mini" icon="el-icon-search" @click="handleSearch"></el-button>
            <el-button size="mini" icon="el-icon-refresh-right" @click="handleEmpty"></el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="top-button">
        <el-button size="mini" type="primary" @click="add()">新增场景</el-button>
      </div>
    </div>

    <div style="max-height: calc(100vh - 245px);height: calc(100vh - 245px); overflow: auto;" v-if="tableData.length!=0">
      <el-row style="margin-top: 20px">
          <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item, index) in tableData" :key="index">
            <el-card shadow="always" :class="{'record-success': item.isEnable == 1 && item.warningType != '3', 'record-danger': item.isEnable == 0}" style="margin: 5px; padding-bottom: 10px;">
              <div style="position: relative; border-bottom: 1px solid #ccc; padding-bottom: 20px;height: 110px;">
                <span style="position:absolute; top: 15px; left: 10px;">
                  <!-- 根据 场景类型 的值显示不同的图片 -->
                  <img style="height: 70px; width: 70px" v-if="item.warningType == '1'" src="@/assets/whereType.png" />
                  <img style="height: 70px; width: 75px" v-else-if="item.warningType == '2'" src="@/assets/taskType.png" />
                  <img style="height: 70px; width: 70px" v-else src="@/assets/shouType.png" />
                  <!-- 添加更多条件根据需要显示不同的图片 -->
                </span>
                <el-tooltip v-if="item.isEnable == 0 || item.warningType == 3" class="item" effect="dark" content="编辑" placement="top">
                  <a style="font-size: 14px; position: absolute; right: 0" @click="handleEdit(item)">
                    <span class="el-icon-setting" style="font-size: 18px;color: #1677FF"></span>
                  </a>
                </el-tooltip>
                <span style="position: absolute; top: 10px; left: 110px; margin-left: 10px; font-size: 24px;
                white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
                width: 230px;color: #646a73;">
                  {{ item.warningName }}
                </span>
                <span style="position: absolute; top: 54px; left: 110px; margin-left: 10px;font-size: 14px">
                  <span style="color: #646a73">类型：</span>
                  <span style="color: #1677ff">{{ warningTypeMap.get(item.warningType) }}</span>
                </span>
                <span style="position: absolute; top: 77px; left: 110px; margin-left: 10px;font-size: 14px">
                  <span style="color: #646a73">优先级：</span>
                  <span style="color: #1677ff">{{ priorityMap.get(item.priority) }}</span>
                </span>
              </div>
              <div style="margin-top: 10px;">
                <div style="float: left">
                  <el-button size="mini" type="primary" v-if="item.isEnable == 1 && item.warningType != 3" style="margin: 5px"
                             @click="handleRestart(item)">重启
                  </el-button>
                  <el-button size="mini" type="primary" v-if="item.isEnable == 0 && item.warningType != 3" style="margin: 5px"
                             @click="rowEnable(item)">启用
                  </el-button>
                  <el-button size="mini" type="primary" v-if="item.warningType == 3" style="margin: 5px"
                             @click="rowRun(item)">执行
                  </el-button>
                  <el-button size="mini" v-if="item.isEnable == 1 && item.warningType != 3" style="margin: 5px"
                             @click="rowClose(item)">禁用
                  </el-button>
                  <el-button size="mini" v-if="item.isEnable == 0 || item.warningType == 3" style="margin: 5px"
                             @click="handleRemove(item)">删除
                  </el-button>
                </div>
                <div style="float: right">
                  <el-button type="text" @click="handleView(item)" size="small">更多信息</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
      </el-row>
    </div>
    <div class="block" style="float: right;" v-if="tableData.length!=0">
      <el-pagination
        background
        :current-page="page.currentPage"
        :page-sizes="[12, 24, 36, 48, 120]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      >
      </el-pagination>
    </div>
    <el-empty
      v-if="tableData.length==0"
      style="width: 100%; margin-top: 50px"
      description="暂无数据"
      :image-size="120"
    ></el-empty>

    <div v-if="progressDialog" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <h2>执行进度</h2>
        <div class="progress-container">
          <div class="progress-bar" :style="{ width: progress + '%' }"></div>
        </div>
        <p>{{ progress }}%</p>
      </div>
    </div>
    <warningTypeDialog ref="warningTypeDialog" @submit="handleAdd">
    </warningTypeDialog>
  </div>
</template>

<script>
import {page as getPage, remove, updateStatus, restart} from '@/api/warning/warning-api'
import warningTypeDialog from './components/warningTypeDialog.vue'

export default {
  name: "warningConfig",
  data() {
    return {
      progressDialog: false,
      progress: 0,
      interval: null,
      formInline: {},
      tableData: [],
      options: [],
      priorityMap: new Map(),
      warningTypeMap: new Map(),
      warningCount: 0,
      page: {
        pageSize: 12,
        currentPage: 1,
        total: 0
      }
    }
  },
  components:{
    warningTypeDialog
  },
  mounted() {
    this.priorityDict = this.$store.state.dict.dictDatas.warning_priority;
    this.priorityMap = this.priorityDict.reduce((m, current) => {
      m.set(current.value * 1, current.label);
      return m;
    }, new Map());
    this.warningTypeMap.set("1", "条件触发");
    this.warningTypeMap.set("2", "定时触发");
    this.warningTypeMap.set("3", "手动触发");
    this.options = this.$store.state.dict.dictDatas.is_enable
    this.onLoad(this.page)
  },
  methods: {
    openModal() {
      this.progressDialog = true;
      this.progress = 0;
      this.startProgress();
    },
    closeModal() {
      this.progressDialog = false;
      clearInterval(this.interval);
    },
    startProgress() {
      this.interval = setInterval(() => {
        let num = Math.random() * (15 - 0) + 0;
        //let num = Math.floor(Math.random() * (70 - 10 + 1)) + 10; // 生成随机数；
        //this.progress += num;
        if (this.progress >= 80){
          num = Math.random() * (3 - 0) + 0;
        }

        if (this.progress >= 90){
          num = Math.random() * (2 - 0) + 0;
        }

        if (this.progress >= 99){
          num = 0;
        }
        this.progress += parseInt(num)
        if (this.progress >= 100) {
          this.progress = 100
          clearInterval(this.interval);
          setTimeout(() => {
            this.closeModal();
          }, 500); // 延时0.5秒执行关闭进度条；
        }
      }, 500);
    },
    handleSizeChange(item) {
      this.page.pageSize = item
      this.onLoad(this.page)
    },
    handlePageChange(item) {
      this.page.currentPage = item
      this.onLoad(this.page)
    },
    // 手动场景执行按钮方法
    rowRun(row) {
      this.$confirm("确认执行场景: " + row.warningName + "?", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "info"
      }).then(() => {
        // 开启执行进度条
        this.openModal();
        this.rowEnable(row);
      })
    },
    rowEnable(row) {
      let data = {
        id : row.id,
        isEnable : 1
      };
      updateStatus(data).then((res) => {
        //如果是手动执行  调整进度条
        if (row.warningType == 3){
          if (res.code != 0) {
            this.$message.warning(res.data.msg);
          } else {
            this.$message.success("执行成功！");
            this.progress = 100
          }
        }
        if (row.warningType != 3) {
          if (res.code != 0) {
            this.$message.warning(res.data.msg);
          } else {
            this.$message.success("启用成功！");
          }
        }
        this.onLoad(this.page)
      }).catch((error)=>{
        this.progress = 100
      })
    },
    handleRestart(row) {
      restart(row.id).then((res) => {
        if (res.code == 0) {
          this.$message.success("重启成功！");
          this.onLoad(this.page)
        } else {
          this.$message.warning(res.data.msg);
        }
      })
    },
    rowClose(row) {
      let data = {
        id : row.id,
        isEnable : 0
      };
      updateStatus(data).then((res) => {
        if (res.code != 0) {
          this.$message.warning(res.data.msg);
        } else {
          this.$message.success("禁用成功！");
        }
        this.onLoad(this.page)
      })
    },
    add() {
      this.$refs.warningTypeDialog.openDialog();
    },
    handleAdd(warningType) {
      this.$router.push({
        path: `/warning/configuration/Add`,
        query: {
          type: 'add',
          warningType
        }
      })
    },
    handleEdit(item) {
      this.$router.push({
        path: `/warning/configuration/Add`,
        query: {
          type: 'edit',
          id: item.id,
        }
      })
    },
    handleView(item) {
      this.$router.push({
        path: `/warning/configuration/Add`,
        query: {
          type: 'view',
          id: item.id
        }
      })
    },
    handleRemove(item) {
      this.$confirm("确定将选择数据删除", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        remove(item.id).then(res => {
          this.$message.success("删除成功！");
          this.onLoad(this.page)
        })
      })
    },
    // 鼠标在盒子上移动
    onMouseOver(str, index) {
      const tag = this.$refs[str][index]
      if (tag) {
        const parentWidth = tag.parentNode.offsetWidth // 获取元素父级可视宽度
        const contentWidth = tag.offsetWidth // 获取元素可视宽度
        this.isTextOverflow = contentWidth <= parentWidth
      }
    },
    parseTime(item) {
      if (!item.createTime) return '';
      const date = new Date(item.createTime);
      if (isNaN(date.getTime())) return '';
      return date.toLocaleString();
    },
    handleSearch() {
      this.page.currentPage = 1
      this.onLoad(this.page, this.formInline)
    },
    handleEmpty() {
      this.formInline = {}
      this.onLoad(this.page)
    },
    onLoad(page, params = {}) {
      this.page = page;
      getPage(
        page.currentPage,
        page.pageSize,
        Object.assign(params, this.formInline)
      ).then(res => {
        const data = res.data;
        this.page.total = data.total;
        this.warningCount = data.total;
        this.tableData = data.records;
      });
    },
  }
};
</script>
<style scoped>
.warningForm ::v-deep .el-form-item {
  margin-bottom: 0;
}

.top-button ::v-deep .el-button + .el-button {
  margin-left: 0;
}

.card {
  font-size: 14px;
  padding-bottom: 10px;
  margin-right: 10px;
  margin-bottom: 10px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
}

.card:hover {
  -webkit-box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  box-shadow: 0 20px 12px 0 rgba(0, 0, 0, 0.1);
}

.block {
  /* position: absolute; */
  bottom: 0;
  width: calc(100% - 48px);
  padding: 12px 24px;
  background-color: #ffffff;
  display: flex;
  flex-direction: row-reverse;
}
.record-danger {
  background: linear-gradient(40deg, #fff, #f4f4f5, #e9e9ed);
}

.record-success {
  background: linear-gradient(40deg, #fff, #eef6ff, #b3d9ff);;
}

.modal {
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 30%;
  text-align: center;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
  display: none;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

.progress-container {
  background: #e0e0e0;
  border-radius: 5px;
  margin: 20px 0;
  height: 20px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #4caf50, #81c784);
  transition: width 0.4s ease;
}

h2 {
  color: #333;
}
</style>
