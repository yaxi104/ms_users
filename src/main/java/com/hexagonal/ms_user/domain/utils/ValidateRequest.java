package com.hexagonal.ms_user.domain.utils;

import com.hexagonal.ms_user.domain.exception.BadRequestException;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

import static com.hexagonal.ms_user.domain.utils.Constanst.MAX_AGE;
import static com.hexagonal.ms_user.domain.utils.Constanst.MAX_LENGTH;
import static com.hexagonal.ms_user.domain.utils.Constanst.PATTERN_EMAIL;
import static com.hexagonal.ms_user.domain.utils.Constanst.PATTERN_NUMBER_PHONE;
import static com.hexagonal.ms_user.domain.utils.Constanst.PATTERN_ONLY_NUMBER;

public class ValidateRequest {

    private ValidateRequest() {
    }

    public static void checkNotBlank(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException();
        }
    }

    public static void checkPattern(String value, String pattern) {
        if (!Pattern.matches(pattern, value)) {
            throw new BadRequestException();
        }
    }

    public static void checkIdNumber(String idNumber) {
        checkNotBlank(idNumber);
        checkPattern(idNumber, PATTERN_ONLY_NUMBER);
    }

    public static void checkEmail(String email) {
        checkNotBlank(email);
        checkPattern(email, PATTERN_EMAIL);
    }

    public static void checkNumberPhone(String numberPhone) {
        checkNotBlank(numberPhone);
        checkMaxLength(numberPhone);
        checkPattern(numberPhone, PATTERN_NUMBER_PHONE);
    }

    private static void checkMaxLength(String value) {
        if (value.length() > MAX_LENGTH) {
            throw new BadRequestException();
        }
    }

    public static void checkPastDate(LocalDate date) {
        if (date == null || !date.isBefore(LocalDate.now()) || Period.between(date, LocalDate.now()).getYears() < MAX_AGE) {
            throw new BadRequestException();
        }
    }
}
