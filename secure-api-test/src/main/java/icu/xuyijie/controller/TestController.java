package icu.xuyijie.controller;

import icu.xuyijie.entity.ResultEntity;
import icu.xuyijie.secureapi.annotation.DecryptApi;
import icu.xuyijie.secureapi.annotation.EncryptApi;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 徐一杰
 * @date 2024/6/18 19:22
 * @description
 */
@RestController
@RequestMapping("secureApiTest")
@EncryptApi
@DecryptApi
public class TestController {
    @RequestMapping("/testResponse")
    public ResultEntity<String> testResponse() {
        return ResultEntity.success("哈哈哈");
    }

    @RequestMapping("/testRequest")
    public ResultEntity<String> testRequest(@RequestHeader String a, @RequestBody ResultEntity<String> resultEntity) {
        System.out.println("请求头正常显示：" + a);
        System.out.println(resultEntity);
        return ResultEntity.success("嘿嘿嘿");
    }
}
