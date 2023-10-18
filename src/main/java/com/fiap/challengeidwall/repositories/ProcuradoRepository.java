package com.fiap.challengeidwall.repositories;

import com.fiap.challengeidwall.model.Procurado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcuradoRepository extends JpaRepository<Procurado, Long> {
    Optional<Procurado> findByProcurado(String procurado);

    Optional<List<Procurado>> findByStatus(String status);

    Optional<List<Procurado>> findByProcuradoContainingAndStatus(String procurado, String status);

    Optional<List<Procurado>> findByProcuradoContaining(String procurado);
}
