package cn.wmyskxz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author wzh
 * @since 2022-02-16
 */
@Getter
@Setter
public class Comment implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String content;

    private Integer userId;

    private Integer questionId;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createDate;

    private Integer likes;

    private Integer replayNum;

    private Integer replayId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUseId() {
        return userId;
    }

    public void setUseId(Integer useId) {
        this.userId = useId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getLike() {
        return likes;
    }

    public void setLike(Integer like) {
        this.likes = like;
    }

    public Integer getReplayNum() {
        return replayNum;
    }

    public void setReplayNum(Integer replayNum) {
        this.replayNum = replayNum;
    }

    public Integer getReplayId() {
        return replayId;
    }

    public void setReplayId(Integer replayId) {
        this.replayId = replayId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content=" + content +
                ", userId=" + userId +
                ", questionId=" + questionId +
                ", createDate=" + createDate +
                ", like=" + likes +
                ", replayNum=" + replayNum +
                ", replayId=" + replayId +
                "}";
    }
}
