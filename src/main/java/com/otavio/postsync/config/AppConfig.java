package com.otavio.postsync.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Classe responsável pelas configurações de componentes
 * utilizados pela aplicação.
 *
 * Neste caso, configura o RestClient utilizado para realizar
 * requisições HTTP à API externa JSONPlaceholder.
 */
@Configuration
public class AppConfig {

    /**
     * Cria e configura um RestClient para comunicação
     * com a API externa.
     *
     * O método é anotado com @Bean para que o Spring crie
     * e gerencie essa instância dentro do seu contexto.
     *
     * @return RestClient configurado com a URL base da API externa.
     */
    @Bean
    public RestClient restClient() {

        return RestClient.builder()

                .baseUrl("https://jsonplaceholder.typicode.com")

                .build();
    }
}