package com.example.uniunboxd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.fragment.app.Fragment;

import com.example.uniunboxd.utilities.StackHandler;

import org.junit.Test;

public class StackHandlerUnitTest {
    @Test
    public void testSingleton() {
        StackHandler a = StackHandler.getInstance();
        StackHandler b = StackHandler.getInstance();
        assertTrue(a == b);
    }

    @Test
    public void testStack() {
        StackHandler s = StackHandler.getInstance();
        assertEquals(s.size(), 0);

        Fragment f1 = new Fragment();
        Fragment f2 = new Fragment();

        s.push(f1);
        assertEquals(s.size(), 1);
        s.push(f2);
        assertEquals(s.size(), 2);

        assertEquals(s.pop(), f2);
        assertEquals(s.size(), 1);

        assertEquals(s.pop(), f1);
        assertEquals(s.size(), 0);

        try {
            s.pop();
            fail();
        } catch (Exception e) {
            // expected
        }
    }


}