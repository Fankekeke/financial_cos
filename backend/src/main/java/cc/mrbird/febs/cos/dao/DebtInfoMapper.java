package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.DebtInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface DebtInfoMapper extends BaseMapper<DebtInfo> {

    IPage<LinkedHashMap<String, Object>> debtInfoByPage(Page page, @Param("debtInfo") DebtInfo debtInfo);
}
