/**
 * Arquivo JavaScript específico para a página cadastro.html
 * Contém funcionalidades para cadastro de cursos/eventos
 */

// Elementos do DOM
document.addEventListener("DOMContentLoaded", function () {
  const formulario = document.querySelector("#eventoForm");

  if (!formulario) {
    console.error("Erro: Formulário não encontrado!");
    return;
  }

  formulario.addEventListener("submit", function (event) {
    event.preventDefault(); // Evita o reload da página

    const evento = {
      nome: document.querySelector("#evento").value,
      palestrante: document.querySelector("#nomePalestrante").value,
      descricao: document.querySelector("#descricaoPalestra").value,
      dataHora: document.querySelector("#dataHora").value,
      local: document.querySelector("#localPalestra").value,
      vagas: document.querySelector("#vagas").value,
      tipoEvento: document.querySelector("#tipoEvento").value
    };

    console.log("Dados enviados:", evento); // Debug para verificar os dados

    fetch("http://localhost:8080/eventos", {
      method: "POST",
      headers: {
        "Accept": "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify(evento)
    })
      .then(response => {
        if (!response.ok) {
          return response.text().then(text => { throw new Error(text) });
        }
        return response.json();
      })
      .then(data => {
        alert("Evento cadastrado com sucesso!");
        formulario.reset(); // Limpa os campos
      })
      .catch(error => {
        console.error("Erro ao cadastrar evento:", error);
        alert("Falha ao cadastrar evento. Verifique os dados e tente novamente.");
      });
  });
});

/**
 * Valida o formulário de cadastro
 * @returns {boolean} Retorna true se o formulário for válido, false caso contrário
 */
function validarFormulario() {
  if (!nomePalestrante.value.trim()) {
    mostrarMensagem("Por favor, informe o nome do palestrante.")
    return false
  }

  if (!descricaoPalestra.value.trim()) {
    mostrarMensagem("Por favor, forneça uma descrição para a palestra.")
    return false
  }

  if (!horarioPalestra.value) {
    mostrarMensagem("Por favor, defina um horário para a palestra.")
    return false
  }

  if (!localPalestra.value.trim()) {
    mostrarMensagem("Por favor, informe o local da palestra.")
    return false
  }

  return true
}

/**
 * Volta para a página inicial
 */
function voltarParaInicio() {
  window.location.href = "../login/login.html"
}

// Event Listeners
backButton.addEventListener("click", voltarParaInicio)