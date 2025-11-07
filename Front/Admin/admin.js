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

  // 🧾 Função para carregar eventos
  function carregarEventos() {
    fetch("http://localhost:8080/api/eventos")
      .then(res => {
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        return res.json();
      })
      .then(eventos => {
        if (!Array.isArray(eventos)) {
          eventosList.innerHTML = "<p>Erro ao carregar eventos.</p>";
          return;
        }

        eventosList.innerHTML = eventos.map(e => `
          <div class="evento-item">
            <strong>${e.nome}</strong> - ${e.dataHora}
            <button onclick="mostrarDetalhesEvento(${e.id})">Ver Detalhes</button>
            <button onclick="editarEvento(${e.id})">✏️ Editar</button>
            <button onclick="excluirEvento(${e.id})">🗑️ Excluir</button>
          </div>
        `).join("");
      })
      .catch(err => {
        console.error("❌ Erro ao carregar eventos:", err);
        eventosList.innerHTML = "<p>Erro ao carregar eventos.</p>";
      });
  }

  carregarEventos();

  // ➕ Criar novo evento
  criarEventoButton.addEventListener("click", () => {
    window.location.href = "../cadastro/cadastro.html#dashboard";
  });

  // 🌐 Mostrar detalhes do evento
  window.mostrarDetalhesEvento = function (eventoId) {
    fetch(`http://localhost:8080/api/eventos/${eventoId}`)
      .then(res => res.json())
      .then(e => {
        detalhesEventoNome.textContent = `Nome: ${e.nome}`;
        detalhesEventoPalestrante.textContent = `Palestrante: ${e.palestrante}`;
        detalhesEventoDescricao.textContent = `Descrição: ${e.descricao}`;
        detalhesEventoDataHora.textContent = `Data/Hora: ${e.dataHora}`;
        detalhesEventoLocal.textContent = `Local: ${e.local}`;
      });

    // Buscar usuários inscritos
    fetch(`http://localhost:8080/api/eventos/${eventoId}/inscritos`)
      .then(res => res.json())
      .then(dados => {
        const usuarios = dados.inscritos || dados || [];
        if (usuarios.length === 0) {
          usuariosList.innerHTML = "<p>Nenhum usuário inscrito.</p>";
        } else {
          usuariosList.innerHTML = usuarios.map(u => `
            <div>
              ${u.nome} (${u.ra})
              <button onclick="removerInscricao(${eventoId}, ${u.id})">❌ Remover</button>
            </div>
          `).join("");
        }
      })
      .catch(err => {
        console.error("❌ Erro ao carregar inscritos:", err);
        usuariosList.innerHTML = "<p>Erro ao carregar inscritos.</p>";
      });
  };

  // ✏️ Editar evento
  window.editarEvento = function (id) {
    window.location.href = `../cadastro/cadastro.html?id=${id}`;
  };

  // 🗑️ Excluir evento
  window.excluirEvento = function (id) {
    if (!confirm("Tem certeza que deseja excluir este evento?")) return;

    fetch(`http://localhost:8080/api/eventos/${id}`, {
      method: "DELETE"
    })
      .then(res => {
        if (res.ok) {
          alert("Evento excluído com sucesso!");
          carregarEventos();
          usuariosList.innerHTML = "";
        } else {
          alert("Erro ao excluir evento.");
        }
      })
      .catch(err => console.error("❌ Erro ao excluir evento:", err));
  };

  // ❌ Remover inscrição de usuário
  window.removerInscricao = function (eventoId, usuarioId) {
    if (!confirm("Remover este usuário do evento?")) return;

    fetch(`http://localhost:8080/api/inscricoes/${eventoId}/${usuarioId}`, {
      method: "DELETE"
    })
      .then(res => {
        if (res.ok) {
          alert("Inscrição removida!");
          mostrarDetalhesEvento(eventoId);
        } else {
          alert("Erro ao remover inscrição.");
        }
      })
      .catch(err => console.error("❌ Erro ao remover inscrição:", err));
  };
});
