package cn.detachment.example.beans;

import cn.detachment.es.utils.ReflectBeanUtil;
import lombok.Data;
import sun.reflect.misc.ReflectUtil;

/**
 * @author haoxp
 * @date 20/12/15
 */
@Data
public class Test {

    private String name;

    public static void main(String[] args) {
        Test tst = new Test();
        tst.setName("hello");
        ReflectBeanUtil.parseClass(Test.class);
    }
}
