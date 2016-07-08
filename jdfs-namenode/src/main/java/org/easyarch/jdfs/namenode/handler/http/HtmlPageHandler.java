package org.easyarch.jdfs.namenode.handler.http;/**
 * Description : 
 * Created by YangZH on 16-7-8
 *  下午3:43
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.easyarch.jdfs.common.util.FileUtil;

import java.io.UnsupportedEncodingException;

/**
 * Description :
 * Created by YangZH on 16-7-8
 * 下午3:43
 */

public class HtmlPageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest)msg;
        String filepath = request.uri();
        if (request.method().equals(HttpMethod.GET)){
            System.out.println("html页面");
            if (FileUtil.fileExists(filepath)){
                System.out.println("获得到html页面");
                response(ctx,HttpResponseStatus.OK,FileUtil.getHtml(filepath).getBytes());
            }else{
                System.out.println("页面404");
                response(ctx,HttpResponseStatus.NOT_FOUND,"<h1 algin='center'>404 NOT FOUND</h1>".getBytes());
            }
        }else{
            ctx.fireChannelRead(request);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    private void response(ChannelHandlerContext ctx,HttpResponseStatus status,byte[] contents) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(contents, 0, contents.length);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status,byteBuf);
        System.out.println("没有请求头，回写数据");
        ctx.channel().writeAndFlush(response);
        ctx.close();
    }
}
