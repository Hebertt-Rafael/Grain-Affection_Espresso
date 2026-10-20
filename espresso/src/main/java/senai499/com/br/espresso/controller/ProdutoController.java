package senai499.com.br.espresso.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import senai499.com.br.espresso.model.Categoria;
import senai499.com.br.espresso.model.Produto;
import senai499.com.br.espresso.model.Usuario;
import senai499.com.br.espresso.service.ProdutoService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping({"/produtos", "/produtos.html", "/cardapio"})
    public String exibirCardapio(
            @RequestParam(required = false) Long categoriaId,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);

        List<Categoria> categorias = produtoService.listarCategorias();
        model.addAttribute("categorias", categorias);

        List<Produto> produtos;
        if (categoriaId != null && categoriaId > 0) {
            produtos = produtoService.listarPorCategoria(categoriaId);
            model.addAttribute("categoriaAtiva", categoriaId);
        } else {
            produtos = produtoService.listarTodosDisponiveis();
            model.addAttribute("categoriaAtiva", 0L);
        }

        model.addAttribute("produtos", produtos);
        return "produtos";
    }

    // Endpoints REST para consumo dinâmico via Fetch API / Frontend separado
    @ResponseBody
    @GetMapping("/api/categorias")
    public ResponseEntity<List<Categoria>> listarCategoriasApi() {
        return ResponseEntity.ok(produtoService.listarCategorias());
    }

    @ResponseBody
    @GetMapping("/api/produtos")
    public ResponseEntity<List<Produto>> listarProdutosApi(@RequestParam(required = false) Long categoriaId) {
        if (categoriaId != null && categoriaId > 0) {
            return ResponseEntity.ok(produtoService.listarPorCategoria(categoriaId));
        }
        return ResponseEntity.ok(produtoService.listarTodosDisponiveis());
    }

    @ResponseBody
    @GetMapping("/api/produtos/{id}")
    public ResponseEntity<Produto> buscarProdutoPorIdApi(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
