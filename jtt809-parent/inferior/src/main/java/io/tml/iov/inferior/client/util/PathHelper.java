package io.tml.iov.inferior.client.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public static List<String> getFiles(String path, String suffix) {
        List<String> files = new ArrayList<String>();
        Path normallizePath = Paths.get(new File(path).toURI());
        try (Stream<Path> walk = Files.walk(normallizePath)) {

            List<String> filters = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(suffix))
                    .collect(Collectors.toList());
            if (null != filters && !filters.isEmpty()) {
                files.addAll(filters);
            }

        } catch (Exception e) {
            log.error("PathHelper getFiles error.", e);
        }

        return files;
    }

}
