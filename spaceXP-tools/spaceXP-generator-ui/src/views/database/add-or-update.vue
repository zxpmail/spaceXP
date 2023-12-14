<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false">
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="120px" @keyup.enter="submitHandle()">
			<el-form-item label="数据库类型" prop="dbType">
				<el-input v-model="dataForm.dbType" placeholder="数据库类型"></el-input>
			</el-form-item>
      <el-form-item label="数据库驱动" prop="driver">
        <el-input v-model="dataForm.driver" placeholder="数据库驱动"></el-input>
      </el-form-item>
			<el-form-item label="数据库URL" prop="url">
				<el-input v-model="dataForm.url" placeholder="数据库URL"></el-input>
			</el-form-item>
			<el-form-item label="数据表名称" prop="tableName">
				<el-input v-model="dataForm.tableName" placeholder="数据表名称"></el-input>
			</el-form-item>
			<el-form-item label="数据表注释" prop="tableComment">
				<el-input v-model="dataForm.tableComment"  placeholder="数据表注释"></el-input>
			</el-form-item>
      <el-form-item label="表字段名称" prop="fieldName">
        <el-input v-model="dataForm.fieldName"  placeholder="表字段名称"></el-input>
      </el-form-item>
      <el-form-item label="表字段类型" prop="fieldType">
        <el-input v-model="dataForm.fieldType"  placeholder="表字段类型"></el-input>
      </el-form-item>
      <el-form-item label="表字段注释" prop="fieldComment">
        <el-input v-model="dataForm.fieldComment"  placeholder="表字段注释"></el-input>
      </el-form-item>
      <el-form-item label="表主键字段" prop="fieldKey">
        <el-input v-model="dataForm.fieldKey"  placeholder="表主键字段"></el-input>
      </el-form-item>
      <el-form-item label="字段信息SQL" prop="tableFields">
        <el-input type="textarea"   v-model="dataForm.tableFields"  placeholder="字段信息SQL"></el-input>
      </el-form-item>
      <el-form-item label="表信息SQL" prop="tableSql">
        <el-input type="textarea"   v-model="dataForm.tableSql"  placeholder="表信息SQL"></el-input>
      </el-form-item>
      <el-form-item label="表名SQL" prop="tableNameSql">
        <el-input type="textarea"   v-model="dataForm.tableNameSql"  placeholder="表名SQL"></el-input>
      </el-form-item>
      <el-form-item label="表附加SQL" prop="tableAddSql">
        <el-input type="textarea"   v-model="dataForm.tableAddSql"  placeholder="表附加SQL"></el-input>
      </el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="submitHandle()">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus/es'
import { useDatabaseApi, useDatabaseSubmitApi } from '@/api/database'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()

const dataForm = reactive({
	id: '',
	dbType: '',
  driver: '',
  url: '',
  tableName: '',
  tableComment: '',
  fieldName: '',
  fieldType: '',
  fieldComment: '',
  fieldKey: '',
  tableFields: '',
  tableSql: '',
  tableNameSql: '',
  tableAddSql: ''
})

const init = (id) => {
	visible.value = true
	dataForm.id = ''

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	// id 存在则为修改
	if (id) {
		getDataSource(id)
	}
}

const getDataSource = (id) => {
  useDatabaseApi(id).then(res => {
		Object.assign(dataForm, res.data)
	})
}

const dataRules = ref({
	dbType: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	connName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	connUrl: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	username: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	password: [{ required: true, message: '必填项不能为空', trigger: 'blur' }]
})

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid) => {
		if (!valid) {
			return false
		}

    useDatabaseSubmitApi({ ...dataForm }).then(() => {
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
