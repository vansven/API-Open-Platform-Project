package com.vansven.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vansven.api.domain.vo.interfaceinfo.CreateInterRequest;
import com.vansven.api.domain.vo.interfaceinfo.PageQueryInterRequest;
import com.vansven.api.domain.vo.interfaceinfo.UpdateInterRequest;
import neu.vansven.entity.InterfaceInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author vansven
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2024-07-09 13:54:19
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 创建接口
     * @param createInter
     */
    boolean createInter(CreateInterRequest createInter, HttpServletRequest request);

    /**
     * 根据id获取接口信息
     * @param id
     */
    InterfaceInfo getInterById(long id, HttpServletRequest request);

    /**
     * 分页获取接口信息
     */
    List<InterfaceInfo> getInterByPage(PageQueryInterRequest pageQueryInterRequest, HttpServletRequest request);

    /**
     * 根据id删除接口信息
     * @param id
     */
    boolean deleteInterById(long id, HttpServletRequest request);

    /**
     * 更新接口信息
     * @param updateInter
     */
    boolean updateInter(UpdateInterRequest updateInter, HttpServletRequest request);

    /**
     * 更新接口状态信息
     * @param id
     * @param request
     * @return
     */
    boolean updateInterStatusOfClosed(Long id, HttpServletRequest request);

    boolean updateGetEntityByPostStatusOfOpen(Long id, HttpServletRequest request);

    boolean updateGetEntityByGetStatusOfOpen(Long id, HttpServletRequest request);
}
