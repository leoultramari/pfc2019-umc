//$(document).ready(function () {
$(window).on('load', function () {

    function preencherFormulario(response) {

        var i = 1;
        $.each(response.dados.documentos, function (key, value) {
            $("#tipoDocumento").val(value.tipoDocumento.id).change();
            novoDocumento();
            $("#documento" + i).val(value.dado);
            $("#validadeDocumento" + i).val(value.validade);
            i = i + 1;
        });
        i = 1;
        $.each(response.dados.contatos, function (key, value) {
            $("#tipoContato").val(value.tipoContato.id).change();
            novoContato();
            $("#contato" + i).val(value.dado);
            i = i + 1;
        });

        fillFormData(response.dados, $("dados"));
        fillFormData(response.dados.endereco, $("endereco"));

        $("#bairro").val(response.dados.endereco.bairro.id).change();

        $("#dataNascimento").val(formatarDataObjeto(response.dados.dataNascimento));

        $('input[name="sexo"]').filter("[value='" + response.dados.sexo + "']").click();

    }
    ;

    var URL = '/GerarInscricao';
    var idConsultar = getUrlParameter("id");

    function prepararAtualizar() {

        //delay(100);

        //atualizando ao invés de cadastrar
        //o operador != é importante
        if (idConsultar != null) {

            $.ajax({
                type: 'POST',
                url: '/ConsultarInscricao',
                data: {id: idConsultar},
                dataType: 'json',
                success: function (response) {

                    preencherFormulario(response);
                    URL = '/AtualizarInscricao?id=' + idConsultar;

                }
            });

        }

    }
    ;


    function prepararVisualizar() {

        URL = '';
        //remover botão de envio
        $("#submit").html('');

        var visualizar = getUrlParameter("visualizar");

        //visualizando ao invés de atualizar
        //o operador != é importante
        var URLAjax = '';
        if (visualizar === "inscricao") {
            URLAjax = '/ConsultarInscricao';
        } else if (visualizar === "aluno") {
            URLAjax = '/ConsultarAluno';
        }

        $.ajax({
            type: 'POST',
            url: URLAjax,
            data: {id: idConsultar},
            dataType: 'json',
            success: function (response) {
                preencherFormulario(response);

                disableForm($("dados"));
                disableForm($("contatos"));
                disableForm($("documentos"));
                disableForm($("endereco"));

                $('#bairro').prop('disabled', 'disabled');

                $("#adicionarContatos").html('');
                $("#adicionarDocumentos").html('');

                for (var i = 0; i < contadorDoc; i++) {
                    $("#delDocumento" + (i + 1)).hide();
                }

                for (var i = 0; i < contadorCon; i++) {
                    $("#delContato" + (i + 1)).hide();
                }

            }
        });

    }
    ;



    $("#submit").click(function () {

        if (!verificarForm("dados")) {
            return;
        }
        if (!verificarForm("documentos")) {
            return;
        }
        if (!verificarForm("contatos")) {
            return;
        }
        if (!verificarForm("endereco")) {
            return;
        }


        //para evitar duplicatas
        var btn = document.getElementById("btnSubmit");
        btn.disabled = true;

        $.ajax({
            type: 'POST',
            url: URL,
            data: {dados: getFormData($("#dados")),
                endereco: getFormData($("#endereco")),
                contatos: getContatos(),
                documentos: getDocumentos()},
            success: function (response) {
                //redirecionar apenas após sucesso
                if (response === "SME") {
                    window.location = '/SME/inscricao/lista-inscricoes.html';
                } else if (response === "UE") {
                    window.location = '/UE/inscricao/lista-inscricoes.html';
                }
                return;
            },
            error: function (response) {
                //reativar botão se houve falha
                btn.disabled = false;
            }
        });

    });


    var atualizar = getUrlParameter("atualizar");
    if (atualizar != null) {
        prepararAtualizar();
    }

    var visualizar = getUrlParameter("visualizar");
    if (visualizar != null) {
        prepararVisualizar();
    }

});