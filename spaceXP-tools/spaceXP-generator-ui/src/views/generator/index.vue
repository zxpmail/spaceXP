<template>
	<el-card>
		<el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
			<el-form-item>
				<el-input v-model="state.queryForm.tableName" placeholder="表名"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button @click="getDataList()">查询</el-button>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="importHandle()">导入</el-button>
			</el-form-item>
			<el-form-item>
				<el-button type="success" @click="downloadBatchHandle()">生成代码</el-button>
			</el-form-item>
			<el-form-item>
				<el-button type="danger" @click="deleteBatchHandle()">删除</el-button>
			</el-form-item>
		</el-form>
		<el-table v-loading="state.dataListLoading" :data="state.dataList" border style="width: 100%" @selection-change="selectionChangeHandle">
			<el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
			<el-table-column prop="tableName" label="表名" header-align="center" align="center"></el-table-column>
			<el-table-column prop="tableComment" label="表说明" header-align="center" align="center"></el-table-column>
			<el-table-column prop="connName" label="数据源名称" header-align="center" align="center"></el-table-column>
			<el-table-column label="操作" fixed="right" header-align="center" align="center" width="250">
				<template #default="scope">
					<el-button type="primary" link @click="editHandle(scope.row.id)">编辑</el-button>
					<el-button type="primary" link @click="generatorHandle(scope.row.id)">生成代码</el-button>
					<el-button type="primary" link @click="deleteBatchHandle(scope.row.id)">删除</el-button>
					<el-button type="primary" link @click="syncHandle(scope.row)">同步</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination
			:current-page="state.page"
			:page-sizes="state.pageSizes"
			:page-size="state.limit"
			:total="state.total"
			layout="total, sizes, prev, pager, next, jumper"
			@size-change="sizeChangeHandle"
			@current-change="currentChangeHandle"
		>
		</el-pagination>

		<import ref="importRef" @refresh-data-list="getDataList"></import>
		<edit ref="editRef" @refresh-data-list="getDataList"></edit>
		<generator ref="generatorRef" @refresh-data-list="getDataList"></generator>
    <BatchGenerator ref="batchGeneratorRef"></BatchGenerator>
	</el-card>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useCrud } from '@/hooks/index.js'
import Import from './import.vue'
import Edit from './edit.vue'
import Generator from './generator.vue'
import BatchGenerator from './batch-generator.vue'
import { useTableSyncApi } from '@/api/table'
import { ElMessage, ElMessageBox } from 'element-plus'

const state = reactive({
	dataListUrl: '/table/list',
	deleteUrl: '/table/delete',
	queryForm: {
		tableName: ''
	}
})

const importRef = ref()
const editRef = ref()
const batchGeneratorRef= ref()
const generatorRef = ref()

const importHandle = (id) => {
	importRef.value.init(id)
}

const editHandle = (id) => {
	editRef.value.init(id)
}

const generatorHandle = (id) => {
	generatorRef.value.init(id)
}

const downloadBatchHandle = () => {
	const tables = state.dataListSelections ? state.dataListSelections : []

	if (tables.length === 0) {
		ElMessage.warning('请选择生成代码的表')
		return
	}
  const data=state.dataList.filter(m=>tables.includes(m.id))
  const uniqueArr = Array.from(new Set(data.map(({ datasourceName }) => datasourceName)));

  if(uniqueArr.length>1){
    ElMessage.warning('生成代码必须保证为相同的数据源名称')
    return
  }
  batchGeneratorRef.value.init(data)
}

const syncHandle = (row) => {
	ElMessageBox.confirm(`确定同步数据表${row.tableName}吗?`, '提示', {
		confirmButtonText: '确定',
		cancelButtonText: '取消',
		type: 'warning'
	})
		.then(() => {
			useTableSyncApi(row).then(() => {
				ElMessage.success('同步成功')
        getDataList()
			})
		})
		.catch(() => {})
}

const { getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle } = useCrud(state)
</script>
