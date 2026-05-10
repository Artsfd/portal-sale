/**
 * Arquivo JavaScript para cadastro de eventos
 */

function mostrarModalMensagem(msg, tipo = "info") {
  return new Promise((resolve) => {
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
      resolve();
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
  });
}

document.addEventListener("DOMContentLoaded", () => {

  // 🔒 Somente ADMIN pode acessar
  const role = localStorage.getItem("role");
  const token = localStorage.getItem("token");

  if (!token || role !== "ADMIN") {
    mostrarModalMensagem("Acesso negado! Apenas administradores podem cadastrar eventos.", "erro");
    setTimeout(() => {
      window.location.href = "../login/login.html";
    }, 2000);
    return;
  }

  const formulario = document.querySelector("#eventoForm");
  if (!formulario) {
    console.error("Erro: Formulário não encontrado!");
    return;
  }

  formulario.addEventListener("submit", (event) => {
    event.preventDefault();

    const evento = {
      nome: document.querySelector("#evento").value,
      palestrante: document.querySelector("#nomePalestrante").value,
      descricao: document.querySelector("#descricaoPalestra").value,
      dataHora: construirDataHora(
        document.querySelector("#data").value,
        document.querySelector("#horaInicio").value
      ),
      horaFim: construirDataHora(
        document.querySelector("#data").value,
        document.querySelector("#horaFim").value
      ),
      local: document.querySelector("#localPalestra").value,
      capacidadeMaxima: Number(document.querySelector("#vagas").value),
      tipoEvento: document.querySelector("#tipoEvento").value
    };

    console.log("Dados enviados:", evento);

    fetch("http://localhost:8080/eventos", {
      method: "POST",
      headers: {
        "Accept": "application/json",
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}` // 🔑 TOKEN AQUI
      },
      body: JSON.stringify(evento)
    })
      .then(async response => {
        const text = await response.text();

        if (!response.ok) {
          throw new Error(text || "Erro ao cadastrar evento");
        }

        return JSON.parse(text);
      })
      .then(() => {
        mostrarModalMensagem("Evento cadastrado com sucesso!", "sucesso");
        formulario.reset();
      })
      .catch(error => {
        console.error("Erro ao cadastrar evento:", error);
        mostrarModalMensagem("Falha ao cadastrar evento: " + error.message, "erro");
      });
  });

  // Botão voltar
  const backButton = document.getElementById("backButton");
  if (backButton) {
    backButton.addEventListener("click", () => {
      window.location.href = "../admin/admin.html";
    });
  }
});

/**
 * Constrói data/hora em formato ISO 8601 a partir de data e hora separadas
 * @param {string} data - Formato YYYY-MM-DD
 * @param {string} hora - Formato HH:mm
 * @returns {string} Formato YYYY-MM-DDTHH:mm
 */
function construirDataHora(data, hora) {
  if (!data || !hora) return null;
  return `${data}T${hora}`;
}
