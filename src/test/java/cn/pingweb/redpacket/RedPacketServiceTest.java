package cn.pingweb.redpacket;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by zhuyuping on 2018/1/5.
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class RedPacketServiceTest {

    @Mock
    RedPacketDao redPacketDao;

    @InjectMocks
    RedPacketService redPacketService;

    @Before
    public void init() {
        redPacketService.initSchedule();
    }

    @Test
    public void runTask() throws Exception {

    }

    @Test
    public void addUnReceivedRedPacket() throws Exception {
        Date date = new Date();
        Date invalidDate = new Date(date.getTime() + 5000L);
        redPacketService.addUnReceivedRedPacket(new RedPacket("hb", date, invalidDate));
        Thread.sleep(10000);
    }

}