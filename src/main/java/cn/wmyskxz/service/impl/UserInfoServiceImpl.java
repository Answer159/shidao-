package cn.wmyskxz.service.impl;

import cn.wmyskxz.entity.UserInfo;
import cn.wmyskxz.mapper.UserInfoMapper;
import cn.wmyskxz.service.UserInfoService;
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
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
