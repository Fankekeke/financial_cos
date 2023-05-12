package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AssetInfo;
import cc.mrbird.febs.cos.entity.Consume;
import cc.mrbird.febs.cos.service.IAssetInfoService;
import cc.mrbird.febs.cos.service.IConsumeService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/asset-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AssetInfoController {

    private final IAssetInfoService assetInfoService;

    private final IConsumeService consumeService;

    /**
     * 获取支出记录
     * @param code
     * @return
     */
    @GetMapping("/getConsumeByCode")
    public R getConsumeByCode(@RequestParam String code) {
        return R.ok(consumeService.list(Wrappers.<Consume>lambdaQuery().eq(Consume::getCode, code)));
    }

    /**
     * 分页获取固定资产
     * @param page
     * @param assetInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, AssetInfo assetInfo) {
        return R.ok(assetInfoService.assetInfoByPage(page, assetInfo));
    }

    /**
     * 添加固定资产
     * @param assetInfo
     * @return
     */
    @PostMapping
    public R save(@RequestBody AssetInfo assetInfo) {
        assetInfo.setCode("CON-"+new Date().getTime());
        assetInfo.setCreateDate(DateUtil.formatDate(new Date()));
        return R.ok(assetInfoService.save(assetInfo));
    }

    /**
     * 修改固定资产
     * @param assetInfo
     * @return
     */
    @PutMapping
    public R edit(@RequestBody AssetInfo assetInfo) {
        return R.ok(assetInfoService.updateById(assetInfo));
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(assetInfoService.removeByIds(ids));
    }

}
