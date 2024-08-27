package com.vansven.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vansven.api.domain.entity.UserInterfaceInfo;
import com.vansven.api.domain.vo.userinterfacerinfo.AddUserInterfaceRequest;
import com.vansven.api.domain.vo.userinterfacerinfo.GetUserInterfaceInfoByPageRequest;
import com.vansven.api.domain.vo.userinterfacerinfo.InterfaceCountRequest;
import com.vansven.api.domain.vo.userinterfacerinfo.UpdateUserInterfaceRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author vansven
* @description 针对表【user_interface_info(用户接口关系表)】的数据库操作Service
* @createDate 2024-08-05 09:28:46
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    boolean addInfo(AddUserInterfaceRequest addUserInterfaceRequest, HttpServletRequest request);

    boolean updateInfo(UpdateUserInterfaceRequest updateUserInterfaceRequest, HttpServletRequest request);

    boolean deleteInfo(List<Long> lisId, HttpServletRequest request);

    UserInterfaceInfo getInfo(Long id, HttpServletRequest request);

    List<UserInterfaceInfo> getByPage(GetUserInterfaceInfoByPageRequest getUserInterfaceInfoByPageRequest, HttpServletRequest request);

    boolean interfaceCount(InterfaceCountRequest interfaceCountRequest);
}
