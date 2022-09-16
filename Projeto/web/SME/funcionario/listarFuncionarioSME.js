//var inscricoes = {};

$.ajax({
    type: 'POST',
    url: '/ListarFuncionario',
    dataType: 'json',
    success: function (response) {

        HTML = '';

        $.each(response, function (key, value) {
            HTML += '<tr>';
            HTML += '<td>' + value.registro + '</td>';
            HTML += '<td>' + value.nome + '</td>';
            HTML += '<td>' + value.email + '</td>';
            HTML += '<td>' + value.telefone + '</td>';
            
            HTML += '<td>' + formatarData(value.dataInicio) + '</td>';
            //HTML += '<td>' + formatarData(value.dataSaida) + '</td>';
            
            HTML += '<td>' + value.escola.nome + '</td>';

            HTML += '<td>' + formatarData(value.dataAtualizado) + '</td>';
            HTML += '<td>' + formatarData(value.dataEnviado) + '</td>';

            HTML += '<td>';
            
            //if(value.dataSaida === undefined){
                HTML += '<button class="btn btn-primary btn-sm" onclick=atualizarFuncionario(' + value.id + ') >Editar</button>';
                HTML += '<button class="btn btn-warning btn-sm" onclick=removerFuncionario(' + value.id + ') >Remover</button>';          
            //}
            HTML += '</td>';

            HTML += '</tr>';
        });
        $("#lista").html(HTML);
        
        document.getElementById("loader").style.display = "none";

    }
});

function atualizarFuncionario(id){
    window.location = '/SME/funcionario/cadastrar-funcionario.html?id=' + id;
}

function removerFuncionario(id){    
    $.ajax({
        type: 'POST',
        url: '/RemoverFuncionario',
        data:{id:id},
        success: function (response) {
            location.reload();
        }
    });
    
};

$("#submit").click(function () {
    $.ajax({
        type: 'POST',
        url: '/EnvioManualFuncionarios',
        success: function (response) {
            location.reload();
        }
    });
    
});