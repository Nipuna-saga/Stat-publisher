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

/**
 * Read stream configuration values from mbStatConfiguration.xml file.
 */
public class StreamConfiguration {

    private String versionMessage;
    private String versionAck;
    private String forwardSlash;
    private String trustStorePassword;
    private String versionSystemStatistic;
    private String versionMBStatistic;

    public void setVersionMessage(String versionMessage) {
        this.versionMessage = versionMessage;
    }

    public void setVersionAck(String versionAck) {
        this.versionAck = versionAck;
    }

    public void setForwardSlash(String forwardSlash) {
        this.forwardSlash = forwardSlash;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public void setVersionSystemStatistic(String versionSystemStatistic) {
        this.versionSystemStatistic = versionSystemStatistic;
    }

    public void setVersionMBStatistic(String versionMBStatistic) {
        this.versionMBStatistic = versionMBStatistic;
    }

    /**
     * Get message version
     *
     * @return versionMessage
     */
    public String getVersionMessage() {
        return versionMessage;
    }

    /**
     * Get acknowledge packet version
     *
     * @return versionMessage
     */
    public String getVersionAck() {
        return versionAck;
    }

    /**
     * Get forward slash
     *
     * @return forwardSlash
     */
    public String getForwardSlash() {
        return forwardSlash;
    }

    /**
     * Get trustStore password
     *
     * @return trustStorePassword
     */
    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    /**
     * Get system statistic version
     *
     * @return versionSystemStatistic
     */
    public String getVersionSystemStatistic() {
        return versionSystemStatistic;
    }

    /**
     * Get message broker statistic version
     *
     * @return versionMBStatistic
     */
    public String getVersionMBStatistic() {
        return versionMBStatistic;
    }
}
