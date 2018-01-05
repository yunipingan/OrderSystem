package cn.pingweb.redpacket;

import cn.pingweb.core.Task;
import cn.pingweb.core.TaskRunner;
import cn.pingweb.core.TaskRunnerListener;
import cn.pingweb.core.TaskSchedule;
import cn.pingweb.core.impl.TaskScheduleImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by zhuyuping on 2018/1/5.
 */
@Component
public class RedPacketService implements CommandLineRunner, TaskRunnerListener{

    private TaskSchedule taskSchedule;

    @Autowired
    private RedPacketDao redPacketDao;

    @Bean
    public TaskSchedule initSchedule() {
        this.taskSchedule = TaskScheduleImpl.getInstance();
        return taskSchedule;
    }

    @Override
    public void run(String... strings) throws Exception {
        taskSchedule.loadTask(redPacketDao.queryunReceivedRedPacket(), this);
    }

    @Override
    public void runTask(Task task) {
        redPacketDao.updateRedPacket((RedPacket) task);
    }

    public void addUnReceivedRedPacket(RedPacket redPacket) {
        TaskRunner taskRunner = new TaskRunner(redPacket, this);
        taskSchedule.startTask(taskRunner);
    }

}
