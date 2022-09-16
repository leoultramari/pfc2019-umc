$.ajax({
    type: 'POST',
    url: '/ListarTurmaCompleta',
    dataType: 'json',
    success: function (response) {

        //inscricoes = response;
        turmasHTML = '';

        $.each(response, function (key, value) {
            turmasHTML += '<tr>';
            turmasHTML += '<td>' + value.serie + '-' + value.classe + '</td>';
            turmasHTML += '<td>' + value.ano + '</td>';
            turmasHTML += '<td>' + value.tamanho + '</td>';

            //turmasHTML += '<td>';
            //turmasHTML += '<button class="btn btn-info btn-sm" onclick=visualizarTurma(' + value.id + ') >Detalhes</button>';         
            ////alunosHTML += '<button class="btn btn-warning btn-sm">Editar</button>';
            //turmasHTML += '</td>';

            turmasHTML += '</tr>';
        });
        $("#lista").html(turmasHTML);
        
        document.getElementById("loader").style.display = "none";

    }
});