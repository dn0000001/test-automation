//
// Construct the JavaScript to do following:
// 1) Suppress any alert or confirm that might appear
// 2) Click the element
// 3) Reverse changes such that alerts appear again
//
// NOTE: In IE6, overriding the alert method is not allowed/applied as such it does not work.
//

function _dn_suppress(bAccept){
	if(bAccept)
	{
		window.confirm = function() { return true; };
		window.alert = function() { return true; };
	}
	else
	{
		window.confirm = function() { return false; };
		window.alert = function() { return false; };
	}
}

function _dn_unsuppress(){
	window.confirm = oldConfirm;
	window.alert = oldAlert;
}

var oldConfirm = window.confirm;
var oldAlert = window.alert;
try
{
	_dn_suppress(arguments[1]);
	arguments[0].click();
}
finally
{
	_dn_unsuppress();
}