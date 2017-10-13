package com.taotao.search.mq.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.search.bean.Item;
import com.taotao.search.service.ItemService;

public class ItemHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private HttpSolrServer httpSolrServer;
    
    @Autowired
    private ItemService itemService;
    
    public void execute(String msg) {
        // 删除缓存中的数据
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            Long itemId = jsonNode.get("itemId").asLong();
            String type = jsonNode.get("type").asText();
            if (StringUtils.equals(type, "insert") || StringUtils.equals(type, "update")) {
                Item item = this.itemService.queryItemById(itemId);
                this.httpSolrServer.addBean(item);
                this.httpSolrServer.commit();
            } else if (StringUtils.equals(type, "delete")) {
                this.httpSolrServer.deleteById(itemId.toString());
                this.httpSolrServer.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
