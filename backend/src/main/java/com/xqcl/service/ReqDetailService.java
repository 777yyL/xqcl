package com.xqcl.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xqcl.entity.ReqDetail;
import com.xqcl.mapper.ReqDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 需求详情 Service
 *
 * @author xqcl
 * @since 2024-01-15
 */
@Slf4j
@Service
public class ReqDetailService {

    @Autowired
    private ReqDetailMapper reqDetailMapper;

    /**
     * 根据需求评估单号查询需求详情列表
     */
    public List<ReqDetail> listByReqNo(String reqNo) {
        LambdaQueryWrapper<ReqDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReqDetail::getReqNo, reqNo);
        return reqDetailMapper.selectList(wrapper);
    }

    /**
     * 导入需求详情 Excel
     */
    @Transactional(rollbackFor = Exception.class)
    public int importExcel(MultipartFile file) throws Exception {
        List<ReqDetail> list = com.xqcl.util.ExcelImportUtil.parseReqDetailExcel(file);

        if (CollUtil.isEmpty(list)) {
            log.warn("Excel 文件解析结果为空");
            return 0;
        }

        int count = 0;
        for (ReqDetail reqDetail : list) {
            // 检查是否已存在
            LambdaQueryWrapper<ReqDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ReqDetail::getReqNo, reqDetail.getReqNo())
                    .eq(ReqDetail::getReqName, reqDetail.getReqName());
            ReqDetail existDetail = reqDetailMapper.selectOne(wrapper);

            if (existDetail != null) {
                // 更新
                reqDetail.setId(existDetail.getId());
                reqDetailMapper.updateById(reqDetail);
                log.info("更新需求详情: {} - {}", reqDetail.getReqNo(), reqDetail.getReqName());
            } else {
                // 新增
                reqDetailMapper.insert(reqDetail);
                log.info("新增需求详情: {} - {}", reqDetail.getReqNo(), reqDetail.getReqName());
            }
            count++;
        }

        log.info("导入需求详情完成，共处理 {} 条", count);
        return count;
    }
}
