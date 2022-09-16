$.ajax({
    type: 'POST',
    url: '/ListarCargo',
    dataType: 'json',
    success: function (response) {
        var listaCargos = '';
        $.each(response, function (key, value) {
            listaCargos += '<option value=' + value.id + '>' + value.nome + '</option>';
        });
        $("#cargo").append(listaCargos);
        ;
    }
});
