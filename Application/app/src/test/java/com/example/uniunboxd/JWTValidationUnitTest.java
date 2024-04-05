package com.example.uniunboxd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.uniunboxd.utilities.JWTValidation;

import org.junit.Before;
import org.junit.Test;

public class JWTValidationUnitTest {
    private Context ctx;

    static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    @Before
    public void setup() {
        ctx = InstrumentationRegistry.getInstrumentation().getContext();
    }

    @Test
    public void login() {
        assertFalse(JWTValidation.isUserLoggedIn(ctx));
        assertNull(JWTValidation.getToken(ctx));
        JWTValidation.placeToken(TOKEN, ctx);
        assertNotNull(JWTValidation.getToken(ctx));
        assertTrue(JWTValidation.isUserLoggedIn(ctx));
    }

    @Test
    public void useToken() {
        assertEquals(JWTValidation.getToken(ctx), TOKEN);
        assertEquals(JWTValidation.getTokenProperty(ctx, "sub"), "1234567890");
        assertEquals(JWTValidation.getTokenProperty(ctx, "name"), "John Doe");
    }

    @Test
    public void logout() {
        assertNotNull(JWTValidation.getToken(ctx));
        assertTrue(JWTValidation.isUserLoggedIn(ctx));
        JWTValidation.deleteToken(ctx);
        assertFalse(JWTValidation.isUserLoggedIn(ctx));
        assertNull(JWTValidation.getToken(ctx));
    }


}
