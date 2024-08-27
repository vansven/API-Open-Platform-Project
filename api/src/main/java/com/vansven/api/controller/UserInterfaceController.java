package com.vansven.api.controller;

import com.vansven.api.constant.StatusCode;
import com.vansven.api.domain.dto.BaseResponse;
import com.vansven.api.domain.entity.UserInterfaceInfo;
import com.vansven.api.domain.vo.userinterfacerinfo.AddUserInterfaceRequest;
import com.vansven.api.domain.vo.userinterfacerinfo.GetUserInterfaceInfoByPageRequest;
import com.vansven.api.domain.vo.userinterfacerinfo.InterfaceCountRequest;
import com.vansven.api.domain.vo.userinterfacerinfo.UpdateUserInterfaceRequest;
import com.vansven.api.service.impl.UserInterfaceInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/userinterfaceinfo")
@Api(tags = "用户接口关系信息管理API")
public class UserInterfaceController {

    @Resource
    private UserInterfaceInfoServiceImpl userInterfaceInfoService;

    // region 后台管理员操作接口

    @PostMapping("/add")
    @ApiOperation(value = "添加用户接口关系信息")
    public BaseResponse<Boolean> addUserInterfaceInfo(@RequestBody AddUserInterfaceRequest addUserInterfaceRequest,
                                                      HttpServletRequest request){
        boolean isadd = userInterfaceInfoService.addInfo(addUserInterfaceRequest,request);
        return new BaseResponse<Boolean>(StatusCode.SUCCESS,"开通用户调用接口成功");
    }

    /**
     * 根据id修改用户接口关系信息
     * @param updateUserInterfaceRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "根据id修改用户接口关系信息")
    public BaseResponse<Boolean> updateUserInterfaceInfoById(@RequestBody UpdateUserInterfaceRequest updateUserInterfaceRequest, HttpServletRequest request){
        boolean isUpdate = userInterfaceInfoService.updateInfo(updateUserInterfaceRequest, request);
        return new BaseResponse<Boolean>(StatusCode.SUCCESS,"修改用户接口关系信息成功", isUpdate);
    }

    /**
     * 删除一个或多个用户接口关系信息，仅管理员可操作
     * @param lisId
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "批量删除接口关系信息")
    public BaseResponse<Boolean> deleteUserInterfaceInfoById(@RequestBody List<Long> lisId, HttpServletRequest request){
        boolean isdelete = userInterfaceInfoService.deleteInfo(lisId, request);
        return new BaseResponse<Boolean>(StatusCode.SUCCESS,"删除用户接口关系信息成功", isdelete);
    }

    /**
     * 根据id获取用户接口关系信息
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/getById")
    @ApiOperation(value = "根据id获取用户接口关系信息")
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(@RequestParam Long id, HttpServletRequest request){
        UserInterfaceInfo info = userInterfaceInfoService.getInfo(id, request);
        return new BaseResponse<UserInterfaceInfo>(StatusCode.SUCCESS,"根据id获取用户接口信息成功", info);
    }

    /**
     * 分页获取用户接口关系信息
     * @param getUserInterfaceInfoByPageRequest
     * @param request
     * @return
     */
    @PostMapping("/getByPage")
    @ApiOperation(value = "分页获取用户接口关系信息")
    public BaseResponse<List<UserInterfaceInfo>> getUserInterfaceInfoByPage(@RequestBody GetUserInterfaceInfoByPageRequest getUserInterfaceInfoByPageRequest,
                                                                            HttpServletRequest request){
        List<UserInterfaceInfo> listInfo = userInterfaceInfoService.getByPage(getUserInterfaceInfoByPageRequest, request);
        return new BaseResponse<List<UserInterfaceInfo>>(StatusCode.SUCCESS,"分页获取用户接口关系信息成功", listInfo);
    }


     //endregion

    // region 用户调用接口后操作接口

    @PostMapping("/invokeInterface")
    @ApiOperation(value = "用户掉用接口次数统计更新")
    public BaseResponse<Boolean> interfaceCount(InterfaceCountRequest interfaceCountRequest){
        boolean updateCount = userInterfaceInfoService.interfaceCount(interfaceCountRequest);
        return new BaseResponse<Boolean>(StatusCode.SUCCESS,"接口统计调用次数更新成功");
    }

    //endregion


}
