/**
 * ============================================================
 * main.js — Entry Point Global do Front-end & Integração API
 * Grain & Affection · v3.0
 * ============================================================
 * Funciona de duas formas simultâneas:
 * 1. MODO STANDALONE (Front-end "Grain & Affection" aberto direto no navegador ou Live Server)
 * 2. MODO CONECTADO AO BACK-END ("espresso" rodando em http://localhost:8080)
 * ============================================================
 */

const GrainApp = (() => {
  const API_BASE = 'http://localhost';

  // Catálogo oficial de 21 produtos (7 categorias) com sugestões para Pinterest
  const PRODUTOS_CATALOGO = [
    // 1. Bebidas Quentes
    { id: 1, categoriaId: 1, categoria: 'Bebidas Quentes', nome: 'Café Expresso', preco: 14.80, imagem: 'img/espresso.jpg', badge: 'Clássico', pinterestQuery: 'espresso coffee aesthetic dark moody cafe', descricao: 'Extrato puro de grão 100% arábica de torra média, crema aveludada e notas intensas de cacau e caramelo.' },
    { id: 2, categoriaId: 1, categoria: 'Bebidas Quentes', nome: 'Cappuccino', preco: 26.65, imagem: 'img/cappuccino.jpg', badge: 'Mais Pedido', pinterestQuery: 'cappuccino latte art aesthetic cozy cafe', descricao: 'Harmonia perfeita entre espresso encorpado, leite vaporizado em microespuma sedosa e toque de canela.' },
    { id: 3, categoriaId: 1, categoria: 'Bebidas Quentes', nome: 'Mocaccino', preco: 28.50, imagem: 'img/mocha.jpg', badge: '', pinterestQuery: 'mocha coffee chocolate aesthetic cafe', descricao: 'Espresso duplo combinado com ganache de chocolate meio amargo 70%, leite cremoso e raspas de cacau.' },

    // 2. Bebidas Geladas
    { id: 4, categoriaId: 2, categoria: 'Bebidas Geladas', nome: 'Café Gelado (Iced Coffee)', preco: 24.50, imagem: 'img/icedlatte.jpg', badge: 'Refrescante', pinterestQuery: 'iced vanilla latte glass aesthetic cafe', descricao: 'Espresso duplo sobre pedras de gelo cristalino com leite cremoso e toque de fava de baunilha.' },
    { id: 5, categoriaId: 2, categoria: 'Bebidas Geladas', nome: 'Cold Brew', preco: 27.90, imagem: 'img/coldbrew.jpg', badge: '18h Infusão', pinterestQuery: 'cold brew coffee orange aesthetic glass', descricao: 'Café especial infusionado a frio lentamente por 18 horas, suave, refrescante e com notas cítricas.' },
    { id: 6, categoriaId: 2, categoria: 'Bebidas Geladas', nome: 'Frappé', preco: 31.00, imagem: 'img/macchiato.jpg', badge: '', pinterestQuery: 'caramel frappe coffee whipped cream aesthetic', descricao: 'Batido gelado e cremoso de café espresso especial, sorvete de creme, calda de caramelo salgado e chantilly.' },

    // 3. Padaria e Confeitaria
    { id: 7, categoriaId: 3, categoria: 'Padaria e Confeitaria', nome: 'Croissant', preco: 16.50, imagem: 'img/croissant.jpg', badge: 'Fornada do Dia', pinterestQuery: 'french butter croissant pastry aesthetic cafe', descricao: 'Massa folhada francesa de fermentação lenta, dourada e crocante por fora e macia com manteiga extra por dentro.' },
    { id: 8, categoriaId: 3, categoria: 'Padaria e Confeitaria', nome: 'Brownie', preco: 18.90, imagem: 'img/brownie.jpg', badge: '', pinterestQuery: 'fudge chocolate walnut brownie aesthetic', descricao: 'Brownie fudge de chocolate belga meio amargo com casquinha crocante, interior úmido e nozes pecan.' },
    { id: 9, categoriaId: 3, categoria: 'Padaria e Confeitaria', nome: 'Cheesecake', preco: 24.00, imagem: 'img/cheesecake.jpg', badge: 'Chef Pâtissier', pinterestQuery: 'berry cheesecake slice aesthetic cafe', descricao: 'Base crocante amanteigada, creme suave de cream cheese com toque cítrico e calda rústica de frutas vermelhas.' },

    // 4. Lanches Salgados
    { id: 10, categoriaId: 4, categoria: 'Lanches Salgados', nome: 'Sanduíche Natural', preco: 26.00, imagem: 'img/caprese.jpg', badge: 'Artesanal', pinterestQuery: 'ciabatta caprese sandwich pesto cafe aesthetic', descricao: 'Ciabatta artesanal tostada no azeite com muçarela de búfala, tomate confit, folhas verdes e pesto de manjericão.' },
    { id: 11, categoriaId: 4, categoria: 'Lanches Salgados', nome: 'Misto Quente', preco: 22.50, imagem: 'img/torrada.jpg', badge: '', pinterestQuery: 'grilled cheese croque monsieur brioche aesthetic', descricao: 'Pão brioche dourado na manteiga clarificada com queijo gruyère e prato derretidos e presunto royal.' },
    { id: 12, categoriaId: 4, categoria: 'Lanches Salgados', nome: 'Pão de Queijo Recheado', preco: 12.00, imagem: 'img/paodequeijo.jpg', badge: 'Mineiro', pinterestQuery: 'pao de queijo brazilian cheese bread aesthetic', descricao: 'Pão de queijo mineiro da Serra da Canastra assado na hora, recheado com requeijão cremoso de corte.' },

    // 5. Sobremesas
    { id: 13, categoriaId: 5, categoria: 'Sobremesas', nome: 'Tiramisù', preco: 34.00, imagem: 'img/tiramisu.jpg', badge: 'Especialidade', pinterestQuery: 'classic italian tiramisu cocoa aesthetic cafe', descricao: 'Sobremesa clássica italiana com biscoitos embebidos em café espresso, creme de mascarpone e cacau puro.' },
    { id: 14, categoriaId: 5, categoria: 'Sobremesas', nome: 'Petit Gâteau', preco: 36.50, imagem: 'img/affogato.jpg', badge: '', pinterestQuery: 'petit gateau chocolate lava cake vanilla ice cream aesthetic', descricao: 'Bolinho quente de chocolate nobre com centro fluido, acompanhado de gelato de baunilha e calda quente.' },
    { id: 15, categoriaId: 5, categoria: 'Sobremesas', nome: 'Pudim', preco: 21.00, imagem: 'img/pudim.jpg', badge: '', pinterestQuery: 'caramel flan creme caramel dessert aesthetic', descricao: 'Pudim de leite condensado lisinho e aveludado com fava de baunilha e calda âmbar de caramelo.' },

    // 6. Opções Saudáveis
    { id: 16, categoriaId: 6, categoria: 'Opções Saudáveis', nome: 'Salada de Frutas', preco: 21.50, imagem: 'img/salada-frutas.jpg', badge: 'Leve & Fresco', pinterestQuery: 'fresh fruit bowl mint chia aesthetic breakfast', descricao: 'Bowl refrescante de frutas da estação selecionadas com sementes de chia, hortelã fresca e mel silvestre.' },
    { id: 17, categoriaId: 6, categoria: 'Opções Saudáveis', nome: 'Iogurte com Granola', preco: 23.00, imagem: 'img/parfait-iogurte.jpg', badge: '', pinterestQuery: 'greek yogurt parfait granola berries aesthetic', descricao: 'Parfait de iogurte grego natural com granola artesanal de castanhas, melado e compota de frutas vermelhas.' },
    { id: 18, categoriaId: 6, categoria: 'Opções Saudáveis', nome: 'Tapioca', preco: 22.00, imagem: 'img/tapioca.jpg', badge: 'Sem Glúten', pinterestQuery: 'brazilian tapioca crepe cheese tomato aesthetic', descricao: 'Goma fininha e crocante na chapa recheada com queijo minas frescal, tomate seco artesanal e orégano.' },

    // 7. Produtos para Venda
    { id: 19, categoriaId: 7, categoria: 'Produtos para Venda', nome: 'Grãos de Café Especiais (250g)', preco: 48.00, imagem: 'img/graos-cafe.jpg', badge: 'Torra Exclusiva', pinterestQuery: 'specialty coffee bag beans Kraft minimal aesthetic', descricao: 'Pacote com válvula aromática de grãos 100% arábica de torra média, notas de chocolate, avelã e caramelo.' },
    { id: 20, categoriaId: 7, categoria: 'Produtos para Venda', nome: 'Caneca Personalizada', preco: 55.00, imagem: 'img/caneca.jpg', badge: 'Edição Limitada', pinterestQuery: 'handmade ceramic coffee mug minimalist aesthetic', descricao: 'Caneca exclusiva em cerâmica artesanal fosca (320ml) com detalhes dourados e selo Grain & Affection.' },
    { id: 21, categoriaId: 7, categoria: 'Produtos para Venda', nome: 'Geleia Artesanal', preco: 32.00, imagem: 'img/geleia.jpg', badge: '100% Natural', pinterestQuery: 'homemade berry jam glass jar rustic aesthetic', descricao: 'Pote de geleia caseira de frutas vermelhas cozida lentamente em tacho, sem conservantes (200g).' }
  ];

  const PEDIDOS_INICIAIS = [
    { id: 1, cliente: 'Ana Beatriz Lima', email: 'ana@exemplo.com', telefone: '(11) 91234-5678', dataPedido: '19/10/2026 08:30', itens: '1x Café Expresso, 1x Cappuccino', tipoAtendimento: 'BALCAO', tipoDescricao: 'Balcão', numeroMesa: null, enderecoEntrega: null, status: 'FINALIZADO', valorTotal: 41.45, observacoes: 'Sem açúcar no café, por favor.' },
    { id: 2, cliente: 'Lucas Oliveira', email: 'lucas@exemplo.com', telefone: '(11) 92345-6789', dataPedido: '19/10/2026 09:15', itens: '1x Café Gelado (Iced Coffee), 1x Misto Quente', tipoAtendimento: 'MESA', tipoDescricao: 'Mesa', numeroMesa: 4, enderecoEntrega: null, status: 'EM_ATENDIMENTO', valorTotal: 47.00, observacoes: 'Mesa próxima à janela.' },
    { id: 3, cliente: 'Juliana Santos', email: 'juliana@exemplo.com', telefone: '(11) 93456-7890', dataPedido: '19/10/2026 09:40', itens: '1x Tiramisù, 1x Croissant', tipoAtendimento: 'ENTREGA', tipoDescricao: 'Entrega (Delivery)', numeroMesa: null, enderecoEntrega: 'Alameda Santos, 450 - Cerqueira César', status: 'EM_ATENDIMENTO', valorTotal: 55.50, observacoes: 'Interfone tocar duas vezes.' },
    { id: 4, cliente: 'Ana Beatriz Lima', email: 'ana@exemplo.com', telefone: '(11) 91234-5678', dataPedido: '19/10/2026 10:10', itens: '1x Mocaccino', tipoAtendimento: 'MESA', tipoDescricao: 'Mesa', numeroMesa: 12, enderecoEntrega: null, status: 'FINALIZADO', valorTotal: 28.50, observacoes: 'Porção extra de canela.' },
    { id: 5, cliente: 'Lucas Oliveira', email: 'lucas@exemplo.com', telefone: '(11) 92345-6789', dataPedido: '19/10/2026 10:35', itens: '1x Petit Gâteau, 1x Grãos de Café Especiais (250g)', tipoAtendimento: 'ENTREGA', tipoDescricao: 'Entrega (Delivery)', numeroMesa: null, enderecoEntrega: 'Rua das Flores, 88 - Centro', status: 'EM_ATENDIMENTO', valorTotal: 89.50, observacoes: 'Embalar com cuidado especial.' }
  ];

  function getPedidosStorage() {
    try {
      const raw = localStorage.getItem('grain_pedidos');
      if (raw) return JSON.parse(raw);
    } catch (e) {}
    localStorage.setItem('grain_pedidos', JSON.stringify(PEDIDOS_INICIAIS));
    return [...PEDIDOS_INICIAIS];
  }

  function savePedidosStorage(lista) {
    try {
      localStorage.setItem('grain_pedidos', JSON.stringify(lista));
    } catch (e) {}
  }

  function getCustomPhotos() {
    try {
      return JSON.parse(localStorage.getItem('grain_custom_photos') || '{}');
    } catch (e) {
      return {};
    }
  }

  function setCustomPhoto(prodId, url) {
    const map = getCustomPhotos();
    if (url) {
      map[prodId] = url;
    } else {
      delete map[prodId];
    }
    localStorage.setItem('grain_custom_photos', JSON.stringify(map));
  }

  function applyCustomPhotosToDOM() {
    const map = getCustomPhotos();
    document.querySelectorAll('[data-product-id]').forEach(card => {
      const id = card.getAttribute('data-product-id');
      const img = card.querySelector('.card-img-wrapper img');
      if (img && map[id]) {
        img.src = map[id];
      }
    });
  }

  // Verifica status do Back-end em http://localhost
  async function checkBackendConnection() {
    try {
      const res = await fetch(`${API_BASE}/api/produtos`, { method: 'GET', mode: 'cors' });
      if (res.ok) {
        return true;
      }
    } catch (e) {
      // Modo standalone
    }
    return false;
  }

  // Inicializa filtros de categoria, busca e modo Pinterest no Cardápio
  function initMenuPage() {
    const grid = document.getElementById('menu-products-grid');
    if (!grid) return;

    const filterBtns = document.querySelectorAll('#category-filters .filter-btn');
    const searchInput = document.getElementById('menu-search-input');
    const emptyMsg = document.getElementById('menu-empty-msg');
    const pinBtn = document.getElementById('toggle-pinterest-mode');
    const pinBanner = document.getElementById('pinterest-helper-banner');

    let activeCat = '0';
    let searchTerm = '';

    function filterCards() {
      const cards = grid.querySelectorAll('.product-card');
      let visibleCount = 0;

      cards.forEach(card => {
        const cardCat = card.getAttribute('data-category-id') || '0';
        const title = (card.querySelector('.card-title')?.textContent || '').toLowerCase();
        const desc = (card.querySelector('.card-desc')?.textContent || '').toLowerCase();
        const catName = (card.querySelector('.card-category')?.textContent || '').toLowerCase();

        const matchesCat = activeCat === '0' || cardCat === activeCat;
        const matchesSearch = !searchTerm || title.includes(searchTerm) || desc.includes(searchTerm) || catName.includes(searchTerm);

        if (matchesCat && matchesSearch) {
          card.style.display = '';
          visibleCount++;
        } else {
          card.style.display = 'none';
        }
      });

      if (emptyMsg) {
        emptyMsg.hidden = visibleCount > 0;
      }
    }

    filterBtns.forEach(btn => {
      btn.addEventListener('click', (e) => {
        const catId = btn.getAttribute('data-cat-id');
        if (catId !== null) {
          e.preventDefault();
          activeCat = catId;
          filterBtns.forEach(b => b.classList.remove('active'));
          btn.classList.add('active');
          filterCards();
        }
      });
    });

    if (searchInput) {
      searchInput.addEventListener('input', (e) => {
        searchTerm = e.target.value.trim().toLowerCase();
        filterCards();
      });
    }

    // Modo Guia & Teste de Fotos do Pinterest
    if (pinBtn) {
      pinBtn.addEventListener('click', () => {
        document.body.classList.toggle('pinterest-mode');
        const active = document.body.classList.contains('pinterest-mode');
        pinBtn.classList.toggle('active', active);
        if (pinBanner) pinBanner.classList.toggle('show', active);
        if (active && typeof GrainAnimations !== 'undefined') {
          GrainAnimations.showToast('Modo Pinterest ativado! Veja o nome de cada arquivo ou teste links de fotos.', 'success');
        }
      });
    }

    // Botões de testar URL de foto do Pinterest em cada card
    grid.querySelectorAll('.btn-pin-url').forEach(btn => {
      btn.addEventListener('click', () => {
        const prodId = btn.getAttribute('data-prod-id');
        const prodNome = btn.getAttribute('data-prod-nome');
        const currentUrl = getCustomPhotos()[prodId] || '';
        const novaUrl = window.prompt(
          `Cole a URL da imagem do Pinterest (ou caminho local) para "${prodNome}":\n(Deixe em branco para restaurar a foto original da pasta img/)`,
          currentUrl
        );
        if (novaUrl === null) return;
        const card = grid.querySelector(`.product-card[data-product-id="${prodId}"]`);
        const img = card ? card.querySelector('.card-img-wrapper img') : null;
        if (novaUrl.trim() === '') {
          setCustomPhoto(prodId, '');
          if (img && img.dataset.defaultSrc) img.src = img.dataset.defaultSrc;
          if (typeof GrainAnimations !== 'undefined') {
            GrainAnimations.showToast(`Foto original de "${prodNome}" restaurada!`, 'success');
          }
        } else {
          setCustomPhoto(prodId, novaUrl.trim());
          if (img) img.src = novaUrl.trim();
          if (typeof GrainAnimations !== 'undefined') {
            GrainAnimations.showToast(`Foto de "${prodNome}" atualizada com sucesso!`, 'success');
          }
        }
      });
    });
  }

  // Inicializa Painel do Gerente (Modo Standalone + Sincronização Localhost)
  function initAdminPage() {
    const tbody = document.getElementById('admin-orders-tbody');
    if (!tbody || tbody.getAttribute('data-server-rendered') === 'true') return;

    const tabAtendimento = document.getElementById('tab-em-atendimento');
    const tabFinalizados = document.getElementById('tab-finalizados');
    const countAtendimento = document.getElementById('count-em-atendimento');
    const countFinalizados = document.getElementById('count-finalizados');
    const kpiTotal = document.getElementById('kpi-total-pedidos');
    const kpiReceita = document.getElementById('kpi-receita-total');
    const colAcao = document.getElementById('th-acao');

    let abaAtual = 'EM_ATENDIMENTO';

    const brl = (v) => Number(v || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

    function renderAdminTable() {
      const pedidos = getPedidosStorage();
      const emAtendimento = pedidos.filter(p => p.status === 'EM_ATENDIMENTO');
      const finalizados = pedidos.filter(p => p.status === 'FINALIZADO');
      const receitaTotal = pedidos.reduce((acc, p) => acc + Number(p.valorTotal || 0), 0);

      if (countAtendimento) countAtendimento.textContent = emAtendimento.length;
      if (countFinalizados) countFinalizados.textContent = finalizados.length;
      if (kpiTotal) kpiTotal.textContent = pedidos.length;
      if (kpiReceita) kpiReceita.textContent = brl(receitaTotal);

      if (colAcao) colAcao.style.display = abaAtual === 'EM_ATENDIMENTO' ? '' : 'none';

      const lista = abaAtual === 'EM_ATENDIMENTO' ? emAtendimento : finalizados;

      if (!lista.length) {
        tbody.innerHTML = `<tr><td colspan="8" style="text-align:center; padding: 2rem;">Nenhum pedido nesta aba no momento.</td></tr>`;
        return;
      }

      tbody.innerHTML = lista.map(p => {
        const detalheAtendimento = p.numeroMesa
          ? `<div><small>Mesa ${p.numeroMesa}</small></div>`
          : (p.enderecoEntrega ? `<div><small>${p.enderecoEntrega}</small></div>` : '');

        const btnFinalizar = abaAtual === 'EM_ATENDIMENTO'
          ? `<td><button type="button" class="btn btn-forest btn-finalizar-pedido" data-id="${p.id}" style="padding: 0.5rem 1rem; font-size: 0.8rem;">Finalizar</button></td>`
          : '';

        return `
          <tr>
            <td><strong>#${p.id}</strong></td>
            <td>${p.dataPedido}</td>
            <td><strong>${p.cliente}</strong><br><small>${p.telefone || ''}</small></td>
            <td>${p.itens}</td>
            <td><span class="status-badge ${p.status === 'FINALIZADO' ? 'status-finalizado' : 'status-atendimento'}">${p.tipoDescricao || p.tipoAtendimento}</span>${detalheAtendimento}</td>
            <td>${p.observacoes || '—'}</td>
            <td><strong>${brl(p.valorTotal)}</strong></td>
            ${btnFinalizar}
          </tr>
        `;
      }).join('');

      tbody.querySelectorAll('.btn-finalizar-pedido').forEach(btn => {
        btn.addEventListener('click', async () => {
          const id = Number(btn.getAttribute('data-id'));
          const todos = getPedidosStorage();
          const idx = todos.findIndex(item => Number(item.id) === id);
          if (idx > -1) {
            todos[idx].status = 'FINALIZADO';
            savePedidosStorage(todos);
          }
          try {
            await fetch(`${API_BASE}/admin/api/pedidos/${id}/finalizar`, { method: 'POST', mode: 'cors' });
          } catch (e) {}
          renderAdminTable();
          if (typeof GrainAnimations !== 'undefined') {
            GrainAnimations.showToast(`Pedido #${id} finalizado com sucesso!`, 'success');
          }
        });
      });
    }

    if (tabAtendimento) {
      tabAtendimento.addEventListener('click', (e) => {
        e.preventDefault();
        abaAtual = 'EM_ATENDIMENTO';
        tabAtendimento.classList.add('active');
        if (tabFinalizados) tabFinalizados.classList.remove('active');
        renderAdminTable();
      });
    }

    if (tabFinalizados) {
      tabFinalizados.addEventListener('click', (e) => {
        e.preventDefault();
        abaAtual = 'FINALIZADOS';
        tabFinalizados.classList.add('active');
        if (tabAtendimento) tabAtendimento.classList.remove('active');
        renderAdminTable();
      });
    }

    renderAdminTable();
  }

  // Inicializa Login & Cadastro no modo standalone
  function initAuthPages() {
    // Botões de preenchimento rápido de demonstração
    document.querySelectorAll('.demo-fill-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        const emailInput = document.getElementById('email');
        const senhaInput = document.getElementById('senha');
        if (emailInput && senhaInput) {
          emailInput.value = btn.getAttribute('data-email') || '';
          senhaInput.value = btn.getAttribute('data-senha') || '';
          if (typeof GrainAnimations !== 'undefined') {
            GrainAnimations.showToast('Credenciais preenchidas! Clique em Entrar.', 'success');
          }
        }
      });
    });

    const loginForm = document.getElementById('standalone-login-form');
    if (loginForm) {
      loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const email = document.getElementById('email').value.trim().toLowerCase();
        const senha = document.getElementById('senha').value.trim();

        let usuario = null;
        if ((email === 'gerente@grainaffection.com.br' || email === 'profvmoraes@grainaffection.com.br') && senha === 'admin123') {
          usuario = { id: 1, nome: 'Marina Alves (Gerente)', email, perfil: 'GERENTE', endereco: 'Rua Augusta, 1200 - Consolação' };
        } else if (senha.length >= 4) {
          const nomeFormatado = email.split('@')[0].replace(/[._]/g, ' ');
          usuario = { id: 3, nome: email === 'ana@exemplo.com' ? 'Ana Beatriz Lima' : nomeFormatado, email, perfil: 'CLIENTE', endereco: 'Rua Oscar Freire, 15 - Jardins' };
        }

        if (!usuario) {
          if (typeof GrainAnimations !== 'undefined') {
            GrainAnimations.showToast('E-mail ou senha inválidos. Use as contas de teste abaixo!', 'error');
          }
          return;
        }

        localStorage.setItem('grain_usuario_logado', JSON.stringify(usuario));
        if (typeof GrainAnimations !== 'undefined') {
          GrainAnimations.showToast(`Bem-vindo(a), ${usuario.nome}! Redirecionando...`, 'success');
        }
        setTimeout(() => {
          window.location.href = usuario.perfil === 'GERENTE' ? 'admin.html' : 'pedidos.html';
        }, 700);
      });
    }

    const cadastroForm = document.getElementById('standalone-cadastro-form');
    if (cadastroForm) {
      cadastroForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const nome = document.getElementById('nome').value.trim();
        const email = document.getElementById('email').value.trim().toLowerCase();
        const telefone = document.getElementById('telefone').value.trim();
        const endereco = document.getElementById('endereco').value.trim();
        const senha = document.getElementById('senha').value;
        const confirmar = document.getElementById('confirmarSenha').value;

        if (senha !== confirmar) {
          if (typeof GrainAnimations !== 'undefined') {
            GrainAnimations.showToast('As senhas informadas não coincidem!', 'error');
          }
          return;
        }

        const novoUsuario = { id: Date.now(), nome, email, telefone, endereco, perfil: 'CLIENTE' };
        localStorage.setItem('grain_usuario_logado', JSON.stringify(novoUsuario));
        if (typeof GrainAnimations !== 'undefined') {
          GrainAnimations.showToast(`Conta criada com sucesso! Bem-vindo(a), ${nome}!`, 'success');
        }
        setTimeout(() => {
          window.location.href = 'pedidos.html';
        }, 800);
      });
    }
  }

  return {
    API_BASE,
    PRODUTOS_CATALOGO,
    getPedidosStorage,
    savePedidosStorage,
    applyCustomPhotosToDOM,
    checkBackendConnection,
    initMenuPage,
    initAdminPage,
    initAuthPages
  };
})();

document.addEventListener('DOMContentLoaded', () => {
  // 1. Componentes Globais
  if (typeof GrainNavbar !== 'undefined') {
    GrainNavbar.init();
  }

  // 2. Animações Globais
  if (typeof GrainAnimations !== 'undefined') {
    GrainAnimations.initScrollReveal();
    GrainAnimations.initCounters();
    GrainAnimations.initCardTilt();
  }

  // 3. Aplica fotos customizadas do Pinterest (se o usuário tiver testado alguma)
  GrainApp.applyCustomPhotosToDOM();
  GrainApp.checkBackendConnection();

  // 4. Inicialização por página
  const page = document.body.getAttribute('data-page');

  if (page === 'home') {
    const typingTarget = document.querySelector('.hero-title .typing-target');
    if (typingTarget && typeof GrainAnimations !== 'undefined') {
      GrainAnimations.initTyping(
        typingTarget,
        ['Cafés Especiais', 'Padaria Fresquinha', 'Pedidos Rápidos', 'Afeto em Cada Xícara'],
        90
      );
    }

    if (typeof GrainCarousel !== 'undefined') {
      GrainCarousel.init('.hero');
    }
  } else if (page === 'cardapio') {
    GrainApp.initMenuPage();
  } else if (page === 'admin') {
    GrainApp.initAdminPage();
  } else if (page === 'login' || page === 'cadastro') {
    GrainApp.initAuthPages();
  }
});