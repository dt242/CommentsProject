package com.example.comments_project.service;

import com.example.comments_project.model.Comment;
import com.example.comments_project.model.CommentDTO;
import com.example.comments_project.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment comment1;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        comment1 = new Comment();
        comment1.setId(1L);
        comment1.setPostId(1L);
        comment1.setUserId(1L);
        comment1.setContent("Test comment 1");
        comment1.setCreatedAt(LocalDateTime.now());
        comment1.setUpdatedAt(LocalDateTime.now());
        comment1.setDeleted(false);

        comment2 = new Comment();
        comment2.setId(2L);
        comment2.setPostId(1L);
        comment2.setUserId(2L);
        comment2.setContent("Test comment 2");
        comment2.setCreatedAt(LocalDateTime.now());
        comment2.setUpdatedAt(LocalDateTime.now());
        comment2.setDeleted(false);
    }

    @Test
    void testGetCommentsByPostId() {
        when(commentRepository.findByPostIdAndIsDeletedFalse(1L))
                .thenReturn(Arrays.asList(comment1, comment2));

        List<Comment> comments = commentService.getCommentsByPostId(1L);

        assertThat(comments).hasSize(2);
        assertThat(comments).extracting("content").containsExactly("Test comment 1", "Test comment 2");
    }

    @Test
    void testAddComment() {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment1);

        Comment createdComment = commentService.addComment(comment1);

        assertThat(createdComment).isNotNull();
        assertThat(createdComment.getId()).isEqualTo(1L);
        assertThat(createdComment.getContent()).isEqualTo("Test comment 1");
    }

    @Test
    void testDeleteComment() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment1);

        commentService.deleteComment(1L);

        verify(commentRepository, times(1)).save(comment1);
        assertThat(comment1.isDeleted()).isTrue();
    }

    @Test
    void testMapToCommentDTO() {
        CommentDTO commentDTO = CommentService.mapToCommentDTO(comment1);

        assertThat(commentDTO).isNotNull();
        assertThat(commentDTO.getId()).isEqualTo(comment1.getId());
        assertThat(commentDTO.getContent()).isEqualTo(comment1.getContent());
    }
}
