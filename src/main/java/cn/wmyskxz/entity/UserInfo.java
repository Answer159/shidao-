package cn.wmyskxz.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wzh
 * @since 2022-02-17
 */
@TableName("userInfo")
@Getter
@Setter
public class UserInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer sex;

    private Integer domainId;

    private String account;

    private String username;

    private String password;

    private String graphId;

    private String selfIntro;

    private String phone;
    @TableField (updateStrategy = FieldStrategy.IGNORED)
    private String collectionClass;
    @TableField (updateStrategy = FieldStrategy.IGNORED)
    private String collectionQuestion;
    @TableField(exist = false)
    private String gender;
    @TableField(exist = false)
    private String userImg;
    @TableField(exist = false)
    private String domainText;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollectionClass() {
        return collectionClass;
    }

    public void setCollectionClass(String collectionClass) {
        this.collectionClass = collectionClass;
    }

    public String getCollectionQuestion() {
        return collectionQuestion;
    }

    public void setCollectionQuestion(String collectionQuestion) {
        this.collectionQuestion = collectionQuestion;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
        "id=" + id +
        ", sex=" + sex +
        ", domainId=" + domainId +
        ", account=" + account +
        ", username=" + username +
        ", password=" + password +
        ", graphId=" + graphId +
        ", selfIntro=" + selfIntro +
        ", phone=" + phone +
        ", collectionClass=" + collectionClass +
        ", collectionQuestion=" + collectionQuestion +
        "}";
    }
}
