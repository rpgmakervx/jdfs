package org.easyarch.jdfs.namenode;/**
 * Description : 
 * Created by YangZH on 16-7-8
 *  下午4:06
 */

import org.easyarch.jdfs.namenode.server.NameNodeServer;

/**
 * Description :
 * Created by YangZH on 16-7-8
 * 下午4:06
 */

public class Main {


    public static void main(String[] args) {
        int port = 8000;
        if (args.length > 0){
            port = Integer.valueOf(args[0]);
        }
        new NameNodeServer().startup(port);
    }
}
