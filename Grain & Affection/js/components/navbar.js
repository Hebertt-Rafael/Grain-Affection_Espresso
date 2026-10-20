/**
 * ============================================================
 * navbar.js — Componente de Navegação, Controle de Perfil & Menu Mobile
 * Grain & Affection · v3.0
 * ============================================================
 */

const GrainNavbar = (() => {

  function init() {
    syncAuthNavbar();
    setupScrollEffect();
    setupMobileMenu();
    highlightActiveLink();
  }

  // Controla visibilidade de "Painel" (exclusivo para Admin/Gerente) e estado de Login/Sair
  function syncAuthNavbar() {
    const navLinks = document.querySelector('.nav-links');
    if (!navLinks) return;

    // Se já foi renderizado pelo servidor (Thymeleaf com .nav-user ou link /logout), mantém o do servidor
    if (navLinks.querySelector('a[href="/logout"]')) return;

    let usuario = null;
    try {
      const raw = localStorage.getItem('grain_usuario_logado');
      if (raw) usuario = JSON.parse(raw);
    } catch (e) {}

    const isAdmin = Boolean(usuario && usuario.perfil === 'GERENTE');

    // Exibe "Painel" somente para Admin (Gerente)
    navLinks.querySelectorAll('.nav-admin-only, a[href*="admin.html"]').forEach(el => {
      const li = el.tagName === 'LI' ? el : el.closest('li');
      if (li) {
        li.hidden = !isAdmin;
      }
    });

    // Atualiza botão "Entrar" para mostrar usuário logado e opção "Sair"
    if (usuario && usuario.nome) {
      const loginLink = navLinks.querySelector('a[href*="login.html"]');
      if (loginLink && loginLink.parentElement) {
        const loginLi = loginLink.parentElement;
        const primeiroNome = usuario.nome.split(' ')[0];
        const loginHref = loginLink.getAttribute('href');

        const userLi = document.createElement('li');
        userLi.className = 'nav-user';
        userLi.innerHTML = `<span>Olá, ${primeiroNome}</span>`;

        const logoutLi = document.createElement('li');
        const logoutA = document.createElement('a');
        logoutA.href = loginHref;
        logoutA.textContent = 'Sair';
        logoutA.addEventListener('click', (e) => {
          e.preventDefault();
          localStorage.removeItem('grain_usuario_logado');
          window.location.href = loginHref;
        });
        logoutLi.appendChild(logoutA);

        loginLi.replaceWith(userLi, logoutLi);
      }
    }
  }

  // Altera estilo do cabeçalho ao rolar a página
  function setupScrollEffect() {
    const header = document.querySelector('.site-header');
    if (!header) return;

    window.addEventListener('scroll', () => {
      if (window.scrollY > 40) {
        header.classList.add('scrolled');
      } else {
        header.classList.remove('scrolled');
      }
    }, { passive: true });
  }

  // Abre e fecha o menu mobile com transição suave
  function setupMobileMenu() {
    const toggleBtn = document.querySelector('.mobile-toggle');
    const navLinks = document.querySelector('.nav-links');
    if (!toggleBtn || !navLinks) return;

    toggleBtn.addEventListener('click', () => {
      navLinks.classList.toggle('nav-open');
      const isOpen = navLinks.classList.contains('nav-open');
      toggleBtn.setAttribute('aria-expanded', isOpen);
    });

    // Fecha ao clicar fora
    document.addEventListener('click', (e) => {
      if (!navLinks.contains(e.target) && !toggleBtn.contains(e.target)) {
        navLinks.classList.remove('nav-open');
      }
    });
  }

  // Identifica a página atual e adiciona a classe .active
  function highlightActiveLink() {
    const links = document.querySelectorAll('.nav-links a');
    const currentPath = window.location.pathname.split('/').pop() || 'index.html';

    links.forEach(link => {
      const href = link.getAttribute('href') || '';
      const linkPath = href.split('/').pop();
      if (linkPath === currentPath && link.textContent.trim().toLowerCase() !== 'sair') {
        link.classList.add('active');
      } else {
        link.classList.remove('active');
      }
    });
  }

  return { init };

})();
