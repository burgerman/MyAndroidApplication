package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class LoginActivityUnitTest {

    @Test
    public void testHandleLogin_EmptyFields() {
        String username = "";
        String password = "";
        String error = null;
        if (username.isEmpty() || password.isEmpty()) {
            error = "Either username or password can't be empty";
        } else if (!LoginActivity.isValidEmail(username)) {
            error = "Invalid email address";
        }
        assertEquals("Either username or password can't be empty", error);
    }

    @Test
    public void testHandleLogin_InvalidEmail() {
        String username = "invalid";
        String password = "pwd";
        String error = null;
        if (username.isEmpty() || password.isEmpty()) {
            error = "Either username or password can't be empty";
        } else if (!LoginActivity.isValidEmail(username)) {
            error = "Invalid email address";
        }
        assertEquals("Invalid email address", error);
    }

    @Test
    public void testHandleLogin_Success() {
        String username = "email@domain.com";
        String password = "pwd";
        String error = null;
        if (username.isEmpty() || password.isEmpty()) {
            error = "Either username or password can't be empty";
        } else if (!LoginActivity.isValidEmail(username)) {
            error = "Invalid email address";
        }
        assertNull(error);
    }
}
