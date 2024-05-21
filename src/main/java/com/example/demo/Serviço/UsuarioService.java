package com.example.demo.Serviço;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Modelo.Usuario;
import com.example.demo.Repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario findByUsername(String nome) {
        return usuarioRepository.findByNome(nome);
    }

    public void excluir(Long id) {
        usuarioRepository.deleteById(id);
    }
}
    

