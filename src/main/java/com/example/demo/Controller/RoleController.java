package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Modelo.Role;
import com.example.demo.Serviço.RoleService;
import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.Repository.BiLinkRepository;
import com.example.demo.Repository.RoleRepository;

@Controller
public class RoleController {

    @Autowired
    private final RoleService roleService;
    private final UsuarioRepository usuarioRepository;
    private final BiLinkRepository biLinkRepository;

    @Autowired
    private RoleRepository roleRepository;

    public RoleController(RoleService roleService, UsuarioRepository usuarioRepository,
            BiLinkRepository biLinkRepository) {
        this.roleService = roleService;
        this.usuarioRepository = usuarioRepository;
        this.biLinkRepository = biLinkRepository;
    }

    @GetMapping("/cadastro")
    public String cadastroForm(Model model) {
        model.addAttribute("usuario", usuarioRepository.findAll());
        model.addAttribute("biLink", biLinkRepository.findAll());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("role", new Role()); // Adicionar o objeto role ao modelo

        return "cadastro";
    }

    @PostMapping("/salvar")
    public String salvarRole(@ModelAttribute Role role, RedirectAttributes redirectAttributes) {
        roleService.salvar(role);

        redirectAttributes.addFlashAttribute("alerta", "sucesso");
        redirectAttributes.addFlashAttribute("titulo", "Permissão adicionada com sucesso!");
        redirectAttributes.addFlashAttribute("texto", "A Permissão foi adicionada com sucesso.");

        return "redirect:/cadastro";
    }

    @GetMapping("/deletePermissao/{id}")
    public String excluirPermissao(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        roleRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("alerta", "sucesso");
        redirectAttributes.addFlashAttribute("titulo", "Permissão excluida com sucesso!");
        redirectAttributes.addFlashAttribute("texto", "A Permissão foi excluida com sucesso.");

        return "redirect:/cadastro";

    }

    @GetMapping("/editarPermissao/{id}") // Define uma rota GET que espera um ID como parâmetro na URL
    public String editarPermissao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) { // Define um método chamado 'editarPermissao'
                                                                        // que recebe um ID e um objeto 'Model'
        Role role = roleRepository.findById(id) // Busca um objeto 'Role' pelo ID fornecido
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id)); // Se o 'Role' não for
                                                                                        // encontrado, lança uma exceção
                                                                                        // com a mensagem "ID inválido:
                                                                                        // [id]"

        List<Role> roles = roleRepository.findAll(); // Busca todos os objetos 'Role' existentes

        model.addAttribute("role", role); // Adiciona o 'Role' encontrado ao objeto 'Model' com o nome "role"
        model.addAttribute("roles", roles); // Adiciona a lista de 'Role' ao objeto 'Model' com o nome "roles"

        redirectAttributes.addFlashAttribute("alerta", "sucesso");
        redirectAttributes.addFlashAttribute("titulo", "Permissão alterada com sucesso!");
        redirectAttributes.addFlashAttribute("texto", "A Permissão foi alterada com sucesso.");

        return "/cadastro"; // Retorna a view "/cadastro", que é usada tanto para criar um novo 'Role'
                            // quanto para editar um existente
    }

}