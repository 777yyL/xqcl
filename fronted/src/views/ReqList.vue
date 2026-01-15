<template>
  <el-card shadow="never">
    <template #header>
      <div class="card-header">
        <span>需求列表</span>
        <el-button type="primary" @click="exportAll">导出全部 Markdown</el-button>
      </div>
    </template>

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="queryForm" class="search-form">
      <el-form-item label="需求评估单号">
        <el-input v-model="queryForm.reqNo" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="项目名称">
        <el-input v-model="queryForm.projectName" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="商机编号">
        <el-input v-model="queryForm.opportunityNo" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="行业">
        <el-input v-model="queryForm.industry" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="区域">
        <el-input v-model="queryForm.region" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="产品系列">
        <el-input v-model="queryForm.productSeries" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="软件版本">
        <el-input v-model="queryForm.softwareVersion" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="状态">
        <el-input v-model="queryForm.status" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="需求负责人">
        <el-input v-model="queryForm.reqOwner" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="负责人所属部门">
        <el-input v-model="queryForm.ownerDept" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="开发单号">
        <el-input v-model="queryForm.devNo" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="JKN单号">
        <el-input v-model="queryForm.jknNo" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
      <el-table-column fixed prop="reqNo" label="需求评估单号" width="150" />
      <el-table-column prop="projectName" label="项目名称" width="150" />
      <el-table-column prop="opportunityNo" label="商机编号" width="120" />
      <el-table-column prop="industry" label="行业" width="100" />
      <el-table-column prop="region" label="区域" width="100" />
      <el-table-column prop="productSeries" label="产品系列" width="120" />
      <el-table-column prop="softwareVersion" label="软件版本" width="100" />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column prop="reqOwner" label="需求负责人" width="120" />
      <el-table-column prop="ownerDept" label="负责人所属部门" width="150" />
      <el-table-column prop="devNo" label="开发单号" width="120" />
      <el-table-column prop="jknNo" label="JKN单号" width="120" />
      <el-table-column prop="submitTime" label="评估单提交时间" width="160" />
      <el-table-column prop="totalWorkload" label="总工作量（人天）" width="120" />
      <el-table-column prop="devHqWorkload" label="开发总部工作量" width="120" />
      <el-table-column prop="devRegionWorkload" label="开发区域工作量" width="120" />
      <el-table-column prop="totalOrderWorkload" label="总订单核算工作量" width="140" />
      <el-table-column prop="orderHqWorkload" label="订单总部工作量" width="120" />
      <el-table-column prop="orderRegionWorkload" label="订单区域工作量" width="120" />
      <el-table-column prop="integrationTestWorkload" label="集成测试工作量" width="120" />
      <el-table-column prop="otherWorkloadDetail" label="其他工作量详情" width="150" show-overflow-tooltip />
      <el-table-column fixed="right" label="操作" width="200">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="handleViewDetail(scope.row)">
            查看详情
          </el-button>
          <el-button link type="success" size="small" @click="handleExportMarkdown(scope.row)">
            导出 Markdown
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="pagination.current"
      v-model:page-size="pagination.size"
      :page-sizes="[10, 20, 50, 100]"
      :total="pagination.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="fetchData"
      @current-change="fetchData"
      style="margin-top: 20px; justify-content: center;"
    />

    <!-- 详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="需求详情" width="80%" :close-on-click-modal="false">
      <el-table :data="detailData" border stripe style="width: 100%" v-loading="detailLoading">
        <el-table-column prop="reqName" label="需求名称" width="200" show-overflow-tooltip />
        <el-table-column prop="reqScene" label="需求场景" width="200" show-overflow-tooltip />
        <el-table-column prop="reqDesc" label="需求描述" width="250" show-overflow-tooltip />
        <el-table-column prop="rdEval" label="研发评估" width="200" show-overflow-tooltip />
        <el-table-column prop="componentId" label="组件标识" width="120" />
        <el-table-column prop="componentVersion" label="组件版本" width="100" />
        <el-table-column prop="reqCategory" label="需求分类" width="100" />
        <el-table-column prop="reqTag" label="需求标签" width="120" />
        <el-table-column prop="isReuse" label="是否复用方案" width="100" />
        <el-table-column prop="evaluator" label="评估人" width="120" />
        <el-table-column prop="evaluatorDept" label="评估人所属部门" width="150" />
        <el-table-column prop="evalWorkload" label="评估工作量" width="100" />
        <el-table-column prop="workloadDetail" label="工作量详情" width="200" show-overflow-tooltip />
      </el-table>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { reqApi } from '@/api/req'

const queryForm = reactive({
  reqNo: '',
  projectName: '',
  opportunityNo: '',
  industry: '',
  region: '',
  productSeries: '',
  softwareVersion: '',
  status: '',
  reqOwner: '',
  ownerDept: '',
  devNo: '',
  jknNo: ''
})

const tableData = ref([])
const loading = ref(false)
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const detailDialogVisible = ref(false)
const detailData = ref([])
const detailLoading = ref(false)

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      ...queryForm,
      current: pagination.current,
      size: pagination.size
    }
    const res: any = await reqApi.pageQuery(params)
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error('获取数据失败', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchData()
}

// 重置
const handleReset = () => {
  Object.assign(queryForm, {
    reqNo: '',
    projectName: '',
    opportunityNo: '',
    industry: '',
    region: '',
    productSeries: '',
    softwareVersion: '',
    status: '',
    reqOwner: '',
    ownerDept: '',
    devNo: '',
    jknNo: ''
  })
  pagination.current = 1
  fetchData()
}

// 查看详情
const handleViewDetail = async (row: any) => {
  detailDialogVisible.value = true
  detailLoading.value = true
  try {
    const res: any = await reqApi.getDetail(row.reqNo)
    detailData.value = res.data
  } catch (error) {
    console.error('获取详情失败', error)
  } finally {
    detailLoading.value = false
  }
}

// 导出单个 Markdown
const handleExportMarkdown = async (row: any) => {
  try {
    const res: any = await reqApi.exportMarkdown(row.reqNo)
    ElMessage.success(`导出成功，文件路径: ${res.data}`)
  } catch (error) {
    console.error('导出失败', error)
  }
}

// 导出全部 Markdown
const exportAll = async () => {
  try {
    await ElMessageBox.confirm('确定要导出全部需求的 Markdown 文件吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res: any = await reqApi.batchExportMarkdown(null)
    ElMessage.success(`导出成功，共导出 ${res.data.length} 个文件`)
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量导出失败', error)
    }
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.search-form {
  margin-bottom: 20px;
}
</style>
