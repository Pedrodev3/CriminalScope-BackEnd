package com.fiap.challengeidwall.repositories;

import com.fiap.challengeidwall.model.Procurado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcuradoRepository extends JpaRepository<Procurado, Long> {


    Procurado findProcuradoById(Long id);

    Procurado findProcuradoByName(String name);
}
