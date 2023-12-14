<template>
	<el-card>
		<el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
			<el-form-item>
				<el-button type="primary" @click="addHandle()">新增</el-button>
			</el-form-item>
		</el-form>
		<el-table v-loading="state.dataListLoading" :data="state.dataList" border style="width: 100%" @selection-change="selectionChangeHandle">
			<el-table-column prop="dbType" label="数据库类型" header-align="center" align="center"></el-table-column>
      <el-table-column prop="driver" label="数据库驱动" header-align="center" align="center"></el-table-column>
      <el-table-column prop="url" label="数据库URL" show-overflow-tooltip header-align="center" align="center"></el-table-column>
			<el-table-column label="操作" fixed="right" header-align="center" align="center" width="170">
				<template #default="scope">
					<el-button type="primary" link @click="updateHandle(scope.row.id)">编辑</el-button>
					<el-button type="primary" link @click="deleteHandle(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<!-- 弹窗, 新增 / 修改 -->
		<add-or-update ref="addOrUpdateRef" @refresh-data-list="getDataList"></add-or-update>
	</el-card>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useCrud } from '@/hooks/index.js'
import AddOrUpdate from './add-or-update.vue'

const state = reactive({
	dataListUrl: '/database/list',
	deleteUrl: '/database/delete',
  isPage: false
})

const addOrUpdateRef = ref()
const addHandle = () => {
  addOrUpdateRef.value.init()
};
const updateHandle = id => {
  addOrUpdateRef.value.init(id)
};
const { getDataList, selectionChangeHandle, deleteHandle } = useCrud(state)


</script>
