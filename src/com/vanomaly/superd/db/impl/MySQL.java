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

package com.vanomaly.superd.db.impl;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.vanomaly.superd.Config;
import com.vanomaly.superd.db.AbstractDatabase;

/**
 * @author Jason Sipula
 *
 */
public class MySQL extends AbstractDatabase {
    
    private static class LazyLoader {
        private static final MySQL INSTANCE = new MySQL();
    }
    
    public static MySQL getInstance() {
        return LazyLoader.INSTANCE;
    }

    private MySQL() {
        super(Config.DATABASE.getString("database.path"), 
                Config.DATABASE.getInteger("database.port"), 
                Config.DATABASE.getString("database.user"), 
                Config.DATABASE.getString("database.pass"));
    }
    
    /* (non-Javadoc)
     * @see com.vanomaly.superd.db.Database#openConnection()
     */
    @Override
    public void openConnection() {
        if (getConnection() == null) {
            try {
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.getDbPath() + ":" + new Integer(this.getDbPort()), this.getDbUser(), this.getDbPass()));
            } catch (SQLException e) {
                // TODO log this and perhaps gracefully exit program
                e.printStackTrace();
            }
        }
        
    }
}
