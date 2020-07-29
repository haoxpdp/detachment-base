package cn.detach.api.builder;

import cn.detach.api.constant.RemoteParameterType;
import lombok.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * parser ${} parameter
 *
 * @author haoxp
 * @version v1.0
 * @date 20/7/28 18:29
 */
public class TokenParser {

    private final static String START_TOKEN = "${";

    private final static String END_TOKEN = "}";

    public static List<ParameterMap> parseTemplate(String input, Map<String, Object> map) {
        List<ParameterMap> queryList = new ArrayList<>();
        if (StringUtils.isEmpty(input)) {
            return queryList;
        }
        char[] src = input.toCharArray();

        int offset = 0;
        int start = input.indexOf(START_TOKEN);
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                queryList.add(new ParameterMap(new String(src, offset, start - offset - 1), RemoteParameterType.STRING));
                offset = start + START_TOKEN.length();
            } else {
                int end = input.indexOf(END_TOKEN, start);
                if (end == -1) {
                    queryList.add(new ParameterMap(new String(src, offset, src.length - offset), RemoteParameterType.STRING));
                    offset = src.length;
                } else {
                    queryList.add(new ParameterMap(new String(src, offset, start - offset), RemoteParameterType.STRING));
                    offset = start + START_TOKEN.length();
                    String content = new String(src, offset, end - offset);
                    queryList.add(new ParameterMap(content, map.containsKey(content) ? RemoteParameterType.REMOTE_PARAMETER : RemoteParameterType.PARAMETER));
                    offset = end + END_TOKEN.length();
                }

            }
            start = input.indexOf(START_TOKEN, offset);
        }

        if (offset < src.length) {
            queryList.add(new ParameterMap(new String(src, offset, src.length - offset), RemoteParameterType.STRING));
        }
        return queryList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class ParameterMap {
        String value;
        RemoteParameterType parameterType;
    }

}
