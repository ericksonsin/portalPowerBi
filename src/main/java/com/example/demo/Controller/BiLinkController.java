package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Modelo.BiLink;
import com.example.demo.Repository.BiLinkRepository;


@RestController
@RequestMapping("/bi-links")
public class BiLinkController {

    @Autowired
    private BiLinkRepository biLinkRepository;

    @GetMapping("/{id}")
    @PreAuthorize("@securityService.hasRequiredRole(#id, authentication.principal)")
    public ResponseEntity<BiLink> getBiLinkById(@PathVariable Long id) {
        java.util.Optional<BiLink> biLink = biLinkRepository.findById(id);
        if (biLink.isPresent()) {
            return ResponseEntity.ok(biLink.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}