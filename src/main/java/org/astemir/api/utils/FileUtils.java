package org.astemir.api.utils;

import java.io.InputStream;

public class FileUtils {

    public InputStream getResource(Class<?> className,String path){
        try {
            InputStream stream = className.getResourceAsStream(path);
            return stream;
        }catch (Exception e){
            return null;
        }
    }
}
