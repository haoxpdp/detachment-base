package cn.detach.tools.download.exception;

/**
 * @author haoxp
 */
public class FileInfoException extends RuntimeException {

    public FileInfoException(String msg) {
        super(msg);
    }

    public static FileInfoException netFileChanged() {
        return new FileInfoException("网络文件已经修改");
    }

    public static FileInfoException localFileChanged() {
        return new FileInfoException("本地文件被修改");
    }

}
