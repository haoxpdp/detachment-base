package cn.detachment.example.beans;

import cn.detachment.es.utils.ReflectBeanUtil;
import lombok.Data;
import sun.reflect.misc.ReflectUtil;

import java.lang.reflect.Method;

/**
 * @author haoxp
 * @date 20/12/15
 */
//@Data
public class Test {


    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        Method name = ReflectBeanUtil.getWriteMethod(Test.class, "name");
        System.out.println(name);
    }
}
