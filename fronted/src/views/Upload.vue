<template>
  <el-card shadow="never">
    <template #header>
      <div class="card-header">
        <span>数据导入</span>
      </div>
    </template>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>导入需求列表</span>
            </div>
          </template>
          <el-upload
            class="upload-demo"
            drag
            :auto-upload="false"
            :on-change="handleListFileChange"
            :limit="1"
            accept=".xlsx,.xls"
          >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              拖拽文件到此处或 <em>点击上传</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                只支持 .xlsx 或 .xls 格式的需求列表文件
              </div>
            </template>
          </el-upload>
          <div style="margin-top: 20px; text-align: center;">
            <el-button type="primary" :loading="listImporting" @click="importList" :disabled="!listFile">
              开始导入
            </el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>导入需求详情</span>
            </div>
          </template>
          <el-upload
            class="upload-demo"
            drag
            :auto-upload="false"
            :on-change="handleDetailFileChange"
            :limit="1"
            accept=".xlsx,.xls"
          >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              拖拽文件到此处或 <em>点击上传</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                只支持 .xlsx 或 .xls 格式的需求详情文件
              </div>
            </template>
          </el-upload>
          <div style="margin-top: 20px; text-align: center;">
            <el-button type="primary" :loading="detailImporting" @click="importDetail" :disabled="!detailFile">
              开始导入
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </el-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'
import { reqApi } from '@/api/req'
import type { UploadFile } from 'element-plus'

const listFile = ref<File | null>(null)
const detailFile = ref<File | null>(null)
const listImporting = ref(false)
const detailImporting = ref(false)

const handleListFileChange = (file: UploadFile) => {
  if (file.raw) {
    listFile.value = file.raw
  }
}

const handleDetailFileChange = (file: UploadFile) => {
  if (file.raw) {
    detailFile.value = file.raw
  }
}

const importList = async () => {
  if (!listFile.value) {
    ElMessage.warning('请先选择需求列表文件')
    return
  }

  listImporting.value = true
  try {
    const res: any = await reqApi.importList(listFile.value)
    ElMessage.success(`导入成功，共处理 ${res.data} 条数据`)
    listFile.value = null
  } catch (error) {
    console.error('导入需求列表失败', error)
  } finally {
    listImporting.value = false
  }
}

const importDetail = async () => {
  if (!detailFile.value) {
    ElMessage.warning('请先选择需求详情文件')
    return
  }

  detailImporting.value = true
  try {
    const res: any = await reqApi.importDetail(detailFile.value)
    ElMessage.success(`导入成功，共处理 ${res.data} 条数据`)
    detailFile.value = null
  } catch (error) {
    console.error('导入需求详情失败', error)
  } finally {
    detailImporting.value = false
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.upload-demo {
  width: 100%;
}

:deep(.el-upload-dragger) {
  width: 100%;
}
</style>
