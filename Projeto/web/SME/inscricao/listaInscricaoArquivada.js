$.ajax({
    type: 'POST',
    url: '/ListarInscricaoArquivada',
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
            inscricoesHTML += '<td>' + value.statusMatricula + '</td>';

            inscricoesHTML += '<td>';
            inscricoesHTML += '<button class="btn btn-info btn-sm" onclick=visualizarInscricao(' + value.id + ') >Detalhes</button>';
            //inscricoesHTML += '<button class="btn btn-secondary btn-sm" onclick=atualizarInscricaoSME(' + value.id + ') >Editar</button>';
            //inscricoesHTML += '<button class="btn btn-warning btn-sm" onclick=cancelarInscricao(' + value.id + ') >Cancelar</button>';          
            inscricoesHTML += '</td>';

            inscricoesHTML += '</tr>';
        });
        $("#lista").html(inscricoesHTML);

        document.getElementById("loader").style.display = "none";
    }
});