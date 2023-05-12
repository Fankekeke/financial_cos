package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 证券名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 证券编号
     */
    @TableField("CODE")
    private String code;

    /**
     * 类型
     */
    @TableField("TYPE")
    private Integer type;

    /**
     * 金额
     */
    @TableField("MONEY")
    private BigDecimal money;

    /**
     * 当前价格
     */
    @TableField("NOW_MONEY")
    private BigDecimal nowMoney;

    /**
     * 股数
     */
    @TableField("AMOUNT")
    private Integer amount;

    @TableField("USER_ID")
    private Long userId;

    /**
     * 单价
     */
    @TableField("UNIT")
    private BigDecimal unit;

    /**
     * 删除标识
     */
    @TableLogic
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;


}
