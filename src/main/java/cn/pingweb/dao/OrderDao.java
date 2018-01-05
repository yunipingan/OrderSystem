package cn.pingweb.dao;

import cn.pingweb.order.Order;

import java.util.List;

/**
 * Created by zhuyuping on 2018/1/3.
 */
public interface OrderDao {
    Order queryBy(String id);
    boolean updateOrder(Order order);
    List<Order> queryUnpaidOrder();
}
