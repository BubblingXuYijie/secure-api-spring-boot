package icu.xuyijie.secureapi.cipher;

/**
 * @author 徐一杰
 * @date 2024/6/18 15:59
 * @description RSA密钥对
 */
public class RsaKeyPair {
    private String publicKey;
    private String privateKey;

    public RsaKeyPair() {
        this.publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCz38IRoDGAQvaFpbZ4l6zAzso7cjzASR6lfW75OvbjmeYrUBeuxVUzWM/Yy7wGPMA7nWvklCnR5FFg+KBfwEz4Kw0GNoEht3RC/biUerfIlAy50f6zwcaIzgXI3TXwa5mdL/zmkdiHkFj+Vr/CwRPJa0QLfp8ETNe5GrqUmOxKtwIDAQAB";
        this.privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALPfwhGgMYBC9oWltniXrMDOyjtyPMBJHqV9bvk69uOZ5itQF67FVTNYz9jLvAY8wDuda+SUKdHkUWD4oF/ATPgrDQY2gSG3dEL9uJR6t8iUDLnR/rPBxojOBcjdNfBrmZ0v/OaR2IeQWP5Wv8LBE8lrRAt+nwRM17kaupSY7Eq3AgMBAAECgYBHsYxvLQv95PLQcyvnJgFPujyF9DL9q0dBS90TDqsALcO9iMdfvDyI4kG51f6j98vhFw49hcajZ57+CSsW7xVd+sihD+4WxoSFgX6jIWNGHrsS/FDEpDuoZMEZBM6UmVi9ZByhuJo+HTR2biqQlZl+gR3+bLSYG5NFOqiOo16yiQJBAL8a6eOZHb96cEFPJZxM6TFUt0pEbnlhIieqywG3OYickfbYug0HXQYtCXxKlD8Vt8VAuYs/sN6SYN95cWnEtN8CQQDw9IIiX5tzo4VhsYX1TV+ah6hfddrSTwVHfPJ66VfbtVeUSM3rKFpYrJv7gm3mV6mrpzEDvmGie8SYPghgRA0pAkEAiIVPhPpDWCC9xMon0irXhBhDAFk2mpubbL8EW2trPH6tf6x/7QLPn7PYzOPyV8bKC64bXrDXR4lGjx8QuEB//QJBAN2eporjEOG9hxKh6yoB7mr9VxnmFkvVLm/gNk5ijNKh6lmS1QLzbJWevSEJh/eSrZYaAvUEiFygJeQ5Og8AyHECQQCNQF3P6zRGFsWxS7I2d0y1vzfUuKnxT2jeBAJF/7udSbtkTDWY35ogY7SDQG4sCTYbOSklt2ziE6gPDt3vE1oC";
    }

    public RsaKeyPair(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

}
