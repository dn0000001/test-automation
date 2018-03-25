//
// Clear the value property which can store the user's input
//
element = arguments[0];
try {
	element.value = "";
	return true;
} catch (e) {
	return false;
}