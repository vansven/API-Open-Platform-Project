package com.vansven.api.vo.userInfo;

import lombok.Data;

@Data
public class LoginQuery {

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

}
