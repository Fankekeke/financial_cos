package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DebtInfo;
import cc.mrbird.febs.cos.dao.DebtInfoMapper;
import cc.mrbird.febs.cos.service.IDebtInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class DebtInfoServiceImpl extends ServiceImpl<DebtInfoMapper, DebtInfo> implements IDebtInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> debtInfoByPage(Page page, DebtInfo debtInfo) {
        return baseMapper.debtInfoByPage(page, debtInfo);
    }
}
