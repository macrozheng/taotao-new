package com.taotao.manage.mapper;

import java.util.List;

import com.github.abel533.mapper.Mapper;
import com.taotao.manage.pojo.Content;

public interface ContentMapper extends Mapper<Content> {

    List<Content> queryListBycategoryId(Long categoryId);

}
