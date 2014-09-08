package org.wso2.carbon.stat.publisher.internal.util;

public class PublisherException extends  Exception {
    public PublisherException(String s) {
        super(s);
    }

    public PublisherException(String s, Throwable e) {
        super(s, e);
    }

}
