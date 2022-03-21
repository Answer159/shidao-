package cn.wmyskxz.controller;

import cn.wmyskxz.entity.*;
import cn.wmyskxz.service.*;
import cn.wmyskxz.vo.ClassVo;
import cn.wmyskxz.vo.CommentVo;
import cn.wmyskxz.vo.EvaluationVo;
import cn.wmyskxz.vo.QuestionVo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//import javax.xml.registry.infomodel.User;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang.math.RandomUtils.nextFloat;

/**
 * 前台控制器
 *
 * @author: @我没有三颗心脏
 * @create: 2018-04-29-下午 14:45
 */
@Controller
@CrossOrigin
@Api("前台")
@RequestMapping("/shidao/fore")
public class ForeController {

    @Value("${prop.upload-folder}")
    private String UPLOAD_FOLDER;
    @Autowired
    CategoryService categoryService;

    @Autowired
    ReferalLinkService referalLinkService;

    @Autowired
    ClassInfoService classInfoService;

    //@Autowired
    //PropertyValueService propertyValueService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserInfoService userInfoService;

    //@Autowired
    //OrderItemService orderItemService;

    @Autowired
    OrderService orderService;


    @Autowired
    QuestionService questionService;

    @Autowired
    ClassImageInfoService classImageInfoService;

    @Autowired
    ClassVideoInfoService classVideoInfoService;
    @Autowired
    QuestionImageInfoService questionImageInfoService;
    @Autowired
    QuestionVideoInfoService questionVideoInfoService;
    @Autowired
    OrderQService order_qService;
    @Autowired
    EvaluationService evaluationService;
    String defaultPath="http://localhost:8080/Tmall_SSM_war/img/";

    private ObjectMapper mapper=new ObjectMapper();

    @RequestMapping("/cancelCollectClass")
    @ResponseBody
    @ApiOperation(value = "取消收藏课程")
    public Map cancelCollectClass(Integer id,HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        Map map=new HashMap();
        String collection=userInfo.getCollectionClass();
        String sub= id +"&";
        String newCollection=deleteString(collection,sub);
        if("error".equals(newCollection)){
            map.put("msg","没有这个收藏");
            return map;
        }
        userInfo.setCollectionClass(newCollection);
        userInfoService.updateById(userInfo);
        map.put("msg","success");
        return map;
    }
    @RequestMapping("/cancelCollectQuestion")
    @ResponseBody
    @ApiOperation(value = "取消收藏求助")
    public Map cancelCollectQuestion(Integer id,HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        Map map=new HashMap();
        String collection=userInfo.getCollectionQuestion();
        String sub= id +"&";
        String newCollection=deleteString(collection,sub);
        if("error".equals(newCollection)){
            map.put("msg","没有这个收藏");
            return map;
        }
//        if(newCollection==null){
//            newCollection="";
//        }
        userInfo.setCollectionQuestion(newCollection);
        userInfoService.updateById(userInfo);
        map.put("msg","success");
        return map;
    }
    public String deleteString(String string,String sub){
        int index=string.indexOf(sub);
        int subLength=sub.length();
        String str="";
        if(index!=-1){
            for(int i=0;i<string.length();i++){
                if(i>=index&&i<index+subLength){
                    continue;
                }
                str+=string.charAt(i);
            }
            if(str.length() == 0){
                str=null;
            }
            return str;
        }
        return "error";
    }
    @RequestMapping("/checkLogin")
    @ResponseBody
    @ApiOperation(value = "检查登录")
    public Map checkLogin(HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        Map map=new HashMap();
        if (null != userInfo) {
            map.put("msg","success");
            return map;
        }
        map.put("msg","fail");
        return map;
    }

    @RequestMapping("/CollectClass")
    @ResponseBody
    @ApiOperation(value = "收藏课程")
    public Map CollectClass(Integer class_id,HttpSession session){
        Map map=new HashMap();
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        String collection=userInfo.getCollectionClass();
        if(collection==null){
            collection=class_id+"&";
            userInfo.setCollectionClass(collection);
            userInfoService.updateById(userInfo);
        }
        else{
            if(isContain(collection,class_id)){
                map.put("msg","课程已收藏");
                return map;
            }
            collection+=class_id+"&";
            userInfo.setCollectionClass(collection);
            userInfoService.updateById(userInfo);
        }
        map.put("msg","success");

        return map;
    }

    @RequestMapping("/CollectQuestion")
    @ResponseBody
    @ApiOperation(value = "收藏课程")
    public Map CollectQuestion(Integer question_id,HttpSession session){
        Map map=new HashMap();
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        String collection=userInfo.getCollectionQuestion();
        System.out.println(collection);
        if(collection==null){
            collection=question_id+"&";
            userInfo.setCollectionQuestion(collection);
            userInfoService.updateById(userInfo);
        }
        else{
            if(isContain(collection,question_id)){
                map.put("msg","提问已收藏");
                return map;
            }
            collection=collection+question_id+"&";
            userInfo.setCollectionQuestion(collection);
            userInfoService.updateById(userInfo);
        }
        map.put("msg","success");
        return map;
    }

    public boolean isContain(String collection,int id){
        String ids[];
        ids=collection.split("&");
        for(String i:ids){
            if(Integer.toString(id).equals(i)){
                return true;
            }
        }
        return false;
    }
    //last:6-8
    @RequestMapping("/confirm")
    @ResponseBody
    @ApiOperation(value = "确认订单，这一步由发布者来做")
    public Map confirm(@RequestParam("orderId") Integer order_id,
                       @RequestParam("price") Float price,
                       @RequestParam("suggestTime") Float suggestTime){
        OrderS order_=orderService.getById(order_id);

        order_.setOrderStatus("1");
        order_.setPrice(price);
        order_.setSuggestTime(suggestTime);

        orderService.updateById(order_);

        Map map=new HashMap();
        map.put("order_",order_);
        map.put("buyer",userInfoService.getById(order_.getBuyerId()));
        map.put("seller",userInfoService.getById(order_.getSellerId()));
        map.put("classInfo",classInfoService.getById(order_.getClassId()));

        return map;
    }

    //last:6-16
    @RequestMapping("/confirm_q")
    @ResponseBody
    @ApiOperation(value = "确认提问订单，这一步由发布者来做")
    public Map confirm_q(@RequestParam("orderQId") Integer order_q_id,
                         Float price,
                         Float suggestTime){
        OrderQ order_q=order_qService.getById(order_q_id);

        order_q.setOrderStatus("1");
        order_q.setPrice(price);
        order_q.setSuggestTime(suggestTime);

        order_qService.updateById(order_q);

        Map map=new HashMap();
        map.put("order_q",order_q);
        map.put("buyer",userInfoService.getById(order_q.getBuyerId()));
        map.put("seller",userInfoService.getById(order_q.getSellerId()));
        map.put("question",questionService.getById(order_q.getQuestionId()));

        return map;
    }


    //6.8 wzh新增
    @RequestMapping("/deleteQuestion")
    @ResponseBody
    @ApiOperation(value = "删除提问")
    public Map deleteQuestion(HttpSession session,Integer id){
        Map map=new HashMap();
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        Question question=questionService.getById(id);
        if(question.getUseId().equals(userInfo.getId())){
            List<Comment> comments=commentService.list(new QueryWrapper<Comment>().eq("question_d",id));
            for(Comment comment:comments){
                commentService.removeById(comment.getId());
            }
            questionService.removeById(id);
            map.put("msg","success");
            return map;
        }
        map.put("msg","fail");
        return map;

    }

    //6.8  wzh新增
    @RequestMapping("/deleteClass")
    @ResponseBody
    @ApiOperation(value = "删除课程")
    public Map deleteClass(HttpSession session,@RequestParam("id") Integer id){
        Map map=new HashMap();
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        ClassInfo classInfo=classInfoService.getById(id);
        if(classInfo.getUserId().equals(userInfo.getId())){
            List<Evaluation> evaluations=evaluationService.list(new QueryWrapper<Evaluation>().eq("class_id",classInfo.getUserId()));
            for(Evaluation evaluation:evaluations){
                evaluationService.removeById(evaluation.getId());
            }
            classInfoService.removeById(id);
            map.put("msg","success");
            return map;
        }
        map.put("msg","fail");
        return map;

    }
    //6.10 wzh新增
    @RequestMapping("/deleteImage")
    @ResponseBody
    @ApiOperation(value = "删除图片，flag=0表示删除课程的，等于1表示删除提问的")
    public Map deleteImage(HttpSession session,Integer id,Integer flag){
        Map map=new HashMap();
        UserInfo userInfo=(UserInfo)session.getAttribute("UserInfo");
        if(flag==0){
            classImageInfoService.removeById(id);
            List<ClassImageInfo> classImageInfos=classImageInfoService.list(new QueryWrapper<ClassImageInfo>().eq("class_info_id",userInfo.getId()));
            map.put("classImageInfos",classImageInfos);
        }
        else{
            questionImageInfoService.removeById(id);
            List<QuestionImageInfo> questionImageInfos=questionImageInfoService.list(new QueryWrapper<QuestionImageInfo>().eq("question_id",userInfo.getId()));
            map.put("questionImageInfos",questionImageInfos);
        }
        return map;
    }

    //6.10 wzh 新增
    @RequestMapping("/deleteVideo")
    @ResponseBody
    @ApiOperation(value = "删除视频，flag=0表示删除课程的，等于1表示删除提问的")
    public Map deleteVideo(HttpSession session,Integer id,Integer flag){
        Map map=new HashMap();
        UserInfo userInfo=(UserInfo)session.getAttribute("UserInfo");
        if(flag==0){
            classVideoInfoService.removeById(id);
            List<ClassVideoInfo> classImageInfos=classVideoInfoService.list(new QueryWrapper<ClassVideoInfo>().eq("class_info_id",userInfo.getId()));
            map.put("classVideoInfos",classImageInfos);
        }
        else{
            questionVideoInfoService.removeById(id);
            List<QuestionVideoInfo> questionImageInfos=questionVideoInfoService.list(new QueryWrapper<QuestionVideoInfo>().eq("question_id",userInfo.getId()));
            map.put("questionVideoInfos",questionImageInfos);
        }
        return map;
    }

    //last:6-16
    @RequestMapping("/deleteOrder")
    @ResponseBody
    @ApiOperation(value = "删除订单")
    public Map deleteOrder(@RequestParam("order_id") Integer order_id,HttpSession session){
        UserInfo userInfo=(UserInfo) session.getAttribute("userInfo");
        Map map=new HashMap();
        orderService.delete(order_id,userInfo.getId());
        map.put("msg","success");
        return map;
    }

    //last:6-16
    @RequestMapping("/deleteOrder_q")
    @ResponseBody
    @ApiOperation(value = "删除提问订单")
    public Map deleteOrder_q(@RequestParam("order_q_id") Integer order_q_id,HttpSession session){
        UserInfo userInfo=(UserInfo) session.getAttribute("userInfo");
        Map map=new HashMap();
        order_qService.delete(order_q_id,userInfo.getId());
        map.put("msg","success");
        return map;
    }
    @RequestMapping("/editQuestionPage")
    @ResponseBody
    @ApiOperation(value = "修改提问页面")
    public Map editQuestionPage(HttpSession session,Integer question_id,
                                HttpServletRequest request){
        Question question=questionService.getById(question_id);
        Map map=new HashMap();
        map.put("questionInfo",question_id);
        Category category=categoryService.getById(question.getDomainId());
        map.put("category",category);
        List<Category> categories=categoryService.list();
        map.put("categories",categories);
        List<QuestionImageInfo> questionImageInfos=questionImageInfoService.list(new QueryWrapper<QuestionImageInfo>().eq("question_id",question_id));
        List<String> imgPath=new ArrayList<>();
        String filePath;
        filePath=defaultPath+"questionImage/";
//		filePath = request.getSession().getServletContext()
//				.getRealPath("img/questionImage/");
//		String path="D:\\SdData\\img\\questionImage\\";
        for(QuestionImageInfo questionImageInfo:questionImageInfos){
            imgPath.add(filePath+question_id+"/"+questionImageInfo.getId()+".jpg");
        }
        map.put("imgPath",imgPath);
        return map;
    }
    @RequestMapping("/editClassPage")
    @ResponseBody
    @ApiOperation(value = "修改课程页面")
    public Map editClassPage(HttpSession session,Integer class_id,
                             HttpServletRequest request){
        ClassInfo classInfo=classInfoService.getById(class_id);
        Map map=new HashMap();
        map.put("classInfo",classInfo);
        Category category=categoryService.getById(classInfo.getDomainId());
        map.put("category",category);
        List<Category> categories=categoryService.list();
        map.put("categories",categories);
        List<ClassImageInfo> classImageInfos=classImageInfoService.list(new QueryWrapper<ClassImageInfo>().eq("class_id",class_id));
        List<String> imgPath=new ArrayList<>();
        String filePath;
        filePath=defaultPath+"classImage/";
//		filePath = request.getSession().getServletContext()
//				.getRealPath("img/classImage/");
//		String path="D:\\SdData\\img\\classImage\\";
        for(ClassImageInfo classImageInfo:classImageInfos){
            imgPath.add(filePath+class_id+"/"+classImageInfo.getId()+".jpg");
        }
        map.put("imgPath",imgPath);
        return map;
    }
    @RequestMapping("/editClass")
    @ResponseBody
    @ApiOperation(value = "修改课程")
    public Map editClass(HttpSession session,@RequestParam("classInfo") String classInfoString,
                         int videoId){
        ClassInfo classInfo = JSONObject.parseObject(classInfoString, ClassInfo.class);
        Map map = new HashMap();
        classInfoService.updateById(classInfo);
        if(videoId!=0){
            ClassVideoInfo classVideoInfo=classVideoInfoService.getById(videoId);
            classVideoInfo.setClassInfoId(classInfo.getId());
            classVideoInfoService.updateById(classVideoInfo);
        }
        map.put("msg","success");
        return map;
    }

    @RequestMapping("/editQuestion")
    @ResponseBody
    @ApiOperation(value = "修改提问")
    public Map editQuestion(HttpSession session,@RequestParam("question") String classInfoString,int videoId){
        Question question = JSONObject.parseObject(classInfoString, Question.class);
        Map map = new HashMap();
        questionService.updateById(question);
        if(videoId!=0){
            QuestionVideoInfo questionVideoInfo=questionVideoInfoService.getById(videoId);
            questionVideoInfo.setQuestionId(question.getId());
            questionVideoInfoService.updateById(questionVideoInfo);
        }
        map.put("msg","success");
        return map;
    }

    @RequestMapping("/editUser")
    @ResponseBody
    @ApiOperation(value = "修改用户信息")
    public Map editUser(HttpSession session,
                            @RequestParam("userInfo")String userInfo1,
                            @RequestParam(value = "picture",required = false) MultipartFile picture){
        UserInfo userInfo = JSONObject.parseObject(userInfo1, UserInfo.class);
        Map map2=new HashMap();
        if(picture!=null){
            String name=picture.getOriginalFilename();
            String[]names=name.split("\\.");
            String suffix = names[1];
            Long time=System.currentTimeMillis();
            userInfo.setGraphId(time+"."+suffix);
            Integer id=userInfo.getId();
            String filePath;
//		filePath=defaultPath+"userImage/"+id;
            filePath = UPLOAD_FOLDER+"img/userImage/"+id+"/";
//		String filePath="D:\\SdData\\img\\userImage\\"+id;

            String fileName=time+"."+suffix;
            File dirs=new File(filePath);
            if(dirs.exists()){
                dirs.delete();
            }
            dirs.mkdirs();
            File uploadPicture=new File(filePath,fileName);
            try{
                uploadPicture.createNewFile();
            }
            catch (IOException e){
                e.printStackTrace();
            }

            try{
                picture.transferTo(uploadPicture);
            }catch (Exception e){
                e.printStackTrace();
                map2.put("msg","头像上传失败");
                return map2;
            }
        }
        userInfoService.updateById(userInfo);

        map2.put("msg","success");
        return map2;

    }

    //last:6-8
    @RequestMapping("/finish")
    @ResponseBody
    @ApiOperation(value = "完成订单，这一步由购买者来做")
    public Map finish(@RequestParam("order_id") Integer order_id){
        OrderS order_=orderService.getById(order_id);

        order_.setOrderStatus("2");

        orderService.updateById(order_);

        Map map=new HashMap();
        map.put("order_",order_);
        map.put("buyer",userInfoService.getById(order_.getBuyerId()));
        map.put("seller",userInfoService.getById(order_.getSellerId()));
        map.put("classInfo",classInfoService.getById(order_.getClassId()));

        return map;
    }



    //last:6-16
    @RequestMapping("/finish_q")
    @ResponseBody
    @ApiOperation(value = "完成提问订单，这一步由购买者来做")
    public Map finish_q(@RequestParam("order_q_id") Integer order_q_id){
        OrderQ order_q=order_qService.getById(order_q_id);

        order_q.setOrderStatus("2");

        order_qService.updateById(order_q);

        Map map=new HashMap();
        map.put("order_q",order_q);
        map.put("buyer",userInfoService.getById(order_q.getBuyerId()));
        map.put("seller",userInfoService.getById(order_q.getSellerId()));
        map.put("question",questionService.getById(order_q.getQuestionId()));

        return map;
    }

    @RequestMapping("/GetUser")
    @ResponseBody
    @ApiOperation(value = "获得用户信息")
    public Map GetUser(HttpSession session){
        Map map=new HashMap();
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        System.out.println("getuser:"+session.getServletContext());
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


    @RequestMapping("/home")
    @ResponseBody
    @ApiOperation(value = "主页内容")
    public Map home(HttpSession session,HttpServletRequest request) {
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        List<Category> categories = categoryService.list();
        List<ReferalLink> links = referalLinkService.list();
        List<ClassInfo> classInfos=new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        if(userInfo!=null){
            classInfos = classInfoService.list(new QueryWrapper<ClassInfo>().eq("domain_id",userInfo.getDomainId()));
            questions = questionService.list(new QueryWrapper<Question>().eq("domain_id",userInfo.getDomainId()));
        }
        else{
            classInfos = classInfoService.list();
            questions = questionService.list();
        }
        List<UserInfo> userInfos=userInfoService.list();
        List<UserInfo> userInfos1=new ArrayList<>();
        List<ClassVo> classVos=new ArrayList<>();
        List<QuestionVo> questionVos = new ArrayList<>();
        int count=0;
        for(Question question:questions){
            String imgPath;
            if(count>=10){
                break;
            }
            QuestionVo questionVo=new QuestionVo();
            questionVo.setId(question.getId());
            questionVo.setTitle(question.getTitle());
            questionVo.setMoney(question.getPrice());
            int i=0;
            float k=i;
            UserInfo u = userInfoService.getById(question.getUserId());
            questionVo.setUsername(u.getUsername());
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
            count++;
        }
        for(ClassInfo classInfo:classInfos){
            String imgPath;
            if(count>=10){
                break;
            }
            ClassVo classVo=new ClassVo();
            classVo.setId(classInfo.getId());
            classVo.setTitle(classInfo.getTitle());
            classVo.setMoney(classInfo.getPrice());
            int i=0;
            float k=i;
            classVo.setScore(k);
            UserInfo u = userInfoService.getById(classInfo.getUserId());
            classVo.setUsername(u.getUsername());
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
            count++;
        }
        count=0;
        for(UserInfo userInfo1:userInfos){
            if(count>=10){
                break;
            }
            userInfo1.setUserImg("/img/userImage/"+userInfo1.getId()+"/"+userInfo1.getGraphId());
            userInfos1.add(userInfo1);
            count++;
        }

        Map map=new HashMap();
        map.put("categories",categories);
        map.put("links",links);
        map.put("classVos",classVos);
        map.put("userInfos",userInfos1);
        map.put("questionVos",questionVos);

        return map;
    }

    //last:6-7
    @GetMapping(value="/login")
//	@RequestMapping(value="/login",method=RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "登录")
    public Map login(@RequestParam("name") String account, @RequestParam("password") String password, HttpSession session) {
        UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("account",account).eq("password",password));
        Map map=new HashMap();
        if (null == userInfo) {
            map.put("msg","fail");
            return map;
        }
        String graph=userInfo.getGraphId();
        userInfo.setUserImg("/img/userImage/"+userInfo.getId()+"/"+graph);
        Category category = categoryService.getById(userInfo.getDomainId());
        userInfo.setDomainText(category.getDomain());
        map.put("msg","success");
        session.setAttribute("userInfo", userInfo);
        map.put("userInfo",userInfo);
        return map;
    }

    //last:6-7
    @RequestMapping("/logout")
    @ResponseBody
    @ApiOperation(value = "退出登录")
    public Map logout(HttpSession session) {
        session.removeAttribute("userInfo");
        Map map=new HashMap();
        map.put("msg","success");
        return map;
    }
    @RequestMapping("/likeEvaluation")
    @ResponseBody
    @ApiOperation(value = "给评价点赞")
    public Map likeEvaluation(HttpSession session,@RequestParam("evaluation_id") Integer evaluation_id){
        UserInfo userInfo=(UserInfo) session.getAttribute("userInfo");
        Map map=new HashMap();
        if(userInfo!=null){
            Evaluation evaluation=evaluationService.getById(evaluation_id);
            int like=evaluation.getLike();
            evaluation.setLike(like+1);
            evaluationService.updateById(evaluation);
            map.put("msg","success");
            return map;
        }
        map.put("msg","fail");
        return map;
    }

    @RequestMapping("/likeComment")
    @ResponseBody
    @ApiOperation(value = "给评论点赞")
    public Map likeComment(Integer comment_id,HttpSession session)
    {
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        Map map=new HashMap();
        if(userInfo!=null){
            Comment comment= commentService.getById(comment_id);
            Integer like_count=comment.getLike();
            like_count++;
            comment.setLike(like_count);
            commentService.updateById(comment);
            map.put("msg","success");
            return map;
        }
        map.put("msg","fail");
        return map;
    }
    @RequestMapping("/listCategory")
    @ResponseBody
    public Map listCategory(){
        List<Category> categories=new ArrayList<>();
        categories=categoryService.list();
        Map map=new HashMap();
        map.put("categories",categories);
        return map;
    }

    @RequestMapping("/listCommentByQuestion")
    @ResponseBody
    @ApiOperation(value = "列出提问的评论")
    public Map listCommentByQuestion(Integer question_id,int pageNum)
    {
        List<Comment> commentList=commentService.list(new QueryWrapper<Comment>().eq("question_id",question_id));

        //分页
        Integer pageCount=10;                     //每页最多显示的数据数
        Integer pages=commentList.size()/pageCount;
        Integer lastPage=pageCount;               //尾页默认显示满数据
        if(pages*pageCount!=commentList.size())      //页码修正
        {
            lastPage=commentList.size()%pageCount;   //尾页未满时，显示相应量数据
            pages++;
        }


        Map map=new HashMap();
        map.put("pageSize",pages);                //先记录页码数
        map.put("resultNum",commentList.size());
        Integer pageC=pageCount;                  //默认当前页显示最大量数据
        for(int page=0;page<pages;page++)
        {
            if(page!=pageNum-1){
                continue;
            }
            List<Comment> pageList=new ArrayList<>();
            List<UserInfo> authorList=new ArrayList<>();
            if(page==pages-1)                     //最后一页更改至相应的量
            {
                pageC=lastPage;
            }
            for(int count=0;count<pageC;count++)
            {
                pageList.add(commentList.get(page*pageCount+count));
                authorList.add(userInfoService.getById(commentList.get(page*pageCount+count).getUseId()));
            }
            map.put("pageList"+Integer.toString(page+1),pageList);
            map.put("authorList"+Integer.toString(page+1),authorList);
            break;
        }

        return map;
    }

    @RequestMapping("/listCommentByUser")
    @ResponseBody
    @ApiOperation(value = "列出用户的所有评论")
    public Map listCommentByUser(Integer user_id,int pageNum)
    {
        List<Comment> commentList=commentService.list(new QueryWrapper<Comment>().eq("user_id",user_id));
        Integer commentCount=commentList.size();

        //分页
        Integer pageCount=10;                     //每页最多显示的数据数
        Integer pages=commentList.size()/pageCount;
        Integer lastPage=pageCount;               //尾页默认显示满数据
        if(pages*pageCount!=commentList.size())      //页码修正
        {
            lastPage=commentList.size()%pageCount;   //尾页未满时，显示相应量数据
            pages++;
        }


        Map map=new HashMap();
        map.put("pageSize",pages);                //先记录页码数
        map.put("resultNum",commentList.size());

        Integer pageC=pageCount;                  //默认当前页显示最大量数据
        for(int page=0;page<pages;page++)
        {
            if(page!=pageNum-1){
                continue;
            }
            List<Comment> pageList=new ArrayList<>();
            List<UserInfo> authorList=new ArrayList<>();
            if(page==pages-1)                     //最后一页更改至相应的量
            {
                pageC=lastPage;
            }
            for(int count=0;count<pageC;count++)
            {
                pageList.add(commentList.get(page*pageCount+count));
                authorList.add(userInfoService.getById(commentList.get(page*pageCount+count).getUseId()));
            }
            map.put("pageList"+ (page + 1),pageList);
            map.put("authorList"+ (page + 1),authorList);
            break;
        }

        return map;
    }

    //
    @RequestMapping("/listOrder_ByClient")
    @ResponseBody
    @ApiOperation(value = "列出用户买的所有订单")
    public Map listOrder_ByClient(Integer user_id,int pageNum)
    {
        List<OrderS> order_s=orderService.list(new QueryWrapper<OrderS>().eq("buyer_id",user_id));

        //分页
        Integer pageCount=10;
        Integer pages=order_s.size()/pageCount;
        Integer lastPage=pageCount;
        if(pages*pageCount!=order_s.size()) {
            lastPage=order_s.size()%pageCount;
            pages++;
        }


        Map map=new HashMap();
        map.put("pageSize",pages);
        map.put("resultNum",order_s.size());
        Integer pageC=pageCount;
        for(int page=0;page<pages;page++)
        {
            if(page!=pageNum-1){
                continue;
            }
            List<OrderS> pageList=new ArrayList<>();
            List<UserInfo> buyerList=new ArrayList<>();
            List<UserInfo> sellerList=new ArrayList<>();
            if(page==pages-1) {
                pageC=lastPage;
            }
            for(int count=0;count<pageC;count++)
            {
                pageList.add(order_s.get(page*pageCount+count));
                buyerList.add(userInfoService.getById(order_s.get(page*pageCount+count).getBuyerId()));
                sellerList.add(userInfoService.getById(order_s.get(page*pageCount+count).getSellerId()));
            }
            map.put("pageList"+ (page + 1),pageList);
            map.put("buyerList"+ (page + 1),buyerList);
            map.put("sellerList"+ (page + 1),sellerList);
            break;
        }

        return map;
    }
    @RequestMapping("/listOrder_BySeller")
    @ResponseBody
    @ApiOperation(value = "列出用户卖的所有订单")
    public Map listOrder_BySeller(Integer seller_id,int pageNum)
    {
        List<OrderS> order_s=orderService.list(new QueryWrapper<OrderS>().eq("seller_id",seller_id));

        //分页
        Integer pageCount=10;
        Integer pages=order_s.size()/pageCount;
        Integer lastPage=pageCount;
        if(pages*pageCount!=order_s.size()) {
            lastPage=order_s.size()%pageCount;
            pages++;
        }


        Map map=new HashMap();
        map.put("pageSize",pages);
        map.put("resultNum",order_s.size());
        Integer pageC=pageCount;
        for(int page=0;page<pages;page++)
        {
            if(page!=pageNum-1){
                continue;
            }
            List<OrderS> pageList=new ArrayList<>();
            List<UserInfo> buyerList=new ArrayList<>();
            List<UserInfo> sellerList=new ArrayList<>();
            if(page==pages-1) {
                pageC=lastPage;
            }
            for(int count=0;count<pageC;count++)
            {
                pageList.add(order_s.get(page*pageCount+count));
                buyerList.add(userInfoService.getById(order_s.get(page*pageCount+count).getBuyerId()));
                sellerList.add(userInfoService.getById(order_s.get(page*pageCount+count).getSellerId()));
            }
            map.put("pageList"+ (page + 1),pageList);
            map.put("buyerList"+ (page + 1),buyerList);
            map.put("sellerList"+ (page + 1),sellerList);
            break;
        }

        return map;
    }

    @RequestMapping("/listOrder_qBySeller")
    @ResponseBody
    @ApiOperation(value = "列出用户卖的提问订单")
    public Map listOrder_qBySeller(Integer seller_id,int pageNum)
    {
        List<OrderQ> order_q=order_qService.list(new QueryWrapper<OrderQ>().eq("seller_id",seller_id));

        //分页
        Integer pageCount=10;
        Integer pages=order_q.size()/pageCount;
        Integer lastPage=pageCount;
        if(pages*pageCount!=order_q.size()) {
            lastPage=order_q.size()%pageCount;
            pages++;
        }


        Map map=new HashMap();
        map.put("pageSize",pages);
        map.put("resultNum",order_q.size());
        Integer pageC=pageCount;
        for(int page=0;page<pages;page++)
        {
            if(page!=pageNum-1){
                continue;
            }
            List<OrderQ> pageList=new ArrayList<>();
            List<UserInfo> buyerList=new ArrayList<>();
            List<UserInfo> sellerList=new ArrayList<>();
            if(page==pages-1) {
                pageC=lastPage;
            }
            for(int count=0;count<pageC;count++)
            {
                pageList.add(order_q.get(page*pageCount+count));
                buyerList.add(userInfoService.getById(order_q.get(page*pageCount+count).getBuyerId()));
                sellerList.add(userInfoService.getById(order_q.get(page*pageCount+count).getSellerId()));
            }
            map.put("pageList"+ (page + 1),pageList);
            map.put("buyerList"+ (page + 1),buyerList);
            map.put("sellerList"+ (page + 1),sellerList);
            break;
        }

        return map;
    }

    @RequestMapping("/listOrder_qByBuyer")
    @ResponseBody
    @ApiOperation(value = "列出用户买的提问订单")
    public Map listOrder_qByBuyer(Integer user_id,int pageNum)
    {
        List<OrderQ> order_s=order_qService.list(new QueryWrapper<OrderQ>().eq("buyer_id",user_id));

        //分页
        Integer pageCount=10;
        Integer pages=order_s.size()/pageCount;
        Integer lastPage=pageCount;
        if(pages*pageCount!=order_s.size()) {
            lastPage=order_s.size()%pageCount;
            pages++;
        }


        Map map=new HashMap();
        map.put("pageSize",pages);
        map.put("resultNum",order_s.size());
        Integer pageC=pageCount;
        for(int page=0;page<pages;page++)
        {
            if(page!=pageNum-1){
                continue;
            }
            List<OrderQ> pageList=new ArrayList<>();
            List<UserInfo> buyerList=new ArrayList<>();
            List<UserInfo> sellerList=new ArrayList<>();
            if(page==pages-1) {
                pageC=lastPage;
            }
            for(int count=0;count<pageC;count++)
            {
                pageList.add(order_s.get(page*pageCount+count));
                buyerList.add(userInfoService.getById(order_s.get(page*pageCount+count).getBuyerId()));
                sellerList.add(userInfoService.getById(order_s.get(page*pageCount+count).getSellerId()));
            }
            map.put("pageList"+ (page + 1),pageList);
            map.put("buyerList"+ (page + 1),buyerList);
            map.put("sellerList"+ (page + 1),sellerList);
            break;
        }

        return map;
    }

    @RequestMapping("/listQuestionByKeyWord")
    @ResponseBody
    @ApiOperation(value = "关键词搜索提问")
    public Map listQuestionByKeyWord(String keyword,int pageNum,HttpServletRequest request)
    {
        List<Question> questionList=questionService.list(new QueryWrapper<Question>().like("title",keyword));
        String filePath;
        filePath=defaultPath+"questionImage/";
//		filePath = request.getSession().getServletContext()
//				.getRealPath("img/questionImage/");
//		String path="D:\\SdData\\img\\questionImage\\";
        //分页
        Integer pageCount=10;
        Integer pages=questionList.size()/pageCount;
        Integer lastPage=pageCount;
        if(pages*pageCount!=questionList.size()) {
            lastPage=questionList.size()%pageCount;
            pages++;
        }


        Map map=new HashMap();
        map.put("pageSize",pages-1);
        map.put("resultNum",questionList.size());
        Integer pageC=pageCount;
        for(int page=0;page<pages;page++)
        {
            if(page!=pageNum-1){
                continue;
            }
            List<Question> pageList=new ArrayList<>();
            List<UserInfo> authorList=new ArrayList<>();
            List<String> imgPath=new ArrayList<>();
            if(page==pages-1) {
                pageC=lastPage;
            }
            for(int count=0;count<pageC;count++)
            {
                Question question=questionList.get(page*pageCount+count);
                pageList.add(question);
                authorList.add(userInfoService.getById(question.getUseId()));
                List<QuestionImageInfo> questionImageInfos=questionImageInfoService.list(new QueryWrapper<QuestionImageInfo>().eq("question_id",question.getId()));
                if(questionImageInfos.size()!=0){
                    imgPath.add(filePath+question.getId()+"/"+questionImageInfos.get(0).getId()+".jpg");
                }
                else{
                    imgPath.add(defaultPath+"questionImage/defaultImg.jpg");
                }

            }
            map.put("pageList"+ (page + 1),pageList);
            map.put("authorList"+ (page + 1),authorList);
            break;
        }

        return map;
    }

    @RequestMapping("/listQuestionByUser")
    @ResponseBody
    @ApiOperation(value = "列出用户发布的所有提问")
    public Map listQuestionByUser(Integer user_id,int pageNum)
    {
        List<Question> questionList=questionService.list(new QueryWrapper<Question>().eq("user_id",user_id));

        //分页
        Integer pageCount=10;
        Integer pages=questionList.size()/pageCount;
        Integer lastPage=pageCount;
        if(pages*pageCount!=questionList.size()) {
            lastPage=questionList.size()%pageCount;
        }
        pages++;

        Map map=new HashMap();
        map.put("pageSize",pages-1);
        map.put("resultNum",questionList.size());
        Integer pageC=pageCount;
        for(int page=0;page<pages;page++)
        {
            if(page!=pageNum-1){
                continue;
            }
            List<Question> pageList=new ArrayList<>();
            List<UserInfo> authorList=new ArrayList<>();
            if(page==pages-1) {
                pageC=lastPage;
            }
            for(int count=0;count<pageC;count++)
            {
                pageList.add(questionList.get(page*pageCount+count));
                authorList.add(userInfoService.getById(questionList.get(page*pageCount+count).getUseId()));
            }
            map.put("page"+ (page + 1),pageList);
            map.put("authorList"+ (page + 1),authorList);
            break;
        }

        return map;
    }

    @RequestMapping("/markScore")
    @ResponseBody
    @ApiOperation(value = "对订单打分,type=0表示课程订单，等于1表示提问订单")
    public Map markScore(Integer order_id,Integer type,Integer score){
        Map map=new HashMap();
        if(type==0){
            OrderS order_=orderService.getById(order_id);
            order_.setScore(score);
            orderService.updateById(order_);
        }
        else{
            OrderQ order_q=order_qService.getById(order_id);
            order_q.setScore(score);
            order_qService.updateById(order_q);
        }
        map.put("msg","success");
        return map;
    }

    //last:6-6
    @RequestMapping("/myQuestion")
    @ResponseBody
    @ApiOperation(value = "我的所有提问")
    public Map myQuestion(HttpSession session,HttpServletRequest request){
        UserInfo userInfo=(UserInfo) session.getAttribute("userInfo");
        Map map=new HashMap();
        String filePath;
        filePath=UPLOAD_FOLDER+"img/image/questionImage/";
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

    //last:6-7
    @RequestMapping("/myClassInfo")
    @ResponseBody
    @ApiOperation(value = "我的所有课程")
    public Map myClassInfo(HttpSession session,HttpServletRequest request) {
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
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

    //last:6-16
    @RequestMapping("/myOrder")
    @ResponseBody
    @ApiOperation(value = "根据要求列出我的订单，identity可以为buyer和seller或者填null，order_status有四种状态，waitUpdate，waitConfirm，finish，delete")
    public Map myOrder(@RequestParam("order_status") String order_status,
                       @RequestParam("identity") String identity,
                       HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");

        Map map=new HashMap();
        List<UserInfo> buyers=new ArrayList<>();
        List<UserInfo> sellers=new ArrayList<>();
        List<UserInfo> buyersForSeller=new ArrayList<>();
        List<UserInfo> sellersForSeller=new ArrayList<>();
        List<UserInfo> buyers_q=new ArrayList<>();
        List<UserInfo> sellers_q=new ArrayList<>();
        List<UserInfo> buyersQForSeller=new ArrayList<>();
        List<UserInfo> sellersQForSeller=new ArrayList<>();
        List<OrderS> order_s = new ArrayList<>();
        List<OrderQ> order_qs = new ArrayList<>();
        List<OrderS> orderSeller = new ArrayList<>();
        List<OrderQ> orderQSeller = new ArrayList<>();

        if(Objects.equals(order_status, "3")){
            if(Objects.equals(identity, "buyer")){
                order_s=orderService.list(new QueryWrapper<OrderS>().eq("buyer_id",userInfo.getId()).ne("order_status",-1));

                order_qs=order_qService.list(new QueryWrapper<OrderQ>().eq("buyer_id",userInfo.getId()).ne("order_status",-1));

            }
            else if(Objects.equals(identity, "seller")){
                orderSeller=orderService.list(new QueryWrapper<OrderS>().eq("seller_id",userInfo.getId()).ne("order_status",-1));

                orderQSeller=order_qService.list(new QueryWrapper<OrderQ>().eq("seller_id",userInfo.getId()).ne("order_status",-1));

            }
            else{
                order_s=orderService.list(new QueryWrapper<OrderS>().eq("buyer_id",userInfo.getId()).ne("order_status",-1));

                order_qs=order_qService.list(new QueryWrapper<OrderQ>().eq("buyer_id",userInfo.getId()).ne("order_status",-1));

                orderSeller=orderService.list(new QueryWrapper<OrderS>().eq("seller_id",userInfo.getId()).ne("order_status",-1));
                orderQSeller=order_qService.list(new QueryWrapper<OrderQ>().eq("seller_id",userInfo.getId()).ne("order_status",-1));

            }
        }
        else{
            if(Objects.equals(identity, "seller")){
                orderSeller=orderService.listByStatus(userInfo.getId(),order_status,identity);
                orderQSeller=order_qService.listByStatus(userInfo.getId(),order_status,identity);
            }
            else if(Objects.equals(identity, "buyer")){
                order_qs=order_qService.
                        listByStatus(userInfo.getId(),order_status,identity);
                order_s=orderService.
                        listByStatus(userInfo.getId(),order_status,identity);
            }
            else{
                orderSeller=orderService.listByStatus(userInfo.getId(),order_status,identity);
                order_s=orderService.listByStatus(userInfo.getId(),order_status,identity);
                orderQSeller=order_qService.
                        listByStatus(userInfo.getId(),order_status,identity);
                order_qs=order_qService.
                        listByStatus(userInfo.getId(),order_status,identity);
            }
        }
        List<ClassInfo> classInfos = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        List<ClassInfo> classInfosSeller = new ArrayList<>();
        List<Question> questionsSeller = new ArrayList<>();
        for(OrderS o:order_s){
            ClassInfo classInfo = classInfoService.getById(o.getClassId());
            List<ClassImageInfo> classImageInfos = classImageInfoService.list(new QueryWrapper<ClassImageInfo>().eq("class_info_id",classInfo.getId()));
            List<String> imgs = new ArrayList<>();
            for(ClassImageInfo classImageInfo:classImageInfos){
                String img = "/img/image/classImage/" + classInfo.getId()+"/";
                img += classImageInfo.getId()+"."+classImageInfo.getSuffix();
                imgs.add(img);
            }
            classInfo.setImgPath(imgs);
            buyers.add(userInfoService.getById(o.getBuyerId()));
            sellers.add(userInfoService.getById(o.getSellerId()));
            classInfos.add(classInfo);
        }
        for(OrderS o:orderSeller){
            ClassInfo classInfo = classInfoService.getById(o.getClassId());
            List<ClassImageInfo> classImageInfos = classImageInfoService.list(new QueryWrapper<ClassImageInfo>().eq("class_info_id",classInfo.getId()));
            List<String> imgs = new ArrayList<>();
            for(ClassImageInfo classImageInfo:classImageInfos){
                String img = "/img/image/classImage/" + classInfo.getId()+"/";
                img += classImageInfo.getId()+"."+classImageInfo.getSuffix();
                imgs.add(img);
            }
            classInfo.setImgPath(imgs);
            buyersForSeller.add(userInfoService.getById(o.getBuyerId()));
            sellersForSeller.add(userInfoService.getById(o.getSellerId()));
            classInfosSeller.add(classInfo);
        }
        for(OrderQ o:order_qs){
            Question question = questionService.getById(o.getQuestionId());
            List<QuestionImageInfo> questionImageInfos = questionImageInfoService.list(new QueryWrapper<QuestionImageInfo>().eq("question_id",question.getId()));
            List<String> imgs = new ArrayList<>();
            for(QuestionImageInfo questionImageInfo:questionImageInfos){
                String img = "/img/image/questionImage/" + question.getId()+"/";
                img += questionImageInfo.getId()+"."+questionImageInfo.getSuffix();
                imgs.add(img);
            }
            question.setImgPath(imgs);
            buyers_q.add(userInfoService.getById(o.getBuyerId()));
            sellers_q.add(userInfoService.getById(o.getSellerId()));
            questions.add(question);
        }
        for(OrderQ o:orderQSeller){
            Question question = questionService.getById(o.getQuestionId());
            List<QuestionImageInfo> questionImageInfos = questionImageInfoService.list(new QueryWrapper<QuestionImageInfo>().eq("question_id",question.getId()));
            List<String> imgs = new ArrayList<>();
            for(QuestionImageInfo questionImageInfo:questionImageInfos){
                String img = "/img/image/questionImage/" + question.getId()+"/";
                img += questionImageInfo.getId()+"."+questionImageInfo.getSuffix();
                imgs.add(img);
            }
            question.setImgPath(imgs);
            buyersQForSeller.add(userInfoService.getById(o.getBuyerId()));
            sellersQForSeller.add(userInfoService.getById(o.getSellerId()));
            questionsSeller.add(question);
        }
        map.put("orderS",order_s);
        map.put("orderSeller",orderSeller);
        map.put("orderQs",order_qs);
        map.put("orderQSeller",orderQSeller);
        map.put("buyers",buyers);
        map.put("buyersForSeller",buyersForSeller);
        map.put("sellers",sellers);
        map.put("sellersForSeller",sellersForSeller);
        map.put("buyersQs",buyers_q);
        map.put("buyersQForSeller",buyersQForSeller);
        map.put("sellersQs",sellers_q);
        map.put("sellersQForSeller",sellersQForSeller);
        map.put("classInfos",classInfos);
        map.put("classInfosSeller",classInfosSeller);
        map.put("questions",questions);
        map.put("questionsSeller",questionsSeller);
        return map;
    }
    //6.22 wzh加
    @RequestMapping("/myAllOrder")
    @ResponseBody
    @ApiOperation(value = "我的所有订单")
    public Map MyAllOrder(HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");

        Map map=new HashMap();
        List<OrderS> order_s;
        List<OrderS> order;
        List<UserInfo> buyers=new ArrayList<>();
        List<UserInfo> sellers=new ArrayList<>();
        List<ClassInfo> classInfos=new ArrayList<>();
        order_s=orderService.list(new QueryWrapper<OrderS>().eq("buyer_id",userInfo.getId()));
        order=orderService.list(new QueryWrapper<OrderS>().eq("seller_id",userInfo.getId()));
        for(OrderS o:order_s){
            order.add(o);
        }
        for(OrderS o:order){
            buyers.add(userInfoService.getById(o.getBuyerId()));
            sellers.add(userInfoService.getById(o.getSellerId()));
            classInfos.add(classInfoService.getById(o.getClassId()));
        }
        map.put("orders",order);
        map.put("buyers",buyers);
        map.put("sellers",sellers);
        map.put("classInfos",classInfos);
        return map;
    }
    //last:6-6
    //6.20 wzh改
    @RequestMapping("/postQuestion")
    @ResponseBody
    @ApiOperation(value = "发布提问")
    public Map postQuestion(@RequestParam("question") String questionString,
                            HttpSession session,
                            HttpServletRequest request,
                            MultipartFile[] pictures,
                            int videoId) throws IOException {
        UserInfo userInfo=(UserInfo) session.getAttribute("userInfo");
        Question question = JSONObject.parseObject(questionString, Question.class);
        //Question question=mapper.readValue(questionString,Question.class);

        question.setUseId(userInfo.getId());
        question.setStatu(1);
        question.setCommentNum(0);
        question.setLike(0);

        Map map=new HashMap();

        questionService.save(question);
        Integer id=question.getId();

        if(pictures!=null){
            if(!postImage(request,pictures,id,1)){
                map.put("msg","fail");
                return map;
            }
//            if(!postVideo(request,videos,id,0)){
//                map.put("msg","fail");
//                return map;
//            }
        }
        if(videoId!=0){
            QuestionVideoInfo questionsVideoInfo=questionVideoInfoService.getById(videoId);
            questionsVideoInfo.setQuestionId(id);
            questionVideoInfoService.updateById(questionsVideoInfo);
        }
        map.put("msg","success");

//        if(!postImage(request,pictures,id,1)){
//            map.put("msg","fail");
//            return map;
//        }
//        if(!postVideo(request,videos,id,1)){
//            map.put("msg","fail");
//            return map;
//        }
        return map;
    }


    //last:6-6
    @RequestMapping("/postClassInfo")
    @ResponseBody
    @ApiOperation(value = "发布课程")
    public Map postClassInfo(@RequestParam("classInfo") String classInfoString,
                             HttpSession session,
                             HttpServletRequest request,
                             MultipartFile[] pictures,
                             int videoId) throws IOException {
        UserInfo userInfo=(UserInfo) session.getAttribute("userInfo");
        ClassInfo classInfo = JSONObject.parseObject(classInfoString, ClassInfo.class);

        //ClassInfo classInfo = mapper.readValue(classInfoString,ClassInfo.class);

        classInfo.setUserId(userInfo.getId());
        classInfo.setStatus(1);
        Float score=(float)Math.random()*5;
        classInfo.setScore(score);
        classInfoService.save(classInfo);
        Integer id=classInfo.getId();
        Map map=new HashMap();
        if(pictures!=null){
            if(!postImage(request,pictures,id,0)){
                map.put("msg","fail");
                return map;
            }
//            if(!postVideo(request,videos,id,0)){
//                map.put("msg","fail");
//                return map;
//            }
        }
        if(videoId!=0){
            ClassVideoInfo classVideoInfo=classVideoInfoService.getById(videoId);
            classVideoInfo.setClassInfoId(id);
            classVideoInfoService.updateById(classVideoInfo);
        }
        map.put("msg","success");
        return map;
    }

    //last:6-8
    @RequestMapping("/postComment")
    @ResponseBody
    @ApiOperation(value = "发布评论,没有回复replay_id为-1")
    public Map postComment(HttpSession session,
                           @RequestParam("questionId") Integer question_id,
                           @RequestParam("content") String content,
                           @RequestParam(value = "replayId") Integer replay_id) {
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(new Date());
        comment.setQuestionId(question_id);
        comment.setUseId(userInfo.getId());
        comment.setLike(0);
        comment.setReplayNum(0);
        comment.setReplayId(replay_id);
        if(replay_id!=-1){
            Comment comment1=commentService.getOne(new QueryWrapper<Comment>().eq("id",replay_id));
            int repalyNum=comment1.getReplayNum();
            comment1.setReplayNum(repalyNum+1);
            commentService.updateById(comment1);
        }
        commentService.save(comment);
        CommentVo commentVo = new CommentVo();
        Question question=questionService.getById(question_id);
        int commentNum=question.getCommentNum();
        question.setCommentNum(commentNum+1);
        questionService.updateById(question);
        userInfo.setUserImg("/img/userImage/"+userInfo.getId()+"/"+userInfo.getGraphId());
        if(comment.getReplayId()!=-1){
            Comment comment1 = commentService.getById(comment.getReplayId());
            UserInfo userInfo1 = userInfoService.getById(comment1.getUserId());
            comment.setContent("@"+userInfo1.getUsername()+": "+comment.getContent());
        }
        commentVo.setComment(comment);
        commentVo.setUserInfo(userInfo);
        Map map=new HashMap();

        map.put("comment",commentVo);
        map.put("question",questionService.getById(question_id));

        return map;
    }
    @RequestMapping("/postEvaluation")
    @ResponseBody
    @ApiOperation(value = "发布评价")
    public Map postEvaluation(HttpSession session,
                              @RequestParam("content") String content,
                              @RequestParam("orderId") Integer order_id){
        UserInfo userInfo=(UserInfo) session.getAttribute("userInfo");
        Map map=new HashMap();
        Evaluation evaluation=new Evaluation();
        evaluation.setEvaluation(content);
        evaluation.setCreateDate(new Date());
        evaluation.setOrderId(order_id);
        evaluation.setLike(0);
        evaluation.setUserId(userInfo.getId());
        float f =(float)0;
        evaluation.setScore(f);
        OrderS order_=orderService.getById(order_id);
        evaluation.setClassId(order_.getClassId());
        evaluationService.save(evaluation);
        map.put("id",evaluation.getId());
        map.put("msg","success");
        return map;
    }
//	//last:6-8
//	@RequestMapping("/postAnswer")
//	@ResponseBody
//	public Map postAnswer(HttpSession session,
//						  @RequestParam("question_id") Integer question_id,
//						  @RequestParam("content") String content){
//		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
//
//		Comment comment = new Comment();
//		comment.setContent(content);
//		comment.setCreateDate(new Date());
//		comment.setQuestion_id(question_id);
//		comment.setUse_id(userInfo.getId());
//		comment.setClass_id(-1);
//		commentService.add(comment);
//
//		Map map=new HashMap();
//		map.put("comment",comment);
//		map.put("userInfo",userInfo);
//		map.put("question",questionService.get(question_id));
//
//		return map;
//	}

    public boolean postImage(HttpServletRequest request, MultipartFile[] pictures, int class_id, int type){
        String filePath;
        filePath=UPLOAD_FOLDER+"img/image/";
        if(type==0){
//			filePath=defaultPath+"classImage/"+class_id;
            filePath += "classImage/"+class_id+"/";
//			filePath="D:\\SdData\\img\\classImage\\"+class_id;
        }
        else{
//			filePath=defaultPath+"questionImage/"+class_id;
            filePath += "questionImage/"+class_id+"/";
//			filePath="D:\\SdData\\img\\questionImage\\"+class_id;
        }
        File dirs=new File(filePath);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        for(MultipartFile p:pictures){
            if(type==0){
                String name=p.getOriginalFilename();
                String[]names=name.split("\\.");
                String suffix = names[1];
                ClassImageInfo classImageInfo=new ClassImageInfo();
                classImageInfo.setClassInfoId(class_id);
                classImageInfo.setStatus(1);
                classImageInfo.setSuffix(suffix);
                classImageInfoService.save(classImageInfo);
                int graph_id=classImageInfo.getId();
                String fileName = graph_id + "." + suffix;
                File uploadPicture = new File(filePath, fileName);
                if(uploadPicture.exists()){
                    uploadPicture.delete();
                }
                try{
                    uploadPicture.createNewFile();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    p.transferTo(uploadPicture);
                } catch (Exception e) {
                    return false;
                }
            }
            else{
                String name=p.getOriginalFilename();
                String[]names=name.split("\\.");
                String suffix = names[1];
                QuestionImageInfo questionImageInfo=new QuestionImageInfo();
                questionImageInfo.setQuestionId(class_id);
                questionImageInfo.setStatus(1);
                questionImageInfo.setSuffix(suffix);
                questionImageInfoService.save(questionImageInfo);
                int graph_id=questionImageInfo.getId();
                String fileName = graph_id + "." + suffix;
                File uploadPicture = new File(filePath, fileName);
                if(uploadPicture.exists()){
                    uploadPicture.delete();
                }
                try{
                    uploadPicture.createNewFile();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    p.transferTo(uploadPicture);
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return true;
    }
    @RequestMapping("/saveVideo")
    @ResponseBody
    public Map saveVideo(HttpServletRequest request,MultipartFile file,int flag) throws IOException {
        Map map=new HashMap();
        if(flag==0){
            ClassVideoInfo classVideoInfo=new ClassVideoInfo();
            classVideoInfo.setClassInfoId(0);
            classVideoInfo.setStatus(1);
            classVideoInfoService.save(classVideoInfo);
            int id = classVideoInfo.getId();
            String filePath;
            filePath=UPLOAD_FOLDER+"img/classVideo";
            File dirs=new File(filePath);
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            String fileName = id + ".mp4";
            File uploadVideo = new File(filePath, fileName);
            if (uploadVideo.exists()) {
                uploadVideo.delete();
            }
            try{
                uploadVideo.createNewFile();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            try {
                file.transferTo(uploadVideo);
            } catch (Exception e) {
                map.put("msg","error");
                return map;
            }
            map.put("msg","success");
            map.put("url","/img/classVideo/"+id+".mp4");
            map.put("id",id);
        }
        else{
            QuestionVideoInfo questionVideoInfo=new QuestionVideoInfo();
            questionVideoInfo.setQuestionId(0);
            questionVideoInfo.setStatus(1);
            questionVideoInfoService.save(questionVideoInfo);
            int id = questionVideoInfo.getId();
            String filePath;
            filePath=UPLOAD_FOLDER+"img/questionVideo";
            File dirs=new File(filePath);
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            String fileName = id + ".mp4";
            File uploadVideo = new File(filePath, fileName);
            if (uploadVideo.exists()) {
                uploadVideo.delete();
            }
            try{
                uploadVideo.createNewFile();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            try {
                file.transferTo(uploadVideo);
            } catch (Exception e) {
                map.put("msg","error");
                return map;
            }
            map.put("msg","success");
            map.put("url","/img/questionVideo/"+id+".mp4");
            map.put("id",id);
        }
        return map;
    }

    public boolean postVideo(HttpServletRequest request,MultipartFile[] videos,int class_id,int type){
        String filePath;

        if(type==0){
//			filePath=defaultPath+"classVideo/"+class_id;
            filePath = request.getSession().getServletContext()
                    .getRealPath("video/classVideo/" + class_id);
//			filePath="D:\\SdData\\img\\classVideo\\"+class_id;
        }
        else{
//			filePath=defaultPath+"questionVideo/"+class_id;
            filePath = request.getSession().getServletContext()
                    .getRealPath("video/questionVideo/" + class_id);
//			filePath="D:\\SdData\\img\\questionImage\\"+class_id;
        }
        File dirs=new File(filePath);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        for(MultipartFile p:videos){
            if(type==0){
                ClassVideoInfo classVideoInfo=new ClassVideoInfo();
                classVideoInfo.setClassInfoId(class_id);
                classVideoInfo.setStatus(1);
                classVideoInfoService.save(classVideoInfo);
                int video_id=classVideoInfo.getId();
                String fileName = video_id + ".mp4";
                File uploadVideo = new File(filePath, fileName);
                if (uploadVideo.exists()) {
                    uploadVideo.delete();
                }
                try{
                    uploadVideo.createNewFile();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    p.transferTo(uploadVideo);
                } catch (Exception e) {
                    return false;
                }
            }
            else{
                QuestionVideoInfo questionVideoInfo=new QuestionVideoInfo();
                questionVideoInfo.setQuestionId(class_id);
                questionVideoInfo.setStatus(1);
                questionVideoInfoService.save(questionVideoInfo);
                int video_id=questionVideoInfo.getId();
                String fileName = video_id + ".mp4";
                File uploadVideo = new File(filePath, fileName);
                if (uploadVideo.exists()) {
                    uploadVideo.delete();

                }
                try{
                    uploadVideo.createNewFile();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    p.transferTo(uploadVideo);
                } catch (Exception e) {
                    return false;
                }
            }

        }
        return true;
    }

    //last:6-8
    @RequestMapping("/register")
    @ResponseBody
    @ApiOperation(value = "注册")
    public Map register(HttpServletRequest request,
                        @RequestParam("userInfo") String userInfo1,
                        @RequestParam("picture") MultipartFile picture) throws IOException {
        UserInfo userInfo = JSONObject.parseObject(userInfo1, UserInfo.class);
//        UserInfo userInfo=mapper.readValue(userInfo1,UserInfo.class);
        List<UserInfo> users=userInfoService.list(new QueryWrapper<UserInfo>().eq("account",userInfo.getAccount()));

        if(users.size()>0){
            Map map1=new HashMap();
            String msg1="用户名已被占用，不能使用";
            map1.put("msg",msg1);
            return map1;
        }
        String name=picture.getOriginalFilename();
        String[]names=name.split("\\.");
        String suffix = names[1];
        Long time=System.currentTimeMillis();
        userInfo.setGraphId(time+"."+suffix);
        userInfo.setCollectionQuestion(null);
        userInfo.setCollectionClass(null);
        userInfoService.save(userInfo);
        Integer id=userInfo.getId();
        String filePath;
//		filePath=defaultPath+"userImage/"+id;
        filePath = UPLOAD_FOLDER+"img/userImage/"+id+"/";
//		String filePath="D:\\SdData\\img\\userImage\\"+id;

        String fileName=time+"."+suffix;
        File dirs=new File(filePath);
        if(dirs.exists()){
            dirs.delete();
        }
        dirs.mkdirs();
        File uploadPicture=new File(filePath,fileName);
        try{
            uploadPicture.createNewFile();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        Map map2=new HashMap();
        try{
            picture.transferTo(uploadPicture);
        }catch (Exception e){
            e.printStackTrace();
            map2.put("msg","头像上传失败");
            return map2;
        }
        map2.put("msg","注册成功");
        return map2;

    }


    //last:6-6
//	@RequestMapping("/searchQuestion")
//	@ResponseBody
//	public Map searchQuestion(@RequestParam("keyword") String keyword){
//		List<Question> questions=questionService.search(keyword);
//		Map map=new HashMap();
//		map.put("questions",questions);
//
//		return map;
//	}

    //last:6-16
    @RequestMapping("/showUserInfo")
    @ResponseBody
    @ApiOperation(value = "得到用户信息")
    public Map showUserInfo(HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        Map map=new HashMap();
        map.put("userInfo",userInfo);
        return map;
    }

    @RequestMapping("/showClassCollection")
    @ResponseBody
    @ApiOperation(value = "显示收藏的课程")
    public Map showClassCollection(HttpSession session){
        Map map=new HashMap();
        UserInfo userInfo=(UserInfo) session.getAttribute("userInfo");
        List<ClassVo> classVos=new ArrayList<>();
        String class_ids[]=new String[100];
        if(userInfo.getCollectionClass()==null){
            map.put("msg","无收藏");
            return map;
        }
        class_ids=userInfo.getCollectionClass().split("&");
        int length=class_ids.length;
        for(int i=0;i<length;i++){
            int id= Integer.parseInt(class_ids[i]);
            ClassInfo classInfo = classInfoService.getById(id);
            String imgPath;
            ClassVo classVo=new ClassVo();
            classVo.setId(classInfo.getId());
            classVo.setTitle(classInfo.getTitle());
            classVo.setMoney(classInfo.getPrice());
            int ii=0;
            float k=ii;
            classVo.setScore(k);
            UserInfo user = userInfoService.getById(classInfo.getUserId());
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

    @RequestMapping("/showQuestionCollection")
    @ResponseBody
    @ApiOperation(value = "显示收藏的提问")
    public Map showQuestionCollection(HttpSession session){
        Map map=new HashMap();
        List<QuestionVo> questionVos = new ArrayList<>();
        UserInfo userInfo=(UserInfo) session.getAttribute("userInfo");
        String question_ids[];
        if(userInfo.getCollectionQuestion()==null){
            map.put("msg","无收藏");
            return map;
        }
        question_ids=userInfo.getCollectionQuestion().split("&");
        int length=question_ids.length;
        for(int i=0;i<length;i++){
            int id= Integer.parseInt(question_ids[i]);
            Question question = questionService.getById(id);
            QuestionVo questionVo=new QuestionVo();
            questionVo.setId(question.getId());
            questionVo.setTitle(question.getTitle());
            UserInfo user = userInfoService.getById(question.getUserId());
            questionVo.setUsername(user.getUsername());
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
        return map;
    }

    //last:6-16
    @RequestMapping("/showOrder_q")
    @ResponseBody
    @ApiOperation(value = "显示所选提问订单")
    public Map showOrder_q(@RequestParam("orderQId") Integer orderQId,HttpSession session){
        OrderQ order_q=order_qService.getById(orderQId);

        Map map=new HashMap();
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        if(userInfo.getId().equals(order_q.getBuyerId())){
            map.put("msg","buyer");
        }
        else if(userInfo.getId().equals(order_q.getSellerId())){
            map.put("msg","seller");
        }
        Question question = questionService.getById(order_q.getQuestionId());
        List<QuestionImageInfo> questionImageInfos = questionImageInfoService.list(new QueryWrapper<QuestionImageInfo>().eq("question_id",question.getId()));
        List<String> imgs = new ArrayList<>();
        for(QuestionImageInfo questionImageInfo:questionImageInfos){
            String img = "/img/image/questionImage/" + question.getId()+"/";
            img += questionImageInfo.getId()+"."+questionImageInfo.getSuffix();
            imgs.add(img);
        }
        question.setImgPath(imgs);
        UserInfo buyer = userInfoService.getById(order_q.getBuyerId());
        String graph=buyer.getGraphId();
        buyer.setUserImg("/img/userImage/"+buyer.getId()+"/"+graph);
        buyer.setDomainText(categoryService.getById(buyer.getDomainId()).getDomain());
        UserInfo seller = userInfoService.getById(order_q.getSellerId());
        seller.setDomainText(categoryService.getById(seller.getDomainId()).getDomain());
        String graphs=seller.getGraphId();
        seller.setUserImg("/img/userImage/"+seller.getId()+"/"+graphs);
        map.put("orderQ",order_q);
        map.put("buyer",buyer);
        map.put("seller",seller);
        map.put("question",question);

        return map;
    }

    //last:6-7
    @RequestMapping("/showClassInfo")
    @ResponseBody
    @ApiOperation(value = "显示课程")
    public Map showClassInfo(@RequestParam("classInfo_id") Integer classInfo_id,HttpServletRequest request) {
        ClassInfo classInfo = classInfoService.getById(classInfo_id);
        List<Evaluation> evaluations = evaluationService.
                list(new QueryWrapper<Evaluation>().eq("class_id",classInfo_id));
        List<UserInfo> evaluationUsers=new ArrayList<>();
        List<EvaluationVo> evaluationVos=new ArrayList<>();
        for(Evaluation evaluation:evaluations){
            EvaluationVo evaluationVo=new EvaluationVo();
            evaluationVo.setEvaluation(evaluation);
            UserInfo userInfo = userInfoService.getById(evaluation.getUserId());
            userInfo.setUserImg("/img/userImage/"+userInfo.getId()+"/"+userInfo.getGraphId());
            evaluationVo.setUserInfo(userInfo);
            evaluationVos.add(evaluationVo);
//            evaluationUsers.add(userInfoService.getById(evaluation.getUserId()));
        }
        List<ClassImageInfo> classImageInfos=classImageInfoService.
                list(new QueryWrapper<ClassImageInfo>().eq("class_info_id",classInfo_id));
        String filePath;
        filePath="/img/image/classImage/"+classInfo_id+"/";
//		filePath = request.getSession().getServletContext()
//				.getRealPath("img/classImage/"+classInfo_id+"/");
//		String path="D:\\SdData\\img\\classImage\\";
//		path+=classInfo_id+"\\";
        Category category=categoryService.getById(classInfo.getDomainId());
        UserInfo user=userInfoService.getById(classInfo.getUserId());
        classInfo.setCateText(category.getDomain());
        user.setUserImg("/img/userImage/"+user.getId()+"/"+user.getGraphId());
        user.setDomainText(category.getDomain());
        List<String> imgPath=new ArrayList<>();
        Map map=new HashMap();
        if(classImageInfos.size()==0){
            imgPath.add("/img/image/0.jpg");
            map.put("classInfo",classInfo);
            map.put("evaluationVos",evaluationVos);
            map.put("category",category);
            map.put("userInfo",user);
            map.put("imgPath",imgPath);
            return map;
        }
        for(ClassImageInfo classImageInfo:classImageInfos){
            imgPath.add(filePath+classImageInfo.getId() + "." + classImageInfo.getSuffix());
        }


        map.put("classInfo",classInfo);
        map.put("evaluationVos",evaluationVos);
        map.put("category",category);
        map.put("userInfo",user);
        map.put("imgPath",imgPath);

        return map;
    }
    //last:6-8
    @RequestMapping("/showOrder")
    @ResponseBody
    @ApiOperation(value = "显示所选订单")
    public Map showOrder(@RequestParam("orderId") Integer order_id,HttpSession session){
        OrderS order_=orderService.getById(order_id);
        Map map=new HashMap();
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        if(userInfo.getId().equals(order_.getBuyerId())){
            map.put("msg","buyer");
        }
        else if(userInfo.getId().equals(order_.getSellerId())){
            map.put("msg","seller");
        }
        ClassInfo classInfo = classInfoService.getById(order_.getClassId());
        List<ClassImageInfo> classImageInfos = classImageInfoService.list(new QueryWrapper<ClassImageInfo>().eq("class_info_id",classInfo.getId()));
        List<String> imgs = new ArrayList<>();
        for(ClassImageInfo classImageInfo:classImageInfos){
            String img = "/img/image/classImage/" + classInfo.getId()+"/";
            img += classImageInfo.getId()+"."+classImageInfo.getSuffix();
            imgs.add(img);
        }
        classInfo.setImgPath(imgs);
        UserInfo buyer = userInfoService.getById(order_.getBuyerId());
        String graph=buyer.getGraphId();
        buyer.setUserImg("/img/userImage/"+buyer.getId()+"/"+graph);
        buyer.setDomainText(categoryService.getById(buyer.getDomainId()).getDomain());
        UserInfo seller = userInfoService.getById(order_.getSellerId());
        seller.setDomainText(categoryService.getById(seller.getDomainId()).getDomain());
        String graphs=seller.getGraphId();
        seller.setUserImg("/img/userImage/"+seller.getId()+"/"+graphs);

        EvaluationVo evaluationVo = new EvaluationVo();
        Evaluation evaluation = evaluationService.getOne(new QueryWrapper<Evaluation>()
                .eq("order_id",order_id));
        evaluationVo.setEvaluation(evaluation);
        evaluationVo.setUserInfo(buyer);

        map.put("order_",order_);
        map.put("buyer",buyer);
        map.put("seller",seller);
        map.put("evaluationVo",evaluationVo);
        map.put("classInfo",classInfo);

        return map;
    }

    //last:6-6
    //详细
    @RequestMapping("/showQuestion")
    @ResponseBody
    @ApiOperation(value = "显示提问")
    public Map showQuestion(@RequestParam("question_id")Integer QuestionId,HttpServletRequest request){
        Question question=questionService.getById(QuestionId);
        Category category=categoryService.getById(question.getDomainId());
        List<Comment> comments=commentService.list(new QueryWrapper<Comment>().eq("question_id",question.getId()));
//        List<Comment> comments1=new ArrayList<>();
        UserInfo userInfo=userInfoService.getById(question.getUseId());
        question.setCateText(category.getDomain());
        userInfo.setUserImg("/img/userImage/"+userInfo.getId()+"/"+userInfo.getGraphId());
        userInfo.setDomainText(category.getDomain());
        List<CommentVo> commentVos = new ArrayList<>();
        int replay_id;
        for(Comment comment:comments){
            CommentVo commentVo=new CommentVo();
            replay_id=comment.getReplayId();
            UserInfo userInfo1 = userInfoService.getById(comment.getUserId());
            userInfo1.setUserImg("/img/userImage/"+userInfo1.getId()+"/"+userInfo1.getGraphId());
            commentVo.setUserInfo(userInfo1);
            if(replay_id!=-1){
                Comment comment1=commentService.getOne(new QueryWrapper<Comment>().eq("id",replay_id));
                UserInfo userInfo2=userInfoService.getById(comment1.getUserId());
                String name=userInfo2.getUsername();
                String content=comment.getContent();
                comment.setContent("@"+name+": "+content);
            }
            commentVo.setComment(comment);
            commentVos.add(commentVo);
        }
        List<QuestionImageInfo> questionImageInfos=questionImageInfoService.
                list(new QueryWrapper<QuestionImageInfo>().eq("question_id",QuestionId));
        String filePath;
        filePath="/img/image/questionImage/"+QuestionId+"/";
//		filePath = request.getSession().getServletContext()
//				.getRealPath("img/questionImage/"+QuestionId+"/");
//		String path="D:\\SdData\\img\\questionImage\\";
//		path+=QuestionId+"\\";
        List<String> imgPath=new ArrayList<>();
        Map map=new HashMap();
        if(questionImageInfos.size()==0){
            imgPath.add("/img/image/0.jpg");
            map.put("question",question);
            map.put("category",category);
            map.put("commentVos",commentVos);
            map.put("userInfo",userInfo);
            map.put("imgPath",imgPath);
        }
        for(QuestionImageInfo questionImageInfo:questionImageInfos){
            imgPath.add(filePath+questionImageInfo.getId() + "." + questionImageInfo.getSuffix());
        }

        map.put("question",question);
        map.put("category",category);
        map.put("commentVos",commentVos);
        map.put("userInfo",userInfo);
        map.put("imgPath",imgPath);
        return map;

    }

    @RequestMapping("/sortQuestion")
    @ResponseBody
    @ApiOperation(value = "求助排序")
    public Map sortQuestion(@RequestParam("sort") Integer sort,
                            @RequestParam("keyword") String keyword,int pageNum,HttpServletRequest request) {
        List<Question> questions = questionService.list(new QueryWrapper<Question>().like("title",keyword));
        String filePath;
        filePath=defaultPath+"questionImage/";
//		filePath = request.getSession().getServletContext()
//				.getRealPath("img/questionImage/");
//		String path="D:\\SdData\\img\\questionImage\\";
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
                userInfos.add(userInfoService.getById(question.getUseId()));
                List<QuestionImageInfo> questionImageInfos=questionImageInfoService.
                        list(new QueryWrapper<QuestionImageInfo>().eq("question_id",question.getId()));
                if(questionImageInfos.size()!=0){
                    imgPath.add(filePath+question.getId()+"/"+questionImageInfos.get(0).getId()+".jpg");
                }
                else{
                    imgPath.add(defaultPath+"questionImage/defaultImg.jpg");
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
    //last:6-16
    @RequestMapping("/sortClassInfo")
    @ResponseBody
    @ApiOperation(value = "课程排序")
    public Map sortClassInfo(@RequestParam("sort") Integer sort,
                             @RequestParam("keyword") String keyword,
                             @RequestParam(value = "categoryId",required = false) String categoryId,
                             int pageNum,
                             HttpServletRequest request) {
        List<ClassInfo> classInfos;
        if(categoryId == null){
            classInfos = classInfoService.list(new QueryWrapper<ClassInfo>().like("title",keyword));
        }
        else{
            classInfos = classInfoService.list(new QueryWrapper<ClassInfo>().like("title",keyword).
                    eq("domain_id",categoryId));
        }
        String filePath;
        filePath="/img/image/classImage/";
//		filePath = request.getSession().getServletContext()
//				.getRealPath("img/classImage/");
//		String path="D:\\SdData\\img\\classImage\\";
        Map map=new HashMap();
        int page=classInfos.size()/10;
        int lastPage=classInfos.size()%10;
        if (0 != sort) {
            switch (sort) {
                case 3:
                    Collections.sort(classInfos, Comparator.comparing(ClassInfo::getSuggestTime));
                    break;

                case 1:
                    Collections.sort(classInfos, Comparator.comparing(ClassInfo::getPrice));
                    break;
            }
        }
        if(lastPage!=0){
            page++;
        }
        if(classInfos.size() == 0){
            map.put("resultNum",0);
            map.put("pages",0);
            return map;
        }
        for(int i=0;i<page;i++){
            if(i!=pageNum-1){
                continue;
            }
            List<ClassInfo> classInfos1=new ArrayList<>();
            List<String> imgPath=new ArrayList<>();
            List<UserInfo> userInfos=new ArrayList<>();
            int num=10;
            if(i==page-1&&lastPage!=0){
                num=lastPage;
            }
            for(int j=0;j<num;j++){
                ClassInfo classInfo=classInfos.get(i*10+j);
                classInfos1.add(classInfo);
                userInfos.add(userInfoService.getById(classInfo.getUserId()));
                List<ClassImageInfo> classImageInfos=classImageInfoService.
                        list(new QueryWrapper<ClassImageInfo>().eq("class_info_id",classInfo.getId()));
                if(classImageInfos.size()!=0){
                    imgPath.add(filePath+classInfo.getId()+"/"+classImageInfos.get(0).getId()+"."+classImageInfos.get(0).getSuffix());
                }
                else{
                    imgPath.add("/img/image/0.jpg");
                }
            }
            //classInfoss.add(classInfos1);
            map.put("resultNum",classInfos.size());
            map.put("classInfos",classInfos1);
            map.put("pages",page);
            map.put("imgPath",imgPath);
            map.put("userInfos",userInfos);
            break;
        }

        return map;
    }

    //last:6-7
    @RequestMapping("/searchClassInfo")
    @ResponseBody
    @ApiOperation(value = "根据关键词搜索课程")
    public Map searchClassInfo(@RequestParam("keyword") String keyword,
                               @RequestParam(value = "categoryId",required = false) String categoryId,
                               int pageNum,
                               HttpServletRequest request) {

        List<ClassInfo> classInfos;
        if(categoryId == null){
            classInfos = classInfoService.list(new QueryWrapper<ClassInfo>().like("title",keyword));
        }
        else{
            classInfos = classInfoService.list(new QueryWrapper<ClassInfo>().like("title",keyword).
                    eq("domain_id",categoryId));
        }
        Map map=new HashMap();
        //List<List<ClassInfo>> classInfoss=new ArrayList<>();
        int page=classInfos.size()/10;
        int lastPage=classInfos.size()%10;
        if(lastPage!=0){
            page++;
        }
        if(classInfos.size() == 0){
            map.put("resultNum",0);
            map.put("pages",0);
            return map;
        }
        for(int i=0;i<page;i++){
            if(i!=pageNum-1){
                continue;
            }
            List<ClassInfo> classInfos1=new ArrayList<>();
            List<UserInfo> userInfos=new ArrayList<>();
            List<String> imgPath=new ArrayList<>();
            int num=10;
            if(i==page-1&&lastPage!=0){
                num=lastPage;
            }
            String filePath;
            filePath="/img/image/classImage/";
//			filePath = request.getSession().getServletContext()
//					.getRealPath("img/classImage/");
//			String path="D:\\SdData\\img\\classImage\\";
            for(int j=0;j<num;j++){
                ClassInfo classInfo=classInfos.get(i*10+j);
                classInfos1.add(classInfo);
                userInfos.add(userInfoService.getById(classInfo.getUserId()));
                List<ClassImageInfo> classImageInfos=classImageInfoService.
                        list(new QueryWrapper<ClassImageInfo>().eq("class_info_id",classInfo.getId()));
                if(classImageInfos.size()!=0){
                    imgPath.add(filePath+classInfo.getId()+"/"+classImageInfos.get(0).getId()+"."+classImageInfos.get(0).getSuffix());
                }
                else{
                    imgPath.add("/img/image/0.jpg");
                }

            }
            map.put("resultNum",classInfos.size());
            map.put("classInfos",classInfos1);
            map.put("userInfos",userInfos);
            map.put("pages",page);
            map.put("imgPath",imgPath);
            break;
        }
        return map;
    }
    //last:6-7
    @RequestMapping("/searchQuestion")
    @ResponseBody
    @ApiOperation(value = "根据关键词搜索提问")
    public Map searchQuestion(@RequestParam("keyword") String keyword,
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
        Map map=new HashMap();
        //List<List<ClassInfo>> classInfoss=new ArrayList<>();
        int page=questions.size()/10;
        int lastPage=questions.size()%10;
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
            List<UserInfo> userInfos=new ArrayList<>();
            List<String> imgPath=new ArrayList<>();
            int num=10;
            if(i==page-1&&lastPage!=0){
                num=lastPage;
            }
            String filePath;
            filePath="/img/image/questionImage/";
//			filePath = request.getSession().getServletContext()
//					.getRealPath("img/classImage/");
//			String path="D:\\SdData\\img\\classImage\\";
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
            map.put("resultNum",questions.size());
            map.put("questions",questions1);
            map.put("userInfos",userInfos);
            map.put("pages",page);
            map.put("imgPath",imgPath);
            break;
        }
        return map;
    }

    //last:6-6
    @RequestMapping("/sendInfoForPostClassInfo")
    @ResponseBody
    @ApiOperation(value = "发布课程需要的标签")
    public Map sendInfoForPostClassInfo(){
        List<Category> categories=categoryService.list();

        Map map=new HashMap();
        map.put("categories",categories);

        return map;
    }
    //last:6-6
    @RequestMapping("/sendInfoForPostQuestion")
    @ResponseBody
    @ApiOperation(value = "发布提问需要的标签")
    public Map sendInfoForPostQuestion(){
        List<Category> categories=categoryService.list();

        Map map=new HashMap();
        map.put("categories",categories);

        return map;
    }
    //last:6-13
//	@RequestMapping("/sendInfoForEditQuestion")
//	@ResponseBody
//	@ApiOperation(value = "")
//	public Map sendInfoForEditQuestion(){
//		List<Category> categories=categoryService.list();
//
//		Map map=new HashMap();
//		map.put("categories",categories);
//
//		return map;
//	}


    //6.8 wzh新增
    @RequestMapping("/updateUserImage")
    @ResponseBody
    @ApiOperation(value = "更换头像")
    public Map updateUserImage(HttpSession session,MultipartFile picture,
                               HttpServletRequest request){
        Map map=new HashMap();
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        Long time=System.currentTimeMillis();

        String filePath;
        filePath=defaultPath+"userImage/"+userInfo.getId();
//		filePath= request.getSession().getServletContext()
//				.getRealPath("img/userImage/"+userInfo.getId());
//		String filePath = "D:\\SdData\\img\\userImage\\"+userInfo.getId();
        String fileName = time + ".jpg";
        userInfo.setGraphId(filePath+"/"+fileName);
        File uploadImg=new File(filePath,fileName);


        try{
            uploadImg.createNewFile();
            picture.transferTo(uploadImg);
        }
        catch (IOException ioException){
            ioException.printStackTrace();
            map.put("msg","fail");
            return map;
        }
        map.put("msg","success");
        return map;
    }

    //6.10 wzh新增
    @RequestMapping("/updateImage")
    @ResponseBody
    @ApiOperation(value = "更新图片，flag为0是课程，flag为1是提问")
    public Map updateImage(HttpSession session,
                           Integer flag,
                           HttpServletRequest request,
                           MultipartFile[] pictures,
                           Integer class_id){
        Map map=new HashMap();
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        if(flag==0){
            postImage(request,pictures,class_id,0);
            List<ClassImageInfo> classImageInfos=classImageInfoService.list(new QueryWrapper<ClassImageInfo>().eq("class_info_id",class_id));
            map.put("classImageInfos",classImageInfos);
        }
        else{
            postImage(request,pictures,class_id,1);
            List<QuestionImageInfo> questionImageInfos=questionImageInfoService.list(new QueryWrapper<QuestionImageInfo>().eq("class_info_id",class_id));
            map.put("questionImageInfos",questionImageInfos);
        }
        return map;
    }

    @RequestMapping("/updateVideo")
    @ResponseBody
    @ApiOperation(value = "更新视频，flag为0是课程，flag为1是提问")
    public Map updateVideo(HttpSession session,
                           Integer flag,
                           HttpServletRequest request,
                           MultipartFile[] videos,
                           Integer class_id){
        Map map=new HashMap();
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        if(flag==0){
            postVideo(request,videos,class_id,0);
            List<ClassVideoInfo> classVideoInfos=classVideoInfoService.list(new QueryWrapper<ClassVideoInfo>().eq("class_info_id",class_id));
            map.put("classVideoInfos",classVideoInfos);
        }
        else{
            postVideo(request,videos,class_id,1);
            List<QuestionVideoInfo> questionVideoInfos=questionVideoInfoService.list(new QueryWrapper<QuestionVideoInfo>().eq("class_info_id",class_id));
            map.put("questionVideoInfos",questionVideoInfos);
        }
        return map;
    }



    //last:6-8
    @RequestMapping("/want")
    @ResponseBody
    @ApiOperation(value = "对课程有意向")
    public Map want(@RequestParam("classInfo_id") Integer classInfo_id,HttpSession session){
        ClassInfo classInfo=classInfoService.getById(classInfo_id);
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");

        OrderS order_=new OrderS();
        order_.setOrderStatus("0");
        order_.setBuyerId(userInfo.getId());
        order_.setSellerId(classInfo.getUserId());
        order_.setClassId(classInfo_id);
        order_.setPrice(classInfo.getPrice());
        order_.setSuggestTime(classInfo.getSuggestTime());
        order_.setCreateDate(new Date());
        order_.setScore(0);
        order_.setBuyerStatus(0);
        order_.setSellerStatus(0);
        orderService.save(order_);

        Map map=new HashMap();
        map.put("order_",order_);
        map.put("buyer",userInfo);
        map.put("seller",userInfoService.getById(order_.getSellerId()));
        map.put("classInfo",classInfoService.getById(classInfo_id));

        return map;
    }
    //last:6-16
    @RequestMapping("/wantQ")
    @ResponseBody
    @ApiOperation(value = "对提问有意向")
    public Map wantQ(@RequestParam("question_id") Integer question_id,HttpSession session){
        Question question=questionService.getById(question_id);
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");

        OrderQ order_q=new OrderQ();
        order_q.setOrderStatus("0");
        order_q.setBuyerId(userInfo.getId());
        order_q.setSellerId(question.getUseId());
        order_q.setQuestionId(question_id);
        order_q.setPrice(question.getPrice());
        order_q.setSuggestTime(question.getSuggestTime());
        order_q.setCreateDate(new Date());
        order_q.setScore(0);
        order_q.setBuyerStatus(0);
        order_q.setSellerStatus(0);

        order_qService.save(order_q);

        Map map=new HashMap();
        map.put("order_q",order_q);
        map.put("buyer",userInfo);
        map.put("seller",userInfoService.getById(order_q.getSellerId()));
        map.put("question",questionService.getById(question_id));

        return map;
    }

}

