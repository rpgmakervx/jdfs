package org.easyarch.jdfs.namenode.server;/**
 * Description : 
 * Created by YangZH on 16-7-5
 *  下午1:06
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.easyarch.jdfs.namenode.handler.ServerChildHandler;

/**
 * Description :
 * Created by YangZH on 16-7-5
 * 下午1:06
 */

public class NameNodeServer {
    public void startup(int port){
        launch(port);
    }

    private void launch(int port){
        System.out.println("正在启动服务。。。");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        ChannelFuture f = null;
        try {
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChildHandler())
                    .option(ChannelOption.SO_BACKLOG, 256).childOption(ChannelOption.SO_KEEPALIVE, true);
            f = b.bind(port).sync();
            System.out.println("服务已启动");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
