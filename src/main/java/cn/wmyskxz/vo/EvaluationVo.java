package cn.wmyskxz.vo;

import cn.wmyskxz.entity.Evaluation;
import cn.wmyskxz.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationVo {
    private Evaluation evaluation;
    private UserInfo userInfo;
}
