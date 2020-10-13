package cn.detachment.utils.http;

import org.apache.http.HttpEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * @author haoxp
 * @date 20/10/13
 */
public class TestDownload {

    public static void main(String[] args) {

        HttpUtil.get("https://w.wallhaven.cc/full/kw/wallhaven-kwqdmq.png", null, null, response -> {
            HttpEntity entity = response.getEntity();
            File file = new File("h:\\a.png");
            try (InputStream inputStream = entity.getContent();
                 FileOutputStream fos = new FileOutputStream(file); FileChannel channel = fos.getChannel()) {
                ReadableByteChannel source = Channels.newChannel(inputStream);
                System.out.println(entity.getContentLength());
                channel.transferFrom(source, 0, entity.getContentLength());

            }
            return null;
        }, null);
    }

}
