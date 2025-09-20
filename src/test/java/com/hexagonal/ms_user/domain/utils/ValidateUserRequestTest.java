package com.hexagonal.ms_user.domain.utils;

import com.hexagonal.ms_user.domain.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateUserRequestTest {

    @Test
    void checkNotBlank_ValidValue_DoesNotThrow() {
        assertDoesNotThrow(() -> ValidateRequest.checkNotBlank("valor"));
    }

    @Test
    void checkNotBlank_Null_ThrowsBadRequestException() {
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkNotBlank(null));
    }

    @Test
    void checkNotBlank_Blank_ThrowsBadRequestException() {
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkNotBlank("   "));
    }

    @Test
    void checkPattern_ValidValue_DoesNotThrow() {
        String value = "12345";
        String pattern = "\\d+";
        assertDoesNotThrow(() -> ValidateRequest.checkPattern(value, pattern));
    }

    @Test
    void checkPattern_InvalidValue_ThrowsBadRequestException() {
        String value = "abc123";
        String pattern = "\\d+";
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkPattern(value, pattern));
    }

    @Test
    void checkPastDate_ValidPastDate_DoesNotThrow() {
        LocalDate date = LocalDate.now().minusYears(20);
        assertDoesNotThrow(() -> ValidateRequest.checkPastDate(date));
    }

    @Test
    void checkPastDate_Null_ThrowsBadRequestException() {
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkPastDate(null));
    }

    @Test
    void checkPastDate_Today_ThrowsBadRequestException() {
        LocalDate today = LocalDate.now();
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkPastDate(today));
    }

    @Test
    void checkPastDate_LessThanRequiredAge_ThrowsBadRequestException() {
        LocalDate recentBirth = LocalDate.now().minusYears(10); // supongamos que MAX_AGE = 18
        assertThrows(BadRequestException.class, () -> ValidateRequest.checkPastDate(recentBirth));
    }
}