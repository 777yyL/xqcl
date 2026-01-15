# 时间字段字符串处理修改说明

## 修改概述

将 Excel 中所有时间字段从 `LocalDateTime` 类型改为 `String` 类型，解析时直接作为字符串读取，不进行日期类型转换。

---

## 修改内容

### 1. 实体类修改

#### ReqList.java
**修改前**：
```java
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime createTime;
```

**修改后**：
```java
private String createTime;
```

**影响字段**：
- `createTime` - 评估单创建时间
- `submitTime` - 评估单提交时间
- `lastSubmitTime` - 最后提交时间
- `evalTime` - 评估时间
- `lastEvalTime` - 最后评估时间
- `scheduleStartTime` - 排期开始时间
- `scheduleEndTime` - 排期结束时间
- `expectedCompleteTime` - 期望完成时间
- `createdAt` - 创建时间
- `updatedAt` - 更新时间

---

#### ReqDetail.java
**修改前**：
```java
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime createTime;
```

**修改后**：
```java
private String createTime;
```

**影响字段**：
- `createTime` - 评估单创建时间
- `submitTime` - 评估单提交时间
- `completeTime` - 评估单完成时间
- `componentEvalStartTime` - 组件评估开始时间
- `componentEvalEndTime` - 组件评估完成时间
- `rdScheduleStartTime` - 排期开始时间（研发评估）
- `rdScheduleEndTime` - 排期结束时间（研发评估）
- `scheduleStartTime` - 排期开始时间
- `scheduleEndTime` - 排期结束时间
- `createdAt` - 创建时间
- `updatedAt` - 更新时间

---

### 2. ExcelImportUtil.java 修改

#### 删除的方法
```java
// 已删除
private static LocalDateTime getCellDateTimeValue(Row row, int columnIndex)
```

#### 修改的解析方法
**修改前**：
```java
reqList.setCreateTime(getCellDateTimeValue(row, 22));
reqList.setSubmitTime(getCellDateTimeValue(row, 23));
// ...
```

**修改后**：
```java
reqList.setCreateTime(getCellStringValue(row, 22));
reqList.setSubmitTime(getCellStringValue(row, 23));
// ...
```

#### 删除的导入
```java
import java.time.Instant;       // 已删除
import java.time.LocalDateTime;  // 已删除
import java.time.ZoneId;         // 已删除
import java.util.Date;           // 已删除
```

---

## 修改原因

### 问题
1. Excel 中的时间格式多样（`yyyy-MM-dd HH:mm:ss`、`yyyy/MM/dd`、`MM-dd-yyyy` 等）
2. 不同格式的日期解析可能失败
3. POI 解析日期类型时依赖 Excel 的单元格格式设置
4. 格式转换增加了解析复杂度和出错概率

### 解决方案
- ✅ 直接将时间作为字符串存储
- ✅ 保留 Excel 中的原始格式
- ✅ 避免类型转换错误
- ✅ 简化解析逻辑

---

## 数据存储格式

### Excel 中的时间格式
```
2024-01-15 10:30:00
2024/01/15 10:30
2024-01-15
15-Jan-2024
```

### 数据库中的存储格式（VARCHAR）
```
2024-01-15 10:30:00
2024/01/15 10:30
2024-01-15
15-Jan-2024
```

**保持原样，不做任何转换**

---

## 影响范围

### ✅ 优点
1. **避免解析错误**：不依赖 POI 的日期解析
2. **保留原始格式**：Excel 中的时间格式原样保存
3. **简化逻辑**：减少类型转换代码
4. **提高成功率**：任何格式的日期都能导入

### ⚠️ 注意事项
1. **数据库字段类型**：需要将数据库中的 `TIMESTAMP` 改为 `VARCHAR`
2. **时间排序**：字符串排序可能与时间排序不一致
3. **时间计算**：无法直接使用数据库的时间函数
4. **格式不统一**：导入的数据可能有多种时间格式

---

## 数据库迁移

### 需要修改的字段类型

#### req_list 表
```sql
-- 修改前
ALTER TABLE req_list
  ALTER COLUMN create_time TYPE TIMESTAMP,
  ALTER COLUMN submit_time TYPE TIMESTAMP,
  -- ... 其他时间字段

-- 修改后
ALTER TABLE req_list
  ALTER COLUMN create_time TYPE VARCHAR(50),
  ALTER COLUMN submit_time TYPE VARCHAR(50),
  ALTER COLUMN last_submit_time TYPE VARCHAR(50),
  ALTER COLUMN eval_time TYPE VARCHAR(50),
  ALTER COLUMN last_eval_time TYPE VARCHAR(50),
  ALTER COLUMN schedule_start_time TYPE VARCHAR(50),
  ALTER COLUMN schedule_end_time TYPE VARCHAR(50),
  ALTER COLUMN expected_complete_time TYPE VARCHAR(50),
  ALTER COLUMN created_at TYPE VARCHAR(50),
  ALTER COLUMN updated_at TYPE VARCHAR(50);
```

#### req_detail 表
```sql
ALTER TABLE req_detail
  ALTER COLUMN create_time TYPE VARCHAR(50),
  ALTER COLUMN submit_time TYPE VARCHAR(50),
  ALTER COLUMN complete_time TYPE VARCHAR(50),
  ALTER COLUMN component_eval_start_time TYPE VARCHAR(50),
  ALTER COLUMN component_eval_end_time TYPE VARCHAR(50),
  ALTER COLUMN rd_schedule_start_time TYPE VARCHAR(50),
  ALTER COLUMN rd_schedule_end_time TYPE VARCHAR(50),
  ALTER COLUMN schedule_start_time TYPE VARCHAR(50),
  ALTER COLUMN schedule_end_time TYPE VARCHAR(50),
  ALTER COLUMN created_at TYPE VARCHAR(50),
  ALTER COLUMN updated_at TYPE VARCHAR(50);
```

---

## 迁移脚本

### 自动迁移脚本
```sql
-- 1. 备份原表
CREATE TABLE req_list_backup AS SELECT * FROM req_list;
CREATE TABLE req_detail_backup AS SELECT * FROM req_detail;

-- 2. 修改字段类型
ALTER TABLE req_list
  ALTER COLUMN create_time TYPE VARCHAR(50),
  ALTER COLUMN submit_time TYPE VARCHAR(50),
  ALTER COLUMN last_submit_time TYPE VARCHAR(50),
  ALTER COLUMN eval_time TYPE VARCHAR(50),
  ALTER COLUMN last_eval_time TYPE VARCHAR(50),
  ALTER COLUMN schedule_start_time TYPE VARCHAR(50),
  ALTER COLUMN schedule_end_time TYPE VARCHAR(50),
  ALTER COLUMN expected_complete_time TYPE VARCHAR(50),
  ALTER COLUMN created_at TYPE VARCHAR(50),
  ALTER COLUMN updated_at TYPE VARCHAR(50);

ALTER TABLE req_detail
  ALTER COLUMN create_time TYPE VARCHAR(50),
  ALTER COLUMN submit_time TYPE VARCHAR(50),
  ALTER COLUMN complete_time TYPE VARCHAR(50),
  ALTER COLUMN component_eval_start_time TYPE VARCHAR(50),
  ALTER COLUMN component_eval_end_time TYPE VARCHAR(50),
  ALTER COLUMN rd_schedule_start_time TYPE VARCHAR(50),
  ALTER COLUMN rd_schedule_end_time TYPE VARCHAR(50),
  ALTER COLUMN schedule_start_time TYPE VARCHAR(50),
  ALTER COLUMN schedule_end_time TYPE VARCHAR(50),
  ALTER COLUMN created_at TYPE VARCHAR(50),
  ALTER COLUMN updated_at TYPE VARCHAR(50);
```

---

## 测试验证

### 测试1：不同格式的时间导入
```
创建 Excel，包含以下格式的时间：
- 2024-01-15 10:30:00
- 2024/01/15 10:30
- 2024-01-15
- 15-Jan-2024

预期：所有格式都原样导入数据库
```

### 测试2：空时间字段
```
创建 Excel，部分行的时间字段为空

预期：空值正常导入，数据库中为 NULL
```

### 测试3：查询验证
```sql
-- 查询导入的数据
SELECT req_no, create_time, submit_time
FROM req_list
LIMIT 10;

-- 验证时间格式是否保持原样
```

---

## 后续建议

### 如果需要时间排序或计算

#### 方案1：使用触发器
```sql
-- 添加格式化的时间字段用于排序
ALTER TABLE req_list
  ADD COLUMN submit_time_sort TIMESTAMP;

-- 使用触发器自动填充
CREATE OR REPLACE FUNCTION update_sort_time()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.submit_time IS NOT NULL THEN
    NEW.submit_time_sort := TO_TIMESTAMP(NEW.submit_time, 'YYYY-MM-DD HH24:MI:SS');
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_sort_time
BEFORE INSERT OR UPDATE ON req_list
FOR EACH ROW
EXECUTE FUNCTION update_sort_time();
```

#### 方案2：应用层处理
```java
// 在需要排序时转换为日期类型
public List<ReqList> listSortedBySubmitTime() {
    List<ReqList> list = baseMapper.selectList(null);
    return list.stream()
        .sorted((a, b) -> {
            if (a.getSubmitTime() == null) return 1;
            if (b.getSubmitTime() == null) return -1;
            return a.getSubmitTime().compareTo(b.getSubmitTime());
        })
        .collect(Collectors.toList());
}
```

#### 方案3：视图
```sql
-- 创建带时间转换的视图
CREATE VIEW req_list_sorted AS
SELECT *,
  TO_TIMESTAMP(submit_time, 'YYYY-MM-DD HH24:MI:SS') AS submit_time_ts
FROM req_list;

-- 按时间排序
SELECT * FROM req_list_sorted ORDER BY submit_time_ts DESC;
```

---

## 修改文件清单

| 文件 | 修改内容 |
|------|---------|
| `ReqList.java` | 所有时间字段改为 String，移除 JsonFormat |
| `ReqDetail.java` | 所有时间字段改为 String，移除 JsonFormat |
| `ExcelImportUtil.java` | 删除 getCellDateTimeValue 方法，时间字段使用 getCellStringValue |
| `schema.sql` | 需要手动修改数据库字段类型 |

---

## 总结

✅ **已完成**：
- 实体类时间字段改为 String
- Excel 解析直接读取为字符串
- 删除日期转换相关代码

⚠️ **需要手动执行**：
- 运行数据库迁移脚本修改字段类型
- 验证现有数据的兼容性

📌 **优势**：
- 避免日期解析错误
- 保留原始格式
- 简化代码逻辑

---

**修改完成日期**：2024-01-15
**影响范围**：所有时间字段
**数据库迁移**：必需
