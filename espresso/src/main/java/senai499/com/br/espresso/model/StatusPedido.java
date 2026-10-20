package senai499.com.br.espresso.model;

public enum StatusPedido {
    EM_ATENDIMENTO("Em atendimento"),
    FINALIZADO("Finalizado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
