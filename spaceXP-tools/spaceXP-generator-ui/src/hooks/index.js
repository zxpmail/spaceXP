import service from '@/utils/request'
import { onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export const useCrud = (options) => {
	const defaultOptions = {
		createdIsNeed: true,
		dataListUrl: '',
		isPage: true,
		deleteUrl: '',
		primaryKey: 'id',
		exportUrl: '',
		queryForm: {},
		dataList: [],
		order: '',
		asc: false,
		page: 1,
		limit: 10,
		total: 0,
		pageSizes: [1, 10, 20, 50, 100, 200],
		dataListLoading: false,
		dataListSelections: []
	}

	const mergeDefaultOptions = (options, props) => {
		for (const key in options) {
			if (!Object.getOwnPropertyDescriptor(props, key)) {
				props[key] = options[key]
			}
		}
		return props
	}

	// 覆盖默认值
	const state = mergeDefaultOptions(defaultOptions, options)

	onMounted(() => {
		if (state.createdIsNeed) {
			query()
		}
	})

	const query = () => {
		if (!state.dataListUrl) {
			return
		}

		state.dataListLoading = true

		service
			.get(state.dataListUrl, {
				params: {
					order: state.order,
					asc: state.asc,
					page: state.isPage ? state.page : null,
					limit: state.isPage ? state.limit : null,
					...state.queryForm
				}
			})
			.then((res) => {
				state.dataList = state.isPage ? res.data.list : res.data
				state.total = state.isPage ? res.data.total : 0
			})

		state.dataListLoading = false
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

	// 排序
	const sortChangeHandle = (data) => {
		const { prop, order } = data

		if (prop && order) {
			state.order = prop
			state.asc = order === 'ascending'
		} else {
			state.order = ''
		}

		query()
	}

	const deleteHandle = (key) => {
		if (!state.deleteUrl) {
			return
		}

		ElMessageBox.confirm('确定进行删除操作?', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		})
			.then(() => {
				service.delete(state.deleteUrl + '/' + key).then(() => {
					ElMessage.success('删除成功')

					query()
				})
			})
			.catch(() => {})
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
		})
			.then(() => {
				if (state.deleteUrl) {
					service.delete(state.deleteUrl, { data }).then(() => {
						ElMessage.success('删除成功')

						query()
					})
				}
			})
			.catch(() => {})
	}

	return { getDataList, sizeChangeHandle, currentChangeHandle, selectionChangeHandle, sortChangeHandle, deleteHandle, deleteBatchHandle }
}
