package cn.wmyskxz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author wzh
 * @since 2022-02-17
 */
@TableName("classInfo")
@Getter
@Setter
public class ClassInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer domainId;

    private String title;

    private Float price;

    private Float suggestTime;

    private String textIntro;

    private Integer userId;

    private String frontKnowledge;

    private String tools;

    private String classContent;

    private Integer status;

    private Float score;

    @TableField(exist = false)
    private List<String> imgPath;
    @TableField(exist = false)
    private String cateText;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getSuggestTime() {
        return suggestTime;
    }

    public void setSuggestTime(Float suggestTime) {
        this.suggestTime = suggestTime;
    }

    public String getTextIntro() {
        return textIntro;
    }

    public void setTextIntro(String textIntro) {
        this.textIntro = textIntro;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer useId) {
        this.userId = useId;
    }

    public String getFrontKnowledge() {
        return frontKnowledge;
    }

    public void setFrontKnowledge(String frontKnowledge) {
        this.frontKnowledge = frontKnowledge;
    }

    public String getTools() {
        return tools;
    }

    public void setTools(String tools) {
        this.tools = tools;
    }

    public String getClassContent() {
        return classContent;
    }

    public void setClassContent(String classContent) {
        this.classContent = classContent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
        "id=" + id +
        ", domainId=" + domainId +
        ", title=" + title +
        ", price=" + price +
        ", suggestTime=" + suggestTime +
        ", textIntro=" + textIntro +
        ", useId=" + userId +
        ", frontKnowledge=" + frontKnowledge +
        ", tools=" + tools +
        ", classContent=" + classContent +
        ", status=" + status +
        "}";
    }
}
