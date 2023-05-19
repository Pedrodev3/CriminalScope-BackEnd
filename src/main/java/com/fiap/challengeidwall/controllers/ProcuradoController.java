package com.fiap.challengeidwall.controllers;

import com.fiap.challengeidwall.model.Procurado;
import com.fiap.challengeidwall.services.ProcuradoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping
    public ResponseEntity<List<Procurado>> getAll() {
        List<Procurado> procurados = procuradoService.findAll();
        return new ResponseEntity<>(procurados, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Procurado> findById(@PathVariable Long id) {
        Procurado procurado = procuradoService.getProcuradoById(id);
        if (procurado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(procurado, HttpStatus.OK);
    }

    @GetMapping("/nome/{name}")
    public ResponseEntity<Procurado> findByName(@PathVariable String name){
        Procurado procurado = procuradoService.getProcuradoByName(name);
        if (procurado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(procurado, HttpStatus.OK);
    }

}
