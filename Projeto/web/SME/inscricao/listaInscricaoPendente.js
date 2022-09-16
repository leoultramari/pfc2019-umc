$.ajax({
    type: 'POST',
    url: '/ListarInscricaoPendente',
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
            //não é possível editar os dados de uma inscrição que já foi enviada
            if (value.dataEnviada === undefined) {
                inscricoesHTML += '<button class="btn btn-primary btn-sm" onclick=atualizarInscricao(' + value.id + ') >Editar</button>';
                inscricoesHTML += '<button class="btn btn-warning btn-sm" onclick=cancelarInscricao(' + value.id + ') >Cancelar</button>';
            } else {
                inscricoesHTML += '<button class="btn btn-disabled btn-sm">Editar</button>';
                inscricoesHTML += '<button class="btn btn-disabled btn-sm">Cancelar</button>';          
            }
            inscricoesHTML += '</td>';

            inscricoesHTML += '</tr>';
        });
        $("#lista").html(inscricoesHTML);
        
        document.getElementById("loader").style.display = "none";

    }
});

$("#alocar").click(function () {
    
    //para evitar duplicatas
    var btn = document.getElementById("btnAlocar");
    btn.disabled = true;
    
    $.ajax({
        type: 'POST',
        url: '/AlocacaoInscricoesManual',
        success: function (response) {
            location.reload();
        },
        error: function (response) {
            //reativar botão se houve falha
            btn.disabled = false;
        }
    });
    
});

$("#submit").click(function () {
    
    //para evitar duplicatas
    var btn = document.getElementById("btnSubmit");
    btn.disabled = true;
    
    $.ajax({
        type: 'POST',
        url: '/EnvioManualInscricoesSME',
        success: function (response) {
            location.reload();
        },
        error: function (response) {
            //reativar botão se houve falha
            btn.disabled = false;
        }
    });
    
});

$("#arquivar").click(function () {
    
    //para evitar duplicatas
    var btn = document.getElementById("btnArquivar");
    btn.disabled = true;
    
    $.ajax({
        type: 'POST',
        url: '/ArquivamentoManual',
        success: function (response) {
            location.reload();
        },
        error: function (response) {
            //reativar botão se houve falha
            btn.disabled = false;
        }
    });
    
});