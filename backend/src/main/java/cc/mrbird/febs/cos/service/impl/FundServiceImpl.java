package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.Fund;
import cc.mrbird.febs.cos.dao.FundMapper;
import cc.mrbird.febs.cos.service.IFundService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class FundServiceImpl extends ServiceImpl<FundMapper, Fund> implements IFundService {

    @Override
    public IPage<LinkedHashMap<String, Object>> getConsumeIncomePage(Page page, Fund fund) {
        return baseMapper.getConsumeIncomePage(page, fund);
    }
}
