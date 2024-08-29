package com.vansven.api.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vansven.api.constant.StatusCode;
import com.vansven.api.controller.exception.BusinessException;
import com.vansven.api.mapper.InterfaceInfoMapper;
import neu.vansven.entity.InterfaceInfo;
import neu.vansven.service.InterfaceRegisterService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InterfaceRegisterServiceImpl implements InterfaceRegisterService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;
    @Override
    public InterfaceInfo isHaveInterface(String url, String method) {
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("url",url);
        wrapper.eq("method",method);
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(wrapper);
        if(interfaceInfo == null){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"接口不存在");
        }
        return interfaceInfo;
    }
}
