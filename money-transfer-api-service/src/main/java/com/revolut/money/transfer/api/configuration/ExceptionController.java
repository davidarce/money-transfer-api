package com.revolut.money.transfer.api.configuration;

import com.revolut.money.transfer.api.domain.exception.RevolutException;
import com.revolut.money.transfer.api.domain.util.ApiError;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.UnknownCurrencyException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExceptionController {

    private final Logger LOG = LoggerFactory.getLogger(ExceptionController.class);

    private static final String EX_GENERIC_ERROR_MSG = "An unexpected error has occurred";
    private static final String EX_BEST_ENGINEERS_MSG ="our best engineers are working to do a better place for you!";

    public void handleGeneralExceptions(Exception ex, Context context) {
        LOG.error(EX_GENERIC_ERROR_MSG, ex);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR_500, EX_BEST_ENGINEERS_MSG,
                this.getStringStackTraceFromEx(ex), ex);
        context.status(HttpStatus.INTERNAL_SERVER_ERROR_500).json(apiError);
    }

    public void handleIllegalArgumentException(IllegalArgumentException ex, Context context) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST_400,
                HttpStatus.getCode(HttpStatus.BAD_REQUEST_400).getMessage(),
                this.getStringStackTraceFromEx(ex), ex);
        context.status(HttpStatus.BAD_REQUEST_400).json(apiError);

    }

    public void handleUnknownCurrencyException(UnknownCurrencyException ex, Context context) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST_400,
                HttpStatus.getCode(HttpStatus.BAD_REQUEST_400).getMessage(),
                this.getStringStackTraceFromEx(ex), ex);
        context.status(HttpStatus.BAD_REQUEST_400).json(apiError);

    }

    public void handleRevolutExceptions(RevolutException ex, Context context) {
        LOG.error(ex.getMessage(), ex);
        int httpCode = this.getHttpCode(ex);
        ApiError apiError = new ApiError(
                ex.getCode(), ex.getMessage(), this.getStringStackTraceFromEx(ex), ex.getCause());
        context.status(HttpStatus.getCode(httpCode).getCode()).json(apiError);
    }

    private String getStringStackTraceFromEx(Exception ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

    private int getHttpCode(RevolutException ex) {
        int httpCode = ex.getCode();
        String stringCode = String.valueOf(ex.getCode());
        Pattern pattern = Pattern.compile("^[\\d]{3}$");
        Matcher matcher = pattern.matcher(stringCode);

        if (!matcher.matches()) {
            return Integer.parseInt(stringCode.substring(0, 3));
        }
        return httpCode;

    }

}
