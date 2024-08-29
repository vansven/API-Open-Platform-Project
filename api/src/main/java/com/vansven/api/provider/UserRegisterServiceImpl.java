package com.vansven.api.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vansven.api.constant.StatusCode;
import com.vansven.api.controller.exception.BusinessException;
import com.vansven.api.mapper.UserInfoMapper;
import neu.vansven.entity.UserInfo;
import neu.vansven.service.UserRegisterService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class UserRegisterServiceImpl implements UserRegisterService {

    @Resource
    private UserInfoMapper userInfoMapper;
    @Override
    public UserInfo isHaveUser(String publicKey) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("public_key", publicKey);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        if(userInfo == null){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"不存在该用户");
        }
        return userInfo;
    }
}
