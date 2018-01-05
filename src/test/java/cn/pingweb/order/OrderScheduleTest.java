package cn.pingweb.order;

import cn.pingweb.dao.OrderDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by zhuyuping on 2018/1/4.
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OrderScheduleTest {
    @Mock
    private OrderDao orderDao;
    @InjectMocks
    private OrderScheduleImpl orderSchedule;
    @Test
    public void start() throws Exception {
        Order order = new Order("123", OrderStatus.UNPAID, 2000);

        orderSchedule.startUnpaidOrderTask(new UnpaidOrderTask(order, (UnpaidOrderCache) orderSchedule) {

        });

        Thread.sleep(10000);

    }

    @Test
    public void cancelUnpaidOrder() throws Exception {

    }

}