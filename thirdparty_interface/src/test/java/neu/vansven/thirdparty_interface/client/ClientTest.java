package neu.vansven.thirdparty_interface.client;

import neu.vansven.apiclientsdk.domain.Person;
import neu.vansven.apiclientsdk.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class ClientTest {

    @Autowired
    ClientService clientService;

    @Test
    void test(){
        ResponseEntity getEntity = clientService.getNameByGet("范思文");
        System.out.println(getEntity.getBody());
        ResponseEntity postEntity = clientService.getEntityByPost(new Person("夏青", 26, "女"));
        System.out.println(postEntity.getBody());
    }
}
