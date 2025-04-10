package com.example.comments_project.controller;

import com.example.comments_project.model.Comment;
import com.example.comments_project.model.CommentDTO;
import com.example.comments_project.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private Comment comment;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        comment = new Comment();
        comment.setPostId(1L);
        comment.setUserId(1L);
        comment.setContent("Test comment");

        commentDTO = CommentDTO.builder()
                .postId(1L)
                .userId(1L)
                .content("Test comment")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
    }

    @Test
    void testGetCommentsByPostId() {
        List<Comment> comments = List.of(comment);
        when(commentService.getCommentsByPostId(1L)).thenReturn(comments);

        ResponseEntity<List<CommentDTO>> response = commentController.getCommentsByPostId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Test comment", response.getBody().get(0).getContent());
    }

    @Test
    void testAddComment() {
        when(commentService.addComment(any(Comment.class))).thenReturn(comment);

        ResponseEntity<CommentDTO> response = commentController.addComment(commentDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test comment", response.getBody().getContent());
    }

    @Test
    void testDeleteComment() {
        doNothing().when(commentService).deleteComment(1L);

        ResponseEntity<Void> response = commentController.deleteComment(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(commentService, times(1)).deleteComment(1L);
    }

    @Test
    void testHandleNotFoundException() {
        RuntimeException exception = new RuntimeException("Not Found");

        ResponseEntity<String> response = commentController.handleNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody());
    }
}
