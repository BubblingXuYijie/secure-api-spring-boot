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
        this.publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkLmPKDZnRO_oBgRe-xp4LudGJ-36EZnGzn4QAKgx_buGdDWGH9jvX6eK6wrm92QEEY_7hG-n32esKcuccfZ0qYI25XEMsm6Lz6Oe-gd9khRIAP7ciEnUCNovfJbbTuDCqA8XAAppssEiCOZ4TgEr8EoQ8cK_jrR0uu-mFANlXyr-cq5z4vjTLo1PT8FmWteOprwE2c7P5TdD5lBFAxmFA1CcylhfOfmrR4KEcO4XGxlBNhf0ZCoFykNXC87k0oNJtf-oyevoE_9ejeEJlPRH6qQsY9rKM1x4bYEZgPWeHt1WOhTzoTMDmalXYRwQlo-wQYYnJ6WUSoQffvfIMtF2RQIDAQAB";
        this.privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCQuY8oNmdE7-gGBF77Gngu50Yn7foRmcbOfhAAqDH9u4Z0NYYf2O9fp4rrCub3ZAQRj_uEb6ffZ6wpy5xx9nSpgjblcQyybovPo576B32SFEgA_tyISdQI2i98lttO4MKoDxcACmmywSII5nhOASvwShDxwr-OtHS676YUA2VfKv5yrnPi-NMujU9PwWZa146mvATZzs_lN0PmUEUDGYUDUJzKWF85-atHgoRw7hcbGUE2F_RkKgXKQ1cLzuTSg0m1_6jJ6-gT_16N4QmU9EfqpCxj2sozXHhtgRmA9Z4e3VY6FPOhMwOZqVdhHBCWj7BBhicnpZRKhB9-98gy0XZFAgMBAAECggEAFKUz49Om9bGXkswq1H1ZKa-6VVXgd4RNVQWsuLtFrmGC867y_DYRJ78Z1R_QBPiSkALPKXxYIUmDyM6P-b3jtI6r0B1JZ2bXsP4xXo-U_TLBdsrkoNiPU9f0MDzA-kab3ieQ62OQ4nwko8pk3RPTtcqLEkHq26uof0ZSlcZKPsIGiTz-8CAxRW0r6vwTeyxvIbinXfN_ODvSHgbCKoB8VT-ns-ACNng5UHI5CQOT_4Eug_K-W12ZRrctYUvdzqsjT8a5vDAFiOzJFdpVeT-cpUNFdmwEuX4raPgQXCjmmno4paPZB-a2I8WM5CwyOhCHYnvi1i0nIyfkmiu2EEwDHQKBgQDmzJC4eLlIw16SsAPHksRsWMSvQLRr0aKAhWAOvehP997DxT75ROH8gmFgK_wM29UJS-jRDKT1jsUU79oTZN2MUqLHzq3EXsTFvkfXS0L0LQdqnsroAE6clO4gSAPCBUKyzWUqw7QiixY1h9XDvGAZM-jRRWeucZegOS9bK5U38wKBgQCghv6z6KuN4Rt6N4XNPjSkVx4pFbKbLXvj4tFDrj3OxycfNuOBw9i8amVrtD0tdXqnjNRpPyaFZNscK9veo7PCnt08B-1zVofz1zMF4nw8Me-YyVIQNzEaVE2auAw1VF8290GpKbxhHLZwCJ3i39xcrFEX8U1xTGw-X8KrS8ye5wKBgCH76Stgi1fNfLV4uXW_hLC3eW6cxnRBz_3Apd5F1FlCFEkbWCR4IhZ-VDxG_bwe-oV96fAuPnPx688jaicE6SJqYJGvOyL5GVxMomNvL5KotrjaXOJAeVLREycps3P0NlgBqm3bl0Cm3kRPlghWSogrfpKh95xWGY98HB7-keBFAoGBAJBW0c6S9y6rqjlKk1TpCgx7CPOIPwCo9S0DVYa1tX2oNNUsVYToxIkmYetmNVwath7R0myQC-MFvL6n8RtPtuLYVbUUq715oOTwK0CvVWYKwJVw8AkEl3JFO0JTGycYpkSWLsQccvYfCyvRk6XcBBiQGJqerjOhs_KhyDid_28hAoGBANXLd--ZxP7sT5AzWDfea6DH4FN10_w8gCI_6TSDCnG9mn8EW_qGyN8oufXccF-vheUFWXnovrivrZTDORpB2s9XywgcFdSCC8yRfNP21KODjTWG7XQ-yoMXOPJWBaqusgUswbTcXwm6Jb_OYRSuZ4NxuddMVbu7ODsjP5jJkczO";
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
