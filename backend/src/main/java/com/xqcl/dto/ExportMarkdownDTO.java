package com.xqcl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 导出 Markdown DTO
 *
 * @author xqcl
 * @since 2024-01-15
 */
@Data
@Schema(description = "导出 Markdown 参数")
public class ExportMarkdownDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "需求评估单号列表，为空则导出所有")
    private List<String> reqNos;
}
