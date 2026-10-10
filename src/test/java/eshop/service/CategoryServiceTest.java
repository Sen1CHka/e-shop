package eshop.service;

import eshop.exception.ApiException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryServiceTest {

    @Test
    void slugifyMakesUrlFriendlyName() {
        assertEquals("smart-home", CategoryService.slugify("Smart home"));
        assertEquals("tv-audio", CategoryService.slugify("  TV & Audio! "));
        assertEquals("prislusenstvi", CategoryService.slugify("Příslušenství"));
    }

    @Test
    void slugifyRejectsNameWithoutLettersOrDigits() {
        assertThrows(ApiException.class, () -> CategoryService.slugify("!!!"));
    }
}
