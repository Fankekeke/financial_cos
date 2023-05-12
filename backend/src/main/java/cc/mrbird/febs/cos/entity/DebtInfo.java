package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 债务债权
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DebtInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 被授人
     */
    private String debtUser;

    /**
     * 金额
     */
    private BigDecimal price;

    /**
     * 发生日期
     */
    private String createDate;

    /**
     * 应付日期
     */
    private String updateDate;

    /**
     * 所属用户
     */
    private Long userId;

    /**
     * 类型 1.债务 2.债权
     */
    private Integer debtType;

    /**
     * 债务类型 1.信用还贷 2.个人借贷
     */
    private Integer debtModel;

    /**
     * 备注
     */
    private String content;


}
