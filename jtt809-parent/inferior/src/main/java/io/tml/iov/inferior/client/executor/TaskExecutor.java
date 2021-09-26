package io.tml.iov.inferior.client.executor;

import java.time.LocalDate;
import java.time.LocalTime;

import io.tml.iov.common.packet.JT809Packet0x1202;
import io.tml.iov.common.util.CommonUtils;
import io.tml.iov.inferior.client.DataSender;

public class TaskExecutor {

    public static void main(String[] args) {

        DataSender s = DataSender.getInstance();
        JT809Packet0x1202 location = new JT809Packet0x1202();
        location.setDirection((short) 120);
        location.setLon(
                CommonUtils.formatLonLat(Double.valueOf("117.2900911")));
        location.setLat(CommonUtils.formatLonLat(Double.valueOf("39.563628")));
        location.setVec1((short) 45);
        location.setVehicleNo("皖A66M27");
        location.setDate(LocalDate.now());
        location.setTime(LocalTime.now());
        
        s.sendMsg2Gov(location);
        while (true) {
            try {
                Thread.sleep(20 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            s.sendMsg2Gov(location);
        }

    }

}
