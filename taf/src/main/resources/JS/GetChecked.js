//
// Return the checked property value to determine is element is selected
//
element = arguments[0];
try {
	if (element.checked == undefined)
		return false;
	else
		return element.checked;
} catch (e) {
	return false;
}