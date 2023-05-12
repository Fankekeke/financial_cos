package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.Supplier;
import cc.mrbird.febs.cos.service.ISupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/supplier")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplierController {

    private final ISupplierService iSupplierService;

    /**
     * 添加供应对象
     * @param supplier
     * @return
     */
    @PostMapping
    public R save(Supplier supplier) {
        return R.ok(iSupplierService.save(supplier));
    }

    /**
     * 修改供应对象
     * @param supplier
     * @return
     */
    @PutMapping
    public R update(Supplier supplier) {
        return R.ok(iSupplierService.updateById(supplier));
    }

    /**
     * 批量删除供应对象
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(iSupplierService.removeByIds(ids));
    }

}
