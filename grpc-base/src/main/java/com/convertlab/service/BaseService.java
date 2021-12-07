package com.convertlab.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.TimeUnit;

public class BaseService implements InitializingBean, DisposableBean {

    private ManagedChannel channel;

    private String server;
    private String port;

    public BaseService(String server, String port) {
        this.server = server;
        this.port = port;
    }

    public ManagedChannel getChannel() {
        return channel;
    }

    @Override
    public void destroy() throws Exception {
        if (channel != null) {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @Override
    public void afterPropertiesSet() {
        channel = ManagedChannelBuilder.forTarget(server+":"+port).usePlaintext().build();
    }
}
