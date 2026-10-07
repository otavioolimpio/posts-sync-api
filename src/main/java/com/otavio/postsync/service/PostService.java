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

@Service
public class PostService {

    private final PostRepository postRepository;
    private final RestClient restClient;

    public PostService(PostRepository postRepository, RestClient restClient) {
        this.postRepository = postRepository;
        this.restClient = restClient;
    }

    @Transactional
    public List<Post> syncPosts() {
        // 1. Consumir a API pública
        PostDTO[] response = restClient.get()
                .uri("/posts")
                .retrieve()
                .body(PostDTO[].class);

        if (response == null || response.length == 0) {
            return List.of();
        }

        // 2. Mapear e atualizar/inserir na base de dados (Idempotência)
        List<Post> postsToSave = Arrays.stream(response).map(dto -> {
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
        }).toList();

        return postRepository.saveAll(postsToSave);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }
}