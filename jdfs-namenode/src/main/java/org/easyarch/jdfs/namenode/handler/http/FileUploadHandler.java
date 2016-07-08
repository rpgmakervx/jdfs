package org.easyarch.jdfs.namenode.handler.http;/**
 * Description : 
 * Created by YangZH on 16-7-5
 *  下午1:11
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import org.easyarch.jdfs.common.codec.Codec;
import org.easyarch.jdfs.common.constants.Const;
import org.easyarch.jdfs.common.util.FileUtil;
import org.easyarch.jdfs.common.util.JedisUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description :
 * Created by YangZH on 16-7-5
 * 下午1:11
 */

public class FileUploadHandler extends ChannelInboundHandlerAdapter {

    private ExecutorService pool = Executors.newCachedThreadPool();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest)msg;
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
        if (request.method().equals(HttpMethod.POST)){
            System.out.println("decoder.isMultipart():"+decoder.isMultipart());
            if (request.uri().equals(File.separator+"fileupload")){
                System.out.println("is a file");
                List<InterfaceHttpData> postList = decoder.getBodyHttpDatas();
                for (InterfaceHttpData data : postList) {
                    String name = data.getName();
                    System.out.println("name : "+name+", datatype :"+data.getHttpDataType()+", classname"+data.getClass().getName());
                    if (name.equals("file")&&InterfaceHttpData.HttpDataType.FileUpload == data.getHttpDataType()) {
                        FileUpload fileUpload = (MemoryFileUpload) data;
                        fileUpload.setCharset(CharsetUtil.UTF_8);
                        ByteBuf buffer = fileUpload.content();
                        byte[] bytes = new byte[buffer.readableBytes()];
                        buffer.readBytes(bytes);
                        System.out.println("收到文件,大小：" + bytes.length);
                        String sha1code = Codec.sha1(bytes);
                        if (!JedisUtil.hExists(Const.FILE_HASHCODE,sha1code)){
                            JedisUtil.hSet(Const.FILE_HASHCODE,sha1code,fileUpload.getFilename());
                            JedisUtil.hSet(Const.FILE_HASHCODE,sha1code,fileUpload.getFilename());
                            FileUtil.upload(bytes, Codec.sha1(bytes) + File.separator + fileUpload.getFilename());
                        }
                    }
                }
                response(ctx,HttpResponseStatus.OK ,"<h1>Fie upload successfully</h1>".getBytes());
            }else{
                ctx.fireChannelRead(msg);
            }
        }else{
            System.out.println("not a file");
//            response(ctx,"<h1>Fie upload fail!!</h1>".getBytes());
            ctx.fireChannelRead(request);
        }
    }
    private void response(ChannelHandlerContext ctx,HttpResponseStatus status,byte[] contents) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(contents, 0, contents.length);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status,byteBuf);
        System.out.println("没有请求头，回写数据");
        ctx.channel().writeAndFlush(response);
        ctx.close();
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
