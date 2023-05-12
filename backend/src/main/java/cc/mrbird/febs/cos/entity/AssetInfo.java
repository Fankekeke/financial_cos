package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 固定资产管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AssetInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 编号
     */
    private String code;

    /**
     * 固定资产名称
     */
    private String name;

    /**
     * 总价格
     */
    private BigDecimal price;

    /**
     * 还款月份
     */
    private Integer monthNum;

    /**
     * 所属用户
     */
    private Long userId;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate startDate;

    /**
     * 月供金额
     */
    private BigDecimal monthPrice;

    /**
     * 创建时间
     */
    private String createDate;

    @TableField(exist = false)
    private String username;

}
