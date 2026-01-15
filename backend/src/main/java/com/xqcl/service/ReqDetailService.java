package com.xqcl.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqcl.entity.ReqDetail;
import com.xqcl.mapper.ReqDetailMapper;
import com.xqcl.util.ExcelImportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 需求详情 Service（优化版 - 支持批量处理）
 *
 * @author xqcl
 * @since 2024-01-15
 */
@Slf4j
@Service
public class ReqDetailService extends ServiceImpl<ReqDetailMapper, ReqDetail> {

    /**
     * 根据需求评估单号查询需求详情列表
     */
    public List<ReqDetail> listByReqNo(String reqNo) {
        LambdaQueryWrapper<ReqDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReqDetail::getReqNo, reqNo);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 导入需求详情 Excel（优化版 - 批量处理）
     */
    @Transactional(rollbackFor = Exception.class)
    public int importExcel(MultipartFile file) throws Exception {
        // 使用批量处理方式解析 Excel
        final int[] totalCount = {0};

        ExcelImportUtil.parseReqDetailExcelBatch(file, batch -> {
            // 批量处理数据
            if (CollUtil.isEmpty(batch)) {
                return;
            }

            // 分离有 reqNo 和无 reqNo 的数据
            List<ReqDetail> hasReqNo = batch.stream()
                    .filter(d -> StrUtil.isNotBlank(d.getReqNo()))
                    .collect(Collectors.toList());

            List<ReqDetail> noReqNo = batch.stream()
                    .filter(d -> StrUtil.isBlank(d.getReqNo()))
                    .collect(Collectors.toList());

            // 1. 处理有 reqNo 的数据（查询已存在的）
            List<ReqDetail> insertList = new ArrayList<>();
            List<ReqDetail> updateList = new ArrayList<>();

            if (!hasReqNo.isEmpty()) {
                // 查询已存在的数据
                List<String> reqNos = hasReqNo.stream()
                        .map(ReqDetail::getReqNo)
                        .distinct()
                        .collect(Collectors.toList());

                LambdaQueryWrapper<ReqDetail> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(ReqDetail::getReqNo, reqNos);
                List<ReqDetail> existDetails = baseMapper.selectList(wrapper);

                // 构建已存在数据的映射
                Map<String, ReqDetail> existMap = existDetails.stream()
                        .collect(Collectors.toMap(
                                detail -> {
                                    String reqName = detail.getReqName();
                                    return detail.getReqNo() + "_" + (reqName != null ? reqName : "");
                                },
                                detail -> detail
                        ));

                // 分类需要新增和更新的数据
                for (ReqDetail reqDetail : hasReqNo) {
                    String reqName = reqDetail.getReqName();
                    String key = reqDetail.getReqNo() + "_" + (reqName != null ? reqName : "");
                    ReqDetail existDetail = existMap.get(key);

                    if (existDetail != null) {
                        // 需要更新
                        reqDetail.setId(existDetail.getId());
                        updateList.add(reqDetail);
                    } else {
                        // 需要新增
                        insertList.add(reqDetail);
                    }
                }
            }

            // 2. 无 reqNo 的数据直接插入（无法判断是否存在）
            insertList.addAll(noReqNo);

            // 3. 批量插入
            if (!insertList.isEmpty()) {
                saveBatch(insertList, 500);
                log.info("批量插入需求详情: {} 条", insertList.size());
            }

            // 4. 批量更新
            if (!updateList.isEmpty()) {
                updateBatchById(updateList, 500);
                log.info("批量更新需求详情: {} 条", updateList.size());
            }

            totalCount[0] += batch.size();
        });

        log.info("导入需求详情完成，共处理 {} 条", totalCount[0]);
        return totalCount[0];
    }

    /**
     * 批量保存或更新需求详情（供外部调用）
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveOrUpdate(List<ReqDetail> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }

        // 查询已存在的数据
        List<String> reqNos = list.stream()
                .map(ReqDetail::getReqNo)
                .distinct()
                .collect(Collectors.toList());

        LambdaQueryWrapper<ReqDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ReqDetail::getReqNo, reqNos);
        List<ReqDetail> existDetails = baseMapper.selectList(wrapper);

        // 构建已存在数据的映射
        Map<String, ReqDetail> existMap = existDetails.stream()
                .collect(Collectors.toMap(
                        detail -> detail.getReqNo() + "_" + detail.getReqName(),
                        detail -> detail
                ));

        // 分类需要新增和更新的数据
        List<ReqDetail> insertList = new ArrayList<>();
        List<ReqDetail> updateList = new ArrayList<>();

        for (ReqDetail reqDetail : list) {
            String key = reqDetail.getReqNo() + "_" + reqDetail.getReqName();
            ReqDetail existDetail = existMap.get(key);

            if (existDetail != null) {
                reqDetail.setId(existDetail.getId());
                updateList.add(reqDetail);
            } else {
                insertList.add(reqDetail);
            }
        }

        // 批量插入和更新
        if (!insertList.isEmpty()) {
            saveBatch(insertList, 500);
        }
        if (!updateList.isEmpty()) {
            updateBatchById(updateList, 500);
        }
    }
}
