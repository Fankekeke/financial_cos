package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.Consume;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IConsumeService extends IService<Consume> {

    // 分页查询
    IPage<LinkedHashMap<String, Object>> getConsumePage(Page page, Consume consume);

    // 根据当天统计支出收入情况
    List<LinkedHashMap<String, Object>> getStatistionDay();

    // 根据月份统计支出类型占比
    List<LinkedHashMap<String, Object>> getStatistionType(String date);

    // 根据月份获取收入支出
    LinkedHashMap<String, Object> statistionMonth();

    // 导出
    List<LinkedHashMap<String, Object>> export(@Param("flag") Integer flag);

    // 导出
    List<LinkedHashMap<String, Object>> export1(Integer flag, String month);

    LinkedHashMap<String, Object> monthPriceByUser(@Param("userId") Integer userId);
}
