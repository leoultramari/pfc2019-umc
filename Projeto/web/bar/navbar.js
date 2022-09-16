//Redirects enviados por servlets não funcionam com requests AJAX
//buscar código de erro na resposta da servlet e realizar o redirecionamento localmente
//$(document).ajaxError(function (e, xhr, settings) {
//    if (xhr.status === 401) {
//        window.location = '/login.html';
//    }
//    if (xhr.status === 403) {
//        window.location = '/index.html';
//    }
//});

var dadosNavbar = {};

$(document).ready(function () {

    function gerarItens(data) {

        itensHTML = '';

        $.each(data, function (key, value) {
            itensHTML += '<div class="btn-group" id="itensJSON">';
            itensHTML += '<button class="btn btn-dark dropdown-toggle" data-toggle="dropdown">';
            itensHTML += value.nome;
            itensHTML += '</button>';
            itensHTML += '<div class="dropdown-menu">';
            $.each(value.itens, function (key, value) {
                itensHTML += '<a class="dropdown-item" href="';
                itensHTML += value.URL;
                itensHTML += '">';
                itensHTML += value.nome;
                itensHTML += '</a>';
            });
            itensHTML += '</div>';
            itensHTML += '</div>';
        });

        $("#itens").html(itensHTML);

    }

    function autenticar(data) {
        //if(data === null || data === {} || data.login === null || data.login === '' || data.login === undefined){
        if (data.login === undefined) {
            window.stop();
            window.location = '/login.html';
            //alert('auth failed');
        }
    }

    function atualizar() {
        $.ajax({
            url: '/PreencherNavbar',
            success: function (data) {
                //alert(data.login);
                autenticar(data);
                $('#login').text(data.login);
                $('#contexto').text(data.contexto);
                gerarItens(data.menus);

                if (data.erro !== undefined) {
                    erroHTML = '<a href="#" class="btn btn-dark">' + data.erro + '</a>';
                    $("#status").html(erroHTML);
                }
                
                data.menus = null;
                dadosNavbar = data;
                
            },
            complete: function () {
                //Agendar a próxima atualização apenas depois de terminar essa
                setTimeout(atualizar, 300 * 1000); //300 segundos
            }
        });
    }
    setTimeout(atualizar, 1);
});