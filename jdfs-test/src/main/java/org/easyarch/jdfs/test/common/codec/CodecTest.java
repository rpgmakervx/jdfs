package org.easyarch.jdfs.test.common.codec;/**
 * Description : 
 * Created by YangZH on 16-7-3
 *  下午8:38
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.easyarch.jdfs.common.codec.Codec;
import org.easyarch.jdfs.common.util.FileUtil;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Description :
 * Created by YangZH on 16-7-3
 * 下午8:38
 */

public class CodecTest {

    @Test
    public void testHash(){
        System.out.println(Codec.hash("123.150.44.10"));
    }
    @Test
    public void testSHA1(){
        System.out.println(Codec.sha1("123.150.44.10".getBytes()));
    }

    @Test
    public void fileHash() throws IOException {
        File file = new File("/home/code4j/util/apache-tomcat-7.0.40-windows-x64.zip");
        FileInputStream fis = new FileInputStream(file);
        FileChannel fileChannel = fis.getChannel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        byte[] bytes = new byte[1024];
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int len = 0;
        while ((len = fis.read(bytes)) != -1){
            baos.write(bytes, 0, len);
        }
        System.out.println(fileChannel.size());

    }

    @Test
    public void fileChannelTest() throws Exception {
        File file = new File("/home/code4j/apache-tomcat-7.0.40-windows-x64.zip");
        FileInputStream fis = new FileInputStream(file);
        FileChannel fileChannel = fis.getChannel();
        MappedByteBuffer buffer = fileChannel.map(
                FileChannel.MapMode.READ_ONLY, 0, file.length());
        System.out.println("buffer.remaining() --> "+buffer.remaining()+", buffer.capacity() --> "+buffer.capacity());
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes,0,bytes.length);
        System.out.println("bytes --> "+bytes.length);
        System.out.println(Codec.sha1(bytes));
    }

    @Test
    public void avaliableTest() throws Exception {
//        System.out.println("bio --> "+copy("ubuntu" + 1));

        for(int index=1;index<=5;index++){
            final int finalIndex = index;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("nio --> "+nioCopy("ubuntu" + finalIndex));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        System.in.read();
    }

    public long copy(String filename) throws Exception{
        long begin = System.currentTimeMillis();
        File file = new File("/home/code4j/util/ubuntu.iso");
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream("/home/code4j/test/"+filename+".iso");
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = fis.read(bytes)) != -1){
            fos.write(bytes, 0, len);
            fos.flush();
        }
        fos.close();
        fis.close();
        System.out.println("end"+filename);
        return System.currentTimeMillis() - begin;
    }

    public long nioCopy(String filename) throws Exception {
        long begin = System.currentTimeMillis();
        File file = new File("/home/code4j/util/"+filename+".iso");
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream("/home/code4j/"+filename+".iso");
        FileChannel inChannel = fis.getChannel();
        MappedByteBuffer buffer = inChannel.map(
                FileChannel.MapMode.READ_ONLY, 0, file.length());
        System.out.println("is direct? --> "+buffer.isDirect());
        FileChannel outChannel = fos.getChannel();
        outChannel.write(buffer);
        fos.close();
        fis.close();
        outChannel.close();
        inChannel.close();
        System.out.println("end"+filename);
        return System.currentTimeMillis() - begin;
    }

    @Test
    public void testFileUpload() throws Exception {
        File file = new File("/home/code4j/picture/壁纸/【1920X1200】【7P】程序员专用励志文字高清桌面壁纸/01.jpeg");
        byte[] bytes = FileUtil.file2Byte(file);
//        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
//        byte[] bytes = new byte[bis.available()];
//        bis.read(bytes);
//        byte[] bytes = new byte[bis.available()];
        FileUtil.upload(bytes, Codec.sha1(bytes) + File.separator + file.getName());
//        bis.read(bytes);
//        File file1 = new File("/home/code4j/test/"+Codec.sha1(bytes)+"/"+file.getName());
//        file1.createNewFile();
//        FileOutputStream fos = new FileOutputStream("/home/code4j/test/"+Codec.sha1(bytes)+"/"+file.getName());
//        fos.write(bytes);
//        fos.flush();
//        fos.close();
//        bis.close();
//        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
//        buffer.get(data);
//        String filepath = Codec.sha1(data)+File.separator+file.getName();
//        FileUtil.upload(data,filepath);
    }

    @Test
    public void testBuffer() throws Exception {
        File file = new File("/home/code4j/file");
        byte[] bytes = FileUtil.file2Byte(file);
        byte[] dst = new byte[bytes.length];
        ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
//        buffer.put(bytes);
//        buffer.flip();
//        System.out.println("buffer position--> " + buffer.position());
//        System.out.println("buffer limit--> " + buffer.limit());
//        System.out.println("--------------------");
        buffer = buffer.wrap(bytes);
        System.out.println("buffer position--> " + buffer.position());
        System.out.println("buffer limit--> " + buffer.limit());
        System.out.println("--------------------");
        buffer.get(dst, 0, dst.length);
        System.out.println("buffer position--> " + buffer.position());
        System.out.println("buffer limit--> " + buffer.limit());
        System.out.println("sha1 --> "+Codec.sha1(bytes)+" --> "+new String(bytes));
        System.out.println("sha1 --> "+Codec.sha1(dst)+" --> "+new String(dst));
    }

    @Test
    public void testNioByteBuf() throws Exception {
        File file = new File("/home/code4j/file");
        byte[] bytes = FileUtil.file2Byte(file);
        byte[] dst = new byte[bytes.length];
        ByteBuf buffer = Unpooled.copiedBuffer(bytes);
        buffer.getBytes(0,dst);
        System.out.println("destination --> "+new String(buffer.array()));
    }
}
