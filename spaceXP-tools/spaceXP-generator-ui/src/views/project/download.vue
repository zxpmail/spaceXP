<template>
  <el-dialog v-model="visible" title="源码下载" :close-on-click-modal="false">
    <el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
      <el-form-item>
        <el-input v-model="state.queryForm.connName" placeholder="数据源名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-input v-model="downloadName" placeholder="下载表名称"></el-input>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border style="width: 100%"
              @selection-change="selectionChangeHandle">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="tableName" label="表名" header-align="center" align="center"></el-table-column>
      <el-table-column prop="tableComment" label="表说明" header-align="center" align="center"></el-table-column>
      <el-table-column prop="connName" label="数据源名称" header-align="center" align="center"></el-table-column>
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
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle()">下载</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {reactive, ref} from 'vue'

import {useDownloadApi} from '@/api/generator'
import {useCrud} from "@/hooks/index.js";
import {ElMessage} from "element-plus";

const state = reactive({
  dataListUrl: '/table/list',
  queryForm: {
    connName: ''
  }
})
const visible = ref(false)
const dataFormRef = ref()
const downloadName = ref('')
const dataForm = reactive({
  project:{}
})

const init = (data) => {
  visible.value = true
  dataForm.project = data

  getDataList()
}

// 表单提交
const submitHandle = () => {
  const tables = state.dataListSelections ? state.dataListSelections : []

  if (tables.length === 0) {
    ElMessage.warning('请选择生成代码的表')
    return
  }
  if (!downloadName.value) {
    ElMessage.warning('请输入下载表名称')
    return
  }
  const data = state.dataList.filter(m => tables.includes(m.id))
  const uniqueArr = Array.from(new Set(data.map(({datasourceName}) => datasourceName)));

  if (uniqueArr.length > 1) {
    ElMessage.warning('生成代码必须保证为相同的数据源名称')
    return
  }
  dataForm.project.tables = data
  dataForm.project.tablePrefix = downloadName.value
  // 源码下载
  useDownloadApi('generator/genProjectCode',dataForm.project)
  visible.value = false
}

defineExpose({
  init
})
const {getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle} = useCrud(state)
</script>
