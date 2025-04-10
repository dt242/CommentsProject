package com.example.comments_project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentDTOTest {

    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        commentDTO = CommentDTO.builder()
                .id(1L)
                .postId(2L)
                .userId(3L)
                .content("This is a test comment")
                .createdAt(LocalDateTime.of(2025, 4, 10, 10, 0))
                .updatedAt(LocalDateTime.of(2025, 4, 10, 12, 0))
                .isDeleted(false)
                .build();
    }

    @Test
    void testCommentDTOInitialization() {
        assertNotNull(commentDTO);
        assertEquals(1L, commentDTO.getId());
        assertEquals(2L, commentDTO.getPostId());
        assertEquals(3L, commentDTO.getUserId());
        assertEquals("This is a test comment", commentDTO.getContent());
        assertEquals(LocalDateTime.of(2025, 4, 10, 10, 0), commentDTO.getCreatedAt());
        assertEquals(LocalDateTime.of(2025, 4, 10, 12, 0), commentDTO.getUpdatedAt());
        assertFalse(commentDTO.isDeleted());
    }

    @Test
    void testCommentDTOBuilder() {
        CommentDTO commentDTOUsingBuilder = CommentDTO.builder()
                .id(100L)
                .postId(200L)
                .userId(300L)
                .content("Builder test comment")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now().plusHours(1))
                .isDeleted(true)
                .build();

        assertNotNull(commentDTOUsingBuilder);
        assertEquals(100L, commentDTOUsingBuilder.getId());
        assertEquals(200L, commentDTOUsingBuilder.getPostId());
        assertEquals(300L, commentDTOUsingBuilder.getUserId());
        assertEquals("Builder test comment", commentDTOUsingBuilder.getContent());
        assertTrue(commentDTOUsingBuilder.isDeleted());
        assertNotNull(commentDTOUsingBuilder.getCreatedAt());
        assertNotNull(commentDTOUsingBuilder.getUpdatedAt());
    }
}
