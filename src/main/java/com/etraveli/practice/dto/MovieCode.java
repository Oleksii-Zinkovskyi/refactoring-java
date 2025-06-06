package com.etraveli.practice.dto;

import lombok.Getter;

@Getter
public enum MovieCode {
    REGULAR("regular"),
    NEW("new"),
    CHILDREN("childrens");

    private final String code;

    MovieCode(String code) {
        this.code = code;
    }

}
