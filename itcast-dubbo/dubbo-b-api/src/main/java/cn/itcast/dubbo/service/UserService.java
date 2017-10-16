package cn.itcast.dubbo.service;

import java.util.List;

import cn.itcast.dubbo.pojo.User;

public interface UserService {

    /**
     * 查询所有的用户数据
     * 
     * @return
     */
    public List<User> queryAll();

}
