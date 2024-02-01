<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false">
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="120px" @keyup.enter="submitHandle()">
			<el-form-item label="数据库类型" prop="dbType">
				<el-input v-model="dataForm.dbType" placeholder="数据库类型"></el-input>
			</el-form-item>
      <el-form-item label="数据库驱动" prop="driver">
        <el-input v-model="dataForm.driverClassName" placeholder="数据库驱动"></el-input>
      </el-form-item>
			<el-form-item label="数据库URL" prop="url">
				<el-input v-model="dataForm.url" placeholder="数据库URL"></el-input>
			</el-form-item>
      <el-form-item label="字段信息SQL" prop="tableFields">
        <el-input type="textarea" autosize  v-model="dataForm.tableFields"  placeholder="字段信息SQL"></el-input>
      </el-form-item>
      <el-form-item label="表信息SQL" prop="tableSql">
        <el-input type="textarea" autosize  v-model="dataForm.tableSql"  placeholder="表信息SQL"></el-input>
      </el-form-item>
      <el-form-item label="拼接数据库" prop="addDatabaseName">
        <el-switch
            :active-value="1"
            :inactive-value="0"
            v-model="dataForm.addDatabaseName"
            inline-prompt
            active-text="Y"
            inactive-text="N"
        />
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
  driverClassName: '',
  url: '',
  tableFields: '',
  tableSql: '',
  addDatabaseName: 0
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
  driverClassName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	url: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  tableName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  tableComment: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  fieldName: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  fieldType: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  tableFields: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  fieldComment: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  fieldKey: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  tableSql: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
  tableNameSql: [{ required: true, message: '必填项不能为空', trigger: 'blur' }]
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
