package cn.pingweb.core;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhuyuping on 2018/1/4.
 */
public interface Task {
    /**
     * 隔时间n毫秒后触发
     * @return
     */
    long getTriggerTime();

    /**
     * 获取触发的时间单位：毫秒/秒/分钟等
     * @return
     */
    TimeUnit getTimeUnit();

    /**
     * 每个任务独一无二的id
     * @return
     */
    String getTaskId();
}
