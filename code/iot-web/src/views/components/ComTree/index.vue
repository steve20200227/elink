<template>
  <div class="common_tree_handle">
    <el-header
      style="text-align: right; font-size: 12px;height:auto;min-height: 46px;background-color: #ffffff;padding:0 12px;display:flex;align-items:center;"
      ref="elHeader" v-if="titleShow">
      <el-row style="display:flex;align-items:center;width: 100%;line-height: 45px;border-bottom: 1px solid #cccccc;">
        <el-col :span="18" style="float: left;display:flex;align-items:center;">
          <el-tooltip effect="dark" :content="treeTitle || '类型分类'" placement="top-start">
            <label
              style="font-size: 14px; text-align: left;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">{{
                treeTitle === "" ? "类型分类" : treeTitle
              }}</label>
          </el-tooltip>
        </el-col>
        <el-col :span="6" v-if="isShowdig">
          <template>
            <el-popover width="40" v-model="visible">
              <p class="popv_p" @click="add()">新增</p>
              <p class="popv_p" @click="edit()">编辑</p>
              <p class="popv_p" @click="getdelete()">删除</p>
              <el-button slot="reference" type="text">
                <i class="el-icon-setting" style="font-size:18px;color:black"></i>
              </el-button>
            </el-popover>
          </template>
        </el-col>
      </el-row>
    </el-header>
    <el-row style="padding: 14px 12px;background: #FFFFFF;position: relative" v-if="isShowSearchInput">
      <el-input
        size="small"
        placeholder="输入关键字进行过滤"
        v-model="filterText"
        class="searchInput"
        :class="popoverShow?'inputWidth':''"
      >
      </el-input>
      <el-popover
        placement="right"
        width="80"
        v-if="popoverShow"
        trigger="hover">
        <el-button type="text" size="mini" style="font-size: 14px" @click="openAll" v-if="treeExpand == false && expandShow">展开全部</el-button>
        <el-button type="text" size="mini" style="font-size: 14px" @click="openAll" v-if="treeExpand == true && expandShow">收缩全部</el-button>
        <el-button type="text" size="mini" style="margin: 0;font-size: 14px" v-if="isInclude == false" @click="includeDown">包含下级</el-button>
        <el-button type="text" size="mini" style="margin: 0;font-size: 14px" v-if="isInclude == true" @click="includeDown">不包含下级</el-button>
        <el-button slot="reference" type="primary" icon="el-icon-more" class="moreBtn"></el-button>
      </el-popover>
    </el-row>
    <el-tree
      ref="commonTree"
      v-loading="loading"
      :data="treeChangeData"
      :props="defaultProps"
      :default-expand-all="treeExpand"
      :highlight-current="true"
      :show-checkbox="showCheckbox"
      :check-strictly="checkStrictly"
      :default-expanded-keys="defaultExpandedKeys"
      :expand-on-click-node="expandOnClickNode"
      :default-checked-keys="defaultCheckedKeys"
      :current-node-key="currentNodeKey"
      :node-key="nodeKey"
      @node-click="nodeClick"
      @check-change="handleCheckChange"
      :filter-node-method="filterNode"
      style="font-size: 12px;border-radius: 4px;padding:0 5px 0 12px"
    >
      <span slot-scope="{ node, data }" style="display: flex">
                        <i v-if="risk" :class="data.isSource ? 'el-icon-message-solid' : 'el-icon-price-tag'" style="line-height: 28px"></i>
                        <i v-else-if="isLeafIcons" :class="data[iconsFlied] ? 'el-icon-message-solid' : 'el-icon-price-tag'" style="line-height: 28px"></i>
                        <i v-else-if="data.children" class="el-icon-price-tag" style="line-height: 28px"></i>
                        <i v-else-if="data.category == 1" class="el-icon-price-tag" style="line-height: 28px"></i>
                        <i v-else-if="data.category == 'all'" class="el-icon-star-off" style="line-height: 28px"></i>
                        <i v-else class="el-icon-document" style="line-height: 28px"></i>

        <!--        <el-tooltip effect="dark" :content="node.label">-->
            <el-tooltip :content="node.label" placement="bottom">
              <span style="margin-left: 10px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis; width: 200px;display: block">{{ node.label }}</span>
            </el-tooltip>
        <!--          </el-tooltip>-->
                  </span>
    </el-tree>
    <!--  -->
  </div>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      filterText: '',
      isInclude:false,
      defaultExpandAll: false,
      treeChangeData: [],
      nodeData: {},
    }
  },

  // treeData            树展示的数据
  // defaultProps        树展示的数据格式
  // searchTitle         过滤的字段名称
  // isShowdig           是否展示搜索旁边的下拉
  // showCheckbox        是否开启多选
  // isShowSearchInput   是否显示搜索框
  // @getTreeAdd         下拉新增方法
  // @getTreeEdit        下拉编辑方法
  // @getTreeDelete      下拉删除方法
  // @getNodeClick       节点点击方法
  // @getHandleCheckChange  多选节点选中状态发生变化时的回调

  props: {
    nodeKey: {
      type: String,
    },
    loading: {
      type: Boolean,
      default: false,
    },
    treeTitle: {
      type: String,
      default: ""
    },
    treeData: {
      type: Array,
    },
    treeForm: {
      type: Object,
      default: () => ({})
    },
    treeFormOption: {
      type: Object,
      default: () => ({})
    },
    defaultProps: {
      type: Object,
    },
    treeExpand: {
      type: Boolean,
      default: true,
    },
    expandShow:{
      type: Boolean,
      default: false,
    },
    popoverShow: {
      type: Boolean,
      default: false,
    },
    checkStrictly: {
      type: Boolean,
      default: false,
    },
    expandOnClickNode:{
      type: Boolean,
      default: true,
    },
    defaultCheckedKeys:{
      type: Array,
    },
    defaultExpandedKeys:{
      type: Array,
    },
    currentNodeKey: {
      type: String
    },
    searchTitle: {
      type: String,
      default: ""
    },
    showCheckbox: {
      type: Boolean,
      default: false,
    },
    isShowdig: {
      type: Boolean,
      default: false,
    },
    isShowSearchInput: {
      type: Boolean,
      default: true
    },
    titleShow: {
      type: Boolean,
      default: true
    },
    risk:{
      type:Boolean, // 区分子节点和父节点不同类型的图标（风险管理）
      default:false
    },
    isLeafIcons:{
      type:Boolean, // 区分子节点和父节点不同类型的图标（通用版本）
      default:false
    },
    iconsFlied:{
      type:String,// 区分子节点和父节点不同类型的图标（通用版本）
      default:''
    }
  },
  methods: {
    openAll() {
      this.treeExpand = !this.treeExpand;
    },
    includeDown() {
      this.isInclude = !this.isInclude
      this.$emit("include-down", this.isInclude);
    },
    // 新增
    add() {
      this.$emit('getTreeAdd', this.nodeData)
      this.visible = false;
    },
    // 编辑
    edit() {
      if (this.nodeData.id) {
        this.$emit('getTreeEdit', this.nodeData)
      } else {
        this.$message({
          message: "请选择要编辑的数据",
          type: 'warning'
        });
      }

      this.visible = false;
    },
    // 删除
    getdelete() {
      if (this.nodeData.id) {
        this.$emit('getTreeDelete', this.nodeData)
      } else {
        this.$message({
          message: "请选择要删除的数据",
          type: 'warning'
        });
      }
      this.visible = false;
    },
    nodeClick(data, node) {
      // this.isInclude = false;
      this.nodeData = data;
      this.$emit('getNodeClick', this.nodeData, node)
    },
    // 多选
    handleCheckChange(data, choosed) {
      this.$emit('getHandleCheckChange', data, choosed)
    },

    filterNode(value, data) {
      if (!value) return true;

      return data[this.searchTitle].indexOf(value) !== -1;
    },
    /**
     * 递归过滤嵌套数组（树形结构）
     * @param arr {Array}要过滤的原始数据
     * @param teamName {Boolean} 你要过滤的关键词
     *
     */
    recursiveFilter(arr, teamName) {
      let result = []
      arr.forEach((item1) => {

        if (item1[this.searchTitle].indexOf(teamName) != -1) {
          result.push(item1)
          if (item1.children) {
            //递归循环
            item1.children = this.recursiveFilter(item1.children, teamName)
          }
        } else {
          if (item1.children) {
            //递归循环
            item1.children = this.recursiveFilter(item1.children, teamName)
          }
        }
      })
      this.treeChangeData = result;
      return result;
      // this.searchResult = result
      // let data = arr.filter(item => item[this.searchTitle].indexOf(teamName)).map((item) => {
      //   item = Object.assign({}, item)
      //   if (item.children) {
      //     //递归循环
      //     item.children = this.recursiveFilter(item.children, teamName)
      //   }
      //   return item
      // })
      // this.treeChangeData = data;
      // return data
    },
    checkNode(val) {
      this.$nextTick(() => {
        this.$refs.commonTree.setCurrentNode(this.nodeData)
      })
    }

  },
  watch: {
    filterText(val) {
      this.$refs.commonTree.filter(val);
    },

    // filterText(n, o) {
    //   if (n && n.length > 0) {
    //     this.recursiveFilter(this.treeData, n);
    //   } else {
    //     this.treeChangeData = this.treeData;
    //   }
    // },
    treeData(n) {
      if (n) {
        this.treeChangeData = n;
      }
    },
    currentNodeKey(val){
      console.log("========",val)
      if(val){
        this.$nextTick(() => {
          this.$refs.commonTree.setCurrentKey(val)
        })
      }
    }
  },
  mounted() {
    this.treeChangeData = this.treeData;
  },
}
</script>

<style lang='scss' scoped>
.inputWidth {
  width: calc(100% - 34px);
}
.common_tree_handle {
  width: 280px;
  // padding-right: 12px;
  box-sizing: border-box;
  border-right: 1px solid #cccccc;
  background: #ffffff;
  flex-shrink: 0;
}

.popv_p {
  line-height: 32px;
  text-align: center;
  margin: 0;
  padding: 0;
  cursor: pointer;
}

.searchInput {
  // padding: 5px 0 6px;
  font-size: 12px;

  ::v-deep input {
    height: 28px;
  }
}
.moreBtn {
  position: absolute;
  top: 14px;
  right: 9px;
  transform: rotate(90deg);
  padding: 7px;
}
</style>
<style lang='scss' scoped>
.common_tree_handle {
  .el-tree {
    /* height:100% !important; */
    height: calc(100vh - 300px) !important;
    overflow-y: scroll;
  }
}

::v-deep .el-tree-node__content {
  line-height: 26px;
}

.el-popover {
  min-width: 80px !important;
}

::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background-color: rgba(135, 206, 235, 0.2);
  color: #409eff; // 节点的字体颜色
  font-weight: bold; // 字体加粗
}

</style>
