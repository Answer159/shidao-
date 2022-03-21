package cn.wmyskxz.controller;


import cn.wmyskxz.entity.*;
import cn.wmyskxz.service.*;
import cn.wmyskxz.vo.ClassVo;
import cn.wmyskxz.vo.QuestionVo;
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
@RequestMapping("/shidao/question")
public class QuestionController {
    @Resource
    private QuestionService questionService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private QuestionImageInfoService questionImageInfoService;
    @Resource
    private QuestionVideoInfoService questionVideoInfoService;
    @RequestMapping("/hisQuestion")
    @ResponseBody
    @ApiOperation(value = "我的所有提问")
    public Map hisQuestion(@RequestParam("id")Integer id, HttpServletRequest request){
        UserInfo userInfo = userInfoService.getById(id);
        Map map=new HashMap();
        List<Question> questions=questionService.list(new QueryWrapper<Question>().eq("user_id",userInfo.getId()));
        List<QuestionVo> questionVos = new ArrayList<>();
        for(Question question:questions){
            QuestionVo questionVo=new QuestionVo();
            questionVo.setId(question.getId());
            questionVo.setTitle(question.getTitle());
            questionVo.setUsername(userInfo.getUsername());
            questionVo.setMoney(question.getPrice());
            List<QuestionImageInfo> questionImageInfos=questionImageInfoService.
                    list(new QueryWrapper<QuestionImageInfo>().eq("question_id",question.getId()));
            if(questionImageInfos.size()!=0){
                questionVo.setImg("/img/image/questionImage/"+question.getId()+"/"+questionImageInfos.get(0).getId()+"."+questionImageInfos.get(0).getSuffix());
            }
            else{
                questionVo.setImg("/img/image/classImage/0.jpg");
            }
            questionVos.add(questionVo);
        }
        map.put("questionVos",questionVos);
        map.put("msg","success");
        return map;
    }
    @RequestMapping("/GetFullQuestion")
    @ResponseBody
    public Map GetFullQuestion(@RequestParam("id")Integer id){
        Question question = questionService.getById(id);
        List<QuestionImageInfo> questionImageInfos=questionImageInfoService.
                list(new QueryWrapper<QuestionImageInfo>().eq("question_id",id));
        String filePath;
        filePath="/img/image/questionImage/"+id+"/";
        UserInfo user=userInfoService.getById(question.getUserId());
        List<String> imgPath=new ArrayList<>();
        Map map=new HashMap();
        String videoPath="";
        QuestionVideoInfo questionVideoInfo = questionVideoInfoService.getOne(new QueryWrapper<QuestionVideoInfo>().eq("question_id",id));
        if(questionVideoInfo!=null){
            videoPath += "img/questionVideo/"+questionVideoInfo.getId()+".mp4";
        }
        if(questionImageInfos.size()==0){
            imgPath.add("/img/image/0.jpg");
            map.put("question",question);
            map.put("imgPaths",imgPath);
            map.put("videoPath",videoPath);
            return map;
        }
        for(QuestionImageInfo questionImageInfo:questionImageInfos){
            imgPath.add(filePath+questionImageInfo.getId() + "." + questionImageInfo.getSuffix());
        }
        map.put("question",question);
        map.put("imgPaths",imgPath);
        map.put("videoPath",videoPath);

        return map;

    }

    @RequestMapping("/sortQuestion")
    @ResponseBody
    @ApiOperation(value = "课程排序")
    public Map sortQuestion(@RequestParam("sort") Integer sort,
                             @RequestParam("keyword") String keyword,
                            @RequestParam(value = "categoryId",required = false) String categoryId,
                            int pageNum,
                             HttpServletRequest request) {
        List<Question> questions;
        if(categoryId == null){
            questions = questionService.list(new QueryWrapper<Question>().like("title",keyword));
        }
        else{
            questions = questionService.list(new QueryWrapper<Question>().like("title",keyword).
                    eq("domain_id",categoryId));
        }
        String filePath;
        filePath="/img/image/questionImage/";
        Map map=new HashMap();
        int page=questions.size()/10;
        int lastPage=questions.size()%10;
        if (0 != sort) {
            switch (sort) {
                case 3:
                    Collections.sort(questions, Comparator.comparing(Question::getSuggestTime));
                    break;

                case 1:
                    Collections.sort(questions, Comparator.comparing(Question::getPrice));
                    break;
            }
        }
        if(lastPage!=0){
            page++;
        }
        if(questions.size() == 0){
            map.put("resultNum",0);
            map.put("pages",0);
            return map;
        }
        for(int i=0;i<page;i++){
            if(i!=pageNum-1){
                continue;
            }
            List<Question> questions1=new ArrayList<>();
            List<String> imgPath=new ArrayList<>();
            List<UserInfo> userInfos=new ArrayList<>();
            int num=10;
            if(i==page-1&&lastPage!=0){
                num=lastPage;
            }
            for(int j=0;j<num;j++){
                Question question=questions.get(i*10+j);
                questions1.add(question);
                userInfos.add(userInfoService.getById(question.getUserId()));
                List<QuestionImageInfo> questionImageInfos=questionImageInfoService.
                        list(new QueryWrapper<QuestionImageInfo>().eq("question_id",question.getId()));
                if(questionImageInfos.size()!=0){
                    imgPath.add(filePath+question.getId()+"/"+questionImageInfos.get(0).getId()+"."+questionImageInfos.get(0).getSuffix());
                }
                else{
                    imgPath.add("/img/image/0.jpg");
                }
            }
            //classInfoss.add(classInfos1);
            map.put("resultNum",questions.size());
            map.put("questions",questions1);
            map.put("pages",page);
            map.put("imgPath",imgPath);
            map.put("userInfos",userInfos);
            break;
        }

        return map;
    }
    //last:6-7
    @RequestMapping("/recommendQuestion")
    @ResponseBody
    public Map recommendQuestion(HttpSession session,HttpServletRequest request) {
        List<Question> questions=questionService.list();
        List<QuestionVo> questionVos=new ArrayList<>();
        int length = questions.size();
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
            Question question = questions.get(start+k);
            UserInfo userInfo = userInfoService.getById(question.getUserId());
            String imgPath;
            QuestionVo questionVo=new QuestionVo();
            questionVo.setId(question.getId());
            questionVo.setTitle(question.getTitle());
            questionVo.setMoney(question.getPrice());
//            int i=0;
//            float k=i;
            questionVo.setUsername(userInfo.getUsername());
            List<QuestionImageInfo> questionImageInfos=questionImageInfoService.
                    list(new QueryWrapper<QuestionImageInfo>().eq("question_id",question.getId()));
            if(questionImageInfos.size()!=0){
                imgPath = "/img/image/questionImage/"+question.getId()+"/"+questionImageInfos.get(0).getId()+"."+questionImageInfos.get(0).getSuffix();
            }
            else{
                imgPath = "/img/image/questionImage/0.jpg";
            }
            questionVo.setImg(imgPath);
            questionVos.add(questionVo);
        }

        Map map=new HashMap();
        map.put("questionVos",questionVos);
        map.put("msg","success");
        return map;
    }
}

