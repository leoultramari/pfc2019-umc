$.ajax({
    type: 'POST',
    url: '/ListarEscolaStatus',
    dataType: 'json',
    success: function (response) {
        //inscricoes = response;
        escolasHTML = '';

        $.each(response, function (key, value) {
            escolasHTML += '<tr>';
            escolasHTML += '<td>' + value.id + '</td>';
            escolasHTML += '<td>' + value.nome + '</td>';
            escolasHTML += '<td>' + value.status + '</td>';
            //escolasHTML += '<td>';
            //escolasHTML += '<button class="btn btn-primary btn-sm" id="editar" onclick="editar(' + value.id + ')">Editar</button>';
            //escolasHTML += '</td>';
            escolasHTML += '</tr>';
        });
        $("#lista").html(escolasHTML);
        
        document.getElementById("loader").style.display = "none";
    }
});

function editar(id){
    window.location.replace("/SME/escola/escola.html?" + id);
}
