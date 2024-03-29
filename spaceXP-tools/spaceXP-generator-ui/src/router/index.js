import { createRouter, createWebHashHistory} from 'vue-router'

export const menuRoutes = [
	{
    path: '/generator',
    name: 'Generator',
    component: () => import('../views/generator/index.vue'),
    meta: {
      title: '代码生成',
      icon: 'icon-fire'
    }
  },
  {
    path: '/database',
    name: 'Database',
    component: () => import('../views/database/index.vue'),
    meta: {
      title: '数据库管理',
      icon: 'icon-control'
    }
  },
  {
    path: '/datasource',
    name: 'DataSource',
    component: () => import('../views/datasource/index.vue'),
    meta: {
      title: '数据源管理',
      icon: 'icon-database-fill'
    }
  },
  {
    path: '/field-type',
    name: 'FieldType',
    component: () => import('../views/field-type/index.vue'),
    meta: {
      title: '字段类型映射',
      icon: 'icon-menu'
    }
  },
  {
    path: '/project',
    name: 'ProjectIndex',
    component: () => import('../views/project/index.vue'),
    meta: {
      title: '项目信息',
      icon: 'icon-edit-square'
    }
  }
]

export const constantRoutes = [
	{
		path: '/redirect',
		component: () => import('../layout/index.vue'),
		children: [
			{
				path: '/redirect/:path(.*)',
				component: () => import('../layout/components/Router/Redirect.vue')
			}
		]
	},
	{
		path: '/',
		component: () => import('../layout/index.vue'),
		redirect: '/generator',
		children: [...menuRoutes]
	},
	{
		path: '/404',
		component: () => import('../views/404.vue')
	},
	{
		path: '/:pathMatch(.*)',
		redirect: '/404'
	}
]

export const router = createRouter({
	history: createWebHashHistory(),
	routes: constantRoutes
})
