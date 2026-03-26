package br.senai.estoque.gerenciamento_estoque.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.service.AuthService;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String sucesso, Model model) {
        if (sucesso != null) {
            model.addAttribute("sucesso", "Conta criada com sucesso. Agora faca o login.");
        }

        return "auth/login";
    }

    @PostMapping("/auth/login")
    public String login(@RequestParam String nif,
            @RequestParam String senha,
            HttpSession session,
            Model model) {
        try {
            Funcionario funcionario = authService.autenticar(nif, senha)
                    .orElseThrow(() -> new IllegalArgumentException("NIF ou senha invalidos."));

            session.setAttribute("usuarioLogado", true);
            session.setAttribute("nif", funcionario.getNif());
            session.setAttribute("nome", funcionario.getNome());
            return "redirect:/app";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("erro", ex.getMessage());
            model.addAttribute("nifDigitado", nif);
            return "auth/login";
        }
    }

    @GetMapping("/cadastro")
    public String cadastroPage() {
        return "auth/cadastro";
    }

    @PostMapping("/auth/cadastro")
    public String cadastro(@RequestParam String nome,
            @RequestParam String nif,
            @RequestParam String senha,
            Model model) {
        try {
            authService.cadastrar(nome, nif, senha);
            return "redirect:/login?sucesso=1";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("erro", ex.getMessage());
            model.addAttribute("nomeDigitado", nome);
            model.addAttribute("nifDigitado", nif);
            return "auth/cadastro";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
