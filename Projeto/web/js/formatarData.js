function formatarData(data){
    
    if (data === undefined){
        return "--/--/----";
    }
    
    var day = data.day;
    var month = data.month;
    var year = data.year;
    
    return day + "/" + month + "/" + year;
    
};

function formatarDataObjeto(data){
    
    if (data === undefined){
        return "";
    }
    
    var day = data.day;
    if (data.day < 10){
        day = "0" + data.day;
    }
    var month = data.month;
    if (data.month < 10){
        month = "0" + data.month;
    }
    var year = data.year;
    
    return year + "-" + month + "-" + day;
    
}