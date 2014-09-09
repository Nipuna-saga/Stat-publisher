/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 *
 */
package org.wso2.andes.server.virtualhost.plugins.logging;

import static org.wso2.andes.server.logging.AbstractRootMessageLogger.DEFAULT_LOG_HIERARCHY_PREFIX;

import org.apache.log4j.Logger;
import org.wso2.andes.server.logging.LogMessage;
import org.wso2.andes.server.registry.ApplicationRegistry;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Generated Using GeneratedLogMessages and LogMessages.vm
 *
 * This file is based on the content of LogMessages.properties
 *
 * It is generated so that we can provide compile time validation of the
 * message parameters.
 *
 * DO NOT EDIT DIRECTLY THIS FILE IS GENERATED.
 *
 */
public class TopicDeletePolicyMessages
{
    static ResourceBundle _messages;
    static Locale _currentLocale;
    
    public static final String TOPICDELETEPOLICY_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "topicdeletepolicy";
    public static final String DISCONNECTING_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "topicdeletepolicy.disconnecting";
    public static final String DELETING_QUEUE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "topicdeletepolicy.deleting_queue";

    static
    {
        Logger.getLogger(TOPICDELETEPOLICY_LOG_HIERARCHY);
        Logger.getLogger(DISCONNECTING_LOG_HIERARCHY);
        Logger.getLogger(DELETING_QUEUE_LOG_HIERARCHY);

        reload();
    }

    public static void reload()
    {
        if (ApplicationRegistry.isConfigured())
        {
            _currentLocale = ApplicationRegistry.getInstance().getConfiguration().getLocale();
        }
        else
        {
            _currentLocale = Locale.getDefault();
        }

        _messages = ResourceBundle.getBundle("org.wso2.andes.server.virtualhost.plugins.logging.TopicDeletePolicy_logmessages", _currentLocale);
    }


    /**
     * Log a TopicDeletePolicy message of the Format:
     * <pre>TDP-1002 : Disconnecting Session</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage DISCONNECTING()
    {
        String rawMessage = _messages.getString("DISCONNECTING");

        final String message = rawMessage;

        return new LogMessage()
        {
            public String toString()
            {
                return message;
            }
            
            public String getLogHierarchy()
            {
                return DISCONNECTING_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a TopicDeletePolicy message of the Format:
     * <pre>TDP-1001 : Deleting Queue</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage DELETING_QUEUE()
    {
        String rawMessage = _messages.getString("DELETING_QUEUE");

        final String message = rawMessage;

        return new LogMessage()
        {
            public String toString()
            {
                return message;
            }
            
            public String getLogHierarchy()
            {
                return DELETING_QUEUE_LOG_HIERARCHY;
            }
        };
    }


}
