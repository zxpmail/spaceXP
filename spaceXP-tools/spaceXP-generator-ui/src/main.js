import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import App from './App.vue'
import { router } from './router/index.js'
import 'virtual:svg-icons-register'

import SvgIcon from '@/components/svg-icon'
import '@/icons/iconfont/iconfont'

import 'element-plus/dist/index.css'
import '@/styles/index.scss'


const app = createApp(App)

app.use(router)
app.use(SvgIcon)
app.use(ElementPlus)

app.mount('#app')
