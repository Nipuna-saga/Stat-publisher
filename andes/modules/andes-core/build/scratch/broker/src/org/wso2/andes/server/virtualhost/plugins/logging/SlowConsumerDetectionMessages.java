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
public class SlowConsumerDetectionMessages
{
    static ResourceBundle _messages;
    static Locale _currentLocale;
    
    public static final String SLOWCONSUMERDETECTION_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "slowconsumerdetection";
    public static final String CHECKING_QUEUE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "slowconsumerdetection.checking_queue";
    public static final String RUNNING_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "slowconsumerdetection.running";
    public static final String COMPLETE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "slowconsumerdetection.complete";

    static
    {
        Logger.getLogger(SLOWCONSUMERDETECTION_LOG_HIERARCHY);
        Logger.getLogger(CHECKING_QUEUE_LOG_HIERARCHY);
        Logger.getLogger(RUNNING_LOG_HIERARCHY);
        Logger.getLogger(COMPLETE_LOG_HIERARCHY);

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

        _messages = ResourceBundle.getBundle("org.wso2.andes.server.virtualhost.plugins.logging.SlowConsumerDetection_logmessages", _currentLocale);
    }


    /**
     * Log a SlowConsumerDetection message of the Format:
     * <pre>SCD-1003 : Checking Status of Queue {0}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage CHECKING_QUEUE(String param1)
    {
        String rawMessage = _messages.getString("CHECKING_QUEUE");

        final Object[] messageArguments = {param1};
        // Create a new MessageFormat to ensure thread safety.
        // Sharing a MessageFormat and using applyPattern is not thread safe
        MessageFormat formatter = new MessageFormat(rawMessage, _currentLocale);

        final String message = formatter.format(messageArguments);

        return new LogMessage()
        {
            public String toString()
            {
                return message;
            }
            
            public String getLogHierarchy()
            {
                return CHECKING_QUEUE_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a SlowConsumerDetection message of the Format:
     * <pre>SCD-1001 : Running</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage RUNNING()
    {
        String rawMessage = _messages.getString("RUNNING");

        final String message = rawMessage;

        return new LogMessage()
        {
            public String toString()
            {
                return message;
            }
            
            public String getLogHierarchy()
            {
                return RUNNING_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a SlowConsumerDetection message of the Format:
     * <pre>SCD-1002 : Complete</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage COMPLETE()
    {
        String rawMessage = _messages.getString("COMPLETE");

        final String message = rawMessage;

        return new LogMessage()
        {
            public String toString()
            {
                return message;
            }
            
            public String getLogHierarchy()
            {
                return COMPLETE_LOG_HIERARCHY;
            }
        };
    }


}
