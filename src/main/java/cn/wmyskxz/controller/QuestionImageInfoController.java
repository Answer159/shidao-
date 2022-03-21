package cn.wmyskxz.controller;


import cn.wmyskxz.entity.ClassImageInfo;
import cn.wmyskxz.entity.QuestionImageInfo;
import cn.wmyskxz.service.ClassImageInfoService;
import cn.wmyskxz.service.QuestionImageInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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
@RequestMapping("/shidao/questionImageInfo")
public class QuestionImageInfoController {
    @Value("${prop.upload-folder}")
    private String UPLOAD_FOLDER;
    @Resource
    private QuestionImageInfoService questionImageInfoService;
    @RequestMapping("/postImage")
    @ResponseBody
    public Map postImage(HttpServletRequest request, MultipartFile picture, int questionId){
        Map map = new HashMap();
        String filePath;
        filePath=UPLOAD_FOLDER+"img/image/";
        filePath += "questionImage/"+questionId+"/";
        File dirs=new File(filePath);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        String name=picture.getOriginalFilename();
        String[]names=name.split("\\.");
        String suffix = names[1];
        QuestionImageInfo questionImageInfo=new QuestionImageInfo();
        questionImageInfo.setQuestionId(questionId);
        questionImageInfo.setStatus(1);
        questionImageInfo.setSuffix(suffix);
        questionImageInfoService.save(questionImageInfo);
        int graph_id=questionImageInfo.getId();
        String fileName = graph_id + "." + suffix;
        File uploadPicture = new File(filePath, fileName);
        List<QuestionImageInfo> questionImageInfoList = questionImageInfoService.list(
                new QueryWrapper<QuestionImageInfo>().eq("question_id",questionId)
        );
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
            picture.transferTo(uploadPicture);
        } catch (Exception e) {
            map.put("msg","fail");
        }
        List<String> imgPaths = new ArrayList<>();
        for(QuestionImageInfo questionImageInfo1: questionImageInfoList){
            String imgPath = "img/image/questionImage/"+questionId+"/"+questionImageInfo1.getId()+"."+questionImageInfo1.getSuffix();
            imgPaths.add(imgPath);
        }
        map.put("msg","success");
        map.put("imgPaths",imgPaths);
        return map;
    }
    @RequestMapping("/removeImage")
    @ResponseBody
    public Map removeImage(String path){
        String[] paths = path.split("/");
        int length = paths.length;
        int questionId = Integer.parseInt(paths[length-2]);
        int imageId = Integer.parseInt(paths[length-1].split("\\.")[0]);
        Map map = new HashMap();
        QuestionImageInfo questionImageInfo = questionImageInfoService.getById(imageId);
        String filePath;
        filePath=UPLOAD_FOLDER+"img/image/";
        filePath += "questionImage/"+questionId+"/";
        int graph_id=questionImageInfo.getId();
        filePath +=graph_id+ "."+questionImageInfo.getSuffix();
        new File(filePath).delete();
        questionImageInfoService.removeById(questionImageInfo.getId());
        List<QuestionImageInfo> questionImageInfoList = questionImageInfoService.list(
                new QueryWrapper<QuestionImageInfo>().eq("question_id",questionId)
        );
        List<String> imgPaths = new ArrayList<>();
        for(QuestionImageInfo questionImageInfo1: questionImageInfoList){
            String imgPath = "/img/image/questionImage/"+questionId+"/"+questionImageInfo1.getId()+"."+questionImageInfo1.getSuffix();
            imgPaths.add(imgPath);
        }
        map.put("msg","success");
        map.put("imgPaths",imgPaths);
        return map;
    }
}

