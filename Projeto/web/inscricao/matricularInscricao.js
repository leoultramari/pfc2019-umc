function matricularInscricao(id){    
    $.ajax({
        type: 'POST',
        url: '/RealizarMatricula',
        data:{id:id},
        success: function (response) {
            location.reload();
        }
    });
    
};