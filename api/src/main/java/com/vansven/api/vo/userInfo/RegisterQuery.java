package com.vansven.api.vo.userInfo;

import lombok.Data;

@Data
public class RegisterQuery {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 是否为管理员，0-不是，1-是，默认为 0
     */
    private Integer isAdmin;

}
