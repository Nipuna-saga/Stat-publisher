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

package org.wso2.andes.client;

public class SSLConfiguration {
	
	private String _keystorePath;
	
	private String _keystorePassword;

    private String trustStorePath;

    private String trustStorePassword;

    private String sslCertAlias;
	
	private String _certType = "SunX509";
	
	public void setKeystorePath(String path) 
	{
		_keystorePath = path;
	}
	
	public String getKeystorePath() 
	{
		return _keystorePath;
	}
	
	public void setKeystorePassword(String password) 
	{
		_keystorePassword = password;
	}
	
	public String getKeystorePassword() 
	{
		return _keystorePassword;
	}
	
	public void setCertType(String type) 
	{
		_certType = type;
	}
	
	public String getCertType() 
	{
		return _certType;
	}

    public String get_keystorePath() {
        return _keystorePath;
    }

    public void set_keystorePath(String _keystorePath) {
        this._keystorePath = _keystorePath;
    }

    public String get_keystorePassword() {
        return _keystorePassword;
    }

    public void set_keystorePassword(String _keystorePassword) {
        this._keystorePassword = _keystorePassword;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public String getSslCertAlias() {
        return sslCertAlias;
    }

    public void setSslCertAlias(String sslCertAlias) {
        this.sslCertAlias = sslCertAlias;
    }

}
