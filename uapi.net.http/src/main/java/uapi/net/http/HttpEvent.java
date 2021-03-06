/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net.http;

import uapi.behavior.BehaviorEvent;
import uapi.common.ArgumentChecker;
import uapi.common.StringHelper;
import uapi.net.IErrorHandler;
import uapi.net.INetEvent;

public class HttpEvent extends BehaviorEvent implements INetEvent {

    public static final String TYPE     = "HTTP";
    public static final String TOPIC    = "HttpRequest";

    private final IHttpRequest _request;
    private final IHttpResponse _response;

    public HttpEvent(
            final String sourceName,
            final IHttpRequest request,
            final IHttpResponse response
    ) {
        super(TOPIC, sourceName);

        ArgumentChecker.required(request, "request");
        ArgumentChecker.required(response, "response");

        this._request = request;
        this._response = response;
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public IHttpRequest request() {
        return this._request;
    }

    @Override
    public IHttpResponse response() {
        return this._response;
    }

    @Override
    public String toString() {
        return StringHelper.makeString("HttpEvent: type={}", type());
    }
}
