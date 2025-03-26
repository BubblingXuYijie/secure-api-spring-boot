package icu.xuyijie.secureapi.cipher;


import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import icu.xuyijie.secureapi.model.SecureApiPropertiesConfig;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.security.KeyPair;
import java.util.Objects;

/**
 * RSA数据签名及数据加密
 */
public class RSASignatureUtils {

    private final Logger log = LoggerFactory.getLogger(RSASignatureUtils.class);

    private static SecureApiPropertiesConfig secureApiPropertiesConfig;

    // 改为实例方法而非静态方法，避免静态配置带来的潜在问题
    public RSASignatureUtils(SecureApiPropertiesConfig secureApiPropertiesConfig) {
        this.secureApiPropertiesConfig = Objects.requireNonNull(secureApiPropertiesConfig, "SecureApiPropertiesConfig cannot be null");
    }

    /**
     * RSA签名
     *
     * @param data 待签名数据
     * @return String 数字签名
     */
    public static String sign(byte[] data) {
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA512withRSA, Base64.decodeBase64(secureApiPropertiesConfig.getPrivateKey()), Base64.decodeBase64(secureApiPropertiesConfig.getPublicKey()));
        //签名
        byte[] signed = sign.sign(data);
        return Base64.encodeBase64String(signed);
    }

    public void generateKeyPair() {
        if (!StringUtils.hasText(secureApiPropertiesConfig.getSignPublicKey()) || !StringUtils.hasText(secureApiPropertiesConfig.getSignPrivateKey())) {
            KeyPair keyPair = KeyUtil.generateKeyPair(SignAlgorithm.SHA512withRSA.getValue());
            String publicKey = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
            String privateKey = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
            secureApiPropertiesConfig.setSignPublicKey(publicKey);
            secureApiPropertiesConfig.setSignPrivateKey(privateKey);
            log.info("\n您未配置数字签名密钥对，生成随机密钥对，请妥善保存\n公钥：{}\n私钥：{}", publicKey, privateKey);
        }
    }

    /**
     * RSA校验数字签名
     *
     * @param data   待校验数据
     * @param signed 数字签名
     * @return boolean 校验成功返回true，失败返回false
     */
    public boolean verify(byte[] data, byte[] signed) {
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA512withRSA, Base64.decodeBase64(secureApiPropertiesConfig.getSignPrivateKey()), Base64.decodeBase64(secureApiPropertiesConfig.getSignPublicKey()));
        boolean verify = sign.verify(data, signed);
        return verify;
    }


    /**
     * 测试方法
     */
/*    public static void main(String[] args) throws Exception {
        String dataToSign3 = "{\n" +
                "    \"createBy\": \"admin\",\n" +
                "    \"createTimeStart\": null,\n" +
                "    \"createTimeEnd\": null,\n" +
                "    \"createTime\": \"2025-03-19 08:33:14\",\n" +
                "    \"updateBy\": null,\n" +
                "    \"updateTime\": null,\n" +
                "    \"remark\": null,\n" +
                "    \"userId\": 33335,\n" +
                "    \"deptId\": null,\n" +
                "    \"userName\": \"zy\",\n" +
                "    \"nickName\": \"zy\",\n" +
                "    \"email\": \"\",\n" +
                "    \"phonenumber\": \"\",\n" +
                "    \"sex\": \"0\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"password\": \"\",\n" +
                "    \"status\": \"0\",\n" +
                "    \"delFlag\": \"0\",\n" +
                "    \"loginIp\": \"127.0.0.1\",\n" +
                "    \"loginDate\": \"2025-03-20 17:24:54\",\n" +
                "    \"pwdTime\": \"2025-03-12 17:22:24\",\n" +
                "    \"dept\": null,\n" +
                "    \"admin\": false,\n" +
                "    \"roles\": [\n" +
                "        {\n" +
                "            \"createBy\": null,\n" +
                "            \"createTimeStart\": null,\n" +
                "            \"createTimeEnd\": null,\n" +
                "            \"createTime\": null,\n" +
                "            \"updateBy\": null,\n" +
                "            \"updateTime\": null,\n" +
                "            \"remark\": null,\n" +
                "            \"roleId\": 2,\n" +
                "            \"roleName\": \"普通角色\",\n" +
                "            \"roleKey\": \"common\",\n" +
                "            \"roleSort\": 2,\n" +
                "            \"dataScope\": \"4\",\n" +
                "            \"menuCheckStrictly\": false,\n" +
                "            \"admin\": false,\n" +
                "            \"deptCheckStrictly\": false,\n" +
                "            \"status\": \"0\",\n" +
                "            \"delFlag\": null,\n" +
                "            \"flag\": false,\n" +
                "            \"menuIds\": null,\n" +
                "            \"deptIds\": null,\n" +
                "            \"permissions\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"createBy\": null,\n" +
                "            \"createTimeStart\": null,\n" +
                "            \"createTimeEnd\": null,\n" +
                "            \"createTime\": null,\n" +
                "            \"updateBy\": null,\n" +
                "            \"updateTime\": null,\n" +
                "            \"remark\": null,\n" +
                "            \"roleId\": 4,\n" +
                "            \"roleName\": \"运维班长\",\n" +
                "            \"roleKey\": \"admin,manager\",\n" +
                "            \"roleSort\": 2,\n" +
                "            \"dataScope\": \"1\",\n" +
                "            \"menuCheckStrictly\": false,\n" +
                "            \"admin\": false,\n" +
                "            \"deptCheckStrictly\": false,\n" +
                "            \"status\": \"0\",\n" +
                "            \"delFlag\": null,\n" +
                "            \"flag\": false,\n" +
                "            \"menuIds\": null,\n" +
                "            \"deptIds\": null,\n" +
                "            \"permissions\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"createBy\": null,\n" +
                "            \"createTimeStart\": null,\n" +
                "            \"createTimeEnd\": null,\n" +
                "            \"createTime\": null,\n" +
                "            \"updateBy\": null,\n" +
                "            \"updateTime\": null,\n" +
                "            \"remark\": null,\n" +
                "            \"roleId\": 5,\n" +
                "            \"roleName\": \"A1\",\n" +
                "            \"roleKey\": \"A1\",\n" +
                "            \"roleSort\": 3,\n" +
                "            \"dataScope\": \"1\",\n" +
                "            \"menuCheckStrictly\": false,\n" +
                "            \"admin\": false,\n" +
                "            \"deptCheckStrictly\": false,\n" +
                "            \"status\": \"0\",\n" +
                "            \"delFlag\": null,\n" +
                "            \"flag\": false,\n" +
                "            \"menuIds\": null,\n" +
                "            \"deptIds\": null,\n" +
                "            \"permissions\": null\n" +
                "        }\n" +
                "    ],\n" +
                "    \"roleIds\": [\n" +
                "        2,\n" +
                "        4,\n" +
                "        5\n" +
                "    ],\n" +
                "    \"postIds\": [],\n" +
                "    \"roleId\": null\n" +
                "}";
        //String signature = sign(dataToSign.getBytes());
        String signature = "uQkiAsA/IJobrw/bXfKHpJ4gX7I9p2g1HRGSRWZheEHsjHv6Leqqs1QLFSxMKPKJ3N6Ku0l+ara8xFDy01gzHNz2p8nGF+BN/ujVOkSk1rz2mW90V460CPvajP4EGnoUKla//8pbcdALetD5hfacY6a3eDhsB8CfM5yC5VfGnmY=";
        System.out.println("原始数据: " + dataToSign3);

        // 验证签名
        RSAEncryptionUtils rsa = new RSAEncryptionUtils();
        boolean isVerified = rsa.verify(dataToSign3.getBytes(), Base64.decodeBase64(signature));
        System.out.println("签名验证结果: " + isVerified);
    }*/
}