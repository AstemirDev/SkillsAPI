package org.astemir.api.utils;

import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

public class FileUtils {

    public static InputStream getResource(String path){
        if (path.contains(":")) {
            String modId = StringUtils.split(path, ":")[0];
            String newPath = StringUtils.split(path, ":")[1];
            return getResource(ModList.get().getModObjectById(modId).get().getClass(),newPath);
        }
        return null;
    }

    public static InputStream getResource(Class<?> className,String path){
        try {
            InputStream stream = className.getClassLoader().getResourceAsStream(path);
            return stream;
        }catch (Exception e){
            return null;
        }
    }
}
