package senai499.com.br.espresso.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import senai499.com.br.espresso.dto.PedidoRequestDto;
import senai499.com.br.espresso.model.*;
import senai499.com.br.espresso.repository.PedidoRepository;
import senai499.com.br.espresso.repository.ProdutoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public Pedido criarPedido(Usuario usuario, PedidoRequestDto dto) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não autenticado.");
        }
        if (dto.getProdutoId() == null) {
            throw new IllegalArgumentException("Selecione um produto do cardápio.");
        }
        if (dto.getQuantidade() == null || dto.getQuantidade() < 1) {
            dto.setQuantidade(1);
        }
        if (dto.getTipoAtendimento() == null) {
            throw new IllegalArgumentException("Informe o tipo de atendimento: Balcão, Mesa (1 a 15) ou Entrega.");
        }

        // Validação da regra de negócio: Atendimento no balcão, mesas (15) ou entrega
        Integer mesa = null;
        String enderecoEntrega = null;

        if (dto.getTipoAtendimento() == TipoAtendimento.MESA) {
            if (dto.getNumeroMesa() == null || dto.getNumeroMesa() < 1 || dto.getNumeroMesa() > 15) {
                throw new IllegalArgumentException("Para atendimento na mesa, selecione uma mesa válida entre 1 e 15.");
            }
            mesa = dto.getNumeroMesa();
        } else if (dto.getTipoAtendimento() == TipoAtendimento.ENTREGA) {
            String end = dto.getEnderecoEntrega();
            if ((end == null || end.isBlank()) && (usuario.getEndereco() != null && !usuario.getEndereco().isBlank())) {
                end = usuario.getEndereco();
            }
            if (end == null || end.isBlank()) {
                throw new IllegalArgumentException("Para pedidos com entrega (delivery), informe o endereço de entrega completo.");
            }
            enderecoEntrega = end.trim();
        }

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new IllegalArgumentException("Produto selecionado não encontrado."));

        BigDecimal precoUnitario = produto.getPreco();
        BigDecimal subtotal = precoUnitario.multiply(BigDecimal.valueOf(dto.getQuantidade()));
        BigDecimal total = subtotal;

        // Se for entrega, adiciona taxa padrão de entrega (R$ 5,00)
        if (dto.getTipoAtendimento() == TipoAtendimento.ENTREGA) {
            total = total.add(new BigDecimal("5.00"));
        }

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .dataPedido(LocalDateTime.now())
                .tipoAtendimento(dto.getTipoAtendimento())
                .numeroMesa(mesa)
                .enderecoEntrega(enderecoEntrega)
                .status(StatusPedido.EM_ATENDIMENTO)
                .valorTotal(total)
                .observacoes(dto.getObservacoes() != null ? dto.getObservacoes().trim() : null)
                .build();

        ItemPedido item = ItemPedido.builder()
                .produto(produto)
                .quantidade(dto.getQuantidade())
                .precoUnitario(precoUnitario)
                .subtotal(subtotal)
                .build();

        pedido.adicionarItem(item);

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByDataPedidoDesc(usuarioId);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAllByOrderByDataPedidoDesc();
    }

    public List<Pedido> listarPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatusOrderByDataPedidoDesc(status);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido finalizarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));
        pedido.setStatus(StatusPedido.FINALIZADO);
        return pedidoRepository.save(pedido);
    }
}
