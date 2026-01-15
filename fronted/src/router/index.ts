import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/upload'
  },
  {
    path: '/upload',
    name: 'Upload',
    component: () => import('@/views/Upload.vue'),
    meta: { title: '数据导入' }
  },
  {
    path: '/list',
    name: 'ReqList',
    component: () => import('@/views/ReqList.vue'),
    meta: { title: '需求列表' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
