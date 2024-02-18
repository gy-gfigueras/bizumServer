package org.gfigueras.bizum.model.entities;

public enum ServerCodes {
    SUCCESS(0),
    INVALID_USERNAME(-1),
    INVALID_EMAIL(-2),
    INVALID_PASSWORD(-3),
    INVALID_LOGIN(-4),
    INVALID_SIGNUP(-5),
    INVALID_UPDATE(-6),
    DATABASE_ERROR(-7),
    INVALID_PHONE(-8),
    INVALID_MONEY(-9); 

    public int code;

    ServerCodes(int code) {
        this.code = code;
    }
    public Integer toInt(){
        return this.code;
    }

}
