package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.Fund;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IFundService extends IService<Fund> {

    // 分页查询基金
    IPage<LinkedHashMap<String, Object>> getConsumeIncomePage(Page page, Fund fund);
}
