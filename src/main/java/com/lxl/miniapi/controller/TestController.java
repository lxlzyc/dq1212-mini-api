package com.lxl.miniapi.controller;

import com.lxl.miniapi.Result;
import com.lxl.miniapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @author: lxl
 */
@RestController
public class TestController {

    private Logger logger = LoggerFactory.getLogger(TestController.class);


    @Autowired
    UserService userService;

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String test() {
        return "服务器回调成功";
    }

    @RequestMapping(path = "/heart", method = RequestMethod.GET)
    public Result heart() {
        return new Result(true);
    }

    @RequestMapping(path = "/test/jdbc", method = RequestMethod.GET)
    public String testJDBC() {
        logger.info("测试 jdbc ");
        return "数据库测试---> first user nickname is " + userService.getFirstUserNickName();
    }

}
