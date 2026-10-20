package senai499.com.br.espresso.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(length = 255)
    private String imagem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    @Builder.Default
    private Boolean disponivel = true;

    public String getBadgeTexto() {
        if (nome == null) return "";
        return switch (nome) {
            case "Café Expresso" -> "Clássico";
            case "Cappuccino" -> "Mais Pedido";
            case "Café Gelado (Iced Coffee)" -> "Refrescante";
            case "Cold Brew" -> "18h Infusão";
            case "Croissant" -> "Fornada do Dia";
            case "Cheesecake" -> "Chef Pâtissier";
            case "Sanduíche Natural" -> "Artesanal";
            case "Pão de Queijo Recheado" -> "Mineiro";
            case "Tiramisù" -> "Especialidade";
            case "Salada de Frutas" -> "Leve & Fresco";
            case "Tapioca" -> "Sem Glúten";
            case "Grãos de Café Especiais (250g)" -> "Torra Exclusiva";
            case "Caneca Personalizada" -> "Edição Limitada";
            default -> "";
        };
    }

    public String getBadgeClasse() {
        if (nome == null) return "";
        return switch (nome) {
            case "Café Gelado (Iced Coffee)", "Cheesecake", "Sanduíche Natural", "Salada de Frutas", "Caneca Personalizada" -> "badge-new";
            default -> "badge-bestseller";
        };
    }
}
