package cn.itcast.dubbo.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.itcast.dubbo.pojo.User;

public class UserServiceTest {

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        // 完成UserService的初始化
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "classpath:dubbo/dubbo-*.xml");
        this.userService = applicationContext.getBean(UserService.class);
    }

    @Test
    public void testQueryAll() {
        for (int i = 0; i < 500; i++) {
            List<User> list = this.userService.queryAll();
            for (User user : list) {
                System.out.println(user);
            }

            try {
                Thread.sleep(i * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
