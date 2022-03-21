package cn.wmyskxz.controller;


import cn.wmyskxz.entity.Category;
import cn.wmyskxz.entity.ClassImageInfo;
import cn.wmyskxz.entity.ClassInfo;
import cn.wmyskxz.entity.UserInfo;
import cn.wmyskxz.service.CategoryService;
import cn.wmyskxz.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/shidao/userInfo")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private CategoryService categoryService;
    @RequestMapping("/checkUser")
    @ResponseBody
    public Map checkUser(@RequestParam("id")Integer id){
        UserInfo userInfo = userInfoService.getById(id);
        Map map = new HashMap();
        if(userInfo==null){
            map.put("msg","fail");
            return map;
        }
        if(userInfo.getSex()==1){
            userInfo.setGender("男");
        }
        else{
            userInfo.setGender("女");
        }
        String graph=userInfo.getGraphId();
        userInfo.setUserImg("/img/userImage/"+userInfo.getId()+"/"+graph);
        Category category = categoryService.getById(userInfo.getDomainId());
        userInfo.setDomainText(category.getDomain());
        map.put("msg","success");
        map.put("userInfo",userInfo);
        return map;
    }
    @RequestMapping("/searchUserInfo")
    @ResponseBody
    public Map searchUserInfo(@RequestParam("keyword") String keyword,
                              @RequestParam(value = "categoryId",required = false) String categoryId,
                              int pageNum,
                              HttpServletRequest request){
        List<UserInfo> userInfos;
        if(categoryId == null){
            userInfos = userInfoService.list(new QueryWrapper<UserInfo>().like("username",keyword));
        }
        else{
            userInfos = userInfoService.list(new QueryWrapper<UserInfo>().like("username",keyword)
                    .eq("domain_id",categoryId));
        }
        Map map=new HashMap();
        int page=userInfos.size()/10;
        int lastPage=userInfos.size()%10;
        if(lastPage!=0){
            page++;
        }
        if(userInfos.size() == 0){
            map.put("resultNum",0);
            map.put("pages",0);
            return map;
        }
        for(int i=0;i<page;i++){
            if(i!=pageNum-1){
                continue;
            }
            List<UserInfo> userInfos1=new ArrayList<>();
            int num=10;
            if(i==page-1&&lastPage!=0){
                num=lastPage;
            }
            for(int j=0;j<num;j++){
                UserInfo userInfo=userInfos.get(i*10+j);
                userInfo.setUserImg("/img/userImage/"+userInfo.getId()+"/"+userInfo.getGraphId());
                Category category = categoryService.getById(userInfo.getDomainId());
                userInfo.setDomainText(category.getDomain());
                if(userInfo.getSex()==1){
                    userInfo.setGender("男");
                }
                else{
                    userInfo.setGender("女");
                }
                userInfos1.add(userInfo);
            }
            map.put("resultNum",userInfos.size());
            map.put("userInfos",userInfos1);
            map.put("pages",page);
            break;
        }
        return map;
    }
}

