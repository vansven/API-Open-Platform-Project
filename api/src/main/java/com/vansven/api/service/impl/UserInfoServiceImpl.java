package com.vansven.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vansven.api.constant.GlobalConstant;
import com.vansven.api.constant.StatusCode;
import com.vansven.api.controller.exception.BusinessException;
import com.vansven.api.domain.entity.UserInfo;
import com.vansven.api.domain.vo.userInfo.LoginRequest;
import com.vansven.api.domain.vo.userInfo.RegisterRequest;
import com.vansven.api.mapper.UserInfoMapper;
import com.vansven.api.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author vansven
* @description 针对表【user_info(用户信息表)】的数据库操作Service实现
* @createDate 2024-07-08 11:07:01
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{
    @Autowired
    UserInfoMapper userInfoMapper;

    public boolean validRegisterParams(RegisterRequest registerQuery) throws BusinessException {
         String userAccount = registerQuery.getUserAccount();
         String userPassword = registerQuery.getUserPassword();
        //1、校验
        if(StringUtils.isAnyEmpty(userAccount,userPassword)){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"账号密码非空");
        }
        if(userAccount.length() < 4 || userPassword.length() < 8){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"账号密码长度过短");
        }
        //账户不包含特殊字符，只允许数字、字母、下划线
        // 1) 先定义一个String类型的正则表达式包括所有特殊字符
        // 2) 再用java中的Pattern和Matcher进行匹配字符串，
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(StatusCode.PARAMATER_ERROR, "账户不允许包括特殊字符");
        }
        //账户不能重复：按指定账户名查询所有用户如果结果不为空就存在相同的
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(" user_account",userAccount);
        Long count = userInfoMapper.selectCount(wrapper);
        if(count > 0){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"账户不允许重复");  // done 参数异常 账户不允许重复
        }
        return true;
    }

    public UserInfo validLoginParams(LoginRequest loginQuery) throws BusinessException {
         String userAccount = loginQuery.getUserAccount();
         String userPassword = loginQuery.getUserPassword();
        //1、初步校验参数
        if(StringUtils.isAnyEmpty(userAccount, userPassword)){ // 只要有一个不为空
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"账户密码非空");  // done 参数异常 账户密码非空
        }
        if(userAccount.length() < 4 || userPassword.length() < 8){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"账户密码不正确"); // done 参数异常 账户密码长度过短
        }
        //账户不包含特殊字符，只允许数字、字母、下划线
        // 1) 先定义一个String类型的正则表达式包括所有特殊字符
        // 2) 再用java中的Pattern和Matcher进行匹配字符串，
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"账户不允许包括特殊字符");  // done 参数异常 账户不允许包括特殊字符
        }
        // 2、密码校验:根据账户和校验密码查询是否存在
        String salt = UUID.nameUUIDFromBytes(GlobalConstant.SALT.getBytes()).toString().replaceAll("-","");
        String md5 = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        // select count(*) from User where account = ?
        wrapper.eq("user_account", userAccount);
        wrapper.eq("user_password",md5);
        UserInfo user = userInfoMapper.selectOne(wrapper);
        if(user == null){
            throw new BusinessException(StatusCode.PARAMATER_ERROR,"账户未注册"); // done 参数异常 未注册无法登录
        }
        return getSafetyUser(user);
    }

    public UserInfo getSafetyUser(UserInfo original){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(original.getId());
        userInfo.setUserName(original.getUserName());
        userInfo.setUserAccount(original.getUserAccount());
        userInfo.setUserPassword(null); // 脱密
        userInfo.setUserAvatar(original.getUserAvatar());
        userInfo.setIsAdmin(original.getIsAdmin());
        userInfo.setCreateTime(original.getCreateTime());
        userInfo.setUpdateTime(original.getUpdateTime());
        userInfo.setIsDelete(original.getIsDelete());
        userInfo.setPublicKey(null);//脱密
        userInfo.setPrivateKey(null);//脱密
        return userInfo;
    }

    public boolean isLogin(HttpServletRequest request){
        UserInfo userLogin = (UserInfo) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
        if(userLogin == null){
            return false;
        }
        return true;
    }

    public boolean isAdmin(UserInfo userInfo){
        Integer isAdmin = userInfo.getIsAdmin();
        if(isAdmin.equals(0)){
            return false;
        }
        return true;
    }

}




