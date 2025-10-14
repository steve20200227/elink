<template>
  <div>
    <el-dialog title="选择用户" width="80%" :visible.sync="open" append-to-body>
        <user ref="user" @singleUserSelect="singleUserSelect" type="selectUser" :multiple="multiple"></user>
      <template slot="footer" v-if="multiple">
        <el-button size="mini" @click="open = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="confirm">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import user from '@/views/system/user/index'
export default {
  name: "userSelect",
  components:{user},
  props:{
    multiple: {
      type: Boolean,
    }
  },
  data(){
    return {
      operate:[],
      open: true,
    }
  },
  methods:{
    confirm(){
      this.$emit('selectUser',this.$refs.user.selectUser)
    },
    singleUserSelect(data){
      this.$emit('singleUserSelect',data)
    }
  }
}
</script>

<style scoped>

</style>
