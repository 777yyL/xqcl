package com.xqcl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 需求列表查询条件 DTO
 *
 * @author xqcl
 * @since 2024-01-15
 */
@Data
@Schema(description = "需求列表查询条件")
public class QueryReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "需求评估单号")
    private String reqNo;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "商机编号")
    private String opportunityNo;

    @Schema(description = "行业")
    private String industry;

    @Schema(description = "区域")
    private String region;

    @Schema(description = "产品系列")
    private String productSeries;

    @Schema(description = "软件版本")
    private String softwareVersion;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "需求负责人")
    private String reqOwner;

    @Schema(description = "负责人所属部门")
    private String ownerDept;

    @Schema(description = "开发单号")
    private String devNo;

    @Schema(description = "JKN单号")
    private String jknNo;

    @Schema(description = "当前页码", example = "1")
    private Integer current = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer size = 10;
}
