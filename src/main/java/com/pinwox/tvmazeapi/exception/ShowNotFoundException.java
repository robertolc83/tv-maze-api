package com.pinwox.tvmazeapi.exception;

public class ShowNotFoundException extends RuntimeException{
    public ShowNotFoundException(Long showId) {
        super("Show no encontrado con id: " + showId);
    }
}
