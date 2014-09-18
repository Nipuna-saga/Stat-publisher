
package org.wso2.carbon.stat.publisher.exception;

/**
 * Runtime Exception handling in Stat Publisher component
 */
public class StatPublisherRuntimeException extends RuntimeException {

    /**
     * Exception send to upper level
     * @param message - Error message
     * @param cause - Error
     */
    public StatPublisherRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * Exception send to upper level
     * @param cause - Error
     */
    public StatPublisherRuntimeException(Throwable cause) {
        super(cause);
    }
}
