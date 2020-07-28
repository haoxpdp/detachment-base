package cn.detach.api.builder;

import org.springframework.util.StringUtils;

/**
 * parser ${} parameter
 *
 * @author haoxp
 * @version v1.0
 * @date 20/7/28 18:29
 */
public class TokenParser {

    private final String startToken;

    private final String endToken;

    private final TokenHandler tokenHandler;

    public TokenParser(String startToken, String endToken, TokenHandler tokenHandler) {
        this.startToken = startToken;
        this.endToken = endToken;
        this.tokenHandler = tokenHandler;
    }

    public String parseTemplate(String input) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isEmpty(input)) {
            return builder.toString();
        }
        char[] src = input.toCharArray();

        int offset = 0;
        int start = input.indexOf(startToken);
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                builder.append(src, offset, start - offset - 1).append(startToken);
                offset = start + startToken.length();
            } else {
                int end = input.indexOf(endToken, start);
                if (end == -1) {
                    builder.append(src, offset, src.length - offset);
                    offset = src.length;
                } else {
                    builder.append(src, offset, start - offset);
                    offset = start + startToken.length();
                    String content = new String(src, offset, end - offset);
                    builder.append(tokenHandler.handleContent(content));
                    offset = end + endToken.length();
                }

            }
            start = input.indexOf(startToken, offset);
        }

        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }

        return builder.toString();
    }


}
