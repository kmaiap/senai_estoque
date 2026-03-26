package br.senai.estoque.gerenciamento_estoque.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senai.estoque.gerenciamento_estoque.model.Material;
import br.senai.estoque.gerenciamento_estoque.model.MovimentacaoEstoque;
import br.senai.estoque.gerenciamento_estoque.model.TipoMovimentacao;
import br.senai.estoque.gerenciamento_estoque.repository.MovimentacaoEstoqueRepository;

@Service
@Transactional
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final MaterialService materialService;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository movimentacaoEstoqueRepository,
            MaterialService materialService) {
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
        this.materialService = materialService;
    }

    public List<MovimentacaoEstoque> listar() {
        return movimentacaoEstoqueRepository.findAll();
    }

    public List<MovimentacaoEstoque> listarRecentes() {
        return movimentacaoEstoqueRepository.findTop5ByOrderByDataMovimentacaoDesc();
    }

    public MovimentacaoEstoque buscarOuFalhar(Long id) {
        return movimentacaoEstoqueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movimentacao nao encontrada."));
    }

    public MovimentacaoEstoque criar(Long materialId, TipoMovimentacao tipo, Integer quantidade, String responsavel,
            String observacao) {
        validarDados(tipo, quantidade, responsavel);

        Material material = materialService.buscarOuFalhar(materialId);
        aplicarMovimentacao(material, tipo, quantidade);

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setMaterial(material);
        movimentacao.setTipo(tipo);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setResponsavel(responsavel.trim());
        movimentacao.setObservacao(textoOpcional(observacao));
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        return movimentacaoEstoqueRepository.save(movimentacao);
    }

    public MovimentacaoEstoque atualizar(Long id, Long materialId, TipoMovimentacao tipo, Integer quantidade,
            String responsavel, String observacao) {
        validarDados(tipo, quantidade, responsavel);

        MovimentacaoEstoque existente = buscarOuFalhar(id);
        desfazerMovimentacao(existente);

        Material material = materialService.buscarOuFalhar(materialId);
        aplicarMovimentacao(material, tipo, quantidade);

        existente.setMaterial(material);
        existente.setTipo(tipo);
        existente.setQuantidade(quantidade);
        existente.setResponsavel(responsavel.trim());
        existente.setObservacao(textoOpcional(observacao));
        existente.setDataMovimentacao(LocalDateTime.now());
        return movimentacaoEstoqueRepository.save(existente);
    }

    public void remover(Long id) {
        MovimentacaoEstoque movimentacao = buscarOuFalhar(id);
        desfazerMovimentacao(movimentacao);
        movimentacaoEstoqueRepository.delete(movimentacao);
    }

    public long contar() {
        return movimentacaoEstoqueRepository.count();
    }

    private void validarDados(TipoMovimentacao tipo, Integer quantidade, String responsavel) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo da movimentacao e obrigatorio.");
        }

        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        if (responsavel == null || responsavel.isBlank()) {
            throw new IllegalArgumentException("O responsavel e obrigatorio.");
        }
    }

    private void aplicarMovimentacao(Material material, TipoMovimentacao tipo, int quantidade) {
        int novoSaldo = material.getQuantidadeDisponivel();

        if (tipo == TipoMovimentacao.ENTRADA) {
            novoSaldo += quantidade;
        } else {
            novoSaldo -= quantidade;
        }

        if (novoSaldo < 0) {
            throw new IllegalArgumentException("Nao ha estoque suficiente para esta saida.");
        }

        material.setQuantidadeDisponivel(novoSaldo);
        materialService.salvar(material);
    }

    private void desfazerMovimentacao(MovimentacaoEstoque movimentacao) {
        Material material = movimentacao.getMaterial();
        int novoSaldo = material.getQuantidadeDisponivel();

        if (movimentacao.getTipo() == TipoMovimentacao.ENTRADA) {
            novoSaldo -= movimentacao.getQuantidade();
        } else {
            novoSaldo += movimentacao.getQuantidade();
        }

        if (novoSaldo < 0) {
            throw new IllegalArgumentException("Nao foi possivel desfazer a movimentacao.");
        }

        material.setQuantidadeDisponivel(novoSaldo);
        materialService.salvar(material);
    }

    private String textoOpcional(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
