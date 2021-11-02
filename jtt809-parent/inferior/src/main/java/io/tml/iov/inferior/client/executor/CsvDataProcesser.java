package io.tml.iov.inferior.client.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.tml.iov.common.packet.JT809Packet0x1202;
import io.tml.iov.common.util.CommonUtils;
import io.tml.iov.common.util.PropertiesUtil;
import io.tml.iov.common.util.constant.Const;
import io.tml.iov.inferior.client.DataSender;
import io.tml.iov.inferior.client.DownLinkServer;
import io.tml.iov.inferior.client.util.PathHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvDataProcesser {

    private static final String INPUT_DIR = "input";
    private static final String SUFFIX = "csv";

    private static final int CSV_COLUMN_LIMIT = 8;
    private static final int CSV_COLUMN_PLUS_LIMIT = 9;

    private static final int VEHICLENO_INDEX = 0;
    private static final int LON_INDEX = 1;
    private static final int LAT_INDEX = 2;
    private static final int VEC1_INDEX = 3;
    private static final int VEC2_INDEX = 4;
    private static final int VEC3_INDEX = 5;
    private static final int DIRECTION_INDEX = 6;
    private static final int ALTUTIDE_INDEX = 7;

    private static final int DATE_INDEX = 8;

    private static DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    private static JT809Packet0x1202 buildLocation(String[] locArray,
            boolean plusDate) {
        JT809Packet0x1202 location = new JT809Packet0x1202();
        location.setDirection(Short.valueOf(locArray[DIRECTION_INDEX].trim()));
        location.setLon(CommonUtils
                .formatLonLat(Double.valueOf(locArray[LON_INDEX].trim())));
        location.setLat(CommonUtils
                .formatLonLat(Double.valueOf(locArray[LAT_INDEX].trim())));
        location.setVec1(Short.valueOf(locArray[VEC1_INDEX].trim()));
        location.setVec2(Short.valueOf(locArray[VEC2_INDEX].trim()));
        location.setVec3(Short.valueOf(locArray[VEC3_INDEX].trim()));
        location.setAltitude(Short.valueOf(locArray[ALTUTIDE_INDEX].trim()));
        location.setVehicleNo(locArray[VEHICLENO_INDEX].trim());
        if (plusDate) {
            String dateStr = locArray[DATE_INDEX].trim();
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            LocalTime localTime = LocalTime.parse(dateStr, formatter);
            location.setDate(localDate);
            location.setTime(localTime);
        } else {
            location.setDate(LocalDate.now());
            location.setTime(LocalTime.now());
        }

        return location;
    }

    public static void main(String[] args) {
        int enableDateColumn = PropertiesUtil
                .getInteger("mock.input.data.date");
        int csvColumn = getCsvColumn(enableDateColumn);
        boolean plusDate = enableDateColumn == Const.SWITCH_ON;
        String inputPath = System.getProperty("user.dir") + File.separator
                + INPUT_DIR;
        log.info("csv path info|{}", inputPath);

        DownLinkServer downLinkServer = new DownLinkServer();
        downLinkServer.starDownLinkServer();

        DataSender sender = DataSender.getInstance();
        waitForChannelSetUp(sender);

        synchronized (DataSender.class) {
            boolean waitLogin = true;
            while (waitLogin) {
                try {
                    DataSender.class.wait();
                } catch (Exception e) {
                    log.error("wait to login error.", e);
                }
                waitLogin = false;
            }
        }

        execute(csvColumn, plusDate, inputPath, sender);
    }

    private static void execute(int csvColumn, boolean plusDate,
            String inputPath, DataSender sender) {
        while (true) {
            List<String> csvFiles = PathHelper.getFiles(inputPath, SUFFIX);
            if (csvFiles.isEmpty()) {
                threadSleep();
                continue;
            }

            log.info("csv processer scan files:{}", csvFiles);
            StatCount statCount = new StatCount();
            boolean sendResult = true;
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
                        if (isSkipLine(csvColumn, locArray, lineNum)) {
                            logParseFile(csvColumn, path, locArray, lineNum);
                            continue;
                        }

                        JT809Packet0x1202 location = buildLocation(locArray,
                                plusDate);
                        checkChannelAvaliable(sender);
                        sendResult &= sender.sendMsg2Gov(location);

                        statCount.judeResult(sendResult);
                    }

                } catch (Exception e2) {
                    log.error("CsvDataProcesser open file error", e2);
                }
            }

            log.info("data send success cnt:{}", statCount.getSucessCnt());
            log.info("data send fail cnt:{}", statCount.getFailCnt());

            cleanDoneFile(csvFiles);

        }
    }

    private static void threadSleep() {
        try {
            Thread.sleep(60 * 1000L);
        } catch (InterruptedException e) {
            log.error("CsvDataProcesser run error", e);
            Thread.currentThread().interrupt();
        }
    }

    private static boolean isSkipLine(int csvColumn, String[] locArray,
            int lineNum) {
        return lineNum == 1 || locArray.length < csvColumn;
    }

    private static void logParseFile(int csvColumn, String path,
            String[] locArray, int lineNum) {
        if (locArray.length < csvColumn) {
            log.info("data parser error,File:{},line num:{}", path, lineNum);
        }
    }

    private static void checkChannelAvaliable(DataSender sender) {
        while (!sender.channelAvaliable()) {
            try {
                log.info("channel is not avaliable,wait 30s");
                Thread.sleep(30 * 1000L);
            } catch (InterruptedException e) {
                log.error("CsvDataProcesser channelAvaliable error", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void cleanDoneFile(List<String> csvFiles) {
        csvFiles.forEach(path -> {
            try {
                Files.delete(Paths.get(path));
            } catch (IOException e) {
                log.error("cleanDoneFile {} error", path);
            }
        });
    }

    private static void waitForChannelSetUp(DataSender sender) {
        while (true) {
            if (sender.channelAvaliable()) {
                sender.login2Superior();
                break;
            }
        }
    }

    private static int getCsvColumn(int enableDateColumn) {
        return enableDateColumn == Const.SWITCH_ON ? CSV_COLUMN_PLUS_LIMIT
                : CSV_COLUMN_LIMIT;
    }

}
