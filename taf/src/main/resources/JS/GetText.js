//
// IE may not support the textContent property as such use innerText
//
element = arguments[0];
try {
	if (element.textContent == undefined)
		return element.innerText;
	else
		return element.textContent;
} catch (e) {
	return null;
}