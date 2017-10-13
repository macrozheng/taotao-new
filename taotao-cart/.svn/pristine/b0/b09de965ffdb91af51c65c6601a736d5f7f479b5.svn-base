package com.taotao.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.cart.bean.User;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartCookieService;
import com.taotao.cart.service.CartService;
import com.taotao.cart.threadlocal.UserThreadLocal;

@RequestMapping("cart")
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartCookieService cartCookieService;

    /**
     * 将商品加入到购物车
     * 
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public String addItemToCart(@PathVariable("itemId") Long itemId, HttpServletRequest request,
            HttpServletResponse response) {
        // 判断用户是否登录
        User user = UserThreadLocal.get();
        if (user == null) {
            // 未登录状态
            this.cartCookieService.addItemToCart(itemId, request, response);
        } else {
            // 已登录
            this.cartService.addItemToCart(itemId);
        }
        return "redirect:/cart/show.html";
    }

    /**
     * 显示购物车列表
     * 
     * @return
     */
    @RequestMapping(value = "show", method = RequestMethod.GET)
    public ModelAndView showCartList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("cart");
        // 判断用户是否登录
        User user = UserThreadLocal.get();
        List<Cart> carts = null;
        if (user == null) {
            // 未登录状态
            carts = this.cartCookieService.queryCartList(request);
        } else {
            // 已登录
            carts = this.cartService.queryCartList();
        }
        mv.addObject("cartList", carts);
        return mv;
    }

    /**
     * 更新购买商品的数量
     * 
     * @param itemId
     * @param num 最终购买的商品数量
     * @return
     */
    @RequestMapping(value = "update/num/{itemId}/{num}", method = RequestMethod.POST)
    public ResponseEntity<Void> udpateNum(@PathVariable("itemId") Long itemId,
            @PathVariable("num") Integer num, HttpServletRequest request, HttpServletResponse response) {
        // 判断用户是否登录
        User user = UserThreadLocal.get();
        if (user == null) {
            // 未登录状态
            this.cartCookieService.updateNum(itemId, num, request, response);
        } else {
            // 已登录
            this.cartService.updateNum(itemId, num);
        }
        return ResponseEntity.ok(null);
    }

    /**
     * 删除购物车中的商品
     * 
     * @param itemId
     * @return
     */
    @RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
    public String deleteItem(@PathVariable("itemId") Long itemId, HttpServletRequest request,
            HttpServletResponse response) {
        // 判断用户是否登录
        User user = UserThreadLocal.get();
        if (user == null) {
            // 未登录状态
            this.cartCookieService.deleteItem(itemId, request, response);
        } else {
            // 已登录
            this.cartService.deleteItem(itemId);
        }
        return "redirect:/cart/show.html";
    }

}
