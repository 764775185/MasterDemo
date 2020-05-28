package com.clj.demo.contorller;

import com.clj.demo.entity.User;
import com.clj.demo.services.RedisService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path="/api/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;
    @ApiOperation(value = "缓存用户信息（username,user）")
    @PostMapping(path = "/saveUser")
    @ResponseBody
    public String saveUser(@RequestBody User user){
        redisService.set(user.getUsername(), user);
        return "success";
    }

    @ApiOperation(value = "缓存限时用户信息（username,user,time）")
    @PostMapping(path = "/saveUserTime")
    @ResponseBody
    public String TestUser(@RequestBody User user, @RequestParam Integer time){
        redisService.set_timeout(user.getUsername(), user, time);
        return "success";
    }

    @ApiOperation(value = "读取用户信息")
    @GetMapping(path = "/getUser")
    @ResponseBody
    public Object getUser(@RequestParam String username){
        return redisService.get(username);
    }

    @ApiOperation(value = "获取key-value值")
    @GetMapping(path = "/getValue")
    @ResponseBody
    public Object getValue(@RequestParam String key) {
        return redisService.get(key);
    }

    @ApiOperation(value = "清空redis缓存")
    @GetMapping(path = "/clear")
    @ResponseBody
    public Map<String, Object> clearRedis() {
        Map<String, Object> result = new HashMap<>();
        try{
            redisService.clearRedis();
        }catch (Exception e){
            result = redisService.setStatus("失败");
            redisService.set("lastClearRedisStatus",result);
            return result;
        }
        redisService.setStatus("成功");
        redisService.set("lastClearRedisStatus",result);
        return result;
    }
}