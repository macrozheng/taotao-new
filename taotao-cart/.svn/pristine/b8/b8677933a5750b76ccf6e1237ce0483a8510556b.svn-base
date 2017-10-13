package com.taotao.cart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.Item;
import com.taotao.cart.pojo.Cart;
import com.taotao.common.util.CookieUtils;

@Service
public class CartCookieService {

    public static final String CART_COOKIE_NAME = "TT_CART";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Integer CART_COOKIE_TIME = 60 * 60 * 24 * 30 * 6;

    @Autowired
    private ItemService itemService;

    public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        // 获取到cookie中的cart list
        List<Cart> carts = queryCartList(request);
        // 判断该商品是否存在购物车中
        Cart cart = null;
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                // 存在
                cart = c;
                break;
            }
        }

        if (cart == null) {
            // 不存在
            cart = new Cart();
            cart.setItemId(itemId);
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setNum(1); // TODO
            Item item = this.itemService.queryItemById(itemId);
            cart.setItemImage(item.getImages()[0]);
            cart.setItemPrice(item.getPrice());
            cart.setItemTitle(item.getTitle());
            carts.add(cart);
        } else {
            // 存在
            cart.setNum(cart.getNum() + 1); // TODO
            cart.setUpdated(new Date());
        }

        try {
            // 将json数据写入到cookie中
            CookieUtils.setCookie(request, response, CART_COOKIE_NAME, MAPPER.writeValueAsString(carts),
                    CART_COOKIE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Cart> queryCartList(HttpServletRequest request) {
        String jsonData = CookieUtils.getCookieValue(request, CART_COOKIE_NAME, true);
        List<Cart> carts = null;
        try {
            carts = MAPPER.readValue(jsonData,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (Exception e) {
            e.printStackTrace();
            carts = new ArrayList<Cart>();
        }
        return carts;
    }

    public void updateNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        // 获取到cookie中的cart list
        List<Cart> carts = queryCartList(request);
        // 判断该商品是否存在购物车中
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                // 存在
                c.setNum(num);
                break;
            }
        }
        try {
            // 将json数据写入到cookie中
            CookieUtils.setCookie(request, response, CART_COOKIE_NAME, MAPPER.writeValueAsString(carts),
                    CART_COOKIE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteItem(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        // 获取到cookie中的cart list
        List<Cart> carts = queryCartList(request);
        // 判断该商品是否存在购物车中
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                // 存在
                carts.remove(c);
                break;
            }
        }
        try {
            // 将json数据写入到cookie中
            CookieUtils.setCookie(request, response, CART_COOKIE_NAME, MAPPER.writeValueAsString(carts),
                    CART_COOKIE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
