$.ajax({
    type: 'POST',
    url: '/ListarFuncionario',
    dataType: 'json',
    success: function (response) {
        HTML = '';
        var num = 0;
        $.each(response, function (key, value) {
            num++;
            HTML += '<tr id='+num+'>';
            HTML += '<td>' + value.registro + '</td>';
            HTML += '<td>' + value.nome + '</td>';
            HTML += '<td>' + value.email + '</td>';
            HTML += '<td>' + value.telefone + '</td>';
            HTML += '<td>' + formatarData(value.dataAtualizado) + '</td>';
            HTML += '<td>';
            //usuariosHTML += '<button class="btn btn-primary btn-sm" id="editar" onclick="detalharUsuario('+num+')">\n\
            //                    Detalhes\n\
            //                </button>';
            HTML += '</td>';

            HTML += '</tr>';
        });
        $("#lista").html(HTML);
        
        document.getElementById("loader").style.display = "none";
    }
});
