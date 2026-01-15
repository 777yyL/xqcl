# Maven 依赖说明

## 核心依赖清单

### 1. MyBatis-Plus 相关依赖

#### mybatis-plus-boot-starter (v3.5.11)
```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.11</version>
</dependency>
```
**用途**：MyBatis-Plus 核心启动器，提供自动配置和基础功能

#### mybatis-plus-extension (v3.5.11) ⭐ 新增
```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-extension</artifactId>
    <version>3.5.11</version>
</dependency>
```
**用途**：MyBatis-Plus 扩展包，包含 `ServiceImpl` 基类
- ✅ **批量插入**：`saveBatch()`
- ✅ **批量更新**：`updateBatchById()`
- ✅ **Service 基类**：`ServiceImpl<Mapper, Entity>`

**重要**：Service 层继承了 `ServiceImpl` 必须添加此依赖！

#### mybatis-plus-jsqlparser (v3.5.11)
```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-jsqlparser</artifactId>
    <version>3.5.11</version>
</dependency>
```
**用途**：JSQLParser 支持，用于 SQL 解析和条件构造器

---

### 2. Apache POI 相关依赖

#### poi (v5.2.5) ⭐ 新增
```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>5.2.5</version>
</dependency>
```
**用途**：Apache POI 核心包，处理 `.xls` 格式（Excel 97-2003）
- 提供基础的 Excel 读写功能
- `HSSFWorkbook` 类
- 单元格类型定义

#### poi-ooxml (v5.2.5)
```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.5</version>
</dependency>
```
**用途**：处理 `.xlsx` 格式（Excel 2007+）
- ✅ **XSSFWorkbook** 类（优化版使用）
- ✅ 流式读取，避免内存溢出
- ✅ 支持 OOXML 格式

**依赖关系**：
```
poi-ooxml 依赖 poi
poi-ooxml 依赖 poi-ooxml-lite
```

---

### 3. Spring Boot 相关依赖

#### spring-boot-starter-jdbc ⭐ 新增
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```
**用途**：JDBC 启动器，提供数据源和事务管理
- ✅ HikariCP 连接池（自动配置）
- ✅ 事务管理器
- ✅ JdbcTemplate

**重要**：MyBatis-Plus 需要此依赖来管理数据源！

#### spring-boot-starter-web
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
**用途**：Web 启动器，提供 RESTful API 支持

#### spring-boot-starter-validation
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```
**用途**：参数校验（`@Valid`、`@NotNull` 等）

---

### 4. 数据库驱动

#### postgresql (runtime)
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```
**用途**：PostgreSQL JDBC 驱动

**注意**：
- `scope=runtime`：运行时才需要，编译时不需要
- 自动由 HikariCP 加载

---

### 5. 工具类库

#### hutool-all (v5.8.25)
```xml
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.8.25</version>
</dependency>
```
**用途**：Java 工具类库
- `CollUtil.isEmpty()` - 集合判空
- `StrUtil.isNotBlank()` - 字符串判空
- `FileUtil.mkdir()` - 文件操作
- `FileUtil.writeUtf8String()` - 写文件

#### lombok
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```
**用途**：简化 Java 代码
- `@Data` - 自动生成 getter/setter
- `@Slf4j` - 日志对象
- `@AllArgsConstructor` - 构造函数

---

### 6. API 文档

#### springdoc-openapi-ui (v1.7.0)
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.7.0</version>
</dependency>
```
**用途**：Swagger UI（OpenAPI 3.0）
- 访问地址：`http://localhost:8080/api/swagger-ui.html`
- 自动生成 API 文档
- 在线测试接口

---

## 依赖关系图

```
spring-boot-starter-web
    ├── spring-boot-starter
    ├── spring-webmvc
    └── jackson-databind

spring-boot-starter-jdbc
    ├── HikariCP (连接池)
    ├── spring-jdbc (事务管理)
    └── spring-tx

mybatis-plus-boot-starter
    ├── mybatis-plus-core (核心)
    ├── mybatis-plus-extension (扩展) ⭐
    ├── mybatis-plus-annotation (注解)
    ├── mybatis (持久层)
    └── mybatis-spring (集成Spring)

mybatis-plus-extension ⭐
    ├── mybatis-plus-core
    └── IService<>, ServiceImpl<> (批量操作)

poi-ooxml
    ├── poi ⭐ (核心)
    ├── poi-ooxml-lite (轻量级)
    └──.xmlbeans (XML处理)

postgresql
    └── JDBC驱动 (runtime)
```

---

## 新增依赖的重要性

### 1️⃣ mybatis-plus-extension
**为什么需要？**
- Service 层继承了 `ServiceImpl<ReqDetailMapper, ReqDetail>`
- 需要使用 `saveBatch()` 和 `updateBatchById()` 批量操作

**如果没有会怎样？**
```
错误: ServiceImpl cannot be resolved to a type
```

### 2️⃣ spring-boot-starter-jdbc
**为什么需要？**
- MyBatis-Plus 需要数据源和事务管理器
- 提供 HikariCP 连接池

**如果没有会怎样？**
```
错误: Failed to configure a DataSource: 'url' attribute is not specified
```

### 3️⃣ poi (核心包)
**为什么需要？**
- `poi-ooxml` 依赖 `poi`
- 提供 `Workbook`、`Sheet`、`Row` 等基础类

**如果没有会怎样？**
```
错误: package org.apache.poi.ss.usermodel does not exist
```

---

## Maven 命令

### 重新加载依赖
```bash
# 清理并重新安装
mvn clean install

# 强制更新（使用最新快照）
mvn clean install -U

# 跳过测试
mvn clean install -DskipTests

# 查看依赖树
mvn dependency:tree
```

### 查看依赖冲突
```bash
# 查看依赖分析
mvn dependency:analyze

# 查看有效 POM
mvn help:effective-pom
```

---

## 常见依赖问题

### 问题1：找不到 ServiceImpl
```
解决：添加 mybatis-plus-extension 依赖
```

### 问题2：数据源配置失败
```
解决：添加 spring-boot-starter-jdbc 依赖
```

### 问题3：POI 类找不到
```
解决：同时添加 poi 和 poi-ooxml 依赖
```

### 问题4：批量插入方法找不到
```
解决：确保 Service 继承了 ServiceImpl<Mapper, Entity>
并添加了 mybatis-plus-extension 依赖
```

---

## 完整依赖列表（按字母顺序）

| 依赖 | 版本 | 用途 |
|------|------|------|
| hutool-all | 5.8.25 | 工具类 |
| mybatis-plus-boot-starter | 3.5.11 | MyBatis-Plus 启动器 |
| mybatis-plus-extension | 3.5.11 | MyBatis-Plus 扩展 ⭐ |
| mybatis-plus-jsqlparser | 3.5.11 | SQL 解析 |
| poi | 5.2.5 | Excel 处理核心 ⭐ |
| poi-ooxml | 5.2.5 | Excel 2007+ 支持 |
| postgresql | (runtime) | PostgreSQL 驱动 |
| spring-boot-starter-jdbc | 2.7.18 | JDBC + 事务 ⭐ |
| spring-boot-starter-validation | 2.7.18 | 参数校验 |
| spring-boot-starter-web | 2.7.18 | Web 支持 |
| springdoc-openapi-ui | 1.7.0 | Swagger 文档 |
| lombok | (optional) | 简化代码 |

---

**更新日期**：2024-01-15
**适用版本**：v1.1.0
