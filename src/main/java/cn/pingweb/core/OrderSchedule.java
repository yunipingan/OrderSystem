package cn.pingweb.core;

import cn.pingweb.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by zhuyuping on 2018/1/3.
 */
@Component
public class OrderSchedule implements CommandLineRunner, UnpaidOrderCache{

    private volatile static ScheduledExecutorService scheduledExecutorService;
    private volatile static OrderSchedule orderSchedule;
    // 不用缓存，从数据库里取的话需要重新new，无法lock同一对象
    private static ConcurrentHashMap<String, UnpaidOrderTask> orderFailureMap = new ConcurrentHashMap<String, UnpaidOrderTask>();

    static {
        scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Autowired
    private OrderDao orderDao;

    /**
     * 改成spring的bean
     * @return
     */
    public static OrderSchedule getInstance() {
        if (null == orderSchedule) {
            synchronized (OrderSchedule.class) {
                if (null == orderSchedule) {
                    orderSchedule = new OrderSchedule();
                }
            }
        }
        return orderSchedule;
    }

    /**
     *
     * 可以实现接口CommandLineRunner
     * 初始化已经开启的定时任务，但是未执行存入库里面的
     */
    private void loadUnpaidOrderTask(){
        List<Order> orderList = orderDao.queryUnpaidOrder();
        for(Order order : orderList) {
            this.start(new UnpaidOrderTask(order, this));
        }
    }

    // 用户未付款才会触发
    public void start(UnpaidOrderTask orderFailure){
        Order order = orderFailure.getOrder();
        long duration = order.getDuration();
        Future future = scheduledExecutorService.schedule(orderFailure, duration, orderFailure.getTimeUnit());
        orderFailure.setFuture(future);
        orderFailureMap.put(order.getId(), orderFailure);
    }

    // 用户付款成功，取消订单失效的定时任务
    public boolean removeUnpaidOrder(String orderId) {

        UnpaidOrderTask orderFailure = orderFailureMap.get(orderId);
        removeUnpaidOrder(orderId);
        return orderFailure.removeUnpaidOrder();
    }

    private String getKey(String orderId) {
        return orderId;
    }

    @Override
    public void removeMapCahce(String orderId) {
        orderFailureMap.remove(getKey(orderId));
    }

    // 用户付款成功，取消订单失效的定时任务
    public boolean cancelUnpaidOrder(UnpaidOrderTask unpaidOrderTask) {

        Future future = unpaidOrderTask.getFuture();
        if (!future.isDone()) {
            return future.cancel(true);
        }
        return false;
    }

    @Override
    public void run(String... strings) throws Exception {
        loadUnpaidOrderTask();
    }
}
