//
// Return the value property which can store the user's input
//
element = arguments[0];
try {
	if (element.value == undefined)
		return "";
	else
		return element.value;
} catch (e) {
	return "";
}