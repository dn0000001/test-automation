var url = arguments[0];
var xmlhttp = new XMLHttpRequest();
xmlhttp.open('GET', url, false);
xmlhttp.send();
return xmlhttp.responseText;
