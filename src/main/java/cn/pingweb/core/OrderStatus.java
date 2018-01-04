package cn.pingweb.core;

/**
 * Created by zhuyuping on 2018/1/3.
 */
public enum OrderStatus {
    UNPAID("未付款"),ING("进行中"),CANCEL("取消"),FINISH("完成");
    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
