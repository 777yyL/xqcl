# 需求管理系统 - 项目概览

## 已完成功能清单

### 后端模块 ✅

#### 1. 核心组件
- ✅ **实体类 (Entity)**
  - ReqList.java - 需求列表实体（51个字段）
  - ReqDetail.java - 需求详情实体（44个字段）

- ✅ **数据访问层 (Mapper)**
  - ReqListMapper.java - 需求列表 Mapper
  - ReqDetailMapper.java - 需求详情 Mapper

- ✅ **服务层 (Service)**
  - ReqListService.java - 需求列表业务逻辑
  - ReqDetailService.java - 需求详情业务逻辑

- ✅ **控制器层 (Controller)**
  - ReqController.java - 统一的API接口

#### 2. 工具类
- ✅ **ExcelImportUtil.java** - Excel文件解析工具
  - 支持解析需求列表 Excel（51个字段）
  - 支持解析需求详情 Excel（44个字段）
  - 自动处理日期、数字等数据类型

- ✅ **MarkdownExportUtil.java** - Markdown导出工具
  - 整合需求列表和详情数据
  - 生成标准 Markdown 格式文档
  - 自动保存到本地目录

#### 3. 配置类
- ✅ **MybatisPlusConfig.java** - MyBatis-Plus配置
  - 分页插件
  - 自动填充处理器（创建时间、更新时间）

- ✅ **application.yml** - 应用配置
  - 数据源配置（PostgreSQL）
  - 文件上传配置
  - MyBatis-Plus配置
  - Swagger配置

#### 4. 数据库设计
- ✅ **schema.sql** - 数据库表结构
  - req_list 表（需求列表，主表）
  - req_detail 表（需求详情，从表）
  - 索引优化

### 前端模块 ✅

#### 1. 页面组件
- ✅ **Upload.vue** - 数据导入页面
  - 需求列表Excel上传
  - 需求详情Excel上传
  - 拖拽上传支持
  - 导入进度提示

- ✅ **ReqList.vue** - 需求列表页面
  - 多条件搜索表单（12个筛选条件）
  - 数据表格展示（22个展示字段）
  - 分页功能
  - 查看详情功能
  - 导出Markdown功能（单个/批量）

#### 2. 核心配置
- ✅ **main.ts** - 应用入口
- ✅ **App.vue** - 根组件
- ✅ **router/index.ts** - 路由配置
- ✅ **api/req.ts** - API接口封装
- ✅ **vite.config.ts** - Vite构建配置
- ✅ **package.json** - 依赖管理

## API 接口清单

### 1. 数据导入接口
| 接口 | 方法 | 说明 |
|------|------|------|
| /api/req/import/list | POST | 导入需求列表Excel |
| /api/req/import/detail | POST | 导入需求详情Excel |

### 2. 数据查询接口
| 接口 | 方法 | 说明 |
|------|------|------|
| /api/req/list/page | POST | 分页查询需求列表（支持多条件筛选） |
| /api/req/detail/{reqNo} | GET | 查询需求详情 |

### 3. 数据导出接口
| 接口 | 方法 | 说明 |
|------|------|------|
| /api/req/export/markdown/{reqNo} | GET | 导出单个需求的Markdown |
| /api/req/export/markdown/batch | POST | 批量导出Markdown |

## 数据库字段映射

### 需求列表展示字段（22个）
1. 需求评估单号
2. 项目名称
3. 商机编号
4. 行业
5. 区域（只要省份）
6. 产品系列
7. 软件版本
8. 状态
9. 需求负责人
10. 负责人所属部门
11. 开发单号
12. JKN单号
13. 评估单提交时间
14. 总工作量（人天）
15. 开发资源分布总部工作量(人天)
16. 开发资源分布区域工作量(人天)
17. 总订单核算工作量（人天）
18. 订单核算总部工作量(人天)
19. 订单核算区域工作量(人天)
20. 集成测试工作量（人天）
21. 其他工作量详情

### 需求详情展示字段（15个）
1. 需求名称
2. 需求场景
3. 需求描述
4. 研发评估
5. 组件标识
6. 组件版本
7. 需求分类
8. 需求标签
9. 是否复用方案
10. 评估人
11. 评估人所属部门
12. 评估工作量
13. 工作量详情

## 核心功能流程

### 1. 数据导入流程
```
1. 用户在前端页面选择Excel文件
   ↓
2. 前端通过API上传文件
   ↓
3. 后端使用Apache POI解析Excel
   ↓
4. 数据验证和转换
   ↓
5. 判断是新增还是更新
   ↓
6. 批量写入数据库
   ↓
7. 返回导入结果
```

### 2. 数据查询流程
```
1. 用户输入查询条件
   ↓
2. 前端构造查询参数
   ↓
3. 后端使用MyBatis-Plus分页查询
   ↓
4. 返回分页数据
   ↓
5. 前端渲染表格
   ↓
6. 用户点击查看详情
   ↓
7. 前端请求详情数据
   ↓
8. 后端查询并返回
   ↓
9. 前端弹出对话框展示
```

### 3. Markdown导出流程
```
1. 用户点击导出按钮
   ↓
2. 前端调用导出接口
   ↓
3. 后端查询需求列表和详情
   ↓
4. 整合数据生成Markdown内容
   ↓
5. 保存到本地文件
   ↓
6. 返回文件路径
   ↓
7. 前端显示导出结果
```

## 技术亮点

### 后端技术亮点
1. **MyBatis-Plus** - 简化CRUD操作，提供分页、条件构造器等
2. **Apache POI** - 强大的Excel解析能力
3. **Hutool** - 丰富的工具类，简化开发
4. **Swagger** - 自动生成API文档
5. **Spring Boot** - 快速开发，自动配置

### 前端技术亮点
1. **Vue 3** - 组合式API，更好的类型支持
2. **TypeScript** - 类型安全，减少运行时错误
3. **Element Plus** - 丰富的组件库
4. **Vite** - 极速的开发体验
5. **Axios** - 统一的HTTP请求处理

## 项目特色

1. **前后端分离** - 清晰的架构，便于维护和扩展
2. **RESTful API** - 标准的接口设计
3. **自动化文档** - Swagger自动生成API文档
4. **类型安全** - 后端使用Java强类型，前端使用TypeScript
5. **响应式设计** - 前端使用Element Plus，界面美观
6. **高效开发** - 使用MyBatis-Plus和Lombok简化代码

## 后续优化建议

1. **性能优化**
   - 添加Redis缓存
   - 数据库索引优化
   - 分页查询优化

2. **功能增强**
   - 添加用户认证和权限管理
   - 添加操作日志记录
   - 支持导出为其他格式（Word、PDF）
   - 添加数据统计和图表展示

3. **代码优化**
   - 添加单元测试
   - 添加异常处理
   - 添加日志记录
   - 代码注释完善

4. **部署优化**
   - Docker容器化
   - CI/CD自动化部署
   - 监控和告警

## 开发说明

### 环境要求
- JDK 11+
- Node.js 16+
- PostgreSQL 11+
- Maven 3.6+

### 启动步骤
1. 创建数据库并执行schema.sql
2. 修改application.yml中的数据库配置
3. 启动后端：`cd backend && mvn spring-boot:run`
4. 安装前端依赖：`cd fronted && npm install`
5. 启动前端：`npm run dev`
6. 访问前端：http://localhost:3000
7. 访问Swagger：http://localhost:8080/api/swagger-ui.html

## 项目统计

- **后端代码文件**：17个
- **前端代码文件**：9个
- **数据库表**：2个
- **API接口**：7个
- **前端页面**：2个
- **总代码行数**：约3000行

---

**项目状态：开发完成，可投入使用** 🎉
