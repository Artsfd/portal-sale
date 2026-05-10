// Elementos do DOM
const loginButton = document.getElementById("loginButton");
const forgotPassword = document.getElementById("forgotPassword");
const calendarIcon = document.getElementById("calendarIcon");
const markEventsButton = document.getElementById("markEventsButton");
const matriculaInput = document.getElementById("matricula");
const senhaInput = document.getElementById("senha");

function validarLogin() {
  const ra = matriculaInput.value;
  const senha = senhaInput.value;

  if (!ra || !senha) {
    return;
  }

  fetch("http://localhost:8080/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ ra, senha })
  })
    .then(async response => {
      const data = await response.json().catch(() => null);

      if (!response.ok) {
        throw new Error(data?.mensagem || "Credenciais inválidas");
      }

      return data;
    })
    .then(usuario => {
      // Salva token e role
      localStorage.setItem("token", usuario.token);
      localStorage.setItem("role", usuario.role);

      if (usuario.role === "ADMIN") {
        window.location.href = "../admin/admin.html";
      } else {
        mostrarPainelPrincipal();
        window.location.hash = "dashboard";
      }
    })
    .catch(error => {
      mostrarMensagem(error.message || "Erro ao realizar login.", "erro");
    });
}

function mostrarMensagem(msg, tipo = "info") {
  const overlay = document.createElement('div');
  overlay.style.cssText = `
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.7);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 10000;
    font-family: Arial, sans-serif;
  `;

  const modal = document.createElement('div');
  modal.style.cssText = `
    background: white;
    padding: 30px;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
    text-align: center;
    max-width: 400px;
    width: 90%;
    position: relative;
    z-index: 10001;
  `;

  const iconoDiv = document.createElement('div');
  iconoDiv.style.cssText = `
    font-size: 48px;
    margin-bottom: 20px;
  `;

  let icono = "ℹ️";
  let cor = "#007bff";
  
  if (tipo === "sucesso") {
    icono = "✓";
    cor = "#28a745";
  } else if (tipo === "erro") {
    icono = "✕";
    cor = "#dc3545";
  } else if (tipo === "aviso") {
    icono = "⚠";
    cor = "#ffc107";
  }

  iconoDiv.textContent = icono;

  const mensagem = document.createElement('p');
  mensagem.textContent = msg;
  mensagem.style.cssText = `
    color: #333;
    font-size: 16px;
    margin: 0 0 25px 0;
    line-height: 1.5;
  `;

  const botao = document.createElement('button');
  botao.textContent = "OK";
  botao.style.cssText = `
    background: ${cor};
    color: white;
    border: none;
    padding: 12px 30px;
    border-radius: 8px;
    font-size: 16px;
    cursor: pointer;
    transition: background 0.3s;
  `;

  const corEscura = tipo === "sucesso" ? "#218838" : tipo === "erro" ? "#c82333" : tipo === "aviso" ? "#e0a800" : "#0056b3";
  botao.onmouseover = () => botao.style.background = corEscura;
  botao.onmouseout = () => botao.style.background = cor;

  const fechar = () => {
    if (document.body.contains(overlay)) {
      document.body.removeChild(overlay);
    }
  };

  botao.onclick = fechar;
  overlay.onclick = (e) => {
    if (e.target === overlay) {
      fechar();
    }
  };

  modal.appendChild(iconoDiv);
  modal.appendChild(mensagem);
  modal.appendChild(botao);
  overlay.appendChild(modal);
  document.body.appendChild(overlay);
}

function mostrarPainelPrincipal() {
  document.querySelector(".login").style.display = "none";
  document.querySelector(".main").style.display = "block";
}

// Redirecionamentos do painel
function redirecionarParaEventos() {
  window.location.href = "../eventos/eventos_novo.html#dashboard";
}

function redirecionarParaMarcarEventos() {
  window.location.href = "../cadastro/cadastro.html#dashboard";
}

function atualizarEstatisticas() {
  const numeroEventosElement = document.querySelector(".stat-item__number");

  fetch("http://localhost:8080/eventos")
    .then(res => res.json())
    .then(eventos => {
      numeroEventosElement.textContent = eventos.length;
    })
    .catch(() => numeroEventosElement.textContent = "0");
}

// EVENTOS
loginButton.addEventListener("click", validarLogin);
forgotPassword.addEventListener("click", () => {
  mostrarMensagem("Para recuperar sua senha, envie um email para a secretaria.", "aviso");
});
calendarIcon.addEventListener("click", redirecionarParaEventos);
markEventsButton.addEventListener("click", redirecionarParaMarcarEventos);

// Enter para logar
senhaInput.addEventListener("keypress", (event) => {
  if (event.key === "Enter") validarLogin();
});

document.addEventListener("DOMContentLoaded", () => {
  if (window.location.hash === "#dashboard") {
    mostrarPainelPrincipal();
    atualizarEstatisticas();
  }
});
