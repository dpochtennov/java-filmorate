package ru.yandex.practicum.filmorate.validation.afterdate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AfterDateValidatorTest {

    private AfterDate afterDateAnnotation;

    @BeforeEach
    public void setup() {
        afterDateAnnotation = Mockito.mock(AfterDate.class);
        when(afterDateAnnotation.value()).thenReturn("1999-01-01");
    }

    @Test
    void isValid() {
        AfterDateValidator afterDateValidator = new AfterDateValidator();

        afterDateValidator.initialize(afterDateAnnotation);

        assertTrue(afterDateValidator.isValid(LocalDate.of(2000, Month.JANUARY, 1), null));
        assertFalse(afterDateValidator.isValid(LocalDate.of(1998, Month.JANUARY, 1), null));
    }

    @Test
    void isNotValidWhenPropagatedValueIsNull() {
        AfterDateValidator afterDateValidator = new AfterDateValidator();

        afterDateValidator.initialize(afterDateAnnotation);

        assertFalse(afterDateValidator.isValid(null, null));
    }

    @Test
    void throwsExceptionWhenAnnotationValueIsOfImproperFormat() {
        AfterDateValidator afterDateValidator = new AfterDateValidator();
        when(afterDateAnnotation.value()).thenReturn("abcd");

        assertThrows(DateTimeParseException.class, () -> {
            afterDateValidator.initialize(afterDateAnnotation);
        });
    }
}