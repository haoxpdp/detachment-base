package cn.detachment.frame.es.util;

import java.io.Serializable;

/**
 * @param <Children>
 * @author haoxp
 */
public interface Nested<Children> extends Serializable {

    Children or(Children children);

}
