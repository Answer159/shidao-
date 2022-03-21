package cn.wmyskxz.controller;


import cn.wmyskxz.entity.Category;
import cn.wmyskxz.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wzh
 * @since 2022-02-17
 */
@Controller
@RequestMapping("/shidao/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @RequestMapping("/list")
    public Map list(){
        List<Category> categories=new ArrayList<>();
        categories=categoryService.list();
        Map map=new HashMap();
        map.put("categories",categories);
        return map;
    }
}

