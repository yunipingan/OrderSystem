package cn.pingweb.redpacket;

import java.util.List;

/**
 * Created by zhuyuping on 2018/1/3.
 */
public interface RedPacketDao {
    RedPacket queryBy(String id);
    boolean updateRedPacket(RedPacket redPacket);
    List<RedPacket> queryunReceivedRedPacket();
}
