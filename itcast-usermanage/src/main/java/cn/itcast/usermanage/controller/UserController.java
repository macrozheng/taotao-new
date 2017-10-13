package cn.itcast.usermanage.controller;

import cn.itcast.usermanage.bean.EasyUIResult;
import cn.itcast.usermanage.pojo.User;
import cn.itcast.usermanage.service.NewUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    private NewUserService userService;

    /**
     * 查询用户列表
     * 
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public EasyUIResult queryUserList(@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows) {
        return this.userService.queryUserList(page, rows);
    }

    @RequestMapping(value = "delete",method = RequestMethod.POST)
    public ResponseEntity<Void> deleteUser(@RequestParam("ids") List<Object> values) {
        try {
            int count = this.userService.deleteUser(values);
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

    @RequestMapping(value = "save",method = RequestMethod.POST)
    public ResponseEntity<Void> saveUser(User user) {
        try {
            int count = this.userService.saveUser(user);
            if(count==0){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
