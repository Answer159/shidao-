package cn.wmyskxz.service.impl;

import cn.wmyskxz.entity.Question;
import cn.wmyskxz.mapper.QuestionMapper;
import cn.wmyskxz.service.QuestionService;
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
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

}
