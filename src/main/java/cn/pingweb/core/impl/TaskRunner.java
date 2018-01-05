package cn.pingweb.core.impl;

import cn.pingweb.core.Task;
import cn.pingweb.core.TaskCache;
import cn.pingweb.core.TaskRunnerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author zhuyuping
 * @date 2018/1/4
 */
public class TaskRunner implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(TaskRunner.class);

    private Future future;

    private Lock taskLock = new ReentrantLock();

    private TaskCache taskCache;

    private Task task;

    private TaskRunnerListener taskRunnerListener;

    private TaskRunner(){}

    public TaskRunner(Task task, TaskRunnerListener taskRunnerListener) {
        this.taskRunnerListener = taskRunnerListener;
        this.task = task;
    }

    @Override
    public void run() {
        Throwable throwable = null;
        try {
            taskLock.lock();
            taskRunnerListener.runBefore(task);
            logger.info("excute runner");
            taskRunnerListener.runTask(task);
        } catch (Exception e) {
            throwable = e;
        } finally {
            taskCache.removeMapCahce(task.getTaskId());
            taskRunnerListener.runAfter(task, throwable);
            taskLock.unlock();
        }

    }

    void setFuture(Future future) {
        this.future = future;
    }

    boolean cancelTask(Task task) {
        try {
            taskLock.lock();
            taskCache.removeMapCahce(task.getTaskId());
            if (!future.isDone()) {
                return future.cancel(true);
            }
        } finally {
            taskLock.unlock();
        }
        return false;
    }

    Task getTask() {
        return task;
    }

    void setTaskCache(TaskCache taskCache) {
        this.taskCache = taskCache;
    }
}
