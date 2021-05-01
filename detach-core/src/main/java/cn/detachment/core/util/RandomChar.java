package cn.detachment.core.util;

/**
 * 随机字符串生成
 *
 * @author haoxp
 */
@SuppressWarnings("unused")
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

    public static final String STRING = "abcdefghijklmnopqrstuvwxyz0123456789-=[];',./";

    public static final String SIMPLE_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        int len = SIMPLE_STRING.length();
        for (int i = 0; i < length; i++) {
            sb.append(SIMPLE_STRING.charAt(getRandom(len - 1)));
        }
        return sb.toString();
    }
}
