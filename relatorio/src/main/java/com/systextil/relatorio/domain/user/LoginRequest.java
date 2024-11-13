package com.systextil.relatorio.domain.user;

record LoginRequest(
	int codigoEmpresa,
    String login,
    String senha
) {}
