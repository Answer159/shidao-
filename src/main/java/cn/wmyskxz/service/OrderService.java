package cn.wmyskxz.service;

import cn.wmyskxz.entity.OrderS;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wzh
 * @since 2022-02-16
 */
public interface OrderService extends IService<OrderS> {
    void delete(Integer id,int user_id);

    List<OrderS> listByStatus(Integer user_id, String status, String identity);
}
