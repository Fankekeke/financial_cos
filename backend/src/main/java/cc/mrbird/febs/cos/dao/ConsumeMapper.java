package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.Consume;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface ConsumeMapper extends BaseMapper<Consume> {

    // 分页查询
    IPage<LinkedHashMap<String, Object>> getConsumePage(Page page, @Param("consume") Consume consume);

    // 跟据日期查询收入合
    LinkedHashMap<String, Object> statistSum(@Param("date") String date);

    // 根据月份统计支出类型占比
    List<LinkedHashMap<String, Object>> statistionType(@Param("date") String date);

    // 根据月份获取收入支出
    LinkedHashMap<String, Object> statistionMonth(@Param("date") String date);

    // 导出
    List<LinkedHashMap<String, Object>> export(@Param("flag") Integer flag);

    // 导出
    List<LinkedHashMap<String, Object>> export1(@Param("flag") Integer flag, @Param("month") String month);

    LinkedHashMap<String, Object> monthPriceByUser(@Param("userId") Integer userId);
}
