package com.otavio.postsync.service;

import com.otavio.postsync.domain.Post;
import com.otavio.postsync.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("Deve retornar todos os posts armazenados no banco de dados com sucesso")
    void shouldFindAllPosts() {
        // Arrange
        Post post1 = new Post(1L, 100L, "Título 1", "Corpo 1", LocalDateTime.now(), null);
        Post post2 = new Post(2L, 100L, "Título 2", "Corpo 2", LocalDateTime.now(), null);
        when(postRepository.findAll()).thenReturn(List.of(post1, post2));

        // Act
        List<Post> result = postService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Título 1", result.get(0).getTitle());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar um post por ID existente com sucesso")
    void shouldFindPostByIdWhenExists() {
        // Arrange
        Post post = new Post(1L, 100L, "Título 1", "Corpo 1", LocalDateTime.now(), null);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // Act
        Optional<Post> result = postService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Título 1", result.get().getTitle());
        verify(postRepository, times(1)).findById(1L);
    }
}