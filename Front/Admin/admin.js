document.addEventListener("DOMContentLoaded", () => {
  const eventosList = document.getElementById("eventosList");
  const usuariosList = document.getElementById("usuariosList");
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
        <div class="evento-item">
          <strong>${e.nome}</strong> - ${e.dataHora} <br>
          <button onclick="verInscritos(${e.id})">Ver Inscritos</button>
          <button onclick="excluirEvento(${e.id})">Excluir</button>
        </div>
      `).join("");
    });

  // ➕ Criar novo evento
  criarEventoButton.addEventListener("click", () => {
    window.location.href = "../cadastro/cadastro.html#dashboard";
  });
});

function verInscritos(eventoId) {
  fetch(`http://localhost:8080/eventos/${eventoId}/inscritos`)
    .then(res => res.json())
    .then(usuarios => {
      const usuariosList = document.getElementById("usuariosList");
      usuariosList.innerHTML = usuarios.map(u => `
        <div>${u.nome} (${u.ra})</div>
      `).join("");
    });
}

function excluirEvento(id) {
  if (confirm("Tem certeza que deseja excluir este evento?")) {
    fetch(`http://localhost:8080/eventos/${id}`, { method: "DELETE" })
      .then(() => window.location.reload());
  }
}
