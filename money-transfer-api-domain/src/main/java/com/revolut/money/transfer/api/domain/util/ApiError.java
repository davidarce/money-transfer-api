package com.revolut.money.transfer.api.domain.util;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ApiError implements Serializable {

    private static final long serialVersionUID = 4274432107041634240L;

    private int code;
    private String message;
    private String stackTrace;
    private Throwable cause;
    private List<?> reasons;

    public ApiError() {
        super();
    }

    public ApiError(int code, String message, String stackTrace, Throwable cause) {
        this(code, message, stackTrace, cause, null);
    }

    public ApiError(int code, String message, String stackTrace, Throwable cause, List<?> reasons) {
        super();
        this.code = code;
        this.message = message;
        this.stackTrace = stackTrace;
        this.cause = cause;
        this.reasons = reasons;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public List<?> getReasons() {
        return reasons;
    }

    public void setReasons(List<?> reasons) {
        this.reasons = reasons;
    }

    @Override public String toString() {
        return "ApiError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                ", cause=" + cause +
                ", reasons=" + reasons +
                '}';
    }
}