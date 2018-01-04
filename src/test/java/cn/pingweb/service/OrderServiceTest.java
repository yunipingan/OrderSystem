package cn.pingweb.service;

import cn.pingweb.core.*;
import cn.pingweb.dao.OrderDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by zhuyuping on 2018/1/3.
 */

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    OrderDao orderDao;

    @Mock
    OrderSchedule orderSchedule;


    @Test
    public void placeOrder() throws Exception {

        for (int i = 0; i < 10; i++) {
            Order order = new Order("123", OrderStatus.UNPAID, 2000);
            orderSchedule.start(new UnpaidOrderTask(order, orderSchedule) {
                @Override
                public void taskResult(Order obj) {
                    System.out.println("update order");
                    orderDao.updateOrder(obj);
                }
            });

        }
        Thread.sleep(10000);
        System.out.println("end");
    }

    /*@Test
    public void payForOrder() throws Exception {
        Order order = new Order("123", OrderStatus.UNPAID, 10000);
        UnpaidOrderTask orderFailure = new UnpaidOrderTask(order, orderDao);
        OrderSchedule.getInstance().start(orderFailure);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(OrderSchedule.getInstance().cancelOrderFailure("123"));
            }
        }).start();
        Order order1 = new Order("1234", OrderStatus.UNPAID, 10000);
        OrderSchedule.getInstance().start(new UnpaidOrderTask(order1, orderDao));
        Thread.sleep(20000);
        System.out.println("end");
    }

    @Test
    public void threadPayAndCancel()throws Exception {
        Order order = new Order("123", OrderStatus.UNPAID, 4000);
        final UnpaidOrderTask orderFailure = new UnpaidOrderTask(order, orderDao);
        OrderSchedule.getInstance().start(orderFailure);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("尝试付款");
                System.out.println(orderFailure.cancelOrderFailure());
            }
        }).start();
        Thread.sleep(20000);
        System.out.println("end");
    }*/

}