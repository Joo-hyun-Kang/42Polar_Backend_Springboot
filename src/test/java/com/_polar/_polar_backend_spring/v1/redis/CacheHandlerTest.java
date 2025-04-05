package com._polar._polar_backend_spring.v1.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CacheHandlerTest {
    @Autowired
    private CacheHandler cacheHandler;

    @Test
    public void testSetCacheForString() {
        // Arrange
        String key = "testKey";
        String expectedValue = "testValue";

        // Act
        cacheHandler.setCache(key, expectedValue);

        // Assert
        Object result = cacheHandler.getCacheOrNull(key);
        assert result.equals(expectedValue);
    }

    @Test
    public void testSetCacheForInt() {
        // Arrange
        String key = "testKeyInt";
        int expectedValue = 42;

        // Act
        cacheHandler.setCache(key, expectedValue);

        // Assert
        Object result = cacheHandler.getCacheOrNull(key);
        assert result.equals(expectedValue);
    }

    @Test
    public void testGetNoValue() {
        // Arrange
        String key = "testKeyObject";
        String expectedValue = "testValue";

        // Act
        cacheHandler.setCache(key, expectedValue);

        // Assert
        Object result = cacheHandler.getCacheOrNull("nonExistentKey");
        assert result == null;
    }

    @Test
    public void testDeleteCache() {
        // Arrange
        String key = "testKeyToDelete";
        String expectedValue = "testValue";

        // Act
        cacheHandler.setCache(key, expectedValue);
        Object resultBeforeDelete = cacheHandler.getCacheOrNull(key);
        assert resultBeforeDelete.equals(expectedValue);

        Boolean result = cacheHandler.deleteCache(key);

        assert result;
    }
}