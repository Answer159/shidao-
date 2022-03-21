package cn.wmyskxz.service.impl;

import cn.wmyskxz.entity.Comment;
import cn.wmyskxz.mapper.CommentMapper;
import cn.wmyskxz.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wzh
 * @since 2022-02-17
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
