package com.taotao.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Cart;
import com.taotao.web.threadlocal.UserThreadLocal;

@Service
public class CartService {

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${TAOTAO_CART_URL}")
    private String TAOTAO_CART_URL;

    public List<Cart> queryCartListByUser() {
        try {
            String url = TAOTAO_CART_URL + "/service/api/cart/" + UserThreadLocal.get().getId();
            String jsonData = this.apiService.doGet(url);
            if (null == jsonData) {
                return null;
            }
            return MAPPER.readValue(jsonData,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
