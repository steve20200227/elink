<template>
  <div style="margin: 20px">
    <div style="margin: 10px 20px">
      <el-form ref="dataForm" :rules="rules" :inline="true" :model="dataForm" label-width="100px" class="product-form">
        <div>
          <el-row>
            <el-col :span="12">
              <el-form-item label="分类名称:" prop="sortName">
                <el-input v-model="dataForm.sortName" :disabled="type == 'view'" size="small" style="width: 90%;"
                          placeholder="请输入分类名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="type === 'add'">
              <el-form-item label="是否根节点:" prop="isRootNode">
                <el-select v-model="dataForm.isRootNode" :disabled="type == 'view'" size="small" style="width: 90%;"
                           placeholder="请选择是否选择为根节点">
                  <el-option
                    v-for="item in isRootNodeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </div>
    <div class="dialog-footer">
      <el-button style="margin-top: 12px" @click="headSave">保存</el-button>
      <el-button style="margin-top: 12px" @click="sortDialogClose">取消</el-button>
    </div>
  </div>
</template>

<script>
import {getTree, save, detail} from '@/api/cps/product-sort.js';

export default {
  props: {
    rowData: {
      type: Object,
      required: true,
    },
  },
  name: 'add',
  data() {
    return {
      isRootNode: '',
      isRootNodeOption: [],
      type: 'edit',
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      parentId: '',
      dataForm: {
        sortName: '',
        superiors: '',
      },
      rules: {
        sortName: [
          {
            required: true,
            message: '请输入分类名称',
            trigger: 'blur'
          },
          { max: 50, message: '输入内容超出限制，请重新编辑。', trigger: 'blur' }
        ],
        isRootNode: [
          {
            required: true,
            message: '请选择是否选择为根节点',
            trigger: 'blur'
          }
        ],
      }
    }
  },
  mounted() {
    this.isRootNodeOption = this.$store.state.dict.dictDatas.yes_or_no
    getTree().then((res) => {
      this.treeData = res.data;
    })
  },
  methods: {
    init(parentId, type) {
      this.type = type
      this.parentId = parentId
      if (this.type === 'edit') {
        detail(this.parentId).then((res) => {
          this.dataForm = res.data
          this.isRootNode = this.dataForm.parentId == 0 ? '1' : '0'
        })
      }
    },
    sortDialogClose() {
      this.dataForm = {};
      this.$emit('sortDialogClose');
    },
    headSave() {
      if (this.type === 'add') {
        this.dataForm.parentId = this.dataForm.isRootNode == '1' ? null : this.parentId
      } else {
        this.dataForm.id = this.parentId;
      }
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          save(this.dataForm).then((res) => {
            this.dataForm = res.data;
            this.$message.success("操作成功！")
            this.sortDialogClose()
          })
        }
      })
    },
  }
}
</script>
<style scoped lang="scss">
.product-form ::v-deep .el-textarea__inner {
  resize: none;
  min-height: 111.6px !important;
}

.product-form ::v-deep .el-form-item {
  width: 100%;
}

.product-form ::v-deep .el-form-item__content {
  width: calc(100% - 100px);
}

.product-form ::v-deep .el-form-item__label {
  color: #3f4448;
  font-weight: 400;
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: center;
  padding: 12px 0;
  border-top: 1px solid rgb(204, 204, 204);
}

.center-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 30px;
}
</style>
