package com.taotao.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.taotao.manage.pojo.ContentCategory;

@Service
public class ContentCategoryService extends BaseService<ContentCategory> {

    public void saveContentCategory(ContentCategory contentCategory) {
        // 设置初始数据
        contentCategory.setId(null);
        contentCategory.setIsParent(false);
        contentCategory.setSortOrder(1);
        contentCategory.setStatus(1);

        super.save(contentCategory);

        // 判断该节点的父节点的isParent是否为true，如果不是，修改为true
        ContentCategory parent = super.queryById(contentCategory.getParentId());
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            super.updateSelective(parent);
        }
    }

    public void updateContentCategory(ContentCategory contentCategory) {
        // 设置不能更新的字段为null
        contentCategory.setCreated(null);
        contentCategory.setIsParent(null);
        contentCategory.setParentId(null);
        contentCategory.setSortOrder(null);
        contentCategory.setStatus(null);
        super.updateSelective(contentCategory);
    }

    public void deleteContentCategory(ContentCategory contentCategory) {
        List<Object> ids = new ArrayList<Object>();// 定义集合用于收集待删除的id
        ids.add(contentCategory.getId());

        // 根据当前节点的id查询所有的子节点（使用递归）
        findAllSubNode(ids, contentCategory.getId());

        // 删除数据
        super.deleteByIds(ContentCategory.class, "id", ids);

        // 判断当前节点是否还有同级节点，如果没有，修改父节点的isParent为false
        ContentCategory record = new ContentCategory();
        record.setParentId(contentCategory.getParentId());
        List<ContentCategory> list = super.queryListByWhere(record);
        if (list == null || list.isEmpty()) {
            // 没有其他的同级节点
            ContentCategory parent = new ContentCategory();
            parent.setId(contentCategory.getParentId());
            parent.setIsParent(false);
            super.updateSelective(parent);
        }
    }

    private void findAllSubNode(List<Object> ids, Long parentId) {
        ContentCategory record = new ContentCategory();
        record.setParentId(parentId);
        List<ContentCategory> list = super.queryListByWhere(record);
        for (ContentCategory contentCategory : list) {
            ids.add(contentCategory.getId());
            // 判断该节点是否还有子节点
            if (contentCategory.getIsParent()) {
                // 开始递归
                findAllSubNode(ids, contentCategory.getId());
            }
        }
    }

}
