package cn.wmyskxz.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ClassVo {
    private Integer id;
    private String img;
    private String title;
    private String username;
    private Float money;
    private Float score;
}
