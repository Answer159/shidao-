package cn.wmyskxz.controller;


import cn.wmyskxz.entity.ClassImageInfo;
import cn.wmyskxz.entity.QuestionImageInfo;
import cn.wmyskxz.service.ClassImageInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/shidao/classImageInfo")
public class ClassImageInfoController {
    @Value("${prop.upload-folder}")
    private String UPLOAD_FOLDER;
    @Resource
    private ClassImageInfoService classImageInfoService;
    @RequestMapping("/postImage")
    @ResponseBody
    public Map postImage(HttpServletRequest request, MultipartFile picture, int class_id){
        Map map = new HashMap();
        String filePath;
        filePath=UPLOAD_FOLDER+"img/image/";
            filePath += "classImage/"+class_id+"/";
        File dirs=new File(filePath);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        String name=picture.getOriginalFilename();
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
        List<ClassImageInfo> classImageInfoList = classImageInfoService.list(
                new QueryWrapper<ClassImageInfo>().eq("class_info_id",class_id)
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
        for(ClassImageInfo classImageInfo1: classImageInfoList){
            String imgPath = "img/image/classImage/"+class_id+"/"+classImageInfo1.getId()+"."+classImageInfo1.getSuffix();
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
        int class_id = Integer.parseInt(paths[length-2]);
        int imageId = Integer.parseInt(paths[length-1].split("\\.")[0]);
        Map map = new HashMap();
        ClassImageInfo classImageInfo = classImageInfoService.getById(imageId);
        String filePath;
        filePath=UPLOAD_FOLDER+"img/image/";
        filePath += "classImage/"+class_id+"/";
        int graph_id=classImageInfo.getId();
        filePath +=graph_id+ "."+classImageInfo.getSuffix();
        new File(filePath).delete();
        classImageInfoService.removeById(classImageInfo.getId());
        List<ClassImageInfo> classImageInfoList = classImageInfoService.list(
                new QueryWrapper<ClassImageInfo>().eq("class_info_id",class_id)
        );
        List<String> imgPaths = new ArrayList<>();
        for(ClassImageInfo classImageInfo1: classImageInfoList){
            String imgPath = "/img/image/classImage/"+class_id+"/"+classImageInfo1.getId()+"."+classImageInfo1.getSuffix();
            imgPaths.add(imgPath);
        }
        map.put("msg","success");
        map.put("imgPaths",imgPaths);
        return map;
    }
}

