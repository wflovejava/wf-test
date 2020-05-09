package com.wf.helloword;

import io.grpc.stub.StreamObserver;

/**
 * @Author: wf
 * @Date: 2020-05-09 15:21
 * @describe 服务提供方代码
 */
public class HelloWordServiceImpl extends HelloWordServiceGrpc.HelloWordServiceImplBase {
    @Override
    public void say(HelloWordRequest request, StreamObserver<HelloWordReply> responseObserver) {
        HelloWordReply reply = HelloWordReply.newBuilder().setMessage("HelloWordServiceImpl response:  "+request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
