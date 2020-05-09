package com.wf.service;

import com.google.protobuf.ByteString;
import com.wf.*;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;

/**
 * @Author: wf
 * @Date: 2020-05-08 18:23
 * @describe 服务具体实现，HelloServiceImpl，这个了类的作用是，我们上面通过hello.proto生成了一些Java文件，
 * 但是怎么样让我们自己的业务逻辑可以走到生成的那些Java代码呐？
 * 就是写到这个HelloServiceImpl服务实现类里面，
 */
public class HelloServiceImpl extends HelloTestGrpc.HelloTestImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        try {
            // 处理请求
            ByteString content = request.getEntity();
            System.out.println("收到的请求序列换前" + content);
            Person person = (Person) SerializeUtil.deserialize(content.toByteArray());
            // 依据person的age来进行同的处理
            String result;
            // 构造返回值，可以处理教复杂的操作后返回，这里只是根据
            HelloReply reply = HelloReply.newBuilder()
                    .setMessage("Hello" + request.getName() + "，收到了你的请求。你的名字：" + person.getName() + "。你的年龄：" + person
                            .getAge()+".你是一个"+request.getSex())
                    .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
}
