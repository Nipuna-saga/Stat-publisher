



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

public class TestTableOkBodyImpl extends AMQMethodBody_8_0 implements TestTableOkBody
{
    private static final AMQMethodBodyInstanceFactory FACTORY_INSTANCE = new AMQMethodBodyInstanceFactory()
    {
        public AMQMethodBody newInstance(ByteBuffer in, long size) throws AMQFrameDecodingException
        {
            return new TestTableOkBodyImpl(in);
        }
		
 
    };
    
	
    public static AMQMethodBodyInstanceFactory getFactory()
    {
        return FACTORY_INSTANCE;
    }

    public static final int CLASS_ID =  120; 
    
    public static final int METHOD_ID = 31; 
    

	
    // Fields declared in specification
    private final long _integerResult; // [integerResult]
    private final byte[] _stringResult; // [stringResult]

    
    // Constructor

    public TestTableOkBodyImpl(ByteBuffer buffer) throws AMQFrameDecodingException
    {
        _integerResult = readLong( buffer );
        _stringResult = readBytes( buffer );
	}
	
    public TestTableOkBodyImpl(
                                long integerResult,
                                byte[] stringResult
                            )
    {
        _integerResult = integerResult;
        _stringResult = stringResult;
    }
    
    public int getClazz() 
    { 
        return CLASS_ID; 
    }
    
    public int getMethod() 
    { 
        return METHOD_ID; 
    }

    
    public final long getIntegerResult()
    {
        return _integerResult;
    }
    public final byte[] getStringResult()
    {
        return _stringResult;
    }

    protected int getBodySize()
    {      
	    int size = 8;
        size += getSizeOf( _stringResult );
        return size;        
    }

    public void writeMethodPayload(ByteBuffer buffer)
    {
        writeLong( buffer, _integerResult );
        writeBytes( buffer, _stringResult );
    }

    public boolean execute(MethodDispatcher dispatcher, int channelId) throws AMQException
	{
    return ((MethodDispatcher_8_0)dispatcher).dispatchTestTableOk(this, channelId);	

	    
	}
	
	
    public String toString()
    {
        StringBuilder buf = new StringBuilder("[TestTableOkBodyImpl: ");
        buf.append( "integerResult=" );
		buf.append(  getIntegerResult() );
		buf.append( ", " );		
        buf.append( "stringResult=" );
		buf.append(  getStringResult() == null  ? "null" : java.util.Arrays.toString( getStringResult() ) );
        buf.append("]");
        return buf.toString();
    }


}