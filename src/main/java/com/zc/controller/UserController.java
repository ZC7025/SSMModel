package com.zc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 7025 on 2017/10/30.
 */
@Controller
//@RestController
@RequestMapping("/user")
public class UserController {

//    @RequestMapping("execute")
    @RequestMapping(value = "execute", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public void execute() {

    }
}
