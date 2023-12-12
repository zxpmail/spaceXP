<template>
  <el-config-provider :locale="zh_cn">
    <router-view />
  </el-config-provider>
</template>

<script setup >
import zh_cn from 'element-plus/dist/locale/zh-cn.mjs'
import { RouterView } from 'vue-router'
import { useTitle } from '@vueuse/core'

// 设置标题
useTitle('XP Generator')
</script>
