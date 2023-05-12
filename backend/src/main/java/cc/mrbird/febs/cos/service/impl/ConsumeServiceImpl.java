package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.Consume;
import cc.mrbird.febs.cos.dao.ConsumeMapper;
import cc.mrbird.febs.cos.service.IConsumeService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author FanK
 */
@Service
public class ConsumeServiceImpl extends ServiceImpl<ConsumeMapper, Consume> implements IConsumeService {

    @Override
    public IPage<LinkedHashMap<String, Object>> getConsumePage(Page page, Consume consume) {
        return baseMapper.getConsumePage(page, consume);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getStatistionDay() {
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 7; i++) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, - (i+1));
            LinkedHashMap<String, Object> sumItem = baseMapper.statistSum(sdf.format(c.getTime()));
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>() {
                {
                    put("date", sdf.format(c.getTime()));
                    put("income", sumItem != null && sumItem.get("income") != null ? sumItem.get("income").toString() : "");
                    put("expenses", sumItem != null && sumItem.get("expenses") != null ? sumItem.get("expenses").toString() : "");
                }
            };
            result.add(item);
        }
        return result;
    }

    @Override
    public List<LinkedHashMap<String, Object>> getStatistionType(String date) {
        return baseMapper.statistionType(date);
    }

    @Override
    public LinkedHashMap<String, Object> statistionMonth() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 本月
        LinkedHashMap<String, Object> toMonth = baseMapper.statistionMonth(sdf.format(new Date()));
        System.out.println("=====>"+JSON.toJSONString(toMonth));
        // 上月
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, - 1);
        System.out.println("=====>"+sdf.format(c.getTime()));
        LinkedHashMap<String, Object> beMonth = baseMapper.statistionMonth(sdf.format(c.getTime()));
        result.put("toMonth", toMonth);
        result.put("beMonth", beMonth);
        return result;
    }

    @Override
    public List<LinkedHashMap<String, Object>> export(Integer flag) {
        return baseMapper.export(flag);
    }

    @Override
    public List<LinkedHashMap<String, Object>> export1(Integer flag, String month) {
        return baseMapper.export1(flag, month);
    }

    @Override
    public LinkedHashMap<String, Object> monthPriceByUser(Integer userId) {
        return baseMapper.monthPriceByUser(userId);
    }
}
