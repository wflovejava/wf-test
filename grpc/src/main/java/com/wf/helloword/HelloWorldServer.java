package com.wf.helloword;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * @Author: wf
 * @Date: 2020-05-09 15:25
 * @describe
 */
public class HelloWorldServer {

    private Server server;

    /**
     * 对外暴露服务
     */
    private void start() throws IOException {
        int port = 8888;
        server = ServerBuilder.forPort(port)
                .addService(new HelloWordServiceImpl())
                .build()
                .start();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                HelloWorldServer.this.stop();
            }
        });

    }

    /**
     * 关闭
     */
    private void stop(){
        if(server != null){
            server.shutdown();
        }
    }

    /**
     * 优化关闭
     * @throws InterruptedException
     */
    private void blockUntilShutdown() throws InterruptedException {
        if(server != null){
            server.awaitTermination();
        }
    }

    public static void main(String[] args) {
        final HelloWorldServer helloWorldServer = new HelloWorldServer();
        try {
            helloWorldServer.start();
            helloWorldServer.blockUntilShutdown();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
