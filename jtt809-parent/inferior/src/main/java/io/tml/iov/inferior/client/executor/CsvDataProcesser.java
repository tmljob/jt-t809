package io.tml.iov.inferior.client.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import io.tml.iov.common.packet.JT809Packet0x1202;
import io.tml.iov.common.util.CommonUtils;
import io.tml.iov.inferior.client.DataSender;
import io.tml.iov.inferior.client.util.PathHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvDataProcesser {

    private static final String INPUT_DIR = "/input/";
    private static final String SUFFIX = "csv";

    private static final int VEHICLENO_INDEX = 0;
    private static final int LON_INDEX = 1;
    private static final int LAT_INDEX = 2;
    private static final int VEC1_INDEX = 3;
    private static final int DIRECTION_INDEX = 4;

    private static JT809Packet0x1202 buildLocation(String[] locArray) {
        JT809Packet0x1202 location = new JT809Packet0x1202();
        location.setDirection(Short.valueOf(locArray[DIRECTION_INDEX]));
        location.setLon(
                CommonUtils.formatLonLat(Double.valueOf(locArray[LON_INDEX])));
        location.setLat(
                CommonUtils.formatLonLat(Double.valueOf(locArray[LAT_INDEX])));
        location.setVec1(Short.valueOf(locArray[VEC1_INDEX]));
        location.setVehicleNo(locArray[VEHICLENO_INDEX]);
        return location;
    }

    public static void main(String[] args) {
        String inputPath = PathHelper.getRootPath() + INPUT_DIR;
        log.info("csv path info|{}", inputPath);
        DataSender sender = DataSender.getInstance();
        sender.login2Superior();

        synchronized (DataSender.class) {
            try {
                DataSender.class.wait();
            } catch (Exception e) {
                log.error("wait to login error.", e);
            }
        }

        new Thread(() -> {
            while (true) {
                List<String> csvFiles = PathHelper.getFiles(inputPath, SUFFIX);
                if (csvFiles.isEmpty()) {
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        log.error("CsvDataProcesser run error", e);
                    }
                    continue;
                }

                log.info("csv processer scan files:{}", csvFiles);
                int sucessCnt = 0;
                int failCnt = 0;
                for (String path : csvFiles) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(new FileInputStream(path),
                                    "GBK"));) {
                        log.info("start process file:{}", path);
                        String line = null;
                        String[] locArray = null;
                        int lineNum = 0;
                        while ((line = br.readLine()) != null) {
                            lineNum++;
                            locArray = line.split(",|\t");
                            if (lineNum == 1 || locArray.length < 5) {
                                if (locArray.length < 5) {
                                    log.info(
                                            "data parser error,File:{},line num:{}",
                                            path, lineNum);
                                }
                                continue;
                            }

                            JT809Packet0x1202 location = buildLocation(
                                    locArray);
                            if (!sender.channelAvaliable()) {
                                try {
                                    Thread.sleep(30 * 1000);
                                } catch (InterruptedException e) {
                                    log.error(
                                            "CsvDataProcesser channelAvaliable error",
                                            e);
                                }
                            }

                            boolean result = sender.sendMsg2Gov(location);

                            if (result) {
                                sucessCnt++;
                            } else {
                                failCnt++;
                            }
                        }

                    } catch (Exception e2) {
                        log.error("CsvDataProcesser open file error", e2);
                    }
                }

                log.info("data send success cnt:{}", sucessCnt);
                log.info("data send fail cnt:{}", failCnt);

                csvFiles.forEach(path -> {
                    File file = new File(path);
                    file.delete();
                });
            }
        }).start();
    }

}
