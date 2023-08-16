package com.example.froggyblogserver.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {
    private int statusCode;
    private String message;
    private Object data;

    public BaseResponse() {
        this.statusCode = 200;
        this.message = "Success";
        this.data = null;
    }

    public BaseResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = null;
    }

    public BaseResponse(Object data) {
        this.statusCode = 200;
        this.message = "Success";
        this.data = data;
    }
}
