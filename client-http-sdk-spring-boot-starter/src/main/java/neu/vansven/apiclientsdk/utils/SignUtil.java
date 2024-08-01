package neu.vansven.apiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class SignUtil {
    public static String genSign(String requestBody, String privateKey) {
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String content = requestBody + "." + privateKey;
        return md5.digestHex(content);
    }
}
