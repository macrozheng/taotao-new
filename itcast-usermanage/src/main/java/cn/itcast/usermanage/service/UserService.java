package cn.itcast.usermanage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.itcast.usermanage.bean.EasyUIResult;
import cn.itcast.usermanage.mapper.UserMapper;
import cn.itcast.usermanage.pojo.User;

/**
 * @deprecated
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public EasyUIResult queryUserList(Integer page, Integer rows) {
        // 使用分页助手，设置分页条件
        PageHelper.startPage(page, rows);

        List<User> users = this.userMapper.queryUserList();
        PageInfo<User> pageInfo = new PageInfo<User>(users);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }
}
