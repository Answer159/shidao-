package cn.wmyskxz.service.impl;

import cn.wmyskxz.entity.OrderS;
import cn.wmyskxz.entity.OrderQ;
import cn.wmyskxz.mapper.OrderQMapper;
import cn.wmyskxz.service.OrderQService;
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
public class OrderQServiceImpl extends ServiceImpl<OrderQMapper, OrderQ> implements OrderQService {
    @Resource
    OrderQMapper order_qmapper;
    @Override
    public void delete(Integer id,int user_id){
        OrderQ order_q=order_qmapper.selectById(id);
        if(order_q.getBuyerId()==user_id){
            order_q.setBuyerStatus(-1);
        }
        else if(order_q.getSellerId()==user_id){
            order_q.setSellerStatus(-1);
        }
        order_qmapper.updateById(order_q);
    }
    @Override
    public List<OrderQ> listByStatus(Integer user_id, String status, String identity) {
        QueryWrapper<OrderQ> example=new QueryWrapper();
        if(identity=="buyer"){
            example.eq("buyer_id",user_id).eq("order_status",status).eq("buyer_status",1);
            return order_qmapper.selectList(example);
        }
        else if(identity=="seller"){
            example.eq("seller_id",user_id).eq("order_status",status).eq("buyer_status",1);
            return order_qmapper.selectList(example);
        }
        else{
            example.eq("order_status",status);
            return order_qmapper.selectList(example);
        }
    }
}
