package cn.pingweb.core.impl;

import cn.pingweb.core.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by zhuyuping on 2018/1/3.
 */
public class TaskScheduleImpl implements TaskSchedule, TaskCache {

    private volatile static ScheduledExecutorService scheduledExecutorService;
    private volatile static TaskScheduleImpl orderSchedule;
    // 不用缓存，从数据库里取的话需要重新new，无法lock同一对象
    private static ConcurrentHashMap<String, TaskRunner> taskRunnerMap = new ConcurrentHashMap<String, TaskRunner>();

    static {
        scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * 改成spring的bean
     * @return
     */
    public static TaskScheduleImpl getInstance() {
        if (null == orderSchedule) {
            synchronized (TaskScheduleImpl.class) {
                if (null == orderSchedule) {
                    orderSchedule = new TaskScheduleImpl();
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
    @Override
    public TaskSchedule loadTask(List<? extends Task> tasks, TaskRunnerListener taskRunningListener) {
        for(Task task : tasks) {
            TaskRunner taskRunner = new TaskRunner(task, taskRunningListener);
            this.startTask(taskRunner);
            taskRunnerMap.put(task.getTaskId(), taskRunner);
        }
        return this;
    }

    @Override
    public void startTask(TaskRunner taskRunner) {
        Task task = taskRunner.getTask();
        long duration = task.getDuration();
        Future future = scheduledExecutorService.schedule(taskRunner, task.getDuration(), task.getTimeUnit());
        taskRunner.setFuture(future);
        taskRunnerMap.put(task.getTaskId(), taskRunner);
    }

    // 用户付款成功，取消订单失效的定时任务
    @Override
    public boolean cancelTask(String taskId) {
        TaskRunner taskRunner = taskRunnerMap.get(taskId);
        //removeMapCahce(taskId);
        return taskRunner.cancelTask(taskRunner.getTask());
    }

    private String getKey(String taskId) {
        return taskId;
    }

    @Override
    public void removeMapCahce(String taskId) {
        taskRunnerMap.remove(getKey(taskId));
    }

}
