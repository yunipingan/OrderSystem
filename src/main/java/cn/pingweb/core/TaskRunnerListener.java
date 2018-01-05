package cn.pingweb.core;

/**
 *
 * @author zhuyuping
 * @date 2018/1/5
 */
public interface TaskRunnerListener {

    default void runBefore(Task task){}

    /**
     * 执行定时任务时，用户自定义实现的操作
     * @param task
     */
    void runTask(Task task);

    default void runAfter(Task task, Throwable throwable) {}

}

