package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.search.bean.SearchResult;
import com.taotao.search.service.SearchService;

@RequestMapping("search")
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView search(@RequestParam("q") String keyWords,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        ModelAndView mv = new ModelAndView("search");

        try {
            //解决中文乱码问题
            keyWords = new String(keyWords.getBytes("ISO-8859-1"), "UTF-8");
            
            SearchResult searchResult = this.searchService.search(keyWords, page);

            // 搜索关键字
            mv.addObject("query", keyWords);
            // 商品列表
            mv.addObject("itemList", searchResult.getList());
            // 当前页
            mv.addObject("page", page);
            // 计算总页数总页数
            int total = searchResult.getTotal().intValue();
            int rows = SearchService.ROWS;
            mv.addObject("pages", total % rows == 0 ? total / rows : total / rows + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

}
