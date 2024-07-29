package icu.xuyijie.controller;

import icu.xuyijie.entity.ResultEntity;
import icu.xuyijie.entity.User;
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
    public int testForm(User user, @DecryptParam Integer age, @DecryptParam Map<String, String> map, @DecryptParam(required = false) Set<String> set) {
        System.out.println(user);
        System.out.println(user.getName());
        System.out.println(age);
        System.out.println(map);
        System.out.println(set);
        return age;
    }

    @RequestMapping("/testParam")
    public void testParam(@RequestHeader(required = false) String a, @RequestParam(defaultValue = "0") String id, @DecryptParam(required = false) Map<String, String> map, @DecryptParam(defaultValue = "1,2,2") Set<String> set) {
        System.out.println(a);
        System.out.println(id);
        System.out.println(map);
        System.out.println(map.get("b"));
        System.out.println(set);
    }

    @RequestMapping("/testResponse")
    public ResultEntity<String> testResponse() {
        return ResultEntity.success("哈哈哈");
    }

    @RequestMapping("/testRequest")
    @DecryptApi
    @EncryptApi
    public ResultEntity<String> testRequest(@RequestBody ResultEntity<String> resultEntity) {
        System.out.println(resultEntity);
        return ResultEntity.success("嘿嘿嘿");
    }
}
