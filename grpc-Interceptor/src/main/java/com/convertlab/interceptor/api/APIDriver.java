package com.convertlab.interceptor.api;

import com.atf.cap.api.APIResponse;
import com.convertlab.grpc.GreeterRequest;
import com.convertlab.grpc.GreeterResponse;
import com.convertlab.interceptor.TenantPropagationClientInterceptor;
import com.convertlab.library.constant.MultiTenancyGrpcConstants;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APIDriver implements ClientInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TenantPropagationClientInterceptor.class);

    private  Metadata headers;

    public APIDriver(Metadata headers){
        this.headers = headers;
    }


    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions, Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            @Override
            public void sendMessage(ReqT message) {
                logger.debug("Request message: {}", message);
                super.sendMessage(message);
            }

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                //headers.put(MultiTenancyGrpcConstants.TENANT_METADATA_KEY, tenantID);
                super.start(
                        new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                            @Override
                            public void onHeaders(Metadata headers) {
                                logger.debug("gRPC headers: {}", headers);
                                System.out.println("headers = " + headers);
                                super.onHeaders(headers);
                            }

                        }, headers);
                }
            };
    }
}
