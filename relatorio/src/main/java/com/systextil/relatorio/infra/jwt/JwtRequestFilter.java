package com.systextil.relatorio.infra.jwt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.systextil.relatorio.domain.user.UserRepository;
import com.systextil.relatorio.infra.exception_handler.DataBaseConnectionException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

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
        final Logger logger = Logger.getLogger(getClass().getName());
        final HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(response);
    
        // Ignorar rotas específicas como Swagger e rotas públicas
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/swagger-ui") || 
            requestURI.startsWith("/v3/api-docs") 
        ){
            filterChain.doFilter(request, response);
            return;
        }
    
        String tokenJWT = recuperarToken(request);
    
        if (tokenJWT != null) {
            String subject = service.getUsernameToken(tokenJWT);
            UserDetails usuario = null;
    
            try {
                usuario = userRepository.getUser(subject);
            } catch (DataBaseConnectionException exception) {
                logger.log(Level.SEVERE, exception.getLocalizedMessage());
                wrappedResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
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