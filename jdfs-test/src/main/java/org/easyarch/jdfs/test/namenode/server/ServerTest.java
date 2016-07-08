package org.easyarch.jdfs.test.namenode.server;/**
 * Description : 
 * Created by YangZH on 16-7-5
 *  下午1:53
 */

import org.easyarch.jdfs.namenode.server.NameNodeServer;
import org.junit.Test;

/**
 * Description :
 * Created by YangZH on 16-7-5
 * 下午1:53
 */

public class ServerTest {

    @Test
    public void testStart(){
        new NameNodeServer().startup(8000);
    }
}
