package com.vansven.api.service.impl;

import com.vansven.api.domain.vo.userinterfacerinfo.InterfaceCountRequest;
import com.vansven.api.service.UserInterfaceInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
public class UserInterfaceInfoServiceImplTest {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;


    @Test
    public void interfaceCount() {
        InterfaceCountRequest request = new InterfaceCountRequest();
        request.setUserId(1l);
        request.setInterfaceId(1l);
        userInterfaceInfoService.interfaceCount(request);
    }
}