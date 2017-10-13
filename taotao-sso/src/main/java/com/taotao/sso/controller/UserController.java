package com.taotao.sso.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.util.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    public static final String COOKIE_NAME = "TT_TOKEN";

    /**
     * 注册
     * 
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String toRegister() {
        return "register";
    }

    /**
     * 登录
     * 
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    /**
     * 检测数据是否可用
     * 
     * @param param
     * @param type
     * @return
     */
    @RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> check(@PathVariable("param") String param,
            @PathVariable("type") Integer type) {
        try {
            Boolean bool = this.userService.check(param, type);
            if (null == bool) {
                // 参数不合法
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            // 为了兼容前端JS逻辑，将返回值取反
            return ResponseEntity.ok(!bool);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 注册
     * 
     * @param user
     * @return
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> doRegister(@Valid User user, BindingResult bindingResult) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            // 数据校验
            if (bindingResult.hasErrors()) {
                List<ObjectError> allErrors = bindingResult.getAllErrors();
                List<String> msgs = new ArrayList<String>();
                for (ObjectError objectError : allErrors) {
                    msgs.add(objectError.getDefaultMessage());
                }
                result.put("data", StringUtils.join(msgs, '|'));
                result.put("status", "209");
                return ResponseEntity.ok(result);
            }

            Boolean bool = this.userService.saveUser(user);

            if (bool) {
                // 注册成功
                result.put("status", "200");
            } else {
                result.put("status", "208");
                result.put("data", "注册失败！请重新注册！");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> doLogin(@RequestParam("username") String username,
            @RequestParam("password") String password, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            String token = this.userService.doLogin(username, password);
            if (StringUtils.isEmpty(token)) {
                // 登录失败
                result.put("status", 208);
            } else {
                // 登录成功
                result.put("status", 200);
                // 将token写入到cookie中
                CookieUtils.setCookie(request, response, COOKIE_NAME, token);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 根据token查询用户数据
     * 
     * @param token
     * @return
     */
    @RequestMapping(value = "{token}", method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByToken(@PathVariable("token") String token) {
        try {
            User user = this.userService.queryUserByToken(token);
            if(null == user){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } 
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
