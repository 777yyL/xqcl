# 需求管理系统

## 项目简介

需求管理系统用于管理软件部门的需求文档，支持 Excel 数据导入、数据查询、详情查看和 Markdown 格式导出。

## 技术栈

### 后端
- Spring Boot 2.7.18
- MyBatis-Plus 3.5.11
- PostgreSQL 11
- Lombok
- Hutool
- SpringDoc (Swagger)

### 前端
- Vue 3
- TypeScript
- Element Plus
- Vite
- Axios
- Pinia
- Vue Router

## 项目结构

```
xqcl/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/xqcl/
│   │   │   │       ├── common/      # 通用类
│   │   │   │       ├── config/      # 配置类
│   │   │   │       ├── controller/  # 控制器
│   │   │   │       ├── dto/         # 数据传输对象
│   │   │   │       ├── entity/      # 实体类
│   │   │   │       ├── mapper/      # MyBatis Mapper
│   │   │   │       ├── service/     # 服务层
│   │   │   │       └── util/        # 工具类
│   │   │   └── resources/
│   │   │       ├── application.yml  # 应用配置
│   │   │       └── db/
│   │   │           └── schema.sql   # 数据库表结构
│   │   └── test/
│   └── pom.xml
└── fronted/                    # 前端项目
    ├── src/
    │   ├── api/              # API 接口
    │   ├── router/           # 路由配置
    │   ├── views/            # 页面组件
    │   ├── App.vue           # 根组件
    │   └── main.ts           # 入口文件
    ├── index.html
    ├── package.json
    ├── tsconfig.json
    └── vite.config.ts
```

## 快速开始

### 1. 数据库准备

#### 创建数据库
```sql
CREATE DATABASE xqcl_db;
```

#### 执行建表脚本
```bash
psql -U postgres -d xqcl_db -f backend/src/main/resources/db/schema.sql
```

### 2. 后端启动

#### 修改配置
编辑 `backend/src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/xqcl_db
    username: postgres
    password: your_password
```

#### 启动后端
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端启动成功后，访问 Swagger 文档：http://localhost:8080/api/swagger-ui.html

### 3. 前端启动

#### 安装依赖
```bash
cd fronted
npm install
```

#### 启动开发服务器
```bash
npm run dev
```

前端启动成功后，访问：http://localhost:3000

## 功能说明

### 1. 数据导入
- 支持上传需求列表 Excel 文件
- 支持上传需求详情 Excel 文件
- 自动解析并入库，已存在数据更新，不存在则新增

### 2. 需求列表查询
- 支持多条件筛选需求列表
- 支持分页查询
- 点击可查看需求详情

### 3. 需求详情查看
- 展示需求的详细信息
- 包含需求场景、描述、评估等字段

### 4. Markdown 导出
- 支持单个需求导出
- 支持批量导出所有需求
- 导出文件保存到本地目录

## Excel 文件格式

### 需求列表文件字段
需求评估单号、项目名称、商机编号、行业、子行业、区域、国家或地区、产品线、产品系列、产品型号、软件名称、软件版本、状态、是否排期、是否复用方案、紧急程度、评估类型、评估单创建人、创建人部门、需求负责人、负责人所属部门、当前处理人、评估单创建时间、评估单提交时间、最后提交时间、评估时间、最后评估时间、总评估用时（小时）、评估停留时间（天）、排期开始时间、排期结束时间、总工作量（人天）、开发资源分布总部工作量(人天)、开发资源分布区域、开发资源分布区域工作量(人天)、总订单核算工作量（人天）、订单核算总部工作量(人天)、订单核算区域、订单核算区域工作量(人天)、系统测试工作量（人天）、集成测试工作量（人天）、学习成本工作量（人天）、流程管理工作量（人天）、其他工作量详情、期望完成时间、定制单号、JKN单号、开发单号

### 需求详情文件字段
需求评估单号、项目名称、商机编号、行业、子行业、区域、产品线、产品系列、产品型号、软件名称、软件版本、状态、需求名称、需求场景、需求描述、研发评估、组件标识、组件版本、需求分类、需求标签、是否复用方案、评估单创建人、需求负责人、负责人所属部门、评估人、评估人所属部门、评估单创建时间、评估单提交时间、评估单完成时间、组件评估开始时间、组件评估完成时间、组件评估周期、评估用时(h)、评估工作量、工作量详情、停留时间、排期开始时间（研发评估）、排期结束时间（研发评估）、排期开始时间、排期结束时间、定制单号、开发单号

## API 接口

详见 Swagger 文档：http://localhost:8080/api/swagger-ui.html

## 注意事项

1. 确保数据库连接配置正确
2. Excel 文件必须是 .xlsx 或 .xls 格式
3. 导出的 Markdown 文件保存在后端配置的目录中（默认：./export/markdown）
4. 需求评估单号不能为空，且作为唯一标识

## 开发规范

- 后端代码使用 Lombok 简化代码
- 前端代码使用 TypeScript 类型检查
- 遵循 RESTful API 设计规范
- 数据库表名使用下划线命名，Java 实体类使用驼峰命名

## 许可证

MIT License
