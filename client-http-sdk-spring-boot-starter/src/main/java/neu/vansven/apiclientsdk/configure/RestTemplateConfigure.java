package neu.vansven.apiclientsdk.configure;


import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateConfigure {
    /**
     *  配置 RestTemplate
     * @return
     */
    public RestTemplate getRestTemplate() {
        // 创建一个 httpCilent 简单工厂
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 设置连接超时
        factory.setConnectTimeout(15000);
        // 设置读取超时
        factory.setReadTimeout(5000);
        return new RestTemplate(factory);
    }
}
