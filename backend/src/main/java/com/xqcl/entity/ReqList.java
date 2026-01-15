package com.xqcl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 需求列表实体类
 *
 * @author xqcl
 * @since 2024-01-15
 */
@Data
@TableName("req_list")
@Schema(description = "需求列表")
public class ReqList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "需求评估单号")
    @TableId(value = "req_no", type = IdType.INPUT)
    private String reqNo;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "商机编号")
    private String opportunityNo;

    @Schema(description = "行业")
    private String industry;

    @Schema(description = "子行业")
    private String subIndustry;

    @Schema(description = "区域")
    private String region;

    @Schema(description = "国家或地区")
    private String country;

    @Schema(description = "产品线")
    private String productLine;

    @Schema(description = "产品系列")
    private String productSeries;

    @Schema(description = "产品型号")
    private String productModel;

    @Schema(description = "软件名称")
    private String softwareName;

    @Schema(description = "软件版本")
    private String softwareVersion;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "是否排期")
    private String isScheduled;

    @Schema(description = "是否复用方案")
    private String isReuse;

    @Schema(description = "紧急程度")
    private String urgency;

    @Schema(description = "评估类型")
    private String evalType;

    @Schema(description = "评估单创建人")
    private String creator;

    @Schema(description = "创建人部门")
    private String creatorDept;

    @Schema(description = "需求负责人")
    private String reqOwner;

    @Schema(description = "负责人所属部门")
    private String ownerDept;

    @Schema(description = "当前处理人")
    private String currentHandler;

    @Schema(description = "评估单创建时间")
    private String createTime;

    @Schema(description = "评估单提交时间")
    private String submitTime;

    @Schema(description = "最后提交时间")
    private String lastSubmitTime;

    @Schema(description = "评估时间")
    private String evalTime;

    @Schema(description = "最后评估时间")
    private String lastEvalTime;

    @Schema(description = "总评估用时（小时）")
    private String totalEvalHours;

    @Schema(description = "评估停留时间（天）")
    private String evalStayDays;

    @Schema(description = "排期开始时间")
    private String scheduleStartTime;

    @Schema(description = "排期结束时间")
    private String scheduleEndTime;

    @Schema(description = "总工作量（人天）")
    private String totalWorkload;

    @Schema(description = "开发资源分布总部工作量(人天)")
    private String devHqWorkload;

    @Schema(description = "开发资源分布区域")
    private String devRegion;

    @Schema(description = "开发资源分布区域工作量(人天)")
    private String devRegionWorkload;

    @Schema(description = "总订单核算工作量（人天）")
    private String totalOrderWorkload;

    @Schema(description = "订单核算总部工作量(人天)")
    private String orderHqWorkload;

    @Schema(description = "订单核算区域")
    private String orderRegion;

    @Schema(description = "订单核算区域工作量(人天)")
    private String orderRegionWorkload;

    @Schema(description = "系统测试工作量（人天）")
    private String systemTestWorkload;

    @Schema(description = "集成测试工作量（人天）")
    private String integrationTestWorkload;

    @Schema(description = "学习成本工作量（人天）")
    private String learningCostWorkload;

    @Schema(description = "流程管理工作量（人天）")
    private String processManageWorkload;

    @Schema(description = "其他工作量详情")
    private String otherWorkloadDetail;

    @Schema(description = "期望完成时间")
    private String expectedCompleteTime;

    @Schema(description = "定制单号")
    private String customNo;

    @Schema(description = "JKN单号")
    private String jknNo;

    @Schema(description = "开发单号")
    private String devNo;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private String createdAt;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedAt;

    @Schema(description = "备注")
    private String remark;
}
