package com.clj.demo.contorller;

import com.clj.demo.dto.UserResponse;
import com.clj.demo.entity.User;
import com.clj.demo.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping(path="/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "显示所有用户信息",notes = "管理员权限")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/getAllUsers")
    @ResponseBody
    public UserResponse<Iterable<User>> getAllUsers(){
        return userService.getAllUsers();
    }

    @ApiOperation(value = "查询某个用户信息",notes = "管理员权限")
    @GetMapping(path="/getUser")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public UserResponse<User> getUser(@RequestParam String email){
        return userService.findUserByEmail(email);
    }

    @ApiOperation(value = "更新用户信息")
    @PutMapping(path="/updateUser")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UserResponse<User> updateUser(@RequestParam String number,
                                         @RequestParam String username) {
        return userService.updateUser(number,username);
    }

    @ApiOperation(value = "删除某个用户信息",notes = "管理员权限")
    @DeleteMapping(path = "/deleteUser")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse<User> deleteUser(@RequestParam int id) {
        return userService.deleteUser(id);
    }

}