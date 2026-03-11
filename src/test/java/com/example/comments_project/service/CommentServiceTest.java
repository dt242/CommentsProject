package com.example.comments_project.service;

import com.example.comments_project.model.Comment;
import com.example.comments_project.model.CommentDTO;
import com.example.comments_project.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment comment1;
    private Comment comment2;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        comment1 = new Comment();
        comment1.setId(1L);
        comment1.setPostId(10L);
        comment1.setUserId(1L);
        comment1.setContent("Test comment 1");
        comment1.setCreatedAt(LocalDateTime.now());
        comment1.setUpdatedAt(LocalDateTime.now());
        comment1.setDeleted(false);
        comment2 = new Comment();
        comment2.setId(2L);
        comment2.setPostId(10L);
        comment2.setUserId(2L);
        comment2.setContent("Test comment 2");
        comment2.setCreatedAt(LocalDateTime.now());
        comment2.setUpdatedAt(LocalDateTime.now());
        comment2.setDeleted(false);
        commentDTO = CommentDTO.builder()
                .postId(10L)
                .userId(1L)
                .content("Test comment 1")
                .build();
    }

    @Test
    void testGetCommentsByPostId_ShouldReturnDTOList() {
        when(commentRepository.findByPostIdAndIsDeletedFalse(10L))
                .thenReturn(Arrays.asList(comment1, comment2));
        List<CommentDTO> comments = commentService.getCommentsByPostId(10L);

        assertThat(comments).hasSize(2);
        assertThat(comments).extracting("content").containsExactly("Test comment 1", "Test comment 2");
    }

    @Test
    void testAddComment_ShouldSaveAndReturnDTO() {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment1);
        CommentDTO createdComment = commentService.addComment(commentDTO);

        assertThat(createdComment).isNotNull();
        assertThat(createdComment.getId()).isEqualTo(1L);
        assertThat(createdComment.getContent()).isEqualTo("Test comment 1");
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testDeleteComment_Success_ShouldSetDeletedFlag() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));
        commentService.deleteComment(1L);

        assertThat(comment1.isDeleted()).isTrue();
        verify(commentRepository, never()).save(any());
    }

    @Test
    void testDeleteComment_WhenNotFound_ShouldThrowException() {
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                commentService.deleteComment(99L)
        );

        assertThat(ex.getMessage()).isEqualTo("Comment not found with ID: 99");
    }

    @Test
    void testMapToCommentDTO_ShouldMapCorrectly() {
        CommentDTO mappedDTO = CommentService.mapToCommentDTO(comment1);

        assertThat(mappedDTO).isNotNull();
        assertThat(mappedDTO.getId()).isEqualTo(comment1.getId());
        assertThat(mappedDTO.getContent()).isEqualTo(comment1.getContent());
        assertThat(mappedDTO.getPostId()).isEqualTo(comment1.getPostId());
    }

    @Test
    void testMapToEntity_ShouldMapCorrectly() {
        Comment mappedEntity = CommentService.mapToEntity(commentDTO);

        assertThat(mappedEntity).isNotNull();
        assertThat(mappedEntity.getPostId()).isEqualTo(10L);
        assertThat(mappedEntity.getUserId()).isEqualTo(1L);
        assertThat(mappedEntity.getContent()).isEqualTo("Test comment 1");
    }
}