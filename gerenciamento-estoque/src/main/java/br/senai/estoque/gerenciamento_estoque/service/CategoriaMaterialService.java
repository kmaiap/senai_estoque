package br.senai.estoque.gerenciamento_estoque.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.senai.estoque.gerenciamento_estoque.model.CategoriaMaterial;
import br.senai.estoque.gerenciamento_estoque.repository.CategoriaMaterialRepository;

@Service
public class CategoriaMaterialService {

    private final CategoriaMaterialRepository categoriaMaterialRepository;

    public CategoriaMaterialService(CategoriaMaterialRepository categoriaMaterialRepository) {
        this.categoriaMaterialRepository = categoriaMaterialRepository;
    }

    public List<CategoriaMaterial> listar() {
        return categoriaMaterialRepository.findAll();
    }

    public CategoriaMaterial buscarOuFalhar(Long id) {
        return categoriaMaterialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria nao encontrada."));
    }

    public CategoriaMaterial criar(CategoriaMaterial categoria) {
        validarCategoria(categoria);
        if (categoriaMaterialRepository.existsByNomeIgnoreCase(categoria.getNome())) {
            throw new IllegalArgumentException("Ja existe uma categoria com este nome.");
        }

        return categoriaMaterialRepository.save(categoria);
    }

    public CategoriaMaterial atualizar(Long id, CategoriaMaterial categoriaAtualizada) {
        CategoriaMaterial existente = buscarOuFalhar(id);
        validarCategoria(categoriaAtualizada);

        String novoNome = categoriaAtualizada.getNome().trim();
        if (!existente.getNome().equalsIgnoreCase(novoNome)
                && categoriaMaterialRepository.existsByNomeIgnoreCase(novoNome)) {
            throw new IllegalArgumentException("Ja existe uma categoria com este nome.");
        }

        existente.setNome(novoNome);
        existente.setDescricao(textoOpcional(categoriaAtualizada.getDescricao()));
        return categoriaMaterialRepository.save(existente);
    }

    public void remover(Long id) {
        categoriaMaterialRepository.deleteById(id);
    }

    public long contar() {
        return categoriaMaterialRepository.count();
    }

    private void validarCategoria(CategoriaMaterial categoria) {
        if (categoria == null || categoria.getNome() == null || categoria.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da categoria e obrigatorio.");
        }

        categoria.setNome(categoria.getNome().trim());
        categoria.setDescricao(textoOpcional(categoria.getDescricao()));
    }

    private String textoOpcional(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
