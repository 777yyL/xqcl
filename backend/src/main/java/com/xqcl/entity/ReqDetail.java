package com.xqcl.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 需求详情实体类
 *
 * @author xqcl
 * @since 2024-01-15
 */
@Data
@TableName("req_detail")
@Schema(description = "需求详情")
public class ReqDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "需求评估单号")
    private String reqNo;

    @Schema(description = "需求名称")
    private String reqName;

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

    @Schema(description = "需求场景")
    private String reqScene;

    @Schema(description = "需求描述")
    private String reqDesc;

    @Schema(description = "研发评估")
    private String rdEval;

    @Schema(description = "组件标识")
    private String componentId;

    @Schema(description = "组件版本")
    private String componentVersion;

    @Schema(description = "需求分类")
    private String reqCategory;

    @Schema(description = "需求标签")
    private String reqTag;

    @Schema(description = "是否复用方案")
    private String isReuse;

    @Schema(description = "评估单创建人")
    private String creator;

    @Schema(description = "需求负责人")
    private String reqOwner;

    @Schema(description = "负责人所属部门")
    private String ownerDept;

    @Schema(description = "评估人")
    private String evaluator;

    @Schema(description = "评估人所属部门")
    private String evaluatorDept;

    @Schema(description = "评估单创建时间")
    private String createTime;

    @Schema(description = "评估单提交时间")
    private String submitTime;

    @Schema(description = "评估单完成时间")
    private String completeTime;

    @Schema(description = "组件评估开始时间")
    private String componentEvalStartTime;

    @Schema(description = "组件评估完成时间")
    private String componentEvalEndTime;

    @Schema(description = "组件评估周期")
    private String componentEvalCycle;

    @Schema(description = "评估用时(h)")
    private String evalHours;

    @Schema(description = "停留时间")
    private String stayTime;

    @Schema(description = "评估工作量")
    private String evalWorkload;

    @Schema(description = "工作量详情")
    private String workloadDetail;

    @Schema(description = "排期开始时间（研发评估）")
    private String rdScheduleStartTime;

    @Schema(description = "排期结束时间（研发评估）")
    private String rdScheduleEndTime;

    @Schema(description = "排期开始时间")
    private String scheduleStartTime;

    @Schema(description = "排期结束时间")
    private String scheduleEndTime;

    @Schema(description = "定制单号")
    private String customNo;

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
