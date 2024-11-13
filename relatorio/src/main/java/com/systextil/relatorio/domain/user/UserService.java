package com.systextil.relatorio.domain.user;

import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.systextil.relatorio.infra.jwt.JwtService;

import br.com.intersys.systextil.global.Criptografia;

@Service
class UserService {
	
	private final UserRepository repository;
	private final JwtService jwtService;
	
	public UserService(UserRepository repository, JwtService jwtService) {
		this.repository = repository;
		this.jwtService = jwtService;
	}
	
	ResponseEntity<String> login(LoginRequest loginRequest) throws SQLException {
		User usuario = new User(loginRequest);

        if (repository.exists(usuario.getUsername(), usuario.getCodigoEmpresa())) {
            String senha = repository.getSenha(usuario.getUsername(), usuario.getCodigoEmpresa());
            String senhaDesembaralhada = null;

            try {
                senhaDesembaralhada = Criptografia.desembaralha(senha);
            } catch (NumberFormatException exception) {
                if (usuario.getPassword().equals(senha)) {
                    String token = jwtService.generateToken(usuario.getUsername());
                    
                    return ResponseEntity.ok().body(token);
                }
                return ResponseEntity.status(401).body("");
            }
            if (senhaDesembaralhada.equals(usuario.getPassword())) {
                String token = jwtService.generateToken(usuario.getUsername());
                
                return ResponseEntity.ok().body(token);
            }
            return ResponseEntity.status(401).body("");
        } else {
            return ResponseEntity.notFound().build();
        }
	}
}
