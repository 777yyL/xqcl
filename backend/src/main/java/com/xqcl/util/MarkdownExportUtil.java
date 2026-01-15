package com.xqcl.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.xqcl.entity.ReqDetail;
import com.xqcl.entity.ReqList;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Markdown 导出工具类
 *
 * @author xqcl
 * @since 2024-01-15
 */
@Slf4j
public class MarkdownExportUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 生成单个需求的 Markdown 内容
     *
     * @param reqList  需求列表
     * @param reqDetails 需求详情列表
     * @return Markdown 内容
     */
    public static String generateMarkdown(ReqList reqList, List<ReqDetail> reqDetails) {
        StringBuilder md = new StringBuilder();

        // 标题
        md.append("# 需求文档\n\n");

        // 需求列表信息
        md.append("## 一、需求列表信息\n\n");
        md.append("| 字段 | 内容 |\n");
        md.append("|------|------|\n");
        appendTableRow(md, "需求评估单号", reqList.getReqNo());
        appendTableRow(md, "项目名称", reqList.getProjectName());
        appendTableRow(md, "商机编号", reqList.getOpportunityNo());
        appendTableRow(md, "行业", reqList.getIndustry());
        appendTableRow(md, "区域", reqList.getRegion());
        appendTableRow(md, "产品系列", reqList.getProductSeries());
        appendTableRow(md, "软件版本", reqList.getSoftwareVersion());
        appendTableRow(md, "状态", reqList.getStatus());
        appendTableRow(md, "需求负责人", reqList.getReqOwner());
        appendTableRow(md, "负责人所属部门", reqList.getOwnerDept());
        appendTableRow(md, "开发单号", reqList.getDevNo());
        appendTableRow(md, "JKN单号", reqList.getJknNo());
        appendTableRow(md, "评估单提交时间", formatDate(reqList.getSubmitTime()));
        appendTableRow(md, "总工作量（人天）", formatBigDecimal(reqList.getTotalWorkload()));
        appendTableRow(md, "开发资源分布总部工作量(人天)", formatBigDecimal(reqList.getDevHqWorkload()));
        appendTableRow(md, "开发资源分布区域工作量(人天)", formatBigDecimal(reqList.getDevRegionWorkload()));
        appendTableRow(md, "总订单核算工作量（人天）", formatBigDecimal(reqList.getTotalOrderWorkload()));
        appendTableRow(md, "订单核算总部工作量(人天)", formatBigDecimal(reqList.getOrderHqWorkload()));
        appendTableRow(md, "订单核算区域工作量(人天)", formatBigDecimal(reqList.getOrderRegionWorkload()));
        appendTableRow(md, "集成测试工作量（人天）", formatBigDecimal(reqList.getIntegrationTestWorkload()));
        appendTableRow(md, "其他工作量详情", reqList.getOtherWorkloadDetail());
        md.append("\n");

        // 需求详情信息
        if (reqDetails != null && !reqDetails.isEmpty()) {
            md.append("## 二、需求详情信息\n\n");
            for (int i = 0; i < reqDetails.size(); i++) {
                ReqDetail detail = reqDetails.get(i);
                md.append("### ").append(i + 1).append(". ").append(detail.getReqName()).append("\n\n");

                md.append("| 字段 | 内容 |\n");
                md.append("|------|------|\n");
                appendTableRow(md, "需求名称", detail.getReqName());
                appendTableRow(md, "需求场景", detail.getReqScene());
                appendTableRow(md, "需求描述", detail.getReqDesc());
                appendTableRow(md, "研发评估", detail.getRdEval());
                appendTableRow(md, "组件标识", detail.getComponentId());
                appendTableRow(md, "组件版本", detail.getComponentVersion());
                appendTableRow(md, "需求分类", detail.getReqCategory());
                appendTableRow(md, "需求标签", detail.getReqTag());
                appendTableRow(md, "是否复用方案", detail.getIsReuse());
                appendTableRow(md, "评估人", detail.getEvaluator());
                appendTableRow(md, "评估人所属部门", detail.getEvaluatorDept());
                appendTableRow(md, "评估工作量", formatBigDecimal(detail.getEvalWorkload()));
                appendTableRow(md, "工作量详情", detail.getWorkloadDetail());
                md.append("\n");
            }
        }

        return md.toString();
    }

    /**
     * 追加表格行
     */
    private static void appendTableRow(StringBuilder md, String key, String value) {
        md.append("| ").append(key).append(" | ").append(StrUtil.nullToEmpty(value)).append(" |\n");
    }

    /**
     * 格式化日期
     */
    private static String formatDate(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_FORMATTER);
    }

    /**
     * 格式化 BigDecimal
     */
    private static String formatBigDecimal(java.math.BigDecimal value) {
        if (value == null) {
            return "";
        }
        return value.toPlainString();
    }

    /**
     * 保存 Markdown 文件
     *
     * @param content   Markdown 内容
     * @param exportDir 导出目录
     * @param fileName  文件名（不含扩展名）
     * @return 文件路径
     */
    public static String saveMarkdownFile(String content, String exportDir, String fileName) {
        // 确保导出目录存在
        FileUtil.mkdir(exportDir);

        // 构建文件路径
        String filePath = exportDir + "/" + fileName + ".md";

        // 保存文件
        FileUtil.writeUtf8String(content, filePath);

        log.info("Markdown 文件已保存: {}", filePath);
        return filePath;
    }
}
