package com.clj.demo.services;

import com.clj.demo.dto.UserResponse;
import com.clj.demo.entity.User;
import com.clj.demo.exception.BaseUserException;
import com.clj.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserInfoService currentUserInfoService;


    public UserResponse<Iterable<User>> getAllUsers(){
        return new UserResponse<>(200,"数据获取成功！",userRepository.findAll());
    }

    public UserResponse<User> findUserByEmail (String email){
        Optional<User> user = userRepository.findUserByEmail(email);
        if(!user.isPresent()){
            throw new BaseUserException("用户不存在!");
        }
        return new UserResponse<>(200,"查找成功！",user.get());

    }

    public UserResponse<User> updateUser(String number, String username) {
        User u = currentUserInfoService.getUserInfo();
        u.setNumber(number);
        u.setUsername(username);
        userRepository.save(u);
        return new UserResponse<>(200,"用户信息更新成功！",u);
    }

    public UserResponse<User> deleteUser(int id) {
        Optional<User> u=userRepository.findById(id);
        if(u.isPresent()){
            userRepository.deleteById(id);
            return new UserResponse<>(200,"该用户信息已删除！",u.get());
        }
        throw new BaseUserException("该用户不存在！");
    }
}