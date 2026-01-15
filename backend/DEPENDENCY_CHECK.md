# 依赖问题修复指南

## ✅ 已修复的依赖问题

### 1. mybatis-plus-extension ⭐ **关键**
**问题**：Service 继承 `ServiceImpl` 时报错
**原因**：缺少扩展包
**解决**：已添加 `mybatis-plus-extension:3.5.11`

### 2. spring-boot-starter-jdbc ⭐ **关键**
**问题**：数据源配置失败
**原因**：缺少 JDBC 启动器
**解决**：已添加 `spring-boot-starter-jdbc`

### 3. poi ⭐ **关键**
**问题**：Excel 处理类找不到
**原因**：只有 poi-ooxml，缺少核心包
**解决**：已添加 `poi:5.2.5`

---

## 🚀 快速修复步骤

### 步骤1：更新 pom.xml（已完成）
pom.xml 已经更新，包含了所有必需的依赖：
```xml
<!-- MyBatis-Plus 扩展包 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-extension</artifactId>
    <version>3.5.11</version>
</dependency>

<!-- JDBC 启动器 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<!-- POI 核心包 -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>5.2.5</version>
</dependency>
```

### 步骤2：重新加载 Maven 依赖

#### 方法1：使用 IDEA
```
1. 打开 IDEA 右侧的 Maven 面板
2. 点击刷新按钮（Reload All Maven Projects）
3. 等待依赖下载完成
```

#### 方法2：使用命令行
```bash
cd backend

# 清理并重新安装
mvn clean install

# 如果下载慢，可以跳过测试
mvn clean install -DskipTests

# 强制更新依赖
mvn clean install -U
```

### 步骤3：验证依赖是否安装成功

```bash
# 查看依赖树
mvn dependency:tree | grep -E "(mybatis-plus|poi|jdbc)"

# 应该看到以下依赖：
# ├── com.baomidou:mybatis-plus-boot-starter:jar:3.5.11
# ├── com.baomidou:mybatis-plus-extension:jar:3.5.11  ✅
# ├── com.baomidou:mybatis-plus-jsqlparser:jar:3.5.11
# ├── org.apache.poi:poi:jar:5.2.5  ✅
# ├── org.apache.poi:poi-ooxml:jar:5.2.5
# └── org.springframework.boot:spring-boot-starter-jdbc:jar:2.7.18  ✅
```

---

## 🧪 测试依赖是否正常

### 测试1：启动后端服务
```bash
cd backend
mvn spring-boot:run
```

**预期结果**：
```
需求管理系统启动成功！
Swagger 文档地址: http://localhost:8080/api/swagger-ui.html
```

### 测试2：访问 Swagger
```
浏览器打开: http://localhost:8080/api/swagger-ui.html
```

**预期结果**：能看到 API 接口列表

### 测试3：测试批量导入接口
```bash
# 上传一个小文件测试
curl -X POST http://localhost:8080/api/req/import/detail \
  -F "file=@test.xlsx"
```

---

## ❌ 常见错误及解决

### 错误1：Service 无法继承 ServiceImpl
```
错误信息:
ServiceImpl cannot be resolved to a type
```
**原因**：缺少 `mybatis-plus-extension`
**解决**：
```bash
mvn clean install
```

### 错误2：数据源配置失败
```
错误信息:
Failed to configure a DataSource: 'url' attribute is not specified
```
**原因**：缺少 `spring-boot-starter-jdbc`
**解决**：
```bash
mvn clean install
```

### 错误3：POI 类找不到
```
错误信息:
package org.apache.poi.ss.usermodel does not exist
```
**原因**：缺少 `poi` 核心包
**解决**：
```bash
mvn clean install
```

### 错误4：Maven 依赖下载失败
```
错误信息:
Could not resolve dependencies
```
**原因**：网络问题或 Maven 仓库配置问题
**解决**：
```bash
# 方法1：使用阿里云镜像
# 在 ~/.m2/settings.xml 中添加：
<mirrors>
    <mirror>
        <id>aliyun</id>
        <mirrorOf>central</mirrorOf>
        <name>Aliyun Maven</name>
        <url>https://maven.aliyun.com/repository/public</url>
    </mirror>
</mirrors>

# 方法2：强制更新
mvn clean install -U

# 方法3：清理本地仓库缓存
rm -rf ~/.m2/repository/com/baomidou
mvn clean install
```

---

## 📦 完整依赖列表（已确认）

### 核心依赖
```xml
<!-- Spring Boot Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Boot JDBC ⭐ 新增 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<!-- Spring Boot Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- MyBatis-Plus Boot Starter -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.11</version>
</dependency>

<!-- MyBatis-Plus Extension ⭐ 新增 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-extension</artifactId>
    <version>3.5.11</version>
</dependency>

<!-- MyBatis-Plus JSQLParser -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-jsqlparser</artifactId>
    <version>3.5.11</version>
</dependency>

<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Apache POI ⭐ 新增 -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>5.2.5</version>
</dependency>

<!-- Apache POI OOXML -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.5</version>
</dependency>

<!-- Hutool -->
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.8.25</version>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- SpringDoc OpenAPI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.7.0</version>
</dependency>
```

---

## ✅ 验证清单

- [ ] pom.xml 已更新（包含新增的3个依赖）
- [ ] Maven 依赖已重新加载（`mvn clean install`）
- [ ] 后端服务可以正常启动
- [ ] Swagger 可以正常访问
- [ ] Excel 导入接口可以正常调用
- [ ] 批量导入功能正常

---

## 📞 如果还有问题

1. **检查 Maven 设置**：
   ```bash
   mvn -version
   ```

2. **检查 Java 版本**：
   ```bash
   java -version  # 应该是 11+
   ```

3. **清理并重新构建**：
   ```bash
   cd backend
   rm -rf target/
   mvn clean install -DskipTests
   ```

4. **查看详细错误信息**：
   ```bash
   mvn clean install -X
   ```

5. **查看日志**：
   ```bash
   tail -f logs/spring.log
   ```

---

**修复完成**：所有必需依赖已添加 ✅
**测试状态**：请运行 `mvn clean install` 验证
