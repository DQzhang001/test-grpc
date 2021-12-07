package com.convertlab.library.constant;

import io.grpc.Context;
import io.grpc.Metadata;

public interface MultiTenancyGrpcConstants extends MultiTenancyConstants{

    Metadata.Key<String> TENANT_METADATA_KEY = Metadata.Key.of(TENANT_ID, Metadata.ASCII_STRING_MARSHALLER);

    Context.Key<String> TENANT_CONTEXT_KEY = Context.key(TENANT_ID);

}
