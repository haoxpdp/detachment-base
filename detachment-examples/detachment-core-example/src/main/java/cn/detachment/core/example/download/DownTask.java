package cn.detachment.core.example.download;

import lombok.Getter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author haoxp
 */
public class DownTask {

    @Getter
    String url;
    @Getter
    String fileName;
    @Getter
    String saveParent = "/tmp";
    @Getter
    private String suffix = "";
    @Getter
    String savePath;

    static final long kb = 1024;
    static final long mb = kb * 1024;
    static final long gb = mb * 1024;

    public DownTask(String url, String fileName, String savePath) {
        this.url = url;
        this.fileName = fileName;
        this.saveParent = savePath;
        this.suffix = url.substring(url.lastIndexOf("."));
    }

    public DownTask(URL url) {
    }

    public static void main(String[] args) {
        String uri = "https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.2/binaries/apache-maven-3.6.2-bin.tar.gz";
        String fn = uri.substring(uri.lastIndexOf("/"));
        try {
            String fp = "D:/tmp" + fn;
            File file = new File(fp);
            if (!file.exists()) {
                file.createNewFile();
            }
            URL url = new URL(uri);
            URLConnection connection = url.openConnection();
            Long fSize = connection.getContentLengthLong();
            try (InputStream in = connection.getInputStream(); FileOutputStream fileOutputStream = new FileOutputStream(fp)) {
                byte[] buf = new byte[1024];
                while (in.read(buf) != -1) {
                    fileOutputStream.write(buf);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
