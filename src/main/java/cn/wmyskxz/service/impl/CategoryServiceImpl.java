package cn.wmyskxz.service.impl;

import cn.wmyskxz.entity.Category;
import cn.wmyskxz.mapper.CategoryMapper;
import cn.wmyskxz.service.CategoryService;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
