// 全局组件安装
export const withInstall = (component, alias) => {
	const comp = component
	comp.install = (app) => {
		app.component(comp.__name || comp.displayName, component)
		if (alias) {
			app.config.globalProperties[alias] = component
		}
	}
	return component
}
