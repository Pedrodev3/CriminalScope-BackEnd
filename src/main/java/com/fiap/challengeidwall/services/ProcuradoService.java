package com.fiap.challengeidwall.services;

import com.fiap.challengeidwall.model.Procurado;
import com.fiap.challengeidwall.model.Status;
import com.fiap.challengeidwall.repositories.ProcuradoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProcuradoService {

    private final ProcuradoRepository procuradoRepository;

    public ProcuradoService(ProcuradoRepository procuradoRepository) {
        this.procuradoRepository = procuradoRepository;
    }

    public List<Procurado> getProcuradoAll() {
        return procuradoRepository.findAll();
    }

    public Optional<Procurado> getProcuradoById(Long id) {return procuradoRepository.findById(id);}

    public Optional<Procurado> getProcuradoByProcurado(String procurado) {return procuradoRepository.findByProcurado(procurado);}

    public Optional<List<Procurado>> getProcuradoByStatus(String status) {return procuradoRepository.findByStatus(status);}

    public Procurado saveProcurado(Procurado procurado) {
        return procuradoRepository.save(procurado);
    }

    public Optional<List<Procurado>> getProcuradoByStatusAndName(String status, String procurado) {return procuradoRepository.findByProcuradoContainingAndStatus(procurado, status);}

    public Optional<List<Procurado>> getProcuradoContainsName(String procurado) {return procuradoRepository.findByProcuradoContaining(procurado);}

    public void deleteAllProcurados() {
        procuradoRepository.deleteAll();
    }

    public void deleteProcuradoById(Long id) {
        procuradoRepository.deleteById(id);
    }

}
