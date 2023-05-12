package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.InsuranceInfo;
import cc.mrbird.febs.cos.dao.InsuranceInfoMapper;
import cc.mrbird.febs.cos.service.IInsuranceInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class InsuranceInfoServiceImpl extends ServiceImpl<InsuranceInfoMapper, InsuranceInfo> implements IInsuranceInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> insuranceInfoByPage(Page page, InsuranceInfo insuranceInfo) {
        return baseMapper.insuranceInfoByPage(page, insuranceInfo);
    }
}
