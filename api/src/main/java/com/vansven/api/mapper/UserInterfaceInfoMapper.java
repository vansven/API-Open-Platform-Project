package com.vansven.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vansven.api.domain.entity.UserInterfaceInfo;
import org.apache.ibatis.annotations.Mapper;

/**
* @author vansven
* @description 针对表【user_interface_info(用户接口关系表)】的数据库操作Mapper
* @createDate 2024-08-05 09:28:46
* @Entity com.vansven.api.domain.entity.UserInterfaceInfo
*/
@Mapper
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

}




