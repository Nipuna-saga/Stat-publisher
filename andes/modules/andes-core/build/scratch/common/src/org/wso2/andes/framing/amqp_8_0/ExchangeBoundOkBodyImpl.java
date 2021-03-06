



/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

/*
 * This file is auto-generated by Qpid Gentools v.0.1 - do not modify.
 * Supported AMQP version:
 *   8-0
 */
 
 
package org.wso2.andes.framing.amqp_8_0;

import java.util.HashMap;

import org.apache.mina.common.ByteBuffer;
import org.wso2.andes.framing.*;
import org.wso2.andes.AMQException;

public class ExchangeBoundOkBodyImpl extends AMQMethodBody_8_0 implements ExchangeBoundOkBody
{
    private static final AMQMethodBodyInstanceFactory FACTORY_INSTANCE = new AMQMethodBodyInstanceFactory()
    {
        public AMQMethodBody newInstance(ByteBuffer in, long size) throws AMQFrameDecodingException
        {
            return new ExchangeBoundOkBodyImpl(in);
        }
		
 
    };
    
	
    public static AMQMethodBodyInstanceFactory getFactory()
    {
        return FACTORY_INSTANCE;
    }

    public static final int CLASS_ID =  40; 
    
    public static final int METHOD_ID = 23; 
    

	
    // Fields declared in specification
    private final int _replyCode; // [replyCode]
    private final AMQShortString _replyText; // [replyText]

    
    // Constructor

    public ExchangeBoundOkBodyImpl(ByteBuffer buffer) throws AMQFrameDecodingException
    {
        _replyCode = readUnsignedShort( buffer );
        _replyText = readAMQShortString( buffer );
	}
	
    public ExchangeBoundOkBodyImpl(
                                int replyCode,
                                AMQShortString replyText
                            )
    {
        _replyCode = replyCode;
        _replyText = replyText;
    }
    
    public int getClazz() 
    { 
        return CLASS_ID; 
    }
    
    public int getMethod() 
    { 
        return METHOD_ID; 
    }

    
    public final int getReplyCode()
    {
        return _replyCode;
    }
    public final AMQShortString getReplyText()
    {
        return _replyText;
    }

    protected int getBodySize()
    {      
	    int size = 2;
        size += getSizeOf( _replyText );
        return size;        
    }

    public void writeMethodPayload(ByteBuffer buffer)
    {
        writeUnsignedShort( buffer, _replyCode );
        writeAMQShortString( buffer, _replyText );
    }

    public boolean execute(MethodDispatcher dispatcher, int channelId) throws AMQException
	{
    return ((MethodDispatcher_8_0)dispatcher).dispatchExchangeBoundOk(this, channelId);	

	    
	}
	
	
    public String toString()
    {
        StringBuilder buf = new StringBuilder("[ExchangeBoundOkBodyImpl: ");
        buf.append( "replyCode=" );
		buf.append(  getReplyCode() );
		buf.append( ", " );		
        buf.append( "replyText=" );
		buf.append(  getReplyText() );
        buf.append("]");
        return buf.toString();
    }


}
