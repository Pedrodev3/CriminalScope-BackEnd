package com.fiap.challengeidwall.controllers;

import com.fiap.challengeidwall.model.Procurado;
import com.fiap.challengeidwall.services.ProcuradoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/procurados")
public class ProcuradoController {

    private final ProcuradoService procuradoService;

    public ProcuradoController(ProcuradoService procuradoService) {
        this.procuradoService = procuradoService;
    }

    public ResponseEntity<List<Procurado>> getAll() {
        List<Procurado> procurados = procuradoService.findAll();
        return new ResponseEntity<>(procurados, HttpStatus.OK);
    }
}
