package com.xqcl.util;

import cn.hutool.core.util.StrUtil;
import com.xqcl.entity.ReqDetail;
import com.xqcl.entity.ReqList;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 导入工具类（优化版 - 支持流式读取和批量处理）
 *
 * @author xqcl
 * @since 2024-01-15
 */
@Slf4j
public class ExcelImportUtil {

    /**
     * 每批处理的数据量
     */
    private static final int BATCH_SIZE = 500;

    /**
     * 解析需求列表 Excel 文件（分批处理）
     *
     * @param file      Excel 文件
     * @param processor 批量处理器，每处理完一批数据就回调一次
     * @return 总数据量
     */
    public static int parseReqListExcelBatch(MultipartFile file, BatchProcessor<ReqList> processor) throws Exception {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalArgumentException("Excel 文件为空");
            }

            int totalRows = sheet.getPhysicalNumberOfRows();
            if (totalRows <= 1) {
                log.warn("Excel 文件只有表头，没有数据");
                return 0;
            }

            List<ReqList> batchList = new ArrayList<>(BATCH_SIZE);
            int count = 0;
            int skippedCount = 0; // 记录解析失败的行数

            // 跳过表头，从第二行开始读取
            for (int i = 1; i < totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    log.warn("第 {} 行为空，跳过", i + 1);
                    skippedCount++;
                    continue;
                }

                try {
                    ReqList reqList = parseReqListRow(row);
                    if (reqList != null) {
                        batchList.add(reqList);
                        count++;

                        // 每达到批次大小就处理一次
                        if (batchList.size() >= BATCH_SIZE) {
                            processor.process(batchList);
                            batchList.clear();
                            log.info("已处理需求列表: {}/{}", count, totalRows - 1);
                        }
                    } else {
                        skippedCount++;
                        log.warn("第 {} 行解析失败，跳过", i + 1);
                    }
                } catch (Exception e) {
                    skippedCount++;
                    log.error("第 {} 行解析异常: {}", i + 1, e.getMessage());
                }
            }

            // 处理剩余数据
            if (!batchList.isEmpty()) {
                processor.process(batchList);
                log.info("已处理需求列表: {}/{}", count, totalRows - 1);
            }

            if (skippedCount > 0) {
                log.warn("共跳过 {} 行数据", skippedCount);
            }

            return count;
        }
    }

    /**
     * 解析需求列表 Excel 文件（一次性读取）
     */
    public static List<ReqList> parseReqListExcel(MultipartFile file) throws Exception {
        List<ReqList> list = new ArrayList<>();
        BatchProcessor<ReqList> processor = batch -> list.addAll(batch);
        parseReqListExcelBatch(file, processor);
        return list;
    }

    /**
     * 解析需求详情 Excel 文件（分批处理）
     *
     * @param file      Excel 文件
     * @param processor 批量处理器
     * @return 总数据量
     */
    public static int parseReqDetailExcelBatch(MultipartFile file, BatchProcessor<ReqDetail> processor) throws Exception {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalArgumentException("Excel 文件为空");
            }

            int totalRows = sheet.getPhysicalNumberOfRows();
            if (totalRows <= 1) {
                log.warn("Excel 文件只有表头，没有数据");
                return 0;
            }

            List<ReqDetail> batchList = new ArrayList<>(BATCH_SIZE);
            int count = 0;
            int skippedCount = 0; // 记录解析失败的行数

            // 跳过表头，从第二行开始读取
            for (int i = 1; i < totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    log.warn("第 {} 行为空，跳过", i + 1);
                    skippedCount++;
                    continue;
                }

                try {
                    ReqDetail reqDetail = parseReqDetailRow(row);
                    if (reqDetail != null) {
                        batchList.add(reqDetail);
                        count++;

                        // 每达到批次大小就处理一次
                        if (batchList.size() >= BATCH_SIZE) {
                            processor.process(batchList);
                            batchList.clear();
                            log.info("已处理需求详情: {}/{}", count, totalRows - 1);
                        }
                    } else {
                        skippedCount++;
                        log.warn("第 {} 行解析失败，跳过", i + 1);
                    }
                } catch (Exception e) {
                    skippedCount++;
                    log.error("第 {} 行解析异常: {}", i + 1, e.getMessage());
                }
            }

            // 处理剩余数据
            if (!batchList.isEmpty()) {
                processor.process(batchList);
                log.info("已处理需求详情: {}/{}", count, totalRows - 1);
            }

            if (skippedCount > 0) {
                log.warn("共跳过 {} 行数据", skippedCount);
            }

            return count;
        }
    }

    /**
     * 解析需求详情 Excel 文件（一次性读取）
     */
    public static List<ReqDetail> parseReqDetailExcel(MultipartFile file) throws Exception {
        List<ReqDetail> list = new ArrayList<>();
        BatchProcessor<ReqDetail> processor = batch -> list.addAll(batch);
        parseReqDetailExcelBatch(file, processor);
        return list;
    }

    /**
     * 批量处理器接口
     */
    @FunctionalInterface
    public interface BatchProcessor<T> {
        void process(List<T> batch);
    }

    /**
     * 判断行是否为空
     */
    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * 解析需求列表行数据
     */
    private static ReqList parseReqListRow(Row row) {
        try {
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
            reqList.setCreateTime(getCellStringValue(row, 22));
            reqList.setSubmitTime(getCellStringValue(row, 23));
            reqList.setLastSubmitTime(getCellStringValue(row, 24));
            reqList.setEvalTime(getCellStringValue(row, 25));
            reqList.setLastEvalTime(getCellStringValue(row, 26));
            reqList.setTotalEvalHours(getCellStringValue(row, 27));
            reqList.setEvalStayDays(getCellStringValue(row, 28));
            reqList.setScheduleStartTime(getCellStringValue(row, 29));
            reqList.setScheduleEndTime(getCellStringValue(row, 30));
            reqList.setTotalWorkload(getCellStringValue(row, 31));
            reqList.setDevHqWorkload(getCellStringValue(row, 32));
            reqList.setDevRegion(getCellStringValue(row, 33));
            reqList.setDevRegionWorkload(getCellStringValue(row, 34));
            reqList.setTotalOrderWorkload(getCellStringValue(row, 35));
            reqList.setOrderHqWorkload(getCellStringValue(row, 36));
            reqList.setOrderRegion(getCellStringValue(row, 37));
            reqList.setOrderRegionWorkload(getCellStringValue(row, 38));
            reqList.setSystemTestWorkload(getCellStringValue(row, 39));
            reqList.setIntegrationTestWorkload(getCellStringValue(row, 40));
            reqList.setLearningCostWorkload(getCellStringValue(row, 41));
            reqList.setProcessManageWorkload(getCellStringValue(row, 42));
            reqList.setOtherWorkloadDetail(getCellStringValue(row, 43));
            reqList.setExpectedCompleteTime(getCellStringValue(row, 44));
            reqList.setCustomNo(getCellStringValue(row, 45));
            reqList.setJknNo(getCellStringValue(row, 46));
            reqList.setDevNo(getCellStringValue(row, 47));
            return reqList;
        } catch (Exception e) {
            log.error("解析需求列表行数据失败，行号: {}", row.getRowNum() + 1, e);
            return null;
        }
    }

    /**
     * 解析需求详情行数据
     */
    private static ReqDetail parseReqDetailRow(Row row) {
        try {
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
            reqDetail.setCreateTime(getCellStringValue(row, 26));
            reqDetail.setSubmitTime(getCellStringValue(row, 27));
            reqDetail.setCompleteTime(getCellStringValue(row, 28));
            reqDetail.setComponentEvalStartTime(getCellStringValue(row, 29));
            reqDetail.setComponentEvalEndTime(getCellStringValue(row, 30));
            reqDetail.setComponentEvalCycle(getCellStringValue(row, 31));
            reqDetail.setEvalHours(getCellStringValue(row, 32));
            reqDetail.setStayTime(getCellStringValue(row, 33));
            reqDetail.setEvalWorkload(getCellStringValue(row, 34));
            reqDetail.setWorkloadDetail(getCellStringValue(row, 35));
            reqDetail.setRdScheduleStartTime(getCellStringValue(row, 36));
            reqDetail.setRdScheduleEndTime(getCellStringValue(row, 37));
            reqDetail.setScheduleStartTime(getCellStringValue(row, 38));
            reqDetail.setScheduleEndTime(getCellStringValue(row, 39));
            reqDetail.setCustomNo(getCellStringValue(row, 40));
            reqDetail.setDevNo(getCellStringValue(row, 41));
            return reqDetail;
        } catch (Exception e) {
            log.error("解析需求详情行数据失败，行号: {}", row.getRowNum() + 1, e);
            return null;
        }
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
                // 数字转字符串，去掉小数点后的0
                double numValue = cell.getNumericCellValue();
                if (numValue == (long) numValue) {
                    return String.valueOf((long) numValue);
                } else {
                    return String.valueOf(numValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return null;
        }
    }
}
