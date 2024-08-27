package com.vansven.api.controller;

import com.vansven.api.constant.GlobalConstant;
import com.vansven.api.constant.StatusCode;
import com.vansven.api.controller.exception.BusinessException;
import com.vansven.api.domain.dto.BaseResponse;
import com.vansven.api.domain.entity.InterfaceInfo;
import com.vansven.api.domain.vo.interfaceinfo.*;
import com.vansven.api.service.impl.InterfaceInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import neu.vansven.apiclientsdk.domain.Person;
import neu.vansven.apiclientsdk.service.ClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/interface")
@Api(tags = "接口信息管理API")
public class InterfaceController {

    @Resource
    InterfaceInfoServiceImpl interServiceImpl;

    @Resource
    ClientService clientService;


    /**
     * 创建接口信息
     * @param createInterRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "创建接口信息")
    public BaseResponse<Boolean> addInterface(@RequestBody CreateInterRequest createInterRequest, HttpServletRequest request){
        boolean add = interServiceImpl.createInter(createInterRequest, request);
        return new BaseResponse<Boolean>(StatusCode.SUCCESS,"添加成功",add);
    }

    /**
     * 根据接口id获取接口信息
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/getById")
    @ApiOperation(value = "根据接口id获取接口信息")
    public BaseResponse<InterfaceInfo> getInterById(@RequestParam Long id, HttpServletRequest request){
        InterfaceInfo inter = interServiceImpl.getInterById(id, request);
        return new BaseResponse<>(StatusCode.SUCCESS,"根据id获取接口信息成功",inter);
    }

    /**
     * 删除接口信息
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除接口信息")
    public BaseResponse<Boolean> deleteInterById(@RequestParam Long id, HttpServletRequest request){
        boolean deleted = interServiceImpl.deleteInterById(id, request);
        return new BaseResponse<>(StatusCode.SUCCESS,"根据id删除接口成功",deleted);

    }

    /**
     * 根据接口id修改接口信息
     * @param updateInterRequest
     * @param request
     * @return
     */
    @PostMapping("/updateById")
    @ApiOperation(value = "根据接口id修改接口信息")
    public BaseResponse<Boolean> updateInter(@RequestBody UpdateInterRequest updateInterRequest,
                                             HttpServletRequest request){
        boolean updated = interServiceImpl.updateInter(updateInterRequest, request);
        return new BaseResponse<>(StatusCode.SUCCESS,"更新接口信息成功",updated);
    }


    /**
     * 分页获取接口信息
     * @param pageQueryInterRequest
     * @param request
     * @return
     */
    @PostMapping("/get")
    @ApiOperation(value = "分页获取接口信息")
    public BaseResponse<List<InterfaceInfo>> getInterByPage(@RequestBody PageQueryInterRequest pageQueryInterRequest,
                                                            HttpServletRequest request){
        List<InterfaceInfo> interByPage = interServiceImpl.getInterByPage(pageQueryInterRequest, request);
        return new BaseResponse<>(StatusCode.SUCCESS,"分页模糊获取接口信息成功",interByPage);
    }

    /**
     * 上线 api2 接口（需要密码）
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/online/GetEntityByPost")
    @ApiOperation(value = "上线 api2 接口（需要密码）")
    public BaseResponse<Boolean> onLineGetEntityByPost(@RequestParam Long id, HttpServletRequest request) {
        boolean updated = interServiceImpl.updateGetEntityByPostStatusOfOpen(id, request);
        return new BaseResponse<>(StatusCode.SUCCESS, "上线POST接口成功", updated);
    }

    /**
     * 上线 api1 接口(不需要密码)
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/online/GetEntityByGet")
    @ApiOperation(value = "上线 api1 接口(不需要密码)")
    public BaseResponse<Boolean> onLineGetEntityByGet(@RequestParam Long id, HttpServletRequest request){
        boolean updated = interServiceImpl.updateGetEntityByGetStatusOfOpen(id, request);
        return new BaseResponse<>(StatusCode.SUCCESS,"上线GET接口成功",updated);
    }

    /**
     * 下线接口
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/offline")
    @ApiOperation(value = "下线接口")
    public BaseResponse<Boolean> offLineGetEntityByPost(@RequestParam Long id, HttpServletRequest request){
        boolean updated = interServiceImpl.updateInterStatusOfClosed(id, request);
        return new BaseResponse<>(StatusCode.SUCCESS,"下线接口成功",updated);
    }

    /**
     * 在线调用 api2 接口（需要密码）
     * @param invokePostRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke/GetEntityByPost")
    @ApiOperation(value = "在线调用 api2 接口（需要密码）")
    public BaseResponse<Object> invokeGetEntityByPost(@RequestBody InvokePostRequest invokePostRequest,
                                                      HttpServletRequest request){
        Long id = invokePostRequest.getId();
        if(id != GlobalConstant.API2){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"输入id有误");
        }
        InterfaceInfo inter = interServiceImpl.getInterById(id, request);
        Integer interStatus = inter.getStatus();
        if(interStatus != GlobalConstant.INTERFACE_OPEN){
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"接口暂时无法对外开放");
        }
        String publicKey = invokePostRequest.getPublicKey();
        String privateKey = invokePostRequest.getPrivateKey();
        if(StringUtils.isAnyBlank(publicKey,privateKey)){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"请输入密钥对");
        }
        Person person = invokePostRequest.getPerson(); //json 自动转 person 对象 反序列化
        ClientService client = new ClientService(publicKey, privateKey); // 传入指定的私钥和公钥进行验证
        ResponseEntity<Person> responseEntity = client.getEntityByPost(person);//调用api2接口
        return new BaseResponse<>(StatusCode.SUCCESS,"调用接口成功",responseEntity);
    }

    /**
     * 在线调用 api1 接口(不需要密码)
     * @param invokeGetRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke/GetEntityByGet")
    @ApiOperation(value = "在线调用 api1 接口(不需要密码)")
    public BaseResponse<Object> invokeGetEntityByGet(@RequestBody InvokeGetRequest invokeGetRequest,
                                                      HttpServletRequest request){
        Long id = invokeGetRequest.getId();
        if(id != GlobalConstant.API1){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"输入id有误");
        }
        InterfaceInfo inter = interServiceImpl.getInterById(id, request);
        Integer interStatus = inter.getStatus();
        if(interStatus != GlobalConstant.INTERFACE_OPEN){
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"接口暂时无法对外开放");
        }
        String requestParams = invokeGetRequest.getName();
        ResponseEntity<String> responseEntity = clientService.getNameByGet(requestParams); // 调用api接口1
        if(responseEntity.getStatusCodeValue() != StatusCode.SUCCESS){
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"调用接口失败");
        }
        return new BaseResponse<>(StatusCode.SUCCESS,"调用接口成功",responseEntity);
    }

}
