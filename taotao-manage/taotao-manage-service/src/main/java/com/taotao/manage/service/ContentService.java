package com.taotao.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.mapper.ContentMapper;
import com.taotao.manage.pojo.Content;

@Service
public class ContentService extends BaseService<Content> {

    @Autowired
    private ContentMapper contentMapper;

    public EasyUIResult queryListBycategoryId(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<Content> contents = this.contentMapper.queryListBycategoryId(categoryId);
        PageInfo<Content> pageInfo = new PageInfo<Content>(contents);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

}
