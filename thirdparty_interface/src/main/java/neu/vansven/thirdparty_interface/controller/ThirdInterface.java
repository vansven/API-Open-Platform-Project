package neu.vansven.thirdparty_interface.controller;


import neu.vansven.thirdparty_interface.domain.Person;
import neu.vansven.thirdparty_interface.utils.SignUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
public class ThirdInterface {

    @GetMapping("/api1")
    public String getNameByGet(@RequestParam String name){
        return "name:" + name;
    }

    @PostMapping("/api2")
    public Person getEntityByPost(@RequestBody Person requestQuery, HttpServletRequest request){
        System.out.println("跳转过来了");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String publickey = request.getHeader("publickey");
        String sign = request.getHeader("sign");

        // 根据第三方中找对应的私钥验证签名 根据相同的参数进行摘要算法对比前端传来的sign签名
        String authorSign = SignUtil.genSign(requestQuery.toString(), "vansvenfan");
        if(!sign.equals(authorSign)){
            throw new RuntimeException("签名认证不通过，无权限访问该资源");
        }
        Person responseBody = new Person();
        BeanUtils.copyProperties(requestQuery,responseBody);
        return responseBody;

    }

}
