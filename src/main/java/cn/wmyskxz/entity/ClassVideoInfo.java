package cn.wmyskxz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wzh
 * @since 2022-02-17
 */
@TableName("ClassVideoInfo")
public class ClassVideoInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer classInfoId;

    private Integer status;


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
        return "ClassVideoInfo{" +
        "id=" + id +
        ", classInfoId=" + classInfoId +
        ", status=" + status +
        "}";
    }
}
