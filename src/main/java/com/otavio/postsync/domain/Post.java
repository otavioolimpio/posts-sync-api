package com.otavio.postsync.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade que representa um post persistido no banco de dados.
 *
 * <p>A classe é gerenciada pelo JPA/Hibernate e está mapeada
 * para a tabela {@code tb_posts} no PostgreSQL.</p>
 *
 * <p>Os dados dessa entidade são obtidos durante a sincronização
 * com a API externa e persistidos através do {@code PostRepository}.</p>
 */
@Entity
@Table(name = "tb_posts")
public class Post {

    /**
     * Identificador único do post.
     *
     * <p>É utilizado como chave primária da tabela.
     * O valor pode ser gerado automaticamente pelo banco de dados.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador do usuário proprietário do post na API de origem.
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * Título do post.
     */
    @Column(nullable = false, length = 255)
    private String title;

    /**
     * Conteúdo do post.
     *
     * <p>Mapeado como tipo {@code TEXT} no banco para suportar conteúdos mais extensos.</p>
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    /**
     * Data e hora em que o post foi criado.
     *
     * <p>O campo não pode ser nulo e não deve ser alterado
     * após a criação do registro.</p>
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Data e hora da última atualização do post no banco de dados.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Construtor padrão utilizado pelo JPA/Hibernate.
     */
    public Post() {
    }

    /**
     * Cria uma instância de {@code Post} com todos os seus atributos.
     *
     * @param id identificador do post
     * @param userId identificador do usuário responsável pelo post
     * @param title título do post
     * @param body conteúdo do post
     * @param createdAt data e hora de criação
     * @param updatedAt data e hora da última atualização
     */
    public Post(Long id, Long userId, String title, String body,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Compara dois posts com base em seus identificadores.
     *
     * <p>Dois objetos {@code Post} são considerados iguais quando
     * possuem o mesmo {@code id}.</p>
     *
     * @param o objeto que será comparado
     * @return {@code true} se os objetos representarem o mesmo post
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return Objects.equals(id, post.id);
    }

    /**
     * Gera o código hash do post com base em seu identificador.
     *
     * <p>O mesmo atributo utilizado no {@link #equals(Object)} é utilizado
     * para manter a consistência entre {@code equals} e {@code hashCode}.</p>
     *
     * @return código hash baseado no identificador do post
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}