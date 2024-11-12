package com.systextil.relatorio.domain.user;

import br.com.intersys.systextil.global.Criptografia;
import com.systextil.relatorio.infra.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class UserController {

    private final UserRepository repository;
    private final JwtService jwtService;
    
    public UserController(UserRepository repository, JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserRequest request) throws SQLException {
        User usuario = new User(request);

        if (repository.exists(usuario.getUsername())) {
            String senha = repository.getSenha(usuario.getUsername());
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
