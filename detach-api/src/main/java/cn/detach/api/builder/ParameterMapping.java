package cn.detach.api.builder;

import cn.detach.api.annoation.RemoteApiBody;
import cn.detach.api.annoation.RemoteHeader;
import cn.detach.api.annoation.RemoteParameter;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/28 20:14
 */
public class ParameterMapping implements TokenHandler {


    List<String> placeHolderList;

    List<String> valueList;

    Map<String, Object> keyMap;

    int order = 0;

    private String url;

    public ParameterMapping(String url, Parameter[] parameters, Object[] args) {
        setupMethodParameter(parameters, args);
        this.url = url;
    }

    public void setupMethodParameter(Parameter[] parameters, Object[] args) {
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object arg = args[i];
            if (parameter.isAnnotationPresent(RemoteHeader.class) || parameter.isAnnotationPresent(RemoteApiBody.class)) {
                continue;
            }
            if (parameter.isAnnotationPresent(RemoteParameter.class)) {
                RemoteParameter annotation = parameter.getAnnotation(RemoteParameter.class);
                if (keyMap == null) {
                    keyMap = new HashMap<>();
                }
                keyMap.put(annotation.name(), arg);
            }
            if (valueList == null) {
                valueList = new ArrayList<>();
            }
            valueList.add(String.valueOf(arg));
        }
    }

    @Override
    public String handleContent(String key) {
        if (placeHolderList == null) {
            placeHolderList = new ArrayList<>();
        }
        if (key.contains(".")) {
            Object arg = keyMap.get(key.substring(0, key.indexOf(".")));
            String method = key.substring(key.indexOf("."));
            return method;
        } else if (keyMap != null && keyMap.containsKey(key)) {
            return String.valueOf(keyMap.get(key));
        } else {
            return valueList.get(order++);
        }
    }

}
