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
package org.wso2.andes.server.logging.messages;

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
public class ChannelMessages
{
    static ResourceBundle _messages;
    static Locale _currentLocale;
    
    public static final String CHANNEL_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "channel";
    public static final String FLOW_ENFORCED_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "channel.flow_enforced";
    public static final String CREATE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "channel.create";
    public static final String IDLE_TXN_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "channel.idle_txn";
    public static final String PREFETCH_SIZE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "channel.prefetch_size";
    public static final String CLOSE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "channel.close";
    public static final String FLOW_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "channel.flow";
    public static final String OPEN_TXN_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "channel.open_txn";
    public static final String FLOW_REMOVED_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "channel.flow_removed";

    static
    {
        Logger.getLogger(CHANNEL_LOG_HIERARCHY);
        Logger.getLogger(FLOW_ENFORCED_LOG_HIERARCHY);
        Logger.getLogger(CREATE_LOG_HIERARCHY);
        Logger.getLogger(IDLE_TXN_LOG_HIERARCHY);
        Logger.getLogger(PREFETCH_SIZE_LOG_HIERARCHY);
        Logger.getLogger(CLOSE_LOG_HIERARCHY);
        Logger.getLogger(FLOW_LOG_HIERARCHY);
        Logger.getLogger(OPEN_TXN_LOG_HIERARCHY);
        Logger.getLogger(FLOW_REMOVED_LOG_HIERARCHY);

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

        _messages = ResourceBundle.getBundle("org.wso2.andes.server.logging.messages.Channel_logmessages", _currentLocale);
    }


    /**
     * Log a Channel message of the Format:
     * <pre>CHN-1005 : Flow Control Enforced (Queue {0})</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage FLOW_ENFORCED(String param1)
    {
        String rawMessage = _messages.getString("FLOW_ENFORCED");

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
                return FLOW_ENFORCED_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a Channel message of the Format:
     * <pre>CHN-1001 : Create</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage CREATE()
    {
        String rawMessage = _messages.getString("CREATE");

        final String message = rawMessage;

        return new LogMessage()
        {
            public String toString()
            {
                return message;
            }
            
            public String getLogHierarchy()
            {
                return CREATE_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a Channel message of the Format:
     * <pre>CHN-1008 : Idle Transaction : {0,number} ms</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage IDLE_TXN(Number param1)
    {
        String rawMessage = _messages.getString("IDLE_TXN");

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
                return IDLE_TXN_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a Channel message of the Format:
     * <pre>CHN-1004 : Prefetch Size (bytes) {0,number} : Count {1,number}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage PREFETCH_SIZE(Number param1, Number param2)
    {
        String rawMessage = _messages.getString("PREFETCH_SIZE");

        final Object[] messageArguments = {param1, param2};
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
                return PREFETCH_SIZE_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a Channel message of the Format:
     * <pre>CHN-1003 : Close</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage CLOSE()
    {
        String rawMessage = _messages.getString("CLOSE");

        final String message = rawMessage;

        return new LogMessage()
        {
            public String toString()
            {
                return message;
            }
            
            public String getLogHierarchy()
            {
                return CLOSE_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a Channel message of the Format:
     * <pre>CHN-1002 : Flow {0}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage FLOW(String param1)
    {
        String rawMessage = _messages.getString("FLOW");

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
                return FLOW_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a Channel message of the Format:
     * <pre>CHN-1007 : Open Transaction : {0,number} ms</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage OPEN_TXN(Number param1)
    {
        String rawMessage = _messages.getString("OPEN_TXN");

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
                return OPEN_TXN_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a Channel message of the Format:
     * <pre>CHN-1006 : Flow Control Removed</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage FLOW_REMOVED()
    {
        String rawMessage = _messages.getString("FLOW_REMOVED");

        final String message = rawMessage;

        return new LogMessage()
        {
            public String toString()
            {
                return message;
            }
            
            public String getLogHierarchy()
            {
                return FLOW_REMOVED_LOG_HIERARCHY;
            }
        };
    }


}
