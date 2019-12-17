package cn.edu.nju.example;

import cn.edu.nju.nioserver.HttpServer;

/**
 * @author jjenkov
 * @date 19-10-2015
 * <p>
 * http example
 */
public class Main {

    public static void main(String[] args) {
        new HttpServer(8080).startServer();
    }

}
