package com.etraveli.practice.dto;

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

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    /*Technically, this isn't being used and should be removed according to YAGNI and KISS principles,
    but I am keeping it to show that there is no functional deterioration due to the enum conversion.*/
    public Movie toMovie() {
        return new Movie(title, code);
    }
}
