package com.xqcl.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xqcl.entity.ReqDetail;
import com.xqcl.entity.ReqList;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Excel 导入工具类
 *
 * @author xqcl
 * @since 2024-01-15
 */
@Slf4j
public class ExcelImportUtil {

    /**
     * 需求列表表头（按顺序）
     */
    private static final String[] REQ_LIST_HEADERS = {
            "需求评估单号", "项目名称", "商机编号", "行业", "子行业", "区域", "国家或地区", "产品线", "产品系列",
            "产品型号", "软件名称", "软件版本", "状态", "是否排期", "是否复用方案", "紧急程度", "评估类型",
            "评估单创建人", "创建人部门", "需求负责人", "负责人所属部门", "当前处理人", "评估单创建时间",
            "评估单提交时间", "最后提交时间", "评估时间", "最后评估时间", "总评估用时（小时）", "评估停留时间（天）",
            "排期开始时间", "排期结束时间", "总工作量（人天）", "开发资源分布总部工作量(人天)", "开发资源分布区域",
            "开发资源分布区域工作量(人天)", "总订单核算工作量（人天）", "订单核算总部工作量(人天)", "订单核算区域",
            "订单核算区域工作量(人天)", "系统测试工作量（人天）", "集成测试工作量（人天）", "学习成本工作量（人天）",
            "流程管理工作量（人天）", "其他工作量详情", "期望完成时间", "定制单号", "JKN单号", "开发单号"
    };

    /**
     * 需求详情表头（按顺序）
     */
    private static final String[] REQ_DETAIL_HEADERS = {
            "需求评估单号", "项目名称", "商机编号", "行业", "子行业", "区域", "产品线", "产品系列",
            "产品型号", "软件名称", "软件版本", "状态", "需求名称", "需求场景", "需求描述", "研发评估",
            "组件标识", "组件版本", "需求分类", "需求标签", "是否复用方案", "评估单创建人", "需求负责人",
            "负责人所属部门", "评估人", "评估人所属部门", "评估单创建时间", "评估单提交时间", "评估单完成时间",
            "组件评估开始时间", "组件评估完成时间", "组件评估周期", "评估用时(h)", "评估工作量", "工作量详情",
            "停留时间", "排期开始时间（研发评估）", "排期结束时间（研发评估）", "排期开始时间", "排期结束时间",
            "定制单号", "开发单号"
    };

    /**
     * 解析需求列表 Excel 文件
     *
     * @param file Excel 文件
     * @return 需求列表
     */
    public static List<ReqList> parseReqListExcel(MultipartFile file) throws Exception {
        List<ReqList> list = new ArrayList<>();
        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalArgumentException("Excel 文件为空");
            }

            // 读取表头行，找到列名对应的索引
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("Excel 表头为空");
            }

            // 跳过表头，从第二行开始读取数据
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                ReqList reqList = new ReqList();
                reqList.setReqNo(getCellStringValue(row, 0));
                reqList.setProjectName(getCellStringValue(row, 1));
                reqList.setOpportunityNo(getCellStringValue(row, 2));
                reqList.setIndustry(getCellStringValue(row, 3));
                reqList.setSubIndustry(getCellStringValue(row, 4));
                reqList.setRegion(getCellStringValue(row, 5));
                reqList.setCountry(getCellStringValue(row, 6));
                reqList.setProductLine(getCellStringValue(row, 7));
                reqList.setProductSeries(getCellStringValue(row, 8));
                reqList.setProductModel(getCellStringValue(row, 9));
                reqList.setSoftwareName(getCellStringValue(row, 10));
                reqList.setSoftwareVersion(getCellStringValue(row, 11));
                reqList.setStatus(getCellStringValue(row, 12));
                reqList.setIsScheduled(getCellStringValue(row, 13));
                reqList.setIsReuse(getCellStringValue(row, 14));
                reqList.setUrgency(getCellStringValue(row, 15));
                reqList.setEvalType(getCellStringValue(row, 16));
                reqList.setCreator(getCellStringValue(row, 17));
                reqList.setCreatorDept(getCellStringValue(row, 18));
                reqList.setReqOwner(getCellStringValue(row, 19));
                reqList.setOwnerDept(getCellStringValue(row, 20));
                reqList.setCurrentHandler(getCellStringValue(row, 21));
                reqList.setCreateTime(getCellDateTimeValue(row, 22));
                reqList.setSubmitTime(getCellDateTimeValue(row, 23));
                reqList.setLastSubmitTime(getCellDateTimeValue(row, 24));
                reqList.setEvalTime(getCellDateTimeValue(row, 25));
                reqList.setLastEvalTime(getCellDateTimeValue(row, 26));
                reqList.setTotalEvalHours(getCellBigDecimalValue(row, 27));
                reqList.setEvalStayDays(getCellBigDecimalValue(row, 28));
                reqList.setScheduleStartTime(getCellDateTimeValue(row, 29));
                reqList.setScheduleEndTime(getCellDateTimeValue(row, 30));
                reqList.setTotalWorkload(getCellBigDecimalValue(row, 31));
                reqList.setDevHqWorkload(getCellBigDecimalValue(row, 32));
                reqList.setDevRegion(getCellStringValue(row, 33));
                reqList.setDevRegionWorkload(getCellBigDecimalValue(row, 34));
                reqList.setTotalOrderWorkload(getCellBigDecimalValue(row, 35));
                reqList.setOrderHqWorkload(getCellBigDecimalValue(row, 36));
                reqList.setOrderRegion(getCellStringValue(row, 37));
                reqList.setOrderRegionWorkload(getCellBigDecimalValue(row, 38));
                reqList.setSystemTestWorkload(getCellBigDecimalValue(row, 39));
                reqList.setIntegrationTestWorkload(getCellBigDecimalValue(row, 40));
                reqList.setLearningCostWorkload(getCellBigDecimalValue(row, 41));
                reqList.setProcessManageWorkload(getCellBigDecimalValue(row, 42));
                reqList.setOtherWorkloadDetail(getCellStringValue(row, 43));
                reqList.setExpectedCompleteTime(getCellDateTimeValue(row, 44));
                reqList.setCustomNo(getCellStringValue(row, 45));
                reqList.setJknNo(getCellStringValue(row, 46));
                reqList.setDevNo(getCellStringValue(row, 47));

                // 需求评估单号不能为空
                if (StrUtil.isBlank(reqList.getReqNo())) {
                    log.warn("第 {} 行需求评估单号为空，跳过", i + 1);
                    continue;
                }

                list.add(reqList);
            }
        }
        return list;
    }

    /**
     * 解析需求详情 Excel 文件
     *
     * @param file Excel 文件
     * @return 需求详情列表
     */
    public static List<ReqDetail> parseReqDetailExcel(MultipartFile file) throws Exception {
        List<ReqDetail> list = new ArrayList<>();
        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalArgumentException("Excel 文件为空");
            }

            // 读取表头行
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("Excel 表头为空");
            }

            // 跳过表头，从第二行开始读取数据
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                ReqDetail reqDetail = new ReqDetail();
                reqDetail.setReqNo(getCellStringValue(row, 0));
                reqDetail.setProjectName(getCellStringValue(row, 1));
                reqDetail.setOpportunityNo(getCellStringValue(row, 2));
                reqDetail.setIndustry(getCellStringValue(row, 3));
                reqDetail.setSubIndustry(getCellStringValue(row, 4));
                reqDetail.setRegion(getCellStringValue(row, 5));
                reqDetail.setProductLine(getCellStringValue(row, 6));
                reqDetail.setProductSeries(getCellStringValue(row, 7));
                reqDetail.setProductModel(getCellStringValue(row, 8));
                reqDetail.setSoftwareName(getCellStringValue(row, 9));
                reqDetail.setSoftwareVersion(getCellStringValue(row, 10));
                reqDetail.setStatus(getCellStringValue(row, 11));
                reqDetail.setReqName(getCellStringValue(row, 12));
                reqDetail.setReqScene(getCellStringValue(row, 13));
                reqDetail.setReqDesc(getCellStringValue(row, 14));
                reqDetail.setRdEval(getCellStringValue(row, 15));
                reqDetail.setComponentId(getCellStringValue(row, 16));
                reqDetail.setComponentVersion(getCellStringValue(row, 17));
                reqDetail.setReqCategory(getCellStringValue(row, 18));
                reqDetail.setReqTag(getCellStringValue(row, 19));
                reqDetail.setIsReuse(getCellStringValue(row, 20));
                reqDetail.setCreator(getCellStringValue(row, 21));
                reqDetail.setReqOwner(getCellStringValue(row, 22));
                reqDetail.setOwnerDept(getCellStringValue(row, 23));
                reqDetail.setEvaluator(getCellStringValue(row, 24));
                reqDetail.setEvaluatorDept(getCellStringValue(row, 25));
                reqDetail.setCreateTime(getCellDateTimeValue(row, 26));
                reqDetail.setSubmitTime(getCellDateTimeValue(row, 27));
                reqDetail.setCompleteTime(getCellDateTimeValue(row, 28));
                reqDetail.setComponentEvalStartTime(getCellDateTimeValue(row, 29));
                reqDetail.setComponentEvalEndTime(getCellDateTimeValue(row, 30));
                reqDetail.setComponentEvalCycle(getCellStringValue(row, 31));
                reqDetail.setEvalHours(getCellBigDecimalValue(row, 32));
                reqDetail.setStayTime(getCellBigDecimalValue(row, 33));
                reqDetail.setEvalWorkload(getCellBigDecimalValue(row, 34));
                reqDetail.setWorkloadDetail(getCellStringValue(row, 35));
                reqDetail.setRdScheduleStartTime(getCellDateTimeValue(row, 36));
                reqDetail.setRdScheduleEndTime(getCellDateTimeValue(row, 37));
                reqDetail.setScheduleStartTime(getCellDateTimeValue(row, 38));
                reqDetail.setScheduleEndTime(getCellDateTimeValue(row, 39));
                reqDetail.setCustomNo(getCellStringValue(row, 40));
                reqDetail.setDevNo(getCellStringValue(row, 41));

                // 需求评估单号和需求名称不能为空
                if (StrUtil.isBlank(reqDetail.getReqNo()) || StrUtil.isBlank(reqDetail.getReqName())) {
                    log.warn("第 {} 行需求评估单号或需求名称为空，跳过", i + 1);
                    continue;
                }

                list.add(reqDetail);
            }
        }
        return list;
    }

    /**
     * 获取单元格字符串值
     */
    private static String getCellStringValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return null;
        }
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    /**
     * 获取单元格日期时间值
     */
    private static LocalDateTime getCellDateTimeValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return null;
        }
        CellType cellType = cell.getCellType();
        if (cellType == CellType.NUMERIC) {
            Date date = cell.getDateCellValue();
            if (date != null) {
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
        }
        return null;
    }

    /**
     * 获取单元格 BigDecimal 值
     */
    private static BigDecimal getCellBigDecimalValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return null;
        }
        CellType cellType = cell.getCellType();
        if (cellType == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        }
        return null;
    }
}
