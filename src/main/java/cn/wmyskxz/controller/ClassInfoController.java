package cn.wmyskxz.controller;


import cn.wmyskxz.entity.*;
import cn.wmyskxz.service.*;
import cn.wmyskxz.vo.ClassVo;
import cn.wmyskxz.vo.EvaluationVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wzh
 * @since 2022-02-17
 */
@Controller
@RequestMapping("/shidao/classInfo")
public class ClassInfoController {
    @Resource
    private ClassInfoService classInfoService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private ClassImageInfoService classImageInfoService;
    @Resource
    private ClassVideoInfoService classVideoInfoService;
    @Resource
    private OrderService orderService;
    @RequestMapping("/hisClassInfo")
    @ResponseBody
    @ApiOperation(value = "我的所有课程")
    public Map hisClassInfo(@RequestParam("id")Integer id, HttpServletRequest request) {
        UserInfo userInfo = userInfoService.getById(id);
        List<ClassInfo> classInfos=classInfoService.list(new QueryWrapper<ClassInfo>().eq("user_id",userInfo.getId()));
//        List<String> imgPath=new ArrayList<>();
        List<ClassVo> classVos=new ArrayList<>();
        for(ClassInfo classInfo:classInfos){
            String imgPath;
            ClassVo classVo=new ClassVo();
            classVo.setId(classInfo.getId());
            classVo.setTitle(classInfo.getTitle());
            classVo.setMoney(classInfo.getPrice());
            int i=0;
            float k=i;
            classVo.setScore(k);
            classVo.setUsername(userInfo.getUsername());
            List<ClassImageInfo> classImageInfos=classImageInfoService.
                    list(new QueryWrapper<ClassImageInfo>().eq("class_info_id",classInfo.getId()));
            if(classImageInfos.size()!=0){
                imgPath = "/img/image/classImage/"+classInfo.getId()+"/"+classImageInfos.get(0).getId()+"."+classImageInfos.get(0).getSuffix();
            }
            else{
                imgPath = "/img/image/classImage/0.jpg";
            }
            classVo.setImg(imgPath);
            classVos.add(classVo);
        }

        Map map=new HashMap();
        map.put("classVos",classVos);
        map.put("msg","success");
        return map;
    }
    @RequestMapping("/GetFullClass")
    @ResponseBody
    @ApiOperation(value = "我的所有课程")
    public Map GetFullClass(@RequestParam("id")Integer id){
        ClassInfo classInfo = classInfoService.getById(id);
        List<ClassImageInfo> classImageInfos=classImageInfoService.
                list(new QueryWrapper<ClassImageInfo>().eq("class_info_id",id));
        String filePath;
        filePath="/img/image/classImage/"+id+"/";
        UserInfo user=userInfoService.getById(classInfo.getUserId());
        List<String> imgPath=new ArrayList<>();
        Map map=new HashMap();
        String videoPath="";
        ClassVideoInfo classVideoInfo = classVideoInfoService.getOne(new QueryWrapper<ClassVideoInfo>().eq("class_info_id",id));
        if(classVideoInfo!=null){
            videoPath += "img/classVideo/"+classVideoInfo.getId()+".mp4";
        }
        if(classImageInfos.size()==0){
            imgPath.add("/img/image/0.jpg");
            map.put("classInfo",classInfo);
            map.put("imgPaths",imgPath);
            map.put("videoPath",videoPath);
            return map;
        }
        for(ClassImageInfo classImageInfo:classImageInfos){
            imgPath.add(filePath+classImageInfo.getId() + "." + classImageInfo.getSuffix());
        }
        map.put("classInfo",classInfo);
        map.put("imgPaths",imgPath);
        map.put("videoPath",videoPath);

        return map;
    }
    //last:6-7
    @RequestMapping("/recommendClassInfo")
    @ResponseBody
    public Map recommendClassInfo(HttpSession session,HttpServletRequest request) {
        List<ClassInfo> classInfos=classInfoService.list();
        List<ClassVo> classVos=new ArrayList<>();
        int length = classInfos.size();
        int multi = 1;
        int start;
        if(length > 4){
            multi = length - 4;
        }
        Random r =new Random();
        start = r.nextInt(multi);
        int k =  0;
        for(k = 0;k < 4;k++){
            if(start + k + 1 > length){
                break;
            }
            ClassInfo classInfo = classInfos.get(start+k);
            UserInfo userInfo = userInfoService.getById(classInfo.getUserId());
            String imgPath;
            ClassVo classVo=new ClassVo();
            classVo.setId(classInfo.getId());
            classVo.setTitle(classInfo.getTitle());
            classVo.setMoney(classInfo.getPrice());
//            int i=0;
//            float k=i;
            classVo.setScore(classInfo.getScore());
            classVo.setUsername(userInfo.getUsername());
            List<ClassImageInfo> classImageInfos=classImageInfoService.
                    list(new QueryWrapper<ClassImageInfo>().eq("class_info_id",classInfo.getId()));
            if(classImageInfos.size()!=0){
                imgPath = "/img/image/classImage/"+classInfo.getId()+"/"+classImageInfos.get(0).getId()+"."+classImageInfos.get(0).getSuffix();
            }
            else{
                imgPath = "/img/image/classImage/0.jpg";
            }
            classVo.setImg(imgPath);
            classVos.add(classVo);
        }

        Map map=new HashMap();
        map.put("classVos",classVos);
        map.put("msg","success");
        return map;
    }
    @RequestMapping("/myStudy")
    @ResponseBody
    public Map myStudy(HttpSession session,HttpServletRequest request) {
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        Map map = new HashMap();
        List<OrderS> orderS = new ArrayList<>();
        orderS = orderService.list(new QueryWrapper<OrderS>().eq("buyer_id",userInfo.getId()).eq("order_status","2"));
        List<ClassInfo> classInfos = new ArrayList<>();
        for(OrderS i:orderS){
            ClassInfo c = classInfoService.getById(i.getClassId());
            classInfos.add(c);
        }
        List<ClassVo> classVos=new ArrayList<>();
        for(ClassInfo classInfo:classInfos){
            UserInfo user = userInfoService.getById(classInfo.getUserId());
            String imgPath;
            ClassVo classVo=new ClassVo();
            classVo.setId(classInfo.getId());
            classVo.setTitle(classInfo.getTitle());
            classVo.setMoney(classInfo.getPrice());
            int i=0;
            float k=i;
            classVo.setScore(k);
            classVo.setUsername(user.getUsername());
            List<ClassImageInfo> classImageInfos=classImageInfoService.
                    list(new QueryWrapper<ClassImageInfo>().eq("class_info_id",classInfo.getId()));
            if(classImageInfos.size()!=0){
                imgPath = "/img/image/classImage/"+classInfo.getId()+"/"+classImageInfos.get(0).getId()+"."+classImageInfos.get(0).getSuffix();
            }
            else{
                imgPath = "/img/image/classImage/0.jpg";
            }
            classVo.setImg(imgPath);
            classVos.add(classVo);
        }

        map.put("classVos",classVos);
        map.put("msg","success");
        return map;
    }
}

