//
// Generic way to check if an KO function is ready. Returns true if DOM ready else false
//
elementID = arguments[0];
checkMethod = arguments[1];

if (ko == null || typeof (ko) != 'object')
	return false;

element = document.getElementById(elementID);
if (element == null)
	return false;

model = ko.dataFor(element);
if (model == null)
	return false;

check = eval('model.' + checkMethod);
if (typeof (check) == 'function')
	return true;
else
	return false;