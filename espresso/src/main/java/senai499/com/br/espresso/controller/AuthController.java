package senai499.com.br.espresso.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import senai499.com.br.espresso.dto.CadastroUsuarioDto;
import senai499.com.br.espresso.dto.LoginDto;
import senai499.com.br.espresso.model.Usuario;
import senai499.com.br.espresso.service.UsuarioService;

import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String exibirLogin(
            @RequestParam(required = false) String erro,
            @RequestParam(required = false) String logout,
            @RequestParam(required = false) String acessoNegado,
            Model model) {

        if (erro != null) {
            model.addAttribute("mensagemErro", "E-mail ou senha inválidos. Tente novamente.");
        }
        if (logout != null) {
            model.addAttribute("mensagemSucesso", "Você encerrou sua sessão com sucesso.");
        }
        if (acessoNegado != null) {
            model.addAttribute("mensagemAlerta", "Área restrita. Por favor, faça login com uma conta autorizada.");
        }

        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String processarLogin(
            @ModelAttribute("loginDto") LoginDto loginDto,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Optional<Usuario> usuarioOpt = usuarioService.autenticar(loginDto.getEmail(), loginDto.getSenha());

        if (usuarioOpt.isEmpty()) {
            return "redirect:/login?erro=true";
        }

        Usuario usuario = usuarioOpt.get();
        session.setAttribute("usuarioLogado", usuario);

        if (usuario.isGerente()) {
            return "redirect:/admin";
        } else {
            return "redirect:/pedidos";
        }
    }

    @GetMapping("/cadastro")
    public String exibirCadastro(Model model) {
        model.addAttribute("cadastroDto", new CadastroUsuarioDto());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String processarCadastro(
            @ModelAttribute("cadastroDto") CadastroUsuarioDto dto,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            Usuario novoUsuario = usuarioService.cadastrar(dto);
            session.setAttribute("usuarioLogado", novoUsuario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastro realizado com sucesso! Seja bem-vindo(a), " + novoUsuario.getNome() + ".");
            return "redirect:/pedidos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("cadastroDto", dto);
            return "cadastro";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }

    @ResponseBody
    @PostMapping("/api/auth/login")
    public ResponseEntity<?> loginApi(@RequestBody LoginDto loginDto, HttpSession session) {
        Optional<Usuario> usuarioOpt = usuarioService.autenticar(loginDto.getEmail(), loginDto.getSenha());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("erro", "E-mail ou senha inválidos."));
        }
        Usuario u = usuarioOpt.get();
        session.setAttribute("usuarioLogado", u);
        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "nome", u.getNome(),
                "email", u.getEmail(),
                "telefone", u.getTelefone() != null ? u.getTelefone() : "",
                "endereco", u.getEndereco() != null ? u.getEndereco() : "",
                "perfil", u.getPerfil().name()
        ));
    }

    @ResponseBody
    @PostMapping("/api/auth/cadastro")
    public ResponseEntity<?> cadastroApi(@RequestBody CadastroUsuarioDto dto, HttpSession session) {
        try {
            Usuario u = usuarioService.cadastrar(dto);
            session.setAttribute("usuarioLogado", u);
            return ResponseEntity.ok(Map.of(
                    "id", u.getId(),
                    "nome", u.getNome(),
                    "email", u.getEmail(),
                    "telefone", u.getTelefone() != null ? u.getTelefone() : "",
                    "endereco", u.getEndereco() != null ? u.getEndereco() : "",
                    "perfil", u.getPerfil().name()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}
