package com.example.demo.Controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Modelo.BiLink;
import com.example.demo.Modelo.BiLinkRole;
import com.example.demo.Modelo.Role;
import com.example.demo.Modelo.Usuario;
import com.example.demo.Repository.BiLinkRepository;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.Serviço.RoleService;
import com.example.demo.Serviço.UsuarioService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BiLinkRepository biLinkRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // @GetMapping("/links")
    // public String listaLinks(Model model, Authentication authentication) {
    //     // Obtém o usuário autenticado
    //     UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    //     // Filtra os BiLinks com base nas roles do usuário
    //     List<BiLink> links = biLinkRepository.findAll().stream()
    //             .filter(biLink -> biLink.hasRequiredRole(userDetails.getAuthorities()))
    //             .collect(Collectors.toList());

    //     List<Role> roles = roleRepository.findAll();
    //     model.addAttribute("links", links);
    //     model.addAttribute("roles", roles);
    //     return "admin/links";
    // }


    @GetMapping("/links")
    public String listaLinks(Model model, Authentication authentication) {
        // Verificar se o usuário tem a role ADMIN
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            // Obtém o usuário autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Filtra os BiLinks com base nas roles do usuário
            List<BiLink> links = biLinkRepository.findAll().stream()
                    .filter(biLink -> biLink.hasRequiredRole(userDetails.getAuthorities()))
                    .collect(Collectors.toList());

            List<Role> roles = roleRepository.findAll();
            model.addAttribute("links", links);
            model.addAttribute("roles", roles);
            return "admin/links";
        } else {
            // Se o usuário não tiver a role ADMIN, redirecione para uma página de acesso
            // negado
            return "redirect:/graficos";
        }
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/links/add")
    public String adicionaLinks(@ModelAttribute BiLink biLink, @RequestParam("roles") Long roleId,
            RedirectAttributes redirectAttributes) {
        Role role = roleService.findById(roleId); // Busca a role pelo ID

        BiLinkRole biLinkRole = new BiLinkRole();
        biLinkRole.setBiLink(biLink);
        biLinkRole.setRole(role);

        biLink.getBiLinkRoles().clear(); // Limpa os BiLinkRoles existentes
        biLink.getBiLinkRoles().add(biLinkRole); // Adiciona o novo BiLinkRole ao BiLink

        biLinkRepository.save(biLink); // Salva o Link no banco de dados

        // Adicione a mensagem de sucesso aos flash attributes
        redirectAttributes.addFlashAttribute("alerta", "sucesso");
        redirectAttributes.addFlashAttribute("titulo", "Grafico Adicionado com sucesso!");
        redirectAttributes.addFlashAttribute("texto", "O grafico foi adicionado com sucesso.");

        return "redirect:/admin/links";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/novo")
    public String novoUsuario(Model model) {
        Usuario usuario = new Usuario();
        usuario.setId(1L); // Defina um ID válido
        model.addAttribute("usuario", usuario);

        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);

        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);

        return "admin/novo";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/save")
    public String salvarNovoUsuario(Usuario usuario, @RequestParam("roles") Set<Long> roleIds, RedirectAttributes redirectAttributes) {
        Set<Role> roles = roleService.findByIdIn(roleIds); // Supondo que você tenha um serviço para buscar as roles por
                                                           // IDs
        usuario.setRoles(roles); // Atribui as roles ao usuário
        usuarioService.save(usuario); // Salva o usuário no banco de dados

        redirectAttributes.addFlashAttribute("alerta", "sucesso");
        redirectAttributes.addFlashAttribute("titulo", "Usuário adicionado com sucesso!");
        redirectAttributes.addFlashAttribute("texto", "O usuário foi adicionado com sucesso.");

        return "redirect:/admin/novo";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editarUsuario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        List<Role> roles = roleRepository.findAll();

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", roles);
        model.addAttribute("isEditing", false); // Indicando que é uma operação de edição

        redirectAttributes.addFlashAttribute("alerta", "sucesso");
        redirectAttributes.addFlashAttribute("titulo", "Usuário editado com sucesso!");
        redirectAttributes.addFlashAttribute("texto", "O usuário foi editado com sucesso.");


        return "/admin/novo"; // A mesma view para criar um novo usuário, mas agora também para edição
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/delete/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("alerta", "sucesso");
        redirectAttributes.addFlashAttribute("titulo", "Usuário excluido com sucesso!");
        redirectAttributes.addFlashAttribute("texto", "O usuário foi excluido com sucesso.");

        return "redirect:/admin/novo";

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/listarGraficos")
    public String index(Model model) {

        List<BiLink> links = biLinkRepository.findAll();
        model.addAttribute("links", links);

        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);

        // Adicione um novo objeto Bilinks ao modelo
        model.addAttribute("bilinks", new BiLink()); // substitua 'Bilinks' pelo nome correto da classe se for diferente

        return "/admin/listarGraficos";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    // Método para editar um link
    @GetMapping("/links/edit/{id}")
    public String editarGraficos(@PathVariable Long id, Model model) {
        BiLink biLink = biLinkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

        List<Role> roles = roleRepository.findAll();

        model.addAttribute("bilinks", biLink);
        model.addAttribute("roles", roles);
        model.addAttribute("isEditing", false); // Indicando que é uma operação de edição

        return "/admin/links"; // Nome do seu arquivo Thymeleaf para a página de edição
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    // Método para excluir um link
    @GetMapping("/links/delete/{id}")
    public String deleteLink(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Aqui você pode deletar o link pelo ID
        // Exemplo:
        biLinkRepository.deleteById(id);

        // Adicione a mensagem de sucesso aos flash attributes
        redirectAttributes.addFlashAttribute("alerta", "sucesso");
        redirectAttributes.addFlashAttribute("titulo", "Link excluído com sucesso!");
        redirectAttributes.addFlashAttribute("texto", "O link foi excluído com sucesso.");

        return "redirect:/admin/listarGraficos"; // Redirecionar para a página de lista de links após a exclusão
    }
}
