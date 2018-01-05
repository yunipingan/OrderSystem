package cn.pingweb.redpacket;

import cn.pingweb.core.Task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhuyuping on 2018/1/5.
 */
public class RedPacket implements Task {

    private String id;

    private Date issueTime;

    private Date invaildTime;

    public RedPacket(String id, Date issueTime, Date invaildTime) {
        this.id = id;
        this.issueTime = issueTime;
        this.invaildTime = invaildTime;
    }

    @Override
    public long getTriggerTime() {
        if (invaildTime.getTime() > System.currentTimeMillis()) {
            return (invaildTime.getTime() - issueTime.getTime()) / 1000L;
        }
        return 0;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return TimeUnit.SECONDS;
    }

    @Override
    public String getTaskId() {
        return this.id;
    }
}
