package org.easyarch.codec;/**
 * Description : 
 * Created by YangZH on 16-7-3
 *  下午5:47
 */

import org.easyarch.jdfs.codec.Codec;
import org.junit.Test;

/**
 * Description :
 * Created by YangZH on 16-7-3
 * 下午5:47
 */

public class CodecTest {

    @Test
    public void testSHA1(){
        System.out.println(Codec.sha1("192.168.1.1"));
        System.out.println(Codec.hash(Codec.sha1("192.168.1.1")));
        System.out.println(Codec.sha1("192.168.1.2"));
        System.out.println(Codec.hash(Codec.sha1("192.168.1.2")));
    }
}
