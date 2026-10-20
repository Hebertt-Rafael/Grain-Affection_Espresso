package senai499.com.br.espresso.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import senai499.com.br.espresso.dto.CadastroUsuarioDto;
import senai499.com.br.espresso.model.Perfil;
import senai499.com.br.espresso.model.Usuario;
import senai499.com.br.espresso.repository.UsuarioRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Optional<Usuario> autenticar(String email, String senha) {
        if (email == null || senha == null) {
            return Optional.empty();
        }
        return usuarioRepository.findByEmail(email.trim().toLowerCase())
                .filter(u -> u.getSenha().equals(senha.trim()));
    }

    @Transactional
    public Usuario cadastrar(CadastroUsuarioDto dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }
        String emailFormatado = dto.getEmail().trim().toLowerCase();
        if (usuarioRepository.existsByEmail(emailFormatado)) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado.");
        }
        if (dto.getSenha() == null || dto.getSenha().length() < 4) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 4 caracteres.");
        }
        if (dto.getConfirmarSenha() != null && !dto.getSenha().equals(dto.getConfirmarSenha())) {
            throw new IllegalArgumentException("As senhas informadas não coincidem.");
        }

        Perfil perfil = Perfil.CLIENTE;
        if ("GERENTE".equalsIgnoreCase(dto.getPerfil())) {
            perfil = Perfil.GERENTE;
        }

        Usuario novoUsuario = Usuario.builder()
                .nome(dto.getNome() != null ? dto.getNome().trim() : "Cliente")
                .email(emailFormatado)
                .senha(dto.getSenha().trim())
                .telefone(dto.getTelefone() != null ? dto.getTelefone().trim() : null)
                .endereco(dto.getEndereco() != null ? dto.getEndereco().trim() : null)
                .perfil(perfil)
                .build();

        return usuarioRepository.save(novoUsuario);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}
