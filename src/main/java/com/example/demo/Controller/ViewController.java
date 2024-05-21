package com.example.demo.Controller;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.Modelo.BiLink;
import com.example.demo.Modelo.Role;
import com.example.demo.Repository.BiLinkRepository;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UsuarioRepository;
import com.example.demo.Serviço.RoleService;
import com.example.demo.Serviço.UsuarioService;
import org.springframework.security.core.Authentication;


@Controller
public class ViewController {

   @Autowired
    private BiLinkRepository biLinkRepository;

    @Autowired
    private RoleRepository roleRepository;


    @GetMapping("/login")
    public String index(Model model) {
        
        return "login"; // Retorna a página index.html
    }

	@GetMapping("/graficos")
    public String graficos(Model model, Authentication authentication) {


		// Obtém o usuário autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Filtra os BiLinks com base nas roles do usuário
        List<BiLink> links = biLinkRepository.findAll().stream()
                .filter(biLink -> biLink.hasRequiredRole(userDetails.getAuthorities()))
                .collect(Collectors.toList());

        List<Role> roles = roleRepository.findAll();
        model.addAttribute("links", links);
        model.addAttribute("roles", roles);
        
        return "graficos"; // Retorna a página index.html
    }

    // login invalido
	@GetMapping({ "/login-error" })
	public String loginError(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Crendenciais inválidas!");
		model.addAttribute("texto", "Login ou senha incorretos, tente novamente.");
		model.addAttribute("subtexto", "Acesso permitido apenas para cadastros já ativados.");
		return "login";
	}

	
}
