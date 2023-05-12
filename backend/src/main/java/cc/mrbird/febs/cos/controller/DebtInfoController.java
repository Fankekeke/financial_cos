package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DebtInfo;
import cc.mrbird.febs.cos.service.IDebtInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/debt-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DebtInfoController {

    private final IDebtInfoService debtInfoService;

    @GetMapping("/page")
    public R page(Page page, DebtInfo debtInfo) {
        return R.ok(debtInfoService.debtInfoByPage(page, debtInfo));
    }

    @PostMapping
    public R save(@RequestBody DebtInfo debtInfo) {
        return R.ok(debtInfoService.save(debtInfo));
    }

    @PutMapping
    public R edit(@RequestBody DebtInfo debtInfo) {
        return R.ok(debtInfoService.updateById(debtInfo));
    }

    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(debtInfoService.removeByIds(ids));
    }

}
