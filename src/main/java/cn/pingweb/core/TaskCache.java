package cn.pingweb.core;

/**
 *
 * @author zhuyuping
 * @date 2018/1/4
 */
public interface TaskCache {
    /**
     * 清除缓存中的任务，防止无用任务过多内存泄露
     * @param taskId
     */
    void removeMapCahce(String taskId);
}
