package com.fiap.challengeidwall.controllers;

import com.fiap.challengeidwall.model.Procurado;
import com.fiap.challengeidwall.repositories.ProcuradoRepository;
import com.fiap.challengeidwall.services.ProcuradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/procurados")
public class ProcuradoController {

    @Autowired
    private ProcuradoRepository procuradoRepository;

    private final ProcuradoService procuradoService;

    public ProcuradoController(ProcuradoService procuradoService) {
        this.procuradoService = procuradoService;
    }

    @GetMapping
    public ResponseEntity<List<Procurado>> findAll() {
        List<Procurado> procurados = procuradoService.getProcuradoAll();
        return new ResponseEntity<>(procurados, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Procurado> findById(@PathVariable Long id) {
        Optional<Procurado> procurado = procuradoService.getProcuradoById(id);
        if (procurado.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(procurado.get(), HttpStatus.OK);
    }

    @GetMapping("/procurado/{name}")
    public ResponseEntity<Procurado> findByName(@PathVariable String name) {
        Optional<Procurado> procurado = procuradoService.getProcuradoByProcurado(name);
        if (procurado.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(procurado.get(), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Optional<List<Procurado>>> findByStatus(@PathVariable String status) {
        Optional<List<Procurado>> procurados = procuradoService.getProcuradoByStatus(String.valueOf(status));
        if (procurados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(procurados);
    }

    @GetMapping("/search")
    public ResponseEntity<String> findByNameAndOrganization(
            @RequestParam("name") Optional<String> name,
            @RequestParam("org") Optional<String> org
    ) {
        return ResponseEntity.ok(name + "" + org);
    }

    @PostMapping
    public ResponseEntity<Procurado> save(@RequestBody Procurado procurado) {
        Procurado savedProcurado = procuradoService.saveProcurado(procurado);
        return new ResponseEntity<>(savedProcurado, HttpStatus.CREATED);
    }

//    @DeleteMapping("/delete-all")
//    public ResponseEntity<String> deleteAllProcurados() {
//        try {
//            procuradoRepository.deleteAll();
//            return ResponseEntity.ok("All Procurados deletados com sucessos.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falhou em deletar todos os Procurados.");
//        }
//    }
}
