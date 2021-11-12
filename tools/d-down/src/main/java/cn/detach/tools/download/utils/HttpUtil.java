package cn.detach.tools.download.utils;

import cn.detach.tools.download.exception.TaskInitException;
import cn.detach.tools.download.task.SplitInfo;
import cn.detach.tools.download.task.TaskInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author haoxp
 */
public class HttpUtil {

    public static String getHttpFileName(String url) {
        int index = url.lastIndexOf("/");
        int query = url.lastIndexOf("?");
        return query == -1 ? url.substring(index + 1) : url.substring(index + 1, query);
    }


    public static boolean checkUrl(String url) {
        // todo
        return true;
    }

    public static TaskInfo getBaseInfo(TaskInfo taskInfo) throws TaskInitException {
        String url = taskInfo.getOriginUrl();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = getHttpURLConnection(url);
            taskInfo.setTotalSize(urlConnection.getContentLengthLong());
        } catch (IOException e) {
            throw new TaskInitException("获取文件大小失败", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return taskInfo;
    }

    public static HttpURLConnection getHttpURLConnection(SplitInfo splitInfo) throws IOException {
        long startPos = splitInfo.getStartPos();
        long endPos = splitInfo.getEndPos();
        HttpURLConnection httpURLConnection = getHttpURLConnection(splitInfo.getOriginUrl());
        System.out.printf("分片 %d , 下载的区间是：%d-%d \n", splitInfo.getSplitNo(), startPos, endPos);

        if (endPos != 0) {
            httpURLConnection.setRequestProperty("RANGE", "bytes=" + startPos + "-" + endPos);
        } else {
            httpURLConnection.setRequestProperty("RANGE", "bytes=" + startPos + "-");
        }

        return httpURLConnection;
    }


    public static HttpURLConnection getHttpURLConnection(String url) throws IOException {
        // todo add proxy 避免太多tcp链接导致封ip…
        URL httpUrl = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1");
        return httpURLConnection;
    }



}
