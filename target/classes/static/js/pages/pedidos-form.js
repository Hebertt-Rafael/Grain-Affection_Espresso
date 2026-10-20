/**
 * pedidos-form.js — Interações do formulário de pedido (Front-end & Back-end).
 * - Exige login no modo standalone (redireciona para login.html se não autenticado)
 * - Pré-seleciona produto vindo da URL (?produto=ID)
 * - Exibe campos de mesa (1 a 15) / endereço conforme o tipo de atendimento
 * - Calcula o resumo/total em tempo real (incluindo taxa de entrega R$ 5,00)
 * - Salva o pedido para acompanhamento dos Gerentes (Admin) no Painel
 */
(() => {
  const form = document.getElementById('order-form');
  if (!form) return;

  const isStandalone = form.getAttribute('data-standalone-form') === 'true';

  const produto = document.getElementById('produto-select');
  const quantidade = document.getElementById('quantidade');
  const tipo = document.getElementById('tipoAtendimento');
  const grupoMesa = document.getElementById('grupo-mesa');
  const grupoEndereco = document.getElementById('grupo-endereco');
  const inputMesa = document.getElementById('numeroMesa');
  const inputEndereco = document.getElementById('enderecoEntrega');
  const inputObs = document.getElementById('observacoes');
  const resumo = document.getElementById('order-summary');
  const clienteInput = document.getElementById('cliente-display');

  const brl = (v) => Number(v || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

  // Pré-seleciona produto se vier na query string (?produto=ID)
  const params = new URLSearchParams(window.location.search);
  const prodParam = params.get('produto');
  if (prodParam && produto) {
    produto.value = prodParam;
  }

  // Verifica usuário logado no modo standalone
  let usuarioAtual = null;
  try {
    const salvo = localStorage.getItem('grain_usuario_logado');
    if (salvo) usuarioAtual = JSON.parse(salvo);
  } catch (e) {}

  if (isStandalone) {
    if (!usuarioAtual) {
      window.location.href = 'login.html?acessoNegado=true';
      return;
    }
    if (clienteInput) {
      clienteInput.value = `${usuarioAtual.nome} — ${usuarioAtual.email}`;
    }
    if (inputEndereco && !inputEndereco.value && usuarioAtual.endereco) {
      inputEndereco.placeholder = usuarioAtual.endereco;
    }
  }

  function atualizarAtendimento() {
    const mesa = tipo.value === 'MESA';
    const entrega = tipo.value === 'ENTREGA';
    grupoMesa.hidden = !mesa;
    grupoEndereco.hidden = !entrega;
    inputMesa.required = mesa;
    inputEndereco.required = entrega;
    atualizarResumo();
  }

  function atualizarResumo() {
    const opt = produto.selectedOptions[0];
    const preco = opt ? parseFloat(opt.dataset.preco) : NaN;
    const qtd = parseInt(quantidade.value, 10) || 1;
    const isEntrega = tipo.value === 'ENTREGA';
    const taxaEntrega = isEntrega ? 5.0 : 0.0;

    if (!opt || !opt.value || isNaN(preco)) {
      resumo.innerHTML = '<p class="text-muted" style="margin:0;font-size:.95rem;">Selecione um produto do cardápio acima para calcular o valor do pedido.</p>';
      return;
    }

    const subtotal = preco * qtd;
    const total = subtotal + taxaEntrega;

    resumo.innerHTML =
      `<div class="summary-row"><span><strong>${qtd}x ${opt.dataset.nome}</strong> (${brl(preco)} un.)</span><span>${brl(subtotal)}</span></div>` +
      (isEntrega ? `<div class="summary-row"><span>Taxa de Entrega (Delivery)</span><span>${brl(taxaEntrega)}</span></div>` : '') +
      `<div class="summary-row summary-total"><span>Total do Pedido</span><span>${brl(total)}</span></div>`;
  }

  // Submissão standalone (quando não estiver rodando via form POST do Thymeleaf)
  if (isStandalone) {
    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const opt = produto.selectedOptions[0];
      if (!opt || !opt.value) return;

      const preco = parseFloat(opt.dataset.preco);
      const qtd = parseInt(quantidade.value, 10) || 1;
      const tipoVal = tipo.value;
      const isEntrega = tipoVal === 'ENTREGA';
      const total = (preco * qtd) + (isEntrega ? 5.0 : 0.0);

      const agora = new Date();
      const dataFmt = agora.toLocaleDateString('pt-BR') + ' ' + agora.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
      const tipoDescMap = { BALCAO: 'Balcão', MESA: 'Mesa', ENTREGA: 'Entrega (Delivery)' };

      const todos = typeof GrainApp !== 'undefined' ? GrainApp.getPedidosStorage() : [];
      const novoId = todos.length ? Math.max(...todos.map(x => Number(x.id) || 0)) + 1 : 1;

      const novoPedido = {
        id: novoId,
        cliente: usuarioAtual.nome,
        email: usuarioAtual.email,
        telefone: usuarioAtual.telefone || '(11) 91234-5678',
        dataPedido: dataFmt,
        itens: `${qtd}x ${opt.dataset.nome}`,
        tipoAtendimento: tipoVal,
        tipoDescricao: tipoDescMap[tipoVal] || tipoVal,
        numeroMesa: tipoVal === 'MESA' ? Number(inputMesa.value) : null,
        enderecoEntrega: tipoVal === 'ENTREGA' ? (inputEndereco.value || usuarioAtual.endereco) : null,
        status: 'EM_ATENDIMENTO',
        valorTotal: total,
        observacoes: inputObs ? inputObs.value.trim() : ''
      };

      todos.unshift(novoPedido);
      if (typeof GrainApp !== 'undefined') {
        GrainApp.savePedidosStorage(todos);
      }

      // Tenta enviar também ao Back-end Localhost se estiver ativo
      try {
        await fetch('http://localhost/api/pedidos', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            emailCliente: usuarioAtual.email,
            produtoId: Number(opt.value),
            quantidade: qtd,
            tipoAtendimento: tipoVal,
            numeroMesa: novoPedido.numeroMesa,
            enderecoEntrega: novoPedido.enderecoEntrega,
            observacoes: novoPedido.observacoes
          })
        });
      } catch (err) {
        // Continua normalmente em modo autônomo
      }

      if (typeof GrainAnimations !== 'undefined') {
        GrainAnimations.showToast(`Pedido #${novoId} confirmado com sucesso! (${novoPedido.tipoDescricao})`, 'success');
      }
      form.reset();
      if (clienteInput && usuarioAtual) {
        clienteInput.value = `${usuarioAtual.nome} — ${usuarioAtual.email}`;
      }
      quantidade.value = 1;
      atualizarAtendimento();
      atualizarResumo();
    });
  }

  tipo.addEventListener('change', atualizarAtendimento);
  produto.addEventListener('change', atualizarResumo);
  quantidade.addEventListener('input', atualizarResumo);

  atualizarAtendimento();
  atualizarResumo();
})();
