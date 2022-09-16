$.ajax({
    type: 'POST',
    url: '/ListarAlunoNaoAlocado',
    dataType: 'json',
    success: function (response) {

        //inscricoes = response;
        alunosHTML = '';

        $.each(response, function (key, value) {
            alunosHTML += '<tr>';
            alunosHTML += '<td>' + value.rm + '</td>';
            alunosHTML += '<td>' + value.dados.nome + '</td>';
            alunosHTML += '<td>' + value.dados.sexo + '</td>';
            alunosHTML += '<td>' + formatarData(value.dados.dataNascimento) + '</td>';
            alunosHTML += '<td>' + value.dados.filiacao1 + '</td>';
            alunosHTML += '<td>' + value.dados.filiacao2 + '</td>';

            alunosHTML += '<td>';
            alunosHTML += '<button class="btn btn-info btn-sm" onclick=visualizarAluno(' + value.id + ') >Detalhes</button>';
            alunosHTML += '<button class="btn btn-warning btn-sm" onclick=removerAluno(' + value.id + ') >Cancelar</button>';          
            //alunosHTML += '<button class="btn btn-warning btn-sm">Editar</button>';
            alunosHTML += '</td>';

            alunosHTML += '</tr>';
        });
        $("#lista").html(alunosHTML);
        
        document.getElementById("loader").style.display = "none";

    }
});


function removerAluno(id){    
    $.ajax({
        type: 'POST',
        url: '/RemoverAluno',
        data:{id:id},
        //dataType: 'json',
        success: function (response) {
            location.reload();
        }
    });
    
};


$("#alocar").click(function () {
    
    //para evitar duplicatas
    var btn = document.getElementById("btnAlocar");
    btn.disabled = true;
    
    $.ajax({
        type: 'POST',
        url: '/AlocacaoAlunosManual',
        success: function (response) {
            location.reload();
        },
        error: function (response) {
            //reativar botão se houve falha
            btn.disabled = false;
        }
    });
    
});