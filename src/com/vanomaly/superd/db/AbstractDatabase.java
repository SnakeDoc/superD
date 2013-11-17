/*******************************************************************************
 *  Copyright 2013 Jason Sipula                                                *
 *                                                                             *
 *  Licensed under the Apache License, Version 2.0 (the "License");            *
 *  you may not use this file except in compliance with the License.           *
 *  You may obtain a copy of the License at                                    *
 *                                                                             *
 *      http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                             *
 *  Unless required by applicable law or agreed to in writing, software        *
 *  distributed under the License is distributed on an "AS IS" BASIS,          *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 *  See the License for the specific language governing permissions and        *
 *  limitations under the License.                                             *
 *******************************************************************************/

package com.vanomaly.superd.db;

import java.sql.Connection;

/**
 * @author Jason Sipula
 *
 */
public abstract class AbstractDatabase implements Database {

     private Connection conn;
    
    final private String dbPath;
    final private int dbPort;
    final private String dbUser;
    final private String dbPass;
    
    @SuppressWarnings("unused")
    final private String driver;
    
    public AbstractDatabase(final String dbPath) {
        this.driver = null;
        this.dbPath = dbPath;
        this.dbPort = 0;
        this.dbUser = null;
        this.dbPass = null;
    }
    
    public AbstractDatabase(final String dbPath, final int dbPort) {
        this.driver = null;
        this.dbPath = dbPath;
        this.dbPort = dbPort;
        this.dbUser = null;
        this.dbPass = null;
    }
 
    public AbstractDatabase(final String dbPath, final String dbUser, final String dbPass) {
        this.driver = null;
        this.dbPath = dbPath;
        this.dbPort = 0;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }
    
    public AbstractDatabase(final String dbPath, final int dbPort, final String dbUser, final String dbPass) {
        this.driver = null;
        this.dbPath = dbPath;
        this.dbPort = dbPort;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }
    
    public AbstractDatabase(final String driver, final String dbPath, final int dbPort, final String dbUser, final String dbPass) {
        this.driver = driver;
        this.dbPath = dbPath;
        this.dbPort = dbPort;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }
    
    /* (non-Javadoc)
     * @see com.vanomaly.superd.db.Database#closeConnection()
     */
    @Override
    public void closeConnection() {
        try {
            this.conn.close();
        } catch (Exception e) {
            // don't care
        }
    }
    
    /* (non-Javadoc)
     * @see com.vanomaly.superd.db.Database#isClosed()
     */
    @Override
    public boolean isClosed() {
        try {
            return this.conn.isClosed();
        } catch (Exception e) {
            return true;
        }
    }
    
    /* (non-Javadoc)
     * @see com.vanomaly.superd.db.Database#getConnection()
     */
    @Override
    public Connection getConnection() {
        return this.conn;
    }
    
    public void setConnection(final Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Gets dbPath
     * 
     * @return Current dbPath value from instance
     */
    public String getDbPath() {
        return this.dbPath;
    }
    
    /**
     * Gets dbPort
     * 
     * @return Current dbPort value from instance
     */
    public int getDbPort() {
        return this.dbPort;
    }

    /**
     * Gets dbUser
     * 
     * @return Current dbUser from instance
     */
    public String getDbUser() {
        return this.dbUser;
    }
     
    /**
     * Gets dbPass New dbPass
     * 
     * @return Current dbPass from instance
     */
    public String getDbPass() {
        return this.dbPass;
    }
}
