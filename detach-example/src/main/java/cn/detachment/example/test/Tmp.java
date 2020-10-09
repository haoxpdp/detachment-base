package cn.detachment.example.test;

import cn.detachment.utils.http.HttpUtil;

/**
 * @author haoxp
 * @date 20/10/9
 */
public class Tmp {
    public static void main(String[] args) {
        System.out.println(HttpUtil.get("https://www.csdn.net/"));
    }
}
