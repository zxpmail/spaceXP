<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false">
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="100px" @keyup.enter="submitHandle()">
			<el-form-item label="项目标识" prop="artifactId">
				<el-input v-model="dataForm.artifactId" placeholder="项目标识"></el-input>
			</el-form-item>
			<el-form-item label="项目包名" prop="groupId">
				<el-input v-model="dataForm.groupId" placeholder="项目包名"></el-input>
			</el-form-item>
			<el-form-item label="版本号" prop="version">
				<el-input v-model="dataForm.version" placeholder="版本号"></el-input>
			</el-form-item>
      <el-form-item label="项目类型" prop="type">
        <el-select v-model="dataForm.type" clearable placeholder="项目类型" style="width: 100%">
          <el-option value="单体" label="单体"></el-option>
          <el-option value="EUREKA" label="EUREKA"></el-option>
          <el-option value="NACOS" label="NACOS"></el-option>
          <el-option value="NACOS CONFIG" label="NACOS CONFIG"></el-option>
          <el-option value="代码" label="代码"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="文档方式" prop="springDoc">
        <el-select v-model="dataForm.springDoc" clearable placeholder="项目类型" style="width: 100%">
          <el-option :value="1" label="springDoc"></el-option>
          <el-option :value="0" label="springFox"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input v-model="dataForm.author" placeholder="作者"></el-input>
      </el-form-item>
      <el-form-item label="EMail" prop="email">
        <el-input v-model="dataForm.email" placeholder="EMail"></el-input>
      </el-form-item>
      <el-form-item label="项目端口" prop="port">
        <el-input v-model="dataForm.port" placeholder="项目端口"></el-input>
      </el-form-item>
      <el-form-item label="项目描述" prop="port">
        <el-input v-model="dataForm.description"  type="textarea" placeholder="项目描述"></el-input>
      </el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="submitHandle()">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup >
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus/es'
import { useProjectApi, useProjectSubmitApi } from '@/api/project.js'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()

const dataForm = reactive({
	id: '',
  artifactId: 'test',
  groupId: 'cn.piesat',
  version: '1.0.0',
  type: '单体',
  springDoc: 1,
  author: 'zhouxiaoping',
  email: 'zhouxiaoping@piesat.cn',
  description: '',
  port: 8080

})

const init = (id) => {
	visible.value = true
	dataForm.id = ''

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	if (id) {
		getProject(id)
	}
}

const getProject = (id) => {
	useProjectApi(id).then(res => {
		Object.assign(dataForm, res.data)
	})
}

const dataRules = ref({
  artifactId: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  groupId: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  version: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  springDoc: [{ required: true, message: '必填项不能为空', trigger: 'change' }]
})

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid) => {
		if (!valid) {
			return false
		}

		useProjectSubmitApi(dataForm).then(() => {
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
