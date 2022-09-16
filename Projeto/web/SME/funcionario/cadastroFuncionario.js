var URL = '/CadastrarFuncionario';

function prepararAtualizar() {

    //delay(100);

    var idAtualizar = getUrlParameter("id");

    //atualizando ao invés de cadastrar
    if (idAtualizar !== null) {

        $.ajax({
            type: 'POST',
            url: '/ConsultarFuncionario',
            data: {id: idAtualizar},
            dataType: 'json',
            success: function (response) {

                fillFormData(response, $("formulario"));

                $("#escola").val(response.escola.id).change();

                $("#dataInicio").val(formatarDataObjeto(response.dataInicio));

                URL = '/AtualizarFuncionario?id=' + idAtualizar;

            }
        });

    }

}
;

prepararAtualizar();

$("#submit").click(function () {

    if (!verificarForm("formulario")) {
        return;
    }

    //para evitar duplicatas
    var btn = document.getElementById("btnSubmit");
    btn.disabled = true;

    $.ajax({
        type: 'POST',
        url: URL,
        data: {dados: getFormData($("#formulario"))},
        success: function (response) {
            //redirecionar
            window.location = '/SME/funcionario/lista-funcionarios.html';
        },
        error: function (response) {
            //reativar botão se houve falha
            btn.disabled = false;
        }
    });

});