package com.vansven.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vansven.api.constant.StatusCode;
import com.vansven.api.domain.dto.AnalysisInterfaceCount;
import com.vansven.api.domain.dto.BaseResponse;
import com.vansven.api.domain.dto.InterfaceCountStatistic;
import com.vansven.api.domain.vo.analysis.InterfaceCountTopNStatisticRequest;
import com.vansven.api.mapper.InterfaceInfoMapper;
import com.vansven.api.mapper.UserInterfaceInfoMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import neu.vansven.entity.InterfaceInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analysis")
@Api(tags = "接口调用次数统计分析")
public class AnalysisController {
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Resource
    private RedisTemplate<String,Long> redisTemplate;
    @ApiOperation(value = "获取调用次数为top n的接口信息")
    @PostMapping("/topn")
    public BaseResponse<List<AnalysisInterfaceCount>> getAllInvokeNumber(InterfaceCountTopNStatisticRequest interfaceCountTopNStatisticRequest){
        Integer n = interfaceCountTopNStatisticRequest.getTopN();
        List<InterfaceCountStatistic> interfaceStatistics = userInterfaceInfoMapper.getAllInvokeNumber(n);
        List<AnalysisInterfaceCount> analysisInterfaceCounts = interfaceStatistics.stream().map(
                interfaceStatistic -> {
                    AnalysisInterfaceCount analysisInterfaceCount = new AnalysisInterfaceCount();
                    BeanUtils.copyProperties(interfaceStatistic, analysisInterfaceCount);
                    QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", interfaceStatistic.getInterfaceId());
                    InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(queryWrapper);
                    analysisInterfaceCount.setInterfaceName(interfaceInfo.getName());
                    return analysisInterfaceCount;
                }).collect(Collectors.toList());
        return new BaseResponse<>(StatusCode.SUCCESS,"获取调用次数top n的接口信息成功",analysisInterfaceCounts);
    }

    @ApiOperation(value = "获取接口id被调用的次数")
    @PostMapping("/getByinterfaceId")
    public BaseResponse<Long> getInvokeNumberById(Long interfaceId){
        ValueOperations<String, Long> opsForValue = redisTemplate.opsForValue();
        Long count = opsForValue.get(String.valueOf(interfaceId));
        return new BaseResponse<>(StatusCode.SUCCESS, "获取统计的单个接口调用次数成功",count);
    }
}
