package com.taotao.sso.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Boolean check(String param, Integer type) {
        User record = new User();
        switch (type) {
        case 1:
            record.setUsername(param);
            break;
        case 2:
            record.setPhone(param);
            break;
        case 3:
            record.setEmail(param);
            break;
        default:
            return null;
        }
        return this.userMapper.selectOne(record) == null;
    }

    public Boolean saveUser(User user) {
        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());

        // 密码加密处理，使用MD5
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));

        return this.userMapper.insert(user) == 1;
    }

    public String doLogin(String username, String password) throws Exception {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (null == user) {
            // 用户不存在
            return null;
        }
        // 判断密码是否相当
        if (!StringUtils.equals(DigestUtils.md5Hex(password), user.getPassword())) {
            return null;
        }

        // 登录成功
        // 生成token
        String token = DigestUtils.md5Hex(System.currentTimeMillis() + username);

        // 将用户数据保存到Redis中
        this.redisService.set("TOKEN_" + token, MAPPER.writeValueAsString(user), 1800);

        return token;
    }

    public User queryUserByToken(String token) throws Exception {
        String key = "TOKEN_" + token;
        String jsonData = this.redisService.get(key);
        if (null == jsonData) {
            // 登录超时
            return null;
        }
        // 重新设置redis中的时间,非常重要
        this.redisService.expire(key, 1800);
        return MAPPER.readValue(jsonData, User.class);
    }

}
