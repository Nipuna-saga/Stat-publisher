
package org.wso2.carbon.stat.publisher.exception;

/**
 * Exception handling in Stat Publisher component
 */
public class StatPublisherConfigurationException extends Exception {

    /**
     * Exception send to upper level
     * @param message - Error message
     * @param cause - Error
     */
    public StatPublisherConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception send to upper level
     * @param message - Error message
     */
    public StatPublisherConfigurationException(String message) {
        super(message);
    }
    /**
     * Exception send to upper level
     * @param cause - Error
     */
    public StatPublisherConfigurationException(Throwable cause) {
        super(cause);
    }
}
