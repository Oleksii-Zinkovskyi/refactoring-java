package com.etraveli.practice.dto;

import lombok.Getter;

@Getter
public enum MovieEnum {
    F001("You've Got Mail", "regular"),
    F002("Matrix", "regular"),
    F003("Cars", "childrens"),
    F004("Fast & Furious X", "new");

    private final String title;
    private final String code;

    MovieEnum(String title, String code) {
        this.title = title;
        this.code = code;
    }

}
