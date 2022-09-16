//var inscricoes = {};

$.ajax({
    type: 'POST',
    url: '/ListarInscricaoEnviada',
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
            //toda inscrição presente nessa página ainda não foi enviada
            //essa verificação juntamente com a data de envio deve ser removida
            if (value.dataEnviada === undefined) {
                inscricoesHTML += '<button class="btn btn-primary btn-sm" onclick=atualizarInscricao(' + value.id + ') >Editar</button>';
                inscricoesHTML += '<button class="btn btn-warning btn-sm" onclick=cancelarInscricao(' + value.id + ') >Cancelar</button>';
            }
            inscricoesHTML += '</td>';

            inscricoesHTML += '</tr>';
        });
        $("#lista").html(inscricoesHTML);

        document.getElementById("loader").style.display = "none";
    }
});

$("#submit").click(function () {

    //para evitar duplicatas
    var btn = document.getElementById("btnSubmit");
    btn.disabled = true;

    $.ajax({
        type: 'POST',
        url: '/EnvioManualInscricoesUE',
        //dataType: 'json',
        //data: {},
        success: function (response) {
            //recarrega a página
            location.reload();
        },
        error: function (response) {
            //reativar botão se houve falha
            btn.disabled = false;
        }
    });

});