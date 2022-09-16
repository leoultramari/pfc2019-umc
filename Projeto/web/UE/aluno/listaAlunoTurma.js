
function atualizar() {
    
    var selecao = document.getElementById("turma").options[document.getElementById("turma").selectedIndex];
    console.log(selecao.value);
    
    $.ajax({
        type: 'POST',
        url: '/ListarAlunoTurma',
        data: {id: selecao.value},
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
}
;

function removerAluno(id) {
    $.ajax({
        type: 'POST',
        url: '/RemoverAluno',
        data: {id: id},
        //dataType: 'json',
        success: function (response) {
            location.reload();
        }
    });

}
;

$.ajax({
    type: 'POST',
    url: '/ListarTurma',
    dataType: 'json',
    success: function (response) {
        var listitems = '';
        $.each(response, function (key, value) {
            listitems += '<option value=' + value.id + '>' + value.ano + ' ' + value.serie + '-' + value.classe + '</option>';
        });
        $("#turma").append(listitems);
    }
});