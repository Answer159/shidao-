package cn.wmyskxz.vo;

import cn.wmyskxz.entity.Comment;
import cn.wmyskxz.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentVo {
    private Comment comment;
    private UserInfo userInfo;
}
