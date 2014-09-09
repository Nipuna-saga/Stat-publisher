



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
 *   0-9
 */
 
 
package org.wso2.andes.framing.amqp_0_9;

import java.util.HashMap;

import org.apache.mina.common.ByteBuffer;
import org.wso2.andes.framing.*;
import org.wso2.andes.AMQException;

public class ExchangeDeclareBodyImpl extends AMQMethodBody_0_9 implements ExchangeDeclareBody
{
    private static final AMQMethodBodyInstanceFactory FACTORY_INSTANCE = new AMQMethodBodyInstanceFactory()
    {
        public AMQMethodBody newInstance(ByteBuffer in, long size) throws AMQFrameDecodingException
        {
            return new ExchangeDeclareBodyImpl(in);
        }
		
 
    };
    
	
    public static AMQMethodBodyInstanceFactory getFactory()
    {
        return FACTORY_INSTANCE;
    }

    public static final int CLASS_ID =  40; 
    
    public static final int METHOD_ID = 10; 
    

	
    // Fields declared in specification
    private final int _ticket; // [ticket]
    private final AMQShortString _exchange; // [exchange]
    private final AMQShortString _type; // [type]
    private final byte _bitfield0; // [passive, durable, autoDelete, internal, nowait]
    private final FieldTable _arguments; // [arguments]

    
    // Constructor

    public ExchangeDeclareBodyImpl(ByteBuffer buffer) throws AMQFrameDecodingException
    {
        _ticket = readUnsignedShort( buffer );
        _exchange = readAMQShortString( buffer );
        _type = readAMQShortString( buffer );
        _bitfield0 = readBitfield( buffer );
        _arguments = readFieldTable( buffer );
	}
	
    public ExchangeDeclareBodyImpl(
                                int ticket,
                                AMQShortString exchange,
                                AMQShortString type,
                                boolean passive,
                                boolean durable,
                                boolean autoDelete,
                                boolean internal,
                                boolean nowait,
                                FieldTable arguments
                            )
    {
        _ticket = ticket;
        _exchange = exchange;
        _type = type;
        byte bitfield0 = (byte)0;
        if( passive )
		{		    
            bitfield0 = (byte) (((int) bitfield0) | (1 << 0));
		}
 
        if( durable )
		{		    
            bitfield0 = (byte) (((int) bitfield0) | (1 << 1));
		}
 
        if( autoDelete )
		{		    
            bitfield0 = (byte) (((int) bitfield0) | (1 << 2));
		}
 
        if( internal )
		{		    
            bitfield0 = (byte) (((int) bitfield0) | (1 << 3));
		}
 
        if( nowait )
		{		    
            bitfield0 = (byte) (((int) bitfield0) | (1 << 4));
		}
 
        _bitfield0 = bitfield0; 
        _arguments = arguments;
    }
    
    public int getClazz() 
    { 
        return CLASS_ID; 
    }
    
    public int getMethod() 
    { 
        return METHOD_ID; 
    }

    
    public final int getTicket()
    {
        return _ticket;
    }
    public final AMQShortString getExchange()
    {
        return _exchange;
    }
    public final AMQShortString getType()
    {
        return _type;
    }
    public final boolean getPassive()
    {
        return (((int)(_bitfield0)) & ( 1 << 0)) != 0;
    }
    public final boolean getDurable()
    {
        return (((int)(_bitfield0)) & ( 1 << 1)) != 0;
    }
    public final boolean getAutoDelete()
    {
        return (((int)(_bitfield0)) & ( 1 << 2)) != 0;
    }
    public final boolean getInternal()
    {
        return (((int)(_bitfield0)) & ( 1 << 3)) != 0;
    }
    public final boolean getNowait()
    {
        return (((int)(_bitfield0)) & ( 1 << 4)) != 0;
    }
    public final FieldTable getArguments()
    {
        return _arguments;
    }

    protected int getBodySize()
    {      
	    int size = 3;
        size += getSizeOf( _exchange );
        size += getSizeOf( _type );
        size += getSizeOf( _arguments );
        return size;        
    }

    public void writeMethodPayload(ByteBuffer buffer)
    {
        writeUnsignedShort( buffer, _ticket );
        writeAMQShortString( buffer, _exchange );
        writeAMQShortString( buffer, _type );
        writeBitfield( buffer, _bitfield0 );
        writeFieldTable( buffer, _arguments );
    }

    public boolean execute(MethodDispatcher dispatcher, int channelId) throws AMQException
	{
    return ((MethodDispatcher_0_9)dispatcher).dispatchExchangeDeclare(this, channelId);	

	    
	}
	
	
    public String toString()
    {
        StringBuilder buf = new StringBuilder("[ExchangeDeclareBodyImpl: ");
        buf.append( "ticket=" );
		buf.append(  getTicket() );
		buf.append( ", " );		
        buf.append( "exchange=" );
		buf.append(  getExchange() );
		buf.append( ", " );		
        buf.append( "type=" );
		buf.append(  getType() );
		buf.append( ", " );		
        buf.append( "passive=" );
		buf.append(  getPassive() );
		buf.append( ", " );		
        buf.append( "durable=" );
		buf.append(  getDurable() );
		buf.append( ", " );		
        buf.append( "autoDelete=" );
		buf.append(  getAutoDelete() );
		buf.append( ", " );		
        buf.append( "internal=" );
		buf.append(  getInternal() );
		buf.append( ", " );		
        buf.append( "nowait=" );
		buf.append(  getNowait() );
		buf.append( ", " );		
        buf.append( "arguments=" );
		buf.append(  getArguments() );
        buf.append("]");
        return buf.toString();
    }


}
