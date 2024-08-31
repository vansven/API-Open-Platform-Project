package com.vansven.api.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vansven.api.constant.GlobalConstant;
import com.vansven.api.constant.StatusCode;
import com.vansven.api.controller.exception.BusinessException;
import com.vansven.api.domain.entity.UserInterfaceInfo;
import com.vansven.api.domain.vo.userinterfacerinfo.AddUserInterfaceRequest;
import com.vansven.api.domain.vo.userinterfacerinfo.GetUserInterfaceInfoByPageRequest;
import com.vansven.api.domain.vo.userinterfacerinfo.InterfaceCountRequest;
import com.vansven.api.domain.vo.userinterfacerinfo.UpdateUserInterfaceRequest;
import com.vansven.api.enums.UserInterfaceEnum;
import com.vansven.api.mapper.UserInterfaceInfoMapper;
import com.vansven.api.service.UserInterfaceInfoService;
import neu.vansven.entity.UserInfo;
import neu.vansven.service.UserInterfaceRegisterService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author vansven
* @description 针对表【user_interface_info(用户接口关系表)】的数据库操作Service实现
* @createDate 2024-08-05 09:28:46
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Resource
    private UserInfoServiceImpl userInfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private UserInterfaceRegisterService userInterfaceRegisterService;

    @Override
    public boolean addInfo(AddUserInterfaceRequest addUserInterfaceRequest, HttpServletRequest request) {
//        UserInfo loginUser = (UserInfo) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
//        if(!userInfoService.isAdmin(loginUser)){
//            throw new BusinessException(StatusCode.NO_AUTHOR_ERROR,"非管理员无法操作");
//        }
        Long userId = addUserInterfaceRequest.getUserId();
        Long interfaceId = addUserInterfaceRequest.getInterfaceId();
        Integer leftNum = addUserInterfaceRequest.getLeftNum();
        // 开通次数最少是10次
        if(leftNum < UserInterfaceEnum.OPENAPI_DEFAULT_COUNT.getCount()){
            throw new BusinessException(StatusCode.PARAMATER_ERROR, "默认开通最少调用接口次数为10次");
        }
        if(userId == null || interfaceId == null){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"请指定要开通的用户id和允许调用的接口id");
        }
        UserInterfaceInfo addData = new UserInterfaceInfo();
        BeanUtils.copyProperties(addUserInterfaceRequest,addData);
        return this.save(addData);
    }

    @Override
    public boolean updateInfo(UpdateUserInterfaceRequest updateUserInterfaceRequest, HttpServletRequest request) {
        UserInfo loginUser = (UserInfo) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
        if(!userInfoService.isAdmin(loginUser)){
            throw new BusinessException(StatusCode.NO_AUTHOR_ERROR,"非管理员无法操作");
        }
        Long id = updateUserInterfaceRequest.getId();
        Integer leftNum = updateUserInterfaceRequest.getLeftNum();
        Integer isPermite = updateUserInterfaceRequest.getIsPermite();
        // 先查询更新的用户接口关系这条记录是否存在
        UserInterfaceInfo preData = this.getById(id);
        if(preData == null){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"不存在该条用户接口关系记录");
        }
        leftNum += preData.getLeftNum();
        UserInterfaceInfo updateData = new UserInterfaceInfo();
        updateData.setId(id);
        updateData.setLeftNum(leftNum);
        updateData.setIsPermite(isPermite);
        return this.updateById(updateData);
    }

    @Override
    public boolean deleteInfo(List<Long> lisId, HttpServletRequest request) {
        // 仅限管理员删除用户接口关系信息
        UserInfo loginUser = (UserInfo) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
        if(!userInfoService.isAdmin(loginUser)){
            throw new BusinessException(StatusCode.NO_AUTHOR_ERROR,"非管理员无法操作");
        }
        if(lisId == null || lisId.isEmpty()){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"至少选中一个删除");
        }
        for(Long id:lisId){
            UserInterfaceInfo deleteData = this.getById(id);
            if(deleteData == null){
                throw new BusinessException(StatusCode.PARAMATER_ERROR,"删除的记录不存在");
            }
            this.removeById(id);
        }
        return false;
    }

    @Override
    public UserInterfaceInfo getInfo(Long id, HttpServletRequest request) {
        // 仅限管理员获取用户接口关系信息
        UserInfo loginUser = (UserInfo) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
        if(!userInfoService.isAdmin(loginUser)){
            throw new BusinessException(StatusCode.NO_AUTHOR_ERROR,"非管理员无法操作");
        }
        UserInterfaceInfo data = this.getById(id);
        if(data == null){
            throw new BusinessException(StatusCode.PARAMATER_ERROR, "数据不存在");
        }
        this.removeById(id);
        return data;
    }

    @Override
    public List<UserInterfaceInfo> getByPage(GetUserInterfaceInfoByPageRequest getUserInterfaceInfoByPageRequest, HttpServletRequest request) {
        Integer pageNumber = getUserInterfaceInfoByPageRequest.getPageNumber();
        Integer pageSize = getUserInterfaceInfoByPageRequest.getPageSize();

        // 仅限管理员分页获取用户接口关系信息
        UserInfo loginUser = (UserInfo) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
        if(!userInfoService.isAdmin(loginUser)){
            throw new BusinessException(StatusCode.NO_AUTHOR_ERROR,"非管理员无法操作");
        }
        Page<UserInterfaceInfo> page = new Page<>(pageNumber, pageSize);
        List<UserInterfaceInfo> records = userInterfaceInfoMapper.selectPage(page, null).getRecords();
        return records;
    }

}




