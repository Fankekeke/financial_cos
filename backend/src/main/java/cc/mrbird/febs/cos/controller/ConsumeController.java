package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.common.utils.email;
import cc.mrbird.febs.cos.entity.Consume;
import cc.mrbird.febs.cos.entity.Fund;
import cc.mrbird.febs.cos.entity.InsuranceInfo;
import cc.mrbird.febs.cos.entity.Stock;
import cc.mrbird.febs.cos.service.IConsumeService;
import cc.mrbird.febs.cos.service.IFundService;
import cc.mrbird.febs.cos.service.IInsuranceInfoService;
import cc.mrbird.febs.cos.service.IStockService;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.UserService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/consume")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumeController {

    private final IConsumeService iConsumeService;

    private final IInsuranceInfoService insuranceInfoService;

    private final IFundService iFundService;

    private final IStockService iStockService;

    private final UserService userService;

    @GetMapping("/send")
    public R sendEmail(Integer userId) throws Exception {
        User user = userService.getById(userId);
        LinkedHashMap<String, Object> result = iConsumeService.monthPriceByUser(userId);
        StringBuilder stringBuilder = new StringBuilder("你好 "+user.getUsername()+"<br/>");
        stringBuilder.append("当您看到这封邮箱时,现在时间为:"+ DateUtil.formatDateTime(new Date()));
        stringBuilder.append("， 您本月收入共"+result.get("expenses").toString()+"元");
        stringBuilder.append("， 支出共"+result.get("income").toString()+"元");
        email.sendEmail(stringBuilder.toString(), user.getEmail());
        return R.ok(true);
    }

    @GetMapping("/home")
    public R home() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        List<Consume> price1 = iConsumeService.list(Wrappers.<Consume>lambdaQuery().eq(Consume::getConsumeFlag, 0).eq(Consume::getDeleteFlag, 0));
        List<Consume> price2 = iConsumeService.list(Wrappers.<Consume>lambdaQuery().eq(Consume::getConsumeFlag, 1).eq(Consume::getDeleteFlag, 0));
        BigDecimal total1 = price1.stream()
                .map(fd -> fd.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total2 = price2.stream()
                .map(fd -> fd.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("price1", total1);
        result.put("price2", total2);
        return R.ok(result);
    }

    @GetMapping("/allReport")
    public R allReport() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        // 收入
        List<Consume> consume1 = iConsumeService.list(Wrappers.<Consume>lambdaQuery().eq(Consume::getDeleteFlag, 0).eq(Consume::getConsumeFlag, 0));
        BigDecimal total1 = consume1.stream().map(fd -> fd.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 支出
        List<Consume> consume2 = iConsumeService.list(Wrappers.<Consume>lambdaQuery().eq(Consume::getDeleteFlag, 0).eq(Consume::getConsumeFlag, 1));
        BigDecimal total2 = consume2.stream().map(fd -> fd.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 保险
        List<InsuranceInfo> insuranceInfoList = insuranceInfoService.list();
        BigDecimal total3 = insuranceInfoList.stream().map(fd -> fd.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 基金
        List<Fund> fundList = iFundService.list(Wrappers.<Fund>lambdaQuery().eq(Fund::getDeleteFlag, 0));
        BigDecimal total4 = fundList.stream().map(fd -> fd.getRecent().multiply(new BigDecimal(fd.getShare()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 股票
        List<Stock> stockList = iStockService.list(Wrappers.<Stock>lambdaQuery().eq(Stock::getDeleteFlag, 0));
        BigDecimal total5 = stockList.stream().map(fd -> fd.getNowMoney().multiply(new BigDecimal(fd.getAmount()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("total1", total1);
        result.put("total2", total2);
        result.put("total3", total3);
        result.put("total4", total4);
        result.put("total5", total5);
        return R.ok(result);
    }

    /**
     * 根据月份统计收入支出情况
     * @return
     */
    @GetMapping("/statistionDay")
    public R getStatistionDay() {
        return R.ok(iConsumeService.getStatistionDay());
    }

    /**
     * 根据月份统计支出类型占比
     * @param date
     * @return
     */
    @GetMapping("/statistionType")
    public R getStatistionType(@RequestParam String date) {
        return R.ok(iConsumeService.getStatistionType(date));
    }

    /**
     * 根据月份获取收入支出
     * @return
     */
    @GetMapping("/statistionMonth")
    public R getStatistionMonth() {
        return R.ok(iConsumeService.statistionMonth());
    }

    /**
     * 获取导出数据
     * @param flag
     * @return
     */
    @GetMapping("/export")
    public R export(@RequestParam Integer flag) {
        return R.ok(iConsumeService.export(flag));
    }

    @GetMapping("/export1")
    public R export1(@RequestParam Integer flag, @RequestParam String month) {
        return R.ok(iConsumeService.export1(flag, month));
    }

    /**
     * 分页查询收入
     * @param page
     * @param consume
     * @return
     */
    @GetMapping("/income/page")
    public R getConsumeIncomePage(Page page, Consume consume) {
        return R.ok(iConsumeService.getConsumePage(page, consume));
    }

    /**
     * 分页查询支出
     * @param page
     * @param consume
     * @return
     */
    @GetMapping("/expenses/page")
    public R getConsumeExpensesPage(Page page, Consume consume) {
        return R.ok(iConsumeService.getConsumePage(page, consume));
    }

    /**
     * 添加收入或支出
     * @param consume
     * @return
     */
    @PostMapping
    public R save(@RequestBody Consume consume) {
        return R.ok(iConsumeService.save(consume));
    }

    /**
     * 修改收入或支出
     * @param consume
     * @return
     */
    @PutMapping
    public R update(@RequestBody Consume consume) {
        System.out.println(consume.toString());
        return R.ok(iConsumeService.updateById(consume));
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(iConsumeService.removeByIds(ids));
    }

}
