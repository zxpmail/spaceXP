<template>
	<el-card>
		<el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
			<el-form-item>
				<el-input v-model="state.queryForm.connName" placeholder="连接名"></el-input>
			</el-form-item>
			<el-form-item prop="dbType">
				<el-select v-model="state.queryForm.dbType" @click="databaseHandle"
                   clearable placeholder="数据库类型"
                  value-key="dbType"
        >
          <el-option
            :label="item.dbType"
            :value="item.dbType"
            v-for="(item) in state.database"
            :key="item.dbType"
            ></el-option>
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button @click="getDataList()">查询</el-button>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
			</el-form-item>
			<el-form-item>
				<el-button type="danger" @click="deleteBatchHandle()">删除</el-button>
			</el-form-item>
		</el-form>
		<el-table v-loading="state.dataListLoading" :data="state.dataList" border style="width: 100%" @selection-change="selectionChangeHandle">
			<el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
			<el-table-column prop="connName" label="连接名" header-align="center" align="center"></el-table-column>
			<el-table-column prop="dbType" label="数据库类型" header-align="center" align="center"></el-table-column>
      <el-table-column prop="driverClassName" label="数据库驱动" header-align="center" align="center"></el-table-column>
			<el-table-column prop="url" label="数据库URL" show-overflow-tooltip header-align="center" align="center"></el-table-column>
			<el-table-column prop="username" label="用户名" header-align="center" align="center"></el-table-column>
			<el-table-column label="操作" fixed="right" header-align="center" align="center" width="170">
				<template #default="scope">
					<el-button type="primary" link @click="datasourceHandle(scope.row)">测试</el-button>
					<el-button type="primary" link @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
					<el-button type="primary" link @click="deleteBatchHandle(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination
			:current-page="state.page"
			:page-sizes="state.size"
			:page-size="state.limit"
			:total="state.total"
			layout="total, sizes, prev, pager, next, jumper"
			@size-change="sizeChangeHandle"
			@current-change="currentChangeHandle"
		>
		</el-pagination>

		<!-- 弹窗, 新增 / 修改 -->
		<add-or-update ref="addOrUpdateRef" @refresh-data-list="getDataList"></add-or-update>
	</el-card>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useDataSourceTestApi } from '@/api/datasource'
import { useDatabaseListApi } from '@/api/database'
import { useCrud } from '@/hooks/index.js'
import { ElMessage } from 'element-plus'
import AddOrUpdate from './add-or-update.vue'

const state = reactive({
	dataListUrl: '/datasource/list',
	deleteUrl: '/datasource/delete',
	queryForm: {
		connName: '',
		dbType: ''
	},
  database:[]
})

const datasourceHandle = (data) => {
	useDataSourceTestApi(data).then((res) => {
		ElMessage.success(res.message)
	})
}
const databaseHandle = () => {
  useDatabaseListApi().then((res) => {
    state.database=res.data
  })
}
const addOrUpdateRef = ref()
const addOrUpdateHandle = (id) => {
	addOrUpdateRef.value.init(id)
}

const { getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle } = useCrud(state)


</script>
