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

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.vanomaly.superd.Config;
import com.vanomaly.superd.controller.MainWindowController;

/**
 * @author Jason Sipula
 *
 */
public class FileScanner extends SimpleFileVisitor<Path> {
    
    final MessageDigest md;
    final ByteBuffer bbf;
    FileInputStream fis;
    FileChannel fc;
    StringBuilder hexString;
    int b;
    
    public FileScanner(final String hashAlgo) throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance(hashAlgo);
        bbf = ByteBuffer.allocateDirect(Config.SUPERD.getInteger("buffer.size"));
    }
    
    // hash all regular files
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attr) {
        try {
            Thread.sleep(10);
            fis = new FileInputStream(file.toString());
            fc = fis.getChannel();
            b = fc.read(bbf);
            while ((b != -1) && (b != 0)) {
                bbf.flip();
                byte[] bytes = new byte[b];
                bbf.get(bytes);
                md.update(bytes, 0, b);
                bbf.clear();
                b = fc.read(bbf);
            }
            fis.close();
            byte[] mdbytes = md.digest();
            hexString = new StringBuilder();
            for (int i = 0; i < mdbytes.length; i++) {
                hexString.append(Integer.toHexString((0xFF & mdbytes[i])));
            }
            MainWindowController.getInstance().addTableRow(
                    new SimpleFileProperty(file.toString(), 
                            hexString.toString(), 
                            attr.size()
                            ));  
        } catch (InterruptedException | IOException e) {
            return FileVisitResult.CONTINUE;
        }
        return FileVisitResult.CONTINUE;
    }

    // on exception, just continue crawling
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException e) {
        return FileVisitResult.CONTINUE;
    }
}
