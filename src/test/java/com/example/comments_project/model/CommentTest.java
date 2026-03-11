package com.example.comments_project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void testOnCreate_ShouldSetCreatedAt() {
        Comment comment = new Comment();
        assertNull(comment.getCreatedAt());

        comment.onCreate();
        assertNotNull(comment.getCreatedAt());
    }

    @Test
    void testOnUpdate_ShouldSetUpdatedAt() {
        Comment comment = new Comment();
        assertNull(comment.getUpdatedAt());

        comment.onUpdate();
        assertNotNull(comment.getUpdatedAt());
    }
}