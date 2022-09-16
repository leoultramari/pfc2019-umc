$.ajax({
    type: 'POST',
    url: '/ListarEscola',
    dataType: 'json',
    success: function (response) {
        var listaEscolas = '';
        $.each(response, function (key, value) {
            listaEscolas += '<option value=' + value.id + '>' + value.nome + '</option>';
        });
        $("#escola").append(listaEscolas);
        ;
    }
});