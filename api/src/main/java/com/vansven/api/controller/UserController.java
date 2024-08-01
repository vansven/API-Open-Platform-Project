package com.vansven.api.controller;

import com.vansven.api.controller.exception.BusinessException;
import com.vansven.api.controller.exception.SystemException;
import com.vansven.api.domain.UserInfo;
import com.vansven.api.constant.StatusCode;
import com.vansven.api.constant.GlobalConstant;
import com.vansven.api.vo.userInfo.LoginQuery;
import com.vansven.api.vo.userInfo.RegisterQuery;
import com.vansven.api.dto.BaseResponse;
import com.vansven.api.service.impl.UserInfoServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserInfoServiceImpl userInfoService;

    @PostMapping("/register")
    public BaseResponse<Boolean> register(@RequestBody RegisterQuery registerQuery){
        //参数验证
        boolean valid = userInfoService.validRegisterParams(registerQuery);
        String userPassword = registerQuery.getUserPassword();
        //密码加密 相同命名空间总是生成相同的UUID
        String salt = UUID.nameUUIDFromBytes(GlobalConstant.SALT.getBytes()).toString().replaceAll("-","");
        String md5Password = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());
        registerQuery.setUserPassword(md5Password);
        //插入数据库中
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(registerQuery, userInfo);
        userInfo.setPublicKey(RandomStringUtils.randomAlphabetic(5));
        userInfo.setPrivateKey(RandomStringUtils.randomAlphanumeric(10));
        boolean save = userInfoService.save(userInfo);
        if(!save){
            throw new SystemException(StatusCode.SYSTEM_ERROR,"数据库插入失败");
        }
        return new BaseResponse<Boolean>(StatusCode.SUCCESS,"正常响应");
    }

    @PutMapping("/login")
    public BaseResponse<UserInfo> login(@RequestBody LoginQuery loginQuery, HttpServletRequest request)  {
        String userAccount = loginQuery.getUserAccount();
        String userPassword = loginQuery.getUserPassword();
        // 验证参数
        if(StringUtils.isAnyEmpty(userAccount,userPassword)){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"账号密码非空");
        }
        UserInfo userLogin = userInfoService.validLoginParams(loginQuery);
        request.getSession().setAttribute(GlobalConstant.LOGIN_USER,userLogin);
        return new BaseResponse<UserInfo>(StatusCode.SUCCESS,"登录成功", userLogin);
    }

    @DeleteMapping("/logout")
    public BaseResponse logout(HttpServletRequest request) {
        if(request.getSession().getAttribute(GlobalConstant.LOGIN_USER) == null){
            throw new BusinessException(StatusCode.LOGIN_ERROR,"请先登录");
        }
        request.getSession().removeAttribute(GlobalConstant.LOGIN_USER);
        return new BaseResponse(StatusCode.SUCCESS, "注销成功");
    }
}
