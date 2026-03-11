package com.example.comments_project.controller;

import com.example.comments_project.model.CommentDTO;
import com.example.comments_project.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        commentDTO = CommentDTO.builder()
                .id(10L)
                .postId(1L)
                .userId(1L)
                .content("Test comment")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
    }

    @Test
    void testGetCommentsByPostId_ShouldReturnJsonArray() throws Exception {
        List<CommentDTO> comments = List.of(commentDTO);
        when(commentService.getCommentsByPostId(1L)).thenReturn(comments);

        mockMvc.perform(get("/api/comments/post/{postId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].content").value("Test comment"));
    }

    @Test
    void testAddComment_ShouldReturnCreated() throws Exception {
        when(commentService.addComment(any(CommentDTO.class))).thenReturn(commentDTO);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Test comment"))
                .andExpect(jsonPath("$.id").value(10));

        verify(commentService, times(1)).addComment(any(CommentDTO.class));
    }

    @Test
    void testDeleteComment_ShouldReturnNoContent() throws Exception {
        doNothing().when(commentService).deleteComment(1L);

        mockMvc.perform(delete("/api/comments/{commentId}", 1L))
                .andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteComment(1L);
    }

    @Test
    void testHandleNotFoundException_ShouldReturn404() throws Exception {
        doThrow(new EntityNotFoundException("Comment not found")).when(commentService).deleteComment(99L);

        mockMvc.perform(delete("/api/comments/{commentId}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Comment not found"));
    }
}