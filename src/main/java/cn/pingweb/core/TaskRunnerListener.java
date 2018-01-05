package cn.pingweb.core;

/**
 * Created by zhuyuping on 2018/1/5.
 */
public interface TaskRunnerListener {

    default void runBefore(Task task){}

    void runTask(Task task);

    default void runAfter(Task task, Throwable throwable) {}

}

