package senai499.com.br.espresso.model;

public enum TipoAtendimento {
    BALCAO("Balcão (Retirada Rápida)", "Balcão"),
    MESA("Mesa (Salão — Mesas 1 a 15)", "Mesa"),
    ENTREGA("Entrega / Delivery (+ R$ 5,00)", "Entrega (Delivery)");

    private final String descricao;
    private final String descricaoCurta;

    TipoAtendimento(String descricao, String descricaoCurta) {
        this.descricao = descricao;
        this.descricaoCurta = descricaoCurta;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDescricaoCurta() {
        return descricaoCurta;
    }
}
