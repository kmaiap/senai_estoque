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

import br.senai.estoque.gerenciamento_estoque.model.Material;
import br.senai.estoque.gerenciamento_estoque.service.MaterialService;

@RestController
@RequestMapping("/api/materiais")
public class MaterialRestController {

    private final MaterialService materialService;

    public MaterialRestController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public List<Material> listar() {
        return materialService.listar();
    }

    @GetMapping("/{id}")
    public Material buscar(@PathVariable Long id) {
        return materialService.buscarOuFalhar(id);
    }

    @PostMapping
    public ResponseEntity<Material> criar(@RequestBody MaterialRequest request) {
        Material material = new Material();
        material.setNome(request.nome());
        material.setDescricao(request.descricao());
        material.setQuantidadeDisponivel(request.quantidadeDisponivel());
        material.setUnidadeMedida(request.unidadeMedida());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(materialService.criar(material, request.categoriaId()));
    }

    @PutMapping("/{id}")
    public Material atualizar(@PathVariable Long id, @RequestBody MaterialRequest request) {
        Material material = new Material();
        material.setNome(request.nome());
        material.setDescricao(request.descricao());
        material.setQuantidadeDisponivel(request.quantidadeDisponivel());
        material.setUnidadeMedida(request.unidadeMedida());
        return materialService.atualizar(id, material, request.categoriaId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        materialService.remover(id);
        return ResponseEntity.noContent().build();
    }

    public record MaterialRequest(
            String nome,
            String descricao,
            int quantidadeDisponivel,
            String unidadeMedida,
            Long categoriaId) {
    }
}
