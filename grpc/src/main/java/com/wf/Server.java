package com.wf;

import com.wf.service.HelloServiceImpl;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author: wf
 * @Date: 2020-05-08 18:21
 * @describe
 */
public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    /**
     * gRPC服务端
     */
    private io.grpc.Server server;
    /**
     * 启动服务
     * @throws IOException 异常
     */
    private void start() throws IOException
    {
        /* The port on which the server should run */
        int port = 8888;
        server = ServerBuilder.forPort(port)
                .addService(new HelloServiceImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                Server.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    /**
     * 关闭服务
     */
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     * @throws InterruptedException 异常
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // 主函数，未详细处理异常
        final Server server = new Server();
        server.start();
        server.blockUntilShutdown();
    }

}
