
package org.wso2.carbon.stat.publisher.exception;

public class StatPublisherRuntimeException extends RuntimeException {

    public StatPublisherRuntimeException() {
        super();
    }

    public StatPublisherRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatPublisherRuntimeException(Throwable cause) {
        super(cause);
    }
}
