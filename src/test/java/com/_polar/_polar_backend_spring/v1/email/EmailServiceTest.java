package com._polar._polar_backend_spring.v1.email;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {
    @Test
    public void validationCodeGenerationTest() {
        HashMap<String, Integer> generatedCodes = new HashMap<>();

        // 1000回以上には被ってしまう
        for (int i = 0; i < 1000; i++) {
            // Generate a random code
            String code = Double.toHexString(Math.random()).substring(4, 10);

            System.out.println(code);

            // Check if the code is of the expected length
            assertEquals(6, code.length());

            // Check if the code contains only alphanumeric characters
            assertTrue(code.matches("[a-zA-Z0-9]+"));

            if (i > 0) {
                // Check if the code is unique
                assertFalse(generatedCodes.containsKey(code), "Duplicate code found: " + code + " at iteration " + i + " with previous index " + generatedCodes.get(code));
            }

            generatedCodes.put(code, i);
        }
    }
}