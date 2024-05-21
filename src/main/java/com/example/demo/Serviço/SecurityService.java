package com.example.demo.Serviço;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.Modelo.BiLink;
import com.example.demo.Modelo.Role;
import com.example.demo.Repository.BiLinkRepository;
import com.example.demo.Modelo.Usuario; // Importe a classe Usuario

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional; // Importe corretamente a classe Optional

@Service
public class SecurityService {

    @Autowired
    private BiLinkRepository biLinkRepository;

    public boolean hasRequiredRole(Long biLinkId, Object principal) {
        if (principal instanceof UserDetails) {
            Optional<BiLink> biLinkOptional = biLinkRepository.findById(biLinkId);
            if (biLinkOptional.isPresent()) {
                BiLink biLink = biLinkOptional.get();
                UserDetails userDetails = (UserDetails) principal;
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                for (Role role : ((Usuario) userDetails).getRoles()) {
                    authorities.add(new SimpleGrantedAuthority(role.getNome()));
                }
                return biLink.hasRequiredRole(authorities);
            }
        }
        return false;
    }
}