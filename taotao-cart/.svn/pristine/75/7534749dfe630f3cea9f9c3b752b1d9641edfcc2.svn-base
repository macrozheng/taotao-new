package com.taotao.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.taotao.cart.bean.Item;
import com.taotao.cart.bean.User;
import com.taotao.cart.mapper.CartMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.threadlocal.UserThreadLocal;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ItemService itemService;

    public void addItemToCart(Long itemId) {

        // 判断该商品在购物车中是否存在
        User user = UserThreadLocal.get();
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(user.getId());
        Cart cart = this.cartMapper.selectOne(record);
        if (null == cart) {
            // 不存在，执行插入操作
            cart = new Cart();
            cart.setItemId(itemId);
            cart.setUserId(user.getId());
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setNum(1); // TODO
            Item item = this.itemService.queryItemById(itemId);
            cart.setItemImage(item.getImages()[0]);
            cart.setItemPrice(item.getPrice());
            cart.setItemTitle(item.getTitle());
            // 保存到数据库
            this.cartMapper.insert(cart);
        } else {
            // 存在，执行更新操作
            cart.setNum(cart.getNum() + 1);// TODO 先暂时实现默认为1
            cart.setUpdated(new Date());
            this.cartMapper.updateByPrimaryKey(cart);
        }

    }

    public List<Cart> queryCartList() {
        return this.queryCartList(UserThreadLocal.get().getId());
    }

    public List<Cart> queryCartList(Long userId) {
        Example example = new Example(Cart.class);
        example.setOrderByClause("created DESC");
        // TODO 分页查询
        example.createCriteria().andEqualTo("userId", userId);
        return this.cartMapper.selectByExample(example);
    }

    public void updateNum(Long itemId, Integer num) {
        // 更新条件
        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("userId", UserThreadLocal.get().getId())
                .andEqualTo("itemId", itemId);

        // 更新的内容
        Cart record = new Cart();
        record.setNum(num);
        record.setUpdated(new Date());

        // 执行更新
        this.cartMapper.updateByExampleSelective(record, example);
    }

    public void deleteItem(Long itemId) {
        // 实现物理删除
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(UserThreadLocal.get().getId());
        this.cartMapper.delete(record);
    }

}
