package ru.clevertec.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class ControllerUtil {

    private ControllerUtil(){
        throw new UnsupportedOperationException("Util class");
    }

    public static boolean isId(String pathInfo) {
        return Objects.nonNull(pathInfo) && StringUtils.isNumeric(pathInfo.substring(1));
    }
}
