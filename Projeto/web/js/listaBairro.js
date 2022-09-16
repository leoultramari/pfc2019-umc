$.ajax({
    type: 'POST',
    url: '/ListarBairro',
    dataType: 'json',
    success: function (response) {
        var listitems = '';
        $.each(response, function (key, value) {
            listitems += '<option value=' + value.id + '>' + value.nome + '</option>';
        });
        $("#bairro").append(listitems);
    }
});