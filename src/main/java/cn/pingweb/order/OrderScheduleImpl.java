package cn.pingweb.order;

import cn.pingweb.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by zhuyuping on 2018/1/3.
 */
@Component
public class OrderScheduleImpl implements CommandLineRunner, OrderSchedule, UnpaidOrderCache{

    private volatile static ScheduledExecutorService scheduledExecutorService;
    private volatile static OrderScheduleImpl orderSchedule;
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
    public static OrderScheduleImpl getInstance() {
        if (null == orderSchedule) {
            synchronized (OrderScheduleImpl.class) {
                if (null == orderSchedule) {
                    orderSchedule = new OrderScheduleImpl();
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
            this.startUnpaidOrderTask(new UnpaidOrderTask(order, this));
        }
    }

    // 用户未付款才会触发
    @Override
    public void startUnpaidOrderTask(UnpaidOrderTask unpaidOrderTask){
        Order order = unpaidOrderTask.getOrder();
        long duration = order.getDuration();
        Future future = scheduledExecutorService.schedule(unpaidOrderTask, duration, unpaidOrderTask.getTimeUnit());
        unpaidOrderTask.setFuture(future);
        orderFailureMap.put(order.getId(), unpaidOrderTask);
    }

    // 用户付款成功，取消订单失效的定时任务
    @Override
    public boolean cancelUnpaidOrderTask(String orderId) {

        UnpaidOrderTask unpaidOrderTask = orderFailureMap.get(orderId);
        removeMapCahce(orderId);
        return unpaidOrderTask.removeUnpaidOrder();
    }

    private String getKey(String orderId) {
        return orderId;
    }

    @Override
    public void removeMapCahce(String orderId) {
        orderFailureMap.remove(getKey(orderId));
    }

//    // 用户付款成功，取消订单失效的定时任务
//    public boolean cancelUnpaidOrderTask(UnpaidOrderTask unpaidOrderTask) {
//
//        Future future = unpaidOrderTask.getFuture();
//        if (!future.isDone()) {
//            return future.cancel(true);
//        }
//        return false;
//    }

    @Override
    public void run(String... strings) throws Exception {
        loadUnpaidOrderTask();
    }
}
