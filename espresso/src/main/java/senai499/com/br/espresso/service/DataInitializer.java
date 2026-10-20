package senai499.com.br.espresso.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import senai499.com.br.espresso.model.*;
import senai499.com.br.espresso.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() > 0 || usuarioRepository.count() > 0) {
            log.info("Base de dados já populada.");
            return;
        }

        log.info("Iniciando população automática do banco de dados (conforme requisitos do PDF)...");

        // 1. Usuários (2 Gerentes e 3 Clientes)
        Usuario gerente1 = Usuario.builder()
                .nome("Marina Alves (Gerente)")
                .email("gerente@grainaffection.com.br")
                .senha("admin123")
                .telefone("(11) 98765-4321")
                .endereco("Rua Augusta, 1200 - Consolação, São Paulo")
                .perfil(Perfil.GERENTE)
                .build();

        Usuario gerente2 = Usuario.builder()
                .nome("Victor Moraes (Gerente)")
                .email("profvmoraes@grainaffection.com.br")
                .senha("admin123")
                .telefone("(11) 99999-8888")
                .endereco("Av. Paulista, 1000 - São Paulo")
                .perfil(Perfil.GERENTE)
                .build();

        Usuario cliente1 = Usuario.builder()
                .nome("Ana Beatriz Lima")
                .email("ana@exemplo.com")
                .senha("cliente123")
                .telefone("(11) 91234-5678")
                .endereco("Rua Oscar Freire, 15 - Jardins")
                .perfil(Perfil.CLIENTE)
                .build();

        Usuario cliente2 = Usuario.builder()
                .nome("Lucas Oliveira")
                .email("lucas@exemplo.com")
                .senha("cliente123")
                .telefone("(11) 92345-6789")
                .endereco("Rua das Flores, 88 - Centro")
                .perfil(Perfil.CLIENTE)
                .build();

        Usuario cliente3 = Usuario.builder()
                .nome("Juliana Santos")
                .email("juliana@exemplo.com")
                .senha("cliente123")
                .telefone("(11) 93456-7890")
                .endereco("Alameda Santos, 450 - Cerqueira César")
                .perfil(Perfil.CLIENTE)
                .build();
        gerente1 = usuarioRepository.save(gerente1);
        gerente2 = usuarioRepository.save(gerente2);
        cliente1 = usuarioRepository.save(cliente1);
        cliente2 = usuarioRepository.save(cliente2);
        cliente3 = usuarioRepository.save(cliente3);

        // 2. Categorias (7 grupos do Anexo I)
        Categoria cat1 = categoriaRepository.save(new Categoria(null, "Bebidas Quentes", "Espressos aromáticos, cappuccinos e infusões"));
        Categoria cat2 = categoriaRepository.save(new Categoria(null, "Bebidas Geladas", "Iced coffees, cold brews e bebidas refrescantes"));
        Categoria cat3 = categoriaRepository.save(new Categoria(null, "Padaria e Confeitaria", "Croissants, bolos e confeitaria artesanal"));
        Categoria cat4 = categoriaRepository.save(new Categoria(null, "Lanches Salgados", "Sanduíches, pão de queijo recheado e salgados"));
        Categoria cat5 = categoriaRepository.save(new Categoria(null, "Sobremesas", "Pudins, tiramisù e doces artesanais"));
        Categoria cat6 = categoriaRepository.save(new Categoria(null, "Opções Saudáveis", "Opções leves, saladas, iogurte e tapioca"));
        Categoria cat7 = categoriaRepository.save(new Categoria(null, "Produtos para Venda", "Grãos de café especiais, canecas e itens exclusivos"));

        // 3. Produtos (3 itens de cada um dos 7 grupos do Anexo I = 21 produtos)
        // Grupo 1: Bebidas Quentes
        Produto p1 = produtoRepository.save(new Produto(null, "Café Expresso", "Extrato puro de grão 100% arábica de torra média, crema aveludada e notas intensas de cacau e caramelo.", new BigDecimal("14.80"), "/img/espresso.jpg", cat1, true));
        Produto p2 = produtoRepository.save(new Produto(null, "Cappuccino", "Harmonia perfeita entre espresso encorpado, leite vaporizado em microespuma sedosa e toque de canela.", new BigDecimal("26.65"), "/img/cappuccino.jpg", cat1, true));
        Produto p3 = produtoRepository.save(new Produto(null, "Mocaccino", "Espresso duplo combinado com ganache de chocolate meio amargo 70%, leite cremoso e raspas de cacau.", new BigDecimal("28.50"), "/img/mocha.jpg", cat1, true));

        // Grupo 2: Bebidas Geladas
        Produto p4 = produtoRepository.save(new Produto(null, "Café Gelado (Iced Coffee)", "Espresso duplo sobre pedras de gelo cristalino com leite cremoso e toque de fava de baunilha.", new BigDecimal("24.50"), "/img/icedlatte.jpg", cat2, true));
        Produto p5 = produtoRepository.save(new Produto(null, "Cold Brew", "Café especial infusionado a frio lentamente por 18 horas, suave, refrescante e com notas cítricas.", new BigDecimal("27.90"), "/img/coldbrew.jpg", cat2, true));
        Produto p6 = produtoRepository.save(new Produto(null, "Frappé", "Batido gelado e cremoso de café espresso especial, sorvete de creme, calda de caramelo salgado e chantilly.", new BigDecimal("31.00"), "/img/macchiato.jpg", cat2, true));

        // Grupo 3: Padaria e Confeitaria
        Produto p7 = produtoRepository.save(new Produto(null, "Croissant", "Massa folhada francesa de fermentação lenta, dourada e crocante por fora e macia com manteiga extra por dentro.", new BigDecimal("16.50"), "/img/croissant.jpg", cat3, true));
        Produto p8 = produtoRepository.save(new Produto(null, "Brownie", "Brownie fudge de chocolate belga meio amargo com casquinha crocante, interior úmido e nozes pecan.", new BigDecimal("18.90"), "/img/brownie.jpg", cat3, true));
        Produto p9 = produtoRepository.save(new Produto(null, "Cheesecake", "Base crocante amanteigada, creme suave de cream cheese com toque cítrico e calda rústica de frutas vermelhas.", new BigDecimal("24.00"), "/img/cheesecake.jpg", cat3, true));

        // Grupo 4: Lanches Salgados
        Produto p10 = produtoRepository.save(new Produto(null, "Sanduíche Natural", "Ciabatta artesanal tostada no azeite com muçarela de búfala, tomate confit, folhas verdes e pesto de manjericão.", new BigDecimal("26.00"), "/img/caprese.jpg", cat4, true));
        Produto p11 = produtoRepository.save(new Produto(null, "Misto Quente", "Pão brioche dourado na manteiga clarificada com queijo gruyère e prato derretidos e presunto royal.", new BigDecimal("22.50"), "/img/torrada.jpg", cat4, true));
        Produto p12 = produtoRepository.save(new Produto(null, "Pão de Queijo Recheado", "Pão de queijo mineiro da Serra da Canastra assado na hora, recheado com requeijão cremoso de corte.", new BigDecimal("12.00"), "/img/paodequeijo.jpg", cat4, true));

        // Grupo 5: Sobremesas
        Produto p13 = produtoRepository.save(new Produto(null, "Tiramisù", "Sobremesa clássica italiana com biscoitos embebidos em café espresso, creme de mascarpone e cacau puro.", new BigDecimal("34.00"), "/img/tiramisu.jpg", cat5, true));
        Produto p14 = produtoRepository.save(new Produto(null, "Petit Gâteau", "Bolinho quente de chocolate nobre com centro fluido, acompanhado de gelato de baunilha e calda quente.", new BigDecimal("36.50"), "/img/affogato.jpg", cat5, true));
        Produto p15 = produtoRepository.save(new Produto(null, "Pudim", "Pudim de leite condensado lisinho e aveludado com fava de baunilha e calda âmbar de caramelo.", new BigDecimal("21.00"), "/img/pudim.jpg", cat5, true));

        // Grupo 6: Opções Saudáveis
        Produto p16 = produtoRepository.save(new Produto(null, "Salada de Frutas", "Bowl refrescante de frutas da estação selecionadas com sementes de chia, hortelã fresca e mel silvestre.", new BigDecimal("21.50"), "/img/salada-frutas.jpg", cat6, true));
        Produto p17 = produtoRepository.save(new Produto(null, "Iogurte com Granola", "Parfait de iogurte grego natural com granola artesanal de castanhas, melado e compota de frutas vermelhas.", new BigDecimal("23.00"), "/img/parfait-iogurte.jpg", cat6, true));
        Produto p18 = produtoRepository.save(new Produto(null, "Tapioca", "Goma fininha e crocante na chapa recheada com queijo minas frescal, tomate seco artesanal e orégano.", new BigDecimal("22.00"), "/img/tapioca.jpg", cat6, true));

        // Grupo 7: Produtos para Venda
        Produto p19 = produtoRepository.save(new Produto(null, "Grãos de Café Especiais (250g)", "Pacote com válvula aromática de grãos 100% arábica de torra média, notas de chocolate, avelã e caramelo.", new BigDecimal("48.00"), "/img/graos-cafe.jpg", cat7, true));
        Produto p20 = produtoRepository.save(new Produto(null, "Caneca Personalizada", "Caneca exclusiva em cerâmica artesanal fosca (320ml) com detalhes dourados e selo Grain & Affection.", new BigDecimal("55.00"), "/img/caneca.jpg", cat7, true));
        Produto p21 = produtoRepository.save(new Produto(null, "Geleia Artesanal", "Pote de geleia caseira de frutas vermelhas cozida lentamente em tacho, sem conservantes (200g).", new BigDecimal("32.00"), "/img/geleia.jpg", cat7, true));

        // 4. Pedidos Iniciais (5 registros: Balcão, Mesas 1 a 15 e Entrega)
        // Pedido 1: Balcão - Finalizado
        Pedido ped1 = Pedido.builder()
                .usuario(cliente1)
                .dataPedido(LocalDateTime.now().minusHours(3))
                .tipoAtendimento(TipoAtendimento.BALCAO)
                .status(StatusPedido.FINALIZADO)
                .valorTotal(new BigDecimal("41.45"))
                .observacoes("Sem açúcar no café, por favor.")
                .build();
        ped1.adicionarItem(new ItemPedido(null, ped1, p1, 1, p1.getPreco(), p1.getPreco()));
        ped1.adicionarItem(new ItemPedido(null, ped1, p2, 1, p2.getPreco(), p2.getPreco()));
        pedidoRepository.save(ped1);

        // Pedido 2: Mesa 4 - Em atendimento
        Pedido ped2 = Pedido.builder()
                .usuario(cliente2)
                .dataPedido(LocalDateTime.now().minusHours(2))
                .tipoAtendimento(TipoAtendimento.MESA)
                .numeroMesa(4)
                .status(StatusPedido.EM_ATENDIMENTO)
                .valorTotal(new BigDecimal("47.00"))
                .observacoes("Mesa próxima à janela.")
                .build();
        ped2.adicionarItem(new ItemPedido(null, ped2, p4, 1, p4.getPreco(), p4.getPreco()));
        ped2.adicionarItem(new ItemPedido(null, ped2, p11, 1, p11.getPreco(), p11.getPreco()));
        pedidoRepository.save(ped2);

        // Pedido 3: Entrega - Em atendimento
        Pedido ped3 = Pedido.builder()
                .usuario(cliente3)
                .dataPedido(LocalDateTime.now().minusMinutes(50))
                .tipoAtendimento(TipoAtendimento.ENTREGA)
                .enderecoEntrega("Alameda Santos, 450 - Cerqueira César")
                .status(StatusPedido.EM_ATENDIMENTO)
                .valorTotal(new BigDecimal("55.50"))
                .observacoes("Interfone tocar duas vezes.")
                .build();
        ped3.adicionarItem(new ItemPedido(null, ped3, p13, 1, p13.getPreco(), p13.getPreco()));
        ped3.adicionarItem(new ItemPedido(null, ped3, p7, 1, p7.getPreco(), p7.getPreco()));
        pedidoRepository.save(ped3);

        // Pedido 4: Mesa 12 - Finalizado
        Pedido ped4 = Pedido.builder()
                .usuario(cliente1)
                .dataPedido(LocalDateTime.now().minusMinutes(35))
                .tipoAtendimento(TipoAtendimento.MESA)
                .numeroMesa(12)
                .status(StatusPedido.FINALIZADO)
                .valorTotal(new BigDecimal("28.50"))
                .observacoes("Porção extra de canela.")
                .build();
        ped4.adicionarItem(new ItemPedido(null, ped4, p3, 1, p3.getPreco(), p3.getPreco()));
        pedidoRepository.save(ped4);

        // Pedido 5: Entrega - Em atendimento
        Pedido ped5 = Pedido.builder()
                .usuario(cliente2)
                .dataPedido(LocalDateTime.now().minusMinutes(15))
                .tipoAtendimento(TipoAtendimento.ENTREGA)
                .enderecoEntrega("Rua das Flores, 88 - Centro")
                .status(StatusPedido.EM_ATENDIMENTO)
                .valorTotal(new BigDecimal("89.50"))
                .observacoes("Embalar com cuidado especial para transporte.")
                .build();
        ped5.adicionarItem(new ItemPedido(null, ped5, p14, 1, p14.getPreco(), p14.getPreco()));
        ped5.adicionarItem(new ItemPedido(null, ped5, p19, 1, p19.getPreco(), p19.getPreco()));
        pedidoRepository.save(ped5);

        log.info("População automática concluída com sucesso! Todos os requisitos atendidos.");
    }
}
