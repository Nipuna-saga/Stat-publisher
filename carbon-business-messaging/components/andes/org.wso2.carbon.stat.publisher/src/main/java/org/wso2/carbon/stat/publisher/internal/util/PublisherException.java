package org.wso2.carbon.stat.publisher.internal.util;

/**
 * Created by dilshani on 8/20/14.
 */
public class PublisherException extends  Exception {
    public PublisherException(String s) {
        super(s);
    }

    public PublisherException(String s, Throwable e) {
        super(s, e);
    }

}
