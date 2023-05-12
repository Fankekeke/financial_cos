package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.Stock;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IStockService extends IService<Stock> {

    // 分页查询证券
    IPage<LinkedHashMap<String, Object>> getStockPage(Page page, Stock stock);
}
