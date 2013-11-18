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

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author Jason Sipula
 *
 */
public class SimpleFileProperty {
    private final SimpleStringProperty path;
    private final SimpleStringProperty hash;
    private final SimpleDoubleProperty size;
    
    public SimpleFileProperty(final String path, final String hash, final double size) {
        this.path = new SimpleStringProperty(path);
        this.hash = new SimpleStringProperty(hash);
        this.size = new SimpleDoubleProperty(size);
    }
    
    public String getPath() {
        return this.path.get();
    }
    
    public String getHash() {
        return this.hash.get();
    }
    
    public Double getSize() {
        return this.size.getValue();
    }
    
}
