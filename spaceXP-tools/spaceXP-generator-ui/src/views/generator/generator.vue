<template>
	<el-dialog v-model="visible" title="生成代码" :close-on-click-modal="false" draggable>
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="120px">
			<el-row>
				<el-col :span="12">
					<el-form-item label="表名" prop="tableName">
						<el-input v-model="dataForm.tableName" disabled placeholder="表名"></el-input>
					</el-form-item>
				</el-col>
        <el-col :span="12">
          <el-form-item label="类名" prop="className">
            <el-input v-model="dataForm.className" placeholder="类名"></el-input>
          </el-form-item>
        </el-col>
			</el-row>
			<el-row>
        <el-col :span="24">
          <el-form-item label="说明" prop="tableComment">
            <el-input v-model="dataForm.tableComment" placeholder="说明"></el-input>
          </el-form-item>
        </el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-form-item label="模块" prop="moduleName">
						<el-input v-model="dataForm.moduleName" placeholder="模块名"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="功能" prop="functionName">
						<el-input v-model="dataForm.functionName" placeholder="功能名"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-form-item label="包名" prop="packageName">
						<el-input v-model="dataForm.packageName" placeholder="项目包名"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="版本" prop="version">
						<el-input v-model="dataForm.version" placeholder="版本号"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-form-item label="作者" prop="author">
						<el-input v-model="dataForm.author" placeholder="默认作者"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="邮箱" prop="email">
						<el-input v-model="dataForm.email" placeholder="作者邮箱"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-form-item label="生成方式" prop="generatorType">
						<el-radio-group v-model="dataForm.generatorType">
							<el-radio :label="0">zip压缩包</el-radio>
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
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="submitHandle()">保存</el-button>
			<el-button type="danger" @click="generatorHandle()">生成代码</el-button>
		</template>
	</el-dialog>
</template>

<script setup >
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus/es'
import { useGeneratorApi, useDownloadApi } from '@/api/generator'
import { useTableApi, useTableSubmitApi } from '@/api/table'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()
const dataForm = reactive({
	id: '',
	baseclassId: '',
	generatorType: 0,
	formLayout: 1,
	backendPath: '',
	frontendPath: '',
	packageName: '',
	email: '',
	author: '',
	version: '',
	moduleName: '',
	functionName: '',
	className: '',
	tableComment: '',
	tableName: ''
})

const init = (id) => {
	visible.value = true
	dataForm.id = ''

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	getTable(id)
}


const getTable = (id) => {
	useTableApi(id).then(res => {
		Object.assign(dataForm, res.data)
	})
}

const dataRules = ref({
	tableName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	tableComment: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	className: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	packageName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	author: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	moduleName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	functionName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  email: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  version: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	generatorType: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	formLayout: [{ required: true, message: '必填项不能为空', trigger: 'blur' }]
})

// 保存
const submitHandle = () => {
	dataFormRef.value.validate((valid) => {
		if (!valid) {
			return false
		}

		useTableSubmitApi(dataForm).then(() => {
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

		// 先保存
		await useTableSubmitApi(dataForm)

		// 生成代码，zip压缩包
		if (dataForm.generatorType === 0) {
			useDownloadApi("generator/genTableCode",[dataForm])
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
