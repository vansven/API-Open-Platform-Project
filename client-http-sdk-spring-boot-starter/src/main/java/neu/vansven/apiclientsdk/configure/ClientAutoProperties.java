package neu.vansven.apiclientsdk.configure;

import lombok.Data;
import neu.vansven.apiclientsdk.service.ClientService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "neu.vansven.client")
@Configuration
@Data
public class ClientAutoProperties {

     private String publickey;

     private String privatekey;

     @Bean
     public ClientService clientService(){
          return new ClientService(publickey,privatekey);
     }

}
