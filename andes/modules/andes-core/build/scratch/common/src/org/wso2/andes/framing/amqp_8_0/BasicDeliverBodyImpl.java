



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

public class BasicDeliverBodyImpl extends AMQMethodBody_8_0 implements BasicDeliverBody
{
    private static final AMQMethodBodyInstanceFactory FACTORY_INSTANCE = new AMQMethodBodyInstanceFactory()
    {
        public AMQMethodBody newInstance(ByteBuffer in, long size) throws AMQFrameDecodingException
        {
            return new BasicDeliverBodyImpl(in);
        }
		
 
    };
    
	
    public static AMQMethodBodyInstanceFactory getFactory()
    {
        return FACTORY_INSTANCE;
    }

    public static final int CLASS_ID =  60; 
    
    public static final int METHOD_ID = 60; 
    

	
    // Fields declared in specification
    private final AMQShortString _consumerTag; // [consumerTag]
    private final long _deliveryTag; // [deliveryTag]
    private final byte _bitfield0; // [redelivered]
    private final AMQShortString _exchange; // [exchange]
    private final AMQShortString _routingKey; // [routingKey]

    
    // Constructor

    public BasicDeliverBodyImpl(ByteBuffer buffer) throws AMQFrameDecodingException
    {
        _consumerTag = readAMQShortString( buffer );
        _deliveryTag = readLong( buffer );
        _bitfield0 = readBitfield( buffer );
        _exchange = readAMQShortString( buffer );
        _routingKey = readAMQShortString( buffer );
	}
	
    public BasicDeliverBodyImpl(
                                AMQShortString consumerTag,
                                long deliveryTag,
                                boolean redelivered,
                                AMQShortString exchange,
                                AMQShortString routingKey
                            )
    {
        _consumerTag = consumerTag;
        _deliveryTag = deliveryTag;
        byte bitfield0 = (byte)0;
        if( redelivered )
		{		    
            bitfield0 = (byte) (((int) bitfield0) | (1 << 0));
		}
 
        _bitfield0 = bitfield0; 
        _exchange = exchange;
        _routingKey = routingKey;
    }
    
    public int getClazz() 
    { 
        return CLASS_ID; 
    }
    
    public int getMethod() 
    { 
        return METHOD_ID; 
    }

    
    public final AMQShortString getConsumerTag()
    {
        return _consumerTag;
    }
    public final long getDeliveryTag()
    {
        return _deliveryTag;
    }
    public final boolean getRedelivered()
    {
        return (((int)(_bitfield0)) & ( 1 << 0)) != 0;
    }
    public final AMQShortString getExchange()
    {
        return _exchange;
    }
    public final AMQShortString getRoutingKey()
    {
        return _routingKey;
    }

    protected int getBodySize()
    {      
	    int size = 9;
        size += getSizeOf( _consumerTag );
        size += getSizeOf( _exchange );
        size += getSizeOf( _routingKey );
        return size;        
    }

    public void writeMethodPayload(ByteBuffer buffer)
    {
        writeAMQShortString( buffer, _consumerTag );
        writeLong( buffer, _deliveryTag );
        writeBitfield( buffer, _bitfield0 );
        writeAMQShortString( buffer, _exchange );
        writeAMQShortString( buffer, _routingKey );
    }

    public boolean execute(MethodDispatcher dispatcher, int channelId) throws AMQException
	{
    return ((MethodDispatcher_8_0)dispatcher).dispatchBasicDeliver(this, channelId);	

	    
	}
	
	
    public String toString()
    {
        StringBuilder buf = new StringBuilder("[BasicDeliverBodyImpl: ");
        buf.append( "consumerTag=" );
		buf.append(  getConsumerTag() );
		buf.append( ", " );		
        buf.append( "deliveryTag=" );
		buf.append(  getDeliveryTag() );
		buf.append( ", " );		
        buf.append( "redelivered=" );
		buf.append(  getRedelivered() );
		buf.append( ", " );		
        buf.append( "exchange=" );
		buf.append(  getExchange() );
		buf.append( ", " );		
        buf.append( "routingKey=" );
		buf.append(  getRoutingKey() );
        buf.append("]");
        return buf.toString();
    }


}
