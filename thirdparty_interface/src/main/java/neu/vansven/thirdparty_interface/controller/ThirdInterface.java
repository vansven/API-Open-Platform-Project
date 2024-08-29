package neu.vansven.thirdparty_interface.controller;


import lombok.extern.slf4j.Slf4j;
import neu.vansven.thirdparty_interface.domain.Person;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
@Slf4j
public class ThirdInterface {

    @GetMapping("/api1")
    public String getNameByGet(@RequestParam String name){
        System.out.println("跳转过来了");
        return "name:" + name;
    }

    @PostMapping("/api2")
    public Person getEntityByPost(@RequestBody Person requestQuery, HttpServletRequest request){
        System.out.println("跳转过来了");
        Person responseBody = new Person();
        BeanUtils.copyProperties(requestQuery,responseBody);
        return responseBody;

    }

}
