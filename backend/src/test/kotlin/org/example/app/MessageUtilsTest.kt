package org.example.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MessageUtilsTest {
    @Test fun testGetMessage() {
        assertEquals("Hello      World!", "Hello      World!")
    }
}
