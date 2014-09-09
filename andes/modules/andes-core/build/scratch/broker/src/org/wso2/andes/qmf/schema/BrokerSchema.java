
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

package org.wso2.andes.qmf.schema;

import org.wso2.andes.qmf.*;
import org.wso2.andes.server.virtualhost.VirtualHost;
import org.wso2.andes.server.message.ServerMessage;
import org.wso2.andes.transport.codec.BBEncoder;
import org.wso2.andes.transport.codec.BBDecoder;

import java.util.Arrays;
import java.util.UUID;
import java.util.Map;

            

public class BrokerSchema extends QMFPackage
{
    private static final byte QMF_VERSION = (byte) '2';
    
    private static final BrokerSchema PACKAGE = new BrokerSchema();
    private static final String SCHEMA_NAME = "org.apache.qpid.broker";
    

    
    protected abstract class QMFInfoCommand<T extends QMFObject> extends QMFCommand
    {
        private final T _object;
        private final long _sampleTime;
        
        
        protected QMFInfoCommand(QMFCommand trigger, QMFOperation op, T object, long sampleTime)
        {
            this(trigger.getHeader().getVersion(),
                 trigger.getHeader().getSeq(),
                 op,
                 object,
                 sampleTime);
        }
        
        protected QMFInfoCommand(QMFOperation op, T object, long sampleTime)
        {
            this(QMF_VERSION,0,op,object,sampleTime);
        }
        
        private QMFInfoCommand(final byte qmfVersion,
                               final int seq,
                               final QMFOperation op,
                               final T object,
                               final long sampleTime)
        {
            super(new QMFCommandHeader(qmfVersion, seq,op));
            _object = object;
            _sampleTime = sampleTime;
        }
          
        public T getObject()
        {
            return _object;
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8(SCHEMA_NAME);
            encoder.writeStr8(_object.getQMFClass().getName());
            encoder.writeBin128(new byte[16]);
            encoder.writeUint64(_sampleTime * 1000000L);
            encoder.writeUint64(_object.getCreateTime() * 1000000L);
            encoder.writeUint64(_object.getDeleteTime() * 1000000L);
            encoder.writeBin128(_object.getId());
        }
    }
    
    protected abstract class QMFConfigInfoCommand<T extends QMFObject> extends QMFInfoCommand<T>
    {
        protected QMFConfigInfoCommand(T object, long sampleTime)
        {
            super(QMFOperation.CONFIG_INDICATION, object, sampleTime);
        }
    }
    
    protected abstract class QMFInstrumentInfoCommand<T extends QMFObject> extends QMFInfoCommand<T>
    {
        protected QMFInstrumentInfoCommand(T object, long sampleTime)
        {
            super(QMFOperation.INSTRUMENTATION_INDICATION, object, sampleTime);
        }
    }
    
    protected abstract class QMFGetQueryResponseCommand<T extends QMFObject> extends QMFInfoCommand<T>
    {
        protected QMFGetQueryResponseCommand(T object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(cmd, QMFOperation.GET_QUERY_RESPONSE, object, sampleTime);
        }
    }
    
    

    
    public class SystemClass extends QMFObjectClass<SystemObject, SystemDelegate>
    {
        
        public class SystemIdProperty extends QMFProperty
        {
            
            private SystemIdProperty()
            {
                super( "systemId",
                       QMFType.UUID,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

            }     
        }
    
        private final SystemIdProperty _systemIdProperty = new SystemIdProperty();
    
        public class OsNameProperty extends QMFProperty
        {
            
            private OsNameProperty()
            {
                super( "osName",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Operating System Name");

            }     
        }
    
        private final OsNameProperty _osNameProperty = new OsNameProperty();
    
        public class NodeNameProperty extends QMFProperty
        {
            
            private NodeNameProperty()
            {
                super( "nodeName",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Node Name");

            }     
        }
    
        private final NodeNameProperty _nodeNameProperty = new NodeNameProperty();
    
        public class ReleaseProperty extends QMFProperty
        {
            
            private ReleaseProperty()
            {
                super( "release",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

            }     
        }
    
        private final ReleaseProperty _releaseProperty = new ReleaseProperty();
    
        public class VersionProperty extends QMFProperty
        {
            
            private VersionProperty()
            {
                super( "version",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

            }     
        }
    
        private final VersionProperty _versionProperty = new VersionProperty();
    
        public class MachineProperty extends QMFProperty
        {
            
            private MachineProperty()
            {
                super( "machine",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

            }     
        }
    
        private final MachineProperty _machineProperty = new MachineProperty();
    
    
        private SystemClass()
        {
            super("system",
                 new byte[16]);

            setProperties( Arrays.asList( new QMFProperty[] { _systemIdProperty, _osNameProperty, _nodeNameProperty, _releaseProperty, _versionProperty, _machineProperty } ) );
            setStatistics( Arrays.asList( new QMFStatistic[] {  } ) );
            setMethods( Arrays.asList( new QMFMethod[] {  } ) );
        }
        
        public SystemObject newInstance(final SystemDelegate delegate)
        {
            return new SystemObject(delegate);
        }
        
    }
    
    private final SystemClass _systemClass = new SystemClass();
    
    public interface SystemDelegate extends QMFObject.Delegate
    {
        UUID getSystemId();
        String getOsName();
        String getNodeName();
        String getRelease();
        String getVersion();
        String getMachine();
    }
    
    public final class SystemObject extends QMFObject<SystemClass, SystemDelegate>
    {
        protected SystemObject(SystemDelegate delegate)
        {
           super(delegate);
        }
        
        public SystemClass getQMFClass()
        {
            return _systemClass;
        }
        
        public QMFCommand asConfigInfoCmd(long sampleTime) 
        {
            return new QMFSystemConfigInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asInstrumentInfoCmd(long sampleTime) 
        {
           return new QMFSystemInstrumentInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asGetQueryResponseCmd(QMFGetQueryCommand queryCommand, long sampleTime) 
        {
            return new QMFSystemGetQueryResponseCommand(this,queryCommand,sampleTime);
        }
    
    
        
        
            public UUID getSystemId()
            {
                return getDelegate().getSystemId();
            }
            
            
        
            public String getOsName()
            {
                return getDelegate().getOsName();
            }
            
            
        
            public String getNodeName()
            {
                return getDelegate().getNodeName();
            }
            
            
        
            public String getRelease()
            {
                return getDelegate().getRelease();
            }
            
            
        
            public String getVersion()
            {
                return getDelegate().getVersion();
            }
            
            
        
            public String getMachine()
            {
                return getDelegate().getMachine();
            }
            
            
    }

    public final class QMFSystemConfigInfoCommand extends QMFConfigInfoCommand<SystemObject>
    {
        
        protected QMFSystemConfigInfoCommand(SystemObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeUuid( getObject().getSystemId() );
                    
            encoder.writeStr8( getObject().getOsName() );
                    
            encoder.writeStr8( getObject().getNodeName() );
                    
            encoder.writeStr8( getObject().getRelease() );
                    
            encoder.writeStr8( getObject().getVersion() );
                    
            encoder.writeStr8( getObject().getMachine() );
                    
        }
    }
    
    public final class QMFSystemInstrumentInfoCommand extends QMFInstrumentInfoCommand<SystemObject>
    {
    
        protected QMFSystemInstrumentInfoCommand(SystemObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
        }
    }
    
    public final class QMFSystemGetQueryResponseCommand extends QMFGetQueryResponseCommand<SystemObject>
    {
    
        protected QMFSystemGetQueryResponseCommand(SystemObject object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(object, cmd, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeUuid( getObject().getSystemId() );
                    
            encoder.writeStr8( getObject().getOsName() );
                    
            encoder.writeStr8( getObject().getNodeName() );
                    
            encoder.writeStr8( getObject().getRelease() );
                    
            encoder.writeStr8( getObject().getVersion() );
                    
            encoder.writeStr8( getObject().getMachine() );
                    
        }
    }
    
    
    


    
    public class BrokerClass extends QMFObjectClass<BrokerObject, BrokerDelegate>
    {
        
        public class NameProperty extends QMFProperty
        {
            
            private NameProperty()
            {
                super( "name",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

                setDescription("Index for the broker at this agent");

            }     
        }
    
        private final NameProperty _nameProperty = new NameProperty();
    
        public class SystemRefProperty extends QMFProperty
        {
            
            private SystemRefProperty()
            {
                super( "systemRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("System ID");

                setReferencedClass("system");

            }     
        }
    
        private final SystemRefProperty _systemRefProperty = new SystemRefProperty();
    
        public class PortProperty extends QMFProperty
        {
            
            private PortProperty()
            {
                super( "port",
                       QMFType.UINT16,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("TCP Port for AMQP Service");

            }     
        }
    
        private final PortProperty _portProperty = new PortProperty();
    
        public class WorkerThreadsProperty extends QMFProperty
        {
            
            private WorkerThreadsProperty()
            {
                super( "workerThreads",
                       QMFType.UINT16,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Thread pool size");

            }     
        }
    
        private final WorkerThreadsProperty _workerThreadsProperty = new WorkerThreadsProperty();
    
        public class MaxConnsProperty extends QMFProperty
        {
            
            private MaxConnsProperty()
            {
                super( "maxConns",
                       QMFType.UINT16,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Maximum allowed connections");

            }     
        }
    
        private final MaxConnsProperty _maxConnsProperty = new MaxConnsProperty();
    
        public class ConnBacklogProperty extends QMFProperty
        {
            
            private ConnBacklogProperty()
            {
                super( "connBacklog",
                       QMFType.UINT16,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Connection backlog limit for listening socket");

            }     
        }
    
        private final ConnBacklogProperty _connBacklogProperty = new ConnBacklogProperty();
    
        public class StagingThresholdProperty extends QMFProperty
        {
            
            private StagingThresholdProperty()
            {
                super( "stagingThreshold",
                       QMFType.UINT32,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Broker stages messages over this size to disk");

            }     
        }
    
        private final StagingThresholdProperty _stagingThresholdProperty = new StagingThresholdProperty();
    
        public class MgmtPubIntervalProperty extends QMFProperty
        {
            
            private MgmtPubIntervalProperty()
            {
                super( "mgmtPubInterval",
                       QMFType.UINT16,
                       QMFProperty.AccessCode.RW,
                       false,
                       false);

                setDescription("Interval for management broadcasts");

                setMin(1);

                setUnit("second");
        
            }     
        }
    
        private final MgmtPubIntervalProperty _mgmtPubIntervalProperty = new MgmtPubIntervalProperty();
    
        public class VersionProperty extends QMFProperty
        {
            
            private VersionProperty()
            {
                super( "version",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Running software version");

            }     
        }
    
        private final VersionProperty _versionProperty = new VersionProperty();
    
        public class DataDirProperty extends QMFProperty
        {
            
            private DataDirProperty()
            {
                super( "dataDir",
                       QMFType.STR16,
                       QMFProperty.AccessCode.RO,
                       false,
                       true);

                setDescription("Persistent configuration storage location");

            }     
        }
    
        private final DataDirProperty _dataDirProperty = new DataDirProperty();
    
        public class UptimeStatistic extends QMFStatistic
        {
        
            private UptimeStatistic()
            {
                super( "uptime", QMFType.DELTATIME, null, null); 
            }     
        }
        
        private final UptimeStatistic _uptimeStatistic = new UptimeStatistic();
    
        public class EchoMethod extends QMFMethod<BrokerObject>
        {
            private EchoMethod()
            {
                super( "echo", "Request a response to test the path to the management broker");
        
                
                QMFMethod.Argument sequence = new QMFMethod.Argument("sequence", QMFType.UINT32);
                sequence.setDirection(QMFMethod.Direction.IO);                  
                addArgument( sequence );
    
                QMFMethod.Argument body = new QMFMethod.Argument("body", QMFType.STR16);
                body.setDirection(QMFMethod.Direction.IO);                  
                addArgument( body );
    
        
            }
            
            
            public EchoMethodInvocation parse(BBDecoder decoder)
            {
                Long sequence = decoder.readUint32();
    String body = decoder.readStr16();
    
                return new EchoMethodInvocation( sequence,  body);
            }
        }
        
        private final EchoMethod _echoMethod = new EchoMethod();
        
        private class EchoMethodInvocation implements QMFMethodInvocation<BrokerObject>
        {
            
            private final Long _sequence;
    
            private final String _body;
    
        
            private EchoMethodInvocation(Long sequence, String body)
            {
                
                _sequence = sequence;
    
                _body = body;
    
            }
        
            public QMFMethodResponseCommand execute(BrokerObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.echo( new EchoMethodResponseCommandFactory(cmd),  _sequence,  _body );
            }
        }
        
        public final class EchoMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private EchoMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public EchoMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new EchoMethodResponseCommand(_requestCmd, status, null);
            }
            
            public EchoMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new EchoMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public EchoMethodResponseCommand createResponseCommand( Long sequence, String body )
            {
                return new EchoMethodResponseCommand(_requestCmd,  sequence,  body);
            }
        }
        
        public final class EchoMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private final Long _sequence;
    
            private final String _body;
    
            private EchoMethodResponseCommand(QMFMethodRequestCommand cmd, Long sequence, String body)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
                _sequence = sequence;
    
                _body = body;
    
            }
            
            private EchoMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
                _sequence = null;
    
                _body = null;
    
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
                if(getStatus().equals(CompletionCode.OK))
                {
                    
        encoder.writeUint32( _sequence );
    
        encoder.writeStr16( _body );
    
                }
                
            }
        }
        
    
        public class ConnectMethod extends QMFMethod<BrokerObject>
        {
            private ConnectMethod()
            {
                super( "connect", "Establish a connection to another broker");
        
                
                QMFMethod.Argument host = new QMFMethod.Argument("host", QMFType.STR8);
                host.setDirection(QMFMethod.Direction.I);                  
                addArgument( host );
    
                QMFMethod.Argument port = new QMFMethod.Argument("port", QMFType.UINT32);
                port.setDirection(QMFMethod.Direction.I);                  
                addArgument( port );
    
                QMFMethod.Argument durable = new QMFMethod.Argument("durable", QMFType.BOOLEAN);
                durable.setDirection(QMFMethod.Direction.I);                  
                addArgument( durable );
    
                QMFMethod.Argument authMechanism = new QMFMethod.Argument("authMechanism", QMFType.STR8);
                authMechanism.setDirection(QMFMethod.Direction.I);                  
                addArgument( authMechanism );
    
                QMFMethod.Argument username = new QMFMethod.Argument("username", QMFType.STR8);
                username.setDirection(QMFMethod.Direction.I);                  
                addArgument( username );
    
                QMFMethod.Argument password = new QMFMethod.Argument("password", QMFType.STR8);
                password.setDirection(QMFMethod.Direction.I);                  
                addArgument( password );
    
                QMFMethod.Argument transport = new QMFMethod.Argument("transport", QMFType.STR8);
                transport.setDirection(QMFMethod.Direction.I);                  
                addArgument( transport );
    
        
            }
            
            
            public ConnectMethodInvocation parse(BBDecoder decoder)
            {
                String host = decoder.readStr8();
    Long port = decoder.readUint32();
    Boolean durable = decoder.readInt8() != 0;
    String authMechanism = decoder.readStr8();
    String username = decoder.readStr8();
    String password = decoder.readStr8();
    String transport = decoder.readStr8();
    
                return new ConnectMethodInvocation( host,  port,  durable,  authMechanism,  username,  password,  transport);
            }
        }
        
        private final ConnectMethod _connectMethod = new ConnectMethod();
        
        private class ConnectMethodInvocation implements QMFMethodInvocation<BrokerObject>
        {
            
            private final String _host;
    
            private final Long _port;
    
            private final Boolean _durable;
    
            private final String _authMechanism;
    
            private final String _username;
    
            private final String _password;
    
            private final String _transport;
    
        
            private ConnectMethodInvocation(String host, Long port, Boolean durable, String authMechanism, String username, String password, String transport)
            {
                
                _host = host;
    
                _port = port;
    
                _durable = durable;
    
                _authMechanism = authMechanism;
    
                _username = username;
    
                _password = password;
    
                _transport = transport;
    
            }
        
            public QMFMethodResponseCommand execute(BrokerObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.connect( new ConnectMethodResponseCommandFactory(cmd),  _host,  _port,  _durable,  _authMechanism,  _username,  _password,  _transport );
            }
        }
        
        public final class ConnectMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private ConnectMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public ConnectMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new ConnectMethodResponseCommand(_requestCmd, status, null);
            }
            
            public ConnectMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new ConnectMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public ConnectMethodResponseCommand createResponseCommand(  )
            {
                return new ConnectMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class ConnectMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private ConnectMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private ConnectMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
        public class QueueMoveMessagesMethod extends QMFMethod<BrokerObject>
        {
            private QueueMoveMessagesMethod()
            {
                super( "queueMoveMessages", "Move messages from one queue to another");
        
                
                QMFMethod.Argument srcQueue = new QMFMethod.Argument("srcQueue", QMFType.STR8);
                srcQueue.setDescription("Source queue"); 
                srcQueue.setDirection(QMFMethod.Direction.I);                  
                addArgument( srcQueue );
    
                QMFMethod.Argument destQueue = new QMFMethod.Argument("destQueue", QMFType.STR8);
                destQueue.setDescription("Destination queue"); 
                destQueue.setDirection(QMFMethod.Direction.I);                  
                addArgument( destQueue );
    
                QMFMethod.Argument qty = new QMFMethod.Argument("qty", QMFType.UINT32);
                qty.setDescription("# of messages to move. 0 means all messages"); 
                qty.setDirection(QMFMethod.Direction.I);                  
                addArgument( qty );
    
        
            }
            
            
            public QueueMoveMessagesMethodInvocation parse(BBDecoder decoder)
            {
                String srcQueue = decoder.readStr8();
    String destQueue = decoder.readStr8();
    Long qty = decoder.readUint32();
    
                return new QueueMoveMessagesMethodInvocation( srcQueue,  destQueue,  qty);
            }
        }
        
        private final QueueMoveMessagesMethod _queueMoveMessagesMethod = new QueueMoveMessagesMethod();
        
        private class QueueMoveMessagesMethodInvocation implements QMFMethodInvocation<BrokerObject>
        {
            
            private final String _srcQueue;
    
            private final String _destQueue;
    
            private final Long _qty;
    
        
            private QueueMoveMessagesMethodInvocation(String srcQueue, String destQueue, Long qty)
            {
                
                _srcQueue = srcQueue;
    
                _destQueue = destQueue;
    
                _qty = qty;
    
            }
        
            public QMFMethodResponseCommand execute(BrokerObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.queueMoveMessages( new QueueMoveMessagesMethodResponseCommandFactory(cmd),  _srcQueue,  _destQueue,  _qty );
            }
        }
        
        public final class QueueMoveMessagesMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private QueueMoveMessagesMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public QueueMoveMessagesMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new QueueMoveMessagesMethodResponseCommand(_requestCmd, status, null);
            }
            
            public QueueMoveMessagesMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new QueueMoveMessagesMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public QueueMoveMessagesMethodResponseCommand createResponseCommand(  )
            {
                return new QueueMoveMessagesMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class QueueMoveMessagesMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private QueueMoveMessagesMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private QueueMoveMessagesMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
        public class SetLogLevelMethod extends QMFMethod<BrokerObject>
        {
            private SetLogLevelMethod()
            {
                super( "setLogLevel", "Set the log level");
        
                
                QMFMethod.Argument level = new QMFMethod.Argument("level", QMFType.STR8);
                level.setDirection(QMFMethod.Direction.I);                  
                addArgument( level );
    
        
            }
            
            
            public SetLogLevelMethodInvocation parse(BBDecoder decoder)
            {
                String level = decoder.readStr8();
    
                return new SetLogLevelMethodInvocation( level);
            }
        }
        
        private final SetLogLevelMethod _setLogLevelMethod = new SetLogLevelMethod();
        
        private class SetLogLevelMethodInvocation implements QMFMethodInvocation<BrokerObject>
        {
            
            private final String _level;
    
        
            private SetLogLevelMethodInvocation(String level)
            {
                
                _level = level;
    
            }
        
            public QMFMethodResponseCommand execute(BrokerObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.setLogLevel( new SetLogLevelMethodResponseCommandFactory(cmd),  _level );
            }
        }
        
        public final class SetLogLevelMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private SetLogLevelMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public SetLogLevelMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new SetLogLevelMethodResponseCommand(_requestCmd, status, null);
            }
            
            public SetLogLevelMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new SetLogLevelMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public SetLogLevelMethodResponseCommand createResponseCommand(  )
            {
                return new SetLogLevelMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class SetLogLevelMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private SetLogLevelMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private SetLogLevelMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
        public class GetLogLevelMethod extends QMFMethod<BrokerObject>
        {
            private GetLogLevelMethod()
            {
                super( "getLogLevel", "Get the current log level");
        
                
                QMFMethod.Argument level = new QMFMethod.Argument("level", QMFType.STR8);
                level.setDirection(QMFMethod.Direction.O);                  
                addArgument( level );
    
        
            }
            
            
            public GetLogLevelMethodInvocation parse(BBDecoder decoder)
            {
                
                return new GetLogLevelMethodInvocation();
            }
        }
        
        private final GetLogLevelMethod _getLogLevelMethod = new GetLogLevelMethod();
        
        private class GetLogLevelMethodInvocation implements QMFMethodInvocation<BrokerObject>
        {
            
        
            private GetLogLevelMethodInvocation()
            {
                
            }
        
            public QMFMethodResponseCommand execute(BrokerObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.getLogLevel( new GetLogLevelMethodResponseCommandFactory(cmd) );
            }
        }
        
        public final class GetLogLevelMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private GetLogLevelMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public GetLogLevelMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new GetLogLevelMethodResponseCommand(_requestCmd, status, null);
            }
            
            public GetLogLevelMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new GetLogLevelMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public GetLogLevelMethodResponseCommand createResponseCommand( String level )
            {
                return new GetLogLevelMethodResponseCommand(_requestCmd,  level);
            }
        }
        
        public final class GetLogLevelMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private final String _level;
    
            private GetLogLevelMethodResponseCommand(QMFMethodRequestCommand cmd, String level)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
                _level = level;
    
            }
            
            private GetLogLevelMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
                _level = null;
    
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
                if(getStatus().equals(CompletionCode.OK))
                {
                    
        encoder.writeStr8( _level );
    
                }
                
            }
        }
        
    
        public class CreateMethod extends QMFMethod<BrokerObject>
        {
            private CreateMethod()
            {
                super( "create", "Create an object of the specified type");
        
                
                QMFMethod.Argument type = new QMFMethod.Argument("type", QMFType.STR8);
                type.setDescription("The type of object to create"); 
                type.setDirection(QMFMethod.Direction.I);                  
                addArgument( type );
    
                QMFMethod.Argument name = new QMFMethod.Argument("name", QMFType.STR8);
                name.setDescription("The name of the object to create"); 
                name.setDirection(QMFMethod.Direction.I);                  
                addArgument( name );
    
                QMFMethod.Argument properties = new QMFMethod.Argument("properties", QMFType.MAP);
                properties.setDescription("Type specific object properties"); 
                properties.setDirection(QMFMethod.Direction.I);                  
                addArgument( properties );
    
                QMFMethod.Argument strict = new QMFMethod.Argument("strict", QMFType.BOOLEAN);
                strict.setDescription("If specified, treat unrecognised object properties as an error"); 
                strict.setDirection(QMFMethod.Direction.I);                  
                addArgument( strict );
    
        
            }
            
            
            public CreateMethodInvocation parse(BBDecoder decoder)
            {
                String type = decoder.readStr8();
    String name = decoder.readStr8();
    Map properties = decoder.readMap();
    Boolean strict = decoder.readInt8() != 0;
    
                return new CreateMethodInvocation( type,  name,  properties,  strict);
            }
        }
        
        private final CreateMethod _createMethod = new CreateMethod();
        
        private class CreateMethodInvocation implements QMFMethodInvocation<BrokerObject>
        {
            
            private final String _type;
    
            private final String _name;
    
            private final Map _properties;
    
            private final Boolean _strict;
    
        
            private CreateMethodInvocation(String type, String name, Map properties, Boolean strict)
            {
                
                _type = type;
    
                _name = name;
    
                _properties = properties;
    
                _strict = strict;
    
            }
        
            public QMFMethodResponseCommand execute(BrokerObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.create( new CreateMethodResponseCommandFactory(cmd),  _type,  _name,  _properties,  _strict );
            }
        }
        
        public final class CreateMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private CreateMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public CreateMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new CreateMethodResponseCommand(_requestCmd, status, null);
            }
            
            public CreateMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new CreateMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public CreateMethodResponseCommand createResponseCommand(  )
            {
                return new CreateMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class CreateMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private CreateMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private CreateMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
        public class DeleteMethod extends QMFMethod<BrokerObject>
        {
            private DeleteMethod()
            {
                super( "delete", "Delete an object of the specified type");
        
                
                QMFMethod.Argument type = new QMFMethod.Argument("type", QMFType.STR8);
                type.setDescription("The type of object to delete"); 
                type.setDirection(QMFMethod.Direction.I);                  
                addArgument( type );
    
                QMFMethod.Argument name = new QMFMethod.Argument("name", QMFType.STR8);
                name.setDescription("The name of the object to delete"); 
                name.setDirection(QMFMethod.Direction.I);                  
                addArgument( name );
    
                QMFMethod.Argument options = new QMFMethod.Argument("options", QMFType.MAP);
                options.setDescription("Type specific object options for deletion"); 
                options.setDirection(QMFMethod.Direction.I);                  
                addArgument( options );
    
        
            }
            
            
            public DeleteMethodInvocation parse(BBDecoder decoder)
            {
                String type = decoder.readStr8();
    String name = decoder.readStr8();
    Map options = decoder.readMap();
    
                return new DeleteMethodInvocation( type,  name,  options);
            }
        }
        
        private final DeleteMethod _deleteMethod = new DeleteMethod();
        
        private class DeleteMethodInvocation implements QMFMethodInvocation<BrokerObject>
        {
            
            private final String _type;
    
            private final String _name;
    
            private final Map _options;
    
        
            private DeleteMethodInvocation(String type, String name, Map options)
            {
                
                _type = type;
    
                _name = name;
    
                _options = options;
    
            }
        
            public QMFMethodResponseCommand execute(BrokerObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.delete( new DeleteMethodResponseCommandFactory(cmd),  _type,  _name,  _options );
            }
        }
        
        public final class DeleteMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private DeleteMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public DeleteMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new DeleteMethodResponseCommand(_requestCmd, status, null);
            }
            
            public DeleteMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new DeleteMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public DeleteMethodResponseCommand createResponseCommand(  )
            {
                return new DeleteMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class DeleteMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private DeleteMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private DeleteMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
    
        private BrokerClass()
        {
            super("broker",
                 new byte[16]);

            setProperties( Arrays.asList( new QMFProperty[] { _nameProperty, _systemRefProperty, _portProperty, _workerThreadsProperty, _maxConnsProperty, _connBacklogProperty, _stagingThresholdProperty, _mgmtPubIntervalProperty, _versionProperty, _dataDirProperty } ) );
            setStatistics( Arrays.asList( new QMFStatistic[] { _uptimeStatistic } ) );
            setMethods( Arrays.asList( new QMFMethod[] { _echoMethod, _connectMethod, _queueMoveMessagesMethod, _setLogLevelMethod, _getLogLevelMethod, _createMethod, _deleteMethod } ) );
        }
        
        public BrokerObject newInstance(final BrokerDelegate delegate)
        {
            return new BrokerObject(delegate);
        }
        
    }
    
    private final BrokerClass _brokerClass = new BrokerClass();
    
    public interface BrokerDelegate extends QMFObject.Delegate
    {
        String getName();
        SystemObject getSystemRef();
        Integer getPort();
        Integer getWorkerThreads();
        Integer getMaxConns();
        Integer getConnBacklog();
        Long getStagingThreshold();
        Integer getMgmtPubInterval();
        String getVersion();
        String getDataDir();
        Long getUptime();
        BrokerClass.EchoMethodResponseCommand echo(BrokerClass.EchoMethodResponseCommandFactory factory, Long sequence, String body);
        BrokerClass.ConnectMethodResponseCommand connect(BrokerClass.ConnectMethodResponseCommandFactory factory, String host, Long port, Boolean durable, String authMechanism, String username, String password, String transport);
        BrokerClass.QueueMoveMessagesMethodResponseCommand queueMoveMessages(BrokerClass.QueueMoveMessagesMethodResponseCommandFactory factory, String srcQueue, String destQueue, Long qty);
        BrokerClass.SetLogLevelMethodResponseCommand setLogLevel(BrokerClass.SetLogLevelMethodResponseCommandFactory factory, String level);
        BrokerClass.GetLogLevelMethodResponseCommand getLogLevel(BrokerClass.GetLogLevelMethodResponseCommandFactory factory);
        BrokerClass.CreateMethodResponseCommand create(BrokerClass.CreateMethodResponseCommandFactory factory, String type, String name, Map properties, Boolean strict);
        BrokerClass.DeleteMethodResponseCommand delete(BrokerClass.DeleteMethodResponseCommandFactory factory, String type, String name, Map options);
    }
    
    public final class BrokerObject extends QMFObject<BrokerClass, BrokerDelegate>
    {
        protected BrokerObject(BrokerDelegate delegate)
        {
           super(delegate);
        }
        
        public BrokerClass getQMFClass()
        {
            return _brokerClass;
        }
        
        public QMFCommand asConfigInfoCmd(long sampleTime) 
        {
            return new QMFBrokerConfigInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asInstrumentInfoCmd(long sampleTime) 
        {
           return new QMFBrokerInstrumentInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asGetQueryResponseCmd(QMFGetQueryCommand queryCommand, long sampleTime) 
        {
            return new QMFBrokerGetQueryResponseCommand(this,queryCommand,sampleTime);
        }
    
    
        
        public BrokerClass.EchoMethodResponseCommand echo(BrokerClass.EchoMethodResponseCommandFactory factory, Long sequence, String body)
        {
            return getDelegate().echo(factory,  sequence,  body );
        }
        
        public BrokerClass.ConnectMethodResponseCommand connect(BrokerClass.ConnectMethodResponseCommandFactory factory, String host, Long port, Boolean durable, String authMechanism, String username, String password, String transport)
        {
            return getDelegate().connect(factory,  host,  port,  durable,  authMechanism,  username,  password,  transport );
        }
        
        public BrokerClass.QueueMoveMessagesMethodResponseCommand queueMoveMessages(BrokerClass.QueueMoveMessagesMethodResponseCommandFactory factory, String srcQueue, String destQueue, Long qty)
        {
            return getDelegate().queueMoveMessages(factory,  srcQueue,  destQueue,  qty );
        }
        
        public BrokerClass.SetLogLevelMethodResponseCommand setLogLevel(BrokerClass.SetLogLevelMethodResponseCommandFactory factory, String level)
        {
            return getDelegate().setLogLevel(factory,  level );
        }
        
        public BrokerClass.GetLogLevelMethodResponseCommand getLogLevel(BrokerClass.GetLogLevelMethodResponseCommandFactory factory)
        {
            return getDelegate().getLogLevel(factory );
        }
        
        public BrokerClass.CreateMethodResponseCommand create(BrokerClass.CreateMethodResponseCommandFactory factory, String type, String name, Map properties, Boolean strict)
        {
            return getDelegate().create(factory,  type,  name,  properties,  strict );
        }
        
        public BrokerClass.DeleteMethodResponseCommand delete(BrokerClass.DeleteMethodResponseCommandFactory factory, String type, String name, Map options)
        {
            return getDelegate().delete(factory,  type,  name,  options );
        }
        
        
            public String getName()
            {
                return getDelegate().getName();
            }
            
            
        
            public SystemObject getSystemRef()
            {
                return getDelegate().getSystemRef();
            }
            
            
        
            public Integer getPort()
            {
                return getDelegate().getPort();
            }
            
            
        
            public Integer getWorkerThreads()
            {
                return getDelegate().getWorkerThreads();
            }
            
            
        
            public Integer getMaxConns()
            {
                return getDelegate().getMaxConns();
            }
            
            
        
            public Integer getConnBacklog()
            {
                return getDelegate().getConnBacklog();
            }
            
            
        
            public Long getStagingThreshold()
            {
                return getDelegate().getStagingThreshold();
            }
            
            
        
            public Integer getMgmtPubInterval()
            {
                return getDelegate().getMgmtPubInterval();
            }
            
            
        
            public String getVersion()
            {
                return getDelegate().getVersion();
            }
            
            
        
            public String getDataDir()
            {
                return getDelegate().getDataDir();
            }
            
            
        
            public Long getUptime()
            {
                return getDelegate().getUptime();
            }
            
            
    }

    public final class QMFBrokerConfigInfoCommand extends QMFConfigInfoCommand<BrokerObject>
    {
        
        protected QMFBrokerConfigInfoCommand(BrokerObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            byte presence0 = (byte) 0;              
        
            if(  getObject().getDataDir() != null )
            {
                presence0 |= (1 <<0);
            }
        
            encoder.writeUint8( presence0 );
        
            encoder.writeStr8( getObject().getName() );
                    
            encoder.writeBin128( getObject().getSystemRef().getId() );
                    
            encoder.writeUint16( getObject().getPort() );
                    
            encoder.writeUint16( getObject().getWorkerThreads() );
                    
            encoder.writeUint16( getObject().getMaxConns() );
                    
            encoder.writeUint16( getObject().getConnBacklog() );
                    
            encoder.writeUint32( getObject().getStagingThreshold() );
                    
            encoder.writeUint16( getObject().getMgmtPubInterval() );
                    
            encoder.writeStr8( getObject().getVersion() );
                    
            if(getObject().getDataDir() != null)
            {
                encoder.writeStr16( getObject().getDataDir() );
            }
            
        }
    }
    
    public final class QMFBrokerInstrumentInfoCommand extends QMFInstrumentInfoCommand<BrokerObject>
    {
    
        protected QMFBrokerInstrumentInfoCommand(BrokerObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeUint64( getObject().getUptime() );
                    
        }
    }
    
    public final class QMFBrokerGetQueryResponseCommand extends QMFGetQueryResponseCommand<BrokerObject>
    {
    
        protected QMFBrokerGetQueryResponseCommand(BrokerObject object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(object, cmd, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            byte presence0 = (byte) 0;              
        
            if(  getObject().getDataDir() != null )
            {
                presence0 |= (1 <<0);
            }
        
            encoder.writeUint8( presence0 );
        
            encoder.writeStr8( getObject().getName() );
                    
            encoder.writeBin128( getObject().getSystemRef().getId() );
                    
            encoder.writeUint16( getObject().getPort() );
                    
            encoder.writeUint16( getObject().getWorkerThreads() );
                    
            encoder.writeUint16( getObject().getMaxConns() );
                    
            encoder.writeUint16( getObject().getConnBacklog() );
                    
            encoder.writeUint32( getObject().getStagingThreshold() );
                    
            encoder.writeUint16( getObject().getMgmtPubInterval() );
                    
            encoder.writeStr8( getObject().getVersion() );
                    
            if(getObject().getDataDir() != null)
            {
                encoder.writeStr16( getObject().getDataDir() );
            }
            
            encoder.writeUint64( getObject().getUptime() );
                    
        }
    }
    
    
    


    
    public class AgentClass extends QMFObjectClass<AgentObject, AgentDelegate>
    {
        
        public class ConnectionRefProperty extends QMFProperty
        {
            
            private ConnectionRefProperty()
            {
                super( "connectionRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RO,
                       true,
                       false);

                setReferencedClass("connection");

            }     
        }
    
        private final ConnectionRefProperty _connectionRefProperty = new ConnectionRefProperty();
    
        public class LabelProperty extends QMFProperty
        {
            
            private LabelProperty()
            {
                super( "label",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Label for agent");

            }     
        }
    
        private final LabelProperty _labelProperty = new LabelProperty();
    
        public class RegisteredToProperty extends QMFProperty
        {
            
            private RegisteredToProperty()
            {
                super( "registeredTo",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Broker agent is registered to");

                setReferencedClass("broker");

            }     
        }
    
        private final RegisteredToProperty _registeredToProperty = new RegisteredToProperty();
    
        public class SystemIdProperty extends QMFProperty
        {
            
            private SystemIdProperty()
            {
                super( "systemId",
                       QMFType.UUID,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Identifier of system where agent resides");

            }     
        }
    
        private final SystemIdProperty _systemIdProperty = new SystemIdProperty();
    
        public class BrokerBankProperty extends QMFProperty
        {
            
            private BrokerBankProperty()
            {
                super( "brokerBank",
                       QMFType.UINT32,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Assigned object-id broker bank");

            }     
        }
    
        private final BrokerBankProperty _brokerBankProperty = new BrokerBankProperty();
    
        public class AgentBankProperty extends QMFProperty
        {
            
            private AgentBankProperty()
            {
                super( "agentBank",
                       QMFType.UINT32,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Assigned object-id agent bank");

            }     
        }
    
        private final AgentBankProperty _agentBankProperty = new AgentBankProperty();
    
    
        private AgentClass()
        {
            super("agent",
                 new byte[16]);

            setProperties( Arrays.asList( new QMFProperty[] { _connectionRefProperty, _labelProperty, _registeredToProperty, _systemIdProperty, _brokerBankProperty, _agentBankProperty } ) );
            setStatistics( Arrays.asList( new QMFStatistic[] {  } ) );
            setMethods( Arrays.asList( new QMFMethod[] {  } ) );
        }
        
        public AgentObject newInstance(final AgentDelegate delegate)
        {
            return new AgentObject(delegate);
        }
        
    }
    
    private final AgentClass _agentClass = new AgentClass();
    
    public interface AgentDelegate extends QMFObject.Delegate
    {
        ConnectionObject getConnectionRef();
        String getLabel();
        BrokerObject getRegisteredTo();
        UUID getSystemId();
        Long getBrokerBank();
        Long getAgentBank();
    }
    
    public final class AgentObject extends QMFObject<AgentClass, AgentDelegate>
    {
        protected AgentObject(AgentDelegate delegate)
        {
           super(delegate);
        }
        
        public AgentClass getQMFClass()
        {
            return _agentClass;
        }
        
        public QMFCommand asConfigInfoCmd(long sampleTime) 
        {
            return new QMFAgentConfigInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asInstrumentInfoCmd(long sampleTime) 
        {
           return new QMFAgentInstrumentInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asGetQueryResponseCmd(QMFGetQueryCommand queryCommand, long sampleTime) 
        {
            return new QMFAgentGetQueryResponseCommand(this,queryCommand,sampleTime);
        }
    
    
        
        
            public ConnectionObject getConnectionRef()
            {
                return getDelegate().getConnectionRef();
            }
            
            
        
            public String getLabel()
            {
                return getDelegate().getLabel();
            }
            
            
        
            public BrokerObject getRegisteredTo()
            {
                return getDelegate().getRegisteredTo();
            }
            
            
        
            public UUID getSystemId()
            {
                return getDelegate().getSystemId();
            }
            
            
        
            public Long getBrokerBank()
            {
                return getDelegate().getBrokerBank();
            }
            
            
        
            public Long getAgentBank()
            {
                return getDelegate().getAgentBank();
            }
            
            
    }

    public final class QMFAgentConfigInfoCommand extends QMFConfigInfoCommand<AgentObject>
    {
        
        protected QMFAgentConfigInfoCommand(AgentObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeBin128( getObject().getConnectionRef().getId() );
                    
            encoder.writeStr8( getObject().getLabel() );
                    
            encoder.writeBin128( getObject().getRegisteredTo().getId() );
                    
            encoder.writeUuid( getObject().getSystemId() );
                    
            encoder.writeUint32( getObject().getBrokerBank() );
                    
            encoder.writeUint32( getObject().getAgentBank() );
                    
        }
    }
    
    public final class QMFAgentInstrumentInfoCommand extends QMFInstrumentInfoCommand<AgentObject>
    {
    
        protected QMFAgentInstrumentInfoCommand(AgentObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
        }
    }
    
    public final class QMFAgentGetQueryResponseCommand extends QMFGetQueryResponseCommand<AgentObject>
    {
    
        protected QMFAgentGetQueryResponseCommand(AgentObject object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(object, cmd, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeBin128( getObject().getConnectionRef().getId() );
                    
            encoder.writeStr8( getObject().getLabel() );
                    
            encoder.writeBin128( getObject().getRegisteredTo().getId() );
                    
            encoder.writeUuid( getObject().getSystemId() );
                    
            encoder.writeUint32( getObject().getBrokerBank() );
                    
            encoder.writeUint32( getObject().getAgentBank() );
                    
        }
    }
    
    
    


    
    public class VhostClass extends QMFObjectClass<VhostObject, VhostDelegate>
    {
        
        public class BrokerRefProperty extends QMFProperty
        {
            
            private BrokerRefProperty()
            {
                super( "brokerRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

                setReferencedClass("broker");

            }     
        }
    
        private final BrokerRefProperty _brokerRefProperty = new BrokerRefProperty();
    
        public class NameProperty extends QMFProperty
        {
            
            private NameProperty()
            {
                super( "name",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

            }     
        }
    
        private final NameProperty _nameProperty = new NameProperty();
    
        public class FederationTagProperty extends QMFProperty
        {
            
            private FederationTagProperty()
            {
                super( "federationTag",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

            }     
        }
    
        private final FederationTagProperty _federationTagProperty = new FederationTagProperty();
    
    
        private VhostClass()
        {
            super("vhost",
                 new byte[16]);

            setProperties( Arrays.asList( new QMFProperty[] { _brokerRefProperty, _nameProperty, _federationTagProperty } ) );
            setStatistics( Arrays.asList( new QMFStatistic[] {  } ) );
            setMethods( Arrays.asList( new QMFMethod[] {  } ) );
        }
        
        public VhostObject newInstance(final VhostDelegate delegate)
        {
            return new VhostObject(delegate);
        }
        
    }
    
    private final VhostClass _vhostClass = new VhostClass();
    
    public interface VhostDelegate extends QMFObject.Delegate
    {
        BrokerObject getBrokerRef();
        String getName();
        String getFederationTag();
    }
    
    public final class VhostObject extends QMFObject<VhostClass, VhostDelegate>
    {
        protected VhostObject(VhostDelegate delegate)
        {
           super(delegate);
        }
        
        public VhostClass getQMFClass()
        {
            return _vhostClass;
        }
        
        public QMFCommand asConfigInfoCmd(long sampleTime) 
        {
            return new QMFVhostConfigInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asInstrumentInfoCmd(long sampleTime) 
        {
           return new QMFVhostInstrumentInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asGetQueryResponseCmd(QMFGetQueryCommand queryCommand, long sampleTime) 
        {
            return new QMFVhostGetQueryResponseCommand(this,queryCommand,sampleTime);
        }
    
    
        
        
            public BrokerObject getBrokerRef()
            {
                return getDelegate().getBrokerRef();
            }
            
            
        
            public String getName()
            {
                return getDelegate().getName();
            }
            
            
        
            public String getFederationTag()
            {
                return getDelegate().getFederationTag();
            }
            
            
    }

    public final class QMFVhostConfigInfoCommand extends QMFConfigInfoCommand<VhostObject>
    {
        
        protected QMFVhostConfigInfoCommand(VhostObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeBin128( getObject().getBrokerRef().getId() );
                    
            encoder.writeStr8( getObject().getName() );
                    
            encoder.writeStr8( getObject().getFederationTag() );
                    
        }
    }
    
    public final class QMFVhostInstrumentInfoCommand extends QMFInstrumentInfoCommand<VhostObject>
    {
    
        protected QMFVhostInstrumentInfoCommand(VhostObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
        }
    }
    
    public final class QMFVhostGetQueryResponseCommand extends QMFGetQueryResponseCommand<VhostObject>
    {
    
        protected QMFVhostGetQueryResponseCommand(VhostObject object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(object, cmd, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeBin128( getObject().getBrokerRef().getId() );
                    
            encoder.writeStr8( getObject().getName() );
                    
            encoder.writeStr8( getObject().getFederationTag() );
                    
        }
    }
    
    
    


    
    public class QueueClass extends QMFObjectClass<QueueObject, QueueDelegate>
    {
        
        public class VhostRefProperty extends QMFProperty
        {
            
            private VhostRefProperty()
            {
                super( "vhostRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

                setReferencedClass("vhost");

            }     
        }
    
        private final VhostRefProperty _vhostRefProperty = new VhostRefProperty();
    
        public class NameProperty extends QMFProperty
        {
            
            private NameProperty()
            {
                super( "name",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

            }     
        }
    
        private final NameProperty _nameProperty = new NameProperty();
    
        public class DurableProperty extends QMFProperty
        {
            
            private DurableProperty()
            {
                super( "durable",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final DurableProperty _durableProperty = new DurableProperty();
    
        public class AutoDeleteProperty extends QMFProperty
        {
            
            private AutoDeleteProperty()
            {
                super( "autoDelete",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final AutoDeleteProperty _autoDeleteProperty = new AutoDeleteProperty();
    
        public class ExclusiveProperty extends QMFProperty
        {
            
            private ExclusiveProperty()
            {
                super( "exclusive",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final ExclusiveProperty _exclusiveProperty = new ExclusiveProperty();
    
        public class ArgumentsProperty extends QMFProperty
        {
            
            private ArgumentsProperty()
            {
                super( "arguments",
                       QMFType.MAP,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Arguments supplied in queue.declare");

            }     
        }
    
        private final ArgumentsProperty _argumentsProperty = new ArgumentsProperty();
    
        public class AltExchangeProperty extends QMFProperty
        {
            
            private AltExchangeProperty()
            {
                super( "altExchange",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RO,
                       false,
                       true);

                setReferencedClass("exchange");

            }     
        }
    
        private final AltExchangeProperty _altExchangeProperty = new AltExchangeProperty();
    
        public class MsgTotalEnqueuesStatistic extends QMFStatistic
        {
        
            private MsgTotalEnqueuesStatistic()
            {
                super( "msgTotalEnqueues", QMFType.UINT64, "message", "Total messages enqueued"); 
            }     
        }
        
        private final MsgTotalEnqueuesStatistic _msgTotalEnqueuesStatistic = new MsgTotalEnqueuesStatistic();
    
        public class MsgTotalDequeuesStatistic extends QMFStatistic
        {
        
            private MsgTotalDequeuesStatistic()
            {
                super( "msgTotalDequeues", QMFType.UINT64, "message", "Total messages dequeued"); 
            }     
        }
        
        private final MsgTotalDequeuesStatistic _msgTotalDequeuesStatistic = new MsgTotalDequeuesStatistic();
    
        public class MsgTxnEnqueuesStatistic extends QMFStatistic
        {
        
            private MsgTxnEnqueuesStatistic()
            {
                super( "msgTxnEnqueues", QMFType.UINT64, "message", "Transactional messages enqueued"); 
            }     
        }
        
        private final MsgTxnEnqueuesStatistic _msgTxnEnqueuesStatistic = new MsgTxnEnqueuesStatistic();
    
        public class MsgTxnDequeuesStatistic extends QMFStatistic
        {
        
            private MsgTxnDequeuesStatistic()
            {
                super( "msgTxnDequeues", QMFType.UINT64, "message", "Transactional messages dequeued"); 
            }     
        }
        
        private final MsgTxnDequeuesStatistic _msgTxnDequeuesStatistic = new MsgTxnDequeuesStatistic();
    
        public class MsgPersistEnqueuesStatistic extends QMFStatistic
        {
        
            private MsgPersistEnqueuesStatistic()
            {
                super( "msgPersistEnqueues", QMFType.UINT64, "message", "Persistent messages enqueued"); 
            }     
        }
        
        private final MsgPersistEnqueuesStatistic _msgPersistEnqueuesStatistic = new MsgPersistEnqueuesStatistic();
    
        public class MsgPersistDequeuesStatistic extends QMFStatistic
        {
        
            private MsgPersistDequeuesStatistic()
            {
                super( "msgPersistDequeues", QMFType.UINT64, "message", "Persistent messages dequeued"); 
            }     
        }
        
        private final MsgPersistDequeuesStatistic _msgPersistDequeuesStatistic = new MsgPersistDequeuesStatistic();
    
        public class MsgDepthStatistic extends QMFStatistic
        {
        
            private MsgDepthStatistic()
            {
                super( "msgDepth", QMFType.UINT32, "message", "Current size of queue in messages"); 
            }     
        }
        
        private final MsgDepthStatistic _msgDepthStatistic = new MsgDepthStatistic();
    
        public class ByteDepthStatistic extends QMFStatistic
        {
        
            private ByteDepthStatistic()
            {
                super( "byteDepth", QMFType.UINT32, "octet", "Current size of queue in bytes"); 
            }     
        }
        
        private final ByteDepthStatistic _byteDepthStatistic = new ByteDepthStatistic();
    
        public class ByteTotalEnqueuesStatistic extends QMFStatistic
        {
        
            private ByteTotalEnqueuesStatistic()
            {
                super( "byteTotalEnqueues", QMFType.UINT64, "octet", "Total messages enqueued"); 
            }     
        }
        
        private final ByteTotalEnqueuesStatistic _byteTotalEnqueuesStatistic = new ByteTotalEnqueuesStatistic();
    
        public class ByteTotalDequeuesStatistic extends QMFStatistic
        {
        
            private ByteTotalDequeuesStatistic()
            {
                super( "byteTotalDequeues", QMFType.UINT64, "octet", "Total messages dequeued"); 
            }     
        }
        
        private final ByteTotalDequeuesStatistic _byteTotalDequeuesStatistic = new ByteTotalDequeuesStatistic();
    
        public class ByteTxnEnqueuesStatistic extends QMFStatistic
        {
        
            private ByteTxnEnqueuesStatistic()
            {
                super( "byteTxnEnqueues", QMFType.UINT64, "octet", "Transactional messages enqueued"); 
            }     
        }
        
        private final ByteTxnEnqueuesStatistic _byteTxnEnqueuesStatistic = new ByteTxnEnqueuesStatistic();
    
        public class ByteTxnDequeuesStatistic extends QMFStatistic
        {
        
            private ByteTxnDequeuesStatistic()
            {
                super( "byteTxnDequeues", QMFType.UINT64, "octet", "Transactional messages dequeued"); 
            }     
        }
        
        private final ByteTxnDequeuesStatistic _byteTxnDequeuesStatistic = new ByteTxnDequeuesStatistic();
    
        public class BytePersistEnqueuesStatistic extends QMFStatistic
        {
        
            private BytePersistEnqueuesStatistic()
            {
                super( "bytePersistEnqueues", QMFType.UINT64, "octet", "Persistent messages enqueued"); 
            }     
        }
        
        private final BytePersistEnqueuesStatistic _bytePersistEnqueuesStatistic = new BytePersistEnqueuesStatistic();
    
        public class BytePersistDequeuesStatistic extends QMFStatistic
        {
        
            private BytePersistDequeuesStatistic()
            {
                super( "bytePersistDequeues", QMFType.UINT64, "octet", "Persistent messages dequeued"); 
            }     
        }
        
        private final BytePersistDequeuesStatistic _bytePersistDequeuesStatistic = new BytePersistDequeuesStatistic();
    
        public class ConsumerCountStatistic extends QMFStatistic
        {
        
            private ConsumerCountStatistic()
            {
                super( "consumerCount", QMFType.UINT32, "consumer", "Current consumers on queue"); 
            }     
        }
        
        private final ConsumerCountStatistic _consumerCountStatistic = new ConsumerCountStatistic();
    
        public class ConsumerCountHighStatistic extends QMFStatistic
        {
        
            private ConsumerCountHighStatistic()
            {
                super( "consumerCountHigh", QMFType.UINT32, "consumer", "Current consumers on queue (High)"); 
            }     
        }
        
        private final ConsumerCountHighStatistic _consumerCountHighStatistic = new ConsumerCountHighStatistic();
    
        public class ConsumerCountLowStatistic extends QMFStatistic
        {
        
            private ConsumerCountLowStatistic()
            {
                super( "consumerCountLow", QMFType.UINT32, "consumer", "Current consumers on queue (Low)"); 
            }     
        }
        
        private final ConsumerCountLowStatistic _consumerCountLowStatistic = new ConsumerCountLowStatistic();
    
        public class BindingCountStatistic extends QMFStatistic
        {
        
            private BindingCountStatistic()
            {
                super( "bindingCount", QMFType.UINT32, "binding", "Current bindings"); 
            }     
        }
        
        private final BindingCountStatistic _bindingCountStatistic = new BindingCountStatistic();
    
        public class BindingCountHighStatistic extends QMFStatistic
        {
        
            private BindingCountHighStatistic()
            {
                super( "bindingCountHigh", QMFType.UINT32, "binding", "Current bindings (High)"); 
            }     
        }
        
        private final BindingCountHighStatistic _bindingCountHighStatistic = new BindingCountHighStatistic();
    
        public class BindingCountLowStatistic extends QMFStatistic
        {
        
            private BindingCountLowStatistic()
            {
                super( "bindingCountLow", QMFType.UINT32, "binding", "Current bindings (Low)"); 
            }     
        }
        
        private final BindingCountLowStatistic _bindingCountLowStatistic = new BindingCountLowStatistic();
    
        public class UnackedMessagesStatistic extends QMFStatistic
        {
        
            private UnackedMessagesStatistic()
            {
                super( "unackedMessages", QMFType.UINT32, "message", "Messages consumed but not yet acked"); 
            }     
        }
        
        private final UnackedMessagesStatistic _unackedMessagesStatistic = new UnackedMessagesStatistic();
    
        public class UnackedMessagesHighStatistic extends QMFStatistic
        {
        
            private UnackedMessagesHighStatistic()
            {
                super( "unackedMessagesHigh", QMFType.UINT32, "message", "Messages consumed but not yet acked (High)"); 
            }     
        }
        
        private final UnackedMessagesHighStatistic _unackedMessagesHighStatistic = new UnackedMessagesHighStatistic();
    
        public class UnackedMessagesLowStatistic extends QMFStatistic
        {
        
            private UnackedMessagesLowStatistic()
            {
                super( "unackedMessagesLow", QMFType.UINT32, "message", "Messages consumed but not yet acked (Low)"); 
            }     
        }
        
        private final UnackedMessagesLowStatistic _unackedMessagesLowStatistic = new UnackedMessagesLowStatistic();
    
        public class MessageLatencySamplesStatistic extends QMFStatistic
        {
        
            private MessageLatencySamplesStatistic()
            {
                super( "messageLatencySamples", QMFType.DELTATIME, "nanosecond", "Broker latency through this queue (Samples)"); 
            }     
        }
        
        private final MessageLatencySamplesStatistic _messageLatencySamplesStatistic = new MessageLatencySamplesStatistic();
    
        public class MessageLatencyMaxStatistic extends QMFStatistic
        {
        
            private MessageLatencyMaxStatistic()
            {
                super( "messageLatencyMax", QMFType.DELTATIME, "nanosecond", "Broker latency through this queue (Max)"); 
            }     
        }
        
        private final MessageLatencyMaxStatistic _messageLatencyMaxStatistic = new MessageLatencyMaxStatistic();
    
        public class MessageLatencyMinStatistic extends QMFStatistic
        {
        
            private MessageLatencyMinStatistic()
            {
                super( "messageLatencyMin", QMFType.DELTATIME, "nanosecond", "Broker latency through this queue (Min)"); 
            }     
        }
        
        private final MessageLatencyMinStatistic _messageLatencyMinStatistic = new MessageLatencyMinStatistic();
    
        public class MessageLatencyAverageStatistic extends QMFStatistic
        {
        
            private MessageLatencyAverageStatistic()
            {
                super( "messageLatencyAverage", QMFType.DELTATIME, "nanosecond", "Broker latency through this queue (Average)"); 
            }     
        }
        
        private final MessageLatencyAverageStatistic _messageLatencyAverageStatistic = new MessageLatencyAverageStatistic();
    
        public class FlowStoppedStatistic extends QMFStatistic
        {
        
            private FlowStoppedStatistic()
            {
                super( "flowStopped", QMFType.BOOLEAN, null, "Flow control active."); 
            }     
        }
        
        private final FlowStoppedStatistic _flowStoppedStatistic = new FlowStoppedStatistic();
    
        public class FlowStoppedCountStatistic extends QMFStatistic
        {
        
            private FlowStoppedCountStatistic()
            {
                super( "flowStoppedCount", QMFType.UINT32, null, "Number of times flow control was activated for this queue"); 
            }     
        }
        
        private final FlowStoppedCountStatistic _flowStoppedCountStatistic = new FlowStoppedCountStatistic();
    
        public class PurgeMethod extends QMFMethod<QueueObject>
        {
            private PurgeMethod()
            {
                super( "purge", "Discard all or some messages on a queue");
        
                
                QMFMethod.Argument request = new QMFMethod.Argument("request", QMFType.UINT32);
                request.setDescription("0 for all messages or n>0 for n messages"); 
                request.setDirection(QMFMethod.Direction.I);                  
                addArgument( request );
    
        
            }
            
            
            public PurgeMethodInvocation parse(BBDecoder decoder)
            {
                Long request = decoder.readUint32();
    
                return new PurgeMethodInvocation( request);
            }
        }
        
        private final PurgeMethod _purgeMethod = new PurgeMethod();
        
        private class PurgeMethodInvocation implements QMFMethodInvocation<QueueObject>
        {
            
            private final Long _request;
    
        
            private PurgeMethodInvocation(Long request)
            {
                
                _request = request;
    
            }
        
            public QMFMethodResponseCommand execute(QueueObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.purge( new PurgeMethodResponseCommandFactory(cmd),  _request );
            }
        }
        
        public final class PurgeMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private PurgeMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public PurgeMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new PurgeMethodResponseCommand(_requestCmd, status, null);
            }
            
            public PurgeMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new PurgeMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public PurgeMethodResponseCommand createResponseCommand(  )
            {
                return new PurgeMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class PurgeMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private PurgeMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private PurgeMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
        public class RerouteMethod extends QMFMethod<QueueObject>
        {
            private RerouteMethod()
            {
                super( "reroute", "Remove all or some messages on this queue and route them to an exchange");
        
                
                QMFMethod.Argument request = new QMFMethod.Argument("request", QMFType.UINT32);
                request.setDescription("0 for all messages or n>0 for n messages"); 
                request.setDirection(QMFMethod.Direction.I);                  
                addArgument( request );
    
                QMFMethod.Argument useAltExchange = new QMFMethod.Argument("useAltExchange", QMFType.BOOLEAN);
                useAltExchange.setDescription("Iff true, use the queue's configured alternate exchange; iff false, use exchange named in the 'exchange' argument"); 
                useAltExchange.setDirection(QMFMethod.Direction.I);                  
                addArgument( useAltExchange );
    
                QMFMethod.Argument exchange = new QMFMethod.Argument("exchange", QMFType.STR8);
                exchange.setDescription("Name of the exchange to route the messages through"); 
                exchange.setDirection(QMFMethod.Direction.I);                  
                addArgument( exchange );
    
        
            }
            
            
            public RerouteMethodInvocation parse(BBDecoder decoder)
            {
                Long request = decoder.readUint32();
    Boolean useAltExchange = decoder.readInt8() != 0;
    String exchange = decoder.readStr8();
    
                return new RerouteMethodInvocation( request,  useAltExchange,  exchange);
            }
        }
        
        private final RerouteMethod _rerouteMethod = new RerouteMethod();
        
        private class RerouteMethodInvocation implements QMFMethodInvocation<QueueObject>
        {
            
            private final Long _request;
    
            private final Boolean _useAltExchange;
    
            private final String _exchange;
    
        
            private RerouteMethodInvocation(Long request, Boolean useAltExchange, String exchange)
            {
                
                _request = request;
    
                _useAltExchange = useAltExchange;
    
                _exchange = exchange;
    
            }
        
            public QMFMethodResponseCommand execute(QueueObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.reroute( new RerouteMethodResponseCommandFactory(cmd),  _request,  _useAltExchange,  _exchange );
            }
        }
        
        public final class RerouteMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private RerouteMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public RerouteMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new RerouteMethodResponseCommand(_requestCmd, status, null);
            }
            
            public RerouteMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new RerouteMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public RerouteMethodResponseCommand createResponseCommand(  )
            {
                return new RerouteMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class RerouteMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private RerouteMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private RerouteMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
    
        private QueueClass()
        {
            super("queue",
                 new byte[16]);

            setProperties( Arrays.asList( new QMFProperty[] { _vhostRefProperty, _nameProperty, _durableProperty, _autoDeleteProperty, _exclusiveProperty, _argumentsProperty, _altExchangeProperty } ) );
            setStatistics( Arrays.asList( new QMFStatistic[] { _msgTotalEnqueuesStatistic, _msgTotalDequeuesStatistic, _msgTxnEnqueuesStatistic, _msgTxnDequeuesStatistic, _msgPersistEnqueuesStatistic, _msgPersistDequeuesStatistic, _msgDepthStatistic, _byteDepthStatistic, _byteTotalEnqueuesStatistic, _byteTotalDequeuesStatistic, _byteTxnEnqueuesStatistic, _byteTxnDequeuesStatistic, _bytePersistEnqueuesStatistic, _bytePersistDequeuesStatistic, _consumerCountStatistic, _consumerCountHighStatistic, _consumerCountLowStatistic, _bindingCountStatistic, _bindingCountHighStatistic, _bindingCountLowStatistic, _unackedMessagesStatistic, _unackedMessagesHighStatistic, _unackedMessagesLowStatistic, _messageLatencySamplesStatistic, _messageLatencyMaxStatistic, _messageLatencyMinStatistic, _messageLatencyAverageStatistic, _flowStoppedStatistic, _flowStoppedCountStatistic } ) );
            setMethods( Arrays.asList( new QMFMethod[] { _purgeMethod, _rerouteMethod } ) );
        }
        
        public QueueObject newInstance(final QueueDelegate delegate)
        {
            return new QueueObject(delegate);
        }
        
    }
    
    private final QueueClass _queueClass = new QueueClass();
    
    public interface QueueDelegate extends QMFObject.Delegate
    {
        VhostObject getVhostRef();
        String getName();
        Boolean getDurable();
        Boolean getAutoDelete();
        Boolean getExclusive();
        Map getArguments();
        ExchangeObject getAltExchange();
        Long getMsgTotalEnqueues();
        Long getMsgTotalDequeues();
        Long getMsgTxnEnqueues();
        Long getMsgTxnDequeues();
        Long getMsgPersistEnqueues();
        Long getMsgPersistDequeues();
        Long getMsgDepth();
        Long getByteDepth();
        Long getByteTotalEnqueues();
        Long getByteTotalDequeues();
        Long getByteTxnEnqueues();
        Long getByteTxnDequeues();
        Long getBytePersistEnqueues();
        Long getBytePersistDequeues();
        Long getConsumerCount();
        Long getConsumerCountHigh();
        Long getConsumerCountLow();
        Long getBindingCount();
        Long getBindingCountHigh();
        Long getBindingCountLow();
        Long getUnackedMessages();
        Long getUnackedMessagesHigh();
        Long getUnackedMessagesLow();
        Long getMessageLatencySamples();
        Long getMessageLatencyMin();
        Long getMessageLatencyMax();
        Long getMessageLatencyAverage();
        Boolean getFlowStopped();
        Long getFlowStoppedCount();
        QueueClass.PurgeMethodResponseCommand purge(QueueClass.PurgeMethodResponseCommandFactory factory, Long request);
        QueueClass.RerouteMethodResponseCommand reroute(QueueClass.RerouteMethodResponseCommandFactory factory, Long request, Boolean useAltExchange, String exchange);
    }
    
    public final class QueueObject extends QMFObject<QueueClass, QueueDelegate>
    {
        protected QueueObject(QueueDelegate delegate)
        {
           super(delegate);
        }
        
        public QueueClass getQMFClass()
        {
            return _queueClass;
        }
        
        public QMFCommand asConfigInfoCmd(long sampleTime) 
        {
            return new QMFQueueConfigInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asInstrumentInfoCmd(long sampleTime) 
        {
           return new QMFQueueInstrumentInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asGetQueryResponseCmd(QMFGetQueryCommand queryCommand, long sampleTime) 
        {
            return new QMFQueueGetQueryResponseCommand(this,queryCommand,sampleTime);
        }
    
    
        
        public QueueClass.PurgeMethodResponseCommand purge(QueueClass.PurgeMethodResponseCommandFactory factory, Long request)
        {
            return getDelegate().purge(factory,  request );
        }
        
        public QueueClass.RerouteMethodResponseCommand reroute(QueueClass.RerouteMethodResponseCommandFactory factory, Long request, Boolean useAltExchange, String exchange)
        {
            return getDelegate().reroute(factory,  request,  useAltExchange,  exchange );
        }
        
        
            public VhostObject getVhostRef()
            {
                return getDelegate().getVhostRef();
            }
            
            
        
            public String getName()
            {
                return getDelegate().getName();
            }
            
            
        
            public Boolean getDurable()
            {
                return getDelegate().getDurable();
            }
            
            
        
            public Boolean getAutoDelete()
            {
                return getDelegate().getAutoDelete();
            }
            
            
        
            public Boolean getExclusive()
            {
                return getDelegate().getExclusive();
            }
            
            
        
            public Map getArguments()
            {
                return getDelegate().getArguments();
            }
            
            
        
            public ExchangeObject getAltExchange()
            {
                return getDelegate().getAltExchange();
            }
            
            
        
            public Long getMsgTotalEnqueues()
            {
                return getDelegate().getMsgTotalEnqueues();
            }
            
            
        
            public Long getMsgTotalDequeues()
            {
                return getDelegate().getMsgTotalDequeues();
            }
            
            
        
            public Long getMsgTxnEnqueues()
            {
                return getDelegate().getMsgTxnEnqueues();
            }
            
            
        
            public Long getMsgTxnDequeues()
            {
                return getDelegate().getMsgTxnDequeues();
            }
            
            
        
            public Long getMsgPersistEnqueues()
            {
                return getDelegate().getMsgPersistEnqueues();
            }
            
            
        
            public Long getMsgPersistDequeues()
            {
                return getDelegate().getMsgPersistDequeues();
            }
            
            
        
            public Long getMsgDepth()
            {
                return getDelegate().getMsgDepth();
            }
            
            
        
            public Long getByteDepth()
            {
                return getDelegate().getByteDepth();
            }
            
            
        
            public Long getByteTotalEnqueues()
            {
                return getDelegate().getByteTotalEnqueues();
            }
            
            
        
            public Long getByteTotalDequeues()
            {
                return getDelegate().getByteTotalDequeues();
            }
            
            
        
            public Long getByteTxnEnqueues()
            {
                return getDelegate().getByteTxnEnqueues();
            }
            
            
        
            public Long getByteTxnDequeues()
            {
                return getDelegate().getByteTxnDequeues();
            }
            
            
        
            public Long getBytePersistEnqueues()
            {
                return getDelegate().getBytePersistEnqueues();
            }
            
            
        
            public Long getBytePersistDequeues()
            {
                return getDelegate().getBytePersistDequeues();
            }
            
            
        
                public Long getConsumerCount()
                {
                    return getDelegate().getConsumerCount();
                }
                
                public Long getConsumerCountHigh()
                {
                    return getDelegate().getConsumerCountHigh();
                }
                
                public Long getConsumerCountLow()
                {
                    return getDelegate().getConsumerCountLow();
                }
                
            
        
                public Long getBindingCount()
                {
                    return getDelegate().getBindingCount();
                }
                
                public Long getBindingCountHigh()
                {
                    return getDelegate().getBindingCountHigh();
                }
                
                public Long getBindingCountLow()
                {
                    return getDelegate().getBindingCountLow();
                }
                
            
        
                public Long getUnackedMessages()
                {
                    return getDelegate().getUnackedMessages();
                }
                
                public Long getUnackedMessagesHigh()
                {
                    return getDelegate().getUnackedMessagesHigh();
                }
                
                public Long getUnackedMessagesLow()
                {
                    return getDelegate().getUnackedMessagesLow();
                }
                
            
        
                public Long getMessageLatencySamples()
                {
                    return getDelegate().getMessageLatencySamples();
                }
                
                public Long getMessageLatencyMin()
                {
                    return getDelegate().getMessageLatencyMin();
                }
                
                public Long getMessageLatencyMax()
                {
                    return getDelegate().getMessageLatencyMax();
                }
                
                public Long getMessageLatencyAverage()
                {
                    return getDelegate().getMessageLatencyAverage();
                }
                
            
        
            public Boolean getFlowStopped()
            {
                return getDelegate().getFlowStopped();
            }
            
            
        
            public Long getFlowStoppedCount()
            {
                return getDelegate().getFlowStoppedCount();
            }
            
            
    }

    public final class QMFQueueConfigInfoCommand extends QMFConfigInfoCommand<QueueObject>
    {
        
        protected QMFQueueConfigInfoCommand(QueueObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            byte presence0 = (byte) 0;              
        
            if(  getObject().getAltExchange() != null )
            {
                presence0 |= (1 <<0);
            }
        
            encoder.writeUint8( presence0 );
        
            encoder.writeBin128( getObject().getVhostRef().getId() );
                    
            encoder.writeStr8( getObject().getName() );
                    
            encoder.writeInt8( getObject().getDurable() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getAutoDelete() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getExclusive() ? (byte) -1 : (byte) 0);
                    
            encoder.writeMap( getObject().getArguments() );
                    
            if(getObject().getAltExchange() != null)
            {
                encoder.writeBin128( getObject().getAltExchange().getId() );
            }
            
        }
    }
    
    public final class QMFQueueInstrumentInfoCommand extends QMFInstrumentInfoCommand<QueueObject>
    {
    
        protected QMFQueueInstrumentInfoCommand(QueueObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeUint64( getObject().getMsgTotalEnqueues() );
                    
            encoder.writeUint64( getObject().getMsgTotalDequeues() );
                    
            encoder.writeUint64( getObject().getMsgTxnEnqueues() );
                    
            encoder.writeUint64( getObject().getMsgTxnDequeues() );
                    
            encoder.writeUint64( getObject().getMsgPersistEnqueues() );
                    
            encoder.writeUint64( getObject().getMsgPersistDequeues() );
                    
            encoder.writeUint32( getObject().getMsgDepth() );
                    
            encoder.writeUint32( getObject().getByteDepth() );
                    
            encoder.writeUint64( getObject().getByteTotalEnqueues() );
                    
            encoder.writeUint64( getObject().getByteTotalDequeues() );
                    
            encoder.writeUint64( getObject().getByteTxnEnqueues() );
                    
            encoder.writeUint64( getObject().getByteTxnDequeues() );
                    
            encoder.writeUint64( getObject().getBytePersistEnqueues() );
                    
            encoder.writeUint64( getObject().getBytePersistDequeues() );
                    
            encoder.writeUint32( getObject().getConsumerCount() );
            encoder.writeUint32( getObject().getConsumerCountHigh() );
            encoder.writeUint32( getObject().getConsumerCountLow() );
                    
            encoder.writeUint32( getObject().getBindingCount() );
            encoder.writeUint32( getObject().getBindingCountHigh() );
            encoder.writeUint32( getObject().getBindingCountLow() );
                    
            encoder.writeUint32( getObject().getUnackedMessages() );
            encoder.writeUint32( getObject().getUnackedMessagesHigh() );
            encoder.writeUint32( getObject().getUnackedMessagesLow() );
                                
            encoder.writeUint64( getObject().getMessageLatencySamples() );
            encoder.writeUint64( getObject().getMessageLatencyMin() );
            encoder.writeUint64( getObject().getMessageLatencyMax() );
            encoder.writeUint64( getObject().getMessageLatencyAverage() );                        
                    
            encoder.writeInt8( getObject().getFlowStopped() ? (byte) -1 : (byte) 0);
                    
            encoder.writeUint32( getObject().getFlowStoppedCount() );
                    
        }
    }
    
    public final class QMFQueueGetQueryResponseCommand extends QMFGetQueryResponseCommand<QueueObject>
    {
    
        protected QMFQueueGetQueryResponseCommand(QueueObject object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(object, cmd, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            byte presence0 = (byte) 0;              
        
            if(  getObject().getAltExchange() != null )
            {
                presence0 |= (1 <<0);
            }
        
            encoder.writeUint8( presence0 );
        
            encoder.writeBin128( getObject().getVhostRef().getId() );
                    
            encoder.writeStr8( getObject().getName() );
                    
            encoder.writeInt8( getObject().getDurable() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getAutoDelete() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getExclusive() ? (byte) -1 : (byte) 0);
                    
            encoder.writeMap( getObject().getArguments() );
                    
            if(getObject().getAltExchange() != null)
            {
                encoder.writeBin128( getObject().getAltExchange().getId() );
            }
            
            encoder.writeUint64( getObject().getMsgTotalEnqueues() );
                    
            encoder.writeUint64( getObject().getMsgTotalDequeues() );
                    
            encoder.writeUint64( getObject().getMsgTxnEnqueues() );
                    
            encoder.writeUint64( getObject().getMsgTxnDequeues() );
                    
            encoder.writeUint64( getObject().getMsgPersistEnqueues() );
                    
            encoder.writeUint64( getObject().getMsgPersistDequeues() );
                    
            encoder.writeUint32( getObject().getMsgDepth() );
                    
            encoder.writeUint32( getObject().getByteDepth() );
                    
            encoder.writeUint64( getObject().getByteTotalEnqueues() );
                    
            encoder.writeUint64( getObject().getByteTotalDequeues() );
                    
            encoder.writeUint64( getObject().getByteTxnEnqueues() );
                    
            encoder.writeUint64( getObject().getByteTxnDequeues() );
                    
            encoder.writeUint64( getObject().getBytePersistEnqueues() );
                    
            encoder.writeUint64( getObject().getBytePersistDequeues() );
                    
            encoder.writeUint32( getObject().getConsumerCount() );
            encoder.writeUint32( getObject().getConsumerCountHigh() );
            encoder.writeUint32( getObject().getConsumerCountLow() );
                    
            encoder.writeUint32( getObject().getBindingCount() );
            encoder.writeUint32( getObject().getBindingCountHigh() );
            encoder.writeUint32( getObject().getBindingCountLow() );
                    
            encoder.writeUint32( getObject().getUnackedMessages() );
            encoder.writeUint32( getObject().getUnackedMessagesHigh() );
            encoder.writeUint32( getObject().getUnackedMessagesLow() );
                                
            encoder.writeUint64( getObject().getMessageLatencySamples() );
            encoder.writeUint64( getObject().getMessageLatencyMin() );
            encoder.writeUint64( getObject().getMessageLatencyMax() );
            encoder.writeUint64( getObject().getMessageLatencyAverage() );                        
                    
            encoder.writeInt8( getObject().getFlowStopped() ? (byte) -1 : (byte) 0);
                    
            encoder.writeUint32( getObject().getFlowStoppedCount() );
                    
        }
    }
    
    
    


    
    public class ExchangeClass extends QMFObjectClass<ExchangeObject, ExchangeDelegate>
    {
        
        public class VhostRefProperty extends QMFProperty
        {
            
            private VhostRefProperty()
            {
                super( "vhostRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

                setReferencedClass("vhost");

            }     
        }
    
        private final VhostRefProperty _vhostRefProperty = new VhostRefProperty();
    
        public class NameProperty extends QMFProperty
        {
            
            private NameProperty()
            {
                super( "name",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

            }     
        }
    
        private final NameProperty _nameProperty = new NameProperty();
    
        public class TypeProperty extends QMFProperty
        {
            
            private TypeProperty()
            {
                super( "type",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

            }     
        }
    
        private final TypeProperty _typeProperty = new TypeProperty();
    
        public class DurableProperty extends QMFProperty
        {
            
            private DurableProperty()
            {
                super( "durable",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

            }     
        }
    
        private final DurableProperty _durableProperty = new DurableProperty();
    
        public class AutoDeleteProperty extends QMFProperty
        {
            
            private AutoDeleteProperty()
            {
                super( "autoDelete",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

            }     
        }
    
        private final AutoDeleteProperty _autoDeleteProperty = new AutoDeleteProperty();
    
        public class AltExchangeProperty extends QMFProperty
        {
            
            private AltExchangeProperty()
            {
                super( "altExchange",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RO,
                       false,
                       true);

                setReferencedClass("exchange");

            }     
        }
    
        private final AltExchangeProperty _altExchangeProperty = new AltExchangeProperty();
    
        public class ArgumentsProperty extends QMFProperty
        {
            
            private ArgumentsProperty()
            {
                super( "arguments",
                       QMFType.MAP,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Arguments supplied in exchange.declare");

            }     
        }
    
        private final ArgumentsProperty _argumentsProperty = new ArgumentsProperty();
    
        public class ProducerCountStatistic extends QMFStatistic
        {
        
            private ProducerCountStatistic()
            {
                super( "producerCount", QMFType.UINT32, null, "Current producers on exchange"); 
            }     
        }
        
        private final ProducerCountStatistic _producerCountStatistic = new ProducerCountStatistic();
    
        public class ProducerCountHighStatistic extends QMFStatistic
        {
        
            private ProducerCountHighStatistic()
            {
                super( "producerCountHigh", QMFType.UINT32, null, "Current producers on exchange (High)"); 
            }     
        }
        
        private final ProducerCountHighStatistic _producerCountHighStatistic = new ProducerCountHighStatistic();
    
        public class ProducerCountLowStatistic extends QMFStatistic
        {
        
            private ProducerCountLowStatistic()
            {
                super( "producerCountLow", QMFType.UINT32, null, "Current producers on exchange (Low)"); 
            }     
        }
        
        private final ProducerCountLowStatistic _producerCountLowStatistic = new ProducerCountLowStatistic();
    
        public class BindingCountStatistic extends QMFStatistic
        {
        
            private BindingCountStatistic()
            {
                super( "bindingCount", QMFType.UINT32, null, "Current bindings"); 
            }     
        }
        
        private final BindingCountStatistic _bindingCountStatistic = new BindingCountStatistic();
    
        public class BindingCountHighStatistic extends QMFStatistic
        {
        
            private BindingCountHighStatistic()
            {
                super( "bindingCountHigh", QMFType.UINT32, null, "Current bindings (High)"); 
            }     
        }
        
        private final BindingCountHighStatistic _bindingCountHighStatistic = new BindingCountHighStatistic();
    
        public class BindingCountLowStatistic extends QMFStatistic
        {
        
            private BindingCountLowStatistic()
            {
                super( "bindingCountLow", QMFType.UINT32, null, "Current bindings (Low)"); 
            }     
        }
        
        private final BindingCountLowStatistic _bindingCountLowStatistic = new BindingCountLowStatistic();
    
        public class MsgReceivesStatistic extends QMFStatistic
        {
        
            private MsgReceivesStatistic()
            {
                super( "msgReceives", QMFType.UINT64, null, "Total messages received"); 
            }     
        }
        
        private final MsgReceivesStatistic _msgReceivesStatistic = new MsgReceivesStatistic();
    
        public class MsgDropsStatistic extends QMFStatistic
        {
        
            private MsgDropsStatistic()
            {
                super( "msgDrops", QMFType.UINT64, null, "Total messages dropped (no matching key)"); 
            }     
        }
        
        private final MsgDropsStatistic _msgDropsStatistic = new MsgDropsStatistic();
    
        public class MsgRoutesStatistic extends QMFStatistic
        {
        
            private MsgRoutesStatistic()
            {
                super( "msgRoutes", QMFType.UINT64, null, "Total routed messages"); 
            }     
        }
        
        private final MsgRoutesStatistic _msgRoutesStatistic = new MsgRoutesStatistic();
    
        public class ByteReceivesStatistic extends QMFStatistic
        {
        
            private ByteReceivesStatistic()
            {
                super( "byteReceives", QMFType.UINT64, null, "Total bytes received"); 
            }     
        }
        
        private final ByteReceivesStatistic _byteReceivesStatistic = new ByteReceivesStatistic();
    
        public class ByteDropsStatistic extends QMFStatistic
        {
        
            private ByteDropsStatistic()
            {
                super( "byteDrops", QMFType.UINT64, null, "Total bytes dropped (no matching key)"); 
            }     
        }
        
        private final ByteDropsStatistic _byteDropsStatistic = new ByteDropsStatistic();
    
        public class ByteRoutesStatistic extends QMFStatistic
        {
        
            private ByteRoutesStatistic()
            {
                super( "byteRoutes", QMFType.UINT64, null, "Total routed bytes"); 
            }     
        }
        
        private final ByteRoutesStatistic _byteRoutesStatistic = new ByteRoutesStatistic();
    
    
        private ExchangeClass()
        {
            super("exchange",
                 new byte[16]);

            setProperties( Arrays.asList( new QMFProperty[] { _vhostRefProperty, _nameProperty, _typeProperty, _durableProperty, _autoDeleteProperty, _altExchangeProperty, _argumentsProperty } ) );
            setStatistics( Arrays.asList( new QMFStatistic[] { _producerCountStatistic, _producerCountHighStatistic, _producerCountLowStatistic, _bindingCountStatistic, _bindingCountHighStatistic, _bindingCountLowStatistic, _msgReceivesStatistic, _msgDropsStatistic, _msgRoutesStatistic, _byteReceivesStatistic, _byteDropsStatistic, _byteRoutesStatistic } ) );
            setMethods( Arrays.asList( new QMFMethod[] {  } ) );
        }
        
        public ExchangeObject newInstance(final ExchangeDelegate delegate)
        {
            return new ExchangeObject(delegate);
        }
        
    }
    
    private final ExchangeClass _exchangeClass = new ExchangeClass();
    
    public interface ExchangeDelegate extends QMFObject.Delegate
    {
        VhostObject getVhostRef();
        String getName();
        String getType();
        Boolean getDurable();
        Boolean getAutoDelete();
        ExchangeObject getAltExchange();
        Map getArguments();
        Long getProducerCount();
        Long getProducerCountHigh();
        Long getProducerCountLow();
        Long getBindingCount();
        Long getBindingCountHigh();
        Long getBindingCountLow();
        Long getMsgReceives();
        Long getMsgDrops();
        Long getMsgRoutes();
        Long getByteReceives();
        Long getByteDrops();
        Long getByteRoutes();
    }
    
    public final class ExchangeObject extends QMFObject<ExchangeClass, ExchangeDelegate>
    {
        protected ExchangeObject(ExchangeDelegate delegate)
        {
           super(delegate);
        }
        
        public ExchangeClass getQMFClass()
        {
            return _exchangeClass;
        }
        
        public QMFCommand asConfigInfoCmd(long sampleTime) 
        {
            return new QMFExchangeConfigInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asInstrumentInfoCmd(long sampleTime) 
        {
           return new QMFExchangeInstrumentInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asGetQueryResponseCmd(QMFGetQueryCommand queryCommand, long sampleTime) 
        {
            return new QMFExchangeGetQueryResponseCommand(this,queryCommand,sampleTime);
        }
    
    
        
        
            public VhostObject getVhostRef()
            {
                return getDelegate().getVhostRef();
            }
            
            
        
            public String getName()
            {
                return getDelegate().getName();
            }
            
            
        
            public String getType()
            {
                return getDelegate().getType();
            }
            
            
        
            public Boolean getDurable()
            {
                return getDelegate().getDurable();
            }
            
            
        
            public Boolean getAutoDelete()
            {
                return getDelegate().getAutoDelete();
            }
            
            
        
            public ExchangeObject getAltExchange()
            {
                return getDelegate().getAltExchange();
            }
            
            
        
            public Map getArguments()
            {
                return getDelegate().getArguments();
            }
            
            
        
                public Long getProducerCount()
                {
                    return getDelegate().getProducerCount();
                }
                
                public Long getProducerCountHigh()
                {
                    return getDelegate().getProducerCountHigh();
                }
                
                public Long getProducerCountLow()
                {
                    return getDelegate().getProducerCountLow();
                }
                
            
        
                public Long getBindingCount()
                {
                    return getDelegate().getBindingCount();
                }
                
                public Long getBindingCountHigh()
                {
                    return getDelegate().getBindingCountHigh();
                }
                
                public Long getBindingCountLow()
                {
                    return getDelegate().getBindingCountLow();
                }
                
            
        
            public Long getMsgReceives()
            {
                return getDelegate().getMsgReceives();
            }
            
            
        
            public Long getMsgDrops()
            {
                return getDelegate().getMsgDrops();
            }
            
            
        
            public Long getMsgRoutes()
            {
                return getDelegate().getMsgRoutes();
            }
            
            
        
            public Long getByteReceives()
            {
                return getDelegate().getByteReceives();
            }
            
            
        
            public Long getByteDrops()
            {
                return getDelegate().getByteDrops();
            }
            
            
        
            public Long getByteRoutes()
            {
                return getDelegate().getByteRoutes();
            }
            
            
    }

    public final class QMFExchangeConfigInfoCommand extends QMFConfigInfoCommand<ExchangeObject>
    {
        
        protected QMFExchangeConfigInfoCommand(ExchangeObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            byte presence0 = (byte) 0;              
        
            if(  getObject().getAltExchange() != null )
            {
                presence0 |= (1 <<0);
            }
        
            encoder.writeUint8( presence0 );
        
            encoder.writeBin128( getObject().getVhostRef().getId() );
                    
            encoder.writeStr8( getObject().getName() );
                    
            encoder.writeStr8( getObject().getType() );
                    
            encoder.writeInt8( getObject().getDurable() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getAutoDelete() ? (byte) -1 : (byte) 0);
                    
            if(getObject().getAltExchange() != null)
            {
                encoder.writeBin128( getObject().getAltExchange().getId() );
            }
            
            encoder.writeMap( getObject().getArguments() );
                    
        }
    }
    
    public final class QMFExchangeInstrumentInfoCommand extends QMFInstrumentInfoCommand<ExchangeObject>
    {
    
        protected QMFExchangeInstrumentInfoCommand(ExchangeObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeUint32( getObject().getProducerCount() );
            encoder.writeUint32( getObject().getProducerCountHigh() );
            encoder.writeUint32( getObject().getProducerCountLow() );
                    
            encoder.writeUint32( getObject().getBindingCount() );
            encoder.writeUint32( getObject().getBindingCountHigh() );
            encoder.writeUint32( getObject().getBindingCountLow() );
                    
            encoder.writeUint64( getObject().getMsgReceives() );
                    
            encoder.writeUint64( getObject().getMsgDrops() );
                    
            encoder.writeUint64( getObject().getMsgRoutes() );
                    
            encoder.writeUint64( getObject().getByteReceives() );
                    
            encoder.writeUint64( getObject().getByteDrops() );
                    
            encoder.writeUint64( getObject().getByteRoutes() );
                    
        }
    }
    
    public final class QMFExchangeGetQueryResponseCommand extends QMFGetQueryResponseCommand<ExchangeObject>
    {
    
        protected QMFExchangeGetQueryResponseCommand(ExchangeObject object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(object, cmd, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            byte presence0 = (byte) 0;              
        
            if(  getObject().getAltExchange() != null )
            {
                presence0 |= (1 <<0);
            }
        
            encoder.writeUint8( presence0 );
        
            encoder.writeBin128( getObject().getVhostRef().getId() );
                    
            encoder.writeStr8( getObject().getName() );
                    
            encoder.writeStr8( getObject().getType() );
                    
            encoder.writeInt8( getObject().getDurable() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getAutoDelete() ? (byte) -1 : (byte) 0);
                    
            if(getObject().getAltExchange() != null)
            {
                encoder.writeBin128( getObject().getAltExchange().getId() );
            }
            
            encoder.writeMap( getObject().getArguments() );
                    
            encoder.writeUint32( getObject().getProducerCount() );
            encoder.writeUint32( getObject().getProducerCountHigh() );
            encoder.writeUint32( getObject().getProducerCountLow() );
                    
            encoder.writeUint32( getObject().getBindingCount() );
            encoder.writeUint32( getObject().getBindingCountHigh() );
            encoder.writeUint32( getObject().getBindingCountLow() );
                    
            encoder.writeUint64( getObject().getMsgReceives() );
                    
            encoder.writeUint64( getObject().getMsgDrops() );
                    
            encoder.writeUint64( getObject().getMsgRoutes() );
                    
            encoder.writeUint64( getObject().getByteReceives() );
                    
            encoder.writeUint64( getObject().getByteDrops() );
                    
            encoder.writeUint64( getObject().getByteRoutes() );
                    
        }
    }
    
    
    


    
    public class BindingClass extends QMFObjectClass<BindingObject, BindingDelegate>
    {
        
        public class ExchangeRefProperty extends QMFProperty
        {
            
            private ExchangeRefProperty()
            {
                super( "exchangeRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

                setReferencedClass("exchange");

            }     
        }
    
        private final ExchangeRefProperty _exchangeRefProperty = new ExchangeRefProperty();
    
        public class QueueRefProperty extends QMFProperty
        {
            
            private QueueRefProperty()
            {
                super( "queueRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

                setReferencedClass("queue");

            }     
        }
    
        private final QueueRefProperty _queueRefProperty = new QueueRefProperty();
    
        public class BindingKeyProperty extends QMFProperty
        {
            
            private BindingKeyProperty()
            {
                super( "bindingKey",
                       QMFType.STR16,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

            }     
        }
    
        private final BindingKeyProperty _bindingKeyProperty = new BindingKeyProperty();
    
        public class ArgumentsProperty extends QMFProperty
        {
            
            private ArgumentsProperty()
            {
                super( "arguments",
                       QMFType.MAP,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final ArgumentsProperty _argumentsProperty = new ArgumentsProperty();
    
        public class OriginProperty extends QMFProperty
        {
            
            private OriginProperty()
            {
                super( "origin",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,
                       false,
                       true);

            }     
        }
    
        private final OriginProperty _originProperty = new OriginProperty();
    
        public class MsgMatchedStatistic extends QMFStatistic
        {
        
            private MsgMatchedStatistic()
            {
                super( "msgMatched", QMFType.UINT64, null, null); 
            }     
        }
        
        private final MsgMatchedStatistic _msgMatchedStatistic = new MsgMatchedStatistic();
    
    
        private BindingClass()
        {
            super("binding",
                 new byte[16]);

            setProperties( Arrays.asList( new QMFProperty[] { _exchangeRefProperty, _queueRefProperty, _bindingKeyProperty, _argumentsProperty, _originProperty } ) );
            setStatistics( Arrays.asList( new QMFStatistic[] { _msgMatchedStatistic } ) );
            setMethods( Arrays.asList( new QMFMethod[] {  } ) );
        }
        
        public BindingObject newInstance(final BindingDelegate delegate)
        {
            return new BindingObject(delegate);
        }
        
    }
    
    private final BindingClass _bindingClass = new BindingClass();
    
    public interface BindingDelegate extends QMFObject.Delegate
    {
        ExchangeObject getExchangeRef();
        QueueObject getQueueRef();
        String getBindingKey();
        Map getArguments();
        String getOrigin();
        Long getMsgMatched();
    }
    
    public final class BindingObject extends QMFObject<BindingClass, BindingDelegate>
    {
        protected BindingObject(BindingDelegate delegate)
        {
           super(delegate);
        }
        
        public BindingClass getQMFClass()
        {
            return _bindingClass;
        }
        
        public QMFCommand asConfigInfoCmd(long sampleTime) 
        {
            return new QMFBindingConfigInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asInstrumentInfoCmd(long sampleTime) 
        {
           return new QMFBindingInstrumentInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asGetQueryResponseCmd(QMFGetQueryCommand queryCommand, long sampleTime) 
        {
            return new QMFBindingGetQueryResponseCommand(this,queryCommand,sampleTime);
        }
    
    
        
        
            public ExchangeObject getExchangeRef()
            {
                return getDelegate().getExchangeRef();
            }
            
            
        
            public QueueObject getQueueRef()
            {
                return getDelegate().getQueueRef();
            }
            
            
        
            public String getBindingKey()
            {
                return getDelegate().getBindingKey();
            }
            
            
        
            public Map getArguments()
            {
                return getDelegate().getArguments();
            }
            
            
        
            public String getOrigin()
            {
                return getDelegate().getOrigin();
            }
            
            
        
            public Long getMsgMatched()
            {
                return getDelegate().getMsgMatched();
            }
            
            
    }

    public final class QMFBindingConfigInfoCommand extends QMFConfigInfoCommand<BindingObject>
    {
        
        protected QMFBindingConfigInfoCommand(BindingObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            byte presence0 = (byte) 0;              
        
            if(  getObject().getOrigin() != null )
            {
                presence0 |= (1 <<0);
            }
        
            encoder.writeUint8( presence0 );
        
            encoder.writeBin128( getObject().getExchangeRef().getId() );
                    
            encoder.writeBin128( getObject().getQueueRef().getId() );
                    
            encoder.writeStr16( getObject().getBindingKey() );
                    
            encoder.writeMap( getObject().getArguments() );
                    
            if(getObject().getOrigin() != null)
            {
                encoder.writeStr8( getObject().getOrigin() );
            }
            
        }
    }
    
    public final class QMFBindingInstrumentInfoCommand extends QMFInstrumentInfoCommand<BindingObject>
    {
    
        protected QMFBindingInstrumentInfoCommand(BindingObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeUint64( getObject().getMsgMatched() );
                    
        }
    }
    
    public final class QMFBindingGetQueryResponseCommand extends QMFGetQueryResponseCommand<BindingObject>
    {
    
        protected QMFBindingGetQueryResponseCommand(BindingObject object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(object, cmd, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            byte presence0 = (byte) 0;              
        
            if(  getObject().getOrigin() != null )
            {
                presence0 |= (1 <<0);
            }
        
            encoder.writeUint8( presence0 );
        
            encoder.writeBin128( getObject().getExchangeRef().getId() );
                    
            encoder.writeBin128( getObject().getQueueRef().getId() );
                    
            encoder.writeStr16( getObject().getBindingKey() );
                    
            encoder.writeMap( getObject().getArguments() );
                    
            if(getObject().getOrigin() != null)
            {
                encoder.writeStr8( getObject().getOrigin() );
            }
            
            encoder.writeUint64( getObject().getMsgMatched() );
                    
        }
    }
    
    
    


    
    public class SubscriptionClass extends QMFObjectClass<SubscriptionObject, SubscriptionDelegate>
    {
        
        public class SessionRefProperty extends QMFProperty
        {
            
            private SessionRefProperty()
            {
                super( "sessionRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

                setReferencedClass("session");

            }     
        }
    
        private final SessionRefProperty _sessionRefProperty = new SessionRefProperty();
    
        public class QueueRefProperty extends QMFProperty
        {
            
            private QueueRefProperty()
            {
                super( "queueRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

                setReferencedClass("queue");

            }     
        }
    
        private final QueueRefProperty _queueRefProperty = new QueueRefProperty();
    
        public class NameProperty extends QMFProperty
        {
            
            private NameProperty()
            {
                super( "name",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

            }     
        }
    
        private final NameProperty _nameProperty = new NameProperty();
    
        public class BrowsingProperty extends QMFProperty
        {
            
            private BrowsingProperty()
            {
                super( "browsing",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final BrowsingProperty _browsingProperty = new BrowsingProperty();
    
        public class AcknowledgedProperty extends QMFProperty
        {
            
            private AcknowledgedProperty()
            {
                super( "acknowledged",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final AcknowledgedProperty _acknowledgedProperty = new AcknowledgedProperty();
    
        public class ExclusiveProperty extends QMFProperty
        {
            
            private ExclusiveProperty()
            {
                super( "exclusive",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final ExclusiveProperty _exclusiveProperty = new ExclusiveProperty();
    
        public class CreditModeProperty extends QMFProperty
        {
            
            private CreditModeProperty()
            {
                super( "creditMode",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("WINDOW or CREDIT");

            }     
        }
    
        private final CreditModeProperty _creditModeProperty = new CreditModeProperty();
    
        public class ArgumentsProperty extends QMFProperty
        {
            
            private ArgumentsProperty()
            {
                super( "arguments",
                       QMFType.MAP,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final ArgumentsProperty _argumentsProperty = new ArgumentsProperty();
    
        public class DeliveredStatistic extends QMFStatistic
        {
        
            private DeliveredStatistic()
            {
                super( "delivered", QMFType.UINT64, "message", "Messages delivered"); 
            }     
        }
        
        private final DeliveredStatistic _deliveredStatistic = new DeliveredStatistic();
    
    
        private SubscriptionClass()
        {
            super("subscription",
                 new byte[16]);

            setProperties( Arrays.asList( new QMFProperty[] { _sessionRefProperty, _queueRefProperty, _nameProperty, _browsingProperty, _acknowledgedProperty, _exclusiveProperty, _creditModeProperty, _argumentsProperty } ) );
            setStatistics( Arrays.asList( new QMFStatistic[] { _deliveredStatistic } ) );
            setMethods( Arrays.asList( new QMFMethod[] {  } ) );
        }
        
        public SubscriptionObject newInstance(final SubscriptionDelegate delegate)
        {
            return new SubscriptionObject(delegate);
        }
        
    }
    
    private final SubscriptionClass _subscriptionClass = new SubscriptionClass();
    
    public interface SubscriptionDelegate extends QMFObject.Delegate
    {
        SessionObject getSessionRef();
        QueueObject getQueueRef();
        String getName();
        Boolean getBrowsing();
        Boolean getAcknowledged();
        Boolean getExclusive();
        String getCreditMode();
        Map getArguments();
        Long getDelivered();
    }
    
    public final class SubscriptionObject extends QMFObject<SubscriptionClass, SubscriptionDelegate>
    {
        protected SubscriptionObject(SubscriptionDelegate delegate)
        {
           super(delegate);
        }
        
        public SubscriptionClass getQMFClass()
        {
            return _subscriptionClass;
        }
        
        public QMFCommand asConfigInfoCmd(long sampleTime) 
        {
            return new QMFSubscriptionConfigInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asInstrumentInfoCmd(long sampleTime) 
        {
           return new QMFSubscriptionInstrumentInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asGetQueryResponseCmd(QMFGetQueryCommand queryCommand, long sampleTime) 
        {
            return new QMFSubscriptionGetQueryResponseCommand(this,queryCommand,sampleTime);
        }
    
    
        
        
            public SessionObject getSessionRef()
            {
                return getDelegate().getSessionRef();
            }
            
            
        
            public QueueObject getQueueRef()
            {
                return getDelegate().getQueueRef();
            }
            
            
        
            public String getName()
            {
                return getDelegate().getName();
            }
            
            
        
            public Boolean getBrowsing()
            {
                return getDelegate().getBrowsing();
            }
            
            
        
            public Boolean getAcknowledged()
            {
                return getDelegate().getAcknowledged();
            }
            
            
        
            public Boolean getExclusive()
            {
                return getDelegate().getExclusive();
            }
            
            
        
            public String getCreditMode()
            {
                return getDelegate().getCreditMode();
            }
            
            
        
            public Map getArguments()
            {
                return getDelegate().getArguments();
            }
            
            
        
            public Long getDelivered()
            {
                return getDelegate().getDelivered();
            }
            
            
    }

    public final class QMFSubscriptionConfigInfoCommand extends QMFConfigInfoCommand<SubscriptionObject>
    {
        
        protected QMFSubscriptionConfigInfoCommand(SubscriptionObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeBin128( getObject().getSessionRef().getId() );
                    
            encoder.writeBin128( getObject().getQueueRef().getId() );
                    
            encoder.writeStr8( getObject().getName() );
                    
            encoder.writeInt8( getObject().getBrowsing() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getAcknowledged() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getExclusive() ? (byte) -1 : (byte) 0);
                    
            encoder.writeStr8( getObject().getCreditMode() );
                    
            encoder.writeMap( getObject().getArguments() );
                    
        }
    }
    
    public final class QMFSubscriptionInstrumentInfoCommand extends QMFInstrumentInfoCommand<SubscriptionObject>
    {
    
        protected QMFSubscriptionInstrumentInfoCommand(SubscriptionObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeUint64( getObject().getDelivered() );
                    
        }
    }
    
    public final class QMFSubscriptionGetQueryResponseCommand extends QMFGetQueryResponseCommand<SubscriptionObject>
    {
    
        protected QMFSubscriptionGetQueryResponseCommand(SubscriptionObject object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(object, cmd, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeBin128( getObject().getSessionRef().getId() );
                    
            encoder.writeBin128( getObject().getQueueRef().getId() );
                    
            encoder.writeStr8( getObject().getName() );
                    
            encoder.writeInt8( getObject().getBrowsing() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getAcknowledged() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getExclusive() ? (byte) -1 : (byte) 0);
                    
            encoder.writeStr8( getObject().getCreditMode() );
                    
            encoder.writeMap( getObject().getArguments() );
                    
            encoder.writeUint64( getObject().getDelivered() );
                    
        }
    }
    
    
    


    
    public class ConnectionClass extends QMFObjectClass<ConnectionObject, ConnectionDelegate>
    {
        
        public class VhostRefProperty extends QMFProperty
        {
            
            private VhostRefProperty()
            {
                super( "vhostRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

                setReferencedClass("vhost");

            }     
        }
    
        private final VhostRefProperty _vhostRefProperty = new VhostRefProperty();
    
        public class AddressProperty extends QMFProperty
        {
            
            private AddressProperty()
            {
                super( "address",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

            }     
        }
    
        private final AddressProperty _addressProperty = new AddressProperty();
    
        public class IncomingProperty extends QMFProperty
        {
            
            private IncomingProperty()
            {
                super( "incoming",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final IncomingProperty _incomingProperty = new IncomingProperty();
    
        public class SystemConnectionProperty extends QMFProperty
        {
            
            private SystemConnectionProperty()
            {
                super( "SystemConnection",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

                setDescription("Infrastucture/ Inter-system connection (Cluster, Federation, ...)");

            }     
        }
    
        private final SystemConnectionProperty _systemConnectionProperty = new SystemConnectionProperty();
    
        public class UserProxyAuthProperty extends QMFProperty
        {
            
            private UserProxyAuthProperty()
            {
                super( "userProxyAuth",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Authorization to proxy for users not on broker");

            }     
        }
    
        private final UserProxyAuthProperty _userProxyAuthProperty = new UserProxyAuthProperty();
    
        public class FederationLinkProperty extends QMFProperty
        {
            
            private FederationLinkProperty()
            {
                super( "federationLink",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("Is this a federation link");

            }     
        }
    
        private final FederationLinkProperty _federationLinkProperty = new FederationLinkProperty();
    
        public class AuthIdentityProperty extends QMFProperty
        {
            
            private AuthIdentityProperty()
            {
                super( "authIdentity",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("authId of connection if authentication enabled");

            }     
        }
    
        private final AuthIdentityProperty _authIdentityProperty = new AuthIdentityProperty();
    
        public class RemoteProcessNameProperty extends QMFProperty
        {
            
            private RemoteProcessNameProperty()
            {
                super( "remoteProcessName",
                       QMFType.STR16,
                       QMFProperty.AccessCode.RO,
                       false,
                       true);

                setDescription("Name of executable running as remote client");

            }     
        }
    
        private final RemoteProcessNameProperty _remoteProcessNameProperty = new RemoteProcessNameProperty();
    
        public class RemotePidProperty extends QMFProperty
        {
            
            private RemotePidProperty()
            {
                super( "remotePid",
                       QMFType.UINT32,
                       QMFProperty.AccessCode.RO,
                       false,
                       true);

                setDescription("Process ID of remote client");

            }     
        }
    
        private final RemotePidProperty _remotePidProperty = new RemotePidProperty();
    
        public class RemoteParentPidProperty extends QMFProperty
        {
            
            private RemoteParentPidProperty()
            {
                super( "remoteParentPid",
                       QMFType.UINT32,
                       QMFProperty.AccessCode.RO,
                       false,
                       true);

                setDescription("Parent Process ID of remote client");

            }     
        }
    
        private final RemoteParentPidProperty _remoteParentPidProperty = new RemoteParentPidProperty();
    
        public class ShadowProperty extends QMFProperty
        {
            
            private ShadowProperty()
            {
                super( "shadow",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setDescription("True for shadow connections");

            }     
        }
    
        private final ShadowProperty _shadowProperty = new ShadowProperty();
    
        public class ClosingStatistic extends QMFStatistic
        {
        
            private ClosingStatistic()
            {
                super( "closing", QMFType.BOOLEAN, null, "This client is closing by management request"); 
            }     
        }
        
        private final ClosingStatistic _closingStatistic = new ClosingStatistic();
    
        public class FramesFromClientStatistic extends QMFStatistic
        {
        
            private FramesFromClientStatistic()
            {
                super( "framesFromClient", QMFType.UINT64, null, null); 
            }     
        }
        
        private final FramesFromClientStatistic _framesFromClientStatistic = new FramesFromClientStatistic();
    
        public class FramesToClientStatistic extends QMFStatistic
        {
        
            private FramesToClientStatistic()
            {
                super( "framesToClient", QMFType.UINT64, null, null); 
            }     
        }
        
        private final FramesToClientStatistic _framesToClientStatistic = new FramesToClientStatistic();
    
        public class BytesFromClientStatistic extends QMFStatistic
        {
        
            private BytesFromClientStatistic()
            {
                super( "bytesFromClient", QMFType.UINT64, null, null); 
            }     
        }
        
        private final BytesFromClientStatistic _bytesFromClientStatistic = new BytesFromClientStatistic();
    
        public class BytesToClientStatistic extends QMFStatistic
        {
        
            private BytesToClientStatistic()
            {
                super( "bytesToClient", QMFType.UINT64, null, null); 
            }     
        }
        
        private final BytesToClientStatistic _bytesToClientStatistic = new BytesToClientStatistic();
    
        public class MsgsFromClientStatistic extends QMFStatistic
        {
        
            private MsgsFromClientStatistic()
            {
                super( "msgsFromClient", QMFType.UINT64, null, null); 
            }     
        }
        
        private final MsgsFromClientStatistic _msgsFromClientStatistic = new MsgsFromClientStatistic();
    
        public class MsgsToClientStatistic extends QMFStatistic
        {
        
            private MsgsToClientStatistic()
            {
                super( "msgsToClient", QMFType.UINT64, null, null); 
            }     
        }
        
        private final MsgsToClientStatistic _msgsToClientStatistic = new MsgsToClientStatistic();
    
        public class CloseMethod extends QMFMethod<ConnectionObject>
        {
            private CloseMethod()
            {
                super( "close", null);
        
                
        
            }
            
            
            public CloseMethodInvocation parse(BBDecoder decoder)
            {
                
                return new CloseMethodInvocation();
            }
        }
        
        private final CloseMethod _closeMethod = new CloseMethod();
        
        private class CloseMethodInvocation implements QMFMethodInvocation<ConnectionObject>
        {
            
        
            private CloseMethodInvocation()
            {
                
            }
        
            public QMFMethodResponseCommand execute(ConnectionObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.close( new CloseMethodResponseCommandFactory(cmd) );
            }
        }
        
        public final class CloseMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private CloseMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public CloseMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new CloseMethodResponseCommand(_requestCmd, status, null);
            }
            
            public CloseMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new CloseMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public CloseMethodResponseCommand createResponseCommand(  )
            {
                return new CloseMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class CloseMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private CloseMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private CloseMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
    
        private ConnectionClass()
        {
            super("connection",
                 new byte[16]);

            setProperties( Arrays.asList( new QMFProperty[] { _vhostRefProperty, _addressProperty, _incomingProperty, _systemConnectionProperty, _userProxyAuthProperty, _federationLinkProperty, _authIdentityProperty, _remoteProcessNameProperty, _remotePidProperty, _remoteParentPidProperty, _shadowProperty } ) );
            setStatistics( Arrays.asList( new QMFStatistic[] { _closingStatistic, _framesFromClientStatistic, _framesToClientStatistic, _bytesFromClientStatistic, _bytesToClientStatistic, _msgsFromClientStatistic, _msgsToClientStatistic } ) );
            setMethods( Arrays.asList( new QMFMethod[] { _closeMethod } ) );
        }
        
        public ConnectionObject newInstance(final ConnectionDelegate delegate)
        {
            return new ConnectionObject(delegate);
        }
        
    }
    
    private final ConnectionClass _connectionClass = new ConnectionClass();
    
    public interface ConnectionDelegate extends QMFObject.Delegate
    {
        VhostObject getVhostRef();
        String getAddress();
        Boolean getIncoming();
        Boolean getSystemConnection();
        Boolean getUserProxyAuth();
        Boolean getFederationLink();
        String getAuthIdentity();
        String getRemoteProcessName();
        Long getRemotePid();
        Long getRemoteParentPid();
        Boolean getShadow();
        Boolean getClosing();
        Long getFramesFromClient();
        Long getFramesToClient();
        Long getBytesFromClient();
        Long getBytesToClient();
        Long getMsgsFromClient();
        Long getMsgsToClient();
        ConnectionClass.CloseMethodResponseCommand close(ConnectionClass.CloseMethodResponseCommandFactory factory);
    }
    
    public final class ConnectionObject extends QMFObject<ConnectionClass, ConnectionDelegate>
    {
        protected ConnectionObject(ConnectionDelegate delegate)
        {
           super(delegate);
        }
        
        public ConnectionClass getQMFClass()
        {
            return _connectionClass;
        }
        
        public QMFCommand asConfigInfoCmd(long sampleTime) 
        {
            return new QMFConnectionConfigInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asInstrumentInfoCmd(long sampleTime) 
        {
           return new QMFConnectionInstrumentInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asGetQueryResponseCmd(QMFGetQueryCommand queryCommand, long sampleTime) 
        {
            return new QMFConnectionGetQueryResponseCommand(this,queryCommand,sampleTime);
        }
    
    
        
        public ConnectionClass.CloseMethodResponseCommand close(ConnectionClass.CloseMethodResponseCommandFactory factory)
        {
            return getDelegate().close(factory );
        }
        
        
            public VhostObject getVhostRef()
            {
                return getDelegate().getVhostRef();
            }
            
            
        
            public String getAddress()
            {
                return getDelegate().getAddress();
            }
            
            
        
            public Boolean getIncoming()
            {
                return getDelegate().getIncoming();
            }
            
            
        
            public Boolean getSystemConnection()
            {
                return getDelegate().getSystemConnection();
            }
            
            
        
            public Boolean getUserProxyAuth()
            {
                return getDelegate().getUserProxyAuth();
            }
            
            
        
            public Boolean getFederationLink()
            {
                return getDelegate().getFederationLink();
            }
            
            
        
            public String getAuthIdentity()
            {
                return getDelegate().getAuthIdentity();
            }
            
            
        
            public String getRemoteProcessName()
            {
                return getDelegate().getRemoteProcessName();
            }
            
            
        
            public Long getRemotePid()
            {
                return getDelegate().getRemotePid();
            }
            
            
        
            public Long getRemoteParentPid()
            {
                return getDelegate().getRemoteParentPid();
            }
            
            
        
            public Boolean getShadow()
            {
                return getDelegate().getShadow();
            }
            
            
        
            public Boolean getClosing()
            {
                return getDelegate().getClosing();
            }
            
            
        
            public Long getFramesFromClient()
            {
                return getDelegate().getFramesFromClient();
            }
            
            
        
            public Long getFramesToClient()
            {
                return getDelegate().getFramesToClient();
            }
            
            
        
            public Long getBytesFromClient()
            {
                return getDelegate().getBytesFromClient();
            }
            
            
        
            public Long getBytesToClient()
            {
                return getDelegate().getBytesToClient();
            }
            
            
        
            public Long getMsgsFromClient()
            {
                return getDelegate().getMsgsFromClient();
            }
            
            
        
            public Long getMsgsToClient()
            {
                return getDelegate().getMsgsToClient();
            }
            
            
    }

    public final class QMFConnectionConfigInfoCommand extends QMFConfigInfoCommand<ConnectionObject>
    {
        
        protected QMFConnectionConfigInfoCommand(ConnectionObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            byte presence0 = (byte) 0;              
        
            if(  getObject().getRemoteProcessName() != null )
            {
                presence0 |= (1 <<0);
            }
        
            if(  getObject().getRemotePid() != null )
            {
                presence0 |= (1 <<1);
            }
        
            if(  getObject().getRemoteParentPid() != null )
            {
                presence0 |= (1 <<2);
            }
        
            encoder.writeUint8( presence0 );
        
            encoder.writeBin128( getObject().getVhostRef().getId() );
                    
            encoder.writeStr8( getObject().getAddress() );
                    
            encoder.writeInt8( getObject().getIncoming() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getSystemConnection() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getUserProxyAuth() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getFederationLink() ? (byte) -1 : (byte) 0);
                    
            encoder.writeStr8( getObject().getAuthIdentity() );
                    
            if(getObject().getRemoteProcessName() != null)
            {
                encoder.writeStr16( getObject().getRemoteProcessName() );
            }
            
            if(getObject().getRemotePid() != null)
            {
                encoder.writeUint32( getObject().getRemotePid() );
            }
            
            if(getObject().getRemoteParentPid() != null)
            {
                encoder.writeUint32( getObject().getRemoteParentPid() );
            }
            
            encoder.writeInt8( getObject().getShadow() ? (byte) -1 : (byte) 0);
                    
        }
    }
    
    public final class QMFConnectionInstrumentInfoCommand extends QMFInstrumentInfoCommand<ConnectionObject>
    {
    
        protected QMFConnectionInstrumentInfoCommand(ConnectionObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeInt8( getObject().getClosing() ? (byte) -1 : (byte) 0);
                    
            encoder.writeUint64( getObject().getFramesFromClient() );
                    
            encoder.writeUint64( getObject().getFramesToClient() );
                    
            encoder.writeUint64( getObject().getBytesFromClient() );
                    
            encoder.writeUint64( getObject().getBytesToClient() );
                    
            encoder.writeUint64( getObject().getMsgsFromClient() );
                    
            encoder.writeUint64( getObject().getMsgsToClient() );
                    
        }
    }
    
    public final class QMFConnectionGetQueryResponseCommand extends QMFGetQueryResponseCommand<ConnectionObject>
    {
    
        protected QMFConnectionGetQueryResponseCommand(ConnectionObject object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(object, cmd, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            byte presence0 = (byte) 0;              
        
            if(  getObject().getRemoteProcessName() != null )
            {
                presence0 |= (1 <<0);
            }
        
            if(  getObject().getRemotePid() != null )
            {
                presence0 |= (1 <<1);
            }
        
            if(  getObject().getRemoteParentPid() != null )
            {
                presence0 |= (1 <<2);
            }
        
            encoder.writeUint8( presence0 );
        
            encoder.writeBin128( getObject().getVhostRef().getId() );
                    
            encoder.writeStr8( getObject().getAddress() );
                    
            encoder.writeInt8( getObject().getIncoming() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getSystemConnection() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getUserProxyAuth() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getFederationLink() ? (byte) -1 : (byte) 0);
                    
            encoder.writeStr8( getObject().getAuthIdentity() );
                    
            if(getObject().getRemoteProcessName() != null)
            {
                encoder.writeStr16( getObject().getRemoteProcessName() );
            }
            
            if(getObject().getRemotePid() != null)
            {
                encoder.writeUint32( getObject().getRemotePid() );
            }
            
            if(getObject().getRemoteParentPid() != null)
            {
                encoder.writeUint32( getObject().getRemoteParentPid() );
            }
            
            encoder.writeInt8( getObject().getShadow() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getClosing() ? (byte) -1 : (byte) 0);
                    
            encoder.writeUint64( getObject().getFramesFromClient() );
                    
            encoder.writeUint64( getObject().getFramesToClient() );
                    
            encoder.writeUint64( getObject().getBytesFromClient() );
                    
            encoder.writeUint64( getObject().getBytesToClient() );
                    
            encoder.writeUint64( getObject().getMsgsFromClient() );
                    
            encoder.writeUint64( getObject().getMsgsToClient() );
                    
        }
    }
    
    
    


    
    public class LinkClass extends QMFObjectClass<LinkObject, LinkDelegate>
    {
        
        public class VhostRefProperty extends QMFProperty
        {
            
            private VhostRefProperty()
            {
                super( "vhostRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

                setReferencedClass("vhost");

            }     
        }
    
        private final VhostRefProperty _vhostRefProperty = new VhostRefProperty();
    
        public class HostProperty extends QMFProperty
        {
            
            private HostProperty()
            {
                super( "host",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

            }     
        }
    
        private final HostProperty _hostProperty = new HostProperty();
    
        public class PortProperty extends QMFProperty
        {
            
            private PortProperty()
            {
                super( "port",
                       QMFType.UINT16,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

            }     
        }
    
        private final PortProperty _portProperty = new PortProperty();
    
        public class TransportProperty extends QMFProperty
        {
            
            private TransportProperty()
            {
                super( "transport",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final TransportProperty _transportProperty = new TransportProperty();
    
        public class DurableProperty extends QMFProperty
        {
            
            private DurableProperty()
            {
                super( "durable",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final DurableProperty _durableProperty = new DurableProperty();
    
        public class StateStatistic extends QMFStatistic
        {
        
            private StateStatistic()
            {
                super( "state", QMFType.STR8, null, "Operational state of the link"); 
            }     
        }
        
        private final StateStatistic _stateStatistic = new StateStatistic();
    
        public class LastErrorStatistic extends QMFStatistic
        {
        
            private LastErrorStatistic()
            {
                super( "lastError", QMFType.STR16, null, "Reason link is not operational"); 
            }     
        }
        
        private final LastErrorStatistic _lastErrorStatistic = new LastErrorStatistic();
    
        public class CloseMethod extends QMFMethod<LinkObject>
        {
            private CloseMethod()
            {
                super( "close", null);
        
                
        
            }
            
            
            public CloseMethodInvocation parse(BBDecoder decoder)
            {
                
                return new CloseMethodInvocation();
            }
        }
        
        private final CloseMethod _closeMethod = new CloseMethod();
        
        private class CloseMethodInvocation implements QMFMethodInvocation<LinkObject>
        {
            
        
            private CloseMethodInvocation()
            {
                
            }
        
            public QMFMethodResponseCommand execute(LinkObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.close( new CloseMethodResponseCommandFactory(cmd) );
            }
        }
        
        public final class CloseMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private CloseMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public CloseMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new CloseMethodResponseCommand(_requestCmd, status, null);
            }
            
            public CloseMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new CloseMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public CloseMethodResponseCommand createResponseCommand(  )
            {
                return new CloseMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class CloseMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private CloseMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private CloseMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
        public class BridgeMethod extends QMFMethod<LinkObject>
        {
            private BridgeMethod()
            {
                super( "bridge", "Bridge messages over the link");
        
                
                QMFMethod.Argument durable = new QMFMethod.Argument("durable", QMFType.BOOLEAN);
                durable.setDirection(QMFMethod.Direction.I);                  
                addArgument( durable );
    
                QMFMethod.Argument src = new QMFMethod.Argument("src", QMFType.STR8);
                src.setDirection(QMFMethod.Direction.I);                  
                addArgument( src );
    
                QMFMethod.Argument dest = new QMFMethod.Argument("dest", QMFType.STR8);
                dest.setDirection(QMFMethod.Direction.I);                  
                addArgument( dest );
    
                QMFMethod.Argument key = new QMFMethod.Argument("key", QMFType.STR16);
                key.setDirection(QMFMethod.Direction.I);                  
                addArgument( key );
    
                QMFMethod.Argument tag = new QMFMethod.Argument("tag", QMFType.STR8);
                tag.setDirection(QMFMethod.Direction.I);                  
                addArgument( tag );
    
                QMFMethod.Argument excludes = new QMFMethod.Argument("excludes", QMFType.STR8);
                excludes.setDirection(QMFMethod.Direction.I);                  
                addArgument( excludes );
    
                QMFMethod.Argument srcIsQueue = new QMFMethod.Argument("srcIsQueue", QMFType.BOOLEAN);
                srcIsQueue.setDirection(QMFMethod.Direction.I);                  
                addArgument( srcIsQueue );
    
                QMFMethod.Argument srcIsLocal = new QMFMethod.Argument("srcIsLocal", QMFType.BOOLEAN);
                srcIsLocal.setDirection(QMFMethod.Direction.I);                  
                addArgument( srcIsLocal );
    
                QMFMethod.Argument dynamic = new QMFMethod.Argument("dynamic", QMFType.BOOLEAN);
                dynamic.setDirection(QMFMethod.Direction.I);                  
                addArgument( dynamic );
    
                QMFMethod.Argument sync = new QMFMethod.Argument("sync", QMFType.UINT16);
                sync.setDirection(QMFMethod.Direction.I);                  
                addArgument( sync );
    
        
            }
            
            
            public BridgeMethodInvocation parse(BBDecoder decoder)
            {
                Boolean durable = decoder.readInt8() != 0;
    String src = decoder.readStr8();
    String dest = decoder.readStr8();
    String key = decoder.readStr16();
    String tag = decoder.readStr8();
    String excludes = decoder.readStr8();
    Boolean srcIsQueue = decoder.readInt8() != 0;
    Boolean srcIsLocal = decoder.readInt8() != 0;
    Boolean dynamic = decoder.readInt8() != 0;
    Integer sync = decoder.readUint16();
    
                return new BridgeMethodInvocation( durable,  src,  dest,  key,  tag,  excludes,  srcIsQueue,  srcIsLocal,  dynamic,  sync);
            }
        }
        
        private final BridgeMethod _bridgeMethod = new BridgeMethod();
        
        private class BridgeMethodInvocation implements QMFMethodInvocation<LinkObject>
        {
            
            private final Boolean _durable;
    
            private final String _src;
    
            private final String _dest;
    
            private final String _key;
    
            private final String _tag;
    
            private final String _excludes;
    
            private final Boolean _srcIsQueue;
    
            private final Boolean _srcIsLocal;
    
            private final Boolean _dynamic;
    
            private final Integer _sync;
    
        
            private BridgeMethodInvocation(Boolean durable, String src, String dest, String key, String tag, String excludes, Boolean srcIsQueue, Boolean srcIsLocal, Boolean dynamic, Integer sync)
            {
                
                _durable = durable;
    
                _src = src;
    
                _dest = dest;
    
                _key = key;
    
                _tag = tag;
    
                _excludes = excludes;
    
                _srcIsQueue = srcIsQueue;
    
                _srcIsLocal = srcIsLocal;
    
                _dynamic = dynamic;
    
                _sync = sync;
    
            }
        
            public QMFMethodResponseCommand execute(LinkObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.bridge( new BridgeMethodResponseCommandFactory(cmd),  _durable,  _src,  _dest,  _key,  _tag,  _excludes,  _srcIsQueue,  _srcIsLocal,  _dynamic,  _sync );
            }
        }
        
        public final class BridgeMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private BridgeMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public BridgeMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new BridgeMethodResponseCommand(_requestCmd, status, null);
            }
            
            public BridgeMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new BridgeMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public BridgeMethodResponseCommand createResponseCommand(  )
            {
                return new BridgeMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class BridgeMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private BridgeMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private BridgeMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
    
        private LinkClass()
        {
            super("link",
                 new byte[16]);

            setProperties( Arrays.asList( new QMFProperty[] { _vhostRefProperty, _hostProperty, _portProperty, _transportProperty, _durableProperty } ) );
            setStatistics( Arrays.asList( new QMFStatistic[] { _stateStatistic, _lastErrorStatistic } ) );
            setMethods( Arrays.asList( new QMFMethod[] { _closeMethod, _bridgeMethod } ) );
        }
        
        public LinkObject newInstance(final LinkDelegate delegate)
        {
            return new LinkObject(delegate);
        }
        
    }
    
    private final LinkClass _linkClass = new LinkClass();
    
    public interface LinkDelegate extends QMFObject.Delegate
    {
        VhostObject getVhostRef();
        String getHost();
        Integer getPort();
        String getTransport();
        Boolean getDurable();
        String getState();
        String getLastError();
        LinkClass.CloseMethodResponseCommand close(LinkClass.CloseMethodResponseCommandFactory factory);
        LinkClass.BridgeMethodResponseCommand bridge(LinkClass.BridgeMethodResponseCommandFactory factory, Boolean durable, String src, String dest, String key, String tag, String excludes, Boolean srcIsQueue, Boolean srcIsLocal, Boolean dynamic, Integer sync);
    }
    
    public final class LinkObject extends QMFObject<LinkClass, LinkDelegate>
    {
        protected LinkObject(LinkDelegate delegate)
        {
           super(delegate);
        }
        
        public LinkClass getQMFClass()
        {
            return _linkClass;
        }
        
        public QMFCommand asConfigInfoCmd(long sampleTime) 
        {
            return new QMFLinkConfigInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asInstrumentInfoCmd(long sampleTime) 
        {
           return new QMFLinkInstrumentInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asGetQueryResponseCmd(QMFGetQueryCommand queryCommand, long sampleTime) 
        {
            return new QMFLinkGetQueryResponseCommand(this,queryCommand,sampleTime);
        }
    
    
        
        public LinkClass.CloseMethodResponseCommand close(LinkClass.CloseMethodResponseCommandFactory factory)
        {
            return getDelegate().close(factory );
        }
        
        public LinkClass.BridgeMethodResponseCommand bridge(LinkClass.BridgeMethodResponseCommandFactory factory, Boolean durable, String src, String dest, String key, String tag, String excludes, Boolean srcIsQueue, Boolean srcIsLocal, Boolean dynamic, Integer sync)
        {
            return getDelegate().bridge(factory,  durable,  src,  dest,  key,  tag,  excludes,  srcIsQueue,  srcIsLocal,  dynamic,  sync );
        }
        
        
            public VhostObject getVhostRef()
            {
                return getDelegate().getVhostRef();
            }
            
            
        
            public String getHost()
            {
                return getDelegate().getHost();
            }
            
            
        
            public Integer getPort()
            {
                return getDelegate().getPort();
            }
            
            
        
            public String getTransport()
            {
                return getDelegate().getTransport();
            }
            
            
        
            public Boolean getDurable()
            {
                return getDelegate().getDurable();
            }
            
            
        
            public String getState()
            {
                return getDelegate().getState();
            }
            
            
        
            public String getLastError()
            {
                return getDelegate().getLastError();
            }
            
            
    }

    public final class QMFLinkConfigInfoCommand extends QMFConfigInfoCommand<LinkObject>
    {
        
        protected QMFLinkConfigInfoCommand(LinkObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeBin128( getObject().getVhostRef().getId() );
                    
            encoder.writeStr8( getObject().getHost() );
                    
            encoder.writeUint16( getObject().getPort() );
                    
            encoder.writeStr8( getObject().getTransport() );
                    
            encoder.writeInt8( getObject().getDurable() ? (byte) -1 : (byte) 0);
                    
        }
    }
    
    public final class QMFLinkInstrumentInfoCommand extends QMFInstrumentInfoCommand<LinkObject>
    {
    
        protected QMFLinkInstrumentInfoCommand(LinkObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeStr8( getObject().getState() );
                    
            encoder.writeStr16( getObject().getLastError() );
                    
        }
    }
    
    public final class QMFLinkGetQueryResponseCommand extends QMFGetQueryResponseCommand<LinkObject>
    {
    
        protected QMFLinkGetQueryResponseCommand(LinkObject object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(object, cmd, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeBin128( getObject().getVhostRef().getId() );
                    
            encoder.writeStr8( getObject().getHost() );
                    
            encoder.writeUint16( getObject().getPort() );
                    
            encoder.writeStr8( getObject().getTransport() );
                    
            encoder.writeInt8( getObject().getDurable() ? (byte) -1 : (byte) 0);
                    
            encoder.writeStr8( getObject().getState() );
                    
            encoder.writeStr16( getObject().getLastError() );
                    
        }
    }
    
    
    


    
    public class BridgeClass extends QMFObjectClass<BridgeObject, BridgeDelegate>
    {
        
        public class LinkRefProperty extends QMFProperty
        {
            
            private LinkRefProperty()
            {
                super( "linkRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

                setReferencedClass("link");

            }     
        }
    
        private final LinkRefProperty _linkRefProperty = new LinkRefProperty();
    
        public class ChannelIdProperty extends QMFProperty
        {
            
            private ChannelIdProperty()
            {
                super( "channelId",
                       QMFType.UINT16,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

            }     
        }
    
        private final ChannelIdProperty _channelIdProperty = new ChannelIdProperty();
    
        public class DurableProperty extends QMFProperty
        {
            
            private DurableProperty()
            {
                super( "durable",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final DurableProperty _durableProperty = new DurableProperty();
    
        public class SrcProperty extends QMFProperty
        {
            
            private SrcProperty()
            {
                super( "src",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final SrcProperty _srcProperty = new SrcProperty();
    
        public class DestProperty extends QMFProperty
        {
            
            private DestProperty()
            {
                super( "dest",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final DestProperty _destProperty = new DestProperty();
    
        public class KeyProperty extends QMFProperty
        {
            
            private KeyProperty()
            {
                super( "key",
                       QMFType.STR16,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final KeyProperty _keyProperty = new KeyProperty();
    
        public class SrcIsQueueProperty extends QMFProperty
        {
            
            private SrcIsQueueProperty()
            {
                super( "srcIsQueue",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final SrcIsQueueProperty _srcIsQueueProperty = new SrcIsQueueProperty();
    
        public class SrcIsLocalProperty extends QMFProperty
        {
            
            private SrcIsLocalProperty()
            {
                super( "srcIsLocal",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final SrcIsLocalProperty _srcIsLocalProperty = new SrcIsLocalProperty();
    
        public class TagProperty extends QMFProperty
        {
            
            private TagProperty()
            {
                super( "tag",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final TagProperty _tagProperty = new TagProperty();
    
        public class ExcludesProperty extends QMFProperty
        {
            
            private ExcludesProperty()
            {
                super( "excludes",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final ExcludesProperty _excludesProperty = new ExcludesProperty();
    
        public class DynamicProperty extends QMFProperty
        {
            
            private DynamicProperty()
            {
                super( "dynamic",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final DynamicProperty _dynamicProperty = new DynamicProperty();
    
        public class SyncProperty extends QMFProperty
        {
            
            private SyncProperty()
            {
                super( "sync",
                       QMFType.UINT16,
                       QMFProperty.AccessCode.RC,
                       false,
                       false);

            }     
        }
    
        private final SyncProperty _syncProperty = new SyncProperty();
    
        public class CloseMethod extends QMFMethod<BridgeObject>
        {
            private CloseMethod()
            {
                super( "close", null);
        
                
        
            }
            
            
            public CloseMethodInvocation parse(BBDecoder decoder)
            {
                
                return new CloseMethodInvocation();
            }
        }
        
        private final CloseMethod _closeMethod = new CloseMethod();
        
        private class CloseMethodInvocation implements QMFMethodInvocation<BridgeObject>
        {
            
        
            private CloseMethodInvocation()
            {
                
            }
        
            public QMFMethodResponseCommand execute(BridgeObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.close( new CloseMethodResponseCommandFactory(cmd) );
            }
        }
        
        public final class CloseMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private CloseMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public CloseMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new CloseMethodResponseCommand(_requestCmd, status, null);
            }
            
            public CloseMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new CloseMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public CloseMethodResponseCommand createResponseCommand(  )
            {
                return new CloseMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class CloseMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private CloseMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private CloseMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
    
        private BridgeClass()
        {
            super("bridge",
                 new byte[16]);

            setProperties( Arrays.asList( new QMFProperty[] { _linkRefProperty, _channelIdProperty, _durableProperty, _srcProperty, _destProperty, _keyProperty, _srcIsQueueProperty, _srcIsLocalProperty, _tagProperty, _excludesProperty, _dynamicProperty, _syncProperty } ) );
            setStatistics( Arrays.asList( new QMFStatistic[] {  } ) );
            setMethods( Arrays.asList( new QMFMethod[] { _closeMethod } ) );
        }
        
        public BridgeObject newInstance(final BridgeDelegate delegate)
        {
            return new BridgeObject(delegate);
        }
        
    }
    
    private final BridgeClass _bridgeClass = new BridgeClass();
    
    public interface BridgeDelegate extends QMFObject.Delegate
    {
        LinkObject getLinkRef();
        Integer getChannelId();
        Boolean getDurable();
        String getSrc();
        String getDest();
        String getKey();
        Boolean getSrcIsQueue();
        Boolean getSrcIsLocal();
        String getTag();
        String getExcludes();
        Boolean getDynamic();
        Integer getSync();
        BridgeClass.CloseMethodResponseCommand close(BridgeClass.CloseMethodResponseCommandFactory factory);
    }
    
    public final class BridgeObject extends QMFObject<BridgeClass, BridgeDelegate>
    {
        protected BridgeObject(BridgeDelegate delegate)
        {
           super(delegate);
        }
        
        public BridgeClass getQMFClass()
        {
            return _bridgeClass;
        }
        
        public QMFCommand asConfigInfoCmd(long sampleTime) 
        {
            return new QMFBridgeConfigInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asInstrumentInfoCmd(long sampleTime) 
        {
           return new QMFBridgeInstrumentInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asGetQueryResponseCmd(QMFGetQueryCommand queryCommand, long sampleTime) 
        {
            return new QMFBridgeGetQueryResponseCommand(this,queryCommand,sampleTime);
        }
    
    
        
        public BridgeClass.CloseMethodResponseCommand close(BridgeClass.CloseMethodResponseCommandFactory factory)
        {
            return getDelegate().close(factory );
        }
        
        
            public LinkObject getLinkRef()
            {
                return getDelegate().getLinkRef();
            }
            
            
        
            public Integer getChannelId()
            {
                return getDelegate().getChannelId();
            }
            
            
        
            public Boolean getDurable()
            {
                return getDelegate().getDurable();
            }
            
            
        
            public String getSrc()
            {
                return getDelegate().getSrc();
            }
            
            
        
            public String getDest()
            {
                return getDelegate().getDest();
            }
            
            
        
            public String getKey()
            {
                return getDelegate().getKey();
            }
            
            
        
            public Boolean getSrcIsQueue()
            {
                return getDelegate().getSrcIsQueue();
            }
            
            
        
            public Boolean getSrcIsLocal()
            {
                return getDelegate().getSrcIsLocal();
            }
            
            
        
            public String getTag()
            {
                return getDelegate().getTag();
            }
            
            
        
            public String getExcludes()
            {
                return getDelegate().getExcludes();
            }
            
            
        
            public Boolean getDynamic()
            {
                return getDelegate().getDynamic();
            }
            
            
        
            public Integer getSync()
            {
                return getDelegate().getSync();
            }
            
            
    }

    public final class QMFBridgeConfigInfoCommand extends QMFConfigInfoCommand<BridgeObject>
    {
        
        protected QMFBridgeConfigInfoCommand(BridgeObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeBin128( getObject().getLinkRef().getId() );
                    
            encoder.writeUint16( getObject().getChannelId() );
                    
            encoder.writeInt8( getObject().getDurable() ? (byte) -1 : (byte) 0);
                    
            encoder.writeStr8( getObject().getSrc() );
                    
            encoder.writeStr8( getObject().getDest() );
                    
            encoder.writeStr16( getObject().getKey() );
                    
            encoder.writeInt8( getObject().getSrcIsQueue() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getSrcIsLocal() ? (byte) -1 : (byte) 0);
                    
            encoder.writeStr8( getObject().getTag() );
                    
            encoder.writeStr8( getObject().getExcludes() );
                    
            encoder.writeInt8( getObject().getDynamic() ? (byte) -1 : (byte) 0);
                    
            encoder.writeUint16( getObject().getSync() );
                    
        }
    }
    
    public final class QMFBridgeInstrumentInfoCommand extends QMFInstrumentInfoCommand<BridgeObject>
    {
    
        protected QMFBridgeInstrumentInfoCommand(BridgeObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
        }
    }
    
    public final class QMFBridgeGetQueryResponseCommand extends QMFGetQueryResponseCommand<BridgeObject>
    {
    
        protected QMFBridgeGetQueryResponseCommand(BridgeObject object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(object, cmd, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeBin128( getObject().getLinkRef().getId() );
                    
            encoder.writeUint16( getObject().getChannelId() );
                    
            encoder.writeInt8( getObject().getDurable() ? (byte) -1 : (byte) 0);
                    
            encoder.writeStr8( getObject().getSrc() );
                    
            encoder.writeStr8( getObject().getDest() );
                    
            encoder.writeStr16( getObject().getKey() );
                    
            encoder.writeInt8( getObject().getSrcIsQueue() ? (byte) -1 : (byte) 0);
                    
            encoder.writeInt8( getObject().getSrcIsLocal() ? (byte) -1 : (byte) 0);
                    
            encoder.writeStr8( getObject().getTag() );
                    
            encoder.writeStr8( getObject().getExcludes() );
                    
            encoder.writeInt8( getObject().getDynamic() ? (byte) -1 : (byte) 0);
                    
            encoder.writeUint16( getObject().getSync() );
                    
        }
    }
    
    
    


    
    public class SessionClass extends QMFObjectClass<SessionObject, SessionDelegate>
    {
        
        public class VhostRefProperty extends QMFProperty
        {
            
            private VhostRefProperty()
            {
                super( "vhostRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

                setReferencedClass("vhost");

            }     
        }
    
        private final VhostRefProperty _vhostRefProperty = new VhostRefProperty();
    
        public class NameProperty extends QMFProperty
        {
            
            private NameProperty()
            {
                super( "name",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RC,
                       true,
                       false);

            }     
        }
    
        private final NameProperty _nameProperty = new NameProperty();
    
        public class ChannelIdProperty extends QMFProperty
        {
            
            private ChannelIdProperty()
            {
                super( "channelId",
                       QMFType.UINT16,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

            }     
        }
    
        private final ChannelIdProperty _channelIdProperty = new ChannelIdProperty();
    
        public class ConnectionRefProperty extends QMFProperty
        {
            
            private ConnectionRefProperty()
            {
                super( "connectionRef",
                       QMFType.OBJECTREFERENCE,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setReferencedClass("connection");

            }     
        }
    
        private final ConnectionRefProperty _connectionRefProperty = new ConnectionRefProperty();
    
        public class DetachedLifespanProperty extends QMFProperty
        {
            
            private DetachedLifespanProperty()
            {
                super( "detachedLifespan",
                       QMFType.UINT32,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

                setUnit("second");
        
            }     
        }
    
        private final DetachedLifespanProperty _detachedLifespanProperty = new DetachedLifespanProperty();
    
        public class AttachedProperty extends QMFProperty
        {
            
            private AttachedProperty()
            {
                super( "attached",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

            }     
        }
    
        private final AttachedProperty _attachedProperty = new AttachedProperty();
    
        public class ExpireTimeProperty extends QMFProperty
        {
            
            private ExpireTimeProperty()
            {
                super( "expireTime",
                       QMFType.ABSTIME,
                       QMFProperty.AccessCode.RO,
                       false,
                       true);

            }     
        }
    
        private final ExpireTimeProperty _expireTimeProperty = new ExpireTimeProperty();
    
        public class MaxClientRateProperty extends QMFProperty
        {
            
            private MaxClientRateProperty()
            {
                super( "maxClientRate",
                       QMFType.UINT32,
                       QMFProperty.AccessCode.RO,
                       false,
                       true);

                setUnit("msgs/sec");
        
            }     
        }
    
        private final MaxClientRateProperty _maxClientRateProperty = new MaxClientRateProperty();
    
        public class FramesOutstandingStatistic extends QMFStatistic
        {
        
            private FramesOutstandingStatistic()
            {
                super( "framesOutstanding", QMFType.UINT32, null, null); 
            }     
        }
        
        private final FramesOutstandingStatistic _framesOutstandingStatistic = new FramesOutstandingStatistic();
    
        public class TxnStartsStatistic extends QMFStatistic
        {
        
            private TxnStartsStatistic()
            {
                super( "TxnStarts", QMFType.UINT64, "transaction", "Total transactions started "); 
            }     
        }
        
        private final TxnStartsStatistic _txnStartsStatistic = new TxnStartsStatistic();
    
        public class TxnCommitsStatistic extends QMFStatistic
        {
        
            private TxnCommitsStatistic()
            {
                super( "TxnCommits", QMFType.UINT64, "transaction", "Total transactions committed"); 
            }     
        }
        
        private final TxnCommitsStatistic _txnCommitsStatistic = new TxnCommitsStatistic();
    
        public class TxnRejectsStatistic extends QMFStatistic
        {
        
            private TxnRejectsStatistic()
            {
                super( "TxnRejects", QMFType.UINT64, "transaction", "Total transactions rejected"); 
            }     
        }
        
        private final TxnRejectsStatistic _txnRejectsStatistic = new TxnRejectsStatistic();
    
        public class TxnCountStatistic extends QMFStatistic
        {
        
            private TxnCountStatistic()
            {
                super( "TxnCount", QMFType.UINT32, "transaction", "Current pending transactions"); 
            }     
        }
        
        private final TxnCountStatistic _txnCountStatistic = new TxnCountStatistic();
    
        public class ClientCreditStatistic extends QMFStatistic
        {
        
            private ClientCreditStatistic()
            {
                super( "clientCredit", QMFType.UINT32, "message", "Client message credit"); 
            }     
        }
        
        private final ClientCreditStatistic _clientCreditStatistic = new ClientCreditStatistic();
    
        public class SolicitAckMethod extends QMFMethod<SessionObject>
        {
            private SolicitAckMethod()
            {
                super( "solicitAck", null);
        
                
        
            }
            
            
            public SolicitAckMethodInvocation parse(BBDecoder decoder)
            {
                
                return new SolicitAckMethodInvocation();
            }
        }
        
        private final SolicitAckMethod _solicitAckMethod = new SolicitAckMethod();
        
        private class SolicitAckMethodInvocation implements QMFMethodInvocation<SessionObject>
        {
            
        
            private SolicitAckMethodInvocation()
            {
                
            }
        
            public QMFMethodResponseCommand execute(SessionObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.solicitAck( new SolicitAckMethodResponseCommandFactory(cmd) );
            }
        }
        
        public final class SolicitAckMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private SolicitAckMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public SolicitAckMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new SolicitAckMethodResponseCommand(_requestCmd, status, null);
            }
            
            public SolicitAckMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new SolicitAckMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public SolicitAckMethodResponseCommand createResponseCommand(  )
            {
                return new SolicitAckMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class SolicitAckMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private SolicitAckMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private SolicitAckMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
        public class DetachMethod extends QMFMethod<SessionObject>
        {
            private DetachMethod()
            {
                super( "detach", null);
        
                
        
            }
            
            
            public DetachMethodInvocation parse(BBDecoder decoder)
            {
                
                return new DetachMethodInvocation();
            }
        }
        
        private final DetachMethod _detachMethod = new DetachMethod();
        
        private class DetachMethodInvocation implements QMFMethodInvocation<SessionObject>
        {
            
        
            private DetachMethodInvocation()
            {
                
            }
        
            public QMFMethodResponseCommand execute(SessionObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.detach( new DetachMethodResponseCommandFactory(cmd) );
            }
        }
        
        public final class DetachMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private DetachMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public DetachMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new DetachMethodResponseCommand(_requestCmd, status, null);
            }
            
            public DetachMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new DetachMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public DetachMethodResponseCommand createResponseCommand(  )
            {
                return new DetachMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class DetachMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private DetachMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private DetachMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
        public class ResetLifespanMethod extends QMFMethod<SessionObject>
        {
            private ResetLifespanMethod()
            {
                super( "resetLifespan", null);
        
                
        
            }
            
            
            public ResetLifespanMethodInvocation parse(BBDecoder decoder)
            {
                
                return new ResetLifespanMethodInvocation();
            }
        }
        
        private final ResetLifespanMethod _resetLifespanMethod = new ResetLifespanMethod();
        
        private class ResetLifespanMethodInvocation implements QMFMethodInvocation<SessionObject>
        {
            
        
            private ResetLifespanMethodInvocation()
            {
                
            }
        
            public QMFMethodResponseCommand execute(SessionObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.resetLifespan( new ResetLifespanMethodResponseCommandFactory(cmd) );
            }
        }
        
        public final class ResetLifespanMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private ResetLifespanMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public ResetLifespanMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new ResetLifespanMethodResponseCommand(_requestCmd, status, null);
            }
            
            public ResetLifespanMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new ResetLifespanMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public ResetLifespanMethodResponseCommand createResponseCommand(  )
            {
                return new ResetLifespanMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class ResetLifespanMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private ResetLifespanMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private ResetLifespanMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
        public class CloseMethod extends QMFMethod<SessionObject>
        {
            private CloseMethod()
            {
                super( "close", null);
        
                
        
            }
            
            
            public CloseMethodInvocation parse(BBDecoder decoder)
            {
                
                return new CloseMethodInvocation();
            }
        }
        
        private final CloseMethod _closeMethod = new CloseMethod();
        
        private class CloseMethodInvocation implements QMFMethodInvocation<SessionObject>
        {
            
        
            private CloseMethodInvocation()
            {
                
            }
        
            public QMFMethodResponseCommand execute(SessionObject obj, QMFMethodRequestCommand cmd)
            {
                return obj.close( new CloseMethodResponseCommandFactory(cmd) );
            }
        }
        
        public final class CloseMethodResponseCommandFactory
        {
            private final QMFMethodRequestCommand _requestCmd;
            private CloseMethodResponseCommandFactory(QMFMethodRequestCommand cmd)
            {
                _requestCmd = cmd;
            }
            
            public CloseMethodResponseCommand createResponseCommand(CompletionCode status)
            {
                return new CloseMethodResponseCommand(_requestCmd, status, null);
            }
            
            public CloseMethodResponseCommand createResponseCommand(CompletionCode status, String msg)
            {
                return new CloseMethodResponseCommand(_requestCmd, status, msg);
            }
            
            public CloseMethodResponseCommand createResponseCommand(  )
            {
                return new CloseMethodResponseCommand(_requestCmd);
            }
        }
        
        public final class CloseMethodResponseCommand extends QMFMethodResponseCommand
        {
            
            private CloseMethodResponseCommand(QMFMethodRequestCommand cmd)
            {
                super(cmd, CompletionCode.OK, "OK");
                
                
            }
            
            private CloseMethodResponseCommand(QMFMethodRequestCommand cmd, CompletionCode status, String msg)
            {
                super(cmd, status, msg);
                
                
            }
            
            @Override
            public void encode(final BBEncoder encoder)
            {
                super.encode(encoder);
                
                
            }
        }
        
    
    
        private SessionClass()
        {
            super("session",
                 new byte[16]);

            setProperties( Arrays.asList( new QMFProperty[] { _vhostRefProperty, _nameProperty, _channelIdProperty, _connectionRefProperty, _detachedLifespanProperty, _attachedProperty, _expireTimeProperty, _maxClientRateProperty } ) );
            setStatistics( Arrays.asList( new QMFStatistic[] { _framesOutstandingStatistic, _txnStartsStatistic, _txnCommitsStatistic, _txnRejectsStatistic, _txnCountStatistic, _clientCreditStatistic } ) );
            setMethods( Arrays.asList( new QMFMethod[] { _solicitAckMethod, _detachMethod, _resetLifespanMethod, _closeMethod } ) );
        }
        
        public SessionObject newInstance(final SessionDelegate delegate)
        {
            return new SessionObject(delegate);
        }
        
    }
    
    private final SessionClass _sessionClass = new SessionClass();
    
    public interface SessionDelegate extends QMFObject.Delegate
    {
        VhostObject getVhostRef();
        String getName();
        Integer getChannelId();
        ConnectionObject getConnectionRef();
        Long getDetachedLifespan();
        Boolean getAttached();
        Long getExpireTime();
        Long getMaxClientRate();
        Long getFramesOutstanding();
        Long getTxnStarts();
        Long getTxnCommits();
        Long getTxnRejects();
        Long getTxnCount();
        Long getClientCredit();
        SessionClass.SolicitAckMethodResponseCommand solicitAck(SessionClass.SolicitAckMethodResponseCommandFactory factory);
        SessionClass.DetachMethodResponseCommand detach(SessionClass.DetachMethodResponseCommandFactory factory);
        SessionClass.ResetLifespanMethodResponseCommand resetLifespan(SessionClass.ResetLifespanMethodResponseCommandFactory factory);
        SessionClass.CloseMethodResponseCommand close(SessionClass.CloseMethodResponseCommandFactory factory);
    }
    
    public final class SessionObject extends QMFObject<SessionClass, SessionDelegate>
    {
        protected SessionObject(SessionDelegate delegate)
        {
           super(delegate);
        }
        
        public SessionClass getQMFClass()
        {
            return _sessionClass;
        }
        
        public QMFCommand asConfigInfoCmd(long sampleTime) 
        {
            return new QMFSessionConfigInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asInstrumentInfoCmd(long sampleTime) 
        {
           return new QMFSessionInstrumentInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asGetQueryResponseCmd(QMFGetQueryCommand queryCommand, long sampleTime) 
        {
            return new QMFSessionGetQueryResponseCommand(this,queryCommand,sampleTime);
        }
    
    
        
        public SessionClass.SolicitAckMethodResponseCommand solicitAck(SessionClass.SolicitAckMethodResponseCommandFactory factory)
        {
            return getDelegate().solicitAck(factory );
        }
        
        public SessionClass.DetachMethodResponseCommand detach(SessionClass.DetachMethodResponseCommandFactory factory)
        {
            return getDelegate().detach(factory );
        }
        
        public SessionClass.ResetLifespanMethodResponseCommand resetLifespan(SessionClass.ResetLifespanMethodResponseCommandFactory factory)
        {
            return getDelegate().resetLifespan(factory );
        }
        
        public SessionClass.CloseMethodResponseCommand close(SessionClass.CloseMethodResponseCommandFactory factory)
        {
            return getDelegate().close(factory );
        }
        
        
            public VhostObject getVhostRef()
            {
                return getDelegate().getVhostRef();
            }
            
            
        
            public String getName()
            {
                return getDelegate().getName();
            }
            
            
        
            public Integer getChannelId()
            {
                return getDelegate().getChannelId();
            }
            
            
        
            public ConnectionObject getConnectionRef()
            {
                return getDelegate().getConnectionRef();
            }
            
            
        
            public Long getDetachedLifespan()
            {
                return getDelegate().getDetachedLifespan();
            }
            
            
        
            public Boolean getAttached()
            {
                return getDelegate().getAttached();
            }
            
            
        
            public Long getExpireTime()
            {
                return getDelegate().getExpireTime();
            }
            
            
        
            public Long getMaxClientRate()
            {
                return getDelegate().getMaxClientRate();
            }
            
            
        
            public Long getFramesOutstanding()
            {
                return getDelegate().getFramesOutstanding();
            }
            
            
        
            public Long getTxnStarts()
            {
                return getDelegate().getTxnStarts();
            }
            
            
        
            public Long getTxnCommits()
            {
                return getDelegate().getTxnCommits();
            }
            
            
        
            public Long getTxnRejects()
            {
                return getDelegate().getTxnRejects();
            }
            
            
        
            public Long getTxnCount()
            {
                return getDelegate().getTxnCount();
            }
            
            
        
            public Long getClientCredit()
            {
                return getDelegate().getClientCredit();
            }
            
            
    }

    public final class QMFSessionConfigInfoCommand extends QMFConfigInfoCommand<SessionObject>
    {
        
        protected QMFSessionConfigInfoCommand(SessionObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            byte presence0 = (byte) 0;              
        
            if(  getObject().getExpireTime() != null )
            {
                presence0 |= (1 <<0);
            }
        
            if(  getObject().getMaxClientRate() != null )
            {
                presence0 |= (1 <<1);
            }
        
            encoder.writeUint8( presence0 );
        
            encoder.writeBin128( getObject().getVhostRef().getId() );
                    
            encoder.writeStr8( getObject().getName() );
                    
            encoder.writeUint16( getObject().getChannelId() );
                    
            encoder.writeBin128( getObject().getConnectionRef().getId() );
                    
            encoder.writeUint32( getObject().getDetachedLifespan() );
                    
            encoder.writeInt8( getObject().getAttached() ? (byte) -1 : (byte) 0);
                    
            if(getObject().getExpireTime() != null)
            {
                encoder.writeUint64( getObject().getExpireTime() );
            }
            
            if(getObject().getMaxClientRate() != null)
            {
                encoder.writeUint32( getObject().getMaxClientRate() );
            }
            
        }
    }
    
    public final class QMFSessionInstrumentInfoCommand extends QMFInstrumentInfoCommand<SessionObject>
    {
    
        protected QMFSessionInstrumentInfoCommand(SessionObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeUint32( getObject().getFramesOutstanding() );
                    
            encoder.writeUint64( getObject().getTxnStarts() );
                    
            encoder.writeUint64( getObject().getTxnCommits() );
                    
            encoder.writeUint64( getObject().getTxnRejects() );
                    
            encoder.writeUint32( getObject().getTxnCount() );
                    
            encoder.writeUint32( getObject().getClientCredit() );
                    
        }
    }
    
    public final class QMFSessionGetQueryResponseCommand extends QMFGetQueryResponseCommand<SessionObject>
    {
    
        protected QMFSessionGetQueryResponseCommand(SessionObject object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(object, cmd, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            byte presence0 = (byte) 0;              
        
            if(  getObject().getExpireTime() != null )
            {
                presence0 |= (1 <<0);
            }
        
            if(  getObject().getMaxClientRate() != null )
            {
                presence0 |= (1 <<1);
            }
        
            encoder.writeUint8( presence0 );
        
            encoder.writeBin128( getObject().getVhostRef().getId() );
                    
            encoder.writeStr8( getObject().getName() );
                    
            encoder.writeUint16( getObject().getChannelId() );
                    
            encoder.writeBin128( getObject().getConnectionRef().getId() );
                    
            encoder.writeUint32( getObject().getDetachedLifespan() );
                    
            encoder.writeInt8( getObject().getAttached() ? (byte) -1 : (byte) 0);
                    
            if(getObject().getExpireTime() != null)
            {
                encoder.writeUint64( getObject().getExpireTime() );
            }
            
            if(getObject().getMaxClientRate() != null)
            {
                encoder.writeUint32( getObject().getMaxClientRate() );
            }
            
            encoder.writeUint32( getObject().getFramesOutstanding() );
                    
            encoder.writeUint64( getObject().getTxnStarts() );
                    
            encoder.writeUint64( getObject().getTxnCommits() );
                    
            encoder.writeUint64( getObject().getTxnRejects() );
                    
            encoder.writeUint32( getObject().getTxnCount() );
                    
            encoder.writeUint32( getObject().getClientCredit() );
                    
        }
    }
    
    
    


    
    public class ManagementSetupStateClass extends QMFObjectClass<ManagementSetupStateObject, ManagementSetupStateDelegate>
    {
        
        public class ObjectNumProperty extends QMFProperty
        {
            
            private ObjectNumProperty()
            {
                super( "objectNum",
                       QMFType.UINT64,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

            }     
        }
    
        private final ObjectNumProperty _objectNumProperty = new ObjectNumProperty();
    
        public class BootSequenceProperty extends QMFProperty
        {
            
            private BootSequenceProperty()
            {
                super( "bootSequence",
                       QMFType.UINT16,
                       QMFProperty.AccessCode.RO,
                       false,
                       false);

            }     
        }
    
        private final BootSequenceProperty _bootSequenceProperty = new BootSequenceProperty();
    
    
        private ManagementSetupStateClass()
        {
            super("managementSetupState",
                 new byte[16]);

            setProperties( Arrays.asList( new QMFProperty[] { _objectNumProperty, _bootSequenceProperty } ) );
            setStatistics( Arrays.asList( new QMFStatistic[] {  } ) );
            setMethods( Arrays.asList( new QMFMethod[] {  } ) );
        }
        
        public ManagementSetupStateObject newInstance(final ManagementSetupStateDelegate delegate)
        {
            return new ManagementSetupStateObject(delegate);
        }
        
    }
    
    private final ManagementSetupStateClass _managementSetupStateClass = new ManagementSetupStateClass();
    
    public interface ManagementSetupStateDelegate extends QMFObject.Delegate
    {
        Long getObjectNum();
        Integer getBootSequence();
    }
    
    public final class ManagementSetupStateObject extends QMFObject<ManagementSetupStateClass, ManagementSetupStateDelegate>
    {
        protected ManagementSetupStateObject(ManagementSetupStateDelegate delegate)
        {
           super(delegate);
        }
        
        public ManagementSetupStateClass getQMFClass()
        {
            return _managementSetupStateClass;
        }
        
        public QMFCommand asConfigInfoCmd(long sampleTime) 
        {
            return new QMFManagementSetupStateConfigInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asInstrumentInfoCmd(long sampleTime) 
        {
           return new QMFManagementSetupStateInstrumentInfoCommand(this,sampleTime);
        }
        
        public QMFCommand asGetQueryResponseCmd(QMFGetQueryCommand queryCommand, long sampleTime) 
        {
            return new QMFManagementSetupStateGetQueryResponseCommand(this,queryCommand,sampleTime);
        }
    
    
        
        
            public Long getObjectNum()
            {
                return getDelegate().getObjectNum();
            }
            
            
        
            public Integer getBootSequence()
            {
                return getDelegate().getBootSequence();
            }
            
            
    }

    public final class QMFManagementSetupStateConfigInfoCommand extends QMFConfigInfoCommand<ManagementSetupStateObject>
    {
        
        protected QMFManagementSetupStateConfigInfoCommand(ManagementSetupStateObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
        
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeUint64( getObject().getObjectNum() );
                    
            encoder.writeUint16( getObject().getBootSequence() );
                    
        }
    }
    
    public final class QMFManagementSetupStateInstrumentInfoCommand extends QMFInstrumentInfoCommand<ManagementSetupStateObject>
    {
    
        protected QMFManagementSetupStateInstrumentInfoCommand(ManagementSetupStateObject object, long sampleTime)
        {
            super(object, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
        }
    }
    
    public final class QMFManagementSetupStateGetQueryResponseCommand extends QMFGetQueryResponseCommand<ManagementSetupStateObject>
    {
    
        protected QMFManagementSetupStateGetQueryResponseCommand(ManagementSetupStateObject object, QMFGetQueryCommand cmd, long sampleTime)
        {
            super(object, cmd, sampleTime);
        }
    
        @Override
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            
            encoder.writeUint64( getObject().getObjectNum() );
                    
            encoder.writeUint16( getObject().getBootSequence() );
                    
        }
    }
    
    
    


        
    public class ClientConnectEventClass extends QMFEventClass
    {

        
                
        public class RhostArg extends QMFProperty
        {
            private  RhostArg()
            {
                super( "rhost",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Address (i.e. DNS name, IP address, etc.) of a remotely connected host");    
                
            }
        }
        
        private final RhostArg _rhostArg = new RhostArg();
            
        public class UserArg extends QMFProperty
        {
            private  UserArg()
            {
                super( "user",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Authentication identity");    
                
            }
        }
        
        private final UserArg _userArg = new UserArg();
    
        
        private ClientConnectEventClass()
        {
            super("clientConnect",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _rhostArg, _userArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.INFORM;
        }
        
        public QMFEventCommand<ClientConnectEventClass> newEvent(String rhost, String user)
        {
            return new  ClientConnectEvent(rhost, user);
        }
        
        
        
    }
        
    private final ClientConnectEventClass _clientConnectEventClass = new ClientConnectEventClass();
        
    private final class ClientConnectEvent extends QMFEventCommand<ClientConnectEventClass>
    {
        
        private final String _rhost;
        private final String _user;
        
        private ClientConnectEvent(String rhost, String user)
        {
        
            _rhost = rhost;
            _user = user;        
        }
        
        public ClientConnectEventClass getEventClass()
        {
            return _clientConnectEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _rhost );
            encoder.writeStr8( _user );        
        
        }
    }
    
        
    public class ClientConnectFailEventClass extends QMFEventClass
    {

        
                
        public class RhostArg extends QMFProperty
        {
            private  RhostArg()
            {
                super( "rhost",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Address (i.e. DNS name, IP address, etc.) of a remotely connected host");    
                
            }
        }
        
        private final RhostArg _rhostArg = new RhostArg();
            
        public class UserArg extends QMFProperty
        {
            private  UserArg()
            {
                super( "user",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Authentication identity");    
                
            }
        }
        
        private final UserArg _userArg = new UserArg();
            
        public class ReasonArg extends QMFProperty
        {
            private  ReasonArg()
            {
                super( "reason",
                       QMFType.STR16,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Reason for a failure");    
                
            }
        }
        
        private final ReasonArg _reasonArg = new ReasonArg();
    
        
        private ClientConnectFailEventClass()
        {
            super("clientConnectFail",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _rhostArg, _userArg, _reasonArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.WARN;
        }
        
        public QMFEventCommand<ClientConnectFailEventClass> newEvent(String rhost, String user, String reason)
        {
            return new  ClientConnectFailEvent(rhost, user, reason);
        }
        
        
        
    }
        
    private final ClientConnectFailEventClass _clientConnectFailEventClass = new ClientConnectFailEventClass();
        
    private final class ClientConnectFailEvent extends QMFEventCommand<ClientConnectFailEventClass>
    {
        
        private final String _rhost;
        private final String _user;
        private final String _reason;
        
        private ClientConnectFailEvent(String rhost, String user, String reason)
        {
        
            _rhost = rhost;
            _user = user;
            _reason = reason;        
        }
        
        public ClientConnectFailEventClass getEventClass()
        {
            return _clientConnectFailEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _rhost );
            encoder.writeStr8( _user );
            encoder.writeStr16( _reason );        
        
        }
    }
    
        
    public class ClientDisconnectEventClass extends QMFEventClass
    {

        
                
        public class RhostArg extends QMFProperty
        {
            private  RhostArg()
            {
                super( "rhost",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Address (i.e. DNS name, IP address, etc.) of a remotely connected host");    
                
            }
        }
        
        private final RhostArg _rhostArg = new RhostArg();
            
        public class UserArg extends QMFProperty
        {
            private  UserArg()
            {
                super( "user",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Authentication identity");    
                
            }
        }
        
        private final UserArg _userArg = new UserArg();
    
        
        private ClientDisconnectEventClass()
        {
            super("clientDisconnect",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _rhostArg, _userArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.INFORM;
        }
        
        public QMFEventCommand<ClientDisconnectEventClass> newEvent(String rhost, String user)
        {
            return new  ClientDisconnectEvent(rhost, user);
        }
        
        
        
    }
        
    private final ClientDisconnectEventClass _clientDisconnectEventClass = new ClientDisconnectEventClass();
        
    private final class ClientDisconnectEvent extends QMFEventCommand<ClientDisconnectEventClass>
    {
        
        private final String _rhost;
        private final String _user;
        
        private ClientDisconnectEvent(String rhost, String user)
        {
        
            _rhost = rhost;
            _user = user;        
        }
        
        public ClientDisconnectEventClass getEventClass()
        {
            return _clientDisconnectEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _rhost );
            encoder.writeStr8( _user );        
        
        }
    }
    
        
    public class BrokerLinkUpEventClass extends QMFEventClass
    {

        
                
        public class RhostArg extends QMFProperty
        {
            private  RhostArg()
            {
                super( "rhost",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Address (i.e. DNS name, IP address, etc.) of a remotely connected host");    
                
            }
        }
        
        private final RhostArg _rhostArg = new RhostArg();
    
        
        private BrokerLinkUpEventClass()
        {
            super("brokerLinkUp",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _rhostArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.INFORM;
        }
        
        public QMFEventCommand<BrokerLinkUpEventClass> newEvent(String rhost)
        {
            return new  BrokerLinkUpEvent(rhost);
        }
        
        
        
    }
        
    private final BrokerLinkUpEventClass _brokerLinkUpEventClass = new BrokerLinkUpEventClass();
        
    private final class BrokerLinkUpEvent extends QMFEventCommand<BrokerLinkUpEventClass>
    {
        
        private final String _rhost;
        
        private BrokerLinkUpEvent(String rhost)
        {
        
            _rhost = rhost;        
        }
        
        public BrokerLinkUpEventClass getEventClass()
        {
            return _brokerLinkUpEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _rhost );        
        
        }
    }
    
        
    public class BrokerLinkDownEventClass extends QMFEventClass
    {

        
                
        public class RhostArg extends QMFProperty
        {
            private  RhostArg()
            {
                super( "rhost",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Address (i.e. DNS name, IP address, etc.) of a remotely connected host");    
                
            }
        }
        
        private final RhostArg _rhostArg = new RhostArg();
    
        
        private BrokerLinkDownEventClass()
        {
            super("brokerLinkDown",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _rhostArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.WARN;
        }
        
        public QMFEventCommand<BrokerLinkDownEventClass> newEvent(String rhost)
        {
            return new  BrokerLinkDownEvent(rhost);
        }
        
        
        
    }
        
    private final BrokerLinkDownEventClass _brokerLinkDownEventClass = new BrokerLinkDownEventClass();
        
    private final class BrokerLinkDownEvent extends QMFEventCommand<BrokerLinkDownEventClass>
    {
        
        private final String _rhost;
        
        private BrokerLinkDownEvent(String rhost)
        {
        
            _rhost = rhost;        
        }
        
        public BrokerLinkDownEventClass getEventClass()
        {
            return _brokerLinkDownEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _rhost );        
        
        }
    }
    
        
    public class QueueDeclareEventClass extends QMFEventClass
    {

        
                
        public class RhostArg extends QMFProperty
        {
            private  RhostArg()
            {
                super( "rhost",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Address (i.e. DNS name, IP address, etc.) of a remotely connected host");    
                
            }
        }
        
        private final RhostArg _rhostArg = new RhostArg();
            
        public class UserArg extends QMFProperty
        {
            private  UserArg()
            {
                super( "user",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Authentication identity");    
                
            }
        }
        
        private final UserArg _userArg = new UserArg();
            
        public class QNameArg extends QMFProperty
        {
            private  QNameArg()
            {
                super( "qName",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Name of a queue");    
                
            }
        }
        
        private final QNameArg _qNameArg = new QNameArg();
            
        public class DurableArg extends QMFProperty
        {
            private  DurableArg()
            {
                super( "durable",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Created object is durable");    
                
            }
        }
        
        private final DurableArg _durableArg = new DurableArg();
            
        public class ExclArg extends QMFProperty
        {
            private  ExclArg()
            {
                super( "excl",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Created object is exclusive for the use of the owner only");    
                
            }
        }
        
        private final ExclArg _exclArg = new ExclArg();
            
        public class AutoDelArg extends QMFProperty
        {
            private  AutoDelArg()
            {
                super( "autoDel",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Created object is automatically deleted when no longer in use");    
                
            }
        }
        
        private final AutoDelArg _autoDelArg = new AutoDelArg();
            
        public class ArgsArg extends QMFProperty
        {
            private  ArgsArg()
            {
                super( "args",
                       QMFType.MAP,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Supplemental arguments or parameters supplied");    
                
            }
        }
        
        private final ArgsArg _argsArg = new ArgsArg();
            
        public class DispArg extends QMFProperty
        {
            private  DispArg()
            {
                super( "disp",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Disposition of a declaration: 'created' if object was created, 'existing' if object already existed");    
                
            }
        }
        
        private final DispArg _dispArg = new DispArg();
    
        
        private QueueDeclareEventClass()
        {
            super("queueDeclare",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _rhostArg, _userArg, _qNameArg, _durableArg, _exclArg, _autoDelArg, _argsArg, _dispArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.INFORM;
        }
        
        public QMFEventCommand<QueueDeclareEventClass> newEvent(String rhost, String user, String qName, Boolean durable, Boolean excl, Boolean autoDel, Map args, String disp)
        {
            return new  QueueDeclareEvent(rhost, user, qName, durable, excl, autoDel, args, disp);
        }
        
        
        
    }
        
    private final QueueDeclareEventClass _queueDeclareEventClass = new QueueDeclareEventClass();
        
    private final class QueueDeclareEvent extends QMFEventCommand<QueueDeclareEventClass>
    {
        
        private final String _rhost;
        private final String _user;
        private final String _qName;
        private final Boolean _durable;
        private final Boolean _excl;
        private final Boolean _autoDel;
        private final Map _args;
        private final String _disp;
        
        private QueueDeclareEvent(String rhost, String user, String qName, Boolean durable, Boolean excl, Boolean autoDel, Map args, String disp)
        {
        
            _rhost = rhost;
            _user = user;
            _qName = qName;
            _durable = durable;
            _excl = excl;
            _autoDel = autoDel;
            _args = args;
            _disp = disp;        
        }
        
        public QueueDeclareEventClass getEventClass()
        {
            return _queueDeclareEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _rhost );
            encoder.writeStr8( _user );
            encoder.writeStr8( _qName );
            encoder.writeInt8( _durable ? (byte) -1 : (byte) 0);
            encoder.writeInt8( _excl ? (byte) -1 : (byte) 0);
            encoder.writeInt8( _autoDel ? (byte) -1 : (byte) 0);
            encoder.writeMap( _args );
            encoder.writeStr8( _disp );        
        
        }
    }
    
        
    public class QueueDeleteEventClass extends QMFEventClass
    {

        
                
        public class RhostArg extends QMFProperty
        {
            private  RhostArg()
            {
                super( "rhost",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Address (i.e. DNS name, IP address, etc.) of a remotely connected host");    
                
            }
        }
        
        private final RhostArg _rhostArg = new RhostArg();
            
        public class UserArg extends QMFProperty
        {
            private  UserArg()
            {
                super( "user",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Authentication identity");    
                
            }
        }
        
        private final UserArg _userArg = new UserArg();
            
        public class QNameArg extends QMFProperty
        {
            private  QNameArg()
            {
                super( "qName",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Name of a queue");    
                
            }
        }
        
        private final QNameArg _qNameArg = new QNameArg();
    
        
        private QueueDeleteEventClass()
        {
            super("queueDelete",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _rhostArg, _userArg, _qNameArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.INFORM;
        }
        
        public QMFEventCommand<QueueDeleteEventClass> newEvent(String rhost, String user, String qName)
        {
            return new  QueueDeleteEvent(rhost, user, qName);
        }
        
        
        
    }
        
    private final QueueDeleteEventClass _queueDeleteEventClass = new QueueDeleteEventClass();
        
    private final class QueueDeleteEvent extends QMFEventCommand<QueueDeleteEventClass>
    {
        
        private final String _rhost;
        private final String _user;
        private final String _qName;
        
        private QueueDeleteEvent(String rhost, String user, String qName)
        {
        
            _rhost = rhost;
            _user = user;
            _qName = qName;        
        }
        
        public QueueDeleteEventClass getEventClass()
        {
            return _queueDeleteEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _rhost );
            encoder.writeStr8( _user );
            encoder.writeStr8( _qName );        
        
        }
    }
    
        
    public class ExchangeDeclareEventClass extends QMFEventClass
    {

        
                
        public class RhostArg extends QMFProperty
        {
            private  RhostArg()
            {
                super( "rhost",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Address (i.e. DNS name, IP address, etc.) of a remotely connected host");    
                
            }
        }
        
        private final RhostArg _rhostArg = new RhostArg();
            
        public class UserArg extends QMFProperty
        {
            private  UserArg()
            {
                super( "user",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Authentication identity");    
                
            }
        }
        
        private final UserArg _userArg = new UserArg();
            
        public class ExNameArg extends QMFProperty
        {
            private  ExNameArg()
            {
                super( "exName",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Name of an exchange");    
                
            }
        }
        
        private final ExNameArg _exNameArg = new ExNameArg();
            
        public class ExTypeArg extends QMFProperty
        {
            private  ExTypeArg()
            {
                super( "exType",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Type of an exchange");    
                
            }
        }
        
        private final ExTypeArg _exTypeArg = new ExTypeArg();
            
        public class AltExArg extends QMFProperty
        {
            private  AltExArg()
            {
                super( "altEx",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Name of the alternate exchange");    
                
            }
        }
        
        private final AltExArg _altExArg = new AltExArg();
            
        public class DurableArg extends QMFProperty
        {
            private  DurableArg()
            {
                super( "durable",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Created object is durable");    
                
            }
        }
        
        private final DurableArg _durableArg = new DurableArg();
            
        public class AutoDelArg extends QMFProperty
        {
            private  AutoDelArg()
            {
                super( "autoDel",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Created object is automatically deleted when no longer in use");    
                
            }
        }
        
        private final AutoDelArg _autoDelArg = new AutoDelArg();
            
        public class ArgsArg extends QMFProperty
        {
            private  ArgsArg()
            {
                super( "args",
                       QMFType.MAP,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Supplemental arguments or parameters supplied");    
                
            }
        }
        
        private final ArgsArg _argsArg = new ArgsArg();
            
        public class DispArg extends QMFProperty
        {
            private  DispArg()
            {
                super( "disp",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Disposition of a declaration: 'created' if object was created, 'existing' if object already existed");    
                
            }
        }
        
        private final DispArg _dispArg = new DispArg();
    
        
        private ExchangeDeclareEventClass()
        {
            super("exchangeDeclare",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _rhostArg, _userArg, _exNameArg, _exTypeArg, _altExArg, _durableArg, _autoDelArg, _argsArg, _dispArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.INFORM;
        }
        
        public QMFEventCommand<ExchangeDeclareEventClass> newEvent(String rhost, String user, String exName, String exType, String altEx, Boolean durable, Boolean autoDel, Map args, String disp)
        {
            return new  ExchangeDeclareEvent(rhost, user, exName, exType, altEx, durable, autoDel, args, disp);
        }
        
        
        
    }
        
    private final ExchangeDeclareEventClass _exchangeDeclareEventClass = new ExchangeDeclareEventClass();
        
    private final class ExchangeDeclareEvent extends QMFEventCommand<ExchangeDeclareEventClass>
    {
        
        private final String _rhost;
        private final String _user;
        private final String _exName;
        private final String _exType;
        private final String _altEx;
        private final Boolean _durable;
        private final Boolean _autoDel;
        private final Map _args;
        private final String _disp;
        
        private ExchangeDeclareEvent(String rhost, String user, String exName, String exType, String altEx, Boolean durable, Boolean autoDel, Map args, String disp)
        {
        
            _rhost = rhost;
            _user = user;
            _exName = exName;
            _exType = exType;
            _altEx = altEx;
            _durable = durable;
            _autoDel = autoDel;
            _args = args;
            _disp = disp;        
        }
        
        public ExchangeDeclareEventClass getEventClass()
        {
            return _exchangeDeclareEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _rhost );
            encoder.writeStr8( _user );
            encoder.writeStr8( _exName );
            encoder.writeStr8( _exType );
            encoder.writeStr8( _altEx );
            encoder.writeInt8( _durable ? (byte) -1 : (byte) 0);
            encoder.writeInt8( _autoDel ? (byte) -1 : (byte) 0);
            encoder.writeMap( _args );
            encoder.writeStr8( _disp );        
        
        }
    }
    
        
    public class ExchangeDeleteEventClass extends QMFEventClass
    {

        
                
        public class RhostArg extends QMFProperty
        {
            private  RhostArg()
            {
                super( "rhost",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Address (i.e. DNS name, IP address, etc.) of a remotely connected host");    
                
            }
        }
        
        private final RhostArg _rhostArg = new RhostArg();
            
        public class UserArg extends QMFProperty
        {
            private  UserArg()
            {
                super( "user",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Authentication identity");    
                
            }
        }
        
        private final UserArg _userArg = new UserArg();
            
        public class ExNameArg extends QMFProperty
        {
            private  ExNameArg()
            {
                super( "exName",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Name of an exchange");    
                
            }
        }
        
        private final ExNameArg _exNameArg = new ExNameArg();
    
        
        private ExchangeDeleteEventClass()
        {
            super("exchangeDelete",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _rhostArg, _userArg, _exNameArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.INFORM;
        }
        
        public QMFEventCommand<ExchangeDeleteEventClass> newEvent(String rhost, String user, String exName)
        {
            return new  ExchangeDeleteEvent(rhost, user, exName);
        }
        
        
        
    }
        
    private final ExchangeDeleteEventClass _exchangeDeleteEventClass = new ExchangeDeleteEventClass();
        
    private final class ExchangeDeleteEvent extends QMFEventCommand<ExchangeDeleteEventClass>
    {
        
        private final String _rhost;
        private final String _user;
        private final String _exName;
        
        private ExchangeDeleteEvent(String rhost, String user, String exName)
        {
        
            _rhost = rhost;
            _user = user;
            _exName = exName;        
        }
        
        public ExchangeDeleteEventClass getEventClass()
        {
            return _exchangeDeleteEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _rhost );
            encoder.writeStr8( _user );
            encoder.writeStr8( _exName );        
        
        }
    }
    
        
    public class BindEventClass extends QMFEventClass
    {

        
                
        public class RhostArg extends QMFProperty
        {
            private  RhostArg()
            {
                super( "rhost",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Address (i.e. DNS name, IP address, etc.) of a remotely connected host");    
                
            }
        }
        
        private final RhostArg _rhostArg = new RhostArg();
            
        public class UserArg extends QMFProperty
        {
            private  UserArg()
            {
                super( "user",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Authentication identity");    
                
            }
        }
        
        private final UserArg _userArg = new UserArg();
            
        public class ExNameArg extends QMFProperty
        {
            private  ExNameArg()
            {
                super( "exName",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Name of an exchange");    
                
            }
        }
        
        private final ExNameArg _exNameArg = new ExNameArg();
            
        public class QNameArg extends QMFProperty
        {
            private  QNameArg()
            {
                super( "qName",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Name of a queue");    
                
            }
        }
        
        private final QNameArg _qNameArg = new QNameArg();
            
        public class KeyArg extends QMFProperty
        {
            private  KeyArg()
            {
                super( "key",
                       QMFType.STR16,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Key text used for routing or binding");    
                
            }
        }
        
        private final KeyArg _keyArg = new KeyArg();
            
        public class ArgsArg extends QMFProperty
        {
            private  ArgsArg()
            {
                super( "args",
                       QMFType.MAP,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Supplemental arguments or parameters supplied");    
                
            }
        }
        
        private final ArgsArg _argsArg = new ArgsArg();
    
        
        private BindEventClass()
        {
            super("bind",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _rhostArg, _userArg, _exNameArg, _qNameArg, _keyArg, _argsArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.INFORM;
        }
        
        public QMFEventCommand<BindEventClass> newEvent(String rhost, String user, String exName, String qName, String key, Map args)
        {
            return new  BindEvent(rhost, user, exName, qName, key, args);
        }
        
        
        
    }
        
    private final BindEventClass _bindEventClass = new BindEventClass();
        
    private final class BindEvent extends QMFEventCommand<BindEventClass>
    {
        
        private final String _rhost;
        private final String _user;
        private final String _exName;
        private final String _qName;
        private final String _key;
        private final Map _args;
        
        private BindEvent(String rhost, String user, String exName, String qName, String key, Map args)
        {
        
            _rhost = rhost;
            _user = user;
            _exName = exName;
            _qName = qName;
            _key = key;
            _args = args;        
        }
        
        public BindEventClass getEventClass()
        {
            return _bindEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _rhost );
            encoder.writeStr8( _user );
            encoder.writeStr8( _exName );
            encoder.writeStr8( _qName );
            encoder.writeStr16( _key );
            encoder.writeMap( _args );        
        
        }
    }
    
        
    public class UnbindEventClass extends QMFEventClass
    {

        
                
        public class RhostArg extends QMFProperty
        {
            private  RhostArg()
            {
                super( "rhost",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Address (i.e. DNS name, IP address, etc.) of a remotely connected host");    
                
            }
        }
        
        private final RhostArg _rhostArg = new RhostArg();
            
        public class UserArg extends QMFProperty
        {
            private  UserArg()
            {
                super( "user",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Authentication identity");    
                
            }
        }
        
        private final UserArg _userArg = new UserArg();
            
        public class ExNameArg extends QMFProperty
        {
            private  ExNameArg()
            {
                super( "exName",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Name of an exchange");    
                
            }
        }
        
        private final ExNameArg _exNameArg = new ExNameArg();
            
        public class QNameArg extends QMFProperty
        {
            private  QNameArg()
            {
                super( "qName",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Name of a queue");    
                
            }
        }
        
        private final QNameArg _qNameArg = new QNameArg();
            
        public class KeyArg extends QMFProperty
        {
            private  KeyArg()
            {
                super( "key",
                       QMFType.STR16,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Key text used for routing or binding");    
                
            }
        }
        
        private final KeyArg _keyArg = new KeyArg();
    
        
        private UnbindEventClass()
        {
            super("unbind",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _rhostArg, _userArg, _exNameArg, _qNameArg, _keyArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.INFORM;
        }
        
        public QMFEventCommand<UnbindEventClass> newEvent(String rhost, String user, String exName, String qName, String key)
        {
            return new  UnbindEvent(rhost, user, exName, qName, key);
        }
        
        
        
    }
        
    private final UnbindEventClass _unbindEventClass = new UnbindEventClass();
        
    private final class UnbindEvent extends QMFEventCommand<UnbindEventClass>
    {
        
        private final String _rhost;
        private final String _user;
        private final String _exName;
        private final String _qName;
        private final String _key;
        
        private UnbindEvent(String rhost, String user, String exName, String qName, String key)
        {
        
            _rhost = rhost;
            _user = user;
            _exName = exName;
            _qName = qName;
            _key = key;        
        }
        
        public UnbindEventClass getEventClass()
        {
            return _unbindEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _rhost );
            encoder.writeStr8( _user );
            encoder.writeStr8( _exName );
            encoder.writeStr8( _qName );
            encoder.writeStr16( _key );        
        
        }
    }
    
        
    public class SubscribeEventClass extends QMFEventClass
    {

        
                
        public class RhostArg extends QMFProperty
        {
            private  RhostArg()
            {
                super( "rhost",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Address (i.e. DNS name, IP address, etc.) of a remotely connected host");    
                
            }
        }
        
        private final RhostArg _rhostArg = new RhostArg();
            
        public class UserArg extends QMFProperty
        {
            private  UserArg()
            {
                super( "user",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Authentication identity");    
                
            }
        }
        
        private final UserArg _userArg = new UserArg();
            
        public class QNameArg extends QMFProperty
        {
            private  QNameArg()
            {
                super( "qName",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Name of a queue");    
                
            }
        }
        
        private final QNameArg _qNameArg = new QNameArg();
            
        public class DestArg extends QMFProperty
        {
            private  DestArg()
            {
                super( "dest",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Destination tag for a subscription");    
                
            }
        }
        
        private final DestArg _destArg = new DestArg();
            
        public class ExclArg extends QMFProperty
        {
            private  ExclArg()
            {
                super( "excl",
                       QMFType.BOOLEAN,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Created object is exclusive for the use of the owner only");    
                
            }
        }
        
        private final ExclArg _exclArg = new ExclArg();
            
        public class ArgsArg extends QMFProperty
        {
            private  ArgsArg()
            {
                super( "args",
                       QMFType.MAP,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Supplemental arguments or parameters supplied");    
                
            }
        }
        
        private final ArgsArg _argsArg = new ArgsArg();
    
        
        private SubscribeEventClass()
        {
            super("subscribe",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _rhostArg, _userArg, _qNameArg, _destArg, _exclArg, _argsArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.INFORM;
        }
        
        public QMFEventCommand<SubscribeEventClass> newEvent(String rhost, String user, String qName, String dest, Boolean excl, Map args)
        {
            return new  SubscribeEvent(rhost, user, qName, dest, excl, args);
        }
        
        
        
    }
        
    private final SubscribeEventClass _subscribeEventClass = new SubscribeEventClass();
        
    private final class SubscribeEvent extends QMFEventCommand<SubscribeEventClass>
    {
        
        private final String _rhost;
        private final String _user;
        private final String _qName;
        private final String _dest;
        private final Boolean _excl;
        private final Map _args;
        
        private SubscribeEvent(String rhost, String user, String qName, String dest, Boolean excl, Map args)
        {
        
            _rhost = rhost;
            _user = user;
            _qName = qName;
            _dest = dest;
            _excl = excl;
            _args = args;        
        }
        
        public SubscribeEventClass getEventClass()
        {
            return _subscribeEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _rhost );
            encoder.writeStr8( _user );
            encoder.writeStr8( _qName );
            encoder.writeStr8( _dest );
            encoder.writeInt8( _excl ? (byte) -1 : (byte) 0);
            encoder.writeMap( _args );        
        
        }
    }
    
        
    public class UnsubscribeEventClass extends QMFEventClass
    {

        
                
        public class RhostArg extends QMFProperty
        {
            private  RhostArg()
            {
                super( "rhost",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Address (i.e. DNS name, IP address, etc.) of a remotely connected host");    
                
            }
        }
        
        private final RhostArg _rhostArg = new RhostArg();
            
        public class UserArg extends QMFProperty
        {
            private  UserArg()
            {
                super( "user",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Authentication identity");    
                
            }
        }
        
        private final UserArg _userArg = new UserArg();
            
        public class DestArg extends QMFProperty
        {
            private  DestArg()
            {
                super( "dest",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Destination tag for a subscription");    
                
            }
        }
        
        private final DestArg _destArg = new DestArg();
    
        
        private UnsubscribeEventClass()
        {
            super("unsubscribe",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _rhostArg, _userArg, _destArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.INFORM;
        }
        
        public QMFEventCommand<UnsubscribeEventClass> newEvent(String rhost, String user, String dest)
        {
            return new  UnsubscribeEvent(rhost, user, dest);
        }
        
        
        
    }
        
    private final UnsubscribeEventClass _unsubscribeEventClass = new UnsubscribeEventClass();
        
    private final class UnsubscribeEvent extends QMFEventCommand<UnsubscribeEventClass>
    {
        
        private final String _rhost;
        private final String _user;
        private final String _dest;
        
        private UnsubscribeEvent(String rhost, String user, String dest)
        {
        
            _rhost = rhost;
            _user = user;
            _dest = dest;        
        }
        
        public UnsubscribeEventClass getEventClass()
        {
            return _unsubscribeEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _rhost );
            encoder.writeStr8( _user );
            encoder.writeStr8( _dest );        
        
        }
    }
    
        
    public class QueueThresholdExceededEventClass extends QMFEventClass
    {

        
                
        public class QNameArg extends QMFProperty
        {
            private  QNameArg()
            {
                super( "qName",
                       QMFType.STR8,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Name of a queue");    
                
            }
        }
        
        private final QNameArg _qNameArg = new QNameArg();
            
        public class MsgDepthArg extends QMFProperty
        {
            private  MsgDepthArg()
            {
                super( "msgDepth",
                       QMFType.UINT32,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Current size of queue in messages");    
                
            }
        }
        
        private final MsgDepthArg _msgDepthArg = new MsgDepthArg();
            
        public class ByteDepthArg extends QMFProperty
        {
            private  ByteDepthArg()
            {
                super( "byteDepth",
                       QMFType.UINT32,
                       QMFProperty.AccessCode.RO,false,false);
                
                
                setDescription("Current size of queue in bytes");    
                
            }
        }
        
        private final ByteDepthArg _byteDepthArg = new ByteDepthArg();
    
        
        private QueueThresholdExceededEventClass()
        {
            super("queueThresholdExceeded",
            new byte[16]);
           
            setProperties( Arrays.asList( new QMFProperty[] { _qNameArg, _msgDepthArg, _byteDepthArg } ) );
        }
        
        public QMFEventSeverity getSeverity()
        {
            return QMFEventSeverity.WARN;
        }
        
        public QMFEventCommand<QueueThresholdExceededEventClass> newEvent(String qName, Long msgDepth, Long byteDepth)
        {
            return new  QueueThresholdExceededEvent(qName, msgDepth, byteDepth);
        }
        
        
        
    }
        
    private final QueueThresholdExceededEventClass _queueThresholdExceededEventClass = new QueueThresholdExceededEventClass();
        
    private final class QueueThresholdExceededEvent extends QMFEventCommand<QueueThresholdExceededEventClass>
    {
        
        private final String _qName;
        private final Long _msgDepth;
        private final Long _byteDepth;
        
        private QueueThresholdExceededEvent(String qName, Long msgDepth, Long byteDepth)
        {
        
            _qName = qName;
            _msgDepth = msgDepth;
            _byteDepth = byteDepth;        
        }
        
        public QueueThresholdExceededEventClass getEventClass()
        {
            return _queueThresholdExceededEventClass;
        }
        
        public void encode(final BBEncoder encoder)
        {
            super.encode(encoder);
            encoder.writeStr8( _qName );
            encoder.writeUint32( _msgDepth );
            encoder.writeUint32( _byteDepth );        
        
        }
    }
    

    private BrokerSchema()
    {
        super(SCHEMA_NAME);
        setClasses( Arrays.asList( new QMFClass[] { _systemClass, _brokerClass, _agentClass, _vhostClass, _queueClass, _exchangeClass, _bindingClass, _subscriptionClass, _connectionClass, _linkClass, _bridgeClass, _sessionClass, _managementSetupStateClass, _clientConnectEventClass, _clientConnectFailEventClass, _clientDisconnectEventClass, _brokerLinkUpEventClass, _brokerLinkDownEventClass, _queueDeclareEventClass, _queueDeleteEventClass, _exchangeDeclareEventClass, _exchangeDeleteEventClass, _bindEventClass, _unbindEventClass, _subscribeEventClass, _unsubscribeEventClass, _queueThresholdExceededEventClass } ) );
    }

    public <T extends QMFClass> T getQMFClassInstance(Class<T> clazz)
    {
        for(QMFClass c : getClasses())
        {
            if(clazz.isInstance(c))
            {
                return (T) c;
            }
        }
        return null;
    } 
    
    

    public static BrokerSchema getPackage()
    {
        return PACKAGE;
    }
    
}
