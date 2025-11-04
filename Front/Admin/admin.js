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
  fetch("http://localhost:8080/api/eventos")
    .then(res => {
      if (!res.ok) throw new Error(`Erro ${res.status}`);
      return res.json();
    })
    .then(eventos => {
      console.log("✅ Eventos recebidos:", eventos);

      if (!Array.isArray(eventos)) {
        console.error("❌ O backend não retornou uma lista:", eventos);
        eventosList.innerHTML = "<p>Erro ao carregar eventos.</p>";
        return;
      }

      eventosList.innerHTML = eventos.map(e => `
        <div class="evento-item" onclick="mostrarDetalhesEvento(${e.id})">
          <strong>${e.nome}</strong> - ${e.dataHora}
        </div>
      `).join("");
    })
    .catch(err => {
      console.error("❌ Erro ao carregar eventos:", err);
      eventosList.innerHTML = "<p>Erro ao carregar eventos.</p>";
    });

  // ➕ Criar novo evento
  criarEventoButton.addEventListener("click", () => {
    window.location.href = "../cadastro/cadastro.html#dashboard";
  });

  // 🌐 Função global para mostrar detalhes
  window.mostrarDetalhesEvento = function (eventoId) {
    console.log("🟦 Carregando evento:", eventoId);

    // Buscar detalhes do evento
    fetch(`http://localhost:8080/api/eventos/${eventoId}`)
      .then(res => {
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        return res.json();
      })
      .then(e => {
        detalhesEventoNome.textContent = `Nome: ${e.nome}`;
        detalhesEventoPalestrante.textContent = `Palestrante: ${e.palestrante}`;
        detalhesEventoDescricao.textContent = `Descrição: ${e.descricao}`;
        detalhesEventoDataHora.textContent = `Data/Hora: ${e.dataHora}`;
        detalhesEventoLocal.textContent = `Local: ${e.local}`;
      })
      .catch(err => {
        console.error("❌ Erro ao carregar evento:", err);
      });

    // Buscar usuários inscritos
    fetch(`http://localhost:8080/api/eventos/${eventoId}/inscritos`)
      .then(res => {
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        return res.json();
      })
      .then(dados => {
        console.log("📋 Inscritos recebidos:", dados);

        const usuarios = dados.inscritos || dados || [];
        if (usuarios.length === 0) {
          usuariosList.innerHTML = "<p>Nenhum usuário inscrito.</p>";
        } else {
          usuariosList.innerHTML = usuarios.map(u => `
            <div>${u.nome} (${u.ra})</div>
          `).join("");
        }
      })
      .catch(err => {
        console.error("❌ Erro ao carregar inscritos:", err);
        usuariosList.innerHTML = "<p>Erro ao carregar inscritos.</p>";
      });
  };
});
