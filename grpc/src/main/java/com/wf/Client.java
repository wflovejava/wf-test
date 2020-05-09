package com.wf;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.logging.Level;

/**
 * @Author: wf
 * @Date: 2020-05-08 18:29
 * @describe
 */
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    /**
     * 传输通道
     */
    private final ManagedChannel channel;
    /**
     * 客户端存根，使得调用服务接口跟在本地调用一致
     */
    private final HelloTestGrpc.HelloTestBlockingStub blockingStub;
    /**
     * Construct client connecting to server at {@code host:port}.
     *
     * @param host ip
     * @param port 端口
     */
    public Client(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build());
    }

    /**
     * Construct client for accessing server using the existing channel.
     *
     * @param channel 通道
     */
    private Client(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = HelloTestGrpc.newBlockingStub(channel);
    }

    /**
     * 关闭通道
     *
     * @throws InterruptedException 异常
     */
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * 客户端进行一些业务处理
     *
     * @param name 名称标识
     */
    public void callRemoteApi(Person person, String name) {
        logger.info("Will try to greet " + name + " ...");
        // 对Person对象进行序列化
        ByteString byteString = SerializeUtil.serialize(person);
        // 构造请求消息结构体
        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .setSex("妹子").setEntity(byteString)
                .build();
        HelloReply response;
        try {
            // 通过存根进行调用服务端的方法，也即服务接口
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            logger.debug("RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info(response.getMessage());
    }

    public static void main(String[] args) throws Exception {
        // host 可设置为远程IP
        Client client = new Client("localhost", 8888);
        try {
            // 客户端逻辑实现
            Person p = new Person();
            p.setName("wf");
            p.setAge(18);

            client.callRemoteApi(p, "wf");
        } finally {
            client.shutdown();
        }
    }
}
