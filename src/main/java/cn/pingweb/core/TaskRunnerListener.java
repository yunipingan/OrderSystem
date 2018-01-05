package cn.pingweb.core;

/**
 *
 * @author zhuyuping
 * @date 2018/1/5
 */
public interface TaskRunnerListener<T> {

    default void runBefore(T task){}

    /**
     * 执行定时任务时，用户自定义实现的操作
     * @param task
     */
    void runTask(T task);

    default void runAfter(T task, Throwable throwable) {}

}

