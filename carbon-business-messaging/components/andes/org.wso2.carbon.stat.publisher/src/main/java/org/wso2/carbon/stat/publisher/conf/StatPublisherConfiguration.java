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

import org.wso2.carbon.stat.publisher.registry.Property;

public class StatPublisherConfiguration {

    //enable stat publisher
    private boolean enableStatPublisher;

    //credential details
    private String username="";
    private String password="";
    private String url="";
    private int tenantID;
    private String nodeURL;
    private Property[] properties;

    //enable Stat publisher features (message,system and message broker)
    private boolean messageStatEnable=false;
    private boolean systemStatEnable=false;
    private boolean MBStatEnable=false;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEnableStatPublisher() {
        return enableStatPublisher;
    }

    public void setEnableStatPublisher(boolean enableStatPublisher) {
        this.enableStatPublisher = enableStatPublisher;
    }

    public boolean isMessageStatEnable() {
        return messageStatEnable;
    }

    public void setMessageStatEnable(boolean messageStatEnable) {
        this.messageStatEnable = messageStatEnable;
    }

    public boolean isSystemStatEnable() {
        return systemStatEnable;
    }

    public void setSystemStatEnable(boolean systemStatEnable) {
        this.systemStatEnable = systemStatEnable;
    }

    public boolean isMBStatEnable() {
        return MBStatEnable;
    }

    public void setMBStatEnable(boolean MBStatEnable) {
        this.MBStatEnable = MBStatEnable;
    }

    public int getTenantID() {
        return tenantID;
    }

    public void setTenantID(int tenantID) {
        this.tenantID = tenantID;
    }

    public String getNodeURL() {
        return nodeURL;
    }

    public void setNodeURL(String nodeURL) {
        this.nodeURL = nodeURL;
    }

    public Property[] getProperties() {
        return properties;
    }

    public void setProperties(Property[] properties) {
        this.properties = properties;
    }

}
