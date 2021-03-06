package org.sam.server.exception;

/**
 * 해당 빈을 찾을 수 없을 때 발생합니다.
 *
 * @author hypernova1
 * */
public class BeanNotFoundException extends RuntimeException {
    public BeanNotFoundException(String message) {
        super(message + " bean is not found");
    }
}
