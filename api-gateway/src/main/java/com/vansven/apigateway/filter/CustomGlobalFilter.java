package com.vansven.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import neu.vansven.apiclientsdk.utils.SignUtil;
import neu.vansven.entity.InterfaceInfo;
import neu.vansven.entity.UserInfo;
import neu.vansven.service.InterfaceRegisterService;
import neu.vansven.service.UserInterfaceRegisterService;
import neu.vansven.service.UserRegisterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private UserRegisterService userRegisterService;

    @DubboReference
    private InterfaceRegisterService interfaceRegisterService;

    @DubboReference
    private UserInterfaceRegisterService userInterfaceRegisterService;

    private final static List<String> BLACK_LIST = Arrays.asList("127.162.72.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //- 1、用户发送请求到API网关
        log.info("全局过滤器");
        //- 2、请求日志
        ServerHttpRequest request = exchange.getRequest();
        request.mutate().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String method = request.getMethodValue();
        String path = request.getPath().value();
        String clientAddress = request.getLocalAddress().getHostString();
        int port = request.getLocalAddress().getPort();
        MultiValueMap<String, String> params = request.getQueryParams();
        log.info("请求方法：" + method);
        log.info("请求的主机地址：" + clientAddress);
        log.info("服务器资源路径：" + path);
        log.info("url拼接的请求参数：" + params);
        //- 3、（黑白名单）
        if(BLACK_LIST.contains(clientAddress)){
            ServerHttpResponse response = exchange.getResponse();
            // 直接设置状态码，标记完成返回
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        //- 4、用户鉴权（判断ak，sk是否合法）
        String nonce = request.getHeaders().getFirst("nonce");
        String timestamp = request.getHeaders().getFirst("timestamp");
        String originalBody = request.getHeaders().getFirst("requestParams");
        String body = null;
        try {
            body = URLDecoder.decode(originalBody, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("字符串解码错误");
        }
        String publickey = request.getHeaders().getFirst("publickey");
        String sign = request.getHeaders().getFirst("sign");
        //- 5、请求的模拟接口是否存在，从数据库中查询模拟的接口是否存在，以及请求方法是否匹配（还可以校验请求参数）
        String url = "http://" + clientAddress + ":" + port + path;
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = interfaceRegisterService.isHaveInterface(url, method);
        }catch (Exception e){
            log.error("查询接口数据库错误");
        }
        //- 6、根据第三方数据库中找对应的私钥验证签名 根据相同的参数进行摘要算法对比前端传来的sign签名
        UserInfo userInfo = null;
        try {
            userInfo = userRegisterService.isHaveUser(publickey);
        } catch (Exception e) {
            throw new RuntimeException("查询用户数据库错误");
        }
        String privateKey = userInfo.getPrivateKey();
        String salt = nonce + timestamp + body + publickey;
        String authorSign = SignUtil.genSign(salt, privateKey);
        if(!sign.equals(authorSign)){
            throw new RuntimeException("签名认证不通过，无权限访问该资源");
        }
        //- 7、转发调用模拟接口，处理响应数据
        //- 调用成功，接口调用次数
        //- 响应日志
        //- 调用失败，返回一个规范的错误码
        log.info("调用转发接口前");
        return handleResponse(exchange,chain,userInfo.getId(),interfaceInfo.getId());

    }

    /**
     * 处理响应数据
     *
     * @param exchange
     * @param chain
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long userId, Long interfaceInfoId) {
        //获取response的返回数据
        ServerHttpResponse originalResponse = exchange.getResponse();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                log.info("调用完转发接口后执行的代码");
                if (getStatusCode().equals(HttpStatus.OK) && body instanceof Flux) {
                    DataBufferFactory bufferFactory = originalResponse.bufferFactory();
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        //释放掉内存
                        DataBufferUtils.release(dataBuffer);
                        //responseData就是下游系统返回的内容,可以查看修改
                        String responseData = new String(content, Charset.forName("UTF-8"));
                        // - 8、调用成功，更新调用接口次数
                        try {
                            userInterfaceRegisterService.invokeCount(userId, interfaceInfoId);
                        } catch (Exception e) {
                            log.error("查询用户接口关系信息错误");
                        }
                        // - 9、响应日志
                        log.info("***********************************响应信息**********************************");
                        log.info("响应内容:{}", responseData);
                        log.info("****************************************************************************\n");
                        return bufferFactory.wrap(responseData.getBytes());
                    }));
                } else {
                    log.error("响应code异常:{}", getStatusCode());
                    //- 8、直接设置错误状态码，标记完成返回
                    setStatusCode(HttpStatus.NOT_ACCEPTABLE);
                    return setComplete();
                }
            }
        };
        log.info("开始调用转发接口");
        return chain.filter(exchange.mutate().response(decoratedResponse).build());// 设置 response 对象为装饰过的 decoratedResponse
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
