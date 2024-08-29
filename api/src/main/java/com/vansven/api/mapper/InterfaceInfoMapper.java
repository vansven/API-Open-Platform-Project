package com.vansven.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.vansven.entity.InterfaceInfo;
import org.apache.ibatis.annotations.Mapper;

/**
* @author vansven
* @description 针对表【interface_info(接口信息表)】的数据库操作Mapper
* @createDate 2024-07-09 13:54:19
* @Entity com.vansven.api.domain.entity.InterfaceInfo
*/
@Mapper
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

}




