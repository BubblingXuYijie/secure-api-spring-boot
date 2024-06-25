package icu.xuyijie.controller;

import icu.xuyijie.entity.ResultEntity;
import icu.xuyijie.secureapi.annotation.DecryptApi;
import icu.xuyijie.secureapi.annotation.DecryptParam;
import icu.xuyijie.secureapi.annotation.EncryptApi;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * @author 徐一杰
 * @date 2024/6/18 19:22
 * @description
 */
@RestController
@RequestMapping("secureApiTest")
public class TestController {
    @RequestMapping("/testForm")
    public void testForm(User user, Integer age) {
        System.out.println(user);
        System.out.println(age);
    }

    @RequestMapping("/testParam")
    public void testParam(@RequestHeader(required = false) String a, @RequestParam(defaultValue = "0") String id, @DecryptParam(name = "name", defaultValue = "徐一杰") Map<String, String> name, @DecryptParam(defaultValue = "1,2,2") Set<String> age) {
        System.out.println(a);
        System.out.println(id);
        System.out.println(name);
        System.out.println(name.get("b"));
        System.out.println(age);
    }

    @RequestMapping("/testResponse")
    public ResultEntity<String> testResponse() {
        return ResultEntity.success("哈哈哈");
    }

    @RequestMapping("/testRequest")
    @DecryptApi
    @EncryptApi
    public ResultEntity<String> testRequest(@RequestHeader String a, @RequestBody ResultEntity<String> resultEntity) {
        System.out.println("请求头正常显示：" + a);
        System.out.println(resultEntity);
        return ResultEntity.success("嘿嘿嘿");
    }
}
