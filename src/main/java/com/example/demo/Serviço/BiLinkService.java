package com.example.demo.Serviço;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Modelo.BiLink;
import com.example.demo.Modelo.Role;
import com.example.demo.Modelo.Usuario;
import com.example.demo.Repository.BiLinkRepository;

@Service
public class BiLinkService {

    @Autowired
    private BiLinkRepository biLinkRepository;

    public List<BiLink> getAccessibleBiLinks(Usuario usuario) {
        Set<Long> roleIds = usuario.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
        return biLinkRepository.findBiLinksByRoleIds(roleIds);
    }
}
