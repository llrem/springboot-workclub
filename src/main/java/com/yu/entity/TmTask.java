package com.yu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author llrem
 * @since 2022-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TmTask implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String description;

    private Long executor;

    private LocalDateTime startDate;

    private LocalDateTime dueDate;

    private Integer priority;

    private Integer type;

    private Integer status;

}
