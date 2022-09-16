// Remove o efeito padrão do Botão
document.getElementById("addContato").addEventListener("click", function (event) {
    event.preventDefault();
});

//Remove o elemento do array
function remover(array, element) {
    const index = array.indexOf(element);

    if (index !== -1) {
        array.splice(index, 1);
    }
}

// Adiciona as opções do Select de acordo com os Tipos de Contato no Banco de Dados
var idContatos = {};
$.ajax({
    type: 'POST',
    url: '/ListarTipoContato',
    dataType: 'json',
    success: function (response) {
        var listaContatos = '';
        $.each(response, function (key, value) {
            listaContatos += '<option value=' + value.id + '>' + value.nome + '</option>';
            idContatos[value.id] = 0;
        });
        $("#tipoContato").append(listaContatos);
        ;
    }
});

var contatosExistentes = [];

//Retorna um array com os dados de todos os contatos
function getContatos() {

    var contatos = {};

    var arrayLength = contatosExistentes.length;
    for (var i = 0; i < arrayLength; i++) {
        contatos["tipoContato" + (i+1)] = $("#contato" + contatosExistentes[i]).data('tipo');
        contatos["contato" + (i+1)] = $("#contato" + contatosExistentes[i]).val();        
    }

    return JSON.stringify(contatos);

}
;

var contadorCon = 0;
function novoContato() {

    var conteudo = $("#tipoContato").val();
    if (conteudo === "") {
        alert("Selecione um Tipo de Contato");
        return;
    }

    var selecao = document.getElementById("tipoContato").options[document.getElementById("tipoContato").selectedIndex];

    contadorCon = contadorCon + 1;

    // criação dos elementos
    var btnDelContato = document.createElement("BUTTON");
    var lblContato = document.createElement("LABEL");
    var inputContato = document.createElement("INPUT");
    var textLblContato = document.createTextNode(selecao.text);
    var txtBtnDel = document.createTextNode("Remover");
    var divContato = document.createElement("DIV");

    // Preparação dos elementos
    lblContato.htmlFor = "contato" + contadorCon;
    lblContato.appendChild(textLblContato);
    inputContato.id = "contato" + contadorCon;
    inputContato.name = selecao.text + contadorCon;
    inputContato.className = "form-control";
    inputContato.required = true;

    btnDelContato.className = "btn btn-danger";
    btnDelContato.id = "delContato" + contadorCon;
    // ---------------------
    //A variável temp é necessária para desrefenciar a variável contadorCon da função
    var temp = contadorCon;
    btnDelContato.onclick = function(){
        remover(contatosExistentes, temp);
        document.getElementById("divContato" + temp).outerHTML = "";
    };
    // ---------------------
    btnDelContato.appendChild(txtBtnDel);

    divContato.className = "form-group col-lg-3";
    divContato.id = "divContato" + contadorCon;
    divContato.name = contadorCon;


    //Adiciona os elementos na página
    document.getElementById("divContatos").appendChild(divContato);
    document.getElementById("divContato" + contadorCon).appendChild(lblContato);
    document.getElementById("divContato" + contadorCon).appendChild(inputContato);
    $("#contato" + contadorCon).attr('data-tipo', selecao.value);
    document.getElementById("divContato" + contadorCon).appendChild(btnDelContato);
    document.getElementById("delContato" + contadorCon).addEventListener("click", function (event) {
        event.preventDefault();
    });

    contatosExistentes.push(contadorCon);
}
;