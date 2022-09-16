function cancelarInscricao(id){    
    $.ajax({
        type: 'POST',
        url: '/CancelarInscricao',
        data:{id:id},
        success: function (response) {
            location.reload();
        }
    });
};