package com.jzli.controller;

import com.alibaba.fastjson.JSONObject;
import com.jzli.service.ZKService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * =======================================================
 *
 * @Company 金色家网络科技有限公司-云存储业务部
 * @Date ：2017/5/25
 * @Author ：li.jinzhao
 * @Version ：0.0.1
 * @Description ：
 * ========================================================
 */
@RestController
@RequestMapping("/zooKeeper")
@Api(value = "/zooKeeper", description = "zooKeeper接口")
public class ZKController {
    @Autowired
    private ZKService zkService;

    @RequestMapping(value = "/increase{path}", method = RequestMethod.GET)
    @ApiOperation(value = "increase", httpMethod = "GET", response = Boolean.class)
    public String increase(@ApiParam(required = true, name = "path", value = "路径") @PathVariable("path") String path) {
        return zkService.increase(path);
    }

    @RequestMapping(value = "/lock{path}", method = RequestMethod.GET)
    @ApiOperation(value = "lock", httpMethod = "GET", response = Boolean.class)
    public Boolean lock(@ApiParam(required = true, name = "path", value = "路径") @PathVariable("path") String path) {
        return zkService.lock(path);
    }


    @RequestMapping(value = "/unlock{path}", method = RequestMethod.GET)
    @ApiOperation(value = "unlock", httpMethod = "GET", response = Boolean.class)
    public Boolean unlock(@ApiParam(required = true, name = "path", value = "路径") @PathVariable("path") String path) {
        return zkService.unlock(path);
    }

    @RequestMapping(value = "/get{path}", method = RequestMethod.GET)
    @ApiOperation(value = "get", httpMethod = "GET", response = String.class)
    public String get(@ApiParam(required = true, name = "get", value = "路径") @PathVariable("path") String path) {
        return zkService.get(path);
    }

    @RequestMapping(value = "/exists{path}", method = RequestMethod.GET)
    @ApiOperation(value = "exists", httpMethod = "GET", response = String.class)
    public Boolean exists(@ApiParam(required = true, name = "get", value = "路径") @PathVariable("path") String path) {
        return zkService.exists(path);
    }

    @RequestMapping(value = "/set", method = RequestMethod.POST)
    @ApiOperation(value = "set", httpMethod = "POST", response = Boolean.class)
    public Boolean set(@ApiParam(required = true, name = "json", value = "json对象") @RequestBody JSONObject jsonObject) {
        return zkService.set(jsonObject);
    }

    @RequestMapping(value = "/watch{path}", method = RequestMethod.GET)
    @ApiOperation(value = "watch", httpMethod = "GET", response = String.class)
    public Boolean watch(@ApiParam(required = true, name = "get", value = "路径") @PathVariable("path") String path) {
        return zkService.exists(path);
    }

}
