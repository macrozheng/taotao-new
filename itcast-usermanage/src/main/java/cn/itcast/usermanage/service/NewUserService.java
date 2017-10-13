package cn.itcast.usermanage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.usermanage.bean.EasyUIResult;
import cn.itcast.usermanage.mapper.NewUserMapper;
import cn.itcast.usermanage.pojo.User;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class NewUserService {

    @Autowired
    private NewUserMapper newUserMapper;

    public EasyUIResult queryUserList(Integer page, Integer rows) {
        // 使用分页助手，设置分页条件
        PageHelper.startPage(page, rows);

        List<User> users = this.newUserMapper.select(null);
        PageInfo<User> pageInfo = new PageInfo<User>(users);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

    public User queryUserById(Long userId) {
        return this.newUserMapper.selectByPrimaryKey(userId);
    }

    public Integer saveUser(User user) {
        return this.newUserMapper.insertSelective(user);
    }

    public int updateUser(User user) {
        return this.newUserMapper.updateByPrimaryKeySelective(user);
    }

    public int deleteUser(List<Object> values) {
        Example example = new Example(User.class);
        example.createCriteria().andIn("id", values);
        return this.newUserMapper.deleteByExample(example);
    }

}
