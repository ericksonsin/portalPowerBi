package com.example.demo.Modelo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class BiLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String url;
    private Integer orderNumber; 

    @OneToMany(mappedBy = "biLink", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BiLinkRole> biLinkRoles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Set<BiLinkRole> getBiLinkRoles() {
        return biLinkRoles;
    }

    public void setBiLinkRoles(Set<BiLinkRole> biLinkRoles) {
        this.biLinkRoles = biLinkRoles;
    }

    public Integer getOrder() {
        return orderNumber;
    }

    public void setOrder(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public boolean hasRequiredRole(Collection<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            for (BiLinkRole biLinkRole : biLinkRoles) {
                if (authority.getAuthority().equals(biLinkRole.getRole().getNome())) {
                    return true;
                }
            }
        }
        return false;
    }
    }



