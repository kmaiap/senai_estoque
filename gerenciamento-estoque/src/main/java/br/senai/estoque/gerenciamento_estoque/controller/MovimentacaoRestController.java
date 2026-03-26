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

import br.senai.estoque.gerenciamento_estoque.model.MovimentacaoEstoque;
import br.senai.estoque.gerenciamento_estoque.model.TipoMovimentacao;
import br.senai.estoque.gerenciamento_estoque.service.MovimentacaoEstoqueService;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoRestController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    public MovimentacaoRestController(MovimentacaoEstoqueService movimentacaoEstoqueService) {
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
    }

    @GetMapping
    public List<MovimentacaoEstoque> listar() {
        return movimentacaoEstoqueService.listar();
    }

    @GetMapping("/{id}")
    public MovimentacaoEstoque buscar(@PathVariable Long id) {
        return movimentacaoEstoqueService.buscarOuFalhar(id);
    }

    @PostMapping
    public ResponseEntity<MovimentacaoEstoque> criar(@RequestBody MovimentacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimentacaoEstoqueService.criar(
                        request.materialId(),
                        request.tipo(),
                        request.quantidade(),
                        request.responsavel(),
                        request.observacao()));
    }

    @PutMapping("/{id}")
    public MovimentacaoEstoque atualizar(@PathVariable Long id, @RequestBody MovimentacaoRequest request) {
        return movimentacaoEstoqueService.atualizar(
                id,
                request.materialId(),
                request.tipo(),
                request.quantidade(),
                request.responsavel(),
                request.observacao());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        movimentacaoEstoqueService.remover(id);
        return ResponseEntity.noContent().build();
    }

    public record MovimentacaoRequest(
            Long materialId,
            TipoMovimentacao tipo,
            Integer quantidade,
            String responsavel,
            String observacao) {
    }
}
