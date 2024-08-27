package com.vansven.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import neu.vansven.apiclientsdk.domain.Person;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiApplicationTests {

    @Test
    void contextLoads() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String str = "{\"name\":\"常婷婷\",\"age\":25,\"gender\":\"女\"}";
        Person person = mapper.readValue(str, Person.class);
        System.out.println(person);
    }

    @Test
    void randomTest(){
        String string = RandomStringUtils.randomAlphabetic(5);
        String str = RandomStringUtils.randomAlphanumeric(10);
        System.out.println(string + " " + str);
    }

}
