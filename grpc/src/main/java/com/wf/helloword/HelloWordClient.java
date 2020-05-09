package com.wf.helloword;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @Author: wf
 * @Date: 2020-05-09 14:32
 * @describe 客户端
 */
public class HelloWordClient {

    /**
     *  传输通道
     */
    private final ManagedChannel channel;

    /**
     * 发送请求
     */
    private final HelloWordServiceGrpc.HelloWordServiceBlockingStub blockingStub;


    /**
     * 构建stub 用于发送请求
     * @param channel
     */
    public HelloWordClient(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub =  HelloWordServiceGrpc.newBlockingStub(channel);;
    }

    /**
     * 构建channel连接 多路复用 （多个channel 复用一个selector器）
     * @param host
     * @param port
     */
    public HelloWordClient(String host,int port){
        this(ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build());
    }

    /**
     * 调用完手动关闭
     * @throws InterruptedException
     */
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * 发送RPC请求
     * @param name
     */
    public  void say(String name){
        //构建入参对象
        HelloWordRequest request = HelloWordRequest.newBuilder().setName(name).build();
        HelloWordReply response;
        //发送请求
        response =  blockingStub.say(request);
        System.out.println("返回结果response："+response);
    }

    public static void main(String[] args) {
        try {
            HelloWordClient client = new HelloWordClient("127.0.0.1",8888);
            client.say("hello Word ok");
            client.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
