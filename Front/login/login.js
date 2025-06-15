/**
 * Arquivo JavaScript específico para a página index.html
 * Contém funcionalidades de login e navegação inicial
 */

// Elementos do DOM
const loginButton = document.getElementById("loginButton")
const forgotPassword = document.getElementById("forgotPassword")
const calendarIcon = document.getElementById("calendarIcon")
const markEventsButton = document.getElementById("markEventsButton")
const matriculaInput = document.getElementById("matricula")
const senhaInput = document.getElementById("senha")

/**
 * Função para validar o login do usuário
 * @returns {boolean} Retorna true se o login for válido, false caso contrário
 */
function validarLogin() {
  const matricula = matriculaInput.value
  const senha = senhaInput.value

  // Validação de campos vazios
  if (matricula === "" || senha === "") {
    mostrarMensagem("Por favor, preencha todos os campos.")
    return false
  }

  // Validação de formato da matrícula (apenas números)
  if (!/^[0-9]+$/.test(matricula)) {
    mostrarMensagem("A matrícula deve conter apenas números.")
    return false
  }

  // Verificação de credenciais
  if (matricula === "6924106422" && senha === "20012005") {
    // Login de aluno
    mostrarPainelPrincipal()
    return true
  } else if (matricula === "1234567890" && senha === "palestrante") {
    // Login de palestrante
    mostrarPainelPrincipal(true)
    return true
  } else {
    mostrarMensagem("Matrícula ou senha incorretas.")
    return false
  }
}

/**
 * Exibe uma mensagem para o usuário
 * @param {string} mensagem - A mensagem a ser exibida
 */
function mostrarMensagem(mensagem) {
  alert(mensagem)
}

/**
 * Exibe o painel principal após o login
 * @param {boolean} isPalestrante - Indica se o usuário é um palestrante
 */
function mostrarPainelPrincipal(isPalestrante = false) {
  document.querySelector(".login").style.display = "none"
  document.querySelector(".main").style.display = "block"

  if (isPalestrante) {
    document.querySelector(".mark-events").style.display = "block"
  }
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
  window.location.href = "../eventos/eventos_novo.html";
}

/**
 * Redireciona para a página de marcar eventos
 */
function redirecionarParaMarcarEventos() {
  window.location.href = "../cadastro/cadastro.html";
}

/**
 * Atualiza as estatísticas de eventos
 */
function atualizarEstatisticas() {
  // Em um ambiente real, estes números viriam de uma API
  const totalEventos = 5
  const eventosHoje = 3

  document.querySelector(".stat-item__number:first-child").textContent = totalEventos
  document.querySelector(".stat-item__number:last-child").textContent = eventosHoje
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

// Adiciona a inicialização das estatísticas
document.addEventListener("DOMContentLoaded", () => {
  atualizarEstatisticas()
})

document.addEventListener("DOMContentLoaded", function () {
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
});