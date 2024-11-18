package com.systextil.relatorio.infra.jwt;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.systextil.relatorio.domain.user.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtService service;

    private final UserRepository userRepository;

    public JwtRequestFilter(JwtService service, UserRepository userRepository) {
    	this.service = service;
    	this.userRepository = userRepository;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String tokenJWT = recuperarToken(request);
        
        if (tokenJWT != null) {
            String subject = service.getUsernameToken(tokenJWT);
            UserDetails usuario = null;
            
			try {
				usuario = userRepository.getUser(subject);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (usuario != null) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
	            SecurityContextHolder.getContext().setAuthentication(authentication);
			}
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "").trim();
        }
        return null;
    }
}