package com.example.comments_project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = new Comment();
        comment.setPostId(1L);
        comment.setUserId(1L);
        comment.setContent("This is a test comment");
    }

    @Test
    void testCommentInitialization() {
        assertNotNull(comment);
        assertEquals(1L, comment.getPostId());
        assertEquals(1L, comment.getUserId());
        assertEquals("This is a test comment", comment.getContent());
    }

    @Test
    void testCreatedAtIsSetOnCreation() {
        assertNotNull(comment.getCreatedAt());
        assertTrue(comment.getCreatedAt().isBefore(LocalDateTime.now()) || comment.getCreatedAt().isEqual(LocalDateTime.now()));
    }

    @Test
    void testUpdatedAtIsNullInitially() {
        assertNull(comment.getUpdatedAt());
    }

    @Test
    void testIsDeletedDefaultsToFalse() {
        assertFalse(comment.isDeleted());
    }

    @Test
    void testPrePersistOnCreate() {
        comment.onCreate();
        assertNotNull(comment.getCreatedAt());
    }

    @Test
    void testPreUpdateOnUpdate() {
        comment.onUpdate();
        assertNotNull(comment.getUpdatedAt());
    }

    @Test
    void testCommentDefaultValues() {
        comment = new Comment();

        assertFalse(comment.isDeleted());
        assertNull(comment.getUpdatedAt());
    }
}
