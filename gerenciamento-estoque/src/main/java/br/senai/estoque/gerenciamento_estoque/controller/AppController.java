package br.senai.estoque.gerenciamento_estoque.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.senai.estoque.gerenciamento_estoque.model.AtivoPatrimonial;
import br.senai.estoque.gerenciamento_estoque.model.Material;
import br.senai.estoque.gerenciamento_estoque.service.AtivoPatrimonialService;
import br.senai.estoque.gerenciamento_estoque.service.CategoriaMaterialService;
import br.senai.estoque.gerenciamento_estoque.service.MaterialService;
import br.senai.estoque.gerenciamento_estoque.service.MovimentacaoEstoqueService;

@Controller
public class AppController {

    private final CategoriaMaterialService categoriaMaterialService;
    private final MaterialService materialService;
    private final MovimentacaoEstoqueService movimentacaoEstoqueService;
    private final AtivoPatrimonialService ativoPatrimonialService;

    public AppController(CategoriaMaterialService categoriaMaterialService,
            MaterialService materialService,
            MovimentacaoEstoqueService movimentacaoEstoqueService,
            AtivoPatrimonialService ativoPatrimonialService) {
        this.categoriaMaterialService = categoriaMaterialService;
        this.materialService = materialService;
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
        this.ativoPatrimonialService = ativoPatrimonialService;
    }

    @GetMapping("/app")
    public String appHome(@RequestParam(required = false) String produtoSalvo,
            @RequestParam(required = false) String ativoSalvo,
            HttpSession session,
            Model model) {
        if (!usuarioLogado(session)) {
            return "redirect:/login";
        }

        carregarDashboard(session, model);
        if (produtoSalvo != null) {
            model.addAttribute("sucessoMaterial", "Produto salvo com sucesso no relatorio.");
        }
        if (ativoSalvo != null) {
            model.addAttribute("sucessoAtivo", "Patrimonio salvo com sucesso no relatorio.");
        }
        return "app/index";
    }

    @PostMapping("/app/materiais")
    public String cadastrarMaterial(@RequestParam String nome,
            @RequestParam String descricao,
            @RequestParam int quantidadeDisponivel,
            @RequestParam String unidadeMedida,
            @RequestParam Long categoriaId,
            HttpSession session,
            Model model) {
        if (!usuarioLogado(session)) {
            return "redirect:/login";
        }

        Material material = new Material();
        material.setNome(nome);
        material.setDescricao(descricao);
        material.setQuantidadeDisponivel(quantidadeDisponivel);
        material.setUnidadeMedida(unidadeMedida);

        try {
            materialService.criar(material, categoriaId);
            return "redirect:/app?produtoSalvo=1";
        } catch (IllegalArgumentException ex) {
            carregarDashboard(session, model);
            model.addAttribute("erroMaterial", ex.getMessage());
            model.addAttribute("materialFormNome", nome);
            model.addAttribute("materialFormDescricao", descricao);
            model.addAttribute("materialFormQuantidade", quantidadeDisponivel);
            model.addAttribute("materialFormUnidade", unidadeMedida);
            model.addAttribute("materialFormCategoriaId", categoriaId);
            return "app/index";
        }
    }

    @PostMapping("/app/materiais/{id}/remover")
    public String removerMaterial(@PathVariable Long id, HttpSession session) {
        if (!usuarioLogado(session)) {
            return "redirect:/login";
        }

        materialService.remover(id);
        return "redirect:/app";
    }

    @PostMapping("/app/ativos")
    public String cadastrarAtivo(@RequestParam String nome,
            @RequestParam String descricao,
            @RequestParam String codigoPatrimonio,
            @RequestParam String localizacao,
            @RequestParam String estadoConservacao,
            HttpSession session,
            Model model) {
        if (!usuarioLogado(session)) {
            return "redirect:/login";
        }

        AtivoPatrimonial ativo = new AtivoPatrimonial();
        ativo.setNome(nome);
        ativo.setDescricao(descricao);
        ativo.setCodigoPatrimonio(codigoPatrimonio);
        ativo.setLocalizacao(localizacao);
        ativo.setEstadoConservacao(estadoConservacao);
        ativo.setAtivo(true);

        try {
            ativoPatrimonialService.criar(ativo);
            return "redirect:/app?ativoSalvo=1";
        } catch (IllegalArgumentException ex) {
            carregarDashboard(session, model);
            model.addAttribute("erroAtivo", ex.getMessage());
            model.addAttribute("ativoFormNome", nome);
            model.addAttribute("ativoFormDescricao", descricao);
            model.addAttribute("ativoFormCodigo", codigoPatrimonio);
            model.addAttribute("ativoFormLocalizacao", localizacao);
            model.addAttribute("ativoFormEstado", estadoConservacao);
            return "app/index";
        }
    }

    @PostMapping("/app/ativos/{id}/remover")
    public String removerAtivo(@PathVariable Long id, HttpSession session) {
        if (!usuarioLogado(session)) {
            return "redirect:/login";
        }

        ativoPatrimonialService.remover(id);
        return "redirect:/app";
    }

    private boolean usuarioLogado(HttpSession session) {
        Object usuarioLogado = session.getAttribute("usuarioLogado");
        return usuarioLogado instanceof Boolean logado && logado;
    }

    private void carregarDashboard(HttpSession session, Model model) {
        model.addAttribute("nome", session.getAttribute("nome"));
        model.addAttribute("nif", session.getAttribute("nif"));
        model.addAttribute("totalCategorias", categoriaMaterialService.contar());
        model.addAttribute("totalMateriais", materialService.contar());
        model.addAttribute("totalMovimentacoes", movimentacaoEstoqueService.contar());
        model.addAttribute("totalAtivos", ativoPatrimonialService.contar());
        model.addAttribute("categorias", categoriaMaterialService.listar());
        model.addAttribute("materiais", materialService.listar());
        model.addAttribute("movimentacoesRecentes", movimentacaoEstoqueService.listarRecentes());
        model.addAttribute("ativos", ativoPatrimonialService.listar());
    }
}
