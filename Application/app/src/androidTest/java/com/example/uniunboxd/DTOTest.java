package com.example.uniunboxd;

import static org.junit.Assert.assertEquals;

import com.example.uniunboxd.DTO.AuthenticationModel;
import com.example.uniunboxd.DTO.CourseModel;
import com.example.uniunboxd.DTO.FlagReviewModel;
import com.example.uniunboxd.DTO.ReplyModel;

import org.junit.Test;

public class DTOTest {
    @Test
    public void testAuthenticationModel() {
        var m = new AuthenticationModel("a@e.com", "pass");
        assertEquals(m.email, "a@e.com");
        assertEquals(m.password, "pass");
    }

    @Test
    public void testCourseModel() {
        var m = new CourseModel(123, "Algo", "22222", null);
        assertEquals(m.id, 123);
        assertEquals(m.name, "Algo");
        assertEquals(m.code, "22222");
        assertEquals(m.image, null);
    }

    @Test
    public void testFlagReviewModel() {
        var m = new FlagReviewModel(123, "hi");
        assertEquals(m.reviewId, 123);
        assertEquals(m.message, "hi");
    }

    @Test
    public void testReplyModel() {
        var m = new ReplyModel("hi", 123);
        assertEquals(m.text, "hi");
        assertEquals(m.reviewId, 123);
    }


}
