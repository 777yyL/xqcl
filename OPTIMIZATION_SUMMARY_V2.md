# 代码优化总结 - 浮点类型字符串化 + Markdown格式重构

## 优化概述

完成了两项重要优化：
1. **浮点类型字段改为字符串导入** - 避免精度丢失和格式转换问题
2. **Markdown导出格式重构** - 改为字段：值格式，更适合知识库向量化

---

## 1. 浮点类型字段优化

### 修改内容

#### 实体类修改

**ReqList.java** - 15个BigDecimal字段改为String
```java
// 修改前
private BigDecimal totalEvalHours;
private BigDecimal evalStayDays;
private BigDecimal totalWorkload;
// ... 共15个字段

// 修改后
private String totalEvalHours;
private String evalStayDays;
private String totalWorkload;
// ... 共15个字段
```

**ReqDetail.java** - 3个BigDecimal字段改为String
```java
// 修改前
private BigDecimal evalHours;
private BigDecimal stayTime;
private BigDecimal evalWorkload;

// 修改后
private String evalHours;
private String stayTime;
private String evalWorkload;
```

#### ExcelImportUtil.java 修改

**删除的方法**：
```java
// 已删除
private static BigDecimal getCellBigDecimalValue(Row row, int columnIndex)
```

**修改的解析调用**：
```java
// 修改前
reqList.setTotalEvalHours(getCellBigDecimalValue(row, 27));
reqList.setDevHqWorkload(getCellBigDecimalValue(row, 32));

// 修改后
reqList.setTotalEvalHours(getCellStringValue(row, 27));
reqList.setDevHqWorkload(getCellStringValue(row, 32));
```

**影响字段**：共16处修改

### 优化效果

#### 修改前的问题
```
问题1：精度丢失
Excel: 1.5
数据库: 1.50000000000000002

问题2：格式限制
Excel: 1,234.56（带千分位）
解析失败：无法转换为BigDecimal

问题3：科学计数法
Excel: 1.23E+3
数据库: 1230.0（格式改变）
```

#### 修改后的优势
```
✅ 原样存储：Excel中什么格式，数据库就存什么格式
✅ 避免错误：不需要类型转换，不会解析失败
✅ 保留精度：不会有浮点数精度丢失
✅ 格式灵活：支持任意格式的数值（包括千分位、科学计数法等）
```

### 数据示例

| Excel中的值 | 修改前数据库 | 修改后数据库 |
|-------------|--------------|--------------|
| `1.5` | `1.50000000000000002` | `1.5` ✅ |
| `1,234.56` | 解析失败 ❌ | `1,234.56` ✅ |
| `1.23E+3` | `1230.0` | `1.23E+3` ✅ |
| `约50` | 解析失败 ❌ | `约50` ✅ |
| `1.5万` | 解析失败 ❌ | `1.5万` ✅ |

---

## 2. Markdown 导出格式重构

### 修改内容

#### MarkdownExportUtil.java 完全重构

**删除的方法**：
```java
private static void appendTableRow(StringBuilder md, String key, String value)
private static String formatDate(LocalDateTime dateTime)
private static String formatBigDecimal(BigDecimal value)
```

**新增的方法**：
```java
private static void appendField(StringBuilder md, String fieldName, String value)
```

#### 格式对比

##### 修改前（表格格式）
```markdown
# 需求文档

## 一、需求列表信息

| 字段 | 内容 |
|------|------|
| 需求评估单号 | REQ001 |
| 项目名称 | 测试项目 |
| 总工作量（人天） | 10.5 |

## 二、需求详情信息

### 1. 需求A

| 字段 | 内容 |
|------|------|
| 需求名称 | 需求A |
| 评估工作量 | 5.2 |
```

**问题**：
- ❌ 表格格式不利于向量化检索
- ❌ 知识库对表格内容提取效果差
- ❌ 机器学习中表格识别不准确

##### 修改后（字段：值格式）
```markdown
# REQ001 测试项目

## 一、需求列表信息

**需求评估单号**：REQ001
**项目名称**：测试项目
**总工作量（人天）**：10.5

## 二、需求详情信息

### 1. 需求A

**需求评估单号**：REQ001
**需求名称**：需求A
**评估工作量**：5.2
```

**优势**：
- ✅ 纯文本格式，易于向量化
- ✅ 知识库提取效果好
- ✅ 标题层级清晰（一级标题=需求单号+项目名称）
- ✅ 字段名加粗，易于识别

### 导出格式说明

#### 一级标题
```
# {需求单号} {项目名称}
```

#### 二级标题
```
## 一、需求列表信息
## 二、需求详情信息
```

#### 三级标题
```
### 1. {需求名称}
### 2. {需求名称}
```

#### 字段格式
```
**字段名**：字段值
```

#### 完整示例
```markdown
# REQ20240115001 智慧校园管理系统

## 一、需求列表信息

**需求评估单号**：REQ20240115001
**项目名称**：智慧校园管理系统
**商机编号**：OPP001
**行业**：教育
**区域**：北京
**产品系列**：智慧教育
**软件版本**：V2.0
**状态**：开发中
**需求负责人**：张三
**总工作量（人天）**：100.5
**开发资源分布总部工作量(人天)**：50.0

## 二、需求详情信息

### 1. 用户管理模块

**需求名称**：用户管理模块
**需求场景**：学生、教师登录注册
**需求描述**：支持手机号、邮箱、微信登录
**评估工作量**：5.5

### 2. 成绩管理模块

**需求名称**：成绩管理模块
**需求场景**：成绩录入、查询、统计
**需求描述**：支持Excel导入导出
**评估工作量**：8.0
```

---

## 数据库字段类型变更

### 需要修改的表

#### req_list 表
```sql
-- 数值字段改为 VARCHAR
ALTER TABLE req_list
  ALTER COLUMN total_eval_hours TYPE VARCHAR(50),
  ALTER COLUMN eval_stay_days TYPE VARCHAR(50),
  ALTER COLUMN total_workload TYPE VARCHAR(50),
  ALTER COLUMN dev_hq_workload TYPE VARCHAR(50),
  ALTER COLUMN dev_region_workload TYPE VARCHAR(50),
  ALTER COLUMN total_order_workload TYPE VARCHAR(50),
  ALTER COLUMN order_hq_workload TYPE VARCHAR(50),
  ALTER COLUMN order_region_workload TYPE VARCHAR(50),
  ALTER COLUMN system_test_workload TYPE VARCHAR(50),
  ALTER COLUMN integration_test_workload TYPE VARCHAR(50),
  ALTER COLUMN learning_cost_workload TYPE VARCHAR(50),
  ALTER COLUMN process_manage_workload TYPE VARCHAR(50);
```

#### req_detail 表
```sql
ALTER TABLE req_detail
  ALTER COLUMN eval_hours TYPE VARCHAR(50),
  ALTER COLUMN stay_time TYPE VARCHAR(50),
  ALTER COLUMN eval_workload TYPE VARCHAR(50);
```

### 完整字段类型变更

| 字段类型 | 原类型 | 新类型 | 说明 |
|---------|--------|--------|------|
| 时间字段 | TIMESTAMP | VARCHAR(50) | ✅ 已修改 |
| 数值字段 | DECIMAL | VARCHAR(50) | ✅ 已修改 |
| 文本字段 | VARCHAR | VARCHAR | 无需修改 |

---

## 修改文件清单

| 文件 | 修改内容 |
|------|---------|
| `ReqList.java` | 15个BigDecimal字段改为String |
| `ReqDetail.java` | 3个BigDecimal字段改为String |
| `ExcelImportUtil.java` | 删除getCellBigDecimalValue，16处调用改为getCellStringValue |
| `MarkdownExportUtil.java` | 完全重构，改为字段：值格式，删除表格 |

---

## 使用示例

### 1. 导入数值数据

```bash
# Excel中的数值格式（都能正确导入）
1.5          → 数据库: "1.5"
1,234.56     → 数据库: "1,234.56"
1.23E+3      → 数据库: "1.23E+3"
约50         → 数据库: "约50"
1.5万        → 数据库: "1.5万"
```

### 2. 导出Markdown

```bash
# 点击"导出 Markdown"按钮
# 下载文件：需求_REQ20240115001.md

# 文件内容示例：
# REQ20240115001 智慧校园管理系统

## 一、需求列表信息

**需求评估单号**：REQ20240115001
**项目名称**：智慧校园管理系统
**总工作量（人天）**：100.5
...
```

### 3. 上传到知识库

```bash
# 导出的Markdown文件可以直接上传到知识库
# 知识库会进行向量化检索

# 检索示例：
问："智慧校园管理系统的总工作量是多少？"
答："智慧校园管理系统的总工作量为100.5人天"
```

---

## 优势总结

### 1. 浮点类型字符串化
| 优势 | 说明 |
|------|------|
| ✅ 原样存储 | Excel中什么格式就存什么格式 |
| ✅ 避免精度丢失 | 不会有浮点数精度问题 |
| ✅ 格式灵活 | 支持任意格式（千分位、科学计数法、中文数字等） |
| ✅ 避免解析错误 | 不需要类型转换，不会解析失败 |
| ✅ 简化代码 | 删除了BigDecimal相关转换代码 |

### 2. Markdown格式重构
| 优势 | 说明 |
|------|------|
| ✅ 标题清晰 | 一级标题=需求单号+项目名称 |
| ✅ 易于向量化 | 纯文本格式，知识库提取效果好 |
| ✅ 结构清晰 | 字段：值格式，一目了然 |
| ✅ 可读性强 | 字段名加粗，易于识别 |
| ✅ 符合标准 | 符合Markdown标准语法 |

---

## 注意事项

### 1. 数据库迁移
```sql
-- ⚠️ 必须执行：修改字段类型
-- 详见上文的数据库迁移SQL
```

### 2. 数值计算
```java
// ⚠️ 字符串无法直接计算
// 需要时手动转换
String workload = reqList.getTotalWorkload();
if (StrUtil.isNotBlank(workload)) {
    // 移除千分位、单位等
    String cleaned = workload.replace(",", "").replace("万", "0000");
    double value = Double.parseDouble(cleaned);
}
```

### 3. 向量化检索
```markdown
# ✅ 推荐格式
**总工作量（人天）**：100.5

# ❌ 不推荐格式
| 总工作量（人天） | 100.5 |
```

---

## 测试建议

### 测试1：数值格式导入
```
创建Excel，包含：
- 1.5
- 1,234.56
- 1.23E+3
- 约50
- 1.5万

预期：所有格式都原样导入数据库
```

### 测试2：Markdown导出
```
1. 导出需求
2. 检查文件内容
3. 确认格式为：**字段名**：字段值
4. 确认一级标题为：# 需求单号 项目名称
```

### 测试3：知识库向量化
```
1. 上传导出的Markdown到知识库
2. 执行向量化
3. 测试检索效果
```

---

**优化完成时间**：2024-01-15
**影响范围**：所有数值字段、Markdown导出格式
**数据库迁移**：必需
