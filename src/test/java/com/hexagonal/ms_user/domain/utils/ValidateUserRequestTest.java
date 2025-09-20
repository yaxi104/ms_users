package com.hexagonal.ms_user.domain.utils;

import com.hexagonal.ms_user.domain.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateUserRequestTest {

    @Test
    void checkNotBlankValidValueDoesNotThrow() {
        assertDoesNotThrow(() -> ValidateRequest.checkNotBlank("value"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void checkNotBlankNullThrowsBadRequestException(String arg) {
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkNotBlank(arg));
    }

    @Test
    void checkPatternwithValidAndInvalidValuesbehavesAsExpected() {
        String validValue = "12345";
        String invalidValue = "abc123";
        String pattern = "\\d+";

        assertAll(
                () -> assertDoesNotThrow(() -> ValidateRequest.checkPattern(validValue, pattern), "Valid value should not throw"),
                () -> assertThrows(BadRequestException.class, () -> ValidateRequest.checkPattern(invalidValue, pattern), "Invalid value should throw BadRequestException")
        );
    }

    @Test
    void checkPastDateValidPastDateDoesNotThrow() {
        LocalDate date = LocalDate.now().minusYears(20);
        assertDoesNotThrow(() -> ValidateRequest.checkPastDate(date));
    }

    @Test
    void checkPastDateNullThrowsBadRequestException() {
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkPastDate(null));
    }

    @Test
    void checkPastDatewithInvalidDatesshouldThrowBadRequestException() {
        LocalDate today = LocalDate.now();
        LocalDate underageBirthDate = LocalDate.now().minusYears(10);

        assertAll(
                () -> assertThrows(BadRequestException.class, () -> ValidateRequest.checkPastDate(today)),
                () -> assertThrows(BadRequestException.class, () -> ValidateRequest.checkPastDate(underageBirthDate))
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"122321515665155651", "2124*"})
    void checkNumberPhoneThrowsBadRequestException(String arg) {
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkNumberPhone(arg));
    }

}