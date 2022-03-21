package cn.wmyskxz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
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
@Data
@Getter
@Setter
public class Question implements Serializable {

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

    private String questionContent;

    private Integer status;

    private Integer likes;

    private Integer commentNum;
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

    public Integer getUseId() {
        return userId;
    }

    public void setUseId(Integer useId) {
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

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public Integer getStatu() {
        return status;
    }

    public void setStatu(Integer statu) {
        this.status = statu;
    }

    public Integer getLike() {
        return likes;
    }

    public void setLike(Integer like) {
        this.likes = like;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    @Override
    public String toString() {
        return "Question{" +
        "id=" + id +
        ", domainId=" + domainId +
        ", title=" + title +
        ", price=" + price +
        ", suggestTime=" + suggestTime +
        ", textIntro=" + textIntro +
        ", userId=" + userId +
        ", frontKnowledge=" + frontKnowledge +
        ", tools=" + tools +
        ", questionContent=" + questionContent +
        ", status=" + status +
        ", likes=" + likes +
        ", commentNum=" + commentNum +
        "}";
    }
}
