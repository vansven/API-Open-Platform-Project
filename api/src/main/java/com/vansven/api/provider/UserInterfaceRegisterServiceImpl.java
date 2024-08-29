package com.vansven.api.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.vansven.api.constant.StatusCode;
import com.vansven.api.controller.exception.BusinessException;
import com.vansven.api.domain.entity.UserInterfaceInfo;
import com.vansven.api.mapper.UserInterfaceInfoMapper;
import neu.vansven.service.UserInterfaceRegisterService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class UserInterfaceRegisterServiceImpl implements UserInterfaceRegisterService {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;
    @Override
    public boolean invokeCount(long userId, long interfaceId) {
        QueryWrapper<UserInterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("interface_id",interfaceId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(wrapper);
        if(userInterfaceInfo == null){
            throw new BusinessException(StatusCode.PARAMATER_ERROR, "不存在用户接口关系信息");
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);
        updateWrapper.eq("interface_id",interfaceId);
        updateWrapper.setSql("total_num = total_num + 1, left_num = left_num - 1");
        int update = userInterfaceInfoMapper.update(null, updateWrapper);
        return update > 0;
    }
}
