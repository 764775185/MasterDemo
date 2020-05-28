package com.clj.demo.services;

import com.clj.demo.dto.BaseResponse;
import com.clj.demo.dto.UserResponse;
import com.clj.demo.entity.User;
import com.clj.demo.exception.AuthorizationException;
import com.clj.demo.exception.OperateException;
import com.clj.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("AuthService")
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CurrentUserInfoService currentUserInfoService;

    public UserResponse<User> readMe(){
        User user = currentUserInfoService.getUserInfo();
        return new UserResponse<>(200,"当前用户数据获取成功！", user);
    }

    public UserResponse<User> register(String number, String username,  String email, String password, String role,  String pkey, String registerCode) {
        String code = redisService.get("Register_"+ email).toString();
        if (code == null || !code.equals(registerCode)){
            throw new OperateException("验证码错误！");
        }
        if (role.equals("ADMIN")){
            throw new OperateException("该接口无法注册管理员！");
        }
        Optional<User> u = userRepository.findUserByEmail(email);
        if (u.isPresent()) {
            throw new OperateException("邮箱已存在!");
        }
        else {
            User n = new User(number, username, email, passwordEncoder.encode(password), role, pkey);
            userRepository.save(n);
            return new UserResponse<>(200, "添加用户成功！", n);
        }
    }

    public UserResponse<User> register(User user, String registerCode) {
        String code = redisService.get("Register_"+ user.getEmail()).toString();
        if (code == null || !code.equals(registerCode)){
            throw new OperateException("验证码错误！");
        }
        if (user.getRole().equals("ADMIN")){
            throw new OperateException("该接口无法注册管理员！");
        }
        Optional<User> u = userRepository.findUserByEmail(user.getEmail());
        if (u.isPresent()) {
            throw new OperateException("邮箱已存在!");
        }
        else {
            User n = new User(user.getNumber(), user.getUsername(), user.getEmail(), passwordEncoder.encode(user.getPassword()), user.getRole(), user.getPkey());
            userRepository.save(n);
            return new UserResponse<>(200, "添加用户成功！", n);
        }
    }

    public UserResponse<User> resetUserPassword(String email, String password, String resetPasswordCode) {
        String code = (String) redisService.get("ResetPassword_"+email);
        if (code == null || !code.equals(resetPasswordCode)){
            throw new OperateException("验证码错误！");
        }
        Optional<User> u = userRepository.findUserByEmail(email);
        if (!u.isPresent()){
            throw new OperateException("用户不存在！");
        }
        else {
            u.get().setPassword(passwordEncoder.encode(password));
            userRepository.save(u.get());
            if (redisService.get("Authentication_" + email) != null) {
                redisService.clearRedisByKey("Authentication_" + email);
            }
            redisService.clearRedisByKey("ResetPassword_" + email);
            return new UserResponse<>(200, "密码重置成功！", u.get());
        }
    }

    public BaseResponse logout(){
        User u = currentUserInfoService.getUserInfo();
        if (u.getEmail() == null || redisService.get("Authentication_"+u.getEmail()) == null){
            throw new OperateException("您未登陆，或者登陆已失效!");
        }
        redisService.clearRedisByKey("Authentication_"+u.getEmail());
        return new BaseResponse(200,"您已退出登陆!");
    }

    public BaseResponse changePermissions(String email, String role){
        Optional<User> u = userRepository.findUserByEmail(email);
        if (!u.isPresent()){
            throw new OperateException("用户不存在!");
        }
        if (u.get().getRole().equals("ADMIN") || role.equals("ADMIN")){
            throw new AuthorizationException("无法更改管理员相关权限!");
        }
        if (redisService.get("Authentication_"+u.get().getEmail()) != null){
            redisService.clearRedisByKey("Authentication_"+u.get().getEmail());
        }
        u.get().setRole(role);
        userRepository.save(u.get());
        return new BaseResponse(200,"该用户已更改至"+role+"权限!");
    }
}
