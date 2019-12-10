package com.zhouyuan.saas.ihrm.ihrm_system;

import com.ihrm.system.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 用户service测试类
 * @author: yuand
 * @create: 2019-12-10 09:34
 **/
public class UserServiceTest extends IhrmSystemApplicationTests {

    @Autowired
    UserService userService;

    @Test
    public void assignRoles(){

        String userId = "1063705989926227968";
        List<String> roles = Arrays.asList("1064098829009293312","1064098935443951616");
        userService.assignRoles(userId,roles);
    }
}
