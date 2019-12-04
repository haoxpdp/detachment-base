package cn.detachment.example.es.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author haoxp
 */
@Data
public class Person {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 国家
     */
    private String country;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 介绍
     */
    private String remark;

}
