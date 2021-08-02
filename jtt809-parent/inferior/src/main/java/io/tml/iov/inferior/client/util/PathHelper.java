package io.tml.iov.inferior.client.util;

import java.io.File;
import java.util.ArrayList;

public class PathHelper {

    /**
     * 获获取当前jar包所在目录
     */
    public static String getRootPath() {
        String path = PathHelper.class.getProtectionDomain().getCodeSource()
                .getLocation().getPath();
        if (System.getProperty("os.name").contains("dows")) {
            path = path.substring(1);
        }
        if (path.contains("jar")) {
            path = path.substring(0, path.lastIndexOf("."));
            return path.substring(0, path.lastIndexOf("/"));
        }

        return path.replace("target/classes/", "");
    }

    public static ArrayList<String> getFiles(String path, String suffix) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()
                    && tempList[i].getName().endsWith(suffix)) {
                files.add(tempList[i].toString());
            }
        }
        return files;
    }

}
