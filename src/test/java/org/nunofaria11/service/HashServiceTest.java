package org.nunofaria11.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class HashServiceTest {

    @Inject
    HashService hashService;

    @Test
    void correctLength() {
        Assertions.assertEquals(6, hashService.generate().length());
    }
}
