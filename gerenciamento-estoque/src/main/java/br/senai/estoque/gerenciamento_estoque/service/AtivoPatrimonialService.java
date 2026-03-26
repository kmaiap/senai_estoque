package br.senai.estoque.gerenciamento_estoque.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.senai.estoque.gerenciamento_estoque.model.AtivoPatrimonial;
import br.senai.estoque.gerenciamento_estoque.repository.AtivoPatrimonialRepository;

@Service
public class AtivoPatrimonialService {

    private final AtivoPatrimonialRepository ativoPatrimonialRepository;

    public AtivoPatrimonialService(AtivoPatrimonialRepository ativoPatrimonialRepository) {
        this.ativoPatrimonialRepository = ativoPatrimonialRepository;
    }

    public List<AtivoPatrimonial> listar() {
        return ativoPatrimonialRepository.findAll();
    }

    public AtivoPatrimonial buscarOuFalhar(Long id) {
        return ativoPatrimonialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ativo patrimonial nao encontrado."));
    }

    public AtivoPatrimonial criar(AtivoPatrimonial ativo) {
        validarAtivo(ativo);
        if (ativoPatrimonialRepository.existsByCodigoPatrimonioIgnoreCase(ativo.getCodigoPatrimonio())) {
            throw new IllegalArgumentException("Ja existe um ativo com este codigo patrimonial.");
        }

        return ativoPatrimonialRepository.save(ativo);
    }

    public AtivoPatrimonial atualizar(Long id, AtivoPatrimonial ativoAtualizado) {
        AtivoPatrimonial existente = buscarOuFalhar(id);
        validarAtivo(ativoAtualizado);

        if (!existente.getCodigoPatrimonio().equalsIgnoreCase(ativoAtualizado.getCodigoPatrimonio())
                && ativoPatrimonialRepository.existsByCodigoPatrimonioIgnoreCase(ativoAtualizado.getCodigoPatrimonio())) {
            throw new IllegalArgumentException("Ja existe um ativo com este codigo patrimonial.");
        }

        existente.setNome(ativoAtualizado.getNome());
        existente.setDescricao(textoOpcional(ativoAtualizado.getDescricao()));
        existente.setCodigoPatrimonio(ativoAtualizado.getCodigoPatrimonio());
        existente.setLocalizacao(ativoAtualizado.getLocalizacao());
        existente.setEstadoConservacao(ativoAtualizado.getEstadoConservacao());
        existente.setAtivo(ativoAtualizado.isAtivo());
        return ativoPatrimonialRepository.save(existente);
    }

    public void remover(Long id) {
        ativoPatrimonialRepository.deleteById(id);
    }

    public long contar() {
        return ativoPatrimonialRepository.count();
    }

    private void validarAtivo(AtivoPatrimonial ativo) {
        if (ativo == null || ativo.getNome() == null || ativo.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do ativo e obrigatorio.");
        }

        if (ativo.getCodigoPatrimonio() == null || ativo.getCodigoPatrimonio().isBlank()) {
            throw new IllegalArgumentException("O codigo patrimonial e obrigatorio.");
        }

        if (ativo.getLocalizacao() == null || ativo.getLocalizacao().isBlank()) {
            throw new IllegalArgumentException("A localizacao e obrigatoria.");
        }

        if (ativo.getEstadoConservacao() == null || ativo.getEstadoConservacao().isBlank()) {
            throw new IllegalArgumentException("O estado de conservacao e obrigatorio.");
        }

        ativo.setNome(ativo.getNome().trim());
        ativo.setDescricao(textoOpcional(ativo.getDescricao()));
        ativo.setCodigoPatrimonio(ativo.getCodigoPatrimonio().trim());
        ativo.setLocalizacao(ativo.getLocalizacao().trim());
        ativo.setEstadoConservacao(ativo.getEstadoConservacao().trim());
    }

    private String textoOpcional(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
