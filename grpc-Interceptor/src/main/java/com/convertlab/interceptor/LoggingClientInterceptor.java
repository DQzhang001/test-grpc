package com.convertlab.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingClientInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        log.info("Received call to {}", method.getFullMethodName());
        System.out.println("method = " + method.getFullMethodName());

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {

            @Override
            public void sendMessage(ReqT message) {
                log.debug("Request message: {}", message);
                System.out.println("message = " + message);
                super.sendMessage(message);
            }

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                super.start(
                    new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                        @Override
                        public void onMessage(RespT message) {
                            log.debug("Response message: {}", message);
                            System.out.println("message = " + message);
                            super.onMessage(message);
                        }

                        @Override
                        public void onHeaders(Metadata headers) {
                            log.debug("gRPC headers: {}", headers);
                            System.out.println("headers = " + headers);
                            super.onHeaders(headers);
                        }

                        @Override
                        public void onClose(Status status, Metadata trailers) {
                            log.info("Interaction ends with status: {}", status);
                            log.info("Trailers: {}", trailers);
                            System.out.println("status = " + status);
                            System.out.println("status = " + trailers);
                            super.onClose(status, trailers);
                        }
                    }, headers);
            }
        };
    }
}
