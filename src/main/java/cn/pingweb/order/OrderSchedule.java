package cn.pingweb.order;

/**
 * Created by zhuyuping on 2018/1/4.
 */
public interface OrderSchedule {
    void startUnpaidOrderTask(UnpaidOrderTask unpaidOrderTask);
    boolean cancelUnpaidOrderTask(String orderId);
}
