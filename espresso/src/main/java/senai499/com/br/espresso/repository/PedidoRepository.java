package senai499.com.br.espresso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senai499.com.br.espresso.model.Pedido;
import senai499.com.br.espresso.model.StatusPedido;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioIdOrderByDataPedidoDesc(Long usuarioId);
    List<Pedido> findByStatusOrderByDataPedidoDesc(StatusPedido status);
    List<Pedido> findAllByOrderByDataPedidoDesc();
}
