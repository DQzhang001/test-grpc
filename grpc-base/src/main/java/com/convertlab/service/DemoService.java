package com.convertlab.service;

import com.convertlab.grpc.GreeterGrpc;
import com.convertlab.grpc.GreeterRequest;
import com.convertlab.grpc.GreeterResponse;
import com.convertlab.interceptor.TenantPropagationClientInterceptor;
import io.grpc.Channel;
import io.grpc.ClientInterceptors;
import org.apache.poi.ss.formula.functions.T;

import java.nio.charset.StandardCharsets;

public class DemoService extends BaseService{

    private GreeterGrpc.GreeterBlockingStub greeterBlockingStub;

    public DemoService(String server, String port) {
        super(server, port);
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Channel channel1 = ClientInterceptors.intercept(super.getChannel(),new TenantPropagationClientInterceptor());
        greeterBlockingStub = GreeterGrpc.newBlockingStub(channel1);
    }


    public String sayHello(final String name) {
        GreeterRequest request = GreeterRequest.newBuilder()
                .setName(name).build();
        System.out.println(" greeterBlockingStub.getChannel()= " + greeterBlockingStub.getChannel().authority());
        System.out.println("greeterBlockingStub.toString() = " + greeterBlockingStub.toString().getBytes(StandardCharsets.UTF_8));
        GreeterResponse response = greeterBlockingStub.sayHello(request);
        return response.getMessage();
    }


}
