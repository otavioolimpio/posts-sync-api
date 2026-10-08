package com.otavio.postsync.dto;

/**
 * DTO (Data Transfer Object) utilizado para representar
 * os dados dos posts recebidos da API externa.
 *
 * O PostDTO é utilizado durante a comunicação com o
 * JSONPlaceholder e posteriormente seus dados são
 * convertidos para a entidade Post.
 */
public class PostDTO {

    private Long id;

    private Long userId;

    private String title;

    private String body;

    /**
     * Construtor vazio. É usado durante a desserialização do JSON recebido da API.
     */
    public PostDTO() {
    }

    /**
     * Construtor completo para criação de um PostDTO.
     *
     */
    public PostDTO(Long id, Long userId, String title, String body) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    /**
     * Retorna o ID do post.
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o ID do post.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retorna o ID do usuário.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Define o ID do usuário.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Retorna o título do post.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Define o título do post.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retorna o conteúdo do post.
     */
    public String getBody() {
        return body;
    }

    /**
     * Define o conteúdo do post.
     */
    public void setBody(String body) {
        this.body = body;
    }
}