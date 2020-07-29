package cn.detach.example.api;

import lombok.*;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/29 15:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthParam {

    private String company;
    private String key;

}
