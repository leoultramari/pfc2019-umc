//var inscricoes = {};
function listar() {

    var btn = document.getElementById("btnMatricular");
    btn.disabled = true;

    $.ajax({
        type: 'POST',
        url: '/ListarInscricaoRecebida',
        dataType: 'json',
        success: function (response) {

            //inscricoes = response;
            inscricoesHTML = '';

            $.each(response, function (key, value) {
                inscricoesHTML += '<tr>';
                inscricoesHTML += '<td>' + value.dados.nome + '</td>';
                inscricoesHTML += '<td>' + value.dados.endereco.bairro.nome + '</td>';
                inscricoesHTML += '<td>' + formatarData(value.dataRecebida) + '</td>';
                inscricoesHTML += '<td>' + value.escolaOriginal.nome + '</td>';
                inscricoesHTML += '<td>' + ((value.escolaAlocada !== undefined) ? value.escolaAlocada.nome : "---") + '</td>';
                inscricoesHTML += '<td>' + formatarData(value.dataEnviada) + '</td>';

                inscricoesHTML += '<td>';
                inscricoesHTML += '<button class="btn btn-info btn-sm" onclick=visualizarInscricao(' + value.id + ') >Detalhes</button>';

                //Podemos manipular apenas as inscrições destinadas a nossa UE
                //console.log(value.escolaAlocada);
                //console.log(value.escolaAlocada.id);
                //console.log(dadosNavbar.id);
                if (value.escolaAlocada !== undefined && value.escolaAlocada.id === dadosNavbar.id) {
                    inscricoesHTML += '<button class="btn btn-primary btn-sm" onclick=matricularInscricao(' + value.id + ') >Matricular</button>';
                    inscricoesHTML += '<button class="btn btn-warning btn-sm" onclick=cancelarInscricao(' + value.id + ') >Cancelar</button>';
                    btn.disabled = false;
                } else {
                    inscricoesHTML += '<button class="btn btn-disabled btn-sm">Matricular</button>';
                    inscricoesHTML += '<button class="btn btn-disabled btn-sm">Cancelar</button>';
                }
                inscricoesHTML += '</td>';

                inscricoesHTML += '</tr>';
            });
            $("#lista").html(inscricoesHTML);

            document.getElementById("loader").style.display = "none";

        }
    });

}
;

function aguardarDadosNavbar() {
    if (typeof (dadosNavbar) == "undefined") {
        setTimeout(aguardarDadosNavbar, 0.5 * 1000); //0.5 segundos
    } else if (dadosNavbar.id == undefined) {
        setTimeout(aguardarDadosNavbar, 0.5 * 1000); //0.5 segundos
    } else {
        listar();
    }
}
;

$("#matricular").click(function () {

    //para evitar duplicatas
    var btn = document.getElementById("btnMatricular");
    btn.disabled = true;

    $.ajax({
        type: 'POST',
        url: '/RealizarMatriculaTodos',
        success: function (response) {
            location.reload();
        },
        error: function (response) {
            //reativar botão se houve falha
            btn.disabled = false;
        }
    });

});

aguardarDadosNavbar();