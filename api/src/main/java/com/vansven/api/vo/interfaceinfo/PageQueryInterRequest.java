package com.vansven.api.vo.interfaceinfo;

import lombok.Data;

@Data
public class PageQueryInterRequest {
    /**
     * 页码
     */
    int pageNum;
    /**
     * 每页查询数量
     */
    int pageSize;
    /**
     * 根据接口名称模糊查询
     */
    String name;
    /**
     * 排序字段
     */
    String sortedFiled;
}
