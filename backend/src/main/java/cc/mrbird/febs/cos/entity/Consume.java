package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Consume implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 收入或消费对象
     */
    @TableField("REASON")
    private String reason;

    /**
     * 收入或消费金额
     */
    @TableField("AMOUNT")
    private BigDecimal amount;

    /**
     * 收入或消费时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CONSUME_DATE")
    private LocalDateTime consumeDate;

    /**
     * 类型
     */
    @TableField("CONSUME_TYPE")
    private Integer consumeType;

    /**
     * 收入支出（0:收入 1:支出）
     */
    @TableField("CONSUME_FLAG")
    private Integer consumeFlag;

    /**
     * 所属人
     */
    @TableField("BELONGING")
    private Long belonging;

    @TableField("CODE")
    private String code;

    /**
     * 删除标识
     */
    @TableLogic
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String consumeDateStr;


}
