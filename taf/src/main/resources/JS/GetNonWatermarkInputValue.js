//
// If values are the same, then return the empty string as the watermark is
// being displayed else return the input value
//
element = arguments[0];
try {
	if (element.value == element.getAttribute('value'))
		return "";
	else
		return element.getAttribute('value');
} catch (e) {
	return null;
}