package com.revolut.money.transfer.api.configuration;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestLogger {

    private static final Logger LOG = LoggerFactory.getLogger(RequestLogger.class);

    public void log(Context context, Float executionTimeMs) {
        String info = context.req.getMethod()
                + " request-uri: " + context.req.getRequestURI()
                + " request-parameters: " + context.req.getParameterMap()
                .toString().replaceAll("^.|.$", "");
        LOG.info("Request: {} took: {} ms", info, executionTimeMs);
    }

}
