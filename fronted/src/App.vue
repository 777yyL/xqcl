<template>
  <div id="app">
    <el-container>
      <el-header style="background-color: #409EFF; color: white; display: flex; align-items: center;">
        <h2 style="margin: 0;">需求管理系统</h2>
        <el-menu
          :default-active="activeMenu"
          mode="horizontal"
          background-color="#409EFF"
          text-color="#fff"
          active-text-color="#ffd04b"
          style="flex: 1; border: none; margin-left: 50px;"
          router
        >
          <el-menu-item index="/upload">数据导入</el-menu-item>
          <el-menu-item index="/list">需求列表</el-menu-item>
        </el-menu>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style>
#app {
  height: 100vh;
}

.el-container {
  height: 100%;
}

.el-header {
  padding: 0 20px;
}

.el-main {
  padding: 20px;
  background-color: #f5f5f5;
}
</style>
