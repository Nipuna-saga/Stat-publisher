/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.stat.publisher.conf;

import org.wso2.andes.kernel.AndesAckData;
import org.wso2.andes.kernel.AndesMessageMetadata;

public class MessageStat {

    private boolean message;
    private AndesMessageMetadata andesMessageMetadata;
    private int noOfSubscribers;
    private String domain;
    private AndesAckData andesAckData;

    public AndesAckData getAndesAckData() {
        return andesAckData;
    }

    public void setAndesAckData(AndesAckData andesAckData) {
        this.andesAckData = andesAckData;
    }

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public AndesMessageMetadata getAndesMessageMetadata() {
        return andesMessageMetadata;
    }

    public void setAndesMessageMetadata(AndesMessageMetadata andesMessageMetadata) {
        this.andesMessageMetadata = andesMessageMetadata;
    }

    public int getNoOfSubscribers() {
        return noOfSubscribers;
    }

    public void setNoOfSubscribers(int noOfSubscribers) {
        this.noOfSubscribers = noOfSubscribers;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
