package com.clj.demo.contorller;

import com.clj.demo.dto.BaseResponse;
import com.clj.demo.entity.FileData;
import com.clj.demo.entity.User;
import com.clj.demo.services.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/api/mail")
@Api(tags = "MailController")
public class MailController {

    @Autowired
    private MailService mailService;

    @ApiOperation(value = "获取注册验证码")
    @PostMapping(path = "/registerCode")
    @ResponseBody
    public BaseResponse sendRegisterCode(@RequestParam String email) {
        return mailService.sendRegisterCode(email);
    }


    @ApiOperation(value = "获取重置密码验证码")
    @PostMapping(path = "/resetPasswordCode")
    @ResponseBody
    public BaseResponse sendResetPasswordCode(@RequestParam String email) {
        return mailService.sendResetPasswordCode(email);
    }

    @ApiOperation(value = "通过邮件分享文章")
    @PostMapping(path = "/sendShareFilesData")
    @ResponseBody
    public BaseResponse sendShareFilesData(@RequestParam String email, @RequestParam User user, @RequestParam String cipher, @RequestParam FileData fileData) {
        return mailService.sendShareFilesData(email,user,cipher,fileData);
    }
}
