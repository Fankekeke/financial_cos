package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.Fund;
import cc.mrbird.febs.cos.service.IFundService;
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
@RequestMapping("/cos/fund")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FundController {

    private final IFundService iFundService;

    /**
     * 分页查询基金
     * @param page
     * @param fund
     * @return
     */
    @GetMapping("/page")
    public R getConsumeIncomePage(Page page, Fund fund) {
        return R.ok(iFundService.getConsumeIncomePage(page, fund));
    }

    /**
     * 添加基金
     * @param fund
     * @return
     */
    @PostMapping
    public R save(@RequestBody Fund fund) {
        return R.ok(iFundService.save(fund));
    }

    /**
     * 修改基金
     * @param fund
     * @return
     */
    @PutMapping
    public R update(@RequestBody Fund fund) {
        return R.ok(iFundService.updateById(fund));
    }

    /**
     * 批量删除基金
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(iFundService.removeByIds(ids));
    }

}
