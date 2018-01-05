package cn.pingweb.core;

import cn.pingweb.order.UnpaidOrderTask;

import java.util.List;

/**
 * Created by zhuyuping on 2018/1/4.
 */
public interface TaskSchedule {
    TaskSchedule loadTask(List<? extends Task> tasks, TaskRunnerListener taskRunningListener);
    void startTask(TaskRunner taskRunner);
    boolean cancelTask(String taskId);
}
