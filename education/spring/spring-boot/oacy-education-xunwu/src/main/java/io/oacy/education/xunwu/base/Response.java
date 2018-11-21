package io.oacy.education.xunwu.base;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {
    private int code;
    private String message;
    private T data;
    private boolean more;

    public Response() {
        this.code = Status.SUCCESS.getCode();
        this.message = Status.SUCCESS.getStandardMessage();
    }

    public static Response<Void> ofMessage(int code,String message){
        return new Response<>(code,message,null,false);
    }

    public static Response<Void> ofStatus(Status status){
        return new Response<>(status.getCode(),status.getStandardMessage(),null,false);
    }

    public static <T> Response<T> ofSuccess(T data){
        return new Response<>(Status.SUCCESS.getCode(),Status.SUCCESS.getStandardMessage(),data,false);
    }


    public enum Status {
        SUCCESS(200, "OK"),
        BAD_REQUEST(400, "Bad Request"),
        NOT_FOUND(404, "Not Found"),
        INTERNAL_SERVER_ERROR(500, "Unknown Internal Error"),
        NOT_VALID_PARAM(40005, "Not valid Params"),
        NOT_SUPPORTED_OPERATION(40006, "Operation not supported"),
        NOT_LOGIN(50000, "Not Login");

        private int code;
        private String standardMessage;

        Status(int code, String standardMessage) {
            this.code = code;
            this.standardMessage = standardMessage;
        }

        public int getCode() {
            return this.code;
        }

        public String getStandardMessage() {
            return this.standardMessage;
        }
    }
}
