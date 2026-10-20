package senai499.com.br.espresso.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import senai499.com.br.espresso.model.Pedido;
import senai499.com.br.espresso.model.StatusPedido;
import senai499.com.br.espresso.model.Usuario;
import senai499.com.br.espresso.service.PedidoService;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final PedidoService pedidoService;

    @GetMapping
    public String painelAdministrativo(
            @RequestParam(defaultValue = "EM_ATENDIMENTO") String aba,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Regra de negócio: Acesso exclusivo para gerentes
        if (usuario == null || !usuario.isGerente()) {
            return "redirect:/login?acessoNegado=true";
        }

        StatusPedido statusFiltro;
        if ("FINALIZADOS".equalsIgnoreCase(aba) || "FINALIZADO".equalsIgnoreCase(aba)) {
            statusFiltro = StatusPedido.FINALIZADO;
            aba = "FINALIZADOS";
        } else {
            statusFiltro = StatusPedido.EM_ATENDIMENTO;
            aba = "EM_ATENDIMENTO";
        }

        List<Pedido> pedidosFiltrados = pedidoService.listarPorStatus(statusFiltro);
        List<Pedido> todosEmAtendimento = pedidoService.listarPorStatus(StatusPedido.EM_ATENDIMENTO);
        List<Pedido> todosFinalizados = pedidoService.listarPorStatus(StatusPedido.FINALIZADO);
        List<Pedido> todosPedidos = pedidoService.listarTodos();

        java.math.BigDecimal receitaTotal = todosPedidos.stream()
                .map(Pedido::getValorTotal)
                .filter(java.util.Objects::nonNull)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        model.addAttribute("usuarioLogado", usuario);
        model.addAttribute("abaAtiva", aba);
        model.addAttribute("pedidos", pedidosFiltrados);
        model.addAttribute("totalEmAtendimento", todosEmAtendimento.size());
        model.addAttribute("totalFinalizados", todosFinalizados.size());
        model.addAttribute("totalPedidos", todosPedidos.size());
        model.addAttribute("receitaTotal", receitaTotal);

        return "admin";
    }

    @PostMapping("/pedidos/{id}/finalizar")
    public String finalizarPedido(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null || !usuario.isGerente()) {
            return "redirect:/login?acessoNegado=true";
        }

        try {
            pedidoService.finalizarPedido(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Pedido #" + id + " finalizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao finalizar pedido: " + e.getMessage());
        }

        return "redirect:/admin?aba=EM_ATENDIMENTO";
    }

    @ResponseBody
    @PostMapping("/api/pedidos/{id}/finalizar")
    public ResponseEntity<?> finalizarPedidoApi(@PathVariable Long id) {
        try {
            Pedido finalizado = pedidoService.finalizarPedido(id);
            return ResponseEntity.ok(finalizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
