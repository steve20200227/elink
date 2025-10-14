<template>
  <el-container class="el-container">
    <el-main>
      <div class="container">
        <div class="content">
          <h3>数据输入</h3>
          <div
            style="margin-top: 5px; margin-bottom: 10px; border: 1px solid #dcdfe6; background-color: #ffffff; border-radius: 8px; padding: 20px;">
            <ul style="display: flex; align-items: center; flex-wrap: wrap;">
              <li
                v-for="(item, index) in dataForm.cpsDataEntries"
                :key="index"
                style="list-style: none; margin-right: 10px; margin-bottom: 5px;"> <!-- 添加margin-bottom以处理多行展示时的间距 -->
                <el-tag type="primary" effect="dark" size="max" @click="dataInputClick(item, index)">
                  {{ item.topic }}
                  <i class="el-icon-close"
                     style="cursor: pointer; "
                     @click.stop.prevent="deleteDataInput(item, index)"></i>
                </el-tag>
              </li>
              <li style="list-style: none;">
                <i class="el-icon-circle-plus" @click="drawer = true" style="font-size: 24px;"></i>
              </li>
            </ul>
          </div>
          <h3>SQL 编辑器</h3>
          <div id="display-content">
            了解更多 SQL 语法，请参考
            <span style="color: green; cursor: pointer;" @click="sqlGrammarClick">SQL 语法与示列。</span>
            <div id="sqlEditor" style="height: 300px; width: 100%; margin-top: 10px"></div>
          </div>
          <h3>数据输出</h3>
          <div
            style="margin-top: 5px; margin-bottom: 10px; border: 1px solid #dcdfe6; background-color: #ffffff; border-radius: 8px; padding: 20px;">
            <ul style="display: flex; align-items: center; flex-wrap: wrap;">
              <li
                v-for="(item, index) in dataForm.cpsActionOutputs"
                :key="index"
                style="list-style: none; margin-right: 10px; margin-bottom: 5px;">
                <el-tag type="warning" effect="dark" size="max" @click="dataMovementClick(item, index)">
                  {{ item.topic }}
                  <i class="el-icon-close"
                     style="cursor: pointer;"
                     @click.stop.prevent="deleteDataMovement(item, index)"></i>
                </el-tag>
              </li>
              <li style="list-style: none;">
                <i class="el-icon-circle-plus" @click="drawerMovementUnfold()" style="font-size: 24px;"></i>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </el-main>
    <el-dialog
      title="添加输入"
       width="30%"
      :append-to-body="true"
      :visible.sync="drawer"
      :before-close="handleClose">
      <div>
        <el-form :rules="rules" ref="dataInput" :model="dataInput" label-width="120px" size="small"
                 label-position="right"
                 class="product-form">
          <el-row>
            <el-col :span="12">
              <el-form-item label="输入类型:" prop="inputType">
                <el-select v-model="dataInput.inputType" size="small" style="width: 100%;"
                >
                  <el-option
                    v-for="item in switchOnInputTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" v-if="dataInput.inputType == '1'">
              <el-form-item label="主题" prop="inputTheme">
                <el-input v-model="dataInput.inputTheme" size="small" style="width: 100%;"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="dataInput.inputType == '2'">
              <el-form-item label="事件" prop="event">
                <el-select v-model="dataInput.event" size="small" style="width: 100%;"
                >
                  <el-option
                    v-for="item in accessEventOption"
                    :key="item.value"
                    :label="item.label"
                    :aria-disabled="inputEventDisable(item)"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div class="drawer-footer" slot="footer">
          <el-button @click="drawer = false">取消</el-button>
          <el-button type="primary" @click="accessConfirm">确定</el-button>
        </div>
      </div>
    </el-dialog>
    <el-dialog
      title="添加动作"
      width="40%"
      :append-to-body="true"
      :visible.sync="drawerMovement"
      :before-close="handleCloseByMovement">
      <div>
        <el-form :rules="rules" ref="dataMovement" :model="dataMovement" label-width="120px" size="small"
                 label-position="right"
                 class="product-form">
          <el-row>
            <el-col :span="10">
              <el-form-item label="动作类型:" prop="actionType">
                <el-select v-model="dataMovement.actionType" @change="selectMovementType()" size="small"
                           style="width: 100%;"
                >
                  <el-option
                    v-for="item in movementTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <div style="text-align: center; padding: 10px 0; margin-bottom: 10px"
               v-if="dataMovement.actionType == '1'">
            <div style="width: 90%; height: 1px; margin: 0 auto; background-color: #ccc;"></div>
          </div>
          <el-row>
            <el-col :span="12" v-if="dataMovement.actionType == '1'">
              <el-form-item label="主题" prop="actionTheme">
                <el-input v-model="dataMovement.actionTheme" size="small"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" v-if="dataMovement.actionType == '1'">
              <el-form-item label="Qos">
                <el-input v-model="dataMovement.qos" readOnly size="small"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" v-if="dataMovement.actionType == '1'">
              <el-form-item label="Retain">
                <el-input v-model="dataMovement.retain" readOnly size="small"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div v-show="dataMovement.actionType == '1'">
          <div style="margin-left: 20px">
            <h3>
              Payload
              <el-tooltip class="item" effect="dark" content="支持使用 ${field} 语法读取数据。" placement="top-start">
                <i class="el-icon-question"></i>
              </el-tooltip>
            </h3>
            <div style="margin-top: 10px">
              支持使用 ${field} 语法读取数据。
            </div>
            <div id="sqlEditorByMovement" style="height: 300px; width: 100%; margin-top: 20px"></div>
          </div>

        </div>
        <div class="drawer-footer" slot="footer">
          <el-button @click="drawerMovement = false">取消</el-button>
          <el-button type="primary" @click="movementConfirm">确定</el-button>
        </div>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
import {cpsRuleSubmit, cpsRuleDetail} from "@/api/cps/equipment-api";

import 'ace-builds/src-noconflict/ace.js';
import 'ace-builds/src-noconflict/mode-sql.js';
import 'ace-builds/src-noconflict/theme-chrome.js';


export default {
  name: 'historyData',
  data() {
    return {
      drawer: false,
      drawerMovement: false,
      direction: 'rtl',
      showDrawer: false,
      dataInput: {},
      dataMovement: {
        messageStats: false
      },
      dataForm: {
        dataInputSql: 'SELECT * FROM ',
        dataInputSqlIndex: 1,
        cpsDataEntries: [],
        cpsActionOutputs: [],
        type: '',
      },
      movementTypeOption: [],
      switchOnInputTypeOption: [],
      accessEventOption: [],
      payloadOption: [],
      rules: {
        inputType: [
          {
            required: true,
            message: "请选择输入类型",
            trigger: ["blur", "change"],
          }
        ],
        inputTheme: [
          {
            required: true,
            message: "请入主题",
            trigger: ["blur", "change"],
          }
        ],
        actionTheme: [
          {
            required: true,
            message: "请入动作主题",
            trigger: ["blur", "change"],
          }
        ],
        event: [
          {
            required: true,
            message: "请输入事件",
            trigger: ["blur", "change"],
          }
        ],
        actionType: [
          {
            required: true,
            message: "请选择动作类型",
            trigger: ["blur", "change"],
          }
        ],
      }
    }
  },
  mounted() {
    this.movementTypeOption = this.$store.state.dict.dictDatas.movement_type
    this.switchOnInputTypeOption = this.$store.state.dict.dictDatas.switch_on_input_type
    this.accessEventOption = this.$store.state.dict.dictDatas.access_event
    this.payloadOption = this.$store.state.dict.dictDatas.payload
    this.initSqlEditor();
  },
  methods: {
    initData(convertId, convertType) {
      //type: 1是上报；2是下发；
      this.dataForm.type = convertType;
      console.log(this.dataForm.type,"this.dataForm.type")
      console.log(this.dataForm.id,"convertId")
      if (convertId) {
        this.dataForm.id = convertId;
        cpsRuleDetail(this.dataForm.id).then((res) => {
          this.dataForm = res.data;
          // this.dataMovement = res.data.cpsActionOutputs == null ? {}:res.data.cpsActionOutputs;
          this.dataForm.cpsDataEntries = res.data.cpsDataEntries == null ? []:res.data.cpsDataEntries;
          this.dataForm.cpsActionOutputs = res.data.cpsActionOutputs == null ? []:res.data.cpsActionOutputs;
          const editor = window.ace.edit("sqlEditor");
          editor.setValue(this.dataForm.ruleSql, -1);
          this.dataForm.cpsDataEntries.forEach(item => {
            this.switchOnInputTypeOption.forEach(e => {
              if (e.value === item.inputType) {
                if (e.value === '1') {
                  item.topic = e.label + " 主题：" + item.inputTheme
                } else if (e.value === '2') {
                  let event = this.accessEventOption.filter(e => e.value === item.event)
                  item.topic = e.label + " 事件：" + event[0].label
                }
              }
            })
          })
          this.dataForm.cpsActionOutputs.forEach(item => {
            this.movementTypeOption.forEach(e => {
              if (e.value === item.actionType) {
                if (e.value === '1') {
                  item.topic = e.label + " 主题：" + item.actionTheme
                  item.qos = 0;
                  item.retain = "false";
                }
              }
            })
          })
        })
      }
    },
    confirm(equipmentId) {
      // 保存方法
      this.dataForm.ruleType = 2;
      const editor = window.ace.edit("sqlEditor");
      this.dataForm.ruleSql = editor.getValue();
      let data = {
        id: equipmentId,
        cpsRuleSaveVO: this.dataForm
      }
      cpsRuleSubmit(data).then((res) => {
        if (res.code === 0) {
          this.$message.success("转换成功");
        }
      })
    },
    accessConfirm() {
      this.$refs.dataInput.validate(valid => {
        if (valid) {
          this.switchOnInputTypeOption.forEach(e => {
            if (e.value === this.dataInput.inputType) {
              if (e.value === '1') {
                this.dataInput.topic = e.label + " 主题：" + this.dataInput.inputTheme
              } else if (e.value === '2') {
                let event = this.accessEventOption.filter(e => e.value === this.dataInput.event)
                this.dataInput.topic = e.label + " 事件：" + event[0].label
              }
            }
          })
          if (this.dataInput.type === 'edit') {
            this.$set(this.dataForm.cpsDataEntries, this.dataInput.index, this.dataInput);
          } else {
            this.dataForm.cpsDataEntries.push(this.dataInput);
          }
          this.initSqlEditor(this.dataInput.inputTheme, this.dataInput.type)
          this.drawer = false
          this.dataInput = {}
        }
      })
    },
    movementConfirm() {
      this.$refs.dataMovement.validate(valid => {
        if (valid) {
          this.movementTypeOption.forEach(e => {
            if (e.value === this.dataMovement.actionType) {
              if (e.value === '1') {
                this.dataMovement.topic = e.label + " 主题：" + this.dataMovement.actionTheme
              }
            }
          })
          const editor = window.ace.edit("sqlEditorByMovement");
          this.dataMovement.payload = editor.getValue();
          if (this.dataMovement.type === 'edit') {
            this.$set(this.dataForm.cpsActionOutputs, this.dataMovement.index, this.dataMovement);
          } else {
            this.dataForm.cpsActionOutputs.push(this.dataMovement)
          }
          this.drawerMovement = false
          this.dataMovement = {}
        }
      })
    },
    inputEventDisable(data) {
      // 选择事件逻辑

    },
    deleteDataMovement(data, index) {
      this.dataForm.cpsActionOutputs.splice(index, 1);
    },
    deleteDataInput(data, index) {
      this.dataForm.cpsDataEntries.splice(index, 1);
      if (this.dataForm.cpsDataEntries.length > 0) {
        const themes = this.dataForm.cpsDataEntries.map(e => "\"" + e.inputTheme + "\"");
        this.dataForm.dataInputSql = `SELECT * FROM \n${themes.join(',\n')}`
      } else {
        this.dataForm.dataInputSql = 'SELECT * FROM '
      }
      const editor = window.ace.edit("sqlEditor");
      editor.setValue(this.dataForm.dataInputSql, -1);
    },
    dataMovementClick(data, index) {
      data.type = 'edit'
      data.index = index
      this.dataMovement = data;
      this.drawerMovement = true
      if (data.actionType == '1') {
        this.$nextTick(() => {
          this.initSqlEditorByMovement(data.payload)
        })
      }
    },
    dataInputClick(data, index) {
      data.type = 'edit'
      data.index = index
      this.dataInput = data;
      this.drawer = true
    },
    selectMovementType() {
      if (this.dataMovement.actionType == '1') {
        this.$nextTick(() => {
          this.initSqlEditorByMovement()
        })
      }
    },
    drawerMovementUnfold() {
      this.drawerMovement = true
      this.dataMovement.qos = 0
      this.dataMovement.retain = "false"
    },
    sqlGrammarClick() {
      window.open("https://docs.emqx.com/zh/emqx/v5.7/data-integration/rule-sql-syntax.html", "_blank")
    },
    initSqlEditor(data, type) {
      // 确保编辑器实例已经存在，这里假设在某个地方已经初始化过editor
      const editor = window.ace.edit("sqlEditor");

      if (type === 'edit') {
        if (this.dataForm.cpsDataEntries.length > 0) {
          const themes = this.dataForm.cpsDataEntries.map(e => "\"" + e.inputTheme + "\"");
          this.dataForm.dataInputSql = `SELECT * FROM \n${themes.join(',\n')}`
        } else {
          this.dataForm.dataInputSql = 'SELECT * FROM '
        }
      } else {
        if (data) {
          // 获取当前编辑器的内容
          let currentValue = editor.getValue();

          // 检查是否需要添加逗号分隔（假设您希望每个追加的内容之间用逗号分隔）
          if (currentValue && currentValue !== '' && this.dataForm.cpsDataEntries.length > 1) {
            currentValue += ',';
          }

          // 追加或设置新数据
          currentValue += '\n\"' + data + '\"';

          // 更新编辑器内容
          this.dataForm.dataInputSql = currentValue;
        } else {
          editor.setTheme("ace/theme/chrome"); // 设置主题为"chrome"
          editor.session.setMode("ace/mode/sql");
        }
      }
      editor.setValue(this.dataForm.dataInputSql, -1); // -1 表示光标移动到末尾
    },
    initSqlEditorByMovement(data) {
      const editor = window.ace.edit("sqlEditorByMovement");
      editor.setTheme("ace/theme/textmate"); // 设置主题为"chrome"
      editor.session.setMode("ace/mode/javascript");
      if (data) {
        editor.setValue(data, -1);
      } else {
        editor.setValue("", -1);
      }
    },
    handleClose() {
      this.dataInput = {};
      this.drawer = false
    },
    handleCloseByMovement() {
      this.dataMovement = {};
      this.drawerMovement = false
    },
  },
}
</script>

<style scoped>

* {
  box-sizing: border-box;
  margin: 0;
}

body {
  font-family: Arial, sans-serif;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f4f4f4;
}

.container {
  display: flex;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 8px;
  overflow: hidden;
  margin-top: -20px;
  height: calc(80vh - 69px);;
}

.content {
  width: 100%;
  padding: 20px;
  background-color: white;
  flex-grow: 1;
  height: calc(80vh - 69px);;
}

.content h2 {
  margin-bottom: 20px;
}

#display-content {
  margin-top: 5px;
  margin-bottom: 10px;
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
  min-height: calc(40vh - 49px);
}

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

.drawer-content {
  display: flex;
  flex-direction: column; /* 设置为列布局 */
  height: 100%; /* 确保内容区域填充整个抽屉高度 */
}

.drawer-footer {
  display: flex;
  justify-content: flex-end; /* 水平居右对齐 */
  align-items: center; /* 垂直居中对齐 */
  padding: 10px; /* 可选，增加一些内边距 */
  margin-top: 20px;
}

</style>
