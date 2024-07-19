<template>
  <el-dialog v-model="visible" title="生成代码" :close-on-click-modal="false" draggable>
    <el-form ref="dataFormRef" :model="dataForm.project" :rules="dataRules" label-width="120px">
      <el-form-item label="表名前缀" prop="tablePrefix">
        <el-input v-model="dataForm.tablePrefix" placeholder="表前缀"></el-input>
      </el-form-item>
      <el-form-item prop="artifactId" label="项目">
        <el-select v-model="dataForm.project.id" placeholder="项目名称" style="width: 100%" clearable
                   @change="projectChange"
                   >
          <el-option v-for="item in projectList" :key="item.id" :label="item.artifactId" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="表单布局" >
        <el-radio-group v-model="dataForm.formLayout">
          <el-radio :label="1" value="1">一列</el-radio>
          <el-radio :label="2" value="2">两列</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="danger" @click="generatorHandle()">生成代码</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {reactive, ref} from 'vue'
import {ElMessage} from 'element-plus/es'
import {useDownloadApi} from '@/api/generator'
import {useProjectAllApi} from "@/api/project.js";

const visible = ref(false)
const dataFormRef = ref()
const projectList = ref([])
const dataForm = reactive({
    tables:[],
    formLayout: 1,
    tablePrefix: '',
    project:{
    }
})
const projectChange = (data) => {
  let p = projectList.value.filter(i => i.id === data)[0]
  if (!p) {
    ElMessage.error('项目不存在')
    return
  }
  dataForm.project = p
}
const getProjectList = () => {
  useProjectAllApi().then(res => {
    projectList.value = res.data
  })
}
const init = (data) => {
  visible.value = true
  dataForm.tables =data
  getProjectList()
}
const dataRules = ref({
  artifactId: [{required: true, message: '必填项不能为空', trigger: 'blur'}]
})
// 生成代码
const generatorHandle = () => {
  dataFormRef.value.validate(async (valid) => {
    if (!valid) {
      return false
    }
    // 生成代码，zip压缩包
    useDownloadApi('/code/batch',dataForm)
    visible.value = false
  })
}

defineExpose({
  init
})
</script>

<style lang="scss" scoped>
.generator-code .el-dialog__body {
  padding: 15px 30px 0 20px;
}
</style>
