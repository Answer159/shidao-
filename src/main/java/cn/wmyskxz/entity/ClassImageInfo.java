package cn.wmyskxz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("ClassImageInfo")
@Getter
@Setter
public class ClassImageInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer classInfoId;

    private Integer status;

    private String suffix;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassInfoId() {
        return classInfoId;
    }

    public void setClassInfoId(Integer classInfoId) {
        this.classInfoId = classInfoId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassImageInfo{" +
        "id=" + id +
        ", classInfoId=" + classInfoId +
        ", status=" + status +
        "}";
    }
}
