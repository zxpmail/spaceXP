
import {  createApp } from 'vue'
import VXETable from 'vxe-table'
import 'vxe-table/lib/style.css'
import ElementPlus from 'element-plus'
import App from './App.vue'
import { router } from './router/index.js'
import 'virtual:svg-icons-register'

import SvgIcon from '@/components/svg-icon'
import '@/icons/iconfont/iconfont'

import 'element-plus/dist/index.css'
import '@/styles/index.scss'

VXETable.setup({
	zIndex: 3000,
	select: {
		transfer: true
	}
})
const app = createApp(App)

app.use(router)
app.use(SvgIcon)
app.use(ElementPlus)
app.use(VXETable)

app.mount('#app')
