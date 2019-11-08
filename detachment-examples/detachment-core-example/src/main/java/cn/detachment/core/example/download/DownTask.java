package cn.detachment.core.example.download;

import lombok.Getter;

import java.io.File;

/**
 * @author haoxp
 */
public class DownTask {

    @Getter
    String url;
    @Getter
    String fileName;
    @Getter
    String saveParent = "/tmp/";
    @Getter
    private String suffix = "";
    @Getter
    String savePath;

    public DownTask(String url,String fileName,String savePath){
        this.url = url;
        this.fileName = fileName;
        this.saveParent = savePath;
        this.suffix = url.substring(url.lastIndexOf("."));
    }

    public DownTask(String url,String savePath){
        this.url = url;
        this.saveParent = savePath;
        initParam();
    }

    public DownTask(String url){
        this.url = url;
        initParam();
    }

    private void initParam(){
        String baseName = initNameAndSuffixParam(url);
        String filePath;
        if (!saveParent.endsWith("/")){
            saveParent += "/";
        }
        filePath = saveParent + baseName + suffix;
        File parent = new File(saveParent);
        if (parent.exists()){
            
        }

        savePath = filePath;
    }


    private String initNameAndSuffixParam(String url){
        String lastSub = url.substring(url.lastIndexOf("/") + 1);
        if (lastSub.contains(".")){
            suffix = url.substring(url.lastIndexOf("."));
            return lastSub.substring(0,lastSub.indexOf("."));
        }
        return lastSub;
    }

    public static void main(String[] args) {
        String url = "http://www.ba.com/tmp.zip";
        DownTask downTask = new DownTask(url);
        System.out.println(downTask.getFileName());
    }

}
