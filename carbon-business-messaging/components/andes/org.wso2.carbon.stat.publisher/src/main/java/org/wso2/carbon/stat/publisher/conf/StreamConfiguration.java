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
    private String versionSystemStatistic;
    private String versionMBStatistic;

    /**
     * Set value of message version
     * @param versionMessage - String value
     */
    public void setVersionMessage(String versionMessage) {
        this.versionMessage = versionMessage;
    }

    /**
     * Set value of acknowledge packet version
     * @param versionAck - String value
     */
    public void setVersionAck(String versionAck) {
        this.versionAck = versionAck;
    }

    /**
     * Set value of system statistic version
     * @param versionSystemStatistic - String value
     */
    public void setVersionSystemStatistic(String versionSystemStatistic) {
        this.versionSystemStatistic = versionSystemStatistic;
    }

    /**
     * Set value of message broker statistic version
     * @param versionMBStatistic - String value
     */
    public void setVersionMBStatistic(String versionMBStatistic) {
        this.versionMBStatistic = versionMBStatistic;
    }

    /**
     * Get message version
     * @return versionMessage
     */
    public String getVersionMessage() {
        return versionMessage;
    }

    /**
     * Get acknowledge packet version
     * @return versionAck
     */
    public String getVersionAck() {
        return versionAck;
    }

    /**
     * Get system statistic version
     * @return versionSystemStatistic
     */
    public String getVersionSystemStatistic() {
        return versionSystemStatistic;
    }

    /**
     * Get message broker statistic version
     * @return versionMBStatistic
     */
    public String getVersionMBStatistic() {
        return versionMBStatistic;
    }
}
