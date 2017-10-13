package com.taotao.web.bean;

import org.apache.commons.lang3.StringUtils;

public class Item extends com.taotao.manage.pojo.Item {

    public String[] getImages() {
        return StringUtils.split(super.getImage(), ',');
    }

}
