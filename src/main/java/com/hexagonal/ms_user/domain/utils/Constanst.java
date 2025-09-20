package com.hexagonal.ms_user.domain.utils;

public class Constanst {

    private Constanst() {
    }

    /*    PATTERNS*/
    public static final String PATTERN_EMAIL = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
    public static final String PATTERN_NUMBER_PHONE = "^\\+?\\d{7,13}$";
    public static final String PATTERN_ONLY_NUMBER = "\\d+";
    public static final Integer MAX_LENGTH = 13;
    public static final Integer MAX_AGE = 18;
}
