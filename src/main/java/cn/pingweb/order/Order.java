package cn.pingweb.order;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created by zhuyuping on 2018/1/3.
 */
@Data
@ToString
public class Order {
    private String id;
    private OrderStatus status;
    private Date createTime;
    private Date invalidTime;

    /**
     *
     * @param id
     * @param status 订单状态
     * @param duration 隔d毫秒之后失效
     */
    public Order(String id, OrderStatus status, long duration) {
        this.id = id;
        this.status = status;
        this.createTime = new Date();
        this.invalidTime = new Date(this.createTime.getTime() + duration);
    }

    public long getDuration() {
        long duration = (this.invalidTime.getTime() - this.createTime.getTime()) / 1000L;
        return duration > 0L ? duration : 0L;
    }

}
