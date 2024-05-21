package com.example.demo.Serviço;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Modelo.Role;
import com.example.demo.Repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role salvar(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Set<Role> findByIdIn(Set<Long> ids) {
        return new HashSet<>(roleRepository.findAllById(ids));
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

}