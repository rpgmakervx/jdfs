package org.easyarch.jdfs.common.util;/**
 * Description : 
 * Created by YangZH on 16-7-4
 *  下午10:09
 */

import io.netty.buffer.ByteBuf;
import org.easyarch.jdfs.common.codec.Codec;
import org.easyarch.jdfs.common.constants.Const;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Description :
 * Created by YangZH on 16-7-4
 * 下午10:09
 */

public class FileUtil {

    public static void upload(ByteBuf data,String filepath) throws Exception{
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.readableBytes());
        data.getBytes(0, buffer);
        System.out.println("buffer size--> "+buffer.remaining());
        File target = new File(Const.DATA+File.separator+filepath);
        if (!target.getParentFile().exists()){
            target.getParentFile().mkdirs();
        }
        if (!target.exists()){
            target.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(target);
        FileChannel channel = fos.getChannel();
        System.out.println("is direct? --> " + buffer.isDirect());
        channel.write(buffer);
        channel.close();
    }

    public static void upload(byte[] data,String filepath){
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
        buffer.put(data);
        buffer.flip();
        try {
            File target = new File(Const.DATA+File.separator+filepath);
            if (!target.getParentFile().exists()){
                target.getParentFile().mkdirs();
            }
            if (!target.exists()){
                target.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(target);
            FileChannel channel = fos.getChannel();
            System.out.println("is direct? --> " + buffer.isDirect());
            channel.write(buffer);
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void upload(File file) throws Exception{
        FileInputStream fis = new FileInputStream(file);
        System.out.println("文件信息 --> "+file.getAbsolutePath());
        FileChannel inChannel = fis.getChannel();
        MappedByteBuffer buffer = inChannel.map(
                FileChannel.MapMode.READ_ONLY, 0, file.length());
        //文件转字节，计算文件哈希值
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes, 0, bytes.length);
        String hashcode = Codec.sha1(bytes);
        //文件的哈希值用来作为文件所在的文件夹，
        //保证文件名冲突但不一样的多个文件可以同时存在
        File target = new File(Const.DATA+hashcode+File.separator+file.getName());
        if (!target.getParentFile().exists()){
            target.getParentFile().mkdirs();
        }
        if (!target.exists()){
            target.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(target);
        FileChannel outChannel = fos.getChannel();
        outChannel.write(buffer);
        fos.close();
        fis.close();
        outChannel.close();
        inChannel.close();
    }

    public static byte[] file2Byte(File file) throws Exception{
        FileInputStream fis = new FileInputStream(file);
        FileChannel fileChannel = fis.getChannel();
        MappedByteBuffer buffer = fileChannel.map(
                FileChannel.MapMode.READ_ONLY, 0, file.length());
        System.out.println("buffer.remaining() --> "+buffer.remaining()+", buffer.capacity() --> "+buffer.capacity());
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes, 0, bytes.length);
        System.out.println("bytes --> "+bytes.length);
        return bytes;
    }

    public static boolean fileExists(String filepath){
        File file = new File(Const.HTML+filepath);
        System.out.println("资源路径 --> "+file.getAbsolutePath());
        return file.exists();
    }

    public static String getHtml(String filepath){
        File file = new File(Const.HTML+filepath);
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line=null;
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
