package cn.wmyskxz.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class QuestionVo {
    private Integer id;
    private String img;
    private String title;
    private String username;
    private Float money;
}
