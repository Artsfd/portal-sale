// Elementos do DOM
const loginButton = document.getElementById("loginButton")
const forgotPassword = document.getElementById("forgotPassword")
const calendarIcon = document.getElementById("calendarIcon")
const markEventsButton = document.getElementById("markEventsButton")
const matriculaInput = document.getElementById("matricula")
const senhaInput = document.getElementById("senha")

function validarLogin() {
  const ra = matriculaInput.value;
  const senha = senhaInput.value;

  if (ra === "" || senha === "") {
    mostrarMensagem("Por favor, preencha todos os campos.");
    return;
  }

  fetch("http://localhost:8080/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ ra: ra, senha: senha })
  })
    .then(response => {
      if (!response.ok) {
        return response.json().then(err => { throw new Error(err.mensagem); });
      }
      return response.json();
    })
    .then(usuario => {
      mostrarMensagem("Login realizado com sucesso!");
      if (usuario.role && usuario.role.toUpperCase() === "ADMIN") {
        window.location.href = "../admin/admin.html";
      } else {
        mostrarPainelPrincipal();
        window.location.hash = "dashboard";
      }
    })
    .catch(error => {
      mostrarMensagem(error.message || "Erro ao realizar login.");
    });
}

/**
 * Exibe uma mensagem para o usuário
 */
function mostrarMensagem(mensagem) {
  alert(mensagem)
}

/**
 * Mostra o painel principal
 */
function mostrarPainelPrincipal() {
  document.querySelector(".login").style.display = "none";
  document.querySelector(".main").style.display = "block";
}

/**
 * Função para recuperar a senha
 */
function recuperarSenha() {
  mostrarMensagem(
    "Para recuperar sua senha, entre em contato com a secretaria ou envie um email para: souzarthur.ferreira@gmail.com informando sua matrícula.",
  )
}

/**
 * Redireciona para a página de eventos
 */
function redirecionarParaEventos() {
  window.location.href = "../eventos/eventos_novo.html#dashboard";
}

/**
 * Redireciona para a página de marcar eventos
 */
function redirecionarParaMarcarEventos() {
  window.location.href = "../cadastro/cadastro.html#dashboard";
}

/**
 * Atualiza as estatísticas de eventos
 */
function atualizarEstatisticas() {
  const numeroEventosElement = document.querySelector(".stat-item__number");

  fetch("http://localhost:8080/eventos")
    .then(response => {
      if (!response.ok) {
        throw new Error("Erro ao buscar eventos");
      }
      return response.json();
    })
    .then(eventos => {
      const totalEventos = Array.isArray(eventos) ? eventos.length : 0;
      numeroEventosElement.textContent = totalEventos;
    })
    .catch(error => {
      console.error("Erro ao carregar eventos:", error);
      numeroEventosElement.textContent = "0";
    });
}

// Event Listeners
loginButton.addEventListener("click", validarLogin)
forgotPassword.addEventListener("click", recuperarSenha)
calendarIcon.addEventListener("click", redirecionarParaEventos)
markEventsButton.addEventListener("click", redirecionarParaMarcarEventos)

// Permitir login com a tecla Enter
senhaInput.addEventListener("keypress", (event) => {
  if (event.key === "Enter") {
    validarLogin()
  }
})

document.addEventListener("DOMContentLoaded", () => {
  // Se veio com hash #dashboard, pula direto pro painel
  if (window.location.hash === "#dashboard") {
    mostrarPainelPrincipal();
    atualizarEstatisticas();
  }
});