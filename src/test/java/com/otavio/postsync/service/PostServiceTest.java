package com.otavio.postsync.service;

import com.otavio.postsync.domain.Post;
import com.otavio.postsync.dto.PostDTO;
import com.otavio.postsync.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Classe de testes unitários para a camada de serviço {@link PostService}.
 *
 * <p>Utiliza o **JUnit 5** integrado ao **Mockito** via {@link MockitoExtension}
 * para isolar as regras de negócio de consulta e sincronização de dados.</p>

 * <p>Os cenários cobrem as operações fundamentais de leitura, idempotência de escrita
 * e resiliência na integração com a API externa.</p>
 */
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private PostService postService;

    /**
     * Teste 1: Busca por ID existente.
     *
     * <p>Cenário de leitura em que a entidade solicitada está persistida no banco.</p>
     */
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

    /**
     * Teste 2: Busca por ID inexistente.
     *
     * <p>Garante o retorno seguro de {@link Optional#empty()} sem disparar exceções.</p>
     */
    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar um post por ID inexistente")
    void shouldReturnEmptyOptionalWhenPostDoesNotExist() {
        // Arrange
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Post> result = postService.findById(99L);

        // Assert
        assertTrue(result.isEmpty());
        verify(postRepository, times(1)).findById(99L);
    }

    /**
     * Teste 3: Sincronização de novos registros (Criação).
     *
     * <p>Valida o fluxo em que dados novos da API externa são gravados com {@code createdAt}.</p>
     */
    @Test
    @DisplayName("Deve sincronizar novos posts salvando-os com data de criacao")
    void shouldSyncNewPostsSuccessfully() {
        // Arrange
        PostDTO dto = new PostDTO(1L, 10L, "Post Novo", "Conteudo Novo");
        PostDTO[] dtos = new PostDTO[]{dto};

        mockRestClientGetPosts(dtos);
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Post postSalvo = new Post(1L, 10L, "Post Novo", "Conteudo Novo", LocalDateTime.now(), null);
        when(postRepository.saveAll(anyList())).thenReturn(List.of(postSalvo));

        // Act
        List<Post> result = postService.syncPosts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Post Novo", result.get(0).getTitle());
        verify(postRepository, times(1)).saveAll(anyList());
    }

    /**
     * Teste 4: Idempotência na Sincronização (Atualização).
     *
     * <p>Garante que registros já existentes são atualizados com a marca temporal {@code updatedAt}.</p>
     */
    @Test
    @DisplayName("Deve atualizar post existente durante a sincronizacao definindo data de atualizacao")
    void shouldUpdateExistingPostDuringSync() {
        // Arrange
        PostDTO dto = new PostDTO(1L, 10L, "Titulo Atualizado", "Corpo Atualizado");
        PostDTO[] dtos = new PostDTO[]{dto};

        Post postExistente = new Post(1L, 10L, "Titulo Antigo", "Corpo Antigo", LocalDateTime.now().minusDays(1), null);

        mockRestClientGetPosts(dtos);
        when(postRepository.findById(1L)).thenReturn(Optional.of(postExistente));

        Post postAtualizado = new Post(1L, 10L, "Titulo Atualizado", "Corpo Atualizado", postExistente.getCreatedAt(), LocalDateTime.now());
        when(postRepository.saveAll(anyList())).thenReturn(List.of(postAtualizado));

        // Act
        List<Post> result = postService.syncPosts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Titulo Atualizado", result.get(0).getTitle());
        verify(postRepository, times(1)).saveAll(anyList());
    }

    /**
     * Teste 5: Resiliência contra respostas nulas/vazias da API externa.
     *
     * <p>Confirma que retornos nulos da API são tratados com lista vazia sem persistência indevida.</p>
     */
    @Test
    @DisplayName("Deve retornar lista vazia e nao persistir nada quando a API externa retornar corpo nulo")
    void shouldReturnEmptyListWhenApiReturnsNullBody() {
        // Arrange
        mockRestClientGetPosts(null);

        // Act
        List<Post> result = postService.syncPosts();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(postRepository, never()).saveAll(anyList());
    }

    /**
     * Método auxiliar para encadeamento e simulação do {@link RestClient}.
     */
    @SuppressWarnings("unchecked")
    private void mockRestClientGetPosts(PostDTO[] responseBody) {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/posts")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(PostDTO[].class)).thenReturn(responseBody);
    }
}