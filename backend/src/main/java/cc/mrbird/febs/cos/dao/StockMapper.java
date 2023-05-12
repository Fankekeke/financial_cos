package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.Stock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface StockMapper extends BaseMapper<Stock> {

    // 分页查询证券
    IPage<LinkedHashMap<String, Object>> getStockPage(Page page, @Param("stock") Stock stock);
}
