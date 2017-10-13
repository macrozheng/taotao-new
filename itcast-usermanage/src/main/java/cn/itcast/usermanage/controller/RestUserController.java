package cn.itcast.usermanage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.itcast.usermanage.pojo.User;
import cn.itcast.usermanage.service.NewUserService;

@RequestMapping("new/user")
@Controller
public class RestUserController {

    @Autowired
    private NewUserService newUserService;

    /**
     * 根据用户id查询用户数据
     * 
     * @param userId
     * @return
     */
    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public ResponseEntity<User> queryUserById(@PathVariable("userId") Long userId) {
        try {
            User user = this.newUserService.queryUserById(userId);
            if (null == user) {
                // 资源不存在，响应404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // 响应200
            return ResponseEntity.ok(user);
            // return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 响应500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 新增用户
     * 
     * @param user
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveUser(User user) {
        try {
            int count = this.newUserService.saveUser(user);
            if (count == 0) {
                // 插入失败，响应500
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            // 插入成功，响应
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 插入失败，响应500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUser(User user) {
        try {
            int count = this.newUserService.updateUser(user);
            if (count == 0) {
                // 插入失败，响应500
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            // 插入成功，响应204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 插入失败，响应500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@RequestParam("ids") List<Object> values) {
        try {
            int count = this.newUserService.deleteUser(values);
            if (count == 0) {
                // 插入失败，响应500
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            // 插入成功，响应204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 插入失败，响应500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
