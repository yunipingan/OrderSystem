package cn.pingweb.core;

import cn.pingweb.order.UnpaidOrderTask;

import java.util.List;

/**
 *
 * @author zhuyuping
 * @date 2018/1/4
 */
public interface TaskSchedule {
    /**
     * 部分定时任务由于宕机，存入库，重启服务器时要事先加载继续执行
     * @param tasks
     * @param taskRunningListener
     * @return
     */
    TaskSchedule loadTask(List<? extends Task> tasks, TaskRunnerListener taskRunningListener);

    /**
     * 开启定时任务
     * @param taskRunner
     */
    void startTask(TaskRunner taskRunner);

    /**
     * 例如订单场景，支付成功后，取消订单定时失效的任务
     * @param taskId
     * @return
     */
    boolean cancelTask(String taskId);
}
