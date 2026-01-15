package com.xqcl.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqcl.common.Result;
import com.xqcl.dto.ExportMarkdownDTO;
import com.xqcl.dto.QueryReqDTO;
import com.xqcl.entity.ReqDetail;
import com.xqcl.entity.ReqList;
import com.xqcl.service.ReqDetailService;
import com.xqcl.service.ReqListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 需求管理 Controller
 *
 * @author xqcl
 * @since 2024-01-15
 */
@Slf4j
@RestController
@RequestMapping("/req")
@Tag(name = "需求管理", description = "需求管理相关接口")
public class ReqController {

    @Autowired
    private ReqListService reqListService;

    @Autowired
    private ReqDetailService reqDetailService;

    /**
     * 分页查询需求列表
     */
    @PostMapping("/list/page")
    @Operation(summary = "分页查询需求列表", description = "支持多条件筛选")
    public Result<Page<ReqList>> pageQuery(@RequestBody QueryReqDTO dto) {
        Page<ReqList> page = reqListService.pageQuery(dto);
        return Result.ok(page);
    }

    /**
     * 查询需求详情
     */
    @GetMapping("/detail/{reqNo}")
    @Operation(summary = "查询需求详情", description = "根据需求评估单号查询详情列表")
    public Result<List<ReqDetail>> getDetail(@PathVariable String reqNo) {
        List<ReqDetail> list = reqDetailService.listByReqNo(reqNo);
        return Result.ok(list);
    }

    /**
     * 导入需求列表 Excel
     */
    @PostMapping("/import/list")
    @Operation(summary = "导入需求列表 Excel", description = "上传需求列表 Excel 文件进行导入")
    public Result<Integer> importList(@RequestParam("file") MultipartFile file) {
        try {
            int count = reqListService.importExcel(file);
            return Result.ok(count);
        } catch (Exception e) {
            log.error("导入需求列表失败", e);
            return Result.fail("导入失败: " + e.getMessage());
        }
    }

    /**
     * 导入需求详情 Excel
     */
    @PostMapping("/import/detail")
    @Operation(summary = "导入需求详情 Excel", description = "上传需求详情 Excel 文件进行导入")
    public Result<Integer> importDetail(@RequestParam("file") MultipartFile file) {
        try {
            int count = reqDetailService.importExcel(file);
            return Result.ok(count);
        } catch (Exception e) {
            log.error("导入需求详情失败", e);
            return Result.fail("导入失败: " + e.getMessage());
        }
    }

    /**
     * 导出单个需求的 Markdown 文件
     */
    @GetMapping("/export/markdown/{reqNo}")
    @Operation(summary = "导出单个需求的 Markdown", description = "根据需求评估单号导出 Markdown 文件")
    public Result<String> exportMarkdown(@PathVariable String reqNo) {
        try {
            String filePath = reqListService.exportMarkdown(reqNo);
            return Result.ok(filePath);
        } catch (Exception e) {
            log.error("导出 Markdown 失败", e);
            return Result.fail("导出失败: " + e.getMessage());
        }
    }

    /**
     * 批量导出 Markdown 文件
     */
    @PostMapping("/export/markdown/batch")
    @Operation(summary = "批量导出 Markdown", description = "批量导出需求的 Markdown 文件")
    public Result<List<String>> batchExportMarkdown(@RequestBody ExportMarkdownDTO dto) {
        try {
            List<String> filePaths = reqListService.batchExportMarkdown(dto.getReqNos());
            return Result.ok(filePaths);
        } catch (Exception e) {
            log.error("批量导出 Markdown 失败", e);
            return Result.fail("导出失败: " + e.getMessage());
        }
    }
}
