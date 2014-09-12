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

package org.wso2.carbon.stat.publisher.data;

public class StatConfiguration {

    //enable stat publisher
    private boolean enableStatPublisher;

    //credential details
    private String username;
    private String password;
    private String URL;
    private int tenantID;
    private String nodeURL;

    //enable Stat publisher features (message,system and message broker)
    private boolean message_statEnable;
    private boolean system_statEnable;
    private boolean MB_statEnable;

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

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public boolean isEnableStatPublisher() {
        return enableStatPublisher;
    }

    public void setEnableStatPublisher(boolean enableStatPublisher) {
        this.enableStatPublisher = enableStatPublisher;
    }

    public boolean isMessage_statEnable() {
        return message_statEnable;
    }

    public void setMessage_statEnable(boolean message_statEnable) {
        this.message_statEnable = message_statEnable;
    }

    public boolean isSystem_statEnable() {
        return system_statEnable;
    }

    public void setSystem_statEnable(boolean system_statEnable) {
        this.system_statEnable = system_statEnable;
    }

    public boolean isMB_statEnable() {
        return MB_statEnable;
    }

    public void setMB_statEnable(boolean MB_statEnable) {
        this.MB_statEnable = MB_statEnable;
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
}
