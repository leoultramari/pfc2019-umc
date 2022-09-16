//adicionando validação
function verificarForm(formId) {

    var form = document.getElementById(formId);

    //if (!$form.checkValidity()) {
    if (!form.reportValidity()) {
        // Cria um botão temporário e clica-o para disparar a validação do navegador
        const tmpSubmit = document.createElement('button');
        form.appendChild(tmpSubmit);
        tmpSubmit.click();
        form.removeChild(tmpSubmit);
        return false;
    } else {
        return true;
    }
}
;

//usado para gerar JSON a partir de forms HTML
function getFormData($form) {
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function (n, i) {
        /*if($(unindexed_array).parent("#contato").length === 1){
         indexed_array[n['contato']] = indexed_array[n['name']] = n['value'];
         } else {*/
        indexed_array[n['name']] = n['value'];
        /*}*/

    });

    //return indexed_array;
    return JSON.stringify(indexed_array);
}
;

//preenche uma form HTML com JSON
function fillFormData(data, form) {
    var inputs = Array.prototype.slice.call(document.querySelectorAll('form input'));
    //var inputs = Array.prototype.slice.call(form);

    Object.keys(data).map(function (dataItem) {
        inputs.map(function (inputItem) {
            return (inputItem.name === dataItem) ? (inputItem.value = data[dataItem]) : false;
        });
    });
}
;

//Busca parametros enviados via GET
//var getUrlParameter = function getUrlParameter(sParam) {
function getUrlParameter(sParam) {
    var sPageURL = window.location.search.substring(1),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
}
;

//desativa uma form HTML
function disableForm(form) {
    var inputs = Array.prototype.slice.call(document.querySelectorAll('form input'));

    inputs.map(function (inputItem) {
        inputItem.disabled = true;
    });
}
;