package senai499.com.br.espresso.model;

public enum Perfil {
    CLIENTE("Cliente"),
    GERENTE("Gerente");

    private final String descricao;

    Perfil(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
