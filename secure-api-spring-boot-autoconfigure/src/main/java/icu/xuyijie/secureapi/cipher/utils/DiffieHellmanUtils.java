package icu.xuyijie.secureapi.cipher.utils;

import icu.xuyijie.secureapi.exception.ErrorEnum;
import icu.xuyijie.secureapi.exception.SecureApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyAgreement;
import java.security.*;
import java.util.Base64;

/**
 * @author 徐一杰
 * @date 2024/6/20 11:08
 * @description DH密钥协商算法
 */
public class DiffieHellmanUtils {
    private DiffieHellmanUtils() {

    }

    private static final Logger log = LoggerFactory.getLogger(DiffieHellmanUtils.class);

    /**
     * 生成DH算法密钥对
     * @return DH算法密钥对
     */
    public static KeyPair getDhKeyPairs() {
        // 服务端生成DH密钥对，并把公钥发送给客户端
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
            keyPairGen.initialize(2048);
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("获取DH密钥对失败", e);
            throw new SecureApiException(ErrorEnum.KEY_CREATE_ERROR);
        }
    }

    /**
     * DH密钥协商算法得出共享密钥
     * @param serverPrivateKey 服务端私钥
     * @param clientPublicKey 客户端公钥
     * @return 共享密钥
     */
    public static String createSharedSecret(PrivateKey serverPrivateKey, PublicKey clientPublicKey) {
        try {
            // 服务端使用自己的私钥创建并初始化Key协议对象
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(serverPrivateKey);
            // 服务端根据自己的私钥和客户端公钥生成共享密钥
            keyAgreement.doPhase(clientPublicKey, true);
            byte[] sharedSecret = keyAgreement.generateSecret();
            return Base64.getUrlEncoder().encodeToString(sharedSecret);
        } catch (NoSuchAlgorithmException e) {
            log.error("获取DH协议对象失败", e);
        } catch (InvalidKeyException e) {
            log.error("传入的DH公钥或私钥错误", e);
        }
        throw new SecureApiException(ErrorEnum.KEY_ERROR);
    }

//    public static void main(String[] args) {
//        // 服务端生成DH密钥对，并把公钥发送给客户端
//        KeyPair serverKeyPair = getDhKeyPairs();
//        // 客户端生成密钥对，并把公钥发送给服务端，本来应该是根据服务端公钥携带的信息进行运算，但是信息中p和g是rfc3526定死的，大家都用固定的p和g，算出来的共享密钥就是一样的，所以这里不需要获取服务端公钥参数也可以
//        //DHParameterSpec dhParams = ((DHPublicKey) serverKeyPair.getPublic()).getParams()
//        //keyPairGen.initialize(dhParams)
//        KeyPair clientKeyPair = getDhKeyPairs();
//
//        // 服务端使用自己的私钥创建并初始化Key协议对象
//        String serverSharedSecretString = createSharedSecret(serverKeyPair.getPrivate(), clientKeyPair.getPublic());
//        // 客户端根据自己的私钥和服务端公钥生成共享密钥
//        String clientSharedSecretString = createSharedSecret(clientKeyPair.getPrivate(), serverKeyPair.getPublic());
//        System.out.println("服务端共享密钥：" + serverSharedSecretString);
//        System.out.println("客户端共享密钥：" + clientSharedSecretString);
//        System.out.println("是否相同：" + clientSharedSecretString.equals(serverSharedSecretString));
//
//        // 使用共享密钥加密
//        String content = "Hello, 你 好/、\\！";
//        CipherUtils cipherUtils = new CipherUtils(CipherAlgorithmEnum.AES_ECB_PKCS5);
//        String encrypt = cipherUtils.encrypt(content, serverSharedSecretString);
//        System.out.println("加密后：" + encrypt);
//        System.out.println("解密后：" + encrypt);
//    }

}
