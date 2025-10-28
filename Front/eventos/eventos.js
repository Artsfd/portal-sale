// Elementos do DOM
const eventsList = document.getElementById("eventsList");
const backButton = document.getElementById("backButton");
const filterButtons = document.querySelectorAll(".filter-button");
const eventCardTemplate = document.getElementById("event-card-template");

// Função para criar card de evento
function criarElementoEvento(evento) {
  const template = eventCardTemplate.content.cloneNode(true);
  const card = template.querySelector(".event-card");

  // Categoria
  const categoryBadge = card.querySelector(".event-card__category");
  categoryBadge.textContent = evento.tipoEvento.charAt(0).toUpperCase() + evento.tipoEvento.slice(1);
  categoryBadge.classList.add(`event-card__category--${evento.tipoEvento}`);

  // Dados do evento
  card.querySelector(".event-card__title").textContent = evento.nome;
  card.querySelector(".event-card__description").textContent = evento.descricao;
  card.querySelector(".event-card__time").textContent = evento.dataHora;
  card.querySelector(".event-card__location").textContent = evento.local;
  card.querySelector(".event-card__speaker").textContent = evento.palestrante;

  // Status de vagas
  const status = card.querySelector(".event-card__status");
  const vagasRestantes = evento.vagas - evento.inscritos.length; // ajustado
  if (vagasRestantes <= 0) {
    status.textContent = "Vagas esgotadas";
    status.classList.add("event-card__status--limited");
  } else if (vagasRestantes === 1) {
    status.textContent = "Última vaga!";
    status.classList.add("event-card__status--limited");
  } else if (vagasRestantes <= 10) {
    status.textContent = `Últimas ${vagasRestantes} vagas!`;
    status.classList.add("event-card__status--limited");
  } else {
    status.textContent = `${vagasRestantes} vagas disponíveis`;
    status.classList.add("event-card__status--available");
  }

  // Botão de inscrição
  const button = card.querySelector(".event-card__button");
  button.addEventListener("click", () => inscreverEvento(evento.id));

  // Botão de exclusão
  const deleteButton = card.querySelector(".event-card__delete-button");
  deleteButton.addEventListener("click", (e) => {
    e.stopPropagation();
    excluirEvento(evento.id);
  });

  return card;
}

// Carregar eventos
function carregarEventos(filtro = "all") {
  fetch("http://localhost:8080/eventos")
    .then(res => res.json())
    .then(eventos => {
      eventsList.innerHTML = "";
      let eventosFiltrados = eventos;

      // Filtro
      if (filtro !== "all") {
        eventosFiltrados = eventos.filter(ev => ev.tipoEvento === filtro);
      }

      eventosFiltrados.forEach(ev => {
        const card = criarElementoEvento(ev);
        eventsList.appendChild(card);
      });
    })
    .catch(err => console.error("Erro ao carregar eventos:", err));
}

// Inscrever usuário
function inscreverEvento(eventoId) {
  const usuarioId = localStorage.getItem("usuarioId");

  if (!usuarioId) {
    alert("Usuário não está logado.");
    return;
  }

  fetch(`http://localhost:8080/eventos/${eventoId}/inscrever/${usuarioId}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" }
  })
  .then(async res => {
    if (!res.ok) {
      const texto = await res.text();
      throw new Error(texto);
    }
    return res.json();
  })
  .then(() => {
    alert("Inscrição realizada com sucesso!");
    // Recarrega eventos para atualizar vagas
    const filtroAtivo = document.querySelector(".filter-button.active")?.dataset.filter || "all";
    carregarEventos(filtroAtivo);
  })
  .catch(err => alert("Erro ao processar inscrição: " + err.message));
}

// Excluir evento
function excluirEvento(eventoId) {
  if (!confirm("Tem certeza que deseja excluir este evento?")) return;

  fetch(`http://localhost:8080/eventos/${eventoId}`, { method: "DELETE" })
    .then(res => {
      if (!res.ok) throw new Error("Erro ao excluir evento");
      alert("Evento excluído com sucesso!");
      carregarEventos(document.querySelector(".filter-button.active")?.dataset.filter || "all");
    })
    .catch(err => alert(err.message));
}

// Filtros
filterButtons.forEach(btn => {
  btn.addEventListener("click", () => {
    filterButtons.forEach(b => b.classList.remove("active"));
    btn.classList.add("active");
    carregarEventos(btn.dataset.filter);
  });
});

// Back button
backButton?.addEventListener("click", () => {
  window.location.href = "../login/login.html#dashboard";
});

// Inicialização
document.addEventListener("DOMContentLoaded", () => carregarEventos());
