package com.jzli.controller;

import com.jzli.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * =======================================================
 *
 * @Company 金色家网络科技有限公司-开发测试云服务部
 * @Date ：2016/7/6
 * @Author ：li.jinzhao
 * @Version ：0.0.1
 * @Description ：
 * ========================================================
 */
@RestController
@Api(value = "用户接口", description = "用户接口")
public class UserController {
    private AtomicInteger counter = new AtomicInteger();

    @RequestMapping(value = "/user/{name}", method = RequestMethod.GET)
    @ApiOperation(value = "添加用户", httpMethod = "GET", response = User.class)
    public User getUser(@ApiParam(required = true, name = "name", value = "用户名") @PathVariable("name") String name) {
        return new User(counter.getAndIncrement(), name);
    }
}
