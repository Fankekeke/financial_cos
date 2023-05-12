package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.Stock;
import cc.mrbird.febs.cos.dao.StockMapper;
import cc.mrbird.febs.cos.service.IStockService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {

    @Override
    public IPage<LinkedHashMap<String, Object>> getStockPage(Page page, Stock stock) {
        return baseMapper.getStockPage(page, stock);
    }
}
