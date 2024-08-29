package neu.vansven.service;

import neu.vansven.entity.UserInfo;

public interface UserRegisterService {
    UserInfo isHaveUser(String publicKey);
}
