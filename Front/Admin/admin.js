document.addEventListener("DOMContentLoaded", () => {
  const eventosList = document.getElementById("eventosList");
  const usuariosList = document.getElementById("usuariosList");

  const detalhesEventoNome = document.getElementById("eventoNome");
  const detalhesEventoPalestrante = document.getElementById("eventoPalestrante");
  const detalhesEventoDescricao = document.getElementById("eventoDescricao");
  const detalhesEventoDataHora = document.getElementById("eventoDataHora");
  const detalhesEventoLocal = document.getElementById("eventoLocal");

  const criarEventoButton = document.getElementById("criarEventoButton");

  // 🔒 Verifica se o usuário logado é admin
  const usuario = JSON.parse(localStorage.getItem("usuarioLogado"));
  if (!usuario || usuario.role !== "ADMIN") {
    alert("Acesso negado. Apenas administradores podem acessar esta página.");
    window.location.href = "../login/login.html";
    return;
  }

  // 🧾 Carregar eventos
  fetch("http://localhost:8080/eventos")
    .then(res => res.json())
    .then(eventos => {
      eventosList.innerHTML = eventos.map(e => `
        <div class="evento-item" onclick="mostrarDetalhesEvento(${e.id})">
          <strong>${e.nome}</strong> - ${e.dataHora}
        </div>
      `).join("");
    });

  // ➕ Criar novo evento
  criarEventoButton.addEventListener("click", () => {
    window.location.href = "../cadastro/cadastro.html#dashboard";
  });

  // Funções globais para acesso pelo onclick
  window.mostrarDetalhesEvento = function (eventoId) {
    // Buscar detalhes do evento
    fetch(`http://localhost:8080/eventos/${eventoId}`)
      .then(res => res.json())
      .then(e => {
        detalhesEventoNome.textContent = `Nome: ${e.nome}`;
        detalhesEventoPalestrante.textContent = `Palestrante: ${e.palestrante}`;
        detalhesEventoDescricao.textContent = `Descrição: ${e.descricao}`;
        detalhesEventoDataHora.textContent = `Data/Hora: ${e.dataHora}`;
        detalhesEventoLocal.textContent = `Local: ${e.local}`;
      });
    // Buscar usuários inscritos
    fetch(`http://localhost:8080/eventos/${eventoId}/inscritos`)
      .then(res => res.json())
      .then(dados => {
        const usuarios = dados.inscritos || [];
        if (usuarios.length === 0) {
          usuariosList.innerHTML = "<p>Nenhum usuário inscrito.</p>";
        } else {
          usuariosList.innerHTML = usuarios.map(u => `
        <div>${u.nome} (${u.ra})</div>
      `).join("");
        }
      })
      .catch(err => {
        console.error("Erro ao carregar inscritos:", err);
        usuariosList.innerHTML = "<p>Erro ao carregar inscritos.</p>";
      });

  }
});
