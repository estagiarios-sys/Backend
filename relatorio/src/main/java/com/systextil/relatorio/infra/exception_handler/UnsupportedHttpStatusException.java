package com.systextil.relatorio.infra.exception_handler;

import org.springframework.http.HttpStatusCode;

public class UnsupportedHttpStatusException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final HttpStatusCode httpStatusCode;

    public UnsupportedHttpStatusException(HttpStatusCode httpStatusCode) {
        super("Código de status HTTP não suportado: " + httpStatusCode.value());
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatusCode getHttpStatus() {
        return httpStatusCode;
    }
}