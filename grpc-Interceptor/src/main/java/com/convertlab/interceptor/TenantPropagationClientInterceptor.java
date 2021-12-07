package com.convertlab.interceptor;

import com.atf.cap.api.APITest;
import com.convertlab.library.constant.MultiTenancyGrpcConstants;
import com.convertlab.testNG.GRPCReporter;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;

@Slf4j
@Listeners(value = {GRPCReporter.class})
public class TenantPropagationClientInterceptor extends APITest implements ClientInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantPropagationClientInterceptor.class);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {


        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
           /** @Override
            public void start(Listener<RespT> responseListener, Metadata metadata) {
                metadata.put(MultiTenancyGrpcConstants.TENANT_METADATA_KEY, "100");
                super.start(responseListener, metadata);
            }
           */

           @Override
           public void sendMessage(ReqT message) {
               LOGGER.info("Request message: {}", message);
               System.out.println("message = " + message);
               super.sendMessage(message);
           }

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(MultiTenancyGrpcConstants.TENANT_METADATA_KEY, "100");
               super.start(
                        new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {

                            @Override
                            public void onMessage(RespT message) {
                                LOGGER.info("Response message: {}", message);
                                System.out.println("message = " + message);
                                super.onMessage(message);
                            }

                            @Override
                            public void onHeaders(Metadata headers) {
                                LOGGER.info("gRPC headers: {}", headers);
                                System.out.println("headers = " + headers);
                                super.onHeaders(headers);
                            }

                            @Override
                            public void onClose(Status status, Metadata trailers) {
                                LOGGER.info("Interaction ends with status: {}", status);
                                LOGGER.info("Trailers: {}", trailers);
                                System.out.println("status = " + status);
                                System.out.println("status = " + trailers);
                                super.onClose(status, trailers);
                            }
                        }, headers);
            }
        };
    }
}
