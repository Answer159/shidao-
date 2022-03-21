package cn.wmyskxz.service.impl;

import cn.wmyskxz.entity.OrderS;
import cn.wmyskxz.mapper.OrderMapper;
import cn.wmyskxz.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wzh
 * @since 2022-02-16
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderS> implements OrderService {
    @Resource
    OrderMapper order_mapper;
    @Override
    public void delete(Integer id,int user_id){
        OrderS order_=order_mapper.selectById(id);
        if(order_.getBuyerId()==user_id){
            order_.setBuyerId(-1);
        }
        else if(order_.getSellerId()==user_id){
            order_.setSellerStatus(-1);
        }
        order_mapper.updateById(order_);
    }

    @Override
    public List<OrderS> listByStatus(Integer user_id, String status, String identity) {
        QueryWrapper<OrderS> example=new QueryWrapper();
        if(identity=="buyer"){
            example.eq("buyer_id",user_id).eq("order_status",status).eq("buyer_status",1);
            return order_mapper.selectList(example);
        }
        else if(identity=="seller"){
            example.eq("seller_id",user_id).eq("order_status",status).eq("buyer_status",1);
            return order_mapper.selectList(example);
        }
        else{
            example.eq("order_status",status);
            return order_mapper.selectList(example);
        }
    }
}
