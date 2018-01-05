package cn.pingweb.order;

/**
 * Created by zhuyuping on 2018/1/4.
 */
public interface UnpaidOrderCache {
    void removeMapCahce(String orderId);
}
