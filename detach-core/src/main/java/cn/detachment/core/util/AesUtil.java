package cn.detachment.core.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/6/5 23:55
 */
public class AesUtil {

    private static final String AES = "AES";


    /**
     * 加密
     *
     * @param str str
     * @param key key
     * @return str
     * @throws Exception exception
     */
    public static String encrypt(String str, String key) throws Exception {
        if (str == null || key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,
                new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES));
        byte[] bytes = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 解密
     *
     * @param str str
     * @param key key
     * @return string
     * @throws Exception exception
     */
    public static String decrypt(String str, String key) throws Exception {
        if (str == null || key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES));
        byte[] bytes = Base64.getDecoder().decode(str);
        bytes = cipher.doFinal(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }


}
