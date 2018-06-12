package com.ufoscout.coreutils.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleValidatorServiceTest extends BaseTest {

    private final ValidatorService service = new SimpleValidatorService();

    @Test
    public void shouldBuildValidator() {
        assertNotNull(service.validator().build());
    }

    @Test
    public void shouldCheckAllRules() {
        Validator<Song> validator = service.<Song>validator()
                .add("id", "id is null", song -> song.getId() != null)
                .add("id", "id is not bigger than 100", song -> 1 > 100)
                .add("artist", "It should really be Queen!", song -> "Queen".equals(song.getArtist()))
                .add("title", "Title should be null", song -> song.getTitle() == null)
                .build();

        ValidationResult<Song> result = validator.validate(new Song());

        assertFalse(result.success());
        assertEquals(2, result.getViolations().size());
        assertEquals("id is null", result.getViolations().get("id").get(0));
        assertEquals("id is not bigger than 100", result.getViolations().get("id").get(1));
        assertEquals("It should really be Queen!", result.getViolations().get("artist").get(0));
    }

    @Test
    public void shouldValidateSuccessfully() {
        Validator<Song> validator = service.<Song>validator()
                .add("id", "id is null", song -> song.getId() != null)
                .add("artist", "It should really be Queen!", song -> "Queen".equals(song.getArtist()))
                .add("title", "Title should not be null", song -> song.getTitle() != null)
                .build();

        Song song = new Song();
        song.setId(1l);
        song.setArtist("Queen");
        song.setTitle("We are the champions");

        ValidationResult<Song> result = validator.validate(song);

        assertTrue(result.success());
        assertEquals(0, result.getViolations().size());
    }

    @Test
    public void shouldThrowExceptionIfValidationFails() {
        assertThrows(ValidationException.class, () -> {
            Validator<Song> validator = service.<Song>validator()
                    .add("id", "id is null", song -> song.getId() != null)
                    .build();

            validator.validateThrowException(new Song());
        });
    }

    @Test
    public void exceptionShouldContainsTheValidationErrors() {
        try {
            Validator<Song> validator = service.<Song>validator()
                    .add("id", "id is null", song -> song.getId() != null)
                    .build();

            validator.validateThrowException(new Song());
        } catch (ValidationException e) {
            assertEquals(1, e.getViolations().size());
            assertEquals("id is null", e.getViolations().get("id").get(0));
        }
    }

}