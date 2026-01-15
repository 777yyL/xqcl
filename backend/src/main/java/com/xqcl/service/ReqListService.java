package com.xqcl.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqcl.dto.QueryReqDTO;
import com.xqcl.entity.ReqDetail;
import com.xqcl.entity.ReqList;
import com.xqcl.mapper.ReqListMapper;
import com.xqcl.util.ExcelImportUtil;
import com.xqcl.util.MarkdownExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 需求列表 Service（优化版 - 支持批量处理）
 *
 * @author xqcl
 * @since 2024-01-15
 */
@Slf4j
@Service
public class ReqListService extends ServiceImpl<ReqListMapper, ReqList> {

    @Autowired
    private ReqDetailService reqDetailService;

    @Value("${app.markdown-export-dir:./export/markdown}")
    private String markdownExportDir;

    /**
     * 分页查询需求列表
     */
    public Page<ReqList> pageQuery(QueryReqDTO dto) {
        Page<ReqList> page = new Page<>(dto.getCurrent(), dto.getSize());
        LambdaQueryWrapper<ReqList> wrapper = new LambdaQueryWrapper<>();

        // 构建查询条件
        wrapper.like(StrUtil.isNotBlank(dto.getReqNo()), ReqList::getReqNo, dto.getReqNo())
                .like(StrUtil.isNotBlank(dto.getProjectName()), ReqList::getProjectName, dto.getProjectName())
                .like(StrUtil.isNotBlank(dto.getOpportunityNo()), ReqList::getOpportunityNo, dto.getOpportunityNo())
                .like(StrUtil.isNotBlank(dto.getIndustry()), ReqList::getIndustry, dto.getIndustry())
                .like(StrUtil.isNotBlank(dto.getRegion()), ReqList::getRegion, dto.getRegion())
                .like(StrUtil.isNotBlank(dto.getProductSeries()), ReqList::getProductSeries, dto.getProductSeries())
                .like(StrUtil.isNotBlank(dto.getSoftwareVersion()), ReqList::getSoftwareVersion, dto.getSoftwareVersion())
                .eq(StrUtil.isNotBlank(dto.getStatus()), ReqList::getStatus, dto.getStatus())
                .like(StrUtil.isNotBlank(dto.getReqOwner()), ReqList::getReqOwner, dto.getReqOwner())
                .like(StrUtil.isNotBlank(dto.getOwnerDept()), ReqList::getOwnerDept, dto.getOwnerDept())
                .like(StrUtil.isNotBlank(dto.getDevNo()), ReqList::getDevNo, dto.getDevNo())
                .like(StrUtil.isNotBlank(dto.getJknNo()), ReqList::getJknNo, dto.getJknNo())
                .orderByDesc(ReqList::getSubmitTime);

        return baseMapper.selectPage(page, wrapper);
    }

    /**
     * 根据需求评估单号查询需求列表
     */
    public ReqList getByReqNo(String reqNo) {
        return baseMapper.selectById(reqNo);
    }

    /**
     * 导入需求列表 Excel（优化版 - 批量处理）
     */
    @Transactional(rollbackFor = Exception.class)
    public int importExcel(MultipartFile file) throws Exception {
        // 使用批量处理方式解析 Excel
        final int[] totalCount = {0};

        ExcelImportUtil.parseReqListExcelBatch(file, batch -> {
            // 批量处理数据
            if (CollUtil.isEmpty(batch)) {
                return;
            }

            // 1. 查询已存在的需求评估单号
            List<String> reqNos = batch.stream()
                    .map(ReqList::getReqNo)
                    .distinct()
                    .collect(Collectors.toList());

            List<ReqList> existReqs = baseMapper.selectBatchIds(reqNos);

            // 2. 构建已存在数据的映射
            List<String> existReqNos = existReqs.stream()
                    .map(ReqList::getReqNo)
                    .collect(Collectors.toList());

            // 3. 分类需要新增和更新的数据
            List<ReqList> insertList = new ArrayList<>();
            List<ReqList> updateList = new ArrayList<>();

            for (ReqList reqList : batch) {
                if (existReqNos.contains(reqList.getReqNo())) {
                    // 需要更新
                    updateList.add(reqList);
                } else {
                    // 需要新增
                    insertList.add(reqList);
                }
            }

            // 4. 批量插入
            if (!insertList.isEmpty()) {
                saveBatch(insertList, 500);
                log.info("批量插入需求列表: {} 条", insertList.size());
            }

            // 5. 批量更新
            if (!updateList.isEmpty()) {
                updateBatchById(updateList, 500);
                log.info("批量更新需求列表: {} 条", updateList.size());
            }

            totalCount[0] += batch.size();
        });

        log.info("导入需求列表完成，共处理 {} 条", totalCount[0]);
        return totalCount[0];
    }

    /**
     * 导出单个需求的 Markdown 内容
     */
    public String generateMarkdownContent(String reqNo) {
        // 查询需求列表
        ReqList reqList = baseMapper.selectById(reqNo);
        if (reqList == null) {
            throw new RuntimeException("需求评估单号不存在: " + reqNo);
        }

        // 查询需求详情
        List<ReqDetail> reqDetails = reqDetailService.listByReqNo(reqNo);

        // 生成 Markdown 内容
        return MarkdownExportUtil.generateMarkdown(reqList, reqDetails);
    }

    /**
     * 导出单个需求的 Markdown 文件（返回文件路径）
     */
    public String exportMarkdown(String reqNo) {
        // 查询需求列表
        ReqList reqList = baseMapper.selectById(reqNo);
        if (reqList == null) {
            throw new RuntimeException("需求评估单号不存在: " + reqNo);
        }

        // 查询需求详情
        List<ReqDetail> reqDetails = reqDetailService.listByReqNo(reqNo);

        // 生成 Markdown 内容
        String content = MarkdownExportUtil.generateMarkdown(reqList, reqDetails);

        // 保存文件
        String fileName = "需求_" + reqNo;
        return MarkdownExportUtil.saveMarkdownFile(content, markdownExportDir, fileName);
    }

    /**
     * 批量导出 Markdown 文件
     */
    public List<String> batchExportMarkdown(List<String> reqNos) {
        List<String> filePaths = new ArrayList<>();

        // 如果需求评估单号列表为空，则导出所有需求
        if (CollUtil.isEmpty(reqNos)) {
            List<ReqList> allReqLists = baseMapper.selectList(null);
            reqNos = allReqLists.stream()
                    .map(ReqList::getReqNo)
                    .collect(Collectors.toList());
        }

        // 导出每个需求
        for (String reqNo : reqNos) {
            try {
                String filePath = exportMarkdown(reqNo);
                filePaths.add(filePath);
            } catch (Exception e) {
                log.error("导出需求失败: {}", reqNo, e);
            }
        }

        return filePaths;
    }
}
