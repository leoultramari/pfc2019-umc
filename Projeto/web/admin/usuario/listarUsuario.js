$.ajax({
    type: 'POST',
    url: '/ListarUsuario',
    dataType: 'json',
    success: function (response) {
        //inscricoes = response;
        usuariosHTML = '';
        var num = 0;
        $.each(response, function (key, value) {
            num++;
            usuariosHTML += '<tr id='+num+'>';
            usuariosHTML += '<td id="id">' + value.id + '</td>';
            usuariosHTML += '<td>' + value.login + '</td>';
            usuariosHTML += '<td>' + value.cargo.nome + '</td>';
            if (value.escola) {
                usuariosHTML += '<td>' + value.escola.nome + '</td>';
            } else {
                usuariosHTML += '<td></td>';
            }
            usuariosHTML += '<td>';
            usuariosHTML += '<button class="btn btn-primary btn-sm" onclick=atualizarUsuario(' + value.id + ') >Editar</button>';           
            usuariosHTML += '<button class="btn btn-warning btn-sm" onclick=removerUsuario(' + value.id + ') >Remover</button>';          
            usuariosHTML += '</td>';

            usuariosHTML += '</tr>';
        });
        $("#lista").html(usuariosHTML);
        
        document.getElementById("loader").style.display = "none";
    }
});

function atualizarUsuario(id){
    window.location = '/admin/usuario/cadastrar-usuario.html?id=' + id;
}

function removerUsuario(id){    
    $.ajax({
        type: 'POST',
        url: '/RemoverUsuario',
        data:{id:id},
        success: function (response) {
            location.reload();
        }
    });
    
};
