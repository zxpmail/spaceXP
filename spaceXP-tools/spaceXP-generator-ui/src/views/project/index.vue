<template>
	<el-card>
		<el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
			<el-form-item>
				<el-input v-model="state.queryForm.projectName" placeholder="项目名"></el-input>
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
			<el-table-column prop="artifactId" label="项目标识" header-align="center" align="center"></el-table-column>
			<el-table-column prop="groupId" label="项目包名" show-overflow-tooltip header-align="center" align="center"></el-table-column>
			<el-table-column prop="version" label="版本" show-overflow-tooltip header-align="center" align="center"></el-table-column>
      <el-table-column prop="type" label="类型" show-overflow-tooltip header-align="center" align="center">
        <template v-slot="{ row }">
          <span>{{ row.type === 1 ? '多模块' : '单体' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="author" label="作者" show-overflow-tooltip header-align="center" align="center"></el-table-column>
      <el-table-column prop="email" label="Email" show-overflow-tooltip header-align="center" align="center"></el-table-column>
			<el-table-column label="操作" fixed="right" header-align="center" align="center" width="180">
				<template #default="scope">
					<el-button type="primary" link @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
					<el-button type="primary" link @click="downloadHandle(scope.row)">源码下载</el-button>
					<el-button type="primary" link @click="deleteBatchHandle(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination
			:current-page="state.page"
			:page-sizes="state.pageSizes"
			:page-size="state.size"
			:total="state.total"
			layout="total, sizes, prev, pager, next, jumper"
			@size-change="sizeChangeHandle"
			@current-change="currentChangeHandle"
		>
		</el-pagination>

		<!-- 弹窗, 新增 / 修改 -->
		<add-or-update ref="addOrUpdateRef" @refresh-data-list="getDataList"></add-or-update>
		<!-- 源码下载 -->
		<download ref="downloadRef"></download>
	</el-card>
</template>

<script setup >
import { reactive, ref } from 'vue'
import { useCrud } from '@/hooks/index.js'
import AddOrUpdate from './add-or-update.vue'
import Download from './download.vue'

const state = reactive({
	dataListUrl: '/project/list',
	deleteUrl: '/project/delete',
	queryForm: {
		projectName: ''
	}
})

const addOrUpdateRef = ref()
const addOrUpdateHandle = (id) => {
	addOrUpdateRef.value.init(id)
}

const downloadRef = ref()
const downloadHandle = (id) => {
	downloadRef.value.init(id)
}

const { getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle } = useCrud(state)
</script>
