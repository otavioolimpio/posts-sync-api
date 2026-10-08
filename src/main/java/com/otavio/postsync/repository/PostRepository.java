package com.otavio.postsync.repository;

import com.otavio.postsync.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável pela persistência da entidade {@link Post}.
 *
 * <p>Estende {@link JpaRepository}, disponibilizando operações CRUD
 * e outras funcionalidades de persistência fornecidas pelo Spring Data JPA.</p>
 *
 * <p>O identificador da entidade {@code Post} é do tipo {@link Long}.</p>
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
