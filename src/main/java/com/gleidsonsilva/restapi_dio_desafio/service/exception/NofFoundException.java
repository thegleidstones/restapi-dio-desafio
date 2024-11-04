package com.gleidsonsilva.restapi_dio_desafio.service.exception;

public class NofFoundException extends BusinessException {
    private static final long serialVersionUID = 1L;

    public NofFoundException() {
        super("Resource not found.");
    }

}
