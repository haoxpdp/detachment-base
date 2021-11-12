package cn.detach.tools.download.utils;

import java.io.File;

/**
 * @author haoxp
 */
public class FileUtils {

    public static long fileLength(String path) {
        File file = new File(path);
        if (!file.exists()) return -1;
        return file.isFile() ? file.length() : 0;
    }

    public static String fileMd5(String path) {
        return "";
    }
}
