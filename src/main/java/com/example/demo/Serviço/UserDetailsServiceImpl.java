package com.example.demo.Serviço;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.Modelo.Usuario;
import com.example.demo.Repository.UsuarioRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.error("Usuário não encontrado com email: {}", email);
        logger.info("Tentativa de carregar usuário com email: {}", email);
        
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Usuário não encontrado com email: {}", email);
                    return new UsernameNotFoundException("Usuário não encontrado");
                });

                List<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(role -> {
                    String roleName = role.getNome();
                    logger.info("Role carregada para o usuário {}: {}", usuario.getEmail(), roleName);
                    return new SimpleGrantedAuthority(roleName);
                })
                .collect(Collectors.toList());
    
        // Log para verificar as roles atribuídas ao usuário
        logger.info("Roles atribuídas ao usuário {}: {}", usuario.getEmail(), authorities);
    
        return new org.springframework.security.core.userdetails.User(usuario.getEmail(), usuario.getSenha(), authorities);
    }
}

