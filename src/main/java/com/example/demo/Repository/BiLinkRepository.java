package com.example.demo.Repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.Modelo.BiLink;

public interface BiLinkRepository extends JpaRepository<BiLink, Long> {

    @Query("SELECT b FROM BiLink b WHERE b.id IN (SELECT br.biLink.id FROM BiLinkRole br WHERE br.role.id IN :roleIds)")
    List<BiLink> findBiLinksByRoleIds(@Param("roleIds") Set<Long> roleIds);

    
}

