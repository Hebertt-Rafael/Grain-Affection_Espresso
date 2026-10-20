package senai499.com.br.espresso.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastroUsuarioDto {
    private String nome;
    private String email;
    private String senha;
    private String confirmarSenha;
    private String telefone;
    private String endereco;
    private String perfil; // Opcional, padrão CLIENTE
}
