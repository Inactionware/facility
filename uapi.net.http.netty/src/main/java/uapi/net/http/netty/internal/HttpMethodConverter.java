/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.net.http.netty.internal;

import uapi.common.ArgumentChecker;
import uapi.net.http.HttpErrors;
import uapi.net.http.HttpException;
import uapi.net.http.HttpMethod;

public class HttpMethodConverter {

    public static io.netty.handler.codec.http.HttpMethod toNettyVersion(
            final HttpMethod httpMethod
    ) {
        ArgumentChecker.required(httpMethod, "httpMethod");
        switch (httpMethod) {
            case GET:
                return io.netty.handler.codec.http.HttpMethod.GET;
            case PUT:
                return io.netty.handler.codec.http.HttpMethod.PUT;
            case POST:
                return io.netty.handler.codec.http.HttpMethod.POST;
            case PATCH:
                return io.netty.handler.codec.http.HttpMethod.PATCH;
            case DELETE:
                return io.netty.handler.codec.http.HttpMethod.DELETE;
            default:
                throw HttpException.builder()
                    .errorCode(HttpErrors.UNSUPPORTED_HTTP_METHOD)
                    .variables(httpMethod.name())
                    .build();
        }
    }

    public static HttpMethod toUapiVersion(
            final io.netty.handler.codec.http.HttpMethod nettyHttpMethod
    ) {
        if (io.netty.handler.codec.http.HttpMethod.GET.equals(nettyHttpMethod)) {
            return HttpMethod.GET;
        } else if (io.netty.handler.codec.http.HttpMethod.PUT.equals(nettyHttpMethod)) {
            return HttpMethod.PUT;
        } else if (io.netty.handler.codec.http.HttpMethod.POST.equals(nettyHttpMethod)) {
            return HttpMethod.POST;
        } else if (io.netty.handler.codec.http.HttpMethod.PATCH.equals(nettyHttpMethod)) {
            return HttpMethod.PATCH;
        } else if (io.netty.handler.codec.http.HttpMethod.DELETE.equals(nettyHttpMethod)) {
            return HttpMethod.DELETE;
        } else {
            throw HttpException.builder()
                    .errorCode(HttpErrors.UNSUPPORTED_HTTP_METHOD)
                    .variables(nettyHttpMethod.name())
                    .build();
        }
    }

    private HttpMethodConverter() { }
}
