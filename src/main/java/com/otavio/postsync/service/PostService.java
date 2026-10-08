package com.otavio.postsync.service;

import com.otavio.postsync.domain.Post;
import com.otavio.postsync.dto.PostDTO;
import com.otavio.postsync.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas operações relacionadas aos posts.
 *
 * <p>Atua como intermediário entre os controllers, o banco de dados
 * e a API externa utilizada para sincronização dos posts.</p>
 *
 * <p>É responsável por buscar os dados externos, transformá-los em
 * entidades {@link Post} e persistir as informações no banco de dados.</p>
 */
@Service
public class PostService {

    private final PostRepository postRepository;
    private final RestClient restClient;

    /**
     * Cria uma instância do serviço com suas dependências.
     *
     * @param postRepository repositório responsável pela persistência dos posts
     * @param restClient cliente HTTP utilizado para comunicação com a API externa
     */
    public PostService(PostRepository postRepository, RestClient restClient) {
        this.postRepository = postRepository;
        this.restClient = restClient;
    }

    /**
     * Sincroniza os posts da API pública com o banco de dados.
     *
     * <p>Os dados são obtidos da API externa como {@link PostDTO},
     * transformados em entidades {@link Post} e posteriormente persistidos.</p>
     *
     * <p>A sincronização é idempotente: caso um post com o mesmo identificador
     * já exista no banco, seus dados são atualizados em vez de um novo registro
     * ser criado.</p>
     *
     * @return lista de posts criados ou atualizados durante a sincronização
     */
    @Transactional
    public List<Post> syncPosts() {
        PostDTO[] response = restClient.get()
                .uri("/posts")
                .retrieve()
                .body(PostDTO[].class);

        if (response == null || response.length == 0) {
            return List.of();
        }

        List<Post> postsToSave = Arrays.stream(response)
                .map(dto -> {
                    Optional<Post> existingPost = postRepository.findById(dto.getId());

                    Post post = existingPost.orElseGet(Post::new);

                    post.setId(dto.getId());
                    post.setUserId(dto.getUserId());
                    post.setTitle(dto.getTitle());
                    post.setBody(dto.getBody());

                    if (existingPost.isEmpty()) {
                        post.setCreatedAt(LocalDateTime.now());
                    } else {
                        post.setUpdatedAt(LocalDateTime.now());
                    }

                    return post;
                })
                .toList();

        return postRepository.saveAll(postsToSave);
    }

    /**
     * Busca todos os posts armazenados no banco de dados.
     *
     * @return lista contendo todos os posts persistidos
     */
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    /**
     * Busca um post pelo seu identificador.
     *
     * @param id identificador do post
     * @return {@link Optional} contendo o post quando encontrado,
     *         ou vazio caso não exista um post com o identificador informado
     */
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }
}