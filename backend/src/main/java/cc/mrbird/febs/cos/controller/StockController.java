package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.Stock;
import cc.mrbird.febs.cos.service.IStockService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/stock")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockController {

    private final IStockService iStockService;

    /**
     * 分页查询证券
     * @param page
     * @param stock
     * @return
     */
    @GetMapping("/page")
    public R getStockPage(Page page, Stock stock) {
        return R.ok(iStockService.getStockPage(page, stock));
    }

    /**
     * 保存证券信息
     * @param stock
     * @return
     */
    @PostMapping
    public R save(@RequestBody Stock stock) {
        return R.ok(iStockService.save(stock));
    }

    /**
     * 修改证券信息
     * @param stock
     * @return
     */
    @PutMapping
    public R update(@RequestBody Stock stock) {
        return R.ok(iStockService.updateById(stock));
    }

    /**
     * 删除证券信息
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(iStockService.removeByIds(ids));
    }
}
