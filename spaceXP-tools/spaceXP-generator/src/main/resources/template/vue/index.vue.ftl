<template>
    <el-card>
        <el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
            <#list queryList as field>
                <el-form-item>
                    <#if field.queryFormType == 'text' || field.queryFormType == 'textarea' || field.queryFormType == 'editor'>
                        <el-input v-model="state.queryForm.${field.attrName}" placeholder="${field.fieldComment!}"></el-input>
                    <#elseif field.queryFormType == 'select'>
                        <#if field.formDict??>
                            <fast-select v-model="state.queryForm.${field.attrName}" dict-type="${field.formDict}" placeholder="${field.fieldComment!}" clearable></fast-select>
                        <#else>
                            <el-select v-model="state.queryForm.${field.attrName}" placeholder="${field.fieldComment!}">
                                <el-option label="选择" value="0"></el-option>
                            </el-select>
                        </#if>
                    <#elseif field.queryFormType == 'radio'>
                        <#if field.formDict??>
                            <fast-radio-group v-model="state.queryForm.${field.attrName}" dict-type="${field.formDict}"></fast-radio-group>
                        <#else>
                            <el-radio-group v-model="state.queryForm.${field.attrName}">
                                <el-radio :label="0">单选</el-radio>
                            </el-radio-group>
                        </#if>
                    <#elseif field.queryFormType == 'date'>
                        <el-date-picker
                                v-model="state.queryForm.${field.attrName}"
                                type="daterange"
                                value-format="YYYY-MM-DD">
                        </el-date-picker>
                    <#elseif field.queryFormType == 'datetime'>
                        <el-date-picker
                                v-model="state.queryForm.${field.attrName}"
                                type="datetimerange"
                                value-format="YYYY-MM-DD HH:mm:ss">
                        </el-date-picker>
                    <#else>
                        <el-input v-model="state.queryForm.${field.attrName}" placeholder="${field.fieldComment!}"></el-input>
                    </#if>
                </el-form-item>
            </#list>
            <el-form-item>
                <el-button @click="getDataList()">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button v-auth="'${moduleName}:${functionName}:save'" type="primary" @click="addOrUpdateHandle()">新增</el-button>
            </el-form-item>
            <el-form-item>
                <el-button v-auth="'${moduleName}:${functionName}:delete'" type="danger" @click="deleteBatchHandle()">删除</el-button>
            </el-form-item>
        </el-form>
        <el-table v-loading="state.dataListLoading" :data="state.dataList" border style="width: 100%" @selection-change="selectionChangeHandle">
            <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
            <#list gridList as field>
                <#if field.formDict??>
                    <fast-table-column prop="${field.attrName}" label="${field.fieldComment!}" dict-type="${field.formDict}"></fast-table-column>
                <#else>
                    <el-table-column prop="${field.attrName}" label="${field.fieldComment!}" header-align="center" align="center"></el-table-column>
                </#if>
            </#list>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
                <template #default="scope">
                    <el-button v-auth="'${moduleName}:${functionName}:update'" type="primary" link @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
                    <el-button v-auth="'${moduleName}:${functionName}:delete'" type="primary" link @click="deleteBatchHandle(scope.row.id)">删除</el-button>
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

        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update ref="addOrUpdateRef" @refreshDataList="getDataList"></add-or-update>
    </el-card>
</template>

<script setup  name="${moduleName?cap_first}${functionName?cap_first}Index">
    import { reactive, ref, onMounted } from 'vue'
    import AddOrUpdate from './add-or-update.vue'
    import { use${functionName?cap_first}ListApi, use${functionName?cap_first}BatchDeleteApi } from '@/api/${functionName}'
    import { ElMessage, ElMessageBox } from 'element-plus'

    const state = reactive({
        queryForm: {
            id: '',
            name: '',
            comments: '',
            createTime: '',
            updateTime: '',
            dataScope: '',
            dataListLoading: false,
            dataList:[],
            page: 1,
            limit: 10,
            total: 0,
            pageSizes: [1, 10, 20, 50, 100, 200],
            dataListSelections: []
        }
    })

    onMounted(() => {
        if (state.createdIsNeed) {
            query()
        }
    })
    const addOrUpdateRef = ref()
    const addOrUpdateHandle = (id) => {
        addOrUpdateRef.value.init(id)
    }

    const query = async() => {
        state.dataListLoading = true
        const res = await use${functionName?cap_first}ListApi(state.queryForm).catch(()=>{state.dataListLoading = false})
        const {code,data} = res
        state.dataListLoading = false
        if(code === 200){
            state.dataList = state.isPage ? data.list : data
            state.total = state.isPage ? data.total : 0
        }
    }

    const getDataList = () => {
        state.page = 1
        query()
    }
    const sizeChangeHandle = (val) => {
        state.page = 1
        state.limit = val
        query()
    }

    const currentChangeHandle = (val) => {
        state.page = val
        query()
    }
    // 多选
    const selectionChangeHandle = (selections) => {
        state.dataListSelections = selections.map((item) => state.primaryKey && item[state.primaryKey])
    }
    const deleteBatchHandle = (key) => {
        let data = []
        if (key) {
            data = [key]
        } else {
            data = state.dataListSelections ? state.dataListSelections : []

            if (data.length === 0) {
                ElMessage.warning('请选择删除记录')
                return
            }
        }

        ElMessageBox.confirm('确定进行删除操作?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(async() => {
            const res = await use${functionName?cap_first}BatchDeleteApi(data)
            const {code,message} = res
            if(code === 200){
                ElMessage.success('删除成功')
                query()
            }else{
                ElMessage.error('删除失败：' + message)
            }
        })
    }
</script>