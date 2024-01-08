<template>
  <el-dialog v-model="visible" title="生成代码" :close-on-click-modal="false" draggable>
    <el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="120px">

      <el-form-item label="表名" prop="tableName">
        <el-input v-model="dataForm.tableName" disabled placeholder="表名"></el-input>
      </el-form-item>


      <el-form-item label="说明" prop="tableComment">
        <el-input v-model="dataForm.tableComment" placeholder="说明"></el-input>
      </el-form-item>

      <el-form-item label="类名" prop="className">
        <el-input v-model="dataForm.className" placeholder="类名"></el-input>
      </el-form-item>
      <el-form-item label="功能" prop="functionName">
        <el-input v-model="dataForm.functionName" placeholder="功能名"></el-input>
      </el-form-item>

      <el-form-item prop="artifactId" label="项目">
        <el-select v-model="dataForm.project.artifactId" placeholder="项目名" style="width: 100%" clearable
                   @change="projectChange"
                   value-key="projectId">
          <el-option v-for="item in projectList" :key="item.id" :label="item.artifactId" :value="item.id"></el-option>
        </el-select>
      </el-form-item>

      <el-row>
        <el-col :span="12">
          <el-form-item label="生成方式" prop="generatorType">
            <el-radio-group v-model="dataForm.generatorType">
              <el-radio :label="0">zip压缩包</el-radio>
              <el-radio :label="1">自定义路径</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="表单布局" prop="formLayout">
            <el-radio-group v-model="dataForm.formLayout">
              <el-radio :label="1">一列</el-radio>
              <el-radio :label="2">两列</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item v-if="dataForm.generatorType === 1" label="后端生成路径" prop="backendPath">
        <el-input v-model="dataForm.backendPath" placeholder="后端生成路径"></el-input>
      </el-form-item>
      <el-form-item v-if="dataForm.generatorType === 1" label="前端生成路径" prop="frontendPath">
        <el-input v-model="dataForm.frontendPath" placeholder="前端生成路径"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle()">保存</el-button>
      <el-button type="danger" @click="generatorHandle()">生成代码</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {reactive, ref} from 'vue'
import {ElMessage} from 'element-plus/es'
import {useProjectAllApi} from '@/api/project'
import {useGeneratorApi, useDownloadApi} from '@/api/generator'
import {useTableApi, useTableSubmitApi} from '@/api/table'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()
const projectList = ref([])
const dataForm = reactive({
  id: '',
  generatorType: 0,
  formLayout: 1,
  artifactId: '',
  project: {
    projectId: '',
    artifactId: '',
    packageName: '',
    email: '',
    author: '',
    version: '',
    moduleName: '',
  },
  functionName: '',
  className: '',
  tableComment: '',
  tableName: ''
})
const projectChange = (data) => {
  let p = projectList.value.filter(i => i.id === data)[0]
  if (!p) {
    ElMessage.error('项目不存在')
    return
  }
  dataForm.project = p
  dataForm.project.projectId = p.id
  dataForm.project.artifactId = p.artifactId
  dataForm.artifactId = p.artifactId
}
const init = (id) => {
  visible.value = true
  dataForm.id = ''

  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields()
  }

  getProjectList()
  getTable(id)

}

const getProjectList = () => {
  useProjectAllApi().then(res => {
    projectList.value = res.data
  })
}

const getTable = (id) => {
  useTableApi(id).then(res => {
    Object.assign(dataForm, res.data)
    dataForm.project.artifactId = res.data.artifactId
    dataForm.project.projectId = res.data.projectId
  })
}

const dataRules = ref({
  functionName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  artifactId: [{required: true, message: '必填项不能为空', trigger: 'blur'}],
  tableName: [{required: true, message: '必填项不能为空', trigger: 'blur'}],
  tableComment: [{required: true, message: '必填项不能为空', trigger: 'blur'}],
  className: [{required: true, message: '必填项不能为空', trigger: 'blur'}],
  formLayout: [{required: true, message: '必填项不能为空', trigger: 'blur'}],
  backendPath: [{required: true, message: '必填项不能为空', trigger: 'blur'}],
  frontendPath: [{required: true, message: '必填项不能为空', trigger: 'blur'}]
})

// 保存
const submitHandle = () => {
  dataFormRef.value.validate((valid) => {
    if (!valid) {
      return false
    }
    const data = {
      'projectId': dataForm.project.projectId,
      'id': dataForm.id,
      'artifactId': dataForm.project.artifactId,
      'className': dataForm.className,
      'tableComment': dataForm.tableComment,
      'functionName': dataForm.functionName
    }
    useTableSubmitApi(data).then(() => {
      ElMessage.success({
        message: '操作成功',
        duration: 500,
        onClose: () => {
          visible.value = false
          emit('refreshDataList')
        }
      })
    })
  })
}

// 生成代码
const generatorHandle = () => {
  dataFormRef.value.validate(async (valid) => {
    if (!valid) {
      return false
    }
    const data = {
      'projectId': dataForm.project.projectId,
      'id': dataForm.id,
      'artifactId': dataForm.project.artifactId,
      'className': dataForm.className,
      'tableComment': dataForm.tableComment,
      'functionName': dataForm.functionName
    }
    // 先保存
    await useTableSubmitApi(data)
    let p = projectList.value.filter(i => {
      return i.id === data.projectId;
    })[0]
    dataForm.project = p
    dataForm.project.projectId = p.id
    dataForm.project.artifactId = p.artifactId
    // 生成代码，zip压缩包
    if (dataForm.generatorType === 0) {
      useDownloadApi('/generator/code',dataForm)
      visible.value = false
      return
    }

    // 生成代码，自定义路径
    useGeneratorApi([dataForm.id]).then(() => {
      ElMessage.success({
        message: '操作成功',
        duration: 500,
        onClose: () => {
          visible.value = false
          emit('refreshDataList')
        }
      })
    })
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
