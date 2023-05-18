package com.fiap.challengeidwall.services;

import com.fiap.challengeidwall.model.Procurado;
import com.fiap.challengeidwall.repositories.ProcuradoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcuradoService {

    private final ProcuradoRepository procuradoRepository;

    public ProcuradoService(ProcuradoRepository procuradoRepository) {
        this.procuradoRepository = procuradoRepository;
    }

    public List<Procurado> findAll() {
        return procuradoRepository.findAll();
    }

}
