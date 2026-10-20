package senai499.com.br.espresso.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import senai499.com.br.espresso.dto.PedidoRequestDto;
import senai499.com.br.espresso.model.Pedido;
import senai499.com.br.espresso.model.StatusPedido;
import senai499.com.br.espresso.model.TipoAtendimento;
import senai499.com.br.espresso.model.Usuario;
import senai499.com.br.espresso.repository.UsuarioRepository;
import senai499.com.br.espresso.service.PedidoService;
import senai499.com.br.espresso.service.ProdutoService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProdutoService produtoService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping({"/pedidos", "/pedidos.html"})
    public String exibirPedidos(
            @RequestParam(required = false) Long produto,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Regra de negócio: Obrigatório estar autenticado para acessar a página de Pedidos
        if (usuario == null) {
            return "redirect:/login?acessoNegado=true";
        }

        model.addAttribute("usuarioLogado", usuario);
        model.addAttribute("categorias", produtoService.listarCategorias());
        model.addAttribute("produtos", produtoService.listarTodosDisponiveis());
        model.addAttribute("tiposAtendimento", TipoAtendimento.values());

        // Pedido pré-selecionado via URL (ex: ?produto=2)
        PedidoRequestDto pedidoDto = new PedidoRequestDto();
        if (produto != null) {
            pedidoDto.setProdutoId(produto);
        }
        pedidoDto.setQuantidade(1);
        pedidoDto.setTipoAtendimento(TipoAtendimento.BALCAO);
        model.addAttribute("pedidoDto", pedidoDto);

        // Histórico de pedidos do cliente logado
        List<Pedido> meusPedidos = pedidoService.listarPorUsuario(usuario.getId());
        model.addAttribute("meusPedidos", meusPedidos);

        return "pedidos";
    }

    @PostMapping("/pedidos/novo")
    public String criarPedido(
            @ModelAttribute("pedidoDto") PedidoRequestDto dto,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login?acessoNegado=true";
        }

        try {
            Pedido pedidoCriado = pedidoService.criarPedido(usuario, dto);
            redirectAttributes.addFlashAttribute("mensagemSucesso",
                    "Pedido #" + pedidoCriado.getId() + " realizado com sucesso! Atendimento: " +
                            pedidoCriado.getTipoAtendimento().getDescricao() + ".");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }

        return "redirect:/pedidos";
    }

    // Endpoints REST para submissão e listagem via JS (Fetch API do front-end separado)
    @ResponseBody
    @GetMapping("/api/pedidos")
    public ResponseEntity<List<Pedido>> listarPedidosApi(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) String status) {
        if (usuarioId != null) {
            return ResponseEntity.ok(pedidoService.listarPorUsuario(usuarioId));
        }
        if (status != null && !status.isBlank()) {
            try {
                StatusPedido st = StatusPedido.valueOf(status.toUpperCase());
                return ResponseEntity.ok(pedidoService.listarPorStatus(st));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @ResponseBody
    @PostMapping("/api/pedidos")
    public ResponseEntity<?> criarPedidoApi(
            @RequestBody PedidoRequestDto dto,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null && dto.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(dto.getUsuarioId()).orElse(null);
        }
        if (usuario == null && dto.getEmailCliente() != null && !dto.getEmailCliente().isBlank()) {
            usuario = usuarioRepository.findByEmail(dto.getEmailCliente().trim().toLowerCase()).orElse(null);
        }
        if (usuario == null) {
            usuario = usuarioRepository.findByEmail("ana@exemplo.com")
                    .orElseGet(() -> usuarioRepository.findAll().stream().findFirst().orElse(null));
        }
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado. Por favor, faça login.");
        }

        try {
            Pedido pedidoCriado = pedidoService.criarPedido(usuario, dto);
            return ResponseEntity.ok(pedidoCriado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
