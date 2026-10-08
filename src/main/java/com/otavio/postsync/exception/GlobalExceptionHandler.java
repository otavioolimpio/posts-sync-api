package com.otavio.postsync.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Manipulador global de exceções da aplicação.
 *
 * <p>Intercepta exceções lançadas durante o processamento das requisições
 * e transforma os erros em respostas HTTP padronizadas para a API.</p>
 *
 * <p>O tratamento centralizado evita que cada controller precise implementar
 * individualmente a lógica de tratamento de erros.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções relacionadas à comunicação com APIs externas.
     *
     * <p>Retorna o status HTTP {@code 502 Bad Gateway}, indicando que a
     * aplicação recebeu ou tentou realizar uma comunicação com um serviço
     * externo, mas não conseguiu concluí-la corretamente.</p>
     *
     * @param ex exceção gerada durante a comunicação com a API externa
     * @return resposta HTTP com status 502 e informações sobre o erro
     */
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Map<String, Object>> handleExternalApiException(
            RestClientException ex) {

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_GATEWAY.value(),
                "error", "Bad Gateway",
                "message", "Falha ao comunicar com a API pública de postagens: "
                        + ex.getMessage()
        ));
    }

    /**
     * Trata exceções não previstas especificamente pela aplicação.
     *
     * <p>Retorna o status HTTP {@code 500 Internal Server Error} para indicar
     * que ocorreu um erro interno durante o processamento da requisição.</p>
     *
     * <p>A mensagem original da exceção não é retornada ao cliente, evitando
     * a exposição de detalhes internos da aplicação.</p>
     *
     * @param ex exceção inesperada ocorrida durante o processamento
     * @return resposta HTTP com status 500 e mensagem genérica de erro
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "error", "Internal Server Error",
                        "message", "Ocorreu um erro interno inesperado no servidor."
                ));
    }
}