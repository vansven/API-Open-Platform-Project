package com.vansven.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.vansven.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
* @author vansven
* @description 针对表【user_info(用户信息表)】的数据库操作Mapper
* @createDate 2024-07-08 14:50:46
* @Entity generator.domain.UserInfo
*/
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}




