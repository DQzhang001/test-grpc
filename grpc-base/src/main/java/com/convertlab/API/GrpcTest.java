package com.convertlab.API;

import com.atf.cap.api.APITest;
import com.atf.cap.domain.api.APIRun;
import com.convertlab.grpc.GreeterRequest;
import com.convertlab.grpc.GreeterResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class GrpcTest extends APITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcTest.class);

    public GreeterResponse execute(GreeterRequest request){
        APIRun apiRun = new APIRun();
        long current = System.currentTimeMillis();
        //URI uri = request.getServiceURI();
        return null;
    }

}
