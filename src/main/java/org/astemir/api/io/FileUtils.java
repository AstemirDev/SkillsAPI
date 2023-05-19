package org.astemir.api.io;

import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;
import org.astemir.api.math.MathUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

    private static final int MAX_SKIP_BUFFER_SIZE = 2048;

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;

    public static InputStream inputStream(Class<?> classWorker,String path){
        return classWorker.getClassLoader().getResourceAsStream(path);
    }

    public static void writeText(File file, Charset charset, String text){
        try {
            FileWriter writer = new FileWriter(file,charset);
            writer.append(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String readText(InputStream input){
        try {
            String text = new String(bytes(input), StandardCharsets.UTF_8);
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] bytes(InputStream stream) throws IOException {
        int len = Integer.MAX_VALUE;
        List<byte[]> bufs = null;
        byte[] result = null;
        int total = 0;
        int remaining = len;
        int n;
        do {
            byte[] buf = new byte[Math.min(remaining, DEFAULT_BUFFER_SIZE)];
            int nread = 0;
            while ((n = stream.read(buf, nread,
                    Math.min(buf.length - nread, remaining))) > 0) {
                nread += n;
                remaining -= n;
            }
            if (nread > 0) {
                if (MAX_BUFFER_SIZE - total < nread) {
                    throw new OutOfMemoryError("Required array size too large");
                }
                if (nread < buf.length) {
                    buf = Arrays.copyOfRange(buf, 0, nread);
                }
                total += nread;
                if (result == null) {
                    result = buf;
                } else {
                    if (bufs == null) {
                        bufs = new ArrayList<>();
                        bufs.add(result);
                    }
                    bufs.add(buf);
                }
            }
        } while (n >= 0 && remaining > 0);

        if (bufs == null) {
            if (result == null) {
                return new byte[0];
            }
            return result.length == total ?
                    result : Arrays.copyOf(result, total);
        }
        result = new byte[total];
        int offset = 0;
        remaining = total;
        for (byte[] b : bufs) {
            int count = Math.min(b.length, remaining);
            System.arraycopy(b, 0, result, offset, count);
            offset += count;
            remaining -= count;
        }
        return result;
    }

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
