package com.taotao.manage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item> {

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemParamItemService itemParamItemService;

    @Autowired
    private PropertieService propertieService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Boolean saveItem(Item item, String desc, String itemParams) {
        // 初始数据
        item.setStatus(1);
        item.setId(null);// 强制设置id为null，从数据库中自增长

        Integer count1 = super.save(item);
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);

        // 保存商品描述数据
        Integer count2 = this.itemDescService.save(itemDesc);

        // 保存规格参数数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        this.itemParamItemService.save(itemParamItem);

        // 发送消息
        sendMsg(item.getId(), "insert");

        return count1.intValue() == 1 && count2.intValue() == 1;
    }

    public EasyUIResult queryPageList(Integer page, Integer rows) {
        // 设置分页信息
        PageHelper.startPage(page, rows);
        Example example = new Example(Item.class);
        example.setOrderByClause("created DESC");// 设置排序信息
        List<Item> items = this.itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<Item>(items);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

    public Boolean updateItem(Item item, String desc, Long itemParamId, String itemParams) {
        // 强制设置不能更新的字段为null
        item.setStatus(null);
        item.setCreated(null);
        int count1 = super.updateSelective(item);

        // 更新商品描述数据
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        int count2 = this.itemDescService.updateSelective(itemDesc);

        // 更新商品规格参数数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setId(itemParamId);
        itemParamItem.setParamData(itemParams);
        this.itemParamItemService.updateSelective(itemParamItem);

        // try {
        // // 通知其它系统该商品已经更新
        // String url = this.propertieService.TAOTAO_WEB_URL + "/item/cache/" + item.getId() +
        // ".html";
        // this.apiService.doPost(url);
        // } catch (Exception e) {
        // // TODO 用邮件或短信通知相关的人检测通知逻辑
        // e.printStackTrace();
        // }

        // 发送消息
        sendMsg(item.getId(), "update");

        // return count1 == 1 && count2 == 1 && count3 == 1;
        // 目前的情况下，并不是所有的商品都有规格参数数据
        return count1 == 1 && count2 == 1;
    }

    private void sendMsg(Long itemId, String type) {
        try {
            // 发送消息通知其它系统，该商品已经更新
            Map<String, Object> msg = new HashMap<String, Object>();
            msg.put("itemId", itemId);
            msg.put("type", type);
            msg.put("data", System.currentTimeMillis());
            this.rabbitTemplate.convertAndSend("item." + type, MAPPER.writeValueAsString(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
