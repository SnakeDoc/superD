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
import java.sql.SQLException;

/**
 * @author Jason Sipula
 *
 */
public interface Database {
    
    /**
     * Open Database Connection
     */
    public void openConnection();
    
    /**
     * Close Database Connection
     */
    public void closeConnection();
    
    /**
     * Tests if Database Connection is Open
     * 
     * @return True if database connection is open.
     * @throws SQLException 
     */
    public boolean isClosed();
    
    /**
     * Returns this instance's database connection.
     * 
     * @return Returns this instance's database connection.
     */
    public Connection getConnection();
    
}
