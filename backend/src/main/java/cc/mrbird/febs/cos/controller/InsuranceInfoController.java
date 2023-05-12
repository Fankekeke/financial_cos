package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.Consume;
import cc.mrbird.febs.cos.entity.InsuranceInfo;
import cc.mrbird.febs.cos.service.IConsumeService;
import cc.mrbird.febs.cos.service.IInsuranceInfoService;
import cn.hutool.core.date.DateUtil;
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
@RequestMapping("/cos/insurance-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InsuranceInfoController {

    private final IInsuranceInfoService insuranceInfoService;

    private final IConsumeService consumeService;

    /**
     * 分页获取投保信息
     * @param page
     * @param insuranceInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, InsuranceInfo insuranceInfo) {
        return R.ok(insuranceInfoService.insuranceInfoByPage(page, insuranceInfo));
    }

    /**
     * 添加投保信息
     * @param insuranceInfo
     * @return
     */
    @PostMapping
    public R save(@RequestBody InsuranceInfo insuranceInfo) {
        Consume consume = new Consume();
        consume.setAmount(insuranceInfo.getPrice());
        consume.setBelonging(insuranceInfo.getPolicyholder());
        consume.setDeleteFlag(0);
        consume.setConsumeDate(DateUtil.toLocalDateTime(new Date()));
        consume.setConsumeFlag(0);
        consume.setConsumeType(9);
        consumeService.save(consume);
        insuranceInfo.setCode("INS-"+new Date().getTime());
        insuranceInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(insuranceInfoService.save(insuranceInfo));
    }

    /**
     * 修改投保信息
     * @param insuranceInfo
     * @return
     */
    @PutMapping
    public R edit(@RequestBody InsuranceInfo insuranceInfo) {
        return R.ok(insuranceInfoService.updateById(insuranceInfo));
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(insuranceInfoService.removeByIds(ids));
    }

}
