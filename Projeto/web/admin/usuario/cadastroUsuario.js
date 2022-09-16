//$(document).ready(function () {

var URL = '/CadastrarUsuario';

function prepararAtualizar() {

    //delay(100);

    var idAtualizar = getUrlParameter("id");//jQuery.url.param("id");

    //atualizando ao invés de cadastrar
    //o operador != é importante
    if (idAtualizar != null) {

        $.ajax({
            type: 'POST',
            url: '/ConsultarUsuario',
            data: {id: idAtualizar},
            dataType: 'json',
            success: function (response) {

                fillFormData(response, $("formulario"));

                $("#cargo").val(response.cargo.id).change();
                if (response.escola != undefined) {
                    $("#escola").val(response.escola.id).change();
                }

                //alert("buscou inscricao " + idAtualizar);
                URL = '/AtualizarUsuario?id=' + idAtualizar;

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

    //confirmar senha
    if ($("#senha").val() != $("#senha2").val()) {
        $("#senha").val("");
        $("#senha2").val("");
        alert("As senhas não são iguais.");
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
            //redirecionar apenas após sucesso
            window.location = '/admin/usuario/lista-usuarios.html';
            return;
        },
        error: function (response) {
            //reativar botão se houve falha
            btn.disabled = false;
        }
    });

});

//});