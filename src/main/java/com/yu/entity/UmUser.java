package com.yu.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author llrem
 * @since 2022-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UmUser implements Serializable {

    private static final long serialVersionUID=1L;

    private Long id;

    private String username;

    private String password;

    private String icon;

    private String nickName;

    private Integer gender;

    private String email;

    private String phone;

    private String job;

    private String city;

    private String personalizedSignature;

    private Integer status;


}
