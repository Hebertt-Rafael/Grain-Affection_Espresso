package senai499.com.br.espresso.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import senai499.com.br.espresso.model.Categoria;
import senai499.com.br.espresso.model.Produto;
import senai499.com.br.espresso.repository.CategoriaRepository;
import senai499.com.br.espresso.repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public List<Produto> listarTodosDisponiveis() {
        return produtoRepository.findByDisponivelTrue();
    }

    public List<Produto> listarPorCategoria(Long categoriaId) {
        return produtoRepository.findByCategoriaIdAndDisponivelTrue(categoriaId);
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }
}
