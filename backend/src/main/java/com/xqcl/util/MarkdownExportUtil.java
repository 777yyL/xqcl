package com.xqcl.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.xqcl.entity.ReqDetail;
import com.xqcl.entity.ReqList;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Markdown 导出工具类（重构版 - 字段：值格式）
 *
 * @author xqcl
 * @since 2024-01-15
 */
@Slf4j
public class MarkdownExportUtil {

    /**
     * 生成单个需求的 Markdown 内容
     *
     * @param reqList  需求列表
     * @param reqDetails 需求详情列表
     * @return Markdown 内容
     */
    public static String generateMarkdown(ReqList reqList, List<ReqDetail> reqDetails) {
        StringBuilder md = new StringBuilder();

        // 一级标题：需求单号 + 项目名称
        String title = StrUtil.isNotBlank(reqList.getProjectName())
            ? reqList.getReqNo() + " " + reqList.getProjectName()
            : reqList.getReqNo();
        md.append("# ").append(title).append("\n\n");

        // 需求列表信息（字段：值格式）
        md.append("## 一、需求列表信息\n\n");
        appendField(md, "需求评估单号", reqList.getReqNo());
        appendField(md, "项目名称", reqList.getProjectName());
        appendField(md, "商机编号", reqList.getOpportunityNo());
        appendField(md, "行业", reqList.getIndustry());
        appendField(md, "子行业", reqList.getSubIndustry());
        appendField(md, "区域", reqList.getRegion());
        appendField(md, "国家或地区", reqList.getCountry());
        appendField(md, "产品线", reqList.getProductLine());
        appendField(md, "产品系列", reqList.getProductSeries());
        appendField(md, "产品型号", reqList.getProductModel());
        appendField(md, "软件名称", reqList.getSoftwareName());
        appendField(md, "软件版本", reqList.getSoftwareVersion());
        appendField(md, "状态", reqList.getStatus());
        appendField(md, "是否排期", reqList.getIsScheduled());
        appendField(md, "是否复用方案", reqList.getIsReuse());
        appendField(md, "紧急程度", reqList.getUrgency());
        appendField(md, "评估类型", reqList.getEvalType());
        appendField(md, "评估单创建人", reqList.getCreator());
        appendField(md, "创建人部门", reqList.getCreatorDept());
        appendField(md, "需求负责人", reqList.getReqOwner());
        appendField(md, "负责人所属部门", reqList.getOwnerDept());
        appendField(md, "当前处理人", reqList.getCurrentHandler());
        appendField(md, "评估单创建时间", reqList.getCreateTime());
        appendField(md, "评估单提交时间", reqList.getSubmitTime());
        appendField(md, "最后提交时间", reqList.getLastSubmitTime());
        appendField(md, "评估时间", reqList.getEvalTime());
        appendField(md, "最后评估时间", reqList.getLastEvalTime());
        appendField(md, "总评估用时（小时）", reqList.getTotalEvalHours());
        appendField(md, "评估停留时间（天）", reqList.getEvalStayDays());
        appendField(md, "排期开始时间", reqList.getScheduleStartTime());
        appendField(md, "排期结束时间", reqList.getScheduleEndTime());
        appendField(md, "总工作量（人天）", reqList.getTotalWorkload());
        appendField(md, "开发资源分布总部工作量(人天)", reqList.getDevHqWorkload());
        appendField(md, "开发资源分布区域", reqList.getDevRegion());
        appendField(md, "开发资源分布区域工作量(人天)", reqList.getDevRegionWorkload());
        appendField(md, "总订单核算工作量（人天）", reqList.getTotalOrderWorkload());
        appendField(md, "订单核算总部工作量(人天)", reqList.getOrderHqWorkload());
        appendField(md, "订单核算区域", reqList.getOrderRegion());
        appendField(md, "订单核算区域工作量(人天)", reqList.getOrderRegionWorkload());
        appendField(md, "系统测试工作量（人天）", reqList.getSystemTestWorkload());
        appendField(md, "集成测试工作量（人天）", reqList.getIntegrationTestWorkload());
        appendField(md, "学习成本工作量（人天）", reqList.getLearningCostWorkload());
        appendField(md, "流程管理工作量（人天）", reqList.getProcessManageWorkload());
        appendField(md, "其他工作量详情", reqList.getOtherWorkloadDetail());
        appendField(md, "期望完成时间", reqList.getExpectedCompleteTime());
        appendField(md, "定制单号", reqList.getCustomNo());
        appendField(md, "JKN单号", reqList.getJknNo());
        appendField(md, "开发单号", reqList.getDevNo());

        // 需求详情信息
        if (reqDetails != null && !reqDetails.isEmpty()) {
            md.append("## 二、需求详情信息\n\n");

            for (int i = 0; i < reqDetails.size(); i++) {
                ReqDetail detail = reqDetails.get(i);

                // 每个需求详情作为一个子章节
                md.append("### ").append(i + 1).append(". ").append(detail.getReqName()).append("\n\n");

                appendField(md, "需求评估单号", detail.getReqNo());
                appendField(md, "项目名称", detail.getProjectName());
                appendField(md, "商机编号", detail.getOpportunityNo());
                appendField(md, "行业", detail.getIndustry());
                appendField(md, "子行业", detail.getSubIndustry());
                appendField(md, "区域", detail.getRegion());
                appendField(md, "产品线", detail.getProductLine());
                appendField(md, "产品系列", detail.getProductSeries());
                appendField(md, "产品型号", detail.getProductModel());
                appendField(md, "软件名称", detail.getSoftwareName());
                appendField(md, "软件版本", detail.getSoftwareVersion());
                appendField(md, "状态", detail.getStatus());
                appendField(md, "需求名称", detail.getReqName());
                appendField(md, "需求场景", detail.getReqScene());
                appendField(md, "需求描述", detail.getReqDesc());
                appendField(md, "研发评估", detail.getRdEval());
                appendField(md, "组件标识", detail.getComponentId());
                appendField(md, "组件版本", detail.getComponentVersion());
                appendField(md, "需求分类", detail.getReqCategory());
                appendField(md, "需求标签", detail.getReqTag());
                appendField(md, "是否复用方案", detail.getIsReuse());
                appendField(md, "评估单创建人", detail.getCreator());
                appendField(md, "需求负责人", detail.getReqOwner());
                appendField(md, "负责人所属部门", detail.getOwnerDept());
                appendField(md, "评估人", detail.getEvaluator());
                appendField(md, "评估人所属部门", detail.getEvaluatorDept());
                appendField(md, "评估单创建时间", detail.getCreateTime());
                appendField(md, "评估单提交时间", detail.getSubmitTime());
                appendField(md, "评估单完成时间", detail.getCompleteTime());
                appendField(md, "组件评估开始时间", detail.getComponentEvalStartTime());
                appendField(md, "组件评估完成时间", detail.getComponentEvalEndTime());
                appendField(md, "组件评估周期", detail.getComponentEvalCycle());
                appendField(md, "评估用时(h)", detail.getEvalHours());
                appendField(md, "停留时间", detail.getStayTime());
                appendField(md, "评估工作量", detail.getEvalWorkload());
                appendField(md, "工作量详情", detail.getWorkloadDetail());
                appendField(md, "排期开始时间（研发评估）", detail.getRdScheduleStartTime());
                appendField(md, "排期结束时间（研发评估）", detail.getRdScheduleEndTime());
                appendField(md, "排期开始时间", detail.getScheduleStartTime());
                appendField(md, "排期结束时间", detail.getScheduleEndTime());
                appendField(md, "定制单号", detail.getCustomNo());
                appendField(md, "开发单号", detail.getDevNo());

                // 添加空行分隔
                if (i < reqDetails.size() - 1) {
                    md.append("\n");
                }
            }
        }

        return md.toString();
    }

    /**
     * 追加字段（格式：**字段名**：字段值）
     */
    private static void appendField(StringBuilder md, String fieldName, String value) {
        md.append("**").append(fieldName).append("**：").append(StrUtil.nullToEmpty(value)).append("\n\n");
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
