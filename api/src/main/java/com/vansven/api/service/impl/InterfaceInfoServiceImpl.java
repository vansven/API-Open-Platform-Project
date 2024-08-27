package com.vansven.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vansven.api.constant.GlobalConstant;
import com.vansven.api.constant.StatusCode;
import com.vansven.api.controller.exception.BusinessException;
import com.vansven.api.controller.exception.SystemException;
import com.vansven.api.domain.entity.InterfaceInfo;
import com.vansven.api.domain.entity.UserInfo;
import com.vansven.api.mapper.InterfaceInfoMapper;
import com.vansven.api.service.InterfaceInfoService;
import com.vansven.api.domain.vo.interfaceinfo.CreateInterRequest;
import com.vansven.api.domain.vo.interfaceinfo.PageQueryInterRequest;
import com.vansven.api.domain.vo.interfaceinfo.UpdateInterRequest;
import neu.vansven.apiclientsdk.domain.Person;
import neu.vansven.apiclientsdk.service.ClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author vansven
* @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
* @createDate 2024-07-09 13:54:19
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{
    @Autowired
    InterfaceInfoMapper infoMapper;

    @Autowired
    UserInfoServiceImpl userInfoService;

    @Autowired
    ClientService clientService;

    @Override
    public boolean createInter(CreateInterRequest createInter, HttpServletRequest request) {
        if(!userInfoService.isLogin(request)){
            throw new BusinessException(StatusCode.LOGIN_ERROR,"请先登录");
        }
        if(createInter == null){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"参数不为空");
        }
        //参数验证
        String name = createInter.getName(); // 非空
        String url = createInter.getUrl(); // 非空
        String method = createInter.getMethod(); // 非空
        if(StringUtils.isAnyBlank(name,url,method)){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"参数不能为空");
        }
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("name",name);
        Long count = infoMapper.selectCount(wrapper);
        if(count != 0){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"不能重复添加相同名称的接口信息");
        }
        InterfaceInfo newInter = new InterfaceInfo();
        BeanUtils.copyProperties(createInter, newInter);
        UserInfo userLogin = (UserInfo) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
        newInter.setUserId(userLogin.getId());
        boolean save = this.save(newInter);
        if(!save){
            throw new SystemException(StatusCode.SYSTEM_ERROR,"接口信息插入数据库失败");
        }
        return save;

    }

    @Override
    public InterfaceInfo getInterById(long id, HttpServletRequest request) {
        if(!userInfoService.isLogin(request)){
            throw new BusinessException(StatusCode.LOGIN_ERROR,"请先登录");
        }
        if(id <= 0){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"输入的id值不正确");
        }
        InterfaceInfo inter = this.getById(id);
        if(inter == null){
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "查询的接口信息不存在");
        }
        return inter;
    }

    @Override
    public List<InterfaceInfo> getInterByPage(PageQueryInterRequest pageQueryInterRequest, HttpServletRequest request) {
        if(!userInfoService.isLogin(request)){
            throw new BusinessException(StatusCode.LOGIN_ERROR,"请先登录");
        }
        int pageNum = pageQueryInterRequest.getPageNum();
        int pageSize = pageQueryInterRequest.getPageSize();
        String name = pageQueryInterRequest.getName();
        String sortedFiled = pageQueryInterRequest.getSortedFiled();
        if(pageNum < 1 || pageSize < 1){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"请正确输入查询信息");
        }
        // 先构造一个Page对象 分页获取 模糊查询 支持排序
        IPage<InterfaceInfo> infoPage = new Page<>(pageNum, pageSize);
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name),"name", name); // 模糊查询
        wrapper.orderBy(StringUtils.isNotBlank(sortedFiled), false, sortedFiled); // 排序输出
        List<InterfaceInfo> records = infoMapper.selectPage(infoPage, wrapper).getRecords();
        if(records == null){
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "数据库分页查询失败");
        }
        return records;
    }

    @Override
    public boolean deleteInterById(long id, HttpServletRequest request) {
        if(!userInfoService.isLogin(request)){
            throw new BusinessException(StatusCode.LOGIN_ERROR,"请先登录");
        }
        if(id <= 0){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"输入的id值不正确");
        }
        InterfaceInfo inter = this.getById(id);
        if(inter == null){
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "接口信息不存在");
        }
        // 仅管理员或本人可删除
        UserInfo userLogin = (UserInfo) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
        if(!userLogin.getId().equals(inter.getUserId()) && !userInfoService.isAdmin(userLogin)){
            throw new BusinessException(StatusCode.NO_AUTHOR_ERROR,"无权限删除接口信息");
        }
        boolean removed = this.removeById(id);
        if(!removed){
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "数据库删除失败");
        }
        return removed;
    }

    @Override
    public boolean updateInter(UpdateInterRequest updateInter, HttpServletRequest request) {
        if(!userInfoService.isLogin(request)){
            throw new BusinessException(StatusCode.LOGIN_ERROR,"请先登录");
        }
        //是否存在
        Long id = updateInter.getId();
        InterfaceInfo inter = infoMapper.selectById(id); // 原本的接口信息
        if(inter == null){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"接口不存在");
        }
        // 仅管理员或本人可修改
        UserInfo userLogin = (UserInfo) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
        if(!userLogin.getId().equals(inter.getUserId()) && !userInfoService.isAdmin(userLogin)){
            throw new BusinessException(StatusCode.NO_AUTHOR_ERROR,"无权限更新接口信息");
        }
        InterfaceInfo newInter = new InterfaceInfo();
        BeanUtils.copyProperties(updateInter, newInter);
        boolean updated = this.updateById(newInter);
        if(!updated){
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"数据库更新失败");
        }
        return updated;
    }

    @Override
    public boolean updateInterStatusOfClosed(Long id, HttpServletRequest request) {
        InterfaceInfo inter = getInterById(id, request);
        if(inter == null || id < 0){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"输入参数有误");
        }
        Integer interStatus = inter.getStatus();
        if(interStatus == GlobalConstant.INTERFACE_CLOSED){
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"接口已下线无需重复执行");
        }
        //只有管理员或者接口本人才能上线
        UserInfo userLogin = (UserInfo) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
        if(!userInfoService.isAdmin(userLogin) && userLogin.getId() != inter.getUserId()){
            throw new BusinessException(StatusCode.NO_AUTHOR_ERROR,"无权限下线接口");
        }
        InterfaceInfo updateInter = new InterfaceInfo();
        BeanUtils.copyProperties(inter,updateInter);
        updateInter.setStatus(GlobalConstant.INTERFACE_CLOSED);
        boolean updated = this.updateById(updateInter);
        if(!updated){
            throw new BusinessException(StatusCode.NO_AUTHOR_ERROR,"关闭接口状态失败");
        }
        return true;
    }

    @Override
    public boolean updateGetEntityByPostStatusOfOpen(Long id, HttpServletRequest request) {
        InterfaceInfo inter = getInterById(id, request);
        if(id != GlobalConstant.API2){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"输入id有误");
        }
        Integer interStatus = inter.getStatus();
        if(interStatus == GlobalConstant.INTERFACE_OPEN){
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"接口已上线无需重复执行");
        }
        //只有管理员或者接口本人才能上线
        UserInfo userLogin = (UserInfo) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
        if(!userInfoService.isAdmin(userLogin) && userLogin.getId() != inter.getUserId()){
            throw new BusinessException(StatusCode.NO_AUTHOR_ERROR,"无权限上线接口");
        }
        //todo 验证接口能不能正常使用，这里写死了待完善,验证的时候使用yml配置里面的公钥和私钥
        Person person = new Person();
        ResponseEntity<Person> responseEntity = clientService.getEntityByPost(person);
        if(responseEntity.getStatusCodeValue() != StatusCode.SUCCESS){
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"接口无法正常使用");
        }
        InterfaceInfo updateInter = new InterfaceInfo();
        BeanUtils.copyProperties(inter,updateInter);
        updateInter.setStatus(GlobalConstant.INTERFACE_OPEN);
        boolean updated = updateById(updateInter);
        if(!updated){
            throw new BusinessException(StatusCode.NO_AUTHOR_ERROR,"开放接口状态失败");
        }
        return updated;
    }

    @Override
    public boolean updateGetEntityByGetStatusOfOpen(Long id, HttpServletRequest request) {
        InterfaceInfo inter = getInterById(id, request);
        if(id != GlobalConstant.API1){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"输入id有误");
        }
        Integer interStatus = inter.getStatus();
        if(interStatus == GlobalConstant.INTERFACE_OPEN){
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"接口已上线无需重复执行");
        }
        //只有管理员或者接口本人才能上线
        UserInfo userLogin = (UserInfo) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
        if(!userInfoService.isAdmin(userLogin) && userLogin.getId() != inter.getUserId()){
            throw new BusinessException(StatusCode.NO_AUTHOR_ERROR,"无权限上线接口");
        }
        //todo 验证接口能不能正常使用，这里写死了待完善
        ResponseEntity<String>  responseEntity = clientService.getNameByGet("范思文");
        if(responseEntity.getStatusCodeValue() != StatusCode.SUCCESS){
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"接口无法正常使用");
        }
        InterfaceInfo updateInter = new InterfaceInfo();
        BeanUtils.copyProperties(inter,updateInter);
        updateInter.setStatus(GlobalConstant.INTERFACE_OPEN);
        boolean updated = updateById(updateInter);
        if(!updated){
            throw new BusinessException(StatusCode.NO_AUTHOR_ERROR,"开放接口状态失败");
        }
        return updated;
    }


}




