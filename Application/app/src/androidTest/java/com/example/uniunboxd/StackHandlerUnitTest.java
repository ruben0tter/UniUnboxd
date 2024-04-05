package com.example.uniunboxd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
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
        assertSame(a, b);
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

        s.push(f1);
        s.push(f1);
        s.push(f1);
        s.push(f1);
        assertEquals(s.size(), 4);
        assertFalse(s.empty());
        s.clear();
        assertEquals(s.size(), 0);
        assertTrue(s.empty());
    }
}
