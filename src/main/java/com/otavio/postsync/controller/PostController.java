package com.otavio.postsync.controller;

import com.otavio.postsync.domain.Post;
import com.otavio.postsync.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controller responsável pelos endpoints REST relacionados aos posts.
 *
 * O Controller recebe as requisições HTTP, chama a camada de Service
 * e devolve a resposta para o cliente.
 *
 * @Fluxo:
 * Cliente -> Controller -> Service -> Repository -> PostgreSQL
 */
@RestController
@RequestMapping
public class PostController {

    /**
     * Serviço responsável pelas regras de negócio relacionadas aos posts.
     *
     * O Spring fornece automaticamente uma instância de PostService
     * através da injeção de dependência.
     */
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Sincroniza os posts da API externa com o banco de dados.
     *
     * Endpoint:
     * POST /sync
     *
     * @return lista de posts sincronizados com status HTTP 201.
     */
    @PostMapping("/sync")
    public ResponseEntity<List<Post>> syncPosts() {

        List<Post> syncedPosts = postService.syncPosts();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(syncedPosts);
    }

    /**
     * Retorna todos os posts armazenados no banco.
     *
     * Endpoint:
     * GET /posts
     *
     * @return lista de posts com status HTTP 200.
     */
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {

        List<Post> posts = postService.findAll();

        return ResponseEntity.ok(posts);
    }

    /**
     * Busca um post específico pelo seu ID.
     *
     * Endpoint:
     * GET /posts/{id}
     *
     * @param id identificador do post recebido através da URL.
     *
     * @return HTTP 200 caso o post seja encontrado;
     *         HTTP 404 caso o post não exista.
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {

        return postService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .build()
                );
    }

    /**
     * Verifica se a API está funcionando.
     *
     * Endpoint:
     * GET /health
     *
     * @return informações sobre o estado da API.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {

        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "message", "Posts Sync API is running smoothly"
        ));
    }
}