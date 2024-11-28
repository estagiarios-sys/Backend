package com.systextil.relatorio.domain.user;

import jakarta.validation.constraints.NotBlank;

record LoginRequest(
	@NotBlank
	int codigoEmpresa,
	@NotBlank
    String login,
    @NotBlank
    String senha
) {}
