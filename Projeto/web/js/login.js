/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$("#submit").click(function () {

    if (!verificarForm("formulario")) {
        return;
    }

    //para evitar duplicatas
    var btn = document.getElementById("btnSubmit");
    btn.disabled = true;

    var dadosLogin = {};//getFormData($("#formulario"));
    dadosLogin.usuario = $("#usuario").val();
    //criptografar com SHA256 e converter para base64
    var hash = CryptoJS.SHA256($("#senha").val());
    dadosLogin.senha = hash.toString(CryptoJS.enc.Base64);
    //dadosLogin.senha = btoa(hash.toString());
    
    //dadosLogin.salt = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
    //dadosLogin.senha = dadosLogin.salt + $("#senha").val();

    $.ajax({
        type: 'POST',
        url: '/RealizarLogin',
        data: {dados: JSON.stringify(dadosLogin)},
        success: function (response) {
            //redirecionar
            window.location = '/index.html';
        },
        error: function (response) {
            //reativar botão se houve falha
            btn.disabled = false;
        }
    });

});