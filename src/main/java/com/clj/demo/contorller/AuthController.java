package com.clj.demo.contorller;

import com.clj.demo.dto.BaseResponse;
import com.clj.demo.dto.UserResponse;
import com.clj.demo.entity.User;
import com.clj.demo.model.LoginUser;
import com.clj.demo.services.AuthService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @ApiOperation(value = "显示当前用户信息",notes = "当前用户权限")
    @GetMapping(path = "/myselfInfo")
    @ResponseBody
    public UserResponse<User> myselfInfo(){
        return authService.readMe();
    }

    @ApiOperation(value = "用户注册",notes = "除管理员外的用户注册")
    @PostMapping(path = "/register")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse<User> register(User user, String registerCode) {
        //return ""+number+username+registerCode;
        System.out.println(user.toString()+"code"+registerCode);
        return authService.register(user, registerCode);
    }

    @ApiOperation(value = "用户登录")
    @PostMapping(path = "/login")
    public String login(@RequestBody LoginUser loginUser){ return "登录成功！"; }


    @ApiOperation(value = "用户口令变更")
    @PostMapping(path = "/resetPassword")
    @ResponseBody
    public UserResponse<User> resetPassword(@RequestParam String email, @RequestParam String password, @RequestParam String resetPasswordCode) {
        return authService.resetUserPassword(email,password,resetPasswordCode);
    }

    @ApiOperation(value = "用户角色变更",notes = "管理员权限")
    @PostMapping(path = "/permissions")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse permissions(@RequestParam String email, @RequestParam String role){
        return authService.changePermissions(email,role);
    }

    @ApiOperation(value = "注销登录")
    @PostMapping(path = "/logout")
    @ResponseBody
    public BaseResponse logout(){
        return authService.logout();
    }
}