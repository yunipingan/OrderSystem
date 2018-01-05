package cn.pingweb.core;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhuyuping on 2018/1/4.
 */
public interface Task {
    //private TaskRunner taskRunner;
    long getDuration();
    TimeUnit getTimeUnit();
    String getTaskId();

//    public TaskRunner getTaskRunner() {
//        return taskRunner;
//    }
}
