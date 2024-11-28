package com.systextil.relatorio.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

record LoginRequest(
	@NotNull
	@Positive
	int codigoEmpresa,
	@NotBlank
    String login,
    @NotBlank
    String senha
) {}
