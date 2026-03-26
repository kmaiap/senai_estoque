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

import br.senai.estoque.gerenciamento_estoque.model.AtivoPatrimonial;
import br.senai.estoque.gerenciamento_estoque.service.AtivoPatrimonialService;

@RestController
@RequestMapping("/api/ativos")
public class AtivoPatrimonialRestController {

    private final AtivoPatrimonialService ativoPatrimonialService;

    public AtivoPatrimonialRestController(AtivoPatrimonialService ativoPatrimonialService) {
        this.ativoPatrimonialService = ativoPatrimonialService;
    }

    @GetMapping
    public List<AtivoPatrimonial> listar() {
        return ativoPatrimonialService.listar();
    }

    @GetMapping("/{id}")
    public AtivoPatrimonial buscar(@PathVariable Long id) {
        return ativoPatrimonialService.buscarOuFalhar(id);
    }

    @PostMapping
    public ResponseEntity<AtivoPatrimonial> criar(@RequestBody AtivoRequest request) {
        AtivoPatrimonial ativo = montarAtivo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ativoPatrimonialService.criar(ativo));
    }

    @PutMapping("/{id}")
    public AtivoPatrimonial atualizar(@PathVariable Long id, @RequestBody AtivoRequest request) {
        return ativoPatrimonialService.atualizar(id, montarAtivo(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        ativoPatrimonialService.remover(id);
        return ResponseEntity.noContent().build();
    }

    private AtivoPatrimonial montarAtivo(AtivoRequest request) {
        AtivoPatrimonial ativo = new AtivoPatrimonial();
        ativo.setNome(request.nome());
        ativo.setDescricao(request.descricao());
        ativo.setCodigoPatrimonio(request.codigoPatrimonio());
        ativo.setLocalizacao(request.localizacao());
        ativo.setEstadoConservacao(request.estadoConservacao());
        ativo.setAtivo(request.ativo() == null || request.ativo());
        return ativo;
    }

    public record AtivoRequest(
            String nome,
            String descricao,
            String codigoPatrimonio,
            String localizacao,
            String estadoConservacao,
            Boolean ativo) {
    }
}
