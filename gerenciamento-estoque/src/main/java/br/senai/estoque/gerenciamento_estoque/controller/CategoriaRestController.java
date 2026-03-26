package br.senai.estoque.gerenciamento_estoque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.senai.estoque.gerenciamento_estoque.model.CategoriaMaterial;
import br.senai.estoque.gerenciamento_estoque.service.CategoriaMaterialService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaRestController {

    private final CategoriaMaterialService categoriaMaterialService;

    public CategoriaRestController(CategoriaMaterialService categoriaMaterialService) {
        this.categoriaMaterialService = categoriaMaterialService;
    }

    @GetMapping
    public List<CategoriaMaterial> listar() {
        return categoriaMaterialService.listar();
    }

    @GetMapping("/{id}")
    public CategoriaMaterial buscar(@PathVariable Long id) {
        return categoriaMaterialService.buscarOuFalhar(id);
    }

    @PostMapping
    public ResponseEntity<CategoriaMaterial> criar(@RequestBody CategoriaMaterial categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaMaterialService.criar(categoria));
    }

    @PutMapping("/{id}")
    public CategoriaMaterial atualizar(@PathVariable Long id, @RequestBody CategoriaMaterial categoria) {
        return categoriaMaterialService.atualizar(id, categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        categoriaMaterialService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
