package org.sam.server.util;

/**
 * 문자열에 관련된 유틸 클래스입니다.
 */
public class StringUtils {

    /**
     * 문자열이 null이나 빈 값인지 확인합니다.
     *
     * @param value 검사할 문자열
     * @return null이나 빈 값인지에 대한 여부
     * */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

}
