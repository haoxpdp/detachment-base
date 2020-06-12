package cn.detachment.core.util;

/**
 * 随机字符串生成
 *
 * @author haoxp
 */
public class RandomChar {
    /**
     * getRandom
     *
     * @param count 长度
     * @return int
     * @author haoxp
     * @date 20/2/1 19:58
     */
    private static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }

    public static final String string = "abcdefghijklmnopqrstuvwxyz0123456789-=[];',./";
    public static final String simpleString = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        int len = simpleString.length();
        for (int i = 0; i < length; i++) {
            sb.append(simpleString.charAt(getRandom(len - 1)));
        }
        return sb.toString();
    }
}
