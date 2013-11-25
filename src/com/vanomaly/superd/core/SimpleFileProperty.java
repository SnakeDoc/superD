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

package com.vanomaly.superd.core;

import java.math.BigDecimal;

import com.vanomaly.superd.Config;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author Jason Sipula
 *
 */
public class SimpleFileProperty {
    private final SimpleStringProperty path;
    private final SimpleStringProperty hash;
    private final SimpleStringProperty size;
    
    // for database storage
    private final SimpleLongProperty fbytes;
    
    public SimpleFileProperty(final String path, final String hash, final Long size) {
        this.path = new SimpleStringProperty(path);
        this.hash = new SimpleStringProperty(hash);
        this.fbytes = new SimpleLongProperty(size);
        this.size = new SimpleStringProperty(formatSize(size));
    }
    
    public String getPath() {
        return this.path.get();
    }
    
    public String getHash() {
        return this.hash.get();
    }
    
    public Long getBytes() {
        return this.fbytes.getValue();
    }
    
    public String getSize() {
        return this.size.get();
    }
    
    private String formatSize(final Long size) {
        return String.format("%,." 
                + Config.SUPERD.getInteger("file.size.precision") 
                + "f %s", convertSize(size), Config.SUPERD.getString("file.size.notation"));
    }
    
    private BigDecimal convertSize(final Long size) {
        switch (Config.SUPERD.getString("file.size.notation")) {
        
        case "B":
            return new BigDecimal(size)
                .setScale(Config.SUPERD.getInteger("file.size.precision"));
        case "KB":
            return new BigDecimal(size).divide(new BigDecimal(1024.00))
                    .setScale(Config.SUPERD.getInteger("file.size.precision"), BigDecimal.ROUND_HALF_UP);
        case "MB":
            return (new BigDecimal(size)
                .divide(new BigDecimal(1024.00))).divide(new BigDecimal(1024.00))
                    .setScale(Config.SUPERD.getInteger("file.size.precision"), BigDecimal.ROUND_HALF_UP);
        case "GB":
            return ((new BigDecimal(size)
            .divide(new BigDecimal(1024.00))).divide(new BigDecimal(1024.00)).divide(new BigDecimal(1024.00)))
                .setScale(Config.SUPERD.getInteger("file.size.precision"), BigDecimal.ROUND_HALF_UP);
        case "TB":
            return ((new BigDecimal(size)
            .divide(new BigDecimal(1024.00))).divide(new BigDecimal(1024.00)).divide(new BigDecimal(1024.00)))
                .setScale(Config.SUPERD.getInteger("file.size.precision"), BigDecimal.ROUND_HALF_UP);
        default:
            return new BigDecimal(size).divide(new BigDecimal(1024.00))
                    .setScale(Config.SUPERD.getInteger("file.size.precision"), BigDecimal.ROUND_HALF_UP);
        }
    }
}
