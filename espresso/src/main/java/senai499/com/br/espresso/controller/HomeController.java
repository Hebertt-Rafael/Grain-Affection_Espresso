package senai499.com.br.espresso.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import senai499.com.br.espresso.model.Produto;
import senai499.com.br.espresso.model.Usuario;
import senai499.com.br.espresso.service.ProdutoService;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProdutoService produtoService;
    private static final Set<Long> IDS_DESTAQUE = Set.of(1L, 2L, 5L, 7L, 9L, 13L);

    @GetMapping({"/", "/index", "/index.html"})
    public String index(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);

        List<Produto> todos = produtoService.listarTodosDisponiveis();
        List<Produto> destaques = todos.stream()
                .filter(p -> p.getId() != null && IDS_DESTAQUE.contains(p.getId()))
                .toList();
        if (destaques.isEmpty()) {
            destaques = todos.stream().limit(6).toList();
        }

        model.addAttribute("produtosDestaque", destaques);
        return "index";
    }

    @GetMapping({"/sobre", "/sobre.html"})
    public String sobre(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);
        return "sobre";
    }

    @GetMapping({"/contato", "/contato.html"})
    public String contato(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);
        return "contato";
    }
}
