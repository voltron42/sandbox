function FormatString(str,obj){
    var newStr = str;
    for (var key in obj) {
        newStr = newStr.replace('{' + key + '}', obj[key]);
    }
    return newStr;
}
