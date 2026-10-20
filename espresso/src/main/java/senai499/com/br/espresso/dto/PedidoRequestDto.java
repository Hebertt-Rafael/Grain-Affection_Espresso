package senai499.com.br.espresso.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import senai499.com.br.espresso.model.TipoAtendimento;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDto {
    private Long usuarioId;
    private String emailCliente;
    private Long produtoId;
    private Integer quantidade;
    private TipoAtendimento tipoAtendimento;
    private Integer numeroMesa;      // 1 a 15 (se tipoAtendimento == MESA)
    private String enderecoEntrega;  // se tipoAtendimento == ENTREGA
    private String observacoes;
}
