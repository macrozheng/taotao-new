package cn.itcast.usermanage.mapper;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.abel533.entity.Example;

import cn.itcast.usermanage.pojo.User;

public class NewUserMapperTest {

    private NewUserMapper newUserMapper;

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "classpath:spring/applicationContext*.xml");
        this.newUserMapper = applicationContext.getBean(NewUserMapper.class);
    }

    @Test
    public void testSelectOne() {
        User record = new User();
        record.setuserName("zhangsan");
        User user = this.newUserMapper.selectOne(record);
        System.out.println(user);
    }

    @Test
    public void testSelect() {
        User record = new User();
        // 多个条件之间是AND关系
        // record.setuserName("zhangsan");
        // record.setPassword("123456");
        record.setSex(1);
        List<User> list = this.newUserMapper.select(record);
        for (User user : list) {
            System.out.println(user);
        }
    }

    @Test
    public void testSelectCount() {
        System.out.println(this.newUserMapper.selectCount(null));
    }

    @Test
    public void testSelectByPrimaryKey() {
        User user = this.newUserMapper.selectByPrimaryKey(1L);
        System.out.println(user);
    }

    @Test
    public void testInsert() {
        User record = new User();
        // record.setAge(20);
        record.setBirthday(new java.sql.Date(new Date().getTime()));
        record.setCreated(new Date());
        // record.setUpdated(new Date());
        record.setName("name_4");
        record.setPassword("20");
        // record.setSex(1);
        record.setuserName("user_name_4");
        // SQL:INSERT INTO tb_user (CREATED,BIRTHDAY,ID,SEX,NAME,AGE,UPDATED,USER_NAME,PASSWORD)
        // VALUES ( ?,?,?,?,?,?,?,?,? )
        // 将对象中的所有属性都当做是SQL语句中字段执行
        this.newUserMapper.insert(record);

        System.out.println(record.getId());
    }

    @Test
    public void testInsertSelective() {
        User record = new User();
        // record.setAge(20);
        record.setBirthday(new java.sql.Date(new Date().getTime()));
        record.setCreated(new Date());
        // record.setUpdated(new Date());
        record.setName("name_3");
        record.setPassword("20");
        // record.setSex(1);
        record.setuserName("user_name_3");

        // INSERT INTO tb_user ( NAME,BIRTHDAY,ID,CREATED,USER_NAME,PASSWORD ) VALUES ( ?,?,?,?,?,?
        // )
        // Selective：将不为null的属性作为SQL语句中字段
        this.newUserMapper.insertSelective(record);
        System.out.println(record.getId());
    }

    @Test
    public void testDelete() {
        fail("Not yet implemented");
    }

    @Test
    public void testDeleteByPrimaryKey() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateByPrimaryKey() {
        // 更新所有的字段，更新条件是:主键
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        // 更新不为null的字段，更新条件是:主键
        fail("Not yet implemented");
    }

    @Test
    public void testSelectCountByExample() {
        fail("Not yet implemented");
    }

    @Test
    public void testDeleteByExample() {
        fail("Not yet implemented");
    }

    @Test
    public void testSelectByExample() {

        Example example = new Example(User.class);
        List<Object> values = new ArrayList<Object>();
        values.add(1L);
        values.add(2L);
        values.add(3L);
        //设置查询条件
        example.createCriteria().andIn("id", values);
        
        //设置排序条件
        example.setOrderByClause("created DESC");
        List<User> list = this.newUserMapper.selectByExample(example);
        for (User user : list) {
            System.out.println(user);
        }
    }

    @Test
    public void testUpdateByExampleSelective() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateByExample() {
        fail("Not yet implemented");
    }

}
