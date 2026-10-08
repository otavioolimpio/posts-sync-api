package com.otavio.postsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal responsável pela inicialização da aplicação Spring Boot.
 *
 * <p>A anotação {@link SpringBootApplication} combina três anotações essenciais do Spring:</p>
 * <ul>
 *   <li>{@code @Configuration}: Define a classe como fonte de definições de beans para o contexto da aplicação.</li>
 *   <li>{@code @EnableAutoConfiguration}: Habilita o mecanismo de autoconfiguração do Spring Boot com base nas dependências do projeto.</li>
 *   <li>{@code @ComponentScan}: Ativa a busca automática por componentes, serviços, repositórios e controllers no pacote atual e seus subpacotes.</li>
 * </ul>
 */
@SpringBootApplication
public class PostsSyncApiApplication {

	/**
	 * Ponto de entrada (main method) que inicializa a aplicação Java.
	 *
	 * @param args argumentos de linha de comando passados durante a execução da aplicação.
	 */
	public static void main(String[] args) {
		SpringApplication.run(PostsSyncApiApplication.class, args);
	}

}