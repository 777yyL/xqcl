package com.xqcl.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqcl.dto.QueryReqDTO;
import com.xqcl.entity.ReqDetail;
import com.xqcl.entity.ReqList;
import com.xqcl.mapper.ReqListMapper;
import com.xqcl.util.MarkdownExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 需求列表 Service
 *
 * @author xqcl
 * @since 2024-01-15
 */
@Slf4j
@Service
public class ReqListService {

    @Autowired
    private ReqListMapper reqListMapper;

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

        return reqListMapper.selectPage(page, wrapper);
    }

    /**
     * 根据需求评估单号查询需求列表
     */
    public ReqList getByReqNo(String reqNo) {
        return reqListMapper.selectById(reqNo);
    }

    /**
     * 导入需求列表 Excel
     */
    @Transactional(rollbackFor = Exception.class)
    public int importExcel(MultipartFile file) throws Exception {
        List<ReqList> list = com.xqcl.util.ExcelImportUtil.parseReqListExcel(file);

        if (CollUtil.isEmpty(list)) {
            log.warn("Excel 文件解析结果为空");
            return 0;
        }

        int count = 0;
        for (ReqList reqList : list) {
            ReqList existReq = reqListMapper.selectById(reqList.getReqNo());
            if (existReq != null) {
                // 更新
                reqListMapper.updateById(reqList);
                log.info("更新需求列表: {}", reqList.getReqNo());
            } else {
                // 新增
                reqListMapper.insert(reqList);
                log.info("新增需求列表: {}", reqList.getReqNo());
            }
            count++;
        }

        log.info("导入需求列表完成，共处理 {} 条", count);
        return count;
    }

    /**
     * 导出单个需求的 Markdown 文件
     */
    public String exportMarkdown(String reqNo) {
        // 查询需求列表
        ReqList reqList = reqListMapper.selectById(reqNo);
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
            List<ReqList> allReqLists = reqListMapper.selectList(null);
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
