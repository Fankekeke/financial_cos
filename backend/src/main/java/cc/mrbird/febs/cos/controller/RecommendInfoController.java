package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.RecommendInfo;
import cc.mrbird.febs.cos.service.IRecommendInfoService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/recommend-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecommendInfoController {

    private final IRecommendInfoService recommendInfoService;

    @GetMapping("/page")
    public R page(Page page, RecommendInfo recommendInfo) {
        return R.ok(recommendInfoService.page(page, Wrappers.<RecommendInfo>lambdaQuery()
                .like(StrUtil.isNotEmpty(recommendInfo.getCode()), RecommendInfo::getCode, recommendInfo.getCode())
                .like(StrUtil.isNotEmpty(recommendInfo.getName()), RecommendInfo::getName, recommendInfo.getName())
                .eq(recommendInfo.getType() != null, RecommendInfo::getType, recommendInfo.getType())));
    }

    @PostMapping
    public R save(@RequestBody RecommendInfo recommendInfo) {
        recommendInfo.setCode("INS-"+new Date().getTime());
        return R.ok(recommendInfoService.save(recommendInfo));
    }

    @PutMapping
    public R edit(@RequestBody RecommendInfo recommendInfo) {
        return R.ok(recommendInfoService.updateById(recommendInfo));
    }

    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(recommendInfoService.removeByIds(ids));
    }

}
