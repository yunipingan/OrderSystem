package cn.pingweb.core;

import cn.pingweb.dao.OrderDao;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhuyuping on 2018/1/3.
 */
public class UnpaidOrderTask implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(UnpaidOrderTask.class);

    @Getter
    private TimeUnit timeUnit;
    @Getter
    private Order order;
    private Lock orderLock = new ReentrantLock();
    @Setter
    @Getter
    private Future future;
    private UnpaidOrderCache unpaidOrderCache;

    public UnpaidOrderTask(Order order, UnpaidOrderCache unpaidOrderCache) {
        this.order = order;
        this.timeUnit = TimeUnit.SECONDS;
        this.unpaidOrderCache = unpaidOrderCache;
    }

    @Override
    public void run() {
        logger.info("尝试获得OrderFailure锁1");
        orderLock.lock();
        unpaidOrderCache.removeMapCahce(order.getId());
        logger.info("已经获得OrderFailure锁1");
        if (order.getStatus() != OrderStatus.UNPAID) {
            return;
        }
        logger.info(order + "订单失效");
        order.setStatus(OrderStatus.CANCEL);
        this.taskResult(order);
        orderLock.unlock();
    }

    // 用户付款成功，取消订单失效的定时任务
    public boolean removeUnpaidOrder() {
        logger.info(orderLock.tryLock()+"");
        logger.info("尝试获得OrderFailure锁");
        orderLock.lock();
        logger.info("已经获得OrderFailure锁");
        unpaidOrderCache.removeMapCahce(order.getId());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!future.isDone()) {
            return future.cancel(true);
        }
        orderLock.unlock();
        return false;
    }

    public void taskResult(Order order){ throw new RuntimeException("You must overwrite this method");};

}