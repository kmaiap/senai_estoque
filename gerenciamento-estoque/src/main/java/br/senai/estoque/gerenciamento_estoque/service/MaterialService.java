package br.senai.estoque.gerenciamento_estoque.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.senai.estoque.gerenciamento_estoque.model.CategoriaMaterial;
import br.senai.estoque.gerenciamento_estoque.model.Material;
import br.senai.estoque.gerenciamento_estoque.repository.MaterialRepository;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final CategoriaMaterialService categoriaMaterialService;

    public MaterialService(MaterialRepository materialRepository, CategoriaMaterialService categoriaMaterialService) {
        this.materialRepository = materialRepository;
        this.categoriaMaterialService = categoriaMaterialService;
    }

    public List<Material> listar() {
        return materialRepository.findAll();
    }

    public Material buscarOuFalhar(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Material nao encontrado."));
    }

    public Material criar(Material material, Long categoriaId) {
        validarMaterial(material);

        if (materialRepository.existsByNomeIgnoreCase(material.getNome())) {
            throw new IllegalArgumentException("Ja existe um material com este nome.");
        }

        CategoriaMaterial categoria = categoriaMaterialService.buscarOuFalhar(categoriaId);
        material.setCategoria(categoria);
        return materialRepository.save(material);
    }

    public Material atualizar(Long id, Material materialAtualizado, Long categoriaId) {
        Material existente = buscarOuFalhar(id);
        validarMaterial(materialAtualizado);

        if (!existente.getNome().equalsIgnoreCase(materialAtualizado.getNome())
                && materialRepository.existsByNomeIgnoreCase(materialAtualizado.getNome())) {
            throw new IllegalArgumentException("Ja existe um material com este nome.");
        }

        CategoriaMaterial categoria = categoriaMaterialService.buscarOuFalhar(categoriaId);
        existente.setNome(materialAtualizado.getNome());
        existente.setDescricao(textoOpcional(materialAtualizado.getDescricao()));
        existente.setQuantidadeDisponivel(materialAtualizado.getQuantidadeDisponivel());
        existente.setUnidadeMedida(materialAtualizado.getUnidadeMedida());
        existente.setCategoria(categoria);

        return materialRepository.save(existente);
    }

    public void remover(Long id) {
        materialRepository.deleteById(id);
    }

    public Material salvar(Material material) {
        return materialRepository.save(material);
    }

    public long contar() {
        return materialRepository.count();
    }

    private void validarMaterial(Material material) {
        if (material == null || material.getNome() == null || material.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do material e obrigatorio.");
        }

        if (material.getUnidadeMedida() == null || material.getUnidadeMedida().isBlank()) {
            throw new IllegalArgumentException("A unidade de medida e obrigatoria.");
        }

        if (material.getQuantidadeDisponivel() < 0) {
            throw new IllegalArgumentException("A quantidade disponivel nao pode ser negativa.");
        }

        material.setNome(material.getNome().trim());
        material.setDescricao(textoOpcional(material.getDescricao()));
        material.setUnidadeMedida(material.getUnidadeMedida().trim());
    }

    private String textoOpcional(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
