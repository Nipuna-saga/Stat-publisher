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
public class ManagementConsoleMessages
{
    static ResourceBundle _messages;
    static Locale _currentLocale;
    
    public static final String MANAGEMENTCONSOLE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "managementconsole";
    public static final String OPEN_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "managementconsole.open";
    public static final String LISTENING_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "managementconsole.listening";
    public static final String SSL_KEYSTORE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "managementconsole.ssl_keystore";
    public static final String STOPPED_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "managementconsole.stopped";
    public static final String CLOSE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "managementconsole.close";
    public static final String SHUTTING_DOWN_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "managementconsole.shutting_down";
    public static final String STARTUP_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "managementconsole.startup";
    public static final String READY_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "managementconsole.ready";

    static
    {
        Logger.getLogger(MANAGEMENTCONSOLE_LOG_HIERARCHY);
        Logger.getLogger(OPEN_LOG_HIERARCHY);
        Logger.getLogger(LISTENING_LOG_HIERARCHY);
        Logger.getLogger(SSL_KEYSTORE_LOG_HIERARCHY);
        Logger.getLogger(STOPPED_LOG_HIERARCHY);
        Logger.getLogger(CLOSE_LOG_HIERARCHY);
        Logger.getLogger(SHUTTING_DOWN_LOG_HIERARCHY);
        Logger.getLogger(STARTUP_LOG_HIERARCHY);
        Logger.getLogger(READY_LOG_HIERARCHY);

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

        _messages = ResourceBundle.getBundle("org.wso2.andes.server.logging.messages.ManagementConsole_logmessages", _currentLocale);
    }


    /**
     * Log a ManagementConsole message of the Format:
     * <pre>MNG-1007 : Open : User {0}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage OPEN(String param1)
    {
        String rawMessage = _messages.getString("OPEN");

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
                return OPEN_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a ManagementConsole message of the Format:
     * <pre>MNG-1002 : Starting : {0} : Listening on port {1,number,#}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage LISTENING(String param1, Number param2)
    {
        String rawMessage = _messages.getString("LISTENING");

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
                return LISTENING_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a ManagementConsole message of the Format:
     * <pre>MNG-1006 : Using SSL Keystore : {0}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage SSL_KEYSTORE(String param1)
    {
        String rawMessage = _messages.getString("SSL_KEYSTORE");

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
                return SSL_KEYSTORE_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a ManagementConsole message of the Format:
     * <pre>MNG-1005 : Stopped</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage STOPPED()
    {
        String rawMessage = _messages.getString("STOPPED");

        final String message = rawMessage;

        return new LogMessage()
        {
            public String toString()
            {
                return message;
            }
            
            public String getLogHierarchy()
            {
                return STOPPED_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a ManagementConsole message of the Format:
     * <pre>MNG-1008 : Close</pre>
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
     * Log a ManagementConsole message of the Format:
     * <pre>MNG-1003 : Shutting down : {0} : port {1,number,#}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage SHUTTING_DOWN(String param1, Number param2)
    {
        String rawMessage = _messages.getString("SHUTTING_DOWN");

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
                return SHUTTING_DOWN_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a ManagementConsole message of the Format:
     * <pre>MNG-1001 : Startup</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage STARTUP()
    {
        String rawMessage = _messages.getString("STARTUP");

        final String message = rawMessage;

        return new LogMessage()
        {
            public String toString()
            {
                return message;
            }
            
            public String getLogHierarchy()
            {
                return STARTUP_LOG_HIERARCHY;
            }
        };
    }

    /**
     * Log a ManagementConsole message of the Format:
     * <pre>MNG-1004 : Ready[ : Using the platform JMX Agent]</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage READY(boolean opt1)
    {
        String rawMessage = _messages.getString("READY");
        StringBuffer msg = new StringBuffer();

        // Split the formatted message up on the option values so we can
        // rebuild the message based on the configured options.
        String[] parts = rawMessage.split("\\[");
        msg.append(parts[0]);

        int end;
        if (parts.length > 1)
        {

            // Add Option : : Using the platform JMX Agent
            end = parts[1].indexOf(']');
            if (opt1)
            {
                msg.append(parts[1].substring(0, end));
            }

            // Use 'end + 1' to remove the ']' from the output
            msg.append(parts[1].substring(end + 1));
        }

        rawMessage = msg.toString();

        final String message = rawMessage;

        return new LogMessage()
        {
            public String toString()
            {
                return message;
            }
            
            public String getLogHierarchy()
            {
                return READY_LOG_HIERARCHY;
            }
        };
    }


}
