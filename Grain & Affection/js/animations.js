/**
 * ============================================================
 * animations.js — Módulo de Animações & Interações Fluidas
 * Grain & Affection · v2.0
 * ============================================================
 * Contém:
 * - Scroll Reveal via IntersectionObserver (alto desempenho)
 * - Efeito Typing dinâmico
 * - Contador animado de estatísticas
 * - 3D Tilt suave nos cards ao passar o mouse
 * - Sistema de Toast Notification elegante
 * ============================================================
 */

const GrainAnimations = (() => {

  /**
   * 1. Scroll Reveal: ativa classes quando os elementos entram no viewport
   */
  function initScrollReveal() {
    const targets = document.querySelectorAll('.reveal-on-scroll');
    if (!targets.length) return;

    const observer = new IntersectionObserver((entries, obs) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('is-visible');
          obs.unobserve(entry.target); // Libera da observação para melhor performance
        }
      });
    }, {
      root: null,
      threshold: 0.15,
      rootMargin: '0px 0px -40px 0px'
    });

    targets.forEach(el => observer.observe(el));
  }

  /**
   * 2. Typing Effect: digita uma lista de palavras dinamicamente
   * @param {HTMLElement} element - elemento onde o texto será digitado
   * @param {Array<string>} words - array de termos
   * @param {number} speed - velocidade em ms
   */
  function initTyping(element, words = [], speed = 100) {
    if (!element || !words.length) return;

    let wordIndex = 0;
    let charIndex = 0;
    let isDeleting = false;

    function type() {
      const currentWord = words[wordIndex];

      if (isDeleting) {
        element.textContent = currentWord.substring(0, charIndex - 1);
        charIndex--;
      } else {
        element.textContent = currentWord.substring(0, charIndex + 1);
        charIndex++;
      }

      let currentSpeed = speed;
      if (isDeleting) currentSpeed /= 2;

      if (!isDeleting && charIndex === currentWord.length) {
        currentSpeed = 2200; // Pausa no final da palavra
        isDeleting = true;
      } else if (isDeleting && charIndex === 0) {
        isDeleting = false;
        wordIndex = (wordIndex + 1) % words.length;
        currentSpeed = 500;
      }

      setTimeout(type, currentSpeed);
    }

    type();
  }

  /**
   * 3. Animação de Contadores Numéricos (Ex: Estatísticas)
   */
  function initCounters() {
    const counters = document.querySelectorAll('[data-counter]');
    if (!counters.length) return;

    const observer = new IntersectionObserver((entries, obs) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          const el = entry.target;
          const target = parseInt(el.getAttribute('data-counter'), 10);
          const duration = 1800; // ms
          const step = Math.ceil(target / (duration / 16));
          let current = 0;

          const timer = setInterval(() => {
            current += step;
            if (current >= target) {
              el.textContent = target.toLocaleString('pt-BR');
              clearInterval(timer);
            } else {
              el.textContent = current.toLocaleString('pt-BR');
            }
          }, 16);

          obs.unobserve(el);
        }
      });
    }, { threshold: 0.5 });

    counters.forEach(c => observer.observe(c));
  }

  /**
   * 4. Efeito 3D Tilt suave nos cards
   */
  function initCardTilt() {
    const cards = document.querySelectorAll('.product-card');
    cards.forEach(card => {
      card.addEventListener('mousemove', (e) => {
        const rect = card.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;
        const centerX = rect.width / 2;
        const centerY = rect.height / 2;
        const rotateX = ((y - centerY) / centerY) * -6;
        const rotateY = ((x - centerX) / centerX) * 6;

        card.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg) translateY(-6px)`;
      });

      card.addEventListener('mouseleave', () => {
        card.style.transform = 'perspective(1000px) rotateX(0deg) rotateY(0deg) translateY(0)';
      });
    });
  }

  /**
   * 5. Toast Notification
   */
  function showToast(message, type = 'success') {
    let toast = document.querySelector('.toast-notification');
    if (!toast) {
      toast = document.createElement('div');
      toast.className = 'toast-notification';
      document.body.appendChild(toast);
    }

    const icon = type === 'success' ? '✓' : 'ℹ';
    toast.innerHTML = `<span><strong>${icon}</strong> ${message}</span>`;
    toast.classList.add('show');

    setTimeout(() => {
      toast.classList.remove('show');
    }, 4000);
  }

  return {
    initScrollReveal,
    initTyping,
    initCounters,
    initCardTilt,
    showToast
  };

})();
