document.addEventListener("DOMContentLoaded", function () {
    const formCadastro = document.querySelector("#formCadastroUsuario");
    const backButton = document.querySelector("#backButton");

    if (!formCadastro) {
        console.error("Formulário não encontrado");
        return;
    }

    if (backButton) {
        backButton.addEventListener("click", voltarParaInicio);
    }

    formCadastro.addEventListener("submit", function (event) {
        event.preventDefault();

        const nome = document.querySelector("#nome").value.trim();
        const email = document.querySelector("#email").value.trim();
        const ra = document.querySelector("#ra").value.trim();
        const senha = document.querySelector("#senha").value;
        const confirmarSenha = document.querySelector("#confirmarSenha").value;

        if (!nome || !email || !ra || !senha || !confirmarSenha) {
            alert("Preencha todos os campos.");
            return;
        }

        if (senha !== confirmarSenha) {
            alert("As senhas não coincidem.");
            return;
        }

        const evento = {
            nome: nome,
            email: email,
            ra: ra,
            senha: senha
        };

        console.log("Cadastro feito:", evento);

        fetch("http://localhost:8080/auth/cadastro", {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            body: JSON.stringify(evento)
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text); });
            }
            return response.json();
        })
        .then(data => {
            alert("Cadastro efetuado!");
            formCadastro.reset();
        })
        .catch(error => {
            console.error("Erro ao cadastrar usuário: ", error);
            alert("Falha ao cadastrar usuário");
        });
    });
});

function voltarParaInicio() {
    window.location.href = "../login/login.html";
}
