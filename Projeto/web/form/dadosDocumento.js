document.getElementById("newDocument").addEventListener("click", function (event) {
    event.preventDefault();
});

//Remove o elemento do array
function remover(array, element) {
    const index = array.indexOf(element);

    if (index !== -1) {
        array.splice(index, 1);
    }
}

var docName = "";

$.ajax({
    type: 'POST',
    url: '/ListarTipoDocumento',
    dataType: 'json',
    success: function (response) {
        var listitems = '';
        $.each(response, function (key, value) {
            listitems += '<option value=' + value.id + ' data-validade="' + value.possuiValidade + '">' + value.nome + '</option>';
        });
        $("#tipoDocumento").append(listitems);
        ;
    }
});

var documentosExistentes = [];

//Retorna um array com os dados de todos os documentos
function getDocumentos() {

    var documentos = {};
    var arrayLength = documentosExistentes.length;
    for (var i = 0; i < arrayLength; i++) {
        documentos["tipoDocumento" + (i + 1)] = $("#documento" + documentosExistentes[i]).data('tipo');
        documentos["documento" + (i + 1)] = $("#documento" + documentosExistentes[i]).val();
        documentos["validadeDocumento" + (i + 1)] = $("#validade" + documentosExistentes[i]).val();
    }

    return JSON.stringify(documentos);
}
;

var contadorDoc = 0;
function novoDocumento() {
    var selecao = document.getElementById("tipoDocumento").options[document.getElementById("tipoDocumento").selectedIndex];
    var conteudo = $("#tipoDocumento").val();
    if (conteudo === "" || conteudo === null || conteudo === "0") {
        alert("Selecione um documento");
        return;
    }

    contadorDoc = contadorDoc + 1;

// criação dos elementos
    var divDoc = document.createElement("DIV");
    var tituloDoc = document.createElement("H5");
    var lblNumero = document.createElement("LABEL");
    var lblValidade = document.createElement("LABEL");
    var inputNum = document.createElement("INPUT");
    var inputValidade = document.createElement("INPUT");
    var textValidade = document.createTextNode("Validade:");
    var btnDelDocumento = document.createElement("BUTTON");
    var txtBtnDel = document.createTextNode("Remover");
    var titulo = document.createTextNode(selecao.text);
    var textNumero = document.createTextNode(selecao.text + ":");
    lblNumero.appendChild(textNumero);
    lblValidade.appendChild(textValidade);

    //prepara DIV
    divDoc.className = "form-group col-lg-3";
    divDoc.id = "divDocumento" + contadorDoc;

    //prepara itens do número do documento
    lblNumero.htmlFor = "documento" + contadorDoc;
    inputNum.id = "documento" + contadorDoc;
    inputNum.name = "documento" + contadorDoc;
    inputNum.className = "form-control";
    inputNum.required = true;

    //prepara itens da validade do documento
    lblValidade.htmlFor = "validade" + contadorDoc;
    inputValidade.type = "date";
    inputValidade.id = "validade" + contadorDoc;
    inputValidade.name = "validade" + contadorDoc;
    inputValidade.className = "form-control";
    inputValidade.required = true;

    btnDelDocumento.className = "btn btn-danger";
    btnDelDocumento.id = "delDocumento" + contadorDoc;
    // ---------------------
    //A variável temp é necessária para desreferenciar a variável contadorDoc da função
    var temp = contadorDoc;
    var temp2 = conteudo;
    btnDelDocumento.onclick = function () {
        remover(documentosExistentes, temp);
        document.getElementById("tipoDocumento").options[temp2].disabled = false;
        document.getElementById("divDocumento" + temp).outerHTML = "";
    };
    // ---------------------
    btnDelDocumento.appendChild(txtBtnDel);

    document.getElementById("divDocumentos").appendChild(divDoc);

    document.getElementById("divDocumento" + contadorDoc).appendChild(lblNumero);
    document.getElementById("divDocumento" + contadorDoc).appendChild(inputNum);
    $("#documento" + contadorDoc).attr('data-tipo', selecao.value);
    if ($("#tipoDocumento").find(':selected').data('validade')) {
        document.getElementById("divDocumento" + contadorDoc).appendChild(lblValidade);
        document.getElementById("divDocumento" + contadorDoc).appendChild(inputValidade);
    }

    document.getElementById("divDocumento" + contadorDoc).appendChild(btnDelDocumento);
    document.getElementById("delDocumento" + contadorDoc).addEventListener("click", function (event) {
        event.preventDefault();
    });

    selecao.disabled = true; //desabilida a opção criada
    documentosExistentes.push(contadorDoc);
    document.getElementById("tipoDocumento").selectedIndex = 0; //volta o select para o elemento 0
}