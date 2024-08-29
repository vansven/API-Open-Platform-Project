package neu.vansven.service;

import neu.vansven.entity.InterfaceInfo;

public interface InterfaceRegisterService {
    InterfaceInfo isHaveInterface(String url, String method);
}
