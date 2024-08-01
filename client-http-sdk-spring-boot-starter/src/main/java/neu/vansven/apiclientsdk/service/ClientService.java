package neu.vansven.apiclientsdk.service;

import neu.vansven.apiclientsdk.configure.RestTemplateConfigure;
import neu.vansven.apiclientsdk.domain.Person;
import neu.vansven.apiclientsdk.utils.SignUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;



public class ClientService {

    private RestTemplateConfigure configure = new RestTemplateConfigure();
    private RestTemplate restTemplate = configure.getRestTemplate();

    /**
     * 前端提供非对称密钥中的公钥和私钥，但是前端发送请求只传递公钥，对发送的数据和私钥使用摘要算法进行签名得到hash值并添加到请求头中
     * 后端先根据公钥看是否能找到对应的私钥，再根据相同的数据和对应的私钥使用摘要算法进行验签得到hash值，与请求头中的签名进行对比
     */
    private String publicKey;
    private String privateKey;

    public ClientService(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public ResponseEntity<String> getNameByGet(String name){
        String url = "Http://localhost:8329/test/api1";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name",name);
        URI buildURI = builder.queryParams(map).build().encode().toUri(); // 拼接url后需要编码
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(buildURI, String.class);
        System.out.println("状态码 --> " + responseEntity.getStatusCode());
        System.out.println("请求头 --> " + responseEntity.getHeaders());
        System.out.println("响应数据 --> " + responseEntity.getBody());
        return responseEntity;
    }

    public ResponseEntity<Person> getEntityByPost(Person requestBody){
        restTemplate = new RestTemplate();
        String name = requestBody.getName();
        int age = requestBody.getAge();
        String gender = requestBody.getGender();
        String url = "http://localhost:8329/test/api2";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("nonce", RandomStringUtils.randomNumeric(10));
        httpHeaders.add("timestamp",String.valueOf(System.currentTimeMillis()));
        if(StringUtils.isAnyBlank(privateKey,publicKey)){
            throw new RuntimeException("POST请求必须输入一对密钥进行前后端签名认证");
        }
        httpHeaders.add("publickey",publicKey);
        httpHeaders.add("sign", SignUtil.genSign(requestBody.toString(),privateKey));
        // 构造请求报文 请求头 + 请求体
        HttpEntity<Person> httpEntity = new HttpEntity<>(requestBody,httpHeaders);
        ResponseEntity<Person> responseEntity = restTemplate.postForEntity(url,httpEntity,Person.class);
        System.out.println("状态码 --> " +responseEntity.getStatusCode());
        System.out.println("响应头 --> " +responseEntity.getHeaders());
        System.out.println("响应体 --> " +responseEntity.getBody());
        Person respnseBody = responseEntity.getBody();
        return responseEntity;
    }

}
